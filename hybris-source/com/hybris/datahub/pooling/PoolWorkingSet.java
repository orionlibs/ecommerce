package com.hybris.datahub.pooling;

import com.google.common.collect.ImmutableList;
import com.hybris.datahub.api.event.WorkingSet;
import com.hybris.datahub.domain.CompositionStatusType;
import com.hybris.datahub.domain.RawItemStatusType;
import com.hybris.datahub.domain.TargetSystem;
import com.hybris.datahub.model.CanonicalItem;
import com.hybris.datahub.model.RawItem;
import com.hybris.datahub.runtime.domain.CompositionAction;
import com.hybris.datahub.runtime.domain.DataHubPool;
import com.hybris.datahub.runtime.domain.DataLoadingAction;
import java.util.List;
import java.util.Optional;

public interface PoolWorkingSet extends WorkingSet
{
    DataHubPool getPool();


    Optional<CompositionAction> getCompositionAction();


    void resetCanonicalItemPage(String paramString);


    ImmutableList<CanonicalItem> getNextCanonicalItemPage(String paramString, TargetSystem paramTargetSystem, CompositionStatusType... paramVarArgs);


    @Deprecated(since = "ages", forRemoval = true)
    <T extends RawItem> ImmutableList<T> getNextRawItemPage(Class<T> paramClass, RawItemStatusType... paramVarArgs);


    ImmutableList<RawItem> getNextRawItemPage(String paramString, RawItemStatusType... paramVarArgs);


    List<DataLoadingAction> getActionList();
}
