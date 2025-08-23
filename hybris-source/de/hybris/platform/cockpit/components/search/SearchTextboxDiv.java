package de.hybris.platform.cockpit.components.search;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Textbox;

public class SearchTextboxDiv extends Div
{
    protected String defaultInfoLabel = "";
    protected Textbox searchInputComponent;


    public SearchTextboxDiv(String defaultInfoLabel)
    {
        this.defaultInfoLabel = defaultInfoLabel;
        initialize();
    }


    protected void initialize()
    {
        Image searchImage = new Image("/cockpit/images/icon_system_search.png");
        searchImage.setClass("dualbox_search_img");
        appendChild((Component)searchImage);
        this.searchInputComponent = new Textbox();
        this.searchInputComponent.setClass("dualbox_search_input_info");
        this.searchInputComponent.setFocus(false);
        this.searchInputComponent.setText(this.defaultInfoLabel);
        appendChild((Component)this.searchInputComponent);
        setStyle("border: 1px solid #CDCDCD; ");
        addEventListener("onClick", (EventListener)new Object(this));
        this.searchInputComponent.addEventListener("onBlur", (EventListener)new Object(this));
    }


    public Textbox getSearchInputComponent()
    {
        return this.searchInputComponent;
    }
}
