/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import com.hybris.cockpitng.compare.ItemComparisonFacade;
import com.hybris.cockpitng.config.compareview.jaxb.CompareView;
import com.hybris.cockpitng.config.compareview.jaxb.Header;
import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import com.hybris.cockpitng.widgets.common.AbstractImageBoxRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.compare.CompareViewController;
import com.hybris.cockpitng.widgets.util.WidgetRenderingUtils;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Button;

public class DefaultCompareViewBoxRenderer extends AbstractImageBoxRenderer<Header>
{
    private static final String PREVIEW_SUFFIX_COMPARE = "compare";
    private static final String SCLASS_YW_COMPARE_VIEW_TILE_NARROW = "yw-compareview-tile-narrow";
    private static final String SCLASS_YW_COMPARE_VIEW_COMPOUND_PANEL = "yw-compareview-compound-panel";
    private static final String SCLASS_YW_COMPARE_VIEW_COMPOUND_CONTAINER = "yw-compareview-compound-container";
    private static final String SCLASS_CELL_HYPERLINK = "yw-compareview-hyperlink";
    private static final String SCLASS_YE_TEXT_BUTTON = "ye-text-button";
    private static final String ATTRIBUTE_ITEM = "compareview-header-item";
    private static final String ATTRIBUTE_ELEMENT_NAME = "compareview-header-element";
    private static final String ELEMENT_NAME_PIN = "pin";
    private static final String ELEMENT_NAME_REMOVE = "remove";
    private static final String MARK_DATA = "mark-data-";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCompareViewBoxRenderer.class);
    private WidgetComponentRenderer<HtmlBasedComponent, Header, Object> pinRenderer;
    private WidgetComponentRenderer<HtmlBasedComponent, Header, Object> removeItemRenderer;
    private WidgetRenderingUtils widgetRenderingUtils;
    private ItemComparisonFacade itemComparisonFacade;
    private TypeFacade typeFacade;
    private NotificationService notificationService;
    private ElementConfigurationProvider elementConfigurationProvider;


    @Override
    public void render(final HtmlBasedComponent parent, final Header configuration, final Object element, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        Validate.notNull("All arguments must be non-null", parent, configuration, element, dataType, widgetInstanceManager);
        if(requiresRerendering(parent, configuration, element, dataType, widgetInstanceManager))
        {
            parent.getChildren().clear();
        }
        else
        {
            detachChildrenToRerender(parent);
        }
        appendPin(parent, configuration, element, dataType, widgetInstanceManager);
        appendRemoveItemIcon(parent, configuration, element, dataType, widgetInstanceManager);
        if(dataType.getClazz().isInstance(element))
        {
            applyAdditionalRenderers(parent, configuration, element, dataType, widgetInstanceManager);
            appendThumbnailAndPopupPreview(element, configuration, dataType, parent, widgetInstanceManager);
        }
        else
        {
            try
            {
                final String elementTypeCode = getTypeFacade().getType(element);
                final DataType elementDataType = getTypeFacade().load(elementTypeCode);
                final CompareView elementConfiguration = getElementConfigurationProvider().getConfiguration(widgetInstanceManager,
                                elementTypeCode);
                applyAdditionalRenderers(parent, elementConfiguration.getHeader(), element, elementDataType, widgetInstanceManager);
                appendThumbnailAndPopupPreview(element, elementConfiguration.getHeader(), elementDataType, parent,
                                widgetInstanceManager);
            }
            catch(final TypeNotFoundException e)
            {
                LOGGER.error(e.getLocalizedMessage(), e);
                final String source = getNotificationService().getWidgetNotificationSource(widgetInstanceManager);
                getNotificationService().notifyUser(source, NotificationEventTypes.EVENT_TYPE_GENERAL,
                                NotificationEvent.Level.FAILURE, e);
            }
            catch(final CockpitConfigurationException e)
            {
                LOGGER.error(e.getLocalizedMessage(), e);
            }
        }
        appendLabel(element, parent, configuration);
        parent.setAttribute(ATTRIBUTE_ITEM, element);
        fireComponentRendered(parent, configuration, element);
    }


    protected boolean requiresRerendering(final HtmlBasedComponent parent, final Header configuration, final Object element,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        return !getItemComparisonFacade().isEqualItem(parent.getAttribute(ATTRIBUTE_ITEM), element);
    }


    protected void detachChildrenToRerender(final Component parent)
    {
        final List<Component> toRemove = parent.getChildren().stream().filter(child -> !child.hasAttribute(ATTRIBUTE_ELEMENT_NAME))
                        .collect(Collectors.toList());
        parent.getChildren().removeAll(toRemove);
    }


    protected void appendPin(final HtmlBasedComponent parent, final Header configuration, final Object element,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        appendConstantElementIfRequired(parent, ELEMENT_NAME_PIN, CompareViewController.MARK_NAME_PIN,
                        () -> getPinRenderer().render(parent, configuration, element, dataType, widgetInstanceManager), element);
    }


    protected void appendRemoveItemIcon(final HtmlBasedComponent parent, final Header configuration, final Object element,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        appendConstantElementIfRequired(parent, ELEMENT_NAME_REMOVE, CompareViewController.MARK_NAME_REMOVE_ITEM,
                        () -> getRemoveItemRenderer().render(parent, configuration, element, dataType, widgetInstanceManager), element);
    }


    protected void appendConstantElementIfRequired(final Component parent, final String elementName, final Executable appendix)
    {
        if(!isConstantElementRendered(parent, elementName))
        {
            final int childrenCountWithoutAppendix = parent.getChildren().size();
            appendix.execute();
            parent.getChildren().stream().skip(childrenCountWithoutAppendix)
                            .forEach(child -> child.setAttribute(ATTRIBUTE_ELEMENT_NAME, elementName));
        }
    }


    /**
     * @deprecated since 1808 - please use {@link #appendConstantElementIfRequired(Component, String, Executable)}
     */
    @Deprecated(since = "1808", forRemoval = true)
    protected void appendConstantElementIfRequired(final Component parent, final String elementName, final String markName,
                    final Executable appendix, final Object element)
    {
        appendConstantElementIfRequired(parent, elementName, appendix);
    }


    protected boolean isConstantElementRendered(final Component parent, final String elementName)
    {
        return parent.getChildren().stream().map(child -> child.getAttribute(ATTRIBUTE_ELEMENT_NAME)).filter(Objects::nonNull)
                        .anyMatch(owner -> owner.equals(elementName));
    }


    /**
     * @deprecated since 1808 - not used anymore
     */
    @Deprecated(since = "1808", forRemoval = true)
    protected boolean isMarkedObjectUpToDate(final Component parent, final String markName, final Object element)
    {
        return parent.getChildren().stream().map(child -> child.getAttribute(MARK_DATA + markName)).filter(Objects::nonNull)
                        .anyMatch(owner -> owner == element);
    }


    @Override
    protected void appendLabel(final Object object, final HtmlBasedComponent tile, final Header configuration)
    {
        final String title = getLabelService().getObjectLabel(object);
        final Button button = new Button(title);
        button.setTooltiptext(title);
        button.setAutodisable("self");
        getWidgetRenderingUtils().markComponent(tile, button, CompareViewController.MARK_NAME_HYPERLINK, object);
        UITools.addSClass(button, SCLASS_YE_TEXT_BUTTON);
        UITools.addSClass(button, SCLASS_CELL_HYPERLINK);
        tile.appendChild(button);
        fireComponentRendered(button, tile, configuration, object);
    }


    @Override
    protected List<WidgetComponentRenderer> getAdditionalRenderers(final Header configuration)
    {
        return configuration.getAdditionalRenderer().stream().map(renderer -> getBean(renderer.getSpringBean()))
                        .collect(Collectors.toList());
    }


    @Override
    protected String getSClassForBoxWithAdditionalRenderers()
    {
        return SCLASS_YW_COMPARE_VIEW_TILE_NARROW;
    }


    @Override
    protected String getSClassForAdditionalPanel()
    {
        return SCLASS_YW_COMPARE_VIEW_COMPOUND_PANEL;
    }


    @Override
    protected String getSClassForAdditionalPanelInnerComponent()
    {
        return SCLASS_YW_COMPARE_VIEW_COMPOUND_CONTAINER;
    }


    @Override
    protected String getPreviewSuffix()
    {
        return PREVIEW_SUFFIX_COMPARE;
    }


    protected WidgetComponentRenderer<HtmlBasedComponent, Header, Object> getPinRenderer()
    {
        return pinRenderer;
    }


    @Required
    public void setPinRenderer(final WidgetComponentRenderer<HtmlBasedComponent, Header, Object> pinRenderer)
    {
        this.pinRenderer = pinRenderer;
    }


    protected WidgetComponentRenderer<HtmlBasedComponent, Header, Object> getRemoveItemRenderer()
    {
        return removeItemRenderer;
    }


    @Required
    public void setRemoveItemRenderer(final WidgetComponentRenderer<HtmlBasedComponent, Header, Object> removeItemRenderer)
    {
        this.removeItemRenderer = removeItemRenderer;
    }


    protected WidgetRenderingUtils getWidgetRenderingUtils()
    {
        return widgetRenderingUtils;
    }


    @Required
    public void setWidgetRenderingUtils(final WidgetRenderingUtils widgetRenderingUtils)
    {
        this.widgetRenderingUtils = widgetRenderingUtils;
    }


    protected ItemComparisonFacade getItemComparisonFacade()
    {
        return itemComparisonFacade;
    }


    @Required
    public void setItemComparisonFacade(final ItemComparisonFacade itemComparisonFacade)
    {
        this.itemComparisonFacade = itemComparisonFacade;
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    protected ElementConfigurationProvider getElementConfigurationProvider()
    {
        return elementConfigurationProvider;
    }


    @Required
    public void setElementConfigurationProvider(final ElementConfigurationProvider elementConfigurationProvider)
    {
        this.elementConfigurationProvider = elementConfigurationProvider;
    }
}
