/*
 * Copyright 2017 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package stroom.pipeline.filter;

import net.sf.saxon.Configuration;
import net.sf.saxon.jaxp.TemplatesImpl;
import net.sf.saxon.jaxp.TransformerImpl;
import net.sf.saxon.s9api.XsltExecutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import stroom.cache.StoredXsltExecutable;
import stroom.cache.XsltPool;
import stroom.docref.DocRef;
import stroom.pipeline.LocationFactoryProxy;
import stroom.pipeline.SupportsCodeInjection;
import stroom.pipeline.XsltStore;
import stroom.pipeline.errorhandler.ErrorListenerAdaptor;
import stroom.pipeline.errorhandler.ErrorReceiver;
import stroom.pipeline.errorhandler.ErrorReceiverIdDecorator;
import stroom.pipeline.errorhandler.ErrorReceiverProxy;
import stroom.pipeline.errorhandler.LoggedException;
import stroom.pipeline.errorhandler.ProcessException;
import stroom.pipeline.errorhandler.StoredErrorReceiver;
import stroom.pipeline.factory.ConfigurableElement;
import stroom.pipeline.factory.PipelineProperty;
import stroom.pipeline.factory.PipelinePropertyDocRef;
import stroom.pipeline.shared.ElementIcons;
import stroom.pipeline.shared.XsltDoc;
import stroom.pipeline.shared.data.PipelineElementType;
import stroom.pipeline.shared.data.PipelineElementType.Category;
import stroom.pipeline.shared.data.PipelineReference;
import stroom.pipeline.state.PipelineContext;
import stroom.pipeline.writer.PathCreator;
import stroom.pool.PoolItem;
import stroom.util.CharBuffer;
import stroom.util.shared.Location;
import stroom.util.shared.Severity;

import javax.inject.Inject;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.TransformerHandler;
import java.util.ArrayList;
import java.util.List;

/**
 * An XML filter for performing inline XSLT transformation of XML.
 */
@ConfigurableElement(type = "XSLTFilter", category = Category.FILTER, roles = {PipelineElementType.ROLE_TARGET,
        PipelineElementType.ROLE_HAS_TARGETS, PipelineElementType.VISABILITY_SIMPLE,
        PipelineElementType.VISABILITY_STEPPING, PipelineElementType.ROLE_MUTATOR,
        PipelineElementType.ROLE_HAS_CODE}, icon = ElementIcons.XSLT)
public class XsltFilter extends AbstractXMLFilter implements SupportsCodeInjection {
    private static final Logger LOGGER = LoggerFactory.getLogger(XsltFilter.class);

    private final XsltPool xsltPool;
    private final ErrorReceiverProxy errorReceiverProxy;
    private final XsltStore xsltStore;
    private final XsltConfig xsltConfig;
    private final LocationFactoryProxy locationFactory;
    private final PipelineContext pipelineContext;
    private final PathCreator pathCreator;

    private ErrorListener errorListener;

    private boolean suppressXSLTNotFoundWarnings;
    private DocRef xsltRef;
    private String xsltNamePattern;

    /**
     * We only need a single transformer factory here as it actually doesn't do
     * much internally when creating a transformer handler.
     */
    private PoolItem<StoredXsltExecutable> poolItem;
    private XsltExecutable xsltExecutable;
    private TransformerHandler handler;
    private Locator locator;
    private boolean xsltRequired = false;
    private boolean passThrough = true;
    private String injectedCode;
    private boolean usePool = true;
    private List<PipelineReference> pipelineReferences;

    private int elementCount;
    private int maxElementCount;

    @Inject
    public XsltFilter(final XsltPool xsltPool,
                      final ErrorReceiverProxy errorReceiverProxy,
                      final XsltStore xsltStore,
                      final XsltConfig xsltConfig,
                      final LocationFactoryProxy locationFactory,
                      final PipelineContext pipelineContext,
                      final PathCreator pathCreator) {
        this.xsltPool = xsltPool;
        this.errorReceiverProxy = errorReceiverProxy;
        this.xsltStore = xsltStore;
        this.xsltConfig = xsltConfig;
        this.locationFactory = locationFactory;
        this.pipelineContext = pipelineContext;
        this.pathCreator = pathCreator;
    }

