/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.editorarea.renderer.impl;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.components.tab.TabboxStaticSection;
import com.hybris.cockpitng.components.tab.TabboxWithStatic;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.AbstractTab;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EditorArea;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.baseeditorarea.DefaultEditorAreaController;
import com.hybris.cockpitng.widgets.common.AbstractWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.ProxyRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.dynamicforms.ComponentsVisitor;
import com.hybris.cockpitng.widgets.common.dynamicforms.ComponentsVisitorFactory;
import com.hybris.cockpitng.widgets.editorarea.renderer.EditorAreaBeforeTabRenderLogicHandler;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Components;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tab;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanel;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Tabs;

/**
 * Default renderer for editor area. Uses configurable renderer for tabs.
 */
public class DefaultEditorAreaRenderer extends AbstractEditorAreaComponentRenderer<EditorArea, Object>
{
    public static final String DEFAULT_HEIGHT = "100%";
    protected TypeFacade typeFacade;
    public static final String TAB_ID_ATTRIBUTE = "tabID";
    public static final String TAB_INITIALIZED_FLAG = "_initialized";
    public static final String ON_TAB_SELECTED_EVENT = "onTabSelected";
    private static final String SCLASS_TABBOX = "yw-editorarea-tabbox";
    private static final String SCLASS_TABBOX_ACCORDION = "yw-editorarea-tabbox-accordion";
    private static final String SCLASS_TABS = "yw-editorarea-tabbox-tabs";
    private static final String SCLASS_TAB = "yw-editorarea-tabbox-tabs-tab";
    private static final String SCLASS_TAB_LABEL = "yw-editorarea-tablabel";
    private static final String SCLASS_TAB_PINNED = "yw-editorarea-tab-pinned";
    private static final String SCLASS_PIN_DIV = "yw-editorarea-tab-pin-div";
    private static final String SCLASS_TABPANELS = "yw-editorarea-tabbox-tabpanels";
    private static final String SCLASS_TABPANEL = "yw-editorarea-tabbox-tabpanels-tabpanel";
    private static final String MOLD_DEFAULT = "default";
    private static final String MOLD_ACCORDION = "accordion";
    private WidgetComponentRenderer<Component, AbstractTab, Object> editorAreaTabRenderer;
    private ComponentsVisitorFactory componentsVisitorFactory;
    private EditorAreaBeforeTabRenderLogicHandler editorAreaBeforeTabRenderLogicHandler;


