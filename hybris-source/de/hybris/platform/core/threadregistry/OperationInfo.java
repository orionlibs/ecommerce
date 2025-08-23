package de.hybris.platform.core.threadregistry;

import com.google.common.annotations.Beta;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

public final class OperationInfo
{
    private final Map<Object, Object> infos;


    private OperationInfo()
    {
        this.infos = Maps.newHashMap();
    }


    private OperationInfo(Map<Object, Object> infos)
    {
        this.infos = Maps.newHashMap(infos);
    }


    private OperationInfo(OperationInfo parent, Map<Object, Object> additionalInfos)
    {
        this.infos = Maps.newHashMap(parent.infos);
        this.infos.putAll(additionalInfos);
    }


    public static OperationInfo empty()
    {
        return new OperationInfo();
    }


    public static OperationInfoBuilder builder()
    {
        return new OperationInfoBuilder();
    }


    public static RevertibleUpdate updateThread(@Nonnull OperationInfo info)
    {
        Objects.requireNonNull(info, "info mustn't be null");
        return updateThread(info, SuspendResumeServices.getInstance().getThreadRegistry());
    }


    @Beta
    public static OperationInfo current()
    {
        return SuspendResumeServices.getInstance().getThreadRegistry().getCurrentOperationInfo();
    }


    static RevertibleUpdate updateThread(OperationInfo info, ThreadRegistry threadRegistry)
    {
        return new RevertibleUpdate(threadRegistry.update(info), threadRegistry);
    }


    @Beta
    public boolean isJunitOperation()
    {
        return Boolean.TRUE.equals(this.infos.get(StandardAttributes.IS_JUNIT_THREAD));
    }


    @Beta
    public boolean isInitOrUpdate()
    {
        return Category.INIT_OR_UPDATE.toString().equals(getCategory());
    }


    @Beta
    public String getCategory()
    {
        return getAttribute(StandardAttributes.CATEGORY);
    }


    @Beta
    public String getTenantId()
    {
        return getAttribute(StandardAttributes.TENANT_ID);
    }


    @Beta
    public String getCronJobCode()
    {
        return getAttribute(StandardAttributes.CRON_JOB_CODE);
    }


    @Beta
    public String getBusinessProcessCode()
    {
        return getAttribute(StandardAttributes.BUSINESS_PROCESS_CODE);
    }


    @Beta
    public String getAspectName()
    {
        return getAttribute(StandardAttributes.ASPECT_NAME);
    }


    boolean canBeSuspended()
    {
        Object suspendable = this.infos.get(StandardAttributes.SUSPENDABLE);
        return (suspendable == null || Boolean.TRUE.equals(suspendable));
    }


    boolean canHandleDbFailures()
    {
        Object canHandleDbFailures = this.infos.get(StandardAttributes.CAN_HANDLE_DB_FAILURES);
        return Boolean.TRUE.equals(canHandleDbFailures);
    }


    OperationInfo merge(@Nonnull OperationInfo updateInfo)
    {
        Objects.requireNonNull(updateInfo, "updateInfo must not be null");
        return new OperationInfo(this, updateInfo.infos);
    }


    boolean wasCreatedForThread(Long threadId)
    {
        return threadId.equals(this.infos.get(StandardAttributes.THREAD_ID));
    }


    Map<Object, Object> getAllAttributes()
    {
        return new HashMap<>(this.infos);
    }


    <T> T getAttribute(Object key)
    {
        return (T)this.infos.get(key);
    }


    public String toString()
    {
        MoreObjects.ToStringHelper resultBuilder = MoreObjects.toStringHelper(getClass());
        this.infos.forEach((k, v) -> resultBuilder.add(k.toString(), Objects.toString(v, "<CLEARED>")));
        return resultBuilder.toString();
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof OperationInfo))
        {
            return false;
        }
        OperationInfo that = (OperationInfo)o;
        return this.infos.equals(that.infos);
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.infos});
    }
}
