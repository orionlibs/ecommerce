package de.hybris.platform.cmscockpit.components.editorarea.view;

import de.hybris.platform.cmscockpit.components.editorarea.model.SimpleSectionRowModel;
import de.hybris.platform.cockpit.components.dialog.DescriptionTooltip;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Toolbarbutton;

public class SimpleSectionRowComponent extends Div
{
    private static final String HBOX_CLASS = "simpleSectionTable";
    private static final String TEXTBOX_CLASS = "simpleEditorInput";
    private static final String VALIDATION_IMG = "validationImg";
    private static final String LABELCONTAINERCLASS = "editorrowlabelcontainer";
    private static final String SECTION_ROW_VALUE_CONTAINER = "section-row-value-container";
    private final Div innerDiv;
    private static final String SCLASS = "sectionRowComponent";
    private final SimpleSectionRowModel model;
    private static final String OPEN_NEW_TAB_IMAGE = "cockpit/images/icon_func_open-in-tab.png";
    private static final String OPEN_NEW_TAB_BUTTON_CLASS = "openNewTabButton";
    private static final String SIMPLE_POPUP_CLASS = "urlTextFieldPopup";


    public SimpleSectionRowComponent(SimpleSectionRowModel model)
    {
        this.model = model;
        setSclass("sectionRowComponent");
        setVisible(model.isVisible());
        this.innerDiv = new Div();
        appendChild((Component)this.innerDiv);
        Image validationIcon = new Image();
        validationIcon.setClass("validationImg");
        this.innerDiv.appendChild((Component)validationIcon);
        Hbox box = new Hbox();
        box.setSclass("simpleSectionTable");
        box.setWidth("100%");
        box.setStyle("table-layout:fixed;");
        box.setWidths("11em,none");
        this.innerDiv.appendChild((Component)box);
        Div labelContainer = new Div();
        labelContainer.setSclass("editorrowlabelcontainer");
        labelContainer.setWidth("100%");
        Label label = new Label(model.getLabel());
        label.setParent((Component)labelContainer);
        box.appendChild((Component)labelContainer);
        Div valueContainer = new Div();
        valueContainer.setSclass("section-row-value-container");
        String stringValue = null;
        if(model.getValue() != null)
        {
            stringValue = model.getValue().toString();
        }
        Hbox valueHbox = new Hbox();
        valueHbox.setWidth("100%");
        valueHbox.setStyle("table-layout:fixed;");
        valueHbox.setWidths("none,22px,22px");
        Textbox valuTextbox = new Textbox(stringValue);
        valuTextbox.setSclass("simpleEditorInput");
        valuTextbox.setReadonly(!model.isEditable());
        Label valueLabel = new Label(stringValue);
        Div popupDiv = new Div();
        popupDiv.setStyle("urlTextFieldPopup");
        popupDiv.appendChild((Component)valueLabel);
        Popup popup = new Popup();
        valueContainer.appendChild((Component)popup);
        valuTextbox.setTooltip(popup);
        valuTextbox.setPopup(popup);
        popup.appendChild((Component)popupDiv);
        valueHbox.appendChild((Component)valuTextbox);
        Toolbarbutton openNewTabButton = new Toolbarbutton(null, "cockpit/images/icon_func_open-in-tab.png");
        openNewTabButton.setTooltiptext(Labels.getLabel("cmscockpit.url.openinnewtab.tooltip"));
        openNewTabButton.setSclass("openNewTabButton");
        openNewTabButton.addEventListener("onClick", (EventListener)new Object(this, model));
        valueHbox.appendChild((Component)openNewTabButton);
        DescriptionTooltip descTooltip = new DescriptionTooltip(model.getToolTipText());
        valueHbox.appendChild((Component)descTooltip);
        valueContainer.appendChild((Component)valueHbox);
        box.appendChild((Component)valueContainer);
    }


    public SimpleSectionRowModel getModel()
    {
        return this.model;
    }
}
