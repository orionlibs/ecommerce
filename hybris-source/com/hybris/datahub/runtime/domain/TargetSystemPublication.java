package com.hybris.datahub.runtime.domain;

import com.hybris.datahub.domain.ManagedTargetItem;
import com.hybris.datahub.domain.TargetSystem;
import java.util.Date;
import java.util.Set;
import javax.validation.constraints.NotNull;

public interface TargetSystemPublication
{
    public static final String _TYPECODE = "TargetSystemPublication";


    @NotNull
    Long getPublicationId();


    void setPublicationId(Long paramLong);


    PublicationType getPublicationType();


    void setPublicationType(PublicationType paramPublicationType);


    Date getStartTime();


    void setStartTime(Date paramDate);


    Date getEndTime();


    void setEndTime(Date paramDate);


    @NotNull
    PublicationActionStatusType getStatus();


    void setStatus(PublicationActionStatusType paramPublicationActionStatusType);


    PublicationAction getPublicationAction();


    void setPublicationAction(PublicationAction paramPublicationAction);


    TargetSystem getTargetSystem();


    void setTargetSystem(TargetSystem paramTargetSystem);


    @Deprecated(since = "ages", forRemoval = true)
    void setTargetItems(Set<ManagedTargetItem> paramSet);


    void addTargetItem(ManagedTargetItem paramManagedTargetItem);


    Set<DataHubPublicationError> getErrors();


    @Deprecated(since = "ages", forRemoval = true)
    void setErrors(Set<DataHubPublicationError> paramSet);


    void addError(DataHubPublicationError paramDataHubPublicationError);


    @Deprecated(since = "ages", forRemoval = true)
    Set<CanonicalItemPublicationStatus> getCanonicalItemPublicationStatuses();


    @Deprecated(since = "ages", forRemoval = true)
    void setCanonicalItemPublicationStatuses(Set<CanonicalItemPublicationStatus> paramSet);


    void addCanonicalItemPublicationStatus(CanonicalItemPublicationStatus paramCanonicalItemPublicationStatus);
}
