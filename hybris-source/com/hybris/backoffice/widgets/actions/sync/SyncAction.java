/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.actions.sync;

import com.google.common.collect.Lists;
import com.hybris.backoffice.sync.facades.SynchronizationFacade;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import com.hybris.cockpitng.util.type.BackofficeTypeUtils;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.core.model.ItemModel;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;

public class SyncAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, List>
{
    protected static final String SOCKET_OUT_SELECTED_OBJECTS = "currentObjects";
    private static final String ITEM_TYPE_CODE = "Item";
    @Resource
    private ObjectFacade objectFacade;
    @Resource
    private SynchronizationFacade synchronizationFacade;
    @Resource
    private BackofficeTypeUtils backofficeTypeUtils;
    @Resource
    private TypeFacade typeFacade;


    @Override
    public boolean canPerform(final ActionContext<Object> ctx)
    {
        if(ctx.getData() != null)
        {
            final List<Object> data = getData(ctx);
            if(areAllInstancesItemTypeSubtype(data))
            {
                if(!areCatalogVersionAssigned(data))
                {
                    return false;
                }
                return CollectionUtils.isNotEmpty(data) && data.stream().noneMatch(getObjectFacade()::isModified)
                                && data.stream().anyMatch(elem -> canSync((ItemModel)elem));
            }
            else
            {
                return false;
            }
        }
        return false;
    }


    protected boolean canSync(final ItemModel elem)
    {
        final Optional<CatalogVersionModel> optionalCatalogVersion = getSynchronizationFacade()
                        .getSyncCatalogVersion(Collections.singleton(elem));
        if(optionalCatalogVersion.isPresent())
        {
            final CatalogVersionModel catalogVersion = optionalCatalogVersion.get();
            final List<SyncItemJobModel> inbound = getSynchronizationFacade().getInboundSynchronizations(catalogVersion);
            final boolean canSync = inbound.stream().anyMatch(sync -> getSynchronizationFacade().canSync(sync));
            if(canSync)
            {
                return true;
            }
            final List<SyncItemJobModel> outbound = getSynchronizationFacade().getOutboundSynchronizations(catalogVersion);
            return outbound.stream().anyMatch(sync -> getSynchronizationFacade().canSync(sync));
        }
        return false;
    }


    @Override
    public ActionResult<List> perform(final ActionContext<Object> context)
    {
        ActionResult<List> result = new ActionResult<>(ActionResult.ERROR);
        if(context.getData() != null)
        {
            sendOutput(SOCKET_OUT_SELECTED_OBJECTS, getData(context));
            result = new ActionResult<>(ActionResult.SUCCESS);
        }
        return result;
    }


    protected boolean areAllInstancesItemTypeSubtype(final List<Object> data)
    {
        return data.stream().allMatch(o -> {
            final String type = getTypeFacade().getType(o);
            return getBackofficeTypeUtils().isAssignableFrom(ITEM_TYPE_CODE, type);
        });
    }


    protected boolean areCatalogVersionAssigned(final List<Object> data)
    {
        final Optional<CatalogVersionModel> syncCatalogVersion = getSynchronizationFacade()
                        .getSyncCatalogVersion(data.stream().map(o -> (ItemModel)o).collect(Collectors.toList()));
        return syncCatalogVersion.isPresent();
    }


    protected List<Object> getData(final ActionContext<Object> context)
    {
        if(context.getData() instanceof Collection)
        {
            final Collection<Object> data = (Collection)context.getData();
            return data.stream().filter(o -> !Objects.isNull(o)).collect(Collectors.toList());
        }
        else if(context.getData() != null)
        {
            return Lists.newArrayList(context.getData());
        }
        return Collections.emptyList();
    }


    protected ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    protected SynchronizationFacade getSynchronizationFacade()
    {
        return synchronizationFacade;
    }


    protected BackofficeTypeUtils getBackofficeTypeUtils()
    {
        return backofficeTypeUtils;
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }
}
