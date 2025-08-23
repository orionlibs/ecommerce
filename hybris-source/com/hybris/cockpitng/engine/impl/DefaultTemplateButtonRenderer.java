/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.impl;

import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.WidgetInstanceSettings;
import com.hybris.cockpitng.core.WidgetSocket;
import com.hybris.cockpitng.core.persistence.WidgetPersistenceService;
import com.hybris.cockpitng.core.util.impl.WidgetSocketUtils;
import com.hybris.cockpitng.engine.TemplateButtonRenderer;
import com.hybris.cockpitng.util.WidgetTreeUIUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Popup;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Toolbarbutton;

public class DefaultTemplateButtonRenderer implements TemplateButtonRenderer
{
    private WidgetPersistenceService widgetPersistenceService;


    @Override
    public void renderTemplateButton(final Component parent, final Widget currentWidget, final WidgetDefinition widgetDefinition,
                    final Widgetslot widgetslot)
    {
        final Toolbarbutton templateBtn = createTemplateButton(parent, currentWidget, widgetslot);
        if(!currentWidget.isTemplate())
        {
            return;
        }
        final WidgetInstanceSettings settings = currentWidget.getWidgetInstanceSettings();
        final Popup templateRulesMenu = createTemplateRulesMenu(parent, currentWidget);
        final List<WidgetSocket> allInputs = WidgetSocketUtils.getAllInputs(currentWidget, widgetDefinition);
        final List<WidgetSocket> allOutputs = WidgetSocketUtils.getAllOutputs(currentWidget, widgetDefinition);
        createOnIncomingSocketModel(currentWidget, settings, templateRulesMenu, allInputs);
        closeOnIncomingSocketModel(currentWidget, settings, templateRulesMenu, allInputs);
        closeOnOutgoingSocketModel(currentWidget, settings, templateRulesMenu, allOutputs);
        selectOnIncomingSocketModel(currentWidget, settings, templateRulesMenu, allInputs);
        Executions.createComponents("templatePopup.zul", templateRulesMenu, Collections.emptyMap());
        templateBtn.addEventListener(Events.ON_RIGHT_CLICK, event -> templateRulesMenu.open(templateBtn));
    }


    protected Toolbarbutton createTemplateButton(final Component parent, final Widget currentWidget, final Widgetslot widgetslot)
    {
        final Toolbarbutton templateBtn = new Toolbarbutton();
        templateBtn.setParent(parent);
        templateBtn.setSclass(currentWidget.isTemplate() //
                        ? "templateBtn templateBtn_activated" //
                        : "templateBtn templateBtn_deactivated");
        templateBtn.addEventListener(Events.ON_CLICK, event -> {
            currentWidget.setTemplate(!currentWidget.isTemplate());
            getWidgetPersistenceService().storeWidgetTree(currentWidget);
            final Widgetslot parentWidgetslot = WidgetTreeUIUtils.getParentWidgetslot(widgetslot);
            if(parentWidgetslot != null)
            {
                parentWidgetslot.updateView();
            }
        });
        return templateBtn;
    }


    protected Popup createTemplateRulesMenu(final Component parent, final Widget currentWidget)
    {
        final Popup templateRulesMenu = new Popup();
        parent.appendChild(templateRulesMenu);
        templateRulesMenu.setAttribute("rulesConfig", currentWidget.getWidgetInstanceSettings());
        templateRulesMenu.setAttribute("persistenceService", getWidgetPersistenceService());
        templateRulesMenu.setAttribute("currentWidget", currentWidget);
        templateRulesMenu.setAttribute("popup", templateRulesMenu);
        final ListitemRenderer<WidgetSocket> socketListRenderer = (item, data, index) -> item.setLabel(data.getId());
        templateRulesMenu.setAttribute("socketListRenderer", socketListRenderer);
        return templateRulesMenu;
    }


    protected void selectOnIncomingSocketModel(final Widget currentWidget, final WidgetInstanceSettings settings,
                    final Popup templateRulesMenu, final List<WidgetSocket> allInputs)
    {
        final SimpleListModel<WidgetSocket> selectOnIncomingSocketsModel = new SimpleListModel<>(allInputs);
        selectOnIncomingSocketsModel.setMultiple(true);
        selectOnIncomingSocketsModel.setSelection(
                        settings.isSelectOnAllIncomingEvents() ? allInputs : getSocketList(settings.getSelectOnIncomingEvents(), allInputs));
        templateRulesMenu.setAttribute("selectOnIncomingSocketsModel", selectOnIncomingSocketsModel);
        selectOnIncomingSocketsModel.addListDataListener(event -> {
            settings.setSelectOnAllIncomingEvents(areAllSocketsSelected(selectOnIncomingSocketsModel));
            settings.getSelectOnIncomingEvents().clear();
            settings.getSelectOnIncomingEvents().addAll(getSelectedSockets(selectOnIncomingSocketsModel));
            getWidgetPersistenceService().storeWidgetTree(currentWidget);
        });
    }


