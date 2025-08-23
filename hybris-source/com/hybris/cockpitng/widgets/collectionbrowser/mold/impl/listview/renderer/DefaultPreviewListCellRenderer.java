/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.listview.renderer;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.services.media.ObjectPreview;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.common.renderer.AbstractImageComponentRenderer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listcell;

public class DefaultPreviewListCellRenderer extends AbstractImageComponentRenderer<Listcell, ListColumn, Object>
{
    public static final Logger LOG = LoggerFactory.getLogger(DefaultPreviewListCellRenderer.class);
    public static final String SCLASS_IMG_PREVIEW_POPUP = "ye-listview-preview-popup-image";
    public static final String SCLASS_IMG_PREVIEW = "ye-listview-def-preview-img";


    @Override
    public void render(final Listcell listcell, final ListColumn configuration, final Object data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final ObjectPreview preview = getObjectPreview(configuration, data, dataType, widgetInstanceManager);
        if(preview != null)
        {
            final Image image = new Image(preview.getUrl());
            UITools.modifySClass(image, SCLASS_IMG_PREVIEW, true);
            listcell.appendChild(image);
            fireComponentRendered(image, listcell, configuration, data);
            if(!preview.isFallback())
            {
                appendPopupPreview(listcell, configuration, data, preview.getUrl(), image, SCLASS_IMG_PREVIEW_POPUP);
            }
        }
        fireComponentRendered(listcell, configuration, data);
    }


    @Override
    protected String getThumbnailSclass()
    {
        return StringUtils.EMPTY;
    }


    @Override
    protected String getPreviewPopupSclass()
    {
        return SCLASS_IMG_PREVIEW_POPUP;
    }
}
