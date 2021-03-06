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

import stroom.entity.MockEntityService;
import stroom.pipeline.shared.PipelineDoc;
import stroom.docref.DocRef;
import stroom.streamtask.shared.FindStreamProcessorCriteria;
import stroom.streamtask.shared.Processor;

import javax.inject.Singleton;

/**
 * Mock object.
 * <p>
 * In memory simple process manager that also uses the mock stream store.
 */
@Singleton
public class MockStreamProcessorService extends MockEntityService<Processor, FindStreamProcessorCriteria>
        implements StreamProcessorService {
    @Override
    public Processor loadByIdInsecure(final long id) {
        return loadById(id);
    }

    @Override
    public boolean isMatch(final FindStreamProcessorCriteria criteria, final Processor entity) {
        if (!super.isMatch(criteria, entity)) {
            return false;
        }
        return criteria.obtainPipelineSet().isMatch(new DocRef(PipelineDoc.DOCUMENT_TYPE, entity.getPipelineUuid()));
    }

    @Override
    public Class<Processor> getEntityClass() {
        return Processor.class;
    }
}
