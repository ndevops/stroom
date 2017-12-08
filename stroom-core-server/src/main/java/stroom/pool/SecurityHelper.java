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
 */

package stroom.pool;

import stroom.security.SecurityContext;
import stroom.util.task.ServerTask;

public class SecurityHelper implements AutoCloseable {
    private volatile SecurityContext securityContext;
    private final Action action;

    private SecurityHelper(final SecurityContext securityContext, final Action action) {
        this.securityContext = securityContext;
        this.action = action;

        if (securityContext != null) {
            switch (action) {
                case PROC_USER:
                    securityContext.pushUser(ServerTask.INTERNAL_PROCESSING_USER_TOKEN);
                    break;
                case ELEVATE:
                    securityContext.elevatePermissions();
                    break;
            }
        }
    }

    public static SecurityHelper asProcUser(SecurityContext securityContext) {
        return new SecurityHelper(securityContext, Action.PROC_USER);
    }

    public static SecurityHelper elev(SecurityContext securityContext) {
        return new SecurityHelper(securityContext, Action.ELEVATE);
    }

    @Override
    public synchronized void close() {
        if (securityContext != null) {
            switch (action) {
                case PROC_USER:
                    securityContext.popUser();
                    break;
                case ELEVATE:
                    securityContext.restorePermissions();
                    break;
            }
        }
        securityContext = null;
    }

    private enum Action {
        PROC_USER, ELEVATE;
    }
}