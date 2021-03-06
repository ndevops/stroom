package stroom.task;

import stroom.security.util.UserTokenUtil;
import stroom.task.api.ServerTask;
import stroom.task.shared.Task;
import stroom.util.shared.VoidResult;

public final class GenericServerTask extends ServerTask<VoidResult> {
    private final String message;
    private volatile transient Runnable runnable;

    private GenericServerTask(final Task<?> parentTask, final String userToken,
                              final String taskName, final String message) {
        super(parentTask, userToken);
        this.message = message;
        setTaskName(taskName);
    }

    public static GenericServerTask create(final String taskName, final String message) {
        return new GenericServerTask(null, UserTokenUtil.INTERNAL_PROCESSING_USER_TOKEN, taskName, message);
    }

    public static GenericServerTask create(final Task<?> parentTask,
                                           final String taskName, final String message) {
        if (parentTask == null) {
            return new GenericServerTask(null, UserTokenUtil.INTERNAL_PROCESSING_USER_TOKEN, taskName, message);
        }

        return new GenericServerTask(parentTask, parentTask.getUserToken(), taskName, message);
    }

    public static GenericServerTask create(final Task<?> parentTask, final String userToken,
                                           final String taskName, final String message) {
        return new GenericServerTask(parentTask, userToken, taskName, message);
    }

    Runnable getRunnable() {
        return runnable;
    }

    public void setRunnable(final Runnable runnable) {
        this.runnable = runnable;
    }

    String getMessage() {
        return message;
    }
}