    @Override
    public void render(final Component parent, final EditorArea editorAreaConfiguration, final Object object,
                    final DataType dataType, final WidgetInstanceManager wim)
    {
        final ComponentsVisitor visitor = componentsVisitorFactory.createVisitor(getCurrentTypeCode(dataType, object), wim);
        final List<AbstractTab> tabs = editorAreaConfiguration.getCustomTabOrTab();
        if(isRenderWithoutTabName(editorAreaConfiguration))
        {
            renderOnlyOneTabContent(parent, editorAreaConfiguration, object, dataType, wim);
            visitor.register(parent);
        }
        else if(CollectionUtils.isNotEmpty(tabs))
        {
            final TabboxWithStatic tabbox = new TabboxWithStatic();
            tabbox.setParent(parent);
            tabbox.setHeight(DEFAULT_HEIGHT);
            tabbox.setSclass(SCLASS_TABBOX);
            if(wim.getWidgetSettings().getBoolean(DefaultEditorAreaController.SETTING_MOLD_ACCORDION))
            {
                tabbox.setMold(MOLD_ACCORDION);
                UITools.modifySClass(tabbox, SCLASS_TABBOX_ACCORDION, true);
            }
            else
            {
                tabbox.setMold(MOLD_DEFAULT);
                UITools.modifySClass(tabbox, SCLASS_TABBOX_ACCORDION, false);
            }
            final ProxyRenderer<Component, EditorArea, Object> proxyRenderer = new ProxyRenderer<>(this, parent,
                            editorAreaConfiguration, object);
            wim.getModel().setValue(DefaultEditorAreaController.EDITOR_AREA_TAB_IS_DYNAMIC, hasConfiguredCustomTab(tabs, object));
            final List<AbstractTab> visibleTabs = tabs.stream().filter(tab -> getEditorAreaBeforeTabRenderLogicHandler().canRender(tab, object))
                            .collect(Collectors.toList());
            final Tabs tabsComponent = renderTabs(visibleTabs, proxyRenderer, dataType, wim);
            tabsComponent.setParent(tabbox);
            tabsComponent.setSclass(SCLASS_TABS);
            fireComponentRendered(tabsComponent, parent, editorAreaConfiguration, object);
            final Tabpanels tabPanels = new Tabpanels();
            tabPanels.setParent(tabbox);
            tabPanels.setSclass(SCLASS_TABPANELS);
            for(final AbstractTab abstractTab : visibleTabs)
            {
                final Tabpanel tabPanel = new Tabpanel();
                tabPanel.setParent(tabPanels);
                tabPanel.setSclass(SCLASS_TABPANEL);
                YTestTools.modifyYTestId(tabPanel, abstractTab.getName());
                tabPanel.addEventListener(ON_TAB_SELECTED_EVENT, event -> {
                    if(Boolean.TRUE.equals(tabPanel.getAttribute(TAB_INITIALIZED_FLAG)))
                    {
                        visitor.unRegister(tabPanel);
                        Components.removeAllChildren(tabPanel);
                    }
                    final Object currentObject = getCurrentObject(wim);
                    proxyRenderer.render(getEditorAreaTabRenderer(), tabPanel, abstractTab, currentObject, dataType, wim);
                    tabPanel.setAttribute(TAB_INITIALIZED_FLAG, Boolean.TRUE);
                    visitor.register(tabPanel);
                    fireComponentRendered(tabPanel, parent, editorAreaConfiguration, object);
                });
            }
            tabbox.addEventListener(Events.ON_SELECT, new EventListener<SelectEvent<Tab, Object>>()
            {
                @Override
                public void onEvent(final SelectEvent<Tab, Object> event)
                {
                    String tabName = null;
                    if(event.getSelectedItems().size() == 1)
                    {
                        tabName = (String)event.getSelectedItems().iterator().next().getAttribute(TAB_ID_ATTRIBUTE);
                    }
                    sendRenderEvent(tabbox, tabPanels);
                    setPinnedTab(wim, tabName, tabsComponent);
                }
            });
            final String lastPinned = wim.getModel().getValue(DefaultEditorAreaController.MODEL_LAST_PINNED_TAB, String.class);
            if(lastPinned != null)
            {
                setPinnedTab(wim, wim.getModel().getValue(DefaultEditorAreaController.MODEL_LAST_PINNED_TAB, String.class),
                                tabsComponent);
            }
            final Widgetslot staticSectionSlot = findStaticSectionSlot(wim.getWidgetslot());
            if(staticSectionSlot != null)
            {
                final TabboxStaticSection staticSection = new TabboxStaticSection();
                staticSection.appendChild(staticSectionSlot);
                tabbox.appendChild(staticSection);
                updateStaticSectionState(tabbox, staticSectionSlot);
            }
            visitor.register(parent);
            sendRenderEvent(tabbox, tabPanels);
            fireComponentRendered(tabPanels, parent, editorAreaConfiguration, object);
            fireComponentRendered(tabbox, parent, editorAreaConfiguration, object);
        }
    }


    protected boolean hasConfiguredCustomTab(final List<AbstractTab> tabs, final Object object)
    {
        final List<AbstractTab> configuredCustomTabs = tabs.stream()
                        .filter(tab -> getEditorAreaBeforeTabRenderLogicHandler().isConfiguredCustomTab(tab, object))
                        .collect(Collectors.toList());
        return !configuredCustomTabs.isEmpty();
    }


    protected Widgetslot findStaticSectionSlot(final Component rootWidgetSlot)
    {
        return rootWidgetSlot != null ? (Widgetslot)rootWidgetSlot.query(DefaultEditorAreaController.SELECTOR_STATIC_SECTION_SLOT)
                        : null;
    }


    protected Object getCurrentObject(final WidgetInstanceManager wim)
    {
        return wim.getModel().getValue(DefaultEditorAreaController.MODEL_CURRENT_OBJECT, Object.class);
    }


    protected String getCurrentTypeCode(final DataType dataType, final Object object)
    {
        return dataType != null ? dataType.getCode() : typeFacade.getType(object);
    }


