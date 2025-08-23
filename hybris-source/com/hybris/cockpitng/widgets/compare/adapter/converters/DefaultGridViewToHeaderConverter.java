/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.adapter.converters;

import com.hybris.cockpitng.config.compareview.jaxb.Header;
import com.hybris.cockpitng.config.compareview.jaxb.Renderer;
import com.hybris.cockpitng.core.config.impl.jaxb.gridview.GridView;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.commonconfig.Parameter;
import java.util.stream.Collectors;

public class DefaultGridViewToHeaderConverter implements CompareViewConverter<GridView, Header>
{
    private static final String OBJECT_TO_CONVERT_CAN_NOT_BE_NULL = "Object to convert can not be null";


    @Override
    public Header convert(final GridView gridView)
    {
        if(gridView == null)
        {
            throw new IllegalArgumentException(OBJECT_TO_CONVERT_CAN_NOT_BE_NULL);
        }
        final Header compareViewHeader = new Header();
        compareViewHeader.setDefaultImage(gridView.getDefaultImage());
        compareViewHeader.setDisplayDefaultImage(gridView.isDisplayDefaultImage());
        compareViewHeader.setDisplayPreview(gridView.isDisplayPreview());
        compareViewHeader.setDisplayThumbnail(gridView.isDisplayThumbnail());
        compareViewHeader.getAdditionalRenderer().addAll(gridView.getAdditionalRenderer().stream()
                        .map(DefaultGridViewToHeaderConverter::convertRenderer).collect(Collectors.toList()));
        return compareViewHeader;
    }


    private static Parameter convertParameter(
                    final com.hybris.cockpitng.core.config.impl.jaxb.gridview.Parameter gridViewParameter)
    {
        final Parameter compareViewParameter = new Parameter();
        compareViewParameter.setName(gridViewParameter.getName());
        compareViewParameter.setValue(gridViewParameter.getValue());
        return compareViewParameter;
    }


    private static Renderer convertRenderer(final com.hybris.cockpitng.core.config.impl.jaxb.gridview.Renderer gridViewRenderer)
    {
        final Renderer compareViewRenderer = new Renderer();
        compareViewRenderer.setMergeMode(gridViewRenderer.getMergeMode());
        compareViewRenderer.setSpringBean(gridViewRenderer.getSpringBean());
        compareViewRenderer.setPosition(gridViewRenderer.getPosition());
        compareViewRenderer.getParameter().addAll(gridViewRenderer.getParameter().stream()
                        .map(DefaultGridViewToHeaderConverter::convertParameter).collect(Collectors.toList()));
        return compareViewRenderer;
    }
}
