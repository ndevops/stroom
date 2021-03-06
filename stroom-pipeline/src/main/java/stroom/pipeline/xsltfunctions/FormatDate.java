/*
 * Copyright 2016 Crown Copyright
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

package stroom.pipeline.xsltfunctions;

import net.sf.saxon.expr.XPathContext;
import net.sf.saxon.om.EmptyAtomicSequence;
import net.sf.saxon.om.Sequence;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.value.StringValue;
import stroom.pipeline.state.StreamHolder;
import stroom.util.date.DateFormatterCache;
import stroom.util.date.DateUtil;
import stroom.util.shared.Severity;

import javax.inject.Inject;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

class FormatDate extends StroomExtensionFunctionCall {
    private static final String FULL_YEAR_PATTERN = "yyyy";
    private static final String FULL_MONTH_PATTERN = "MM";
    private static final String FULL_DAY_PATTERN = "dd";

    private final StreamHolder streamHolder;

    private ZonedDateTime baseTime;

    @Inject
    FormatDate(final StreamHolder streamHolder) {
        this.streamHolder = streamHolder;
    }

    @Override
    protected Sequence call(final String functionName, final XPathContext context, final Sequence[] arguments) {
        String result = null;

        try {
            if (arguments.length == 1) {
                result = convertMilliseconds(functionName, context, arguments);

            } else if (arguments.length >= 2 && arguments.length <= 3) {
                result = convertToStandardDateFormat(functionName, context, arguments);

            } else if (arguments.length >= 4 && arguments.length <= 5) {
                result = convertToSpecifiedDateFormat(functionName, context, arguments);
            }
        } catch (final XPathException | RuntimeException e) {
            log(context, Severity.ERROR, e.getMessage(), e);
        }

        if (result == null) {
            return EmptyAtomicSequence.getInstance();
        }
        return StringValue.makeStringValue(result);
    }

    private String convertMilliseconds(final String functionName, final XPathContext context,
                                       final Sequence[] arguments) throws XPathException {
        String result = null;
        final String milliseconds = getSafeString(functionName, context, arguments, 0);

        try {
            final long ms = Long.parseLong(milliseconds);
            result = DateUtil.createNormalDateTimeString(ms);

        } catch (final RuntimeException e) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to parse date: \"");
            sb.append(milliseconds);
            sb.append('"');
            outputWarning(context, sb, e);
        }

        return result;
    }

    private String convertToStandardDateFormat(final String functionName, final XPathContext context,
                                               final Sequence[] arguments) throws XPathException {
        String result = null;
        final String value = getSafeString(functionName, context, arguments, 0);
        final String pattern = getSafeString(functionName, context, arguments, 1);
        String timeZone = null;
        if (arguments.length == 3) {
            timeZone = getSafeString(functionName, context, arguments, 2);
        }

        // Parse the supplied date.
        long ms = -1;
        try {
            // If the incoming pattern doesn't contain year then we might need to figure the year out for ourselves.
            ms = parseDate(context, value, pattern, timeZone);
        } catch (final RuntimeException e) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to parse date: \"");
            sb.append(value);
            sb.append("\" (Pattern: ");
            sb.append(pattern);
            sb.append(", Time Zone: ");
            sb.append(timeZone);
            sb.append(")");
            outputWarning(context, sb, e);
        }

        if (ms != -1) {
            result = DateUtil.createNormalDateTimeString(ms);
        }

        return result;
    }

    private String convertToSpecifiedDateFormat(final String functionName, final XPathContext context,
                                                final Sequence[] arguments) throws XPathException {
        String result = null;
        final String value = getSafeString(functionName, context, arguments, 0);
        final String patternIn = getSafeString(functionName, context, arguments, 1);
        final String timeZoneIn = getSafeString(functionName, context, arguments, 2);
        final String patternOut = getSafeString(functionName, context, arguments, 3);
        String timeZoneOut = null;
        if (arguments.length == 5) {
            timeZoneOut = getSafeString(functionName, context, arguments, 4);
        }

        // Parse the supplied date.
        long ms = -1;
        try {
            // If the incoming pattern doesn't contain year then we might need to figure the year out for ourselves.
            ms = parseDate(context, value, patternIn, timeZoneIn);
        } catch (final RuntimeException e) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to parse date: \"");
            sb.append(value);
            sb.append("\" (Pattern: ");
            sb.append(patternIn);
            sb.append(", Time Zone: ");
            sb.append(timeZoneIn);
            sb.append(")");
            outputWarning(context, sb, e);
        }

        if (ms != -1) {
            // Resolve the output time zone.
            final ZoneId zoneId = getTimeZone(context, timeZoneOut);
            try {
                // Now format the date using the specified pattern and time
                // zone.
                final ZonedDateTime dateTime = Instant.ofEpochMilli(ms).atZone(zoneId);
                final DateTimeFormatter dateTimeFormatter = DateFormatterCache.getFormatter(patternOut);
                result = dateTimeFormatter.format(dateTime);
            } catch (final RuntimeException e) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Failed to format date: \"");
                sb.append(value);
                sb.append("\" (Pattern: ");
                sb.append(patternOut);
                sb.append(", Time Zone: ");
                sb.append(timeZoneOut);
                sb.append(")");
                outputWarning(context, sb, e);
            }
        }

        return result;
    }

    long parseDate(final XPathContext context, final String value, final String pattern, final String timeZone) {
        ZonedDateTime dateTime;

        // Don't use the defaulting formatter if we can help it.
        if (pattern.contains(FULL_YEAR_PATTERN) && pattern.contains(FULL_MONTH_PATTERN) && pattern.contains(FULL_DAY_PATTERN)) {
            final DateTimeFormatter formatter = DateFormatterCache.getFormatter(pattern);
            final ZoneId zoneId = getTimeZone(context, timeZone);

            // Parse the date as best we can.
            dateTime = DateUtil.parseBest(value, formatter, zoneId);

        } else {
            final ZonedDateTime referenceDateTime = getBaseTime();
            final DateTimeFormatter formatter = DateFormatterCache.getDefaultingFormatter(pattern, referenceDateTime);
            final ZoneId zoneId = getTimeZone(context, timeZone);

            // Parse the date as best we can.
            dateTime = DateUtil.parseBest(value, formatter, zoneId);

            // Subtract a year if the date appears to be after our reference time.
            if (dateTime.isAfter(referenceDateTime)) {
                if (!pattern.contains("y")) {
                    if (!pattern.contains("M")) {
                        dateTime = dateTime.minusMonths(1);
                    } else {
                        dateTime = dateTime.minusYears(1);
                    }
                }
            }
        }

        return dateTime.toInstant().toEpochMilli();
    }

    private ZoneId getTimeZone(final XPathContext context, final String timeZone) {
        try {
            return DateFormatterCache.getZoneId(timeZone);
        } catch (final RuntimeException e) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Time Zone '");
            sb.append(timeZone);
            sb.append("' is not recognised, defaulting to UTC");
            outputWarning(context, sb, e);
        }
        return ZoneOffset.UTC;
    }

    private ZonedDateTime getBaseTime() {
        if (baseTime == null) {
            Long createMs = null;
            if (streamHolder != null) {
                if (streamHolder.getStream() != null) {
                    createMs = streamHolder.getStream().getCreateMs();
                }
            }
            if (createMs != null) {
                baseTime = Instant.ofEpochMilli(createMs).atZone(ZoneOffset.UTC);
            } else {
                baseTime = Instant.now().atZone(ZoneOffset.UTC);
            }
        }
        return baseTime;
    }
}
