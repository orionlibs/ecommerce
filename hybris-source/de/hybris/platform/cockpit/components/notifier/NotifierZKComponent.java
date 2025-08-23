package de.hybris.platform.cockpit.components.notifier;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlMacroComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Timer;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

public class NotifierZKComponent extends HtmlMacroComponent
{
    public static final String CAPTION_SCLASS = "notifierCaption";
    public static final String MESSAGE_SCLASS = "notifierMessage";
    private Div contentAreaDiv = null;
    private Timer timerComponent = null;
    private String sclass = null;
    private Notification notification = null;
    private final EventListener timerListener = (EventListener)new MyTimerListener(this);


    public void setTimerComponent(Timer timerComponent)
    {
        if(this.timerComponent != timerComponent)
        {
            if(this.timerComponent != null)
            {
                this.timerComponent.removeEventListener("onTimer", this.timerListener);
            }
            this.timerComponent = timerComponent;
            if(this.timerComponent != null)
            {
                this.timerComponent.addEventListener("onTimer", this.timerListener);
            }
        }
    }


    public Timer getTimerComponent()
    {
        return this.timerComponent;
    }


    public void setContentAreaDiv(Div contentAreaDiv)
    {
        this.contentAreaDiv = contentAreaDiv;
        if(this.contentAreaDiv != null)
        {
            this.contentAreaDiv.setVisible(false);
            setSclass(this.sclass);
            updateView();
        }
    }


    protected Div getContentAreaDiv()
    {
        return this.contentAreaDiv;
    }


    public void setSclass(String sclass)
    {
        this.sclass = sclass;
        setDynamicProperty("sclass", sclass);
        if(getContentAreaDiv() != null)
        {
            getContentAreaDiv().setSclass(sclass);
        }
    }


    public void setNotification(Notification notification)
    {
        if(this.notification != notification)
        {
            this.notification = notification;
            updateView();
        }
    }


    public void setDialogNotification(Notification notification)
    {
        if(this.notification != notification)
        {
            this.notification = notification;
            showDialog();
        }
    }


    public Notification getNotification()
    {
        return this.notification;
    }


    protected void showDialog()
    {
        if(getContentAreaDiv() != null && this.notification != null)
        {
            String caption = this.notification.getCaption();
            String message = this.notification.getMessage();
            Window dialog = new Window();
            dialog.setTitle((caption == null || caption.isEmpty()) ? Labels.getLabel("general.notification") : caption);
            dialog.setHeight("300px");
            dialog.setWidth("400px");
            dialog.setClosable(true);
            Div content = new Div();
            content.setStyle("overflow-y: auto; height: 100%;");
            dialog.appendChild((Component)content);
            if(message != null)
            {
                Label label = new Label(message);
                label.setStyle("font-weight: bold;");
                content.appendChild((Component)label);
            }
            if(!this.notification.getMessages().isEmpty())
            {
                Div addMsgContainer = new Div();
                addMsgContainer.setStyle("margin: 4px; border: 1px solid #ccc;");
                content.appendChild((Component)addMsgContainer);
                for(String msg : this.notification.getMessages())
                {
                    Div msgDiv = new Div();
                    msgDiv.setStyle("padding: 4px;");
                    msgDiv.appendChild((Component)new Label(msg));
                    addMsgContainer.appendChild((Component)msgDiv);
                }
            }
            getContentAreaDiv().getRoot().appendChild((Component)dialog);
            dialog.doHighlighted();
        }
    }


    protected void updateView()
    {
        if(getContentAreaDiv() != null)
        {
            getContentAreaDiv().getChildren().clear();
            if(this.notification != null)
            {
                String caption = this.notification.getCaption();
                String message = this.notification.getMessage();
                Vbox vbox = new Vbox();
                vbox.setHeight("100%");
                vbox.setWidth("100%");
                vbox.setAlign("center");
                getContentAreaDiv().appendChild((Component)vbox);
                if(caption != null && caption.length() > 0)
                {
                    Label captionLabel = new Label(caption);
                    captionLabel.setSclass("notifierCaption");
                    vbox.appendChild((Component)captionLabel);
                }
                if(message != null && message.length() > 0)
                {
                    Label messageLabel = new Label(message);
                    messageLabel.setSclass("notifierMessage");
                    vbox.appendChild((Component)messageLabel);
                }
                showNotification();
            }
        }
    }


    protected void showNotification()
    {
        if(getContentAreaDiv() != null && this.notification != null)
        {
            getContentAreaDiv().setVisible(true);
            if(getTimerComponent() != null)
            {
                getTimerComponent().start();
            }
        }
    }
}
