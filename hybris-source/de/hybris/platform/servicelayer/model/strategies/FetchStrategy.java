package de.hybris.platform.servicelayer.model.strategies;

import java.io.Serializable;

public interface FetchStrategy extends Serializable
{
    boolean needsFetch(String paramString);


    boolean isMutable();
}
