package de.hybris.platform.cockpit.components;

import de.hybris.platform.cockpit.util.UITools;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;

public class AdvancedGroupbox extends IdSpaceGroupbox
{
    private static final long serialVersionUID = -7081957865771702931L;
    private static final String OPEN_STATE_SCLASS = "advancedGroupboxOpen";
    private static final String CLOSED_STATE_SCLASS = "advancedGroupboxClosed";
    private Label labelComponent = null;
    private Div preLabelComponent = null;
    private Div captionContainer = null;
    private Div imgDiv = null;
    private boolean handleVisibility = true;


    public void setOpen(boolean open)
    {
        super.setOpen(open);
        if(open)
        {
            this.imgDiv.setSclass("advancedGroupboxOpen");
            this.imgDiv.setTooltiptext(Labels.getLabel("general.collapse"));
        }
        else
        {
            this.imgDiv.setSclass("advancedGroupboxClosed");
            this.imgDiv.setTooltiptext(Labels.getLabel("general.expand"));
        }
    }


    protected void onOpen()
    {
    }


    protected void onClose()
    {
    }


    public void setClosable(boolean closable)
    {
        super.setClosable(closable);
        if(this.imgDiv != null)
        {
            if(isHandleVisibility())
            {
                this.imgDiv.setVisible(isClosable());
            }
            else
            {
                UITools.modifyStyle((HtmlBasedComponent)this.imgDiv, "display", isClosable() ? "block" : "none");
            }
        }
    }


    public AdvancedGroupbox()
    {
        (new Caption()).setParent((Component)this);
        setSclass("advancedGroupbox");
        this.imgDiv = new Div();
        this.imgDiv.setSclass(isOpen() ? "advancedGroupboxOpen" : "advancedGroupboxClosed");
        this.imgDiv.setStyle("cursor: pointer; height: 20px; width: 20px;");
        this.imgDiv.setTooltiptext(Labels.getLabel(isOpen() ? "general.collapse" : "general.expand"));
        if(isHandleVisibility())
        {
            this.imgDiv.setVisible(isClosable());
        }
        else
        {
            UITools.modifyStyle((HtmlBasedComponent)this.imgDiv, "display", isClosable() ? "block" : "none");
        }
        getCaption().appendChild(createCaption(false));
        getCaption().setSclass("hiddenLabel");
        setMold("3d");
        addEventListener("onOpen", (EventListener)new Object(this));
    }


    public void setArrowLeft(boolean value)
    {
        getCaption().getChildren().clear();
        getCaption().appendChild(createCaption(value));
    }


    private Component createCaption(boolean arrowLeft)
    {
        Hbox hbox = new Hbox();
        hbox.setSclass("caption_table");
        this.labelComponent = new Label();
        Hbox labelHbox = new Hbox();
        this.preLabelComponent = new Div();
        labelHbox.appendChild((Component)this.preLabelComponent);
        labelHbox.appendChild((Component)this.labelComponent);
        hbox.appendChild((Component)labelHbox);
        this.labelComponent.setSclass("advancedGroupbox_label");
        hbox.appendChild((Component)(this.captionContainer = new Div()));
        if(arrowLeft)
        {
            hbox.setWidths("20px, 0, none");
            hbox.insertBefore((Component)this.imgDiv, (Component)labelHbox);
            UITools.modifySClass((HtmlBasedComponent)hbox, "arrowLeft", true);
        }
        else
        {
            hbox.setWidths("0, none, 20px");
            hbox.appendChild((Component)this.imgDiv);
        }
        return (Component)hbox;
    }


    public Div getHeaderImgDiv()
    {
        return this.imgDiv;
    }


    public void setLabel(String label)
    {
        this.labelComponent.setValue(label);
    }


    public String getLabel()
    {
        return this.labelComponent.getValue();
    }


    public Div getCaptionContainer()
    {
        return this.captionContainer;
    }


    public Div getPreLabelComponent()
    {
        return this.preLabelComponent;
    }


    public boolean isHandleVisibility()
    {
        return this.handleVisibility;
    }


    public void setHandleVisibility(boolean handleVisibility)
    {
        this.handleVisibility = handleVisibility;
    }
}
