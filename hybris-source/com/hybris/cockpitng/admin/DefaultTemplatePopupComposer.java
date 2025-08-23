/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetInstanceSettings;
import com.hybris.cockpitng.core.persistence.WidgetPersistenceService;
import com.hybris.cockpitng.util.ViewAnnotationAwareComposer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Popup;

public class DefaultTemplatePopupComposer extends ViewAnnotationAwareComposer
{
    private Combobox routingModelCombo;
    private Checkbox createOnInit;
    private Checkbox reuseExisting;
    private Checkbox responsive;
    private transient WidgetPersistenceService persistenceService;
    private Widget currentWidget;
    private Popup popup;


    @Override
    public void doAfterCompose(final Component comp) throws Exception
    {
        super.doAfterCompose(comp);
        routingModelCombo.setModel(new ListModelList<Object>(WidgetInstanceSettings.SocketEventRoutingMode.values()));
    }


    @ViewEvent(eventName = Events.ON_CHANGE, componentID = "routingModelCombo")
    public void onRoutingModelComboChange()
    {
        currentWidget.getWidgetInstanceSettings().setSocketEventRoutingMode(
                        routingModelCombo.getSelectedItem().<WidgetInstanceSettings.SocketEventRoutingMode>getValue());
        persistenceService.storeWidgetTree(currentWidget);
    }


    @ViewEvent(eventName = Events.ON_CHECK, componentID = "createOnInit")
    public void onCreateOnInitCheck()
    {
        currentWidget.getWidgetInstanceSettings().setCreateOnInit(createOnInit.isChecked());
        persistenceService.storeWidgetTree(currentWidget);
    }


    @ViewEvent(eventName = Events.ON_CHECK, componentID = "reuseExisting")
    public void onReuseExistingCheck()
    {
        currentWidget.getWidgetInstanceSettings().setReuseExisting(reuseExisting.isChecked());
        persistenceService.storeWidgetTree(currentWidget);
    }


    @ViewEvent(eventName = Events.ON_CHECK, componentID = "responsive")
    public void onResponsiveCheck()
    {
        currentWidget.getWidgetInstanceSettings().setResponsive(responsive.isChecked());
        persistenceService.storeWidgetTree(currentWidget);
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = "okButton")
    public void onOkButtonClick()
    {
        popup.close();
    }
}