    protected boolean isRenderWithoutTabName(final EditorArea editorAreaConfiguration)
    {
        return (editorAreaConfiguration.isHideTabNameIfOnlyOneVisible() && editorAreaConfiguration.getCustomTabOrTab().size() == 1);
    }


    protected void renderOnlyOneTabContent(final Component parent, final EditorArea editorAreaConfiguration, final Object object,
                    final DataType dataType, final WidgetInstanceManager widgetInstanceManager)
    {
        final Component currentParent = preserveTabboxCssStructure(parent, widgetInstanceManager);
        getEditorAreaTabRenderer().render(currentParent, editorAreaConfiguration.getCustomTabOrTab().get(0), object, dataType,
                        widgetInstanceManager);
    }


    private Component preserveTabboxCssStructure(final Component parent, final WidgetInstanceManager wim)
    {
        final Div pseudoTabbox = new Div();
        pseudoTabbox.setSclass(SCLASS_TABBOX);
        pseudoTabbox.setHeight(DEFAULT_HEIGHT);
        final Div pseudoTabBoxContent = new Div();
        pseudoTabBoxContent.setSclass("z-tabbox-content");
        final Div pseudoTabPanels = new Div();
        pseudoTabPanels.setSclass("yw-editorarea-tabbox-tabpanels z-tabpanels");
        final Div pseudoTabPanel = new Div();
        pseudoTabPanel.setSclass("yw-editorarea-tabbox-tabpanels-tabpanel z-tabpanel-content z-tabpanel");
        parent.appendChild(pseudoTabbox);
        final Div contentWrapper = new Div();
        contentWrapper.setSclass("z-tabbox-content-wrapper");
        contentWrapper.appendChild(pseudoTabBoxContent);
        pseudoTabbox.appendChild(contentWrapper);
        pseudoTabBoxContent.appendChild(pseudoTabPanels);
        pseudoTabPanels.appendChild(pseudoTabPanel);
        final Widgetslot staticSectionSlot = findStaticSectionSlot(wim.getWidgetslot());
        if(staticSectionSlot != null)
        {
            final TabboxStaticSection staticSection = new TabboxStaticSection();
            staticSection.appendChild(staticSectionSlot);
            pseudoTabBoxContent.appendChild(staticSection);
            updateStaticSectionState(pseudoTabbox, staticSectionSlot);
        }
        return pseudoTabPanel;
    }


    protected void updateStaticSectionState(final HtmlBasedComponent tabbox, final Widgetslot staticSectionSlot)
    {
        if(tabbox != null && staticSectionSlot != null && staticSectionSlot.getWidgetInstance() != null)
        {
            UITools.modifySClass(tabbox, DefaultEditorAreaController.YW_STATIC_SECTION_ACTIVE, true);
            UITools.modifySClass(staticSectionSlot, DefaultEditorAreaController.YW_STATIC_SECTION_ACTIVE, true);
            UITools.modifySClass(tabbox, DefaultEditorAreaController.YW_STATIC_SECTION_INACTIVE, false);
            UITools.modifySClass(staticSectionSlot, DefaultEditorAreaController.YW_STATIC_SECTION_INACTIVE, false);
        }
    }


    private void sendRenderEvent(final Tabbox tabbox, final Tabpanels tabPanels)
    {
        final int selectedTabIndex = tabbox.getSelectedIndex();
        if(selectedTabIndex >= 0)
        {
            final Tabpanel selectedTabPanel = (Tabpanel)tabPanels.getChildren().get(selectedTabIndex);
            if(selectedTabPanel != null && !Boolean.TRUE.equals(selectedTabPanel.getAttribute(TAB_INITIALIZED_FLAG)))
            {
                Events.sendEvent(ON_TAB_SELECTED_EVENT, selectedTabPanel, null);
            }
        }
    }


