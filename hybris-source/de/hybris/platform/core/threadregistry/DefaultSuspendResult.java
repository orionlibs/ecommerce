package de.hybris.platform.core.threadregistry;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.suspend.SuspendResult;
import de.hybris.platform.core.suspend.SystemStatus;
import java.util.Objects;

class DefaultSuspendResult implements SuspendResult
{
    private final String resumeToken;
    private final SystemStatus status;


    private DefaultSuspendResult(String resumeToken, SystemStatus status)
    {
        this.resumeToken = resumeToken;
        this.status = status;
    }


    public String getResumeToken()
    {
        return this.resumeToken;
    }


    public SystemStatus getCurrentStatus()
    {
        return this.status;
    }


    static SuspendResult systemHasBeenRequestedToSuspend(SystemStatus status, String resumeToken)
    {
        Preconditions.checkNotNull(status, "status mustn't be null");
        Preconditions.checkNotNull(resumeToken, "resumeToken mustn't be null");
        return new DefaultSuspendResult(Objects.<String>requireNonNull(resumeToken), Objects.<SystemStatus>requireNonNull(status));
    }


    static SuspendResult systemIsSuspendedOrWaiting(SystemStatus status)
    {
        Preconditions.checkNotNull(status, "status mustn't be null");
        Preconditions.checkArgument((status != SystemStatus.RUNNING), "status mustn't be RUNNING");
        return new DefaultSuspendResult(null, Objects.<SystemStatus>requireNonNull(status));
    }
}
