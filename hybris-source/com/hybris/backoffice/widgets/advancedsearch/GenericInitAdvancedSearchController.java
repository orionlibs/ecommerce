/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.advancedsearch;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchData;
import com.hybris.backoffice.widgets.advancedsearch.impl.AdvancedSearchInitContext;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.advancedsearch.AdvancedSearch;
import com.hybris.cockpitng.tree.node.DynamicNode;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zkplus.spring.SpringUtil;

public class GenericInitAdvancedSearchController extends AbstractInitAdvancedSearchAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(GenericInitAdvancedSearchController.class);


    @Override
    @SocketEvent(socketId = SOCKET_IN_NODE_SELECTED)
    public void createAdvancedSearchInitContext(final NavigationNode navigationNode)
    {
        if(navigationNode == null)
        {
            LOG.warn("navigationNode is null");
            return;
        }
        if(canHandleNavigationNode(navigationNode))
        {
            final AdvancedSearch config = loadAdvancedConfiguration(getTypeCode());
            final AdvancedSearchData searchData = initFromConfig(config, getTypeCode());
            addSearchDataConditions(searchData, Optional.of(navigationNode));
            sendOutput(getOutputSocketName(), new AdvancedSearchInitContext(searchData, config));
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.debug(navigationNode.getId() + " doesn't match the required id: " + getNavigationNodeId());
        }
    }


    protected boolean canHandleNavigationNode(final NavigationNode navigationNode)
    {
        return (StringUtils.equals(getNavigationNodeId(), navigationNode.getId())
                        || (navigationNode instanceof DynamicNode && navigationNode.getId().startsWith(getNavigationNodeId())));
    }


    @Override
    public void addSearchDataConditions(final AdvancedSearchData searchData, final Optional<NavigationNode> navigationNode)
    {
        final String beanId = getWidgetSettings().getString("handlerBeanId");
        if(StringUtils.isNotBlank(beanId))
        {
            final Object bean = SpringUtil.getBean(beanId, AdvancedSearchInitializer.class);
            if(bean instanceof AdvancedSearchInitializer)
            {
                ((AdvancedSearchInitializer)bean).addSearchDataConditions(searchData, navigationNode);
                return;
            }
        }
        LOG.warn("Could not find matching bean of id: {}", beanId);
    }


    @Override
    public String getNavigationNodeId()
    {
        return getWidgetSettings().getString("navigationNodeId");
    }


    @Override
    public String getTypeCode()
    {
        return getWidgetSettings().getString("requiredTypeCode");
    }
}
