package com.hybris.datahub.runtime.domain;

import java.io.Serializable;
import java.util.Date;

public interface PublicationRetry extends Serializable
{
    Long getCanonicalItemId();


    Long getTargetSystemId();


    int getRetryCount();


    Date getCreationTime();
}
