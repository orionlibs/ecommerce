/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config.impl;

import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.collectionbrowser.CollectionBrowser;
import com.hybris.cockpitng.core.config.impl.jaxb.collectionbrowser.Mold;
import com.hybris.cockpitng.core.config.impl.jaxb.collectionbrowser.MoldList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultCollectionBrowserConfigFallbackStrategy extends
                AbstractCockpitConfigurationFallbackStrategy<CollectionBrowser>
{
    public static final String LIST_VIEW_BEAN = "listViewCollectionBrowserMoldStrategy";
    public static final String TREE_MOLD_BEAN = "treeViewCollectionBrowserMoldStrategy";
    public static final String GRID_VIEW_BEAN = "gridViewCollectionBrowserMoldStrategy";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCollectionBrowserConfigFallbackStrategy.class);


    @Override
    public CollectionBrowser loadFallbackConfiguration(final ConfigContext context,
                    final Class<CollectionBrowser> configurationType)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Fallback for {} has been called", DefaultCollectionBrowserConfigFallbackStrategy.class);
        }
        final CollectionBrowser fallbackConfig = new CollectionBrowser();
        final MoldList moldList = new MoldList();
        fallbackConfig.setAvailableMolds(moldList);
        final Mold listViewMold = new Mold();
        listViewMold.setSpringBean(LIST_VIEW_BEAN);
        moldList.getMold().add(listViewMold);
        final Mold treeViewMold = new Mold();
        treeViewMold.setSpringBean(TREE_MOLD_BEAN);
        moldList.getMold().add(treeViewMold);
        final Mold gridViewMold = new Mold();
        gridViewMold.setSpringBean(GRID_VIEW_BEAN);
        moldList.getMold().add(gridViewMold);
        return fallbackConfig;
    }
}
