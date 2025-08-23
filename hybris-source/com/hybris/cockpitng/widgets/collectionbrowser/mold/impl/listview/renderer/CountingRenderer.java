/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.listview.renderer;

import com.hybris.cockpitng.common.logic.AssetsCalculator;
import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;

public class CountingRenderer extends AbstractWidgetComponentRenderer<Listcell, ListColumn, Object>
{
    private static final String ASSETS_DELIMITER = ",";
    private static final String ASSETS_DELIMITER_SPLIT_REGEXP = "(?<!\\\\)" + Pattern.quote(ASSETS_DELIMITER);
    private static final String ASSETS_DELIMITER_ESCAPED_REGEXP = "\\\\" + ASSETS_DELIMITER;
    private String counterFormat;
    private AssetsCalculator assetsCalculator;


    @Override
    public void render(final Listcell parent, final ListColumn configuration, final Object data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final List<String> assetsGroups = Arrays.asList(configuration.getQualifier().split(ASSETS_DELIMITER_SPLIT_REGEXP)).stream()
                        .map(group -> group.replaceAll(ASSETS_DELIMITER_ESCAPED_REGEXP, ASSETS_DELIMITER)).collect(Collectors.toList());
        final int numberOfAssets = getAssetsCalculator().calculateAssets(assetsGroups, data);
        final String numberOfAssetsFormatted = String.format(getCounterFormat(), Integer.valueOf(numberOfAssets));
        final Label label = new Label(String.valueOf(numberOfAssetsFormatted));
        parent.appendChild(label);
        fireComponentRendered(parent, configuration, data);
    }


    public AssetsCalculator getAssetsCalculator()
    {
        return assetsCalculator;
    }


    @Required
    public void setAssetsCalculator(final AssetsCalculator assetsCalculator)
    {
        this.assetsCalculator = assetsCalculator;
    }


    public void setCounterFormat(final String counterFormat)
    {
        this.counterFormat = counterFormat;
    }


    public String getCounterFormat()
    {
        return counterFormat;
    }
}
