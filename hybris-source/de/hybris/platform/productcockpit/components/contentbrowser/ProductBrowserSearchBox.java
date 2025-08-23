package de.hybris.platform.productcockpit.components.contentbrowser;

import de.hybris.platform.cockpit.components.StyledDiv;
import de.hybris.platform.productcockpit.session.impl.ProductPerspective;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Textbox;

public class ProductBrowserSearchBox extends StyledDiv
{
    protected static final String SEARCH_MAGNIFIER_BTN_IMG = "/cockpit/images/BUTTON_GREEN_search.gif";


    public ProductBrowserSearchBox()
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
        toolbarButton.setSclass("btngreen");
        toolbarButton.setParent((Component)hbox);
        toolbarButton.setDisabled(false);
        toolbarButton.setTooltiptext(Labels.getLabel("general.search"));
        toolbarButton.setImage("/cockpit/images/BUTTON_GREEN_search.gif");
        getContainerDiv().addEventListener("onClick", (EventListener)new Object(this, searchTextBox));
        searchTextBox.addEventListener("onFocus", (EventListener)new Object(this, searchTextBox));
        searchTextBox.addEventListener("onBlur", (EventListener)new Object(this));
        searchTextBox.addEventListener("onOK", (EventListener)new Object(this, searchTextBox));
        searchTextBox.addEventListener("onLater", (EventListener)new Object(this, searchTextBox));
        toolbarButton.addEventListener("onClick", (EventListener)new Object(this, searchTextBox));
    }


    protected ProductPerspective getProductPerspective()
    {
        return (ProductPerspective)SpringUtil.getBean("ProductPerspective");
    }
}
