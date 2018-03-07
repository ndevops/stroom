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
 */

package stroom.pipeline;

import org.junit.Assert;
import org.junit.Test;
import stroom.entity.shared.DocRefUtil;
import stroom.io.StreamCloser;
import stroom.pipeline.errorhandler.ErrorReceiverProxy;
import stroom.pipeline.errorhandler.LoggingErrorReceiver;
import stroom.pipeline.factory.Pipeline;
import stroom.pipeline.factory.PipelineDataCache;
import stroom.pipeline.factory.PipelineFactory;
import stroom.pipeline.parser.CombinedParser;
import stroom.pipeline.shared.PipelineEntity;
import stroom.pipeline.shared.TextConverter;
import stroom.pipeline.shared.TextConverter.TextConverterType;
import stroom.pipeline.shared.XSLT;
import stroom.pipeline.shared.data.PipelineData;
import stroom.pipeline.shared.data.PipelineDataUtil;
import stroom.pipeline.state.RecordCount;
import stroom.test.AbstractProcessIntegrationTest;
import stroom.test.ComparisonHelper;
import stroom.test.StroomPipelineTestFileUtil;
import stroom.util.io.FileUtil;
import stroom.util.io.StreamUtil;
import stroom.util.shared.Severity;

import javax.inject.Inject;
import java.io.InputStream;
import java.nio.file.Path;

public class TestXMLTransformer extends AbstractProcessIntegrationTest {
    private static final String DIR = "TestXMLTransformer/";

    private static final int NUMBER_OF_RECORDS = 10;

    private static final String REFERENCE = DIR + "TestXMLTransformer.out";

    private static final String INPUT_DEFAULT = DIR + "XML-EVENTS.nxml";
    private static final String INPUT_UTF_8 = DIR + "XML-EVENTS_UTF-8.nxml";
    private static final String INPUT_UTF_8_BOM = DIR + "XML-EVENTS_UTF-8_BOM.nxml";
    private static final String INPUT_UTF_16_LE = DIR + "XML-EVENTS_UTF-16LE.nxml";
    private static final String INPUT_UTF_16_LE_BOM = DIR + "XML-EVENTS_UTF-16LE_BOM.nxml";
    private static final String INPUT_UTF_16_BE = DIR + "XML-EVENTS_UTF-16BE.nxml";
    private static final String INPUT_UTF_16_BE_BOM = DIR + "XML-EVENTS_UTF-16BE_BOM.nxml";
    private static final String INPUT_FRAGMENT = DIR + "XML-EVENTS_fragment.nxml";
    private static final String XSLT_PATH = DIR + "DATA_SPLITTER-EVENTS_no-ref.xsl";
    private static final String FRAGMENT_WRAPPER = DIR + "fragment_wrapper.xml";

    private static final String TRANSFORMER_PIPELINE = DIR + "XMLTransformer.Pipeline.data.xml";
    private static final String FRAGMENT_PIPELINE = DIR + "XMLFragment.Pipeline.data.xml";

    @Inject
    private PipelineFactory pipelineFactory;
    @Inject
    private ErrorReceiverProxy errorReceiver;
    @Inject
    private RecordCount recordCount;
    @Inject
    private XSLTService xsltService;
    @Inject
    private TextConverterService textConverterService;
    @Inject
    private PipelineService pipelineService;
    @Inject
    private PipelineDataCache pipelineDataCache;
    @Inject
    private StreamCloser streamCloser;

    @Test
    public void testDefault() throws Exception {
        testXMLTransformer(INPUT_DEFAULT, null);
    }

    @Test
    public void testUTF8() throws Exception {
        testXMLTransformer(INPUT_UTF_8, "UTF-8");
    }

    @Test
    public void testUTF8BOM() throws Exception {
        testXMLTransformer(INPUT_UTF_8_BOM, "UTF-8");
    }

    @Test
    public void testUTF16LE() throws Exception {
        testXMLTransformer(INPUT_UTF_16_LE, "UTF-16LE");
    }

    @Test
    public void testUTF16LEBOM() throws Exception {
        testXMLTransformer(INPUT_UTF_16_LE_BOM, "UTF-16LE");
    }

    @Test
    public void testUTF16BE() throws Exception {
        testXMLTransformer(INPUT_UTF_16_BE, "UTF-16BE");
    }

    @Test
    public void testUTF16BEBOM() throws Exception {
        testXMLTransformer(INPUT_UTF_16_BE_BOM, "UTF-16BE");
    }

