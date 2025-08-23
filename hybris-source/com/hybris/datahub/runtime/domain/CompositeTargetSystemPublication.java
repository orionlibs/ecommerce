package com.hybris.datahub.runtime.domain;

import java.util.List;

public interface CompositeTargetSystemPublication extends TargetSystemPublication
{
    List<SubTargetSystemPublication> getSubTargetSystemPublications();


    @Deprecated(since = "ages", forRemoval = true)
    void setSubTargetSystemPublications(List<SubTargetSystemPublication> paramList);


    void addSubTargetSystemPublication(SubTargetSystemPublication paramSubTargetSystemPublication);


    long getCanonicalItemCount();


    void setCanonicalItemCount(long paramLong);


    long getIgnoredCount();


    void setIgnoredCount(long paramLong);


    long getInternalErrorCount();


    void setInternalErrorCount(long paramLong);


    long getExternalErrorCount();


    void setExternalErrorCount(long paramLong);
}
