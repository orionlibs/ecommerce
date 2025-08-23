package de.hybris.platform.cockpit.model.advancedsearch.impl;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Space;
import org.zkoss.zul.Toolbarbutton;

public class EditSearchGroupComponent extends SearchGroupComponent
{
    private Toolbarbutton labelBtn;


    public EditSearchGroupComponent(EventListener clickListener)
    {
        super(clickListener);
    }


    public void init(String label)
    {
        this.labelBtn = new Toolbarbutton(label);
        this.labelBtn.setSclass("editSearchGroupLabel");
        if(this.clickListener != null)
        {
            this.labelBtn.addEventListener("onClick", this.clickListener);
        }
        appendChild((Component)this.labelBtn);
        setSclass("advancedEditSearchGroup");
        this.groupContent = new Div();
        this.groupContent.setSclass("advancedSearchGroup-cnt");
        Space space = new Space();
        space.setBar(true);
        space.setOrient("horizontal");
        appendChild((Component)space);
        appendChild((Component)this.groupContent);
    }
}
