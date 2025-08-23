package de.hybris.platform.cockpit.model.general.impl;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Toolbarbutton;

public class LoadingProgressContainer extends Div
{
    private final Toolbarbutton cancelLoadingButton;
    private final Progressmeter loadingProgress;
    private int index = 0;


    public LoadingProgressContainer(boolean blockingView)
    {
        Div container = new Div();
        if(blockingView)
        {
            setStyle("position: absolute; left: 0; top: 0; width: 100%; height: 100%;");
        }
        container
                        .setStyle("position: absolute; bottom: 0; left: 2px; border: 1px solid #ccc; padding: 3px; background: #eee; opacity: 0.8;");
        Hbox hbox = new Hbox();
        container.appendChild((Component)hbox);
        appendChild((Component)container);
        this.loadingProgress = new Progressmeter();
        this.loadingProgress.setWidth("300px");
        hbox.appendChild((Component)this.loadingProgress);
        this.cancelLoadingButton = new Toolbarbutton(Labels.getLabel("general.cancel"));
        this.cancelLoadingButton.setSclass("cancelLazyLoad sectionEditButton");
        this.cancelLoadingButton.addEventListener("onClick", (EventListener)new Object(this));
        hbox.appendChild((Component)this.cancelLoadingButton);
        setVisible(false);
    }


    public void reset()
    {
        this.loadingProgress.setValue(0);
        this.index = 0;
    }


    public void setValues(int index, int size)
    {
        int percentageLoaded = index * 100 / size;
        this.index = index;
        this.loadingProgress.setValue(percentageLoaded);
    }


    public int getIndex()
    {
        return this.index;
    }


    public void addCancelButtonEventListener(String eventName, EventListener listener)
    {
        this.cancelLoadingButton.addEventListener(eventName, listener);
    }
}
