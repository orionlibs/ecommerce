package com.hybris.datahub.service;

import com.hybris.datahub.domain.CompositionStatusType;
import com.hybris.datahub.model.CanonicalItem;
import com.hybris.datahub.model.CanonicalItemPublicationStatusDetail;
import com.hybris.datahub.model.RawItem;
import com.hybris.datahub.paging.DataHubIdBasedPageable;
import com.hybris.datahub.paging.DataHubPage;
import com.hybris.datahub.runtime.domain.CanonicalItemPublicationStatus;
import com.hybris.datahub.runtime.domain.CanonicalItemPublicationStatusType;
import com.hybris.datahub.runtime.domain.CompositionAction;
import com.hybris.datahub.runtime.domain.DataHubPool;
import com.hybris.datahub.runtime.domain.DataHubPublicationError;
import com.hybris.datahub.runtime.domain.TargetSystemPublication;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.validation.constraints.NotNull;

public interface CanonicalItemService
{
    @Deprecated(since = "ages", forRemoval = true)
    <T extends CanonicalItem> T newCanonicalItem(T paramT, Class<T> paramClass);


    CanonicalItem newCanonicalItem(CanonicalItem paramCanonicalItem);


    @Deprecated(since = "ages", forRemoval = true)
    List<? extends CanonicalItem> findCanonicalItems(DataHubPool paramDataHubPool, String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    <T extends CanonicalItem> DataHubPage<T> findCanonicalItems(DataHubPool paramDataHubPool, Class<T> paramClass, DataHubIdBasedPageable paramDataHubIdBasedPageable, CompositionStatusType... paramVarArgs);


    @NotNull
    DataHubPage<CanonicalItem> findCanonicalItems(DataHubPool paramDataHubPool, String paramString, DataHubIdBasedPageable paramDataHubIdBasedPageable, CompositionStatusType... paramVarArgs);


    @NotNull
    DataHubPage<CanonicalItem> findComposedCanonicalItems(DataHubPool paramDataHubPool, String paramString, DataHubIdBasedPageable paramDataHubIdBasedPageable, CompositionStatusType... paramVarArgs);


    @NotNull
    DataHubPage<CanonicalItem> findComposedCanonicalItems(DataHubPool paramDataHubPool, String paramString, DataHubIdBasedPageable paramDataHubIdBasedPageable, Optional<CompositionAction> paramOptional, CompositionStatusType... paramVarArgs);


    @Deprecated(since = "ages", forRemoval = true)
    String calculateIntegrationKey(String paramString, CanonicalItem paramCanonicalItem);


    @Deprecated(since = "ages", forRemoval = true)
    String calculateIntegrationKeyFromRawItem(String paramString, RawItem paramRawItem);


    @Deprecated(since = "ages", forRemoval = true)
    <T extends CanonicalItem> Collection<T> findItemsByIntegrationKey(Class<T> paramClass, String paramString, DataHubPool paramDataHubPool);


    Collection<? extends CanonicalItem> findItemsByIntegrationKey(String paramString1, String paramString2, DataHubPool paramDataHubPool);


    @Deprecated(since = "ages", forRemoval = true)
    <T extends CanonicalItem> T findLatestValidItem(Class<T> paramClass, String paramString, DataHubPool paramDataHubPool);


    CanonicalItem findLatestValidItem(String paramString1, String paramString2, DataHubPool paramDataHubPool);


    @Deprecated(since = "ages", forRemoval = true)
    <T extends CanonicalItem> T findLatestValidItem(Class<T> paramClass, Map<String, ?> paramMap, DataHubPool paramDataHubPool);


    CanonicalItem findLatestValidItem(String paramString, Map<String, ?> paramMap, DataHubPool paramDataHubPool);


    @Deprecated(since = "ages", forRemoval = true)
    <T extends CanonicalItem> T merge(List<T> paramList, Optional<CompositionAction> paramOptional);


    CanonicalItemMergeResult merge(CanonicalItem paramCanonicalItem, Optional<CompositionAction> paramOptional);


    void setCompositionStatus(List<? extends CanonicalItem> paramList, CompositionStatusType paramCompositionStatusType);


    CanonicalItemPublicationStatus setTargetSystemPublicationStatus(CanonicalItem paramCanonicalItem, TargetSystemPublication paramTargetSystemPublication, CanonicalItemPublicationStatusType paramCanonicalItemPublicationStatusType,
                    CanonicalItemPublicationStatusDetail paramCanonicalItemPublicationStatusDetail, Set<DataHubPublicationError> paramSet);


    @Deprecated(since = "6.2", forRemoval = true)
    int deleteByIntegrationKey(String paramString1, String paramString2, DataHubPool paramDataHubPool);


    @Deprecated(since = "6.2", forRemoval = true)
    int deleteByPrimaryKey(String paramString, Map<String, ?> paramMap, DataHubPool paramDataHubPool);


    @Deprecated(since = "6.2", forRemoval = true)
    int deleteAll(String paramString, DataHubPool paramDataHubPool);


    <T extends CanonicalItem> T getCanonicalItem(Long paramLong);
}
