package de.hybris.platform.cockpit.components.dialog;

import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Toolbarbutton;

public class DescriptionTooltip extends Div
{
    private static final Logger LOG = LoggerFactory.getLogger(DescriptionTooltip.class);
    public static final String DEFAULT_TOOLTIP_WIDTH = "200px";
    private final Label description;
    private final Div popupDiv;
    private Popup popup = null;
    private final Toolbarbutton button;
    private String buttonIconFileName = "/cockpit/images/icon_func_questionmark.png";
    private final String buttonCssClass = "property_description_button";
    private Boolean render = Boolean.FALSE;


    public Boolean isRender()
    {
        return this.render;
    }


    public DescriptionTooltip()
    {
        this.button = new Toolbarbutton("", this.buttonIconFileName);
        appendChild((Component)this.button);
        this.button.setImage(this.buttonIconFileName);
        this.description = new Label();
        this.popupDiv = new Div();
        this.popupDiv.setStyle("max-width: 400px;");
        this.popupDiv.appendChild((Component)this.description);
        this.popup = new Popup();
        appendChild((Component)this.popup);
        this.button.setTooltip(this.popup);
        this.button.setPopup(this.popup);
        this.button.setSclass("property_description_button");
        this.button.setWidth("200px");
        this.popup.appendChild((Component)this.popupDiv);
    }


    public DescriptionTooltip(String descriptionText)
    {
        this();
        setDescription(descriptionText);
    }


    public DescriptionTooltip(String descriptionText, String tooltipHoverImage)
    {
        this(descriptionText);
        setTooltipButtonIcon(tooltipHoverImage);
    }


    public void setDescription(String descriptionText)
    {
        if(!GenericValidator.isBlankOrNull(descriptionText))
        {
            this.description.setValue(descriptionText);
            this.render = Boolean.TRUE;
            LOG.debug("Tooltip rendered with description: " + descriptionText);
        }
    }


    public void setTooltipButtonIcon(String tooltipIcon)
    {
        this.buttonIconFileName = tooltipIcon;
        this.button.setImage(tooltipIcon);
    }
}
