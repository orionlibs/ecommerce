package de.hybris.platform.cockpit.components;

import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.IdSpace;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Panel;

public class AdvancedPanel extends Panel implements IdSpace
{
    private static final String OPENED_IMG_URI = "/cockpit/images/panel_button_down.png";
    private static final String CLOSED_IMG_URI = "/cockpit/images/panel_button_right.png";
    private final Image openedStateImage;
    private final Label labelComponent;
    private final Div captionContainer;


    public AdvancedPanel()
    {
        (new Caption()).setParent((Component)this);
        setSclass("advancedPanel");
        this.openedStateImage = new Image(isOpen() ? "/cockpit/images/panel_button_down.png" : "/cockpit/images/panel_button_right.png");
        this.openedStateImage.setStyle("left: 5px; top: 7px; position: relative;");
        this.openedStateImage.setTooltiptext(Labels.getLabel(isOpen() ? "general.collapse" : "general.expand"));
        Div imgDiv = new Div();
        imgDiv.setStyle("cursor: pointer; height: 20px; width: 20px; left: 10px; position: relative;");
        Hbox hbox = new Hbox();
        hbox.setWidths("none, 100%, none, none");
        hbox.setSclass("caption_table");
        hbox.appendChild((Component)(this.labelComponent = new Label()));
        this.labelComponent.setSclass("advancedGroupbox_label");
        Div captionDiv = new Div();
        hbox.appendChild((Component)captionDiv);
        captionDiv.appendChild((Component)(this.captionContainer = new Div()));
        Div dragDiv = new Div();
        dragDiv.setStyle("width: 20px; height: 20px; cursor:move; left: 10px; position: relative;");
        dragDiv.setSclass("panelDragArea");
        hbox.appendChild((Component)dragDiv);
        imgDiv.appendChild((Component)this.openedStateImage);
        hbox.appendChild((Component)imgDiv);
        getCaption().appendChild((Component)hbox);
        getCaption().setSclass("hiddenLabel");
    }


    public void setOpen(boolean open)
    {
        super.setOpen(open);
        if(open)
        {
            doOnOpen();
        }
        else
        {
            doOnClose();
        }
    }


    public void doOnOpen()
    {
        this.openedStateImage.setSrc("/cockpit/images/panel_button_down.png");
        this.openedStateImage.setTooltiptext(Labels.getLabel("general.collapse"));
        this.captionContainer.setVisible(true);
    }


    public void doOnClose()
    {
        this.openedStateImage.setSrc("/cockpit/images/panel_button_right.png");
        this.openedStateImage.setTooltiptext(Labels.getLabel("general.expand"));
        this.captionContainer.setVisible(false);
    }


    public void setLabel(String label)
    {
        this.labelComponent.setValue(label);
        if(UISessionUtils.getCurrentSession().isUsingTestIDs())
        {
            UITools.applyTestID((Component)this.labelComponent, "Section_" + ((label == null) ? "" : label.replaceAll(" ", "_")) + "_tab");
        }
    }


    public String getLabel()
    {
        return this.labelComponent.getValue();
    }


    public void setLabelSClass(String sClass)
    {
        this.labelComponent.setSclass(sClass);
    }


    public Div getCaptionContainer()
    {
        return this.captionContainer;
    }
}
