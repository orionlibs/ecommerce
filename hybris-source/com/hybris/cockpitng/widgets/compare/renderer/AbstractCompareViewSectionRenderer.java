/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import com.hybris.cockpitng.components.table.TableRow;
import com.hybris.cockpitng.components.table.TableRowsGroup;
import com.hybris.cockpitng.config.compareview.jaxb.Attribute;
import com.hybris.cockpitng.config.compareview.jaxb.CompareView;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.impl.IdentifiableMarkEventListener;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.compare.model.PartialRendererData;
import com.hybris.cockpitng.widgets.util.WidgetRenderingUtils;
import java.util.Collection;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;

public abstract class AbstractCompareViewSectionRenderer<CONFIG, ELEMENT> extends
                AbstractWidgetComponentRenderer<TableRowsGroup, CONFIG, PartialRendererData<Collection>>
{
    public static final String MARK_LOCALIZED_ATTRIBUTE_EXPAND_TRIGGER = "localized-attribute-expand-trigger";
    public static final String MARK_EXPAND_TRIGGER = "expand-trigger";
    private static final String ATTRIBUTE_COLLAPSED = "section-collapsed";
    private static final String LISTENER_EXPAND_SECTION = "expand-section-listener";
    private static final String SCLASS_SECTION_HEADER_ROW = "yw-compareview-section-header-row";
    protected WidgetComponentRenderer<TableRow, CONFIG, PartialRendererData<Collection>> headerRenderer;
    protected WidgetComponentRenderer<TableRow, ELEMENT, PartialRendererData<Collection>> attributeRenderer;
    protected CockpitLocaleService cockpitLocaleService;
    protected CockpitUserService cockpitUserService;
    protected WidgetRenderingUtils widgetRenderingUtils;


    protected abstract String getConfiguredSectionIdentifier(final CONFIG configuration);


    protected abstract boolean isSectionContentRendered(final TableRowsGroup parent, final CONFIG configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager);


    protected abstract void renderSection(final TableRowsGroup parent, final TableRow headerRow, final CONFIG configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager);


    protected abstract void updateTableRowDifferentMark(final TableRowsGroup parent, final TableRow tableRow,
                    final ELEMENT attribute, final PartialRendererData<Collection> data);


    @Override
    public void render(final TableRowsGroup parent, final CONFIG configuration, final PartialRendererData<Collection> data,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final TableRow headerRow = parent.getHeaderRow();
        renderSectionHeader(parent, headerRow, configuration, data, dataType, widgetInstanceManager);
        sectionHeaderRendered(parent, headerRow, configuration, data, dataType, widgetInstanceManager);
        fireComponentRendered(headerRow, parent, configuration, data);
        if(!isCollapsed(parent) || isSectionContentRendered(parent, configuration, data, dataType, widgetInstanceManager))
        {
            renderSection(parent, headerRow, configuration, data, dataType, widgetInstanceManager);
        }
        else
        {
            parent.rowsIterator().removeRemaining();
        }
        fireComponentRendered(parent, configuration, data);
    }


    protected void renderSectionHeader(final TableRowsGroup parent, final TableRow row, final CONFIG configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final ProxyRenderer<TableRow, CompareView, PartialRendererData<Collection>> proxyRenderer = new ProxyRenderer(this, parent,
                        configuration, data, null);
        proxyRenderer.render(getHeaderRenderer(), row, configuration, data, dataType, widgetInstanceManager);
        UITools.addSClass(row, SCLASS_SECTION_HEADER_ROW);
    }


    protected void sectionHeaderRendered(final TableRowsGroup parent, final TableRow row, final CONFIG configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        parent.setCollapsed(isCollapsed(parent));
        getWidgetRenderingUtils().registerMarkedComponentsListener(row, MARK_EXPAND_TRIGGER, Events.ON_CLICK,
                        new IdentifiableMarkEventListener(LISTENER_EXPAND_SECTION)
                        {
                            @Override
                            public void onEvent(final Event event, final Object markData)
                            {
                                handleSectionExpandStateChangeRequested(parent, row, configuration, data, dataType, widgetInstanceManager,
                                                markData);
                            }
                        });
        updateSectionHeaderDifferentMark(configuration, row, data);
    }


    protected void updateSectionHeaderDifferentMark(final CONFIG configuration, final TableRow tableRow,
                    final PartialRendererData<Collection> data)
    {
        if(data.getComparisonResult().getGroupsWithDifferences().contains(getConfiguredSectionIdentifier(configuration)))
        {
            DefaultCompareViewLayout.markAsNotEqual(tableRow);
        }
        else
        {
            DefaultCompareViewLayout.markAsEqual(tableRow);
        }
    }


    /**
     * @deprecated since 1808, please use handleSectionExpandStateChangeRequested(TableRowsGroup, TableRow, CONFIG, PartialRendererData, DataType, WidgetInstanceManager, Object)
     */
    @Deprecated(since = "1808", forRemoval = true)
    protected void handleSectionExpandStateChangeRequested(final TableRowsGroup parent, final CONFIG configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        handleSectionExpandStateChangeRequested(parent, null, configuration, data, dataType, widgetInstanceManager, null);
    }


    protected void handleSectionExpandStateChangeRequested(final TableRowsGroup parent, final TableRow headerRow,
                    final CONFIG configuration, final PartialRendererData<Collection> data, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager, final Object markData)
    {
        final boolean newCollapsedState = !isCollapsed(parent);
        if(!newCollapsedState && !isSectionContentRendered(parent, configuration, data, dataType, widgetInstanceManager))
        {
            renderSection(parent, headerRow, configuration, data, dataType, widgetInstanceManager);
        }
        setCollapsed(parent, newCollapsedState);
    }


    protected boolean isCollapsed(final TableRowsGroup parent)
    {
        return BooleanUtils.toBooleanDefaultIfNull((Boolean)parent.getAttribute(ATTRIBUTE_COLLAPSED), true);
    }


    protected void setCollapsed(final TableRowsGroup parent, final boolean collapsed)
    {
        parent.setAttribute(ATTRIBUTE_COLLAPSED, collapsed);
        parent.setCollapsed(collapsed);
    }


    /**
     * @deprecated since 1808, please use renderSection(TableRowsGroup, TableRow, CONFIG, PartialRendererData, DataType, WidgetInstanceManager)
     */
    @Deprecated(since = "1808", forRemoval = true)
    protected void renderSection(final TableRowsGroup parent, final CONFIG configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        renderSection(parent, null, configuration, data, dataType, widgetInstanceManager);
    }


    protected void renderAttribute(final TableRowsGroup parent, final TableRow row, final ELEMENT attribute,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final ProxyRenderer<TableRow, Attribute, PartialRendererData<Collection>> proxyRenderer = new ProxyRenderer(this, parent,
                        attribute, data);
        proxyRenderer.render(getAttributeRenderer(attribute), row, attribute, data, dataType, widgetInstanceManager);
        updateTableRowDifferentMark(parent, row, attribute, data);
    }


    protected WidgetComponentRenderer<TableRow, CONFIG, PartialRendererData<Collection>> getHeaderRenderer()
    {
        return headerRenderer;
    }


    @Required
    public void setHeaderRenderer(final WidgetComponentRenderer<TableRow, CONFIG, PartialRendererData<Collection>> headerRenderer)
    {
        this.headerRenderer = headerRenderer;
    }


    protected WidgetComponentRenderer<TableRow, ELEMENT, PartialRendererData<Collection>> getAttributeRenderer(
                    final ELEMENT attributeConfig)
    {
        return getAttributeRenderer();
    }


    protected WidgetComponentRenderer<TableRow, ELEMENT, PartialRendererData<Collection>> getAttributeRenderer()
    {
        return attributeRenderer;
    }


    @Required
    public void setAttributeRenderer(
                    final WidgetComponentRenderer<TableRow, ELEMENT, PartialRendererData<Collection>> attributeRenderer)
    {
        this.attributeRenderer = attributeRenderer;
    }


    public CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    @Required
    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    public CockpitUserService getCockpitUserService()
    {
        return cockpitUserService;
    }


    @Required
    public void setCockpitUserService(final CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
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


    /**
     * @deprecated since 1808, not used any more
     */
    @Deprecated(since = "1808", forRemoval = true)
    protected class ExpandSectionListener extends IdentifiableMarkEventListener
    {
        private final TableRowsGroup parent;
        private final CONFIG configuration;
        private final PartialRendererData<Collection> data;
        private final DataType dataType;
        private final WidgetInstanceManager widgetInstanceManager;
        private TableRow headerRow;


        public ExpandSectionListener(final TableRowsGroup parent, final CONFIG configuration,
                        final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
        {
            super(LISTENER_EXPAND_SECTION);
            this.parent = parent;
            this.configuration = configuration;
            this.data = data;
            this.dataType = dataType;
            this.widgetInstanceManager = widgetInstanceManager;
        }


        public ExpandSectionListener(final TableRowsGroup parent, final TableRow headerRow, final CONFIG configuration,
                        final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
        {
            this(parent, configuration, data, dataType, widgetInstanceManager);
            this.headerRow = headerRow;
        }


        @Override
        public void onEvent(final Event event, final Object markData)
        {
            handleSectionExpandStateChangeRequested(parent, headerRow, configuration, data, dataType, widgetInstanceManager,
                            markData);
        }


        protected TableRowsGroup getParent()
        {
            return parent;
        }
    }
}
