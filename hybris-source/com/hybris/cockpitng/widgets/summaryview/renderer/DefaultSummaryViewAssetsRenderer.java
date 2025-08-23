/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.summaryview.renderer;

import com.hybris.cockpitng.common.logic.AssetsCalculator;
import com.hybris.cockpitng.common.logic.impl.ObjectAssetsCalculator;
import com.hybris.cockpitng.config.summaryview.jaxb.Attribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.type.ObjectValueService;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.expression.EvaluationException;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class DefaultSummaryViewAssetsRenderer extends AbstractSummaryViewItemWithIconRenderer<Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSummaryViewAssetsRenderer.class);
    private static final String ICON_GROUP_ASSETS = "assets";
    private static final String LABEL_ASSETS_ATTACHED = "assets.attached";
    private static final String ASSETS_DELIMITER = ",";
    private AssetsCalculator assetsCalculator;
    private String iconSclass;
    /**
     * @deprecated since 6.5
     */
    @Deprecated(since = "6.5", forRemoval = true)
    private ObjectValueService objectValueService;


    @Override
    protected String getIconStatusSClass(final HtmlBasedComponent iconContainer, final Attribute attributeConfiguration,
                    final Object data, final DataAttribute dataAttribute, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        if(StringUtils.isEmpty(getIconSclass()))
        {
            final String qualifiers = attributeConfiguration.getQualifier().replaceAll(ASSETS_DELIMITER, "-").replaceAll("\\s", "")
                            .toLowerCase();
            return getIconStatusSClass(ICON_GROUP_ASSETS, qualifiers);
        }
        else
        {
            return getIconSclass();
        }
    }


    @Override
    protected void renderValue(final Div attributeContainer, final Attribute attributeConfiguration, final Object data,
                    final DataAttribute dataAttribute, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final List<String> assetsGroups = Arrays.asList(attributeConfiguration.getQualifier().split(ASSETS_DELIMITER));
        final int assetsAmount = getAssetsCalculator().calculateAssets(assetsGroups, data);
        renderValue(attributeContainer, assetsAmount, attributeConfiguration, widgetInstanceManager);
    }


    /**
     * @deprecated since 1811, use {@link #renderValue(Div, int, Attribute, WidgetInstanceManager)}
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected void renderValue(final Div container, final int assetsAmount, final WidgetInstanceManager widgetInstanceManager)
    {
        final Object[] arguments =
                        {String.valueOf(assetsAmount)};
        final String label = widgetInstanceManager.getLabel(LABEL_ASSETS_ATTACHED, arguments);
        final Label assetsValue = new Label(label);
        container.appendChild(assetsValue);
    }


    protected void renderValue(final Div container, final int assetsAmount, final Attribute attributeConfiguration,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        if(StringUtils.isNotEmpty(attributeConfiguration.getLabel()))
        {
            final Object[] arguments =
                            {String.valueOf(assetsAmount)};
            final String label = widgetInstanceManager.getLabel(attributeConfiguration.getLabel(), arguments);
            if(StringUtils.isEmpty(label))
            {
                renderValue(container, assetsAmount, widgetInstanceManager);
            }
            else
            {
                final Label assetsValue = new Label(label);
                container.appendChild(assetsValue);
            }
        }
        else
        {
            renderValue(container, assetsAmount, widgetInstanceManager);
        }
    }


    @Required
    public void setAssetsCalculator(final AssetsCalculator assetsCalculator)
    {
        this.assetsCalculator = assetsCalculator;
    }


    public AssetsCalculator getAssetsCalculator()
    {
        return assetsCalculator;
    }


    /**
     * @deprecated since 6.5, Please use {@link AssetsCalculator#calculateAssets(java.util.List, java.lang.Object)}
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected int calculateAssets(final String qualifier, final Object object)
    {
        return assetsCalculator.calculateAssets(Arrays.asList(qualifier.split(ASSETS_DELIMITER)), object);
    }


    /**
     * @deprecated since 6.5, Please use
     *             {@link ObjectAssetsCalculator#calculateAssetsInGroup(java.lang.String, java.lang.Object)}
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected int calculateAssetsInGroup(final String assetsGroup, final Object object)
    {
        final Optional<Object> value = tryToGetObjectValue(assetsGroup, object);
        final Function<Object, Integer> sizeMapper = o -> Integer.valueOf((o instanceof Collection) ? ((Collection)o).size() : 1);
        return value.map(sizeMapper).orElse(Integer.valueOf(0)).intValue();
    }


    /**
     * @deprecated since 6.5, Please use
     *             {@link ObjectAssetsCalculator#tryToGetObjectValue(java.lang.String, java.lang.Object)}
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected Optional<Object> tryToGetObjectValue(final String assetsGroup, final Object object)
    {
        try
        {
            return Optional.ofNullable(getObjectValueService().getValue(assetsGroup, object));
        }
        catch(final EvaluationException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error occurred", e);
            }
            return Optional.empty();
        }
    }


    /**
     * @deprecated since 6.5
     */
    @Deprecated(since = "6.5", forRemoval = true)
    @Required
    public void setObjectValueService(final ObjectValueService objectValueService)
    {
        this.objectValueService = objectValueService;
    }


    /**
     * @deprecated since 6.5
     */
    @Deprecated(since = "6.5", forRemoval = true)
    public ObjectValueService getObjectValueService()
    {
        return objectValueService;
    }


    protected String getIconSclass()
    {
        return iconSclass;
    }


    public void setIconSclass(final String iconSclass)
    {
        this.iconSclass = iconSclass;
    }
}
