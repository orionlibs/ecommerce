package com.hybris.datahub.domain;

import com.hybris.datahub.runtime.domain.DataLoadingAction;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface ManagedRawItem extends ManagedBaseDataItem
{
    public static final String _TYPECODE = "ManagedRawItem";


    @Size(max = 255)
    String getIsoCode();


    void setIsoCode(String paramString);


    @Size(max = 255)
    String getExtensionSource();


    void setExtensionSource(String paramString);


    DataLoadingAction getDataLoadingAction();


    void setDataLoadingAction(DataLoadingAction paramDataLoadingAction);


    RawItemStatusType getStatus();


    void setStatus(RawItemStatusType paramRawItemStatusType);


    boolean isDelete();


    void setDelete(boolean paramBoolean);


    @NotNull
    long getRawItemId();


    void setRawItemId(long paramLong);


    Set<ManagedCanonicalItem> getCanonicalItems();


    @Deprecated(since = "ages", forRemoval = true)
    void setCanonicalItems(Set<ManagedCanonicalItem> paramSet);


    void addCanonicalItem(ManagedCanonicalItem paramManagedCanonicalItem);


    @Size(max = 255)
    String getBatchId();


    void setBatchId(String paramString);


    @Size(max = 36)
    String getTraceId();


    void setTraceId(String paramString);


    @Size(max = 36)
    String getUuid();


    void setUuid(String paramString);
}