    protected WidgetComponentRenderer<Tabs, AbstractTab, Object> createTabRenderer()
    {
        return new AbstractWidgetComponentRenderer<Tabs, AbstractTab, Object>()
        {
            @Override
            public void render(final Tabs tabsComponent, final AbstractTab abstractTab, final Object o, final DataType dataType,
                            final WidgetInstanceManager widgetInstanceManager)
            {
                final Tab tab = new Tab();
                final Caption caption = new Caption();
                final Label label = new Label(resolveLabel(abstractTab.getName()));
                label.setSclass(SCLASS_TAB_LABEL);
                caption.appendChild(label);
                tab.appendChild(caption);
                fireComponentRendered(caption, tabsComponent, abstractTab, o);
                tab.setSclass(SCLASS_TAB);
                if(abstractTab.getTooltipText() != null)
                {
                    tab.setTooltiptext(resolveLabel(abstractTab.getTooltipText()));
                }
                else
                {
                    tab.setTooltiptext(label.getValue());
                }
                YTestTools.modifyYTestId(tab, abstractTab.getName() + "_head");
                tabsComponent.appendChild(tab);
                final Div pinDiv = new Div();
                pinDiv.setSclass(SCLASS_PIN_DIV);
                caption.appendChild(pinDiv);
                final String tabId = abstractTab.getName();
                tab.setAttribute(TAB_ID_ATTRIBUTE, tabId);
                tab.setAttribute(ComponentsVisitor.COMPONENT_CTX, abstractTab);
                pinDiv.addEventListener(Events.ON_CLICK, event -> {
                    if(tab.isSelected())
                    {
                        String lastPinned = widgetInstanceManager.getModel().getValue(DefaultEditorAreaController.MODEL_LAST_PINNED_TAB,
                                        String.class);
                        if(tabId.equals(lastPinned))
                        {
                            lastPinned = null;
                        }
                        else
                        {
                            lastPinned = tabId;
                        }
                        setPinnedTab(widgetInstanceManager, lastPinned, tabsComponent);
                    }
                });
                fireComponentRendered(tab, tabsComponent, abstractTab, o);
            }
        };
    }


    /**
     * Renders Tabs for the editor area.
     */
    protected Tabs renderTabs(final List<AbstractTab> tabs, final ProxyRenderer<Component, EditorArea, Object> proxyRenderer,
                    final DataType dataType, final WidgetInstanceManager wim)
    {
        final Tabs tabsComponent = new Tabs();
        final WidgetComponentRenderer<Tabs, AbstractTab, Object> tabRenderer = createTabRenderer();
        for(final AbstractTab abstractTab : tabs)
        {
            proxyRenderer.render(tabRenderer, tabsComponent, abstractTab, proxyRenderer.getData(), dataType, wim);
        }
        return tabsComponent;
    }


    protected void setPinnedTab(final WidgetInstanceManager widgetInstanceManager, final String pinned,
                    final Component tabsComponent)
    {
        widgetInstanceManager.getModel().setValue(DefaultEditorAreaController.MODEL_LAST_PINNED_TAB, pinned);
        tabsComponent.getChildren().stream().filter(tabChild -> tabChild instanceof Tab).forEach(tabChild -> {
            if(StringUtils.equals(pinned, String.valueOf(tabChild.getAttribute(TAB_ID_ATTRIBUTE))))
            {
                ((Tab)tabChild).setSclass(SCLASS_TAB + " " + SCLASS_TAB_PINNED);
                ((Tab)tabChild).setSelected(true);
            }
            else
            {
                ((Tab)tabChild).setSclass(SCLASS_TAB);
            }
        });
    }


    protected WidgetComponentRenderer<Component, AbstractTab, Object> getEditorAreaTabRenderer()
    {
        return editorAreaTabRenderer;
    }


    @Required
    public void setEditorAreaTabRenderer(final WidgetComponentRenderer<Component, AbstractTab, Object> editorAreaTabRenderer)
    {
        this.editorAreaTabRenderer = editorAreaTabRenderer;
    }


    protected EditorAreaBeforeTabRenderLogicHandler getEditorAreaBeforeTabRenderLogicHandler()
    {
        return editorAreaBeforeTabRenderLogicHandler;
    }


    @Required
    public void setEditorAreaBeforeTabRenderLogicHandler(final EditorAreaBeforeTabRenderLogicHandler editorAreaBeforeTabRenderLogicHandler)
    {
        this.editorAreaBeforeTabRenderLogicHandler = editorAreaBeforeTabRenderLogicHandler;
    }


    @Required
    public void setComponentsVisitorFactory(final ComponentsVisitorFactory componentsVisitorFactory)
    {
        this.componentsVisitorFactory = componentsVisitorFactory;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }
}
