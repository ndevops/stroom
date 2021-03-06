/*
 * Copyright 2016 Crown Copyright
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

package stroom.entity.shared;

import stroom.docref.SharedObject;

public class EntityServiceFindAction<C extends BaseCriteria, E extends SharedObject> extends FindAction<C, E> {
    private static final long serialVersionUID = 800905016214418723L;

    public EntityServiceFindAction() {
        // Default constructor necessary for GWT serialisation.
    }

    public EntityServiceFindAction(final C criteria) {
        super(criteria);
    }
}
