/*
 * Copyright 2018 Crown Copyright
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

package stroom.statistics.stroomstats.internal;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import stroom.explorer.ExplorerActionHandler;
import stroom.pipeline.factory.Element;
import stroom.statistics.internal.InternalStatisticsService;

public class InternalModule extends AbstractModule {
    @Override
    protected void configure() {
        final Multibinder<InternalStatisticsService> multibinder = Multibinder.newSetBinder(binder(), InternalStatisticsService.class);
        multibinder.addBinding().to(StroomStatsInternalStatisticsService.class);

        final Multibinder<Element> elementBinder = Multibinder.newSetBinder(binder(), Element.class);
        elementBinder.addBinding().to(stroom.statistics.stroomstats.pipeline.appender.StroomStatsAppender.class);
        elementBinder.addBinding().to(stroom.statistics.stroomstats.pipeline.filter.StroomStatsFilter.class);
    }
}