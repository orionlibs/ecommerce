package com.hybris.datahub.service;

import com.hybris.datahub.dto.publication.PublicationResult;
import com.hybris.datahub.filter.PublishedItemFilter;
import com.hybris.datahub.model.TargetItem;
import com.hybris.datahub.paging.DataHubIdBasedPageable;
import com.hybris.datahub.paging.DataHubPage;
import com.hybris.datahub.paging.DataHubPageable;
import com.hybris.datahub.runtime.domain.CanonicalItemPublicationStatus;
import com.hybris.datahub.runtime.domain.PublicationActionStatusType;
import com.hybris.datahub.runtime.domain.TargetSystemPublication;
import java.util.List;
import javax.validation.constraints.NotNull;

public interface PublicationActionService
{
    void completeTargetSystemPublication(Long paramLong, PublicationResult paramPublicationResult);


    TargetSystemPublication completeTargetSystemPublication(long paramLong, PublicationActionStatusType paramPublicationActionStatusType);


    @Deprecated(since = "6.7", forRemoval = true)
    DataHubPage<TargetItem> findByPublication(Long paramLong, String paramString, DataHubIdBasedPageable paramDataHubIdBasedPageable);


    List<TargetItem> findTargetItemsByPublication(Long paramLong, String paramString, DataHubIdBasedPageable paramDataHubIdBasedPageable);


    @NotNull
    DataHubPage<CanonicalItemPublicationStatus> findCanonicalItemPublicationStatuses(long paramLong, DataHubPageable paramDataHubPageable, PublishedItemFilter paramPublishedItemFilter);
}
