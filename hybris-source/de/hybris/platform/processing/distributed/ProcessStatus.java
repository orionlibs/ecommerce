package de.hybris.platform.processing.distributed;

import com.google.common.base.MoreObjects;
import de.hybris.platform.processing.enums.DistributedProcessState;
import java.util.Optional;
import java.util.OptionalDouble;

public class ProcessStatus
{
    private final Optional<DistributedProcessState> state;
    private final OptionalDouble progress;
    private final Optional<String> status;
    private final Optional<String> extendedStatus;


    public static Builder builder()
    {
        return new Builder();
    }


    private ProcessStatus(Builder builder)
    {
        this.state = builder.state;
        this.progress = builder.progress;
        this.status = builder.status;
        this.extendedStatus = builder.extendedStatus;
    }


    public boolean isUnknown()
    {
        return (!this.state.isPresent() && !this.progress.isPresent() && !this.status.isPresent() && !this.extendedStatus.isPresent());
    }


    public Optional<DistributedProcessState> getState()
    {
        return this.state;
    }


    public OptionalDouble getProgress()
    {
        return this.progress;
    }


    public Optional<String> getStatus()
    {
        return this.status;
    }


    public Optional<String> getExtendedStatus()
    {
        return this.extendedStatus;
    }


    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("state", this.state).add("progress", this.progress).add("status", this.status)
                        .add("extendedStatus", this.extendedStatus).toString();
    }
}