    @Test
    public void testFragment() throws Exception {
        final PipelineEntity pipelineEntity = createFragmentPipeline();
        test(pipelineEntity, INPUT_FRAGMENT, null);
    }

    private void testXMLTransformer(final String inputResource, final String encoding) throws Exception {
        final PipelineEntity pipelineEntity = createTransformerPipeline();
        test(pipelineEntity, inputResource, encoding);
    }

    private PipelineEntity createFragmentPipeline() {
        // Create a record for the TextConverter.
        final InputStream textConverterInputStream = StroomPipelineTestFileUtil.getInputStream(FRAGMENT_WRAPPER);
        TextConverter textConverter = textConverterService.create("Test Text Converter");
        textConverter.setConverterType(TextConverterType.XML_FRAGMENT);
        textConverter.setData(StreamUtil.streamToString(textConverterInputStream));
        textConverter = textConverterService.save(textConverter);

        // Get the pipeline config.
        final String data = StroomPipelineTestFileUtil.getString(FRAGMENT_PIPELINE);
        final PipelineEntity pipelineEntity = PipelineTestUtil.createTestPipeline(pipelineService, data);
        pipelineEntity.getPipelineData().addProperty(
                PipelineDataUtil.createProperty(CombinedParser.DEFAULT_NAME, "textConverter", textConverter));
        pipelineEntity.setParentPipeline(DocRefUtil.create(createTransformerPipeline()));
        return pipelineService.save(pipelineEntity);
    }

    private PipelineEntity createTransformerPipeline() {
        // Create a record for the XSLT.
        final InputStream xsltInputStream = StroomPipelineTestFileUtil.getInputStream(XSLT_PATH);
        XSLT xslt = xsltService.create("Test XSLT");
        xslt.setData(StreamUtil.streamToString(xsltInputStream));
        xslt = xsltService.save(xslt);

        // Get the pipeline config.
        final String data = StroomPipelineTestFileUtil.getString(TRANSFORMER_PIPELINE);
        final PipelineEntity pipelineEntity = PipelineTestUtil.createTestPipeline(pipelineService, data);
        pipelineEntity.getPipelineData()
                .addProperty(PipelineDataUtil.createProperty("translationFilter", "xslt", xslt));
        return pipelineService.save(pipelineEntity);
    }

    private void test(final PipelineEntity pipelineEntity, final String inputResource, final String encoding)
            throws Exception {
        final Path tempDir = getCurrentTestDir();

        // Make sure the config dir is set.
        System.setProperty("stroom.temp", FileUtil.getCanonicalPath(tempDir));

        // Delete any output file.
        final Path outputFile = tempDir.resolve("TestXMLTransformer.xml");
        final Path outputLockFile = tempDir.resolve("TestXMLTransformer.xml.lock");
        FileUtil.deleteFile(outputFile);
        FileUtil.deleteFile(outputLockFile);

        // Setup the error handler.
        final LoggingErrorReceiver loggingErrorReceiver = new LoggingErrorReceiver();
        errorReceiver.setErrorReceiver(loggingErrorReceiver);

        // Create the parser.
        final PipelineData pipelineData = pipelineDataCache.get(pipelineEntity);
        final Pipeline pipeline = pipelineFactory.create(pipelineData);

        // Get the input stream.
        final InputStream inputStream = StroomPipelineTestFileUtil.getInputStream(inputResource);

        pipeline.startProcessing();

        pipeline.process(inputStream, encoding);

        pipeline.endProcessing();

        // Close all streams that have been written.,
        streamCloser.close();

        Assert.assertTrue(recordCount.getRead() > 0);
        Assert.assertTrue(recordCount.getWritten() > 0);
        Assert.assertEquals(recordCount.getRead(), recordCount.getWritten());
        Assert.assertEquals(NUMBER_OF_RECORDS, recordCount.getRead());
        Assert.assertEquals(NUMBER_OF_RECORDS, recordCount.getWritten());
        Assert.assertEquals(0, loggingErrorReceiver.getRecords(Severity.WARNING));
        Assert.assertEquals(0, loggingErrorReceiver.getRecords(Severity.ERROR));
        Assert.assertEquals(0, loggingErrorReceiver.getRecords(Severity.FATAL_ERROR));

        if (!loggingErrorReceiver.isAllOk()) {
            Assert.fail(loggingErrorReceiver.toString());
        }

        final Path refFile = StroomPipelineTestFileUtil.getTestResourcesFile(REFERENCE);
        ComparisonHelper.compareFiles(refFile, outputFile);
    }
}