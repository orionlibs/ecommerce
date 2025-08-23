package com.hybris.datahub.runtime.domain;

import java.util.Set;
import javax.validation.constraints.NotNull;

public interface PublicationAction extends DataHubPoolAction
{
    public static final String _TYPECODE = "PublicationAction";


    Set<TargetSystemPublication> getTargetSystemPublications();


    @Deprecated(since = "ages", forRemoval = true)
    void setTargetSystemPublications(Set<TargetSystemPublication> paramSet);


    void addTargetSystemPublication(TargetSystemPublication paramTargetSystemPublication);


    @NotNull
    PublicationActionStatusType getStatus();


    void setStatus(PublicationActionStatusType paramPublicationActionStatusType);
}
