package com.hybris.datahub.runtime.domain;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface DataHubPublicationError
{
    public static final String _TYPECODE = "DataHubPublicationError";


    @NotNull
    TargetSystemPublication getTargetSystemPublication();


    void setTargetSystemPublication(TargetSystemPublication paramTargetSystemPublication);


    @NotNull
    String getMessage();


    void setMessage(String paramString);


    @NotNull
    @Size(max = 255)
    String getCode();


    void setCode(String paramString);


    CanonicalItemPublicationStatus getCanonicalItemPublicationStatus();


    void setCanonicalItemPublicationStatus(CanonicalItemPublicationStatus paramCanonicalItemPublicationStatus);
}