    @Override
    public void startProcessing() {
        try {
            errorListener = new ErrorListenerAdaptor(getElementId(), locationFactory, errorReceiverProxy);
            maxElementCount = xsltConfig.getMaxElements();
            XsltDoc xslt = null;

            // Load XSLT from a name pattern if one has been specified.
            if (xsltNamePattern != null && xsltNamePattern.trim().length() > 0) {
                // Resolve replacement variables.
                final String resolvedName = pathCreator.replaceContextVars(xsltNamePattern.trim());
                // Make sure there are no replacement vars left.
                final String[] vars = PathCreator.findVars(resolvedName);
                if (vars.length > 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("XSLT name pattern \"");
                    sb.append(xsltNamePattern);
                    sb.append("\" contains invalid replacement variables (");
                    for (final String var : vars) {
                        sb.append(var);
                        sb.append(", ");
                    }
                    sb.setLength(sb.length() - 2);
                    sb.append(")");
                    throw new ProcessException(sb.toString());
                }

                LOGGER.debug("Finding XSLT with resolved name '{}' from pattern '{}'", resolvedName, xsltNamePattern);
                final List<DocRef> docs = xsltStore.findByName(resolvedName);
                if (docs == null || docs.size() == 0) {
                    if (!suppressXSLTNotFoundWarnings) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("No XSLT found with name '");
                        sb.append(resolvedName);
                        sb.append("' from pattern '");
                        sb.append(xsltNamePattern);

                        if (xsltRef != null) {
                            sb.append("' - using default '");
                            sb.append(xsltRef.getName());
                            sb.append("'");
                        } else {
                            sb.append("' - no default specified");
                        }

                        errorReceiverProxy.log(Severity.WARNING, null, getElementId(), sb.toString(), null);
                    }
                } else {
                    xslt = xsltStore.readDocument(docs.get(0));

                    if (docs.size() > 1) {
                        final String message = "" +
                                "Multiple XSLT found with name '" +
                                resolvedName +
                                "' from pattern '" +
                                xsltNamePattern +
                                "' - using XSLT with uuid (" +
                                xslt.getUuid() +
                                ")";
                        errorReceiverProxy.log(Severity.WARNING, null, getElementId(), message, null);
                    }
                }
            }

            // Load the XSLT from a reference if we haven't found it by name.
            if (xslt == null && xsltRef != null) {
                xslt = xsltStore.readDocument(xsltRef);
                if (xslt == null) {
                    final String message = "" +
                            "XSLT \"" +
                            xsltRef.getName() +
                            "\" appears to have been deleted";
                    throw new ProcessException(message);
                }
            }

            // If we have found XSLT then get a template.
            if (xslt != null) {
                // If we are in stepping mode and have made code changes then we
                // want to add them to the newly loaded XSLT.

                if (injectedCode != null) {
                    xslt.setData(injectedCode);
                    usePool = false;
                }

                // If no XSLT has been provided then don't try and get compiled
                // XSLT for it.
                if (xslt.getData() != null && xslt.getData().trim().length() > 0) {
                    // Get compiled XSLT from the pool.
                    final ErrorReceiver errorReceiver = new ErrorReceiverIdDecorator(getElementId(),
                            errorReceiverProxy);
                    poolItem = xsltPool.borrowConfiguredTemplate(xslt, errorReceiver,
                            locationFactory, pipelineReferences, usePool);
                    final StoredXsltExecutable storedXsltExecutable = poolItem.getValue();
                    // Get the errors.
                    final StoredErrorReceiver storedErrors = storedXsltExecutable.getErrorReceiver();
                    // Get the XSLT executable.
                    xsltExecutable = storedXsltExecutable.getXsltExecutable();

                    if (storedErrors.getTotalErrors() > 0) {
                        // Replay any exceptions that were created when
                        // compiling the XSLT into the pipeline error handler.
                        storedErrors.replay(errorReceiver);
                    }

                    if (xsltExecutable == null) {
                        // If the XSLT has previously failed to compile they
                        // will have stored null in the pool. Throw an exception
                        // to record this.
                        final CharBuffer sb = new CharBuffer(100);
                        sb.append("There is a problem with the XSLT \"");
                        sb.append(xslt.getName());
                        sb.append("\", see previous logs for details");
                        final String msg = sb.toString();
                        throw new ProcessException(msg);
                    }
                }
            }

            if (xsltRequired && xsltExecutable == null && !pipelineContext.isStepping()) {
                passThrough = false;
                final String msg = "XSLT is required but either no XSLT was found or there is an error in the XSLT";
                throw new ProcessException(msg);
            }
        } catch (final RuntimeException e) {
            errorReceiverProxy.log(Severity.FATAL_ERROR, null, getElementId(), e.getMessage(), e);
            // If we aren't stepping then throw an exception to terminate early.
            if (!pipelineContext.isStepping()) {
                throw new LoggedException(e.getMessage(), e);
            }
        } finally {
            super.startProcessing();
        }
    }

    @Override
    public void endProcessing() {
        try {
            if (poolItem != null) {
                xsltPool.returnObject(poolItem, usePool);
                poolItem = null;
            }
        } finally {
            super.endProcessing();
        }
    }

    /**
     * @param locator an object that can return the location of any SAX document
     *                event
     * @see org.xml.sax.Locator
     * @see stroom.pipeline.filter.AbstractXMLFilter#setDocumentLocator(org.xml.sax.Locator)
     */
    @Override
    public void setDocumentLocator(final Locator locator) {
        if (this.locator == null) {
            this.locator = locator;
            super.setDocumentLocator(locator);
        }
    }

    /**
     * @throws org.xml.sax.SAXException any SAX exception, possibly wrapping another exception
     * @see #endDocument
     * @see org.xml.sax.helpers.XMLFilterImpl#startDocument()
     */
    @Override
    public void startDocument() throws SAXException {
        try {
            if (xsltExecutable != null) {
                // Make sure the executable points at the local error handler.
                final Configuration configuration = xsltExecutable.getUnderlyingCompiledStylesheet().getConfiguration();
                configuration.setErrorListener(errorListener);
                configuration.setLineNumbering(!pipelineContext.isStepping());

                // Create a handler to receive all SAX events.
                final TemplatesImpl templates = new TemplatesImpl(xsltExecutable);
                final TransformerImpl transformer = (TransformerImpl) templates.newTransformer();
                transformer.setErrorListener(errorListener);

                handler = transformer.newTransformerHandler();
                handler.setResult(new SAXResult(getFilter()));
                if (locator != null) {
                    handler.setDocumentLocator(locator);
                }
                handler.startDocument();

            } else if (passThrough) {
                super.startDocument();
            }

        } catch (final TransformerConfigurationException | RuntimeException e) {
            final Throwable throwable = unwrapException(e);

            errorReceiverProxy.log(Severity.FATAL_ERROR, getLocation(throwable), getElementId(), throwable.toString(), throwable);
            // If we aren't stepping then throw an exception to terminate early.
            if (!pipelineContext.isStepping()) {
                throw new LoggedException(throwable.getMessage(), throwable);
            }
        }
    }

    /**
     * @throws org.xml.sax.SAXException any SAX exception, possibly wrapping another exception
     * @see #startDocument
     * @see stroom.pipeline.filter.AbstractXMLFilter#endDocument()
     */
    @Override
    public void endDocument() throws SAXException {
        if (handler != null) {
            try {
                handler.endDocument();
            } catch (final RuntimeException e) {
                try {
                    final Throwable throwable = unwrapException(e);

                    errorReceiverProxy.log(Severity.FATAL_ERROR, getLocation(throwable), getElementId(), throwable.toString(), throwable);
                    // If we aren't stepping then throw an exception to terminate early.
                    if (!pipelineContext.isStepping()) {
                        throw new LoggedException(throwable.getMessage(), throwable);
                    }

                } finally {
                    // We don't want the whole pipeline to terminate processing
                    // if there is a problem with the transform.
                    super.endDocument();
                }
            } finally {
                handler = null;
                elementCount = 0;
            }
        } else if (passThrough) {
            super.endDocument();
        }
    }

    private Location getLocation(final Throwable e) {
        if (e instanceof TransformerException) {
            return locationFactory.create(((TransformerException) e).getLocator());
        }

        return null;
    }

    private Throwable unwrapException(final Throwable e) {
        Throwable cause = e;

        while (cause != null && cause.getCause() != null && cause != cause.getCause()) {
            // Return cause early if it is a TransformerException or ProcessException
            if (cause instanceof TransformerException || cause instanceof ProcessException) {
                return cause;
            }

            cause = cause.getCause();
        }

        return cause;
    }

    /**
     * @param prefix the Namespace prefix being declared. An empty string is used
     *               for the default element namespace, which has no prefix.
     * @param uri    the Namespace URI the prefix is mapped to
     * @throws org.xml.sax.SAXException the client may throw an exception during processing
     * @see #endPrefixMapping
     * @see #startElement
     * @see stroom.pipeline.filter.AbstractXMLFilter#startPrefixMapping(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
        if (handler != null) {
            handler.startPrefixMapping(prefix, uri);
        } else if (passThrough) {
            super.startPrefixMapping(prefix, uri);
        }
    }

    /**
     * the prefix that was being mapped. This is the empty string when a default
     * mapping scope ends.
     *
     * @param prefix the prefix that was being mapped. This is the empty string
     *               when a default mapping scope ends.
     * @throws org.xml.sax.SAXException the client may throw an exception during processing
     * @see #startPrefixMapping
     * @see #endElement
     * @see stroom.pipeline.filter.AbstractXMLFilter#endPrefixMapping(java.lang.String)
     */
    @Override
    public void endPrefixMapping(final String prefix) throws SAXException {
        if (handler != null) {
            handler.endPrefixMapping(prefix);
        } else if (passThrough) {
            super.endPrefixMapping(prefix);
        }
    }

    /**
     * @param uri       the Namespace URI, or the empty string if the element has no
     *                  Namespace URI or if Namespace processing is not being
     *                  performed
     * @param localName the local name (without prefix), or the empty string if
     *                  Namespace processing is not being performed
     * @param qName     the qualified name (with prefix), or the empty string if
     *                  qualified names are not available
     * @param atts      the attributes attached to the element. If there are no
     *                  attributes, it shall be an empty Attributes object. The value
     *                  of this object after startElement returns is undefined
     * @throws org.xml.sax.SAXException any SAX exception, possibly wrapping another exception
     * @see #endElement
     * @see org.xml.sax.Attributes
     * @see org.xml.sax.helpers.AttributesImpl
     * @see stroom.pipeline.filter.AbstractXMLFilter#startElement(java.lang.String,
     * java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {
        if (handler != null) {
            elementCount++;
            if (elementCount > maxElementCount) {
                final String message = "" +
                        "Max element count of " +
                        maxElementCount +
                        " has been exceeded. Please ensure a split filter is present and is configured correctly for this pipeline.";

                final ProcessException exception = new ProcessException(message);
                if (pipelineContext.isStepping()) {
                    errorReceiverProxy.log(Severity.FATAL_ERROR, null, getElementId(), exception.getMessage(),
                            exception);
                } else {
                    errorReceiverProxy.log(Severity.FATAL_ERROR, locationFactory.create(locator), getElementId(),
                            exception.getMessage(), exception);
                }

                // If we aren't stepping then throw an exception to terminate
                // early.
                if (!pipelineContext.isStepping()) {
                    throw new LoggedException(exception.getMessage(), exception);
                }
            }

            handler.startElement(uri, localName, qName, atts);
        } else if (passThrough) {
            super.startElement(uri, localName, qName, atts);
        }
    }

    /**
     * @param uri       the Namespace URI, or the empty string if the element has no
     *                  Namespace URI or if Namespace processing is not being
     *                  performed
     * @param localName the local name (without prefix), or the empty string if
     *                  Namespace processing is not being performed
     * @param qName     the qualified XML name (with prefix), or the empty string if
     *                  qualified names are not available
     * @throws org.xml.sax.SAXException any SAX exception, possibly wrapping another exception
     * @see stroom.pipeline.filter.AbstractXMLFilter#endElement(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if (handler != null) {
            handler.endElement(uri, localName, qName);
        } else if (passThrough) {
            super.endElement(uri, localName, qName);
        }
    }

    /**
     * @param ch     the characters from the XML document
     * @param start  the start position in the array
     * @param length the number of characters to read from the array
     * @throws org.xml.sax.SAXException any SAX exception, possibly wrapping another exception
     * @see #ignorableWhitespace
     * @see org.xml.sax.Locator
     * @see stroom.pipeline.filter.AbstractXMLFilter#characters(char[],
     * int, int)
     */
    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        if (handler != null) {
            handler.characters(ch, start, length);
        } else if (passThrough) {
            super.characters(ch, start, length);
        }
    }

    /**
     * @param ch     the characters from the XML document
     * @param start  the start position in the array
     * @param length the number of characters to read from the array
     * @throws org.xml.sax.SAXException any SAX exception, possibly wrapping another exception
     * @see #characters
     * @see stroom.pipeline.filter.AbstractXMLFilter#ignorableWhitespace(char[],
     * int, int)
     */
    @Override
    public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
        if (handler != null) {
            handler.ignorableWhitespace(ch, start, length);
        } else if (passThrough) {
            super.ignorableWhitespace(ch, start, length);
        }
    }

    /**
     * @param target the processing instruction target
     * @param data   the processing instruction data, or null if none was supplied.
     *               The data does not include any whitespace separating it from
     *               the target
     * @throws org.xml.sax.SAXException any SAX exception, possibly wrapping another exception
     * @see stroom.pipeline.filter.AbstractXMLFilter#processingInstruction(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void processingInstruction(final String target, final String data) throws SAXException {
        if (handler != null) {
            handler.processingInstruction(target, data);
        } else if (passThrough) {
            super.processingInstruction(target, data);
        }
    }

    /**
     * @param name the name of the skipped entity. If it is a parameter entity,
     *             the name will begin with '%', and if it is the external DTD
     *             subset, it will be the string "[dtd]"
     * @throws org.xml.sax.SAXException any SAX exception, possibly wrapping another exception
     * @see stroom.pipeline.filter.AbstractXMLFilter#skippedEntity(java.lang.String)
     */
    @Override
    public void skippedEntity(final String name) throws SAXException {
        if (handler != null) {
            handler.skippedEntity(name);
        } else if (passThrough) {
            super.skippedEntity(name);
        }
    }

    public boolean isXsltRequired() {
        return xsltRequired;
    }

    public void setXsltRequired(final boolean xsltRequired) {
        this.xsltRequired = xsltRequired;
    }

    public List<PipelineReference> getPipelineReferences() {
        return pipelineReferences;
    }

    @PipelineProperty(description = "The XSLT to use.", displayPriority = 1)
    @PipelinePropertyDocRef(types = XsltDoc.DOCUMENT_TYPE)
    public void setXslt(final DocRef xsltRef) {
        this.xsltRef = xsltRef;
    }

    @PipelineProperty(description = "A name pattern to load XSLT dynamically.", displayPriority = 2)
    public void setXsltNamePattern(final String xsltNamePattern) {
        this.xsltNamePattern = xsltNamePattern;
    }

    @PipelineProperty(description = "If XSLT cannot be found to match the name pattern suppress warnings.",
            defaultValue = "false", displayPriority = 3)
    public void setSuppressXSLTNotFoundWarnings(final boolean suppressXSLTNotFoundWarnings) {
        this.suppressXSLTNotFoundWarnings = suppressXSLTNotFoundWarnings;
    }

    @PipelineProperty(description = "A list of places to load reference data from if required.", displayPriority = 5)
    public void setPipelineReference(final PipelineReference pipelineReference) {
        if (pipelineReferences == null) {
            pipelineReferences = new ArrayList<>();
        }

        pipelineReferences.add(pipelineReference);
    }

    @PipelineProperty(
            description = "Advanced: Choose whether or not you want to use cached XSLT templates to improve performance.",
            defaultValue = "true",
            displayPriority = 4)
    public void setUsePool(final boolean usePool) {
        this.usePool = usePool;
    }

    @Override
    public void setInjectedCode(final String injectedCode) {
        this.injectedCode = injectedCode;
    }
}
