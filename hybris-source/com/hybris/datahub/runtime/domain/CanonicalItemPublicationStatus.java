package com.hybris.datahub.runtime.domain;

import com.hybris.datahub.domain.ManagedCanonicalItem;
import com.hybris.datahub.util.AttributelessCloneable;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface CanonicalItemPublicationStatus extends AttributelessCloneable
{
    public static final String _TYPECODE = "CanonicalItemPublicationStatus";


    @NotNull
    TargetSystemPublication getTargetSystemPublication();


    void setTargetSystemPublication(TargetSystemPublication paramTargetSystemPublication);


    @NotNull
    ManagedCanonicalItem getCanonicalItem();


    void setCanonicalItem(ManagedCanonicalItem paramManagedCanonicalItem);


    @NotNull
    CanonicalItemPublicationStatusType getStatus();


    void setStatus(CanonicalItemPublicationStatusType paramCanonicalItemPublicationStatusType);


    @Size(max = 255)
    String getStatusDetail();


    void setStatusDetail(String paramString);


    Set<DataHubPublicationError> getPublicationErrors();


    @Deprecated(since = "ages", forRemoval = true)
    void setPublicationErrors(Set<DataHubPublicationError> paramSet);


    void addPublicationError(DataHubPublicationError paramDataHubPublicationError);
}
