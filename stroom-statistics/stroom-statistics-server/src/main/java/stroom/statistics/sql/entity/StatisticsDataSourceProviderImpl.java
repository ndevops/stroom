/*
 * Copyright 2017 Crown Copyright
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package stroom.statistics.sql.entity;

import stroom.datasource.api.v2.DataSource;
import stroom.datasource.api.v2.DataSourceField;
import stroom.datasource.api.v2.DataSourceField.DataSourceFieldType;
import stroom.docref.DocRef;
import stroom.query.api.v2.ExpressionTerm;
import stroom.query.api.v2.ExpressionTerm.Condition;
import stroom.security.Security;
import stroom.statistics.shared.StatisticStoreDoc;
import stroom.statistics.shared.StatisticType;
import stroom.statistics.shared.common.StatisticField;
import stroom.statistics.sql.Statistics;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class StatisticsDataSourceProviderImpl implements StatisticsDataSourceProvider {
    private final StatisticStoreCache statisticStoreCache;
    private final Statistics statistics;
    private final Security security;

    @Inject
    StatisticsDataSourceProviderImpl(final StatisticStoreCache statisticStoreCache,
                                     final Statistics statistics,
                                     final Security security) {
        this.statisticStoreCache = statisticStoreCache;
        this.statistics = statistics;
        this.security = security;
    }

    @Override
    public DataSource getDataSource(final DocRef docRef) {
        return security.useAsReadResult(() -> {
            final StatisticStoreDoc entity = statisticStoreCache.getStatisticsDataSource(docRef);
            if (entity == null) {
                return null;
            }

            final List<DataSourceField> fields = buildFields(entity);

            return new DataSource(fields);
        });
    }

    /**
     * Turn the {@link StatisticStoreDoc} into an {@link List<DataSourceField>} object
     * <p>
     * This builds the standard set of fields for a statistics store, which can
     * be filtered by the relevant statistics store instance
     */
    private List<DataSourceField> buildFields(final StatisticStoreDoc entity) {
        List<DataSourceField> fields = new ArrayList<>();

        // TODO currently only BETWEEN is supported, but need to add support for
        // more conditions like >, >=, <, <=, =
        addField(StatisticStoreDoc.FIELD_NAME_DATE_TIME, DataSourceFieldType.DATE_FIELD, true,
                Arrays.asList(ExpressionTerm.Condition.BETWEEN), fields);

        // one field per tag
        if (entity.getConfig() != null) {
            final List<Condition> supportedConditions = Arrays.asList(Condition.EQUALS, Condition.IN);

            for (final StatisticField statisticField : entity.getStatisticFields()) {
                // TODO currently only EQUALS is supported, but need to add
                // support for more conditions like CONTAINS
                addField(statisticField.getFieldName(), DataSourceFieldType.FIELD, true,
                        supportedConditions, fields);
            }
        }

        addField(StatisticStoreDoc.FIELD_NAME_COUNT, DataSourceFieldType.NUMERIC_FIELD, false, null, fields);

        if (entity.getStatisticType().equals(StatisticType.VALUE)) {
            addField(StatisticStoreDoc.FIELD_NAME_VALUE, DataSourceFieldType.NUMERIC_FIELD, false, null, fields);
        }

        addField(StatisticStoreDoc.FIELD_NAME_PRECISION_MS, DataSourceFieldType.NUMERIC_FIELD, false, null, fields);

        // Filter fields.
        if (entity.getConfig() != null) {
            fields = statistics.getSupportedFields(fields);
        }

        return fields;
    }

    /**
     * @return A reference to the create index field so additional modifications
     * can be made
     */
    private void addField(final String name, final DataSourceFieldType type, final boolean isQueryable,
                          final List<Condition> supportedConditions, final List<DataSourceField> fields) {
        final DataSourceField field = new DataSourceField(type, name, isQueryable, supportedConditions);
        fields.add(field);
    }
}
