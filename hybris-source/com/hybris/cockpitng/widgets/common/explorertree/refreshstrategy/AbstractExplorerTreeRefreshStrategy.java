/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.explorertree.refreshstrategy;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.tree.node.DynamicNode;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * An abstract implementation of {@link ExplorerTreeRefreshStrategy} that provides most common implementation of some
 * methods and additional convenient ones.
 */
public abstract class AbstractExplorerTreeRefreshStrategy implements ExplorerTreeRefreshStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractExplorerTreeRefreshStrategy.class);
    private ObjectFacade objectFacade;


    @Override
    public void refreshNode(final NavigationNode node)
    {
        if(node instanceof DynamicNode)
        {
            final DynamicNode dynamicNodeToUpdate = (DynamicNode)node;
            final Object data = dynamicNodeToUpdate.getData();
            if(data != null)
            {
                dynamicNodeToUpdate.setData(reloadItemModel(data));
            }
            node.setChildren(Collections.emptyList());
        }
    }


    protected Object reloadItemModel(final Object modelToRefresh)
    {
        try
        {
            return objectFacade.reload(modelToRefresh);
        }
        catch(final ObjectNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error occurred while reloading item model", e);
            }
            return modelToRefresh;
        }
    }


    protected ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    @Required
    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }
}
