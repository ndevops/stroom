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

package stroom.entity.cluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stroom.entity.FindClearService;
import stroom.lifecycle.StroomBeanStore;
import stroom.security.Security;
import stroom.task.api.AbstractTaskHandler;
import stroom.task.api.TaskHandlerBean;
import stroom.util.shared.VoidResult;

import javax.inject.Inject;

@TaskHandlerBean(task = FindClearServiceClusterTask.class)
class FindClearServiceClusterHandler extends AbstractTaskHandler<FindClearServiceClusterTask<?>, VoidResult> {
    private static final Logger LOGGER = LoggerFactory.getLogger(FindClearServiceClusterHandler.class);

    private final StroomBeanStore stroomBeanStore;
    private final Security security;

    @Inject
    FindClearServiceClusterHandler(final StroomBeanStore stroomBeanStore,
                                   final Security security) {
        this.stroomBeanStore = stroomBeanStore;
        this.security = security;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public VoidResult exec(final FindClearServiceClusterTask<?> task) {
        return security.secureResult(() -> {
            try {
                if (task == null) {
                    throw new RuntimeException("No task supplied");
                }

                if (task.getBeanClass() == null) {
                    throw new RuntimeException("No task bean class supplied");
                }

                final Object obj = stroomBeanStore.getInstance(task.getBeanClass());
                if (obj == null) {
                    throw new RuntimeException("Cannot find bean of class type: " + task.getBeanClass());
                }

                ((FindClearService) obj).findClear(task.getCriteria());

            } catch (final RuntimeException e) {
                LOGGER.error(e.getMessage(), e);
            }

            return new VoidResult();
        });
    }
}
