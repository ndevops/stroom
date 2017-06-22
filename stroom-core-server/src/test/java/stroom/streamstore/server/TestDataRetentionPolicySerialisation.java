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

package stroom.streamstore.server;

import org.junit.Test;
import stroom.entity.server.util.XMLMarshallerUtil;
import stroom.query.shared.ExpressionBuilder;
import stroom.query.shared.ExpressionOperator;
import stroom.query.shared.ExpressionOperator.Op;
import stroom.query.shared.ExpressionTerm.Condition;
import stroom.streamstore.shared.DataRetentionPolicy;
import stroom.streamstore.shared.DataRetentionRule;
import stroom.streamstore.shared.TimeUnit;

import javax.xml.bind.JAXBContext;
import java.util.ArrayList;
import java.util.List;

public class TestDataRetentionPolicySerialisation {
    @Test
    public void test() throws Exception {
        final ExpressionBuilder builder = new ExpressionBuilder(true, Op.AND);
        builder.addTerm("StreamType", Condition.EQUALS, "Raw Events");
        builder.addTerm("Feed", Condition.EQUALS, "TEST_FEED");
        final ExpressionOperator expression = builder.build();

        final List<DataRetentionRule> list = new ArrayList<>();
        list.add(new DataRetentionRule(expression, 10, TimeUnit.DAYS, false));
        list.add(new DataRetentionRule(expression, 1, TimeUnit.MONTHS, false));
        list.add(new DataRetentionRule(expression, 2, TimeUnit.WEEKS, false));

        final DataRetentionPolicy policies = new DataRetentionPolicy(list);

        final JAXBContext context = JAXBContext.newInstance(DataRetentionPolicy.class);
        final String xml = XMLMarshallerUtil.marshal(context, policies);

        System.out.println(xml);
    }
}