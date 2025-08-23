package de.hybris.platform.cockpit.components;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Timer;

public class SlideUpGroupbox extends Div
{
    private final Div contentDiv;
    private final AdvancedGroupbox groupbox;
    private final Timer closeTimer;
    private int timeOut = 5000;


    public SlideUpGroupbox()
    {
        this(false);
    }


    public SlideUpGroupbox(boolean open)
    {
        setSclass("slideUpGroupbox");
        Div positioningDiv = new Div();
        positioningDiv.setStyle("position: relative; height: 0px; width: 100%;");
        this.contentDiv = new Div();
        this.contentDiv.setWidth("100%");
        this.contentDiv.setSclass("slideUpGroupboxContent");
        this.contentDiv.setVisible(false);
        this.contentDiv.setAction("onshow: anima.appear(#{self}); onhide: anima.fade(#{self});");
        this.groupbox = new AdvancedGroupbox();
        this.groupbox.setOpen(open);
        this.groupbox.addEventListener("onOpen", (EventListener)new Object(this));
        positioningDiv.appendChild((Component)this.contentDiv);
        appendChild((Component)positioningDiv);
        appendChild((Component)this.groupbox);
        this.closeTimer = new Timer();
        this.closeTimer.setRunning(false);
        this.closeTimer.setDelay(getTimeOut());
        this.closeTimer.setRepeats(false);
        this.closeTimer.addEventListener("onTimer", (EventListener)new Object(this));
        appendChild((Component)this.closeTimer);
        if(open)
        {
            this.contentDiv.setVisible(true);
            this.closeTimer.start();
        }
    }


    public void setLabel(String label)
    {
        this.groupbox.setLabel(label);
    }


    public void setCaption(Component component)
    {
        this.groupbox.getCaptionContainer().getChildren().clear();
        this.groupbox.getCaptionContainer().appendChild(component);
    }


    public String getLabel()
    {
        return this.groupbox.getLabel();
    }


    public void setContent(Component comp)
    {
        this.contentDiv.appendChild(comp);
    }


    public void clearContent()
    {
        this.contentDiv.getChildren().clear();
    }


    public Component getContent()
    {
        return (Component)this.contentDiv;
    }


    public void setTimeOut(int timeOut)
    {
        this.timeOut = timeOut;
    }


    public int getTimeOut()
    {
        return this.timeOut;
    }
}