    protected void closeOnOutgoingSocketModel(final Widget currentWidget, final WidgetInstanceSettings settings,
                    final Popup templateRulesMenu, final List<WidgetSocket> allOutputs)
    {
        final SimpleListModel<WidgetSocket> closeOnOutgoingSocketsModel = new SimpleListModel<>(allOutputs);
        closeOnOutgoingSocketsModel.setMultiple(true);
        closeOnOutgoingSocketsModel.setSelection(
                        settings.isCloseOnAllOutgoingEvents() ? allOutputs : getSocketList(settings.getCloseOnOutgoingEvents(), allOutputs));
        templateRulesMenu.setAttribute("closeOnOutgoingSocketsModel", closeOnOutgoingSocketsModel);
        closeOnOutgoingSocketsModel.addListDataListener(event -> {
            settings.setCloseOnAllOutgoingEvents(areAllSocketsSelected(closeOnOutgoingSocketsModel));
            settings.getCloseOnOutgoingEvents().clear();
            settings.getCloseOnOutgoingEvents().addAll(getSelectedSockets(closeOnOutgoingSocketsModel));
            getWidgetPersistenceService().storeWidgetTree(currentWidget);
        });
    }


    protected void closeOnIncomingSocketModel(final Widget currentWidget, final WidgetInstanceSettings settings,
                    final Popup templateRulesMenu, final List<WidgetSocket> allInputs)
    {
        final SimpleListModel<WidgetSocket> closeOnIncomingSocketsModel = new SimpleListModel<>(allInputs);
        closeOnIncomingSocketsModel.setMultiple(true);
        closeOnIncomingSocketsModel.setSelection(
                        settings.isCloseOnAllIncomingEvents() ? allInputs : getSocketList(settings.getCloseOnIncomingEvents(), allInputs));
        templateRulesMenu.setAttribute("closeOnIncomingSocketsModel", closeOnIncomingSocketsModel);
        closeOnIncomingSocketsModel.addListDataListener(event -> {
            settings.setCloseOnAllIncomingEvents(areAllSocketsSelected(closeOnIncomingSocketsModel));
            settings.getCloseOnIncomingEvents().clear();
            settings.getCloseOnIncomingEvents().addAll(getSelectedSockets(closeOnIncomingSocketsModel));
            getWidgetPersistenceService().storeWidgetTree(currentWidget);
        });
    }


    protected void createOnIncomingSocketModel(final Widget currentWidget, final WidgetInstanceSettings settings,
                    final Popup templateRulesMenu, final List<WidgetSocket> allInputs)
    {
        final SimpleListModel<WidgetSocket> createOnIncomingSocketsModel = new SimpleListModel<>(allInputs);
        createOnIncomingSocketsModel.setMultiple(true);
        createOnIncomingSocketsModel.setSelection(
                        settings.isCreateOnAllIncomingEvents() ? allInputs : getSocketList(settings.getCreateOnIncomingEvents(), allInputs));
        templateRulesMenu.setAttribute("createOnIncomingSocketsModel", createOnIncomingSocketsModel);
        createOnIncomingSocketsModel.addListDataListener(event -> {
            settings.setCreateOnAllIncomingEvents(areAllSocketsSelected(createOnIncomingSocketsModel));
            settings.getCreateOnIncomingEvents().clear();
            settings.getCreateOnIncomingEvents().addAll(getSelectedSockets(createOnIncomingSocketsModel));
            getWidgetPersistenceService().storeWidgetTree(currentWidget);
        });
    }


    protected List<WidgetSocket> getSocketList(final Set<String> idList, final List<WidgetSocket> possible)
    {
        final List<WidgetSocket> result = new ArrayList<>(idList.size());
        for(final String id : idList)
        {
            for(final WidgetSocket socket : possible)
            {
                if(id.equals(socket.getId()))
                {
                    result.add(socket);
                    break;
                }
            }
        }
        return result;
    }


    protected boolean areAllSocketsSelected(final SimpleListModel<WidgetSocket> model)
    {
        return model.getSize() == model.getSelection().size();
    }


    protected List<String> getSelectedSockets(final SimpleListModel<WidgetSocket> model)
    {
        if(model.isSelectionEmpty())
        {
            return Collections.emptyList();
        }
        else
        {
            final Set<WidgetSocket> selected = model.getSelection();
            final List<String> result = new ArrayList<>(selected.size());
            for(final WidgetSocket socket : selected)
            {
                result.add(socket.getId());
            }
            return result;
        }
    }


    public WidgetPersistenceService getWidgetPersistenceService()
    {
        return widgetPersistenceService;
    }


    @Required
    public void setWidgetPersistenceService(final WidgetPersistenceService widgetPersistenceService)
    {
        this.widgetPersistenceService = widgetPersistenceService;
    }
}
