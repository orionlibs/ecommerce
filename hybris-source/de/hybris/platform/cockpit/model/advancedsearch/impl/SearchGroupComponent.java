package de.hybris.platform.cockpit.model.advancedsearch.impl;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Space;
import org.zkoss.zul.Toolbarbutton;

public class SearchGroupComponent extends Div
{
    protected Div groupContent;
    protected Label label;
    protected EventListener clickListener = null;


    public SearchGroupComponent(EventListener clickListener)
    {
        this.clickListener = clickListener;
    }


    public void init(String label)
    {
        Toolbarbutton removeBtn = new Toolbarbutton("", "/cockpit/images/remove.png");
        removeBtn.setTooltiptext(Labels.getLabel("advancedsearch.button.remove.tooltip"));
        if(this.clickListener != null)
        {
            removeBtn.addEventListener("onClick", this.clickListener);
        }
        removeBtn.setSclass("advancedSearchRemoveBtn");
        appendChild((Component)removeBtn);
        this.label = new Label(label);
        this.label.setSclass("searchGroupLabel");
        appendChild((Component)this.label);
        setSclass("advancedSearchGroup");
        this.groupContent = new Div();
        this.groupContent.setSclass("advancedSearchGroup-cnt");
        Space space = new Space();
        space.setSclass("searchGroupSep");
        space.setBar(true);
        space.setOrient("horizontal");
        appendChild((Component)space);
        appendChild((Component)this.groupContent);
    }


    public Component getGroupContainer()
    {
        return (Component)this.groupContent;
    }
}
