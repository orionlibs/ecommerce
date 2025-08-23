package de.hybris.platform.cockpit.components.sectionpanel;

import de.hybris.platform.cockpit.components.IdSpaceDiv;
import de.hybris.platform.cockpit.helpers.validation.ValidationUIHelper;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.impl.DefaultEditorSectionPanelModel;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.H3;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Div;
import org.zkoss.zul.Html;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;

public class DefaultMessageBoxRenderer implements MessageBoxRenderer
{
    public void render(SectionPanel panel, Component parent, List<Message> messages)
    {
        Div msgDiv = new Div();
        msgDiv.setSclass("sectionpanel_msg_container");
        for(Message message : messages)
        {
            if(!message.isVisible())
            {
                continue;
            }
            IdSpaceDiv idSpaceDiv = new IdSpaceDiv();
            Image validationIcon = new Image();
            validationIcon.setClass("headerValidationImg");
            idSpaceDiv.appendChild((Component)validationIcon);
            if(message.getConstraintPk() != null)
            {
                SectionPanelModel model = panel.getModel();
                ObjectValueContainer container = null;
                if(model instanceof DefaultEditorSectionPanelModel)
                {
                    container = ((DefaultEditorSectionPanelModel)model).getEditorArea().getCurrentObjectValues();
                }
                ValidationUIHelper validationUiHelper = (ValidationUIHelper)SpringUtil.getBean("validationUIHelper");
                List<String> forceWritePks = new ArrayList<>();
                forceWritePks.add(message.getConstraintPk());
                Menupopup menuPopup = validationUiHelper.buildTypeConstraintValidationMenuPopup(message.getConstraintPk(), container, message
                                .getLevel(), forceWritePks, panel.getModel());
                idSpaceDiv.appendChild((Component)menuPopup);
                validationIcon.setPopup((Popup)menuPopup);
            }
            switch(message.getLevel())
            {
                case 3:
                    idSpaceDiv.setSclass("section_message_error");
                    validationIcon.setSrc("cockpit/images/validation_error_14.gif");
                    break;
                case 2:
                    idSpaceDiv.setSclass("section_message_warning");
                    validationIcon.setSrc("cockpit/images/validation_warning_14.gif");
                    break;
                case 1:
                    idSpaceDiv.setSclass("section_message_ok_warning");
                    validationIcon.setSrc("cockpit/images/validation_warning_14.gif");
                    break;
                case 0:
                    idSpaceDiv.setSclass("section_message_info");
                    validationIcon.setSrc("cockpit/images/validation_info_14.gif");
                    break;
            }
            Html content = new Html(message.getText());
            content.setParent((Component)idSpaceDiv);
            idSpaceDiv.setStyle("position:relative");
            Popup popup = new Popup();
            Div popupDiv = new Div();
            H3 popupHeader = new H3();
            Label headerLabel = new Label();
            headerLabel.setValue(Labels.getLabel("validation.popup.header"));
            popupHeader.appendChild((Component)headerLabel);
            popupDiv.appendChild((Component)popupHeader);
            popupDiv.setSclass("validationTooltipPopup");
            popupDiv.setStyle("max-width: 400px");
            popup.appendChild((Component)popupDiv);
            Html label = new Html();
            label.setContent(message.getTooltip());
            popupDiv.appendChild((Component)label);
            validationIcon.setTooltip(popup);
            Div overlayDiv = new Div();
            idSpaceDiv.appendChild((Component)popup);
            overlayDiv.setAlign("right");
            overlayDiv.setStyle("position:absolute;left:0;top:0;width:100%;height:30px;");
            Div closeButton = new Div();
            closeButton.setSclass("plainBtn");
            closeButton.setWidth("14px");
            closeButton.setHeight(closeButton.getWidth());
            closeButton.setStyle("background-image:Url('/productcockpit/cockpit/images/remove.png');cursor:pointer");
            closeButton.setVisible(false);
            closeButton.setParent((Component)overlayDiv);
            closeButton.setId("btn_close");
            closeButton.addEventListener("onClick", (EventListener)new Object(this, panel, message));
            overlayDiv.setAction("onmouseover: anima.appear(#{" + closeButton.getId() + "}); onmouseout:if( !((this==event.target && #{" + closeButton
                            .getId() + "}==event.relatedTarget)|| (this==event.relatedTarget && #{" + closeButton
                            .getId() + "}==event.target))) anima.fade(#{" + closeButton
                            .getId() + "})");
            idSpaceDiv.setParent((Component)msgDiv);
            idSpaceDiv.addEventListener("onClick", (EventListener)new Object(this, panel, message));
            Menupopup menuPopUp = new Menupopup();
            Menuitem menuItem = new Menuitem();
            menuItem.setLabel(Labels.getLabel("sectionmenu.showmessageoccurrence"));
            menuItem.addEventListener("onClick", (EventListener)new Object(this, panel, message));
            menuItem.setParent((Component)menuPopUp);
            menuItem = new Menuitem();
            menuItem.setLabel(Labels.getLabel("sectionmenu.hidemessage"));
            menuItem.addEventListener("onClick", (EventListener)new Object(this, panel, message));
            menuItem.setParent((Component)menuPopUp);
            menuItem = new Menuitem();
            menuItem.setLabel(Labels.getLabel("sectionmenu.showallmessages"));
            menuItem.addEventListener("onClick", (EventListener)new Object(this, panel));
            menuItem.setParent((Component)menuPopUp);
            menuPopUp.setParent((Component)idSpaceDiv);
            idSpaceDiv.setContext((Popup)menuPopUp);
        }
        msgDiv.setParent(parent);
    }
}
