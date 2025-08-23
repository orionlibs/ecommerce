package com.hybris.datahub.runtime.domain;

import java.io.Serializable;
import java.util.Set;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public interface DataHubPool extends Serializable
{
    public static final String _TYPECODE = "DataHubPool";


    @NotNull
    Long getPoolId();


    void setPoolId(Long paramLong);


    @NotNull
    @Size(max = 255)
    String getPoolName();


    void setPoolName(String paramString);


    @Size(max = 255)
    String getCompositionStrategy();


    void setCompositionStrategy(String paramString);


    @Size(max = 255)
    String getPublicationStrategy();


    void setPublicationStrategy(String paramString);


    Set<DataHubFeed> getFeeds();


    @Deprecated(since = "ages", forRemoval = true)
    void setFeeds(Set<DataHubFeed> paramSet);


    void addFeed(DataHubFeed paramDataHubFeed);


    boolean isDeletable();


    void setDeletable(boolean paramBoolean);
}
