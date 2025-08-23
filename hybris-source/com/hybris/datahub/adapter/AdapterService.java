package com.hybris.datahub.adapter;

import com.hybris.datahub.api.publication.PublicationException;
import com.hybris.datahub.domain.TargetSystem;
import com.hybris.datahub.runtime.domain.TargetSystemPublication;

public interface AdapterService
{
    String getTargetSystemType();


    void publish(TargetSystemPublication paramTargetSystemPublication, String paramString) throws PublicationException;


    default boolean isTargetSystemAvailable(TargetSystem targetSystem)
    {
        return true;
    }
}
