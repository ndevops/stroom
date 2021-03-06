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

package stroom.data.store;

import org.junit.Test;
import stroom.data.meta.api.MetaDataSource;
import stroom.entity.util.XMLMarshallerUtil;
import stroom.query.api.v2.ExpressionOperator;
import stroom.query.api.v2.ExpressionOperator.Op;
import stroom.query.api.v2.ExpressionTerm.Condition;
import stroom.ruleset.shared.DataRetentionPolicy;
import stroom.ruleset.shared.DataRetentionRule;
import stroom.streamstore.shared.TimeUnit;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.util.ArrayList;
import java.util.List;

public class TestDataRetentionPolicySerialisation {
    @Test
    public void test() throws JAXBException {
        final ExpressionOperator.Builder builder = new ExpressionOperator.Builder(true, Op.AND);
        builder.addTerm(MetaDataSource.STREAM_TYPE_NAME, Condition.EQUALS, "Raw Events");
        builder.addTerm(MetaDataSource.FEED_NAME, Condition.EQUALS, "TEST_FEED");
        final ExpressionOperator expression = builder.build();

        final List<DataRetentionRule> list = new ArrayList<>();
        list.add(createRule(1, expression, 10, TimeUnit.DAYS));
        list.add(createRule(2, expression, 1, TimeUnit.MONTHS));
        list.add(createRule(3, expression, 2, TimeUnit.WEEKS));

        final DataRetentionPolicy policies = new DataRetentionPolicy(list);

        final JAXBContext context = JAXBContext.newInstance(DataRetentionPolicy.class);
        final String xml = XMLMarshallerUtil.marshal(context, policies);

        System.out.println(xml);
    }

    private DataRetentionRule createRule(final int num, final ExpressionOperator expression, final int age, final TimeUnit timeUnit) {
        return new DataRetentionRule(num, System.currentTimeMillis(), "rule " + num, true, expression, age, timeUnit, false);
    }
}