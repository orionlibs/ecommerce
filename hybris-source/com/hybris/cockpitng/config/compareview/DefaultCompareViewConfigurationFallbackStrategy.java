/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.config.compareview;

import com.hybris.cockpitng.config.compareview.jaxb.CompareView;
import com.hybris.cockpitng.config.compareview.jaxb.EditorAreaCtx;
import com.hybris.cockpitng.config.compareview.jaxb.GridViewCtx;
import com.hybris.cockpitng.core.config.ConfigContext;
import com.hybris.cockpitng.core.config.impl.AbstractCockpitConfigurationFallbackStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultCompareViewConfigurationFallbackStrategy extends AbstractCockpitConfigurationFallbackStrategy<CompareView>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCompareViewConfigurationFallbackStrategy.class);


    @Override
    public CompareView loadFallbackConfiguration(final ConfigContext context, final Class<CompareView> configurationType)
    {
        LOG.debug("Fallback for {} has been called", DefaultCompareViewConfigurationFallbackStrategy.class);
        final EditorAreaCtx editorAreaCtx = new EditorAreaCtx();
        final GridViewCtx gridViewCtx = new GridViewCtx();
        final CompareView compareView = new CompareView();
        compareView.setEditorAreaCtx(editorAreaCtx);
        compareView.setGridViewCtx(gridViewCtx);
        return compareView;
    }
}
