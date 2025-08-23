package com.hybris.datahub.domain;

import com.hybris.datahub.runtime.domain.CanonicalItemPublicationStatus;
import com.hybris.datahub.runtime.domain.CompositionAction;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface ManagedCanonicalItem extends ManagedBaseDataItem
{
    public static final String _TYPECODE = "CanonicalItem";


    @NotNull
    long getCanonicalItemId();


    void setCanonicalItemId(long paramLong);


    @NotNull
    CompositionStatusType getStatus();


    void setStatus(CompositionStatusType paramCompositionStatusType);


    @Size(max = 255)
    String getCompositionStatusDetail();


    void setCompositionStatusDetail(String paramString);


    @Size(max = 255)
    String getIntegrationKey();


    void setIntegrationKey(String paramString);


    @Size(max = 255)
    String getBatchId();


    void setBatchId(String paramString);


    @Size(max = 36)
    String getTraceId();


    void setTraceId(String paramString);


    @Size(max = 36)
    String getUuid();


    void setUuid(String paramString);


    @Size(max = 255)
    String getDocumentId();


    void setDocumentId(String paramString);


    Set<CanonicalItemPublicationStatus> getPublicationStatuses();


    @Deprecated(since = "ages", forRemoval = true)
    void setPublicationStatuses(Set<CanonicalItemPublicationStatus> paramSet);


    void addPublicationStatus(CanonicalItemPublicationStatus paramCanonicalItemPublicationStatus);


    Set<ManagedRawItem> getRawItems();


    @Deprecated(since = "ages", forRemoval = true)
    void setRawItems(Set<ManagedRawItem> paramSet);


    void addRawItem(ManagedRawItem paramManagedRawItem);


    CompositionAction getCompositionAction();


    void setCompositionAction(CompositionAction paramCompositionAction);
}
