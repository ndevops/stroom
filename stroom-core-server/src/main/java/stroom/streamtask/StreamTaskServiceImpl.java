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

import event.logging.BaseAdvancedQueryItem;
import stroom.entity.CriteriaLoggingUtil;
import stroom.entity.QueryAppender;
import stroom.entity.StroomEntityManager;
import stroom.entity.SystemEntityServiceImpl;
import stroom.entity.shared.BaseResultList;
import stroom.entity.shared.SQLNameConstants;
import stroom.entity.shared.SummaryDataRow;
import stroom.entity.util.FieldMap;
import stroom.entity.util.HqlBuilder;
import stroom.entity.util.SqlBuilder;
import stroom.node.shared.Node;
import stroom.pipeline.shared.PipelineDoc;
import stroom.security.Security;
import stroom.security.shared.PermissionNames;
import stroom.streamtask.shared.FindStreamTaskCriteria;
import stroom.streamtask.shared.Processor;
import stroom.streamtask.shared.ProcessorFilter;
import stroom.streamtask.shared.ProcessorFilterTask;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Set;

/**
 * Base class the API stream task services.
 */
@Singleton
public class StreamTaskServiceImpl extends SystemEntityServiceImpl<ProcessorFilterTask, FindStreamTaskCriteria>
        implements StreamTaskService {
    private static final String TABLE_PREFIX_STREAM_TASK = "ST.";
    private final Security security;

    @Inject
    StreamTaskServiceImpl(final StroomEntityManager entityManager,
                          final Security security) {
        super(entityManager, security);
        this.security = security;
    }

    @Override
    public Class<ProcessorFilterTask> getEntityClass() {
        return ProcessorFilterTask.class;
    }

    @Override
    public BaseResultList<SummaryDataRow> findSummary(final FindStreamTaskCriteria criteria) {
        return security.secureResult(permission(), () -> {
            final SqlBuilder sql = new SqlBuilder();
            sql.append("SELECT D.* FROM (");
            sql.append("SELECT 0");
//            sql.append(StreamProcessor.PIPELINE_UUID);
//            sql.append(" PIPE_UUID,");
            sql.append(", SP.");
            sql.append(Processor.PIPELINE_UUID);
            sql.append(" PIPE_UUID,");
//            sql.append(" F.");
//            sql.append(FeedEntity.ID);
//            sql.append(" FEED_ID, F.");
//            sql.append(SQLNameConstants.NAME);
//            sql.append(" F_NAME,");
            sql.append(" SPF.");
            sql.append(ProcessorFilter.PRIORITY);
            sql.append(" PRIORITY_1, SPF.");
            sql.append(ProcessorFilter.PRIORITY);
            sql.append(" PRIORITY_2, ST.");
            sql.append(ProcessorFilterTask.STATUS);
            sql.append(" STAT_ID1, ST.");
            sql.append(ProcessorFilterTask.STATUS);
            sql.append(" STAT_ID2, COUNT(*) AS ");
            sql.append(SQLNameConstants.COUNT);
            sql.append(" FROM ");
            sql.append(ProcessorFilterTask.TABLE_NAME);
            sql.append(" ST JOIN ");
//            sql.append(StreamEntity.TABLE_NAME);
//            sql.append(" S ON (S.");
//            sql.append(StreamEntity.ID);
//            sql.append(" = ST.");
//            sql.append(StreamEntity.FOREIGN_KEY);
//            sql.append(") JOIN ");
//            sql.append(FeedEntity.TABLE_NAME);
//            sql.append(" F ON (F.");
//            sql.append(FeedEntity.ID);
//            sql.append(" = S.");
//            sql.append(FeedEntity.FOREIGN_KEY);
//            sql.append(") JOIN ");
            sql.append(ProcessorFilter.TABLE_NAME);
            sql.append(" SPF ON (SPF.");
            sql.append(ProcessorFilter.ID);
            sql.append(" = ST.");
            sql.append(ProcessorFilter.FOREIGN_KEY);
            sql.append(") JOIN ");
            sql.append(Processor.TABLE_NAME);
            sql.append(" SP ON (SP.");
            sql.append(Processor.ID);
            sql.append(" = SPF.");
            sql.append(Processor.FOREIGN_KEY);
            sql.append(")");
            sql.append(" WHERE 1=1");

//            sql.appendPrimitiveValueSetQuery("S." + StreamEntity.STATUS, StreamStatusId.convertStatusSet(criteria.getStatusSet()));
            sql.appendDocRefSetQuery("SP." + Processor.PIPELINE_UUID, criteria.getPipelineSet());
//            sql.appendEntityIdSetQuery("F." + BaseEntity.ID, feedService.convertNameSet(criteria.getFeedNameSet()));

            sql.append(" GROUP BY PIPE_UUID, PRIORITY_1, STAT_ID1");
            sql.append(") D");

            sql.appendOrderBy(getSqlFieldMap().getSqlFieldMap(), criteria, null);

            return getEntityManager().executeNativeQuerySummaryDataResult(sql, 3);
        });
    }

    @Override
    public FindStreamTaskCriteria createCriteria() {
        return new FindStreamTaskCriteria();
    }

    @Override
    public void appendCriteria(final List<BaseAdvancedQueryItem> items, final FindStreamTaskCriteria criteria) {
        CriteriaLoggingUtil.appendCriteriaSet(items, "streamTaskStatusSet", criteria.getStreamTaskStatusSet());
        CriteriaLoggingUtil.appendCriteriaSet(items, "streamIdSet", criteria.getStreamIdSet());
        CriteriaLoggingUtil.appendCriteriaSet(items, "nodeIdSet", criteria.getNodeIdSet());
        CriteriaLoggingUtil.appendCriteriaSet(items, "streamTaskIdSet", criteria.getStreamTaskIdSet());
        CriteriaLoggingUtil.appendCriteriaSet(items, "streamProcessorFilterIdSet", criteria.getStreamProcessorFilterIdSet());
        CriteriaLoggingUtil.appendCriteriaSet(items, "statusSet", criteria.getStatusSet());
        CriteriaLoggingUtil.appendCriteriaSet(items, "pipelineSet", criteria.getPipelineSet());
//        CriteriaLoggingUtil.appendCriteriaSet(items, "feedNameSet", criteria.getFeedNameSet());
        CriteriaLoggingUtil.appendDateTerm(items, "createMs", criteria.getCreateMs());
        CriteriaLoggingUtil.appendRangeTerm(items, "createPeriod", criteria.getCreatePeriod());
//        CriteriaLoggingUtil.appendRangeTerm(items, "effectivePeriod", criteria.getEffectivePeriod());
        super.appendCriteria(items, criteria);
    }

    @Override
    protected QueryAppender<ProcessorFilterTask, FindStreamTaskCriteria> createQueryAppender(StroomEntityManager entityManager) {
        return new StreamTaskQueryAppender(entityManager);
    }

    @Override
    protected FieldMap createFieldMap() {
        return super.createFieldMap()
                .add(FindStreamTaskCriteria.FIELD_CREATE_TIME, TABLE_PREFIX_STREAM_TASK + ProcessorFilterTask.CREATE_MS, "createMs")
                .add(FindStreamTaskCriteria.FIELD_START_TIME, TABLE_PREFIX_STREAM_TASK + ProcessorFilterTask.START_TIME_MS, "startTimeMs")
                .add(FindStreamTaskCriteria.FIELD_END_TIME_DATE, TABLE_PREFIX_STREAM_TASK + ProcessorFilterTask.END_TIME_MS, "endTimeMs")
                .add(FindStreamTaskCriteria.FIELD_FEED_NAME, "F_NAME", "stream.feed.name")
                .add(FindStreamTaskCriteria.FIELD_PRIORITY, "PRIORITY_1", "streamProcessorFilter.priority")
                .add(FindStreamTaskCriteria.FIELD_PIPELINE_UUID, "P_NAME", "streamProcessorFilter.streamProcessor.pipeline.uuid")
                .add(FindStreamTaskCriteria.FIELD_STATUS, "STAT_ID1", "pstatus")
                .add(FindStreamTaskCriteria.FIELD_COUNT, SQLNameConstants.COUNT, "NA")
                .add(FindStreamTaskCriteria.FIELD_NODE, null, "node.name");
    }

    @Override
    protected String permission() {
        return PermissionNames.MANAGE_PROCESSORS_PERMISSION;
    }

    private class StreamTaskQueryAppender extends QueryAppender<ProcessorFilterTask, FindStreamTaskCriteria> {

        StreamTaskQueryAppender(final StroomEntityManager entityManager) {
            super(entityManager);
        }

        @Override
        protected void appendBasicJoin(final HqlBuilder sql, final String alias, final Set<String> fetchSet) {
            super.appendBasicJoin(sql, alias, fetchSet);

            if (fetchSet != null) {
                if (fetchSet.contains(Node.ENTITY_TYPE)) {
                    sql.append(" LEFT JOIN FETCH " + alias + ".node AS node");
                }
                if (fetchSet.contains(ProcessorFilter.ENTITY_TYPE) || fetchSet.contains(Processor.ENTITY_TYPE)
                        || fetchSet.contains(PipelineDoc.DOCUMENT_TYPE)) {
                    sql.append(" JOIN FETCH " + alias + ".streamProcessorFilter AS spf");
                }
                if (fetchSet.contains(Processor.ENTITY_TYPE) || fetchSet.contains(PipelineDoc.DOCUMENT_TYPE)) {
                    sql.append(" JOIN FETCH spf.streamProcessor AS sp");
                }
//                if (fetchSet.contains(StreamEntity.ENTITY_TYPE)) {
//                    sql.append(" JOIN FETCH " + alias + ".stream AS s");
//                }
//                if (fetchSet.contains(FeedEntity.ENTITY_TYPE)) {
//                    sql.append(" JOIN FETCH s.feed AS f");
//                }
//                if (fetchSet.contains(StreamTypeEntity.ENTITY_TYPE)) {
//                    sql.append(" JOIN FETCH s.streamType AS st");
//                }
            }
        }

        @Override
        protected void appendBasicCriteria(final HqlBuilder sql,
                                           final String alias,
                                           final FindStreamTaskCriteria criteria) {
            super.appendBasicCriteria(sql, alias, criteria);

            // Append all the criteria
            sql.appendPrimitiveValueSetQuery(alias + ".pstatus", criteria.getStreamTaskStatusSet());

            sql.appendCriteriaSetQuery(alias + ".id", criteria.getStreamTaskIdSet());

            sql.appendCriteriaSetQuery(alias + ".node.id", criteria.getNodeIdSet());

            sql.appendDocRefSetQuery(alias + ".streamProcessorFilter.streamProcessor.pipelineUuid",
                    criteria.obtainPipelineSet());

            sql.appendCriteriaSetQuery(alias + ".streamProcessorFilter.id", criteria.getStreamProcessorFilterIdSet());

            sql.appendValueQuery(alias + ".createMs", criteria.getCreateMs());

//            if (criteria.getStatusSet() != null || criteria.getFeedIdSet() != null || criteria.getPipelineSet() != null) {
            sql.appendCriteriaSetQuery(alias + ".streamId", criteria.getStreamIdSet());

//            sql.appendCriteriaSetQuery(alias + ".stream.streamType", streamTypeService.convertNameSet(criteria.getStreamTypeNameSet()));
//
//            sql.appendPrimitiveValueSetQuery(alias + ".stream.pstatus", StreamStatusId.convertStatusSet(criteria.getStatusSet()));

//            sql.appendEntityIdSetQuery(alias + ".streamProcessorFilter.streamProcessor.pipeline",
//                    criteria.getPipelineSet());
//
//                sql.appendEntityIdSetQuery(alias + ".streamProcessorFilter.streamProcessor",
//                        criteria.getStreamProcessorIdSet());
//
//            sql.appendEntityIdSetQuery(alias + ".stream.feed", feedService.convertNameSet(criteria.getFeedNameSet()));

            sql.appendRangeQuery(alias + ".createMs", criteria.getCreatePeriod());
//            sql.appendRangeQuery(alias + ".stream.effectiveMs", criteria.getEffectivePeriod());
        }
    }
}