package de.hybris.platform.cockpit.components;

import org.zkoss.util.resource.Labels;
import org.zkoss.zul.Image;

public class CreateNewComponent extends Image
{
    protected static String ADD_BUTTON_ICON = "cockpit/images/icon_func_new_available.png";


    public CreateNewComponent()
    {
        super(ADD_BUTTON_ICON);
        setSclass("toolbar-btn-createnew");
        setTooltiptext(Labels.getLabel("editorarea.button.createnewitem.tooltip"));
    }
}
