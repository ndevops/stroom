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

import stroom.entity.BaseEntityService;
import stroom.entity.FindService;
import stroom.docref.DocRef;
import stroom.streamstore.shared.QueryData;
import stroom.streamtask.shared.FindStreamProcessorFilterCriteria;
import stroom.streamtask.shared.Processor;
import stroom.streamtask.shared.ProcessorFilter;

public interface StreamProcessorFilterService
        extends BaseEntityService<ProcessorFilter>, FindService<ProcessorFilter, FindStreamProcessorFilterCriteria> {
    void addFindStreamCriteria(Processor streamProcessor,
                               int priority,
                               QueryData queryData);

    ProcessorFilter createNewFilter(DocRef pipelineRef,
                                    QueryData queryData,
                                    boolean enabled,
                                    int priority);
}
