package com.hybris.datahub.runtime.domain;

import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface DataHubFeed extends Serializable
{
    public static final String _TYPECODE = "DataHubFeed";


    @NotNull
    long getFeedId();


    void setFeedId(long paramLong);


    @NotNull
    @Size(max = 255)
    String getName();


    void setName(String paramString);


    @Size(max = 255)
    String getDescription();


    void setDescription(String paramString);


    @Size(max = 255)
    String getPoolingStrategy();


    void setPoolingStrategy(String paramString);


    @Size(max = 255)
    String getPoolingCondition();


    void setPoolingCondition(String paramString);


    @Size(max = 255)
    String getDefaultCompositionStrategy();


    void setDefaultCompositionStrategy(String paramString);


    @Size(max = 255)
    String getDefaultPublicationStrategy();


    void setDefaultPublicationStrategy(String paramString);


    Set<DataHubPool> getPools();


    @Deprecated(since = "ages", forRemoval = true)
    void setPools(Set<DataHubPool> paramSet);


    void addPool(DataHubPool paramDataHubPool);
}
