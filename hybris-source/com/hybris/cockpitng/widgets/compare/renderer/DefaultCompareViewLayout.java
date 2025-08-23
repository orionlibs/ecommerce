/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare.renderer;

import com.hybris.cockpitng.components.table.Table;
import com.hybris.cockpitng.components.table.TableRows;
import com.hybris.cockpitng.components.table.TableRowsGroup;
import com.hybris.cockpitng.components.table.iterator.TableComponentIterator;
import com.hybris.cockpitng.config.compareview.jaxb.CompareView;
import com.hybris.cockpitng.config.compareview.jaxb.Section;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.impl.IdentifiableMarkEventConsumer;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.compare.model.CompareViewData;
import com.hybris.cockpitng.widgets.compare.model.PartialRendererData;
import com.hybris.cockpitng.widgets.compare.model.impl.DefaultPartialRendererData;
import com.hybris.cockpitng.widgets.summaryview.CustomRendererClassUtil;
import com.hybris.cockpitng.widgets.util.WidgetRenderingUtils;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;

public class DefaultCompareViewLayout extends AbstractWidgetComponentRenderer<HtmlBasedComponent, CompareView, CompareViewData>
                implements CompareViewLayout
{
    private static final String MARK_EXPAND_TRIGGER = "expand-trigger";
    private static final String ATTRIBUTE_COLLAPSED = "section-collapsed";
    private static final String SETTING_ACCORDION_MODE = "accordionMode";
    private static final String ACCORDION_MODE_LISTENER = "accordion-mode-listener";
    private static final String SCLASS_COMPAREVIEW_EMPTY = "yw-compareview-empty";
    private static final String SCLASS_COMPAREVIEW_DIFFERENT_MARK = "yw-compareview-different-mark";
    private static final String SCLASS_COMPAREVIEW_IN_PROGRESS = "yw-compareview-in-progress";
    private static final String SCLASS_CELL_CONTENT = "yw-compareview-cell-content";
    private WidgetComponentRenderer<TableRows, CompareView, PartialRendererData<Collection>> headerRenderer;
    private WidgetComponentRenderer<TableRowsGroup, Section, PartialRendererData<Collection>> sectionRenderer;
    private WidgetRenderingUtils widgetRenderingUtils;
    private boolean isInitiallyOpenConfigurationEnabled = true;


    /**
     * Marks table cell as the one containing data not equal to reference
     *
     * @param component
     *           cell containing not equal data
     */
    public static void markAsNotEqual(final HtmlBasedComponent component)
    {
        UITools.addSClass(component, SCLASS_COMPAREVIEW_DIFFERENT_MARK);
    }


    /**
     * Marks table cell as the one containing data equal to reference
     *
     * @param component
     *           cell containing equal data
     */
    public static void markAsEqual(final HtmlBasedComponent component)
    {
        UITools.removeSClass(component, SCLASS_COMPAREVIEW_DIFFERENT_MARK);
    }


    /**
     * Marks component as container for cell data
     *
     * @param component
     *           component containing cell data
     */
    public static void markAsContentsContainer(final HtmlBasedComponent component)
    {
        UITools.addSClass(component, SCLASS_CELL_CONTENT);
    }


    /**
     * Marks table cell as the one containing data that may change in near future due to ongoing calculation in background.
     *
     * @param component
     *           cell containing data that is an object of ongoing calculation
     */
    public static void markAsDuringCalculation(final HtmlBasedComponent component)
    {
        UITools.addSClass(component, SCLASS_COMPAREVIEW_IN_PROGRESS);
    }


    /**
     * Marks table cell as the one containing data that will not change in near future due to ongoing calculation in
     * background.
     *
     * @param component
     *           cell containing data that is already calculated
     */
    public static void markAsCalculated(final HtmlBasedComponent component)
    {
        UITools.removeSClass(component, SCLASS_COMPAREVIEW_IN_PROGRESS);
    }


    @Override
    public void render(final HtmlBasedComponent component, final CompareView configuration, final CompareViewData compareViewData,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Table tableComponent = getTableComponent(component);
        try
        {
            tableComponent.setFrozen(true);
            final PartialRendererData<Collection> rendererData = createPartialData(compareViewData);
            renderHeader(component, configuration, rendererData, dataType, widgetInstanceManager);
            fireComponentRendered(tableComponent.getHeaderRows(), component, configuration, compareViewData);
            final TableRows sections = tableComponent.getRows();
            final TableComponentIterator<TableRowsGroup> sectionsIterator = sections.groupsIterator();
            configuration.getSection().forEach(section -> {
                final TableRowsGroup rowsGroup = sectionsIterator.request();
                setSectionStatus(rowsGroup, section, configuration, widgetInstanceManager);
                renderSection(component, rowsGroup, configuration, section, rendererData, dataType, widgetInstanceManager);
                fireComponentRendered(rowsGroup, component, configuration, compareViewData);
                getWidgetRenderingUtils().registerMarkedComponentsListener(rowsGroup.getHeaderRow(), MARK_EXPAND_TRIGGER,
                                Events.ON_CLICK, new IdentifiableMarkEventConsumer(ACCORDION_MODE_LISTENER, event -> {
                                    setInitiallyOpenConfigurationEnabled(false);
                                    if(widgetInstanceManager.getWidgetSettings().getBoolean(SETTING_ACCORDION_MODE))
                                    {
                                        collapseOtherSections(rowsGroup, sections);
                                    }
                                }));
            });
            sectionsIterator.removeRemaining();
            tableComponent.getRows().rowsIterator().removeRemaining();
            UITools.modifySClass(component, SCLASS_COMPAREVIEW_EMPTY, compareViewData.getReference() == null);
            fireComponentRendered(tableComponent, component, configuration, compareViewData);
            fireComponentRendered(component, configuration, compareViewData);
        }
        finally
        {
            tableComponent.setFrozen(false);
        }
    }


    @Override
    public void onUpdateItemType()
    {
        setInitiallyOpenConfigurationEnabled(true);
    }


    protected void setSectionStatus(final TableRowsGroup sectionGroup, final Section section,
                    final CompareView configuration, final WidgetInstanceManager widgetInstanceManager)
    {
        final boolean isCongfigurationEnable = isInitiallyOpenConfigurationEnabled();
        if(isCongfigurationEnable && getInitiallyOpenedSections(configuration, widgetInstanceManager).contains(section.getName()))
        {
            sectionGroup.setAttribute(ATTRIBUTE_COLLAPSED, false);
            sectionGroup.setCollapsed(false);
        }
        else if(isCongfigurationEnable)
        {
            sectionGroup.setAttribute(ATTRIBUTE_COLLAPSED, true);
            sectionGroup.setCollapsed(true);
        }
    }


    protected Set<String> getInitiallyOpenedSections(final CompareView configuration, final WidgetInstanceManager widgetInstanceManager)
    {
        final boolean accordionMode = widgetInstanceManager.getWidgetSettings().getBoolean(SETTING_ACCORDION_MODE);
        final List<Section> sections = configuration.getSection();
        Set<String> initiallyOpenedSections = new HashSet<>();
        for(Section section : sections)
        {
            if(accordionMode)
            {
                if(section.isInitiallyOpened())
                {
                    initiallyOpenedSections.add(section.getName());
                    break;
                }
            }
            else
            {
                if(section.isInitiallyOpened())
                {
                    initiallyOpenedSections.add(section.getName());
                }
            }
        }
        return initiallyOpenedSections;
    }


    protected void collapseOtherSections(final TableRowsGroup currentSection, final TableRows sections)
    {
        sections.groupsIterator().forEachRemaining(section -> {
            if(!section.equals(currentSection))
            {
                section.setCollapsed(true);
                section.setAttribute(ATTRIBUTE_COLLAPSED, true);
            }
        });
    }


    protected Table getTableComponent(final HtmlBasedComponent container)
    {
        final Component firstChild = container.getFirstChild();
        if(firstChild instanceof Table)
        {
            return (Table)firstChild;
        }
        else
        {
            final Table table = new Table();
            container.getChildren().add(0, table);
            return table;
        }
    }


    protected PartialRendererData<Collection> createPartialData(final CompareViewData compareViewData)
    {
        final DefaultPartialRendererData<Collection> partialRendererData = new DefaultPartialRendererData<>(
                        compareViewData.getComparisonResult(), compareViewData.getTargets(), compareViewData.getComparisonState());
        partialRendererData.setDiffOnly(compareViewData.isDiffOnly());
        return partialRendererData;
    }


    protected void renderHeader(final HtmlBasedComponent parent, final CompareView configuration,
                    final PartialRendererData<Collection> data, final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Table tableComponent = getTableComponent(parent);
        final ProxyRenderer<Component, CompareView, PartialRendererData<Collection>> proxyRenderer = new ProxyRenderer(this, parent,
                        configuration, data);
        proxyRenderer.render(getHeaderRenderer(), tableComponent.getHeaderRows(), configuration, data, dataType,
                        widgetInstanceManager);
    }


    protected void renderSection(final HtmlBasedComponent parent, final TableRowsGroup sectionGroup, final CompareView compareView,
                    final Section section, final PartialRendererData<Collection> compareViewData, final DataType dataType,
                    final WidgetInstanceManager widgetInstanceManager)
    {
        final ProxyRenderer<Component, CompareView, CompareViewData> proxyRenderer = new ProxyRenderer(this, parent, compareView,
                        compareViewData);
        proxyRenderer.render(getSectionRenderer(section), sectionGroup, section, compareViewData, dataType, widgetInstanceManager);
    }


    protected WidgetComponentRenderer<TableRows, CompareView, PartialRendererData<Collection>> getHeaderRenderer()
    {
        return headerRenderer;
    }


    @Required
    public void setHeaderRenderer(
                    final WidgetComponentRenderer<TableRows, CompareView, PartialRendererData<Collection>> headerRenderer)
    {
        this.headerRenderer = headerRenderer;
    }


    protected WidgetComponentRenderer<TableRowsGroup, Section, PartialRendererData<Collection>> getSectionRenderer(
                    final Section sectionConfig)
    {
        if(StringUtils.isNotEmpty(sectionConfig.getRenderer()))
        {
            return CustomRendererClassUtil.createRenderer(sectionConfig.getRenderer());
        }
        else
        {
            return getSectionRenderer();
        }
    }


    protected WidgetComponentRenderer<TableRowsGroup, Section, PartialRendererData<Collection>> getSectionRenderer()
    {
        return sectionRenderer;
    }


    @Required
    public void setSectionRenderer(
                    final WidgetComponentRenderer<TableRowsGroup, Section, PartialRendererData<Collection>> sectionRenderer)
    {
        this.sectionRenderer = sectionRenderer;
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


    public boolean isInitiallyOpenConfigurationEnabled()
    {
        return isInitiallyOpenConfigurationEnabled;
    }


    public void setInitiallyOpenConfigurationEnabled(final boolean initiallyOpenConfigurationEnabled)
    {
        isInitiallyOpenConfigurationEnabled = initiallyOpenConfigurationEnabled;
    }
}
