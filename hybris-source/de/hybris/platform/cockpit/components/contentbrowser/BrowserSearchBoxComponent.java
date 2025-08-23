package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.components.StyledDiv;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Textbox;

public class BrowserSearchBoxComponent extends StyledDiv
{
    public BrowserSearchBoxComponent()
    {
        setSclass("newContentBrowserGroupbox");
        setWidth("100%");
        getContainerDiv().setWidth("100%");
        getContainerDiv().setAlign("left");
        Hbox hbox = new Hbox();
        hbox.setParent((Component)getContainerDiv());
        hbox.setAlign("center");
        hbox.setSclass("hiddenLabel");
        hbox.setHeight("19px");
        Textbox searchTextBox = new Textbox(Labels.getLabel("browserarea.newboxsearch"));
        searchTextBox.setParent((Component)hbox);
        searchTextBox.setCols(25);
        Button toolbarButton = new Button(Labels.getLabel("general.search"));
        toolbarButton.setSclass("btnblue");
        toolbarButton.setParent((Component)hbox);
        toolbarButton.setDisabled(false);
        toolbarButton.setTooltiptext(Labels.getLabel("general.search"));
        getContainerDiv().addEventListener("onClick", (EventListener)new Object(this, searchTextBox));
        searchTextBox.addEventListener("onFocus", (EventListener)new Object(this, searchTextBox));
        searchTextBox.addEventListener("onBlur", (EventListener)new Object(this));
        searchTextBox.addEventListener("onOK", (EventListener)new Object(this, searchTextBox));
        searchTextBox.addEventListener("onLater", (EventListener)new Object(this, searchTextBox));
        toolbarButton.addEventListener("onClick", (EventListener)new Object(this, searchTextBox));
    }
}
