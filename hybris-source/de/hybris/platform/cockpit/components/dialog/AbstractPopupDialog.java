package de.hybris.platform.cockpit.components.dialog;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

public abstract class AbstractPopupDialog extends Window
{
    protected static final String DEFAULT_WIDTH = "500px";
    protected static final String DEFAULT_HEIGHT = "350px";
    protected static final String CLOSE_BTN_IMG = "/cockpit/images/close_btn.png";
    protected String popupWidth = "500px";
    protected String popupHeight = "350px";
    protected String popupTitle;
    protected boolean popupVisible = false;
    protected Div mainContainer;


    public AbstractPopupDialog() throws InterruptedException
    {
    }


    public AbstractPopupDialog(String popupTitle) throws InterruptedException
    {
        this.popupTitle = popupTitle;
    }


    public AbstractPopupDialog(String popupWidth, String popupHeight, String popupTitle) throws InterruptedException
    {
        this.popupWidth = popupWidth;
        this.popupHeight = popupHeight;
        this.popupTitle = popupTitle;
    }


    public void createPopup() throws InterruptedException
    {
        boolean visible = isPopupVisible();
        setSclass("dialogPopupWindow");
        setShadow(false);
        setBorder("none");
        setClosable(Boolean.FALSE.booleanValue());
        setPosition("center");
        setSizable(Boolean.FALSE.booleanValue());
        setMode("popup");
        setMinheight(200);
        setMinwidth(300);
        setClosable(true);
        setWidth(getPopupWidth());
        setHeight(getPopupHeight());
        setTitle(getPopupTitle());
        setVisible(visible);
        this.mainContainer = new Div();
        this.mainContainer.setWidth("100%");
        this.mainContainer.setHeight("100%");
        appendChild((Component)this.mainContainer);
    }


    public abstract void createPopupContent();


    public String getPopupWidth()
    {
        return this.popupWidth;
    }


    public void setPopupWidth(String popupWidth)
    {
        this.popupWidth = popupWidth;
    }


    public String getPopupHeight()
    {
        return this.popupHeight;
    }


    public void setPopupHeight(String popupHeight)
    {
        this.popupHeight = popupHeight;
    }


    public String getPopupTitle()
    {
        return this.popupTitle;
    }


    public void setPopupTitle(String popupTitle)
    {
        this.popupTitle = popupTitle;
    }


    public boolean isPopupVisible()
    {
        return this.popupVisible;
    }


    public boolean setVisible(boolean visible)
    {
        this.popupVisible = visible;
        return super.setVisible(visible);
    }


    public HtmlBasedComponent getMainContainer()
    {
        return (HtmlBasedComponent)this.mainContainer;
    }
}
