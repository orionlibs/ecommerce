/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.admin;

import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.util.ViewAnnotationAwareComposer;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Tabbox;
import org.zkoss.zul.Tabpanels;
import org.zkoss.zul.Window;

public class DefaultGroupWizardComposer extends ViewAnnotationAwareComposer
{
    private Button backBtn;
    private Button nextBtn;
    private Button finBtn;
    private Tabbox tabs;
    private Tabpanels panels;
    private Window mainWizardWindow;


    @ViewEvent(eventName = Events.ON_CREATE, componentID = "updateButtonsContainer")
    public void updateButtons()
    {
        final boolean last = tabs.getSelectedIndex() == panels.getChildren().size() - 1;
        final boolean first = tabs.getSelectedIndex() == 0;
        backBtn.setVisible(!first);
        nextBtn.setVisible(!last);
        finBtn.setVisible(last);
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = "backBtn")
    public void onBackBtnClick()
    {
        if(tabs.getSelectedIndex() > 0)
        {
            tabs.setSelectedIndex(tabs.getSelectedIndex() - 1);
        }
        updateButtons();
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = "nextBtn")
    public void onNextBtnClick()
    {
        if(tabs.getSelectedIndex() < panels.getChildren().size() - 1)
        {
            tabs.setSelectedIndex(tabs.getSelectedIndex() + 1);
        }
        updateButtons();
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = "finBtn")
    public void onFinBtnClick(final Event event) throws Exception
    {
        ((EventListener)mainWizardWindow.getAttribute("finishListener")).onEvent(event);
    }


    @ViewEvent(eventName = Events.ON_CLICK, componentID = "cancelBtn")
    public void onCancelBtnClick()
    {
        mainWizardWindow.detach();
    }


    @ViewEvent(eventName = Events.ON_DROP, componentID = "sockets")
    public void onSocketsDrop(final Event event) throws Exception
    {
        ((EventListener)mainWizardWindow.getAttribute("dropListener")).onEvent(event);
    }
}
