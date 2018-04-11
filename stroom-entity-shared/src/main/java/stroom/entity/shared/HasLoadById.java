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

import java.util.Set;

public interface HasLoadById<E extends Entity> {
    /**
     * @param id The id of the entity in the database
     * @return The entity, or null if it could not be found
     */
    E loadById(long id);

    E loadById(long id, Set<String> fetchSet);

//    E loadByIdInsecure(long id, Set<String> fetchSet);
}
