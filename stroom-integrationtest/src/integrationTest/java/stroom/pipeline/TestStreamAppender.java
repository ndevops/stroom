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
import stroom.data.meta.api.Data;
import stroom.data.meta.api.DataMetaService;
import stroom.data.meta.api.FindDataCriteria;
import stroom.data.store.api.StreamSource;
import stroom.data.store.api.StreamStore;
import stroom.util.io.ByteCountInputStream;
import stroom.util.io.StreamUtil;

import javax.inject.Inject;
import java.util.List;

public class TestStreamAppender extends AbstractStreamAppenderTest {
    @Inject
    private DataMetaService dataMetaService;
    @Inject
    private StreamStore streamStore;

    @Test
    public void testXML() throws Exception {
        test("TestStreamAppender", "XML");
        validateOuptut("TestStreamAppender/TestStreamAppender_XML.out", "XML");
    }

    @Test
    public void testXMLRolling() throws Exception {
        test("TestStreamAppender", "XML_Rolling");

        final List<Data> list = dataMetaService.find(new FindDataCriteria());
        final long id = list.get(0).getId();
        final StreamSource streamSource = streamStore.openStreamSource(id);
        final ByteCountInputStream byteCountInputStream = new ByteCountInputStream(streamSource.getInputStream());
        StreamUtil.streamToString(byteCountInputStream);
        Assert.assertEquals(1198, byteCountInputStream.getCount());
        streamStore.closeStreamSource(streamSource);
    }

    @Test
    public void testText() throws Exception {
        test("TestStreamAppender", "Text");
        validateOuptut("TestStreamAppender/TestStreamAppender_Text.out", "Text");
    }
}