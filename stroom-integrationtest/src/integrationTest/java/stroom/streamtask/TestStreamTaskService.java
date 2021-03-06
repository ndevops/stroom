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

package stroom.streamtask;

import org.junit.Assert;
import org.junit.Test;
import stroom.entity.shared.Period;
import stroom.node.shared.Node;
import stroom.data.meta.api.Data;
import stroom.data.meta.api.DataMetaService;
import stroom.streamstore.shared.StreamTypeNames;
import stroom.streamtask.shared.FindStreamTaskCriteria;
import stroom.streamtask.shared.Processor;
import stroom.streamtask.shared.ProcessorFilterTask;
import stroom.streamtask.shared.TaskStatus;
import stroom.task.api.SimpleTaskContext;
import stroom.test.AbstractCoreIntegrationTest;
import stroom.test.CommonTestScenarioCreator;
import stroom.util.test.FileSystemTestUtil;

import javax.inject.Inject;
import java.time.Instant;
import java.time.ZoneOffset;

public class TestStreamTaskService extends AbstractCoreIntegrationTest {
    @Inject
    private CommonTestScenarioCreator commonTestScenarioCreator;
    @Inject
    private StreamTaskService streamTaskService;
    @Inject
    private DataMetaService streamMetaService;
    @Inject
    private StreamTaskCreator streamTaskCreator;

    @Test
    public void testSaveAndGetAll() {
        final String feedName = FileSystemTestUtil.getUniqueTestString();
        final Data file1 = commonTestScenarioCreator.createSample2LineRawFile(feedName, StreamTypeNames.RAW_EVENTS);
        final Data file2 = commonTestScenarioCreator.createSampleBlankProcessedFile(feedName, file1);
        final Data file3 = commonTestScenarioCreator.createSample2LineRawFile(feedName, StreamTypeNames.RAW_EVENTS);

        commonTestScenarioCreator.createBasicTranslateStreamProcessor(feedName);

        Assert.assertEquals("checking we can delete stand alone files", 1, streamMetaService.delete(file3.getId()));

        // Create all required tasks.
        createTasks();

        final ProcessorFilterTask ps1 = streamTaskService.find(FindStreamTaskCriteria.createWithStream(file1)).getFirst();
        Assert.assertNotNull(ps1);
        ps1.setStatus(TaskStatus.COMPLETE);

        streamTaskService.save(ps1);

        FindStreamTaskCriteria criteria = new FindStreamTaskCriteria();
//        criteria.getFetchSet().add(StreamEntity.ENTITY_TYPE);
        criteria.obtainStreamTaskStatusSet().add(TaskStatus.COMPLETE);

        Assert.assertEquals(1, streamTaskService.find(criteria).size());

        // Check the date filter works
        criteria.setCreatePeriod(new Period(file1.getCreateMs() - 10000, file1.getCreateMs() + 10000));
        Assert.assertEquals(1, streamTaskService.find(criteria).size());

        criteria.setCreatePeriod(
                new Period(Instant.ofEpochMilli(criteria.getCreatePeriod().getFrom()).atZone(ZoneOffset.UTC).plusYears(100).toInstant().toEpochMilli(),
                        Instant.ofEpochMilli(criteria.getCreatePeriod().getTo()).atZone(ZoneOffset.UTC).plusYears(100).toInstant().toEpochMilli()));
        Assert.assertEquals(0, streamTaskService.find(criteria).size());

        Assert.assertNotNull(streamMetaService.getData(file1.getId()));
        Assert.assertNotNull(streamMetaService.getData(file2.getId()));

        criteria = new FindStreamTaskCriteria();
        Assert.assertNotNull(streamTaskService.findSummary(criteria));
    }

    @Test
    public void testApplyAllCriteria() {
        final String feedName = FileSystemTestUtil.getUniqueTestString();

        final Node testNode = new Node();
        testNode.setId(1L);

        final FindStreamTaskCriteria criteria = new FindStreamTaskCriteria();
        criteria.getFetchSet().add(Node.ENTITY_TYPE);
        criteria.obtainNodeIdSet().add(1L);
        criteria.setSort(FindStreamTaskCriteria.FIELD_CREATE_TIME);
        criteria.obtainStreamTaskIdSet().add(1L);
//        criteria.obtainFeedNameSet().add(feedName);
        criteria.obtainStreamIdSet().add(1L);
//        criteria.obtainStreamTypeNameSet().add(StreamTypeNames.RAW_EVENTS);
        criteria.obtainStreamTaskStatusSet().add(TaskStatus.COMPLETE);

        criteria.setCreatePeriod(new Period(System.currentTimeMillis(), System.currentTimeMillis()));
//        criteria.setEffectivePeriod(new Period(System.currentTimeMillis(), System.currentTimeMillis()));
//        criteria.obtainStreamTypeNameSet().add(StreamTypeNames.CONTEXT);

//        criteria.getFetchSet().add(StreamEntity.ENTITY_TYPE);
        criteria.getFetchSet().add(Node.ENTITY_TYPE);
//        criteria.getFetchSet().add(FeedDoc.DOCUMENT_TYPE);
        criteria.getFetchSet().add(Processor.ENTITY_TYPE);

        Assert.assertEquals(0, streamTaskService.find(criteria).size());
    }

    @Test
    public void testApplyAllCriteriaSummary() {
        final String feedName = FileSystemTestUtil.getUniqueTestString();

        final Node testNode = new Node();
        testNode.setId(1L);

        final FindStreamTaskCriteria criteria = new FindStreamTaskCriteria();
        criteria.getFetchSet().add(Node.ENTITY_TYPE);
        criteria.obtainNodeIdSet().add(1L);
        criteria.setSort(FindStreamTaskCriteria.FIELD_CREATE_TIME);
        criteria.obtainStreamTaskIdSet().add(1L);
//        criteria.obtainFeedNameSet().add(feedName);
        criteria.obtainStreamIdSet().add(1L);
//        criteria.obtainStreamTypeNameSet().add(StreamTypeNames.RAW_EVENTS);
        criteria.obtainStreamTaskStatusSet().add(TaskStatus.COMPLETE);

        criteria.setCreatePeriod(new Period(System.currentTimeMillis(), System.currentTimeMillis()));
//        criteria.setEffectivePeriod(new Period(System.currentTimeMillis(), System.currentTimeMillis()));
//        criteria.obtainStreamTypeNameSet().add(StreamTypeNames.CONTEXT);

    }

    private void createTasks() {
        // Make sure there are no tasks yet.
        streamTaskCreator.createTasks(new SimpleTaskContext());
    }
}
