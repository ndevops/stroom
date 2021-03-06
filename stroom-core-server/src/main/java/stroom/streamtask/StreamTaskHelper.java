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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stroom.node.shared.Node;
import stroom.persist.EntityManagerSupport;
import stroom.streamtask.shared.ProcessorFilterTask;
import stroom.streamtask.shared.TaskStatus;

import javax.inject.Inject;
import javax.persistence.EntityNotFoundException;

// @Transactional
class StreamTaskHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(StreamTaskHelper.class);

    private final StreamTaskService streamTaskService;
    private final EntityManagerSupport entityManagerSupport;

    @Inject
    StreamTaskHelper(final StreamTaskService streamTaskService,
                     final EntityManagerSupport entityManagerSupport) {
        this.streamTaskService = streamTaskService;
        this.entityManagerSupport = entityManagerSupport;
    }

    ProcessorFilterTask changeTaskStatus(final ProcessorFilterTask streamTask, final Node node, final TaskStatus status,
                                         final Long startTime, final Long endTime) {
        return entityManagerSupport.transactionResult(em -> {
            LOGGER.debug("changeTaskStatus() - Changing task status of {} to node={}, status={}", streamTask, node, status);
            final long now = System.currentTimeMillis();

            ProcessorFilterTask result = null;

            try {
                try {
                    modify(streamTask, node, status, now, startTime, endTime);
                    result = streamTaskService.save(streamTask);
                } catch (final EntityNotFoundException e) {
                    LOGGER.warn("changeTaskStatus() - Task cannot be found {}", streamTask);
                } catch (final RuntimeException e) {
                    // Try this operation a few times.
                    boolean success = false;
                    RuntimeException lastError = null;

                    // Try and do this up to 100 times.
                    for (int tries = 0; tries < 100 && !success; tries++) {
                        success = true;

                        try {
                            if (LOGGER.isDebugEnabled()) {
                                LOGGER.warn("changeTaskStatus() - {} - Task has changed, attempting reload {}", e.getMessage(), streamTask, e);
                            } else {
                                LOGGER.warn("changeTaskStatus() - Task has changed, attempting reload {}", streamTask);
                            }

                            final ProcessorFilterTask loaded = streamTaskService.load(streamTask);
                            if (loaded == null) {
                                LOGGER.warn("changeTaskStatus() - Failed to reload task {}", streamTask);
                            } else if (TaskStatus.DELETED.equals(loaded.getStatus())) {
                                LOGGER.warn("changeTaskStatus() - Task has been deleted {}", streamTask);
                            } else {
                                LOGGER.warn("changeTaskStatus() - Loaded stream task {}", loaded);
                                modify(loaded, node, status, now, startTime, endTime);
                                result = streamTaskService.save(loaded);
                            }
                        } catch (final EntityNotFoundException e2) {
                            LOGGER.warn("changeTaskStatus() - Failed to reload task as it cannot be found {}", streamTask);
                        } catch (final RuntimeException e2) {
                            success = false;
                            lastError = e2;
                            // Wait before trying this operation again.
                            Thread.sleep(1000);
                        }
                    }

                    if (!success) {
                        LOGGER.error("Error changing task status for task '{}': {}", streamTask, lastError.getMessage(), lastError);
                    }
                }
            } catch (final InterruptedException e) {
                LOGGER.error(e.getMessage(), e);

                // Continue to interrupt this thread.
                Thread.currentThread().interrupt();
            }

            return result;
        });
    }

    private void modify(final ProcessorFilterTask streamTask, final Node node, final TaskStatus status, final Long statusMs,
                        final Long startTimeMs, final Long endTimeMs) {
        streamTask.setNode(node);
        streamTask.setStatus(status);
        streamTask.setStatusMs(statusMs);
        streamTask.setStartTimeMs(startTimeMs);
        streamTask.setEndTimeMs(endTimeMs);
    }
}
