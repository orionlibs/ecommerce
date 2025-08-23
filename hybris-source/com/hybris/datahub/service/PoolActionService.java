package com.hybris.datahub.service;

import com.hybris.datahub.model.CanonicalItem;
import com.hybris.datahub.paging.DataHubPage;
import com.hybris.datahub.paging.DataHubPageable;
import com.hybris.datahub.runtime.domain.CompositionAction;
import com.hybris.datahub.runtime.domain.CompositionActionStatusType;
import com.hybris.datahub.runtime.domain.DataHubPool;
import com.hybris.datahub.runtime.domain.DataHubPoolAction;
import com.hybris.datahub.runtime.domain.PublicationAction;
import com.hybris.datahub.runtime.domain.PublicationActionStatusType;
import com.hybris.datahub.runtime.domain.TargetSystemPublication;
import java.util.List;
import javax.validation.constraints.NotNull;

public interface PoolActionService
{
    List<DataHubPoolAction> findDataHubPoolActions(DataHubPool paramDataHubPool);


    @Deprecated(since = "ages", forRemoval = true)
    List<PublicationAction> findPublicationActions(DataHubPool paramDataHubPool);


    @NotNull
    DataHubPage<PublicationAction> findPublicationActions(DataHubPageable paramDataHubPageable, DataHubPool paramDataHubPool);


    List<CompositionAction> findCompositionActions(DataHubPool paramDataHubPool);


    CompositionAction findCompositionAction(long paramLong);


    @NotNull
    DataHubPage<CompositionAction> findCompositionActions(DataHubPageable paramDataHubPageable, CompositionActionStatusType... paramVarArgs);


    PublicationAction findPublicationAction(long paramLong);


    TargetSystemPublication findTargetSystemPublication(long paramLong);


    @NotNull
    DataHubPage<PublicationAction> findPublicationActions(DataHubPageable paramDataHubPageable, PublicationActionStatusType... paramVarArgs);


    @NotNull
    DataHubPage<CanonicalItem> findCompositionActionErrors(long paramLong, DataHubPageable paramDataHubPageable);
}
