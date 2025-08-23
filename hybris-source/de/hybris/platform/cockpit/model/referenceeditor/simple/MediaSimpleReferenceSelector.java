package de.hybris.platform.cockpit.model.referenceeditor.simple;

import de.hybris.platform.cockpit.model.referenceeditor.simple.impl.DefaultSimpleMediaReferenceSelectorModel;
import de.hybris.platform.cockpit.util.UITools;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zhtml.Img;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Toolbarbutton;

public class MediaSimpleReferenceSelector extends SimpleReferenceSelector
{
    private static final Logger LOG = LoggerFactory.getLogger(MediaSimpleReferenceSelector.class);
    protected transient Div imageLabelContainer;
    protected static final String MEDIA_SIMPLE_REF_SELECTOR_LABEL_SCLASS = "mediaSimpleReferenceSelectorLabel";
    protected static final String MEDIA_SIMPLE_REF_SELECTOR_ITEM_SCLASS = "mediaSimpleReferenceSelectorItemEntry";
    protected static final String MEDIA_SIMPLE_REF_IMAGE_CELL = "mediaSimpleReferenceSelectorImageCell";
    private String imageHeightInAutocompleteList = "30px";
    private String imageHeight = "20px";
    private boolean imageAsLabel = true;


    public boolean isImageAsLabel()
    {
        return this.imageAsLabel;
    }


    protected void updateEditButtonVisibility()
    {
        boolean areaRelatedEditVisibility = false;
        Object object = getModel().getParameters().get("showEditButton");
        if(object instanceof String && Boolean.parseBoolean((String)object) && getModel().getValue() != null)
        {
            areaRelatedEditVisibility = true;
        }
        else if(getModel() instanceof DefaultSimpleMediaReferenceSelectorModel && object == null)
        {
            areaRelatedEditVisibility = true;
        }
        this.openPopupEditorBtn.setVisible((isShowEditButton() && areaRelatedEditVisibility));
    }


    public void setImageAsLabel(boolean imageAsLabel)
    {
        this.imageAsLabel = imageAsLabel;
    }


    public void setImageHeightInAutocompleteList(String imageHeightInAutocompleteList)
    {
        this.imageHeightInAutocompleteList = imageHeightInAutocompleteList;
    }


    public void setImageHeight(String imageHeight)
    {
        this.imageHeight = imageHeight;
    }


    protected ListitemRenderer autoCompleteListItemRenderer()
    {
        return (ListitemRenderer)new Object(this);
    }


    public boolean initialize()
    {
        boolean state = super.initialize();
        if(isImageAsLabel())
        {
            UITools.detachChildren((Component)this.componentContainer);
            this.componentContainer.appendChild((Component)prepareImageCounterpart());
            this.mainHbox.setWidths("none,30px,30px");
        }
        return state;
    }


    protected Toolbarbutton createAdvancedSearchButton()
    {
        Toolbarbutton button = new Toolbarbutton("", "/cockpit/images/open_advanced_editor.gif");
        button.addEventListener("onClick", (EventListener)new Object(this));
        return button;
    }


    private Div prepareImageCounterpart()
    {
        Object currentValue = getModel().getValue();
        Div parentContainer = new Div();
        parentContainer.setSclass("mediaSimpleReferenceSelectorLabel");
        Img imageLabel = new Img();
        String nocachedUrl = ((DefaultSimpleMediaReferenceSelectorModel)getModel()).getDownloadUrlCachingFree(currentValue);
        if(!StringUtils.isBlank(nocachedUrl))
        {
            if(!UITools.isUrlAbsolute(nocachedUrl) && nocachedUrl.charAt(0) == '/')
            {
                nocachedUrl = ".." + nocachedUrl;
            }
        }
        imageLabel.setDynamicProperty("src", nocachedUrl);
        imageLabel.setDynamicProperty("alt", computeLabel(currentValue));
        imageLabel.setDynamicProperty("height", this.imageHeight);
        parentContainer.appendChild((Component)imageLabel);
        Image popupImage = new Image(nocachedUrl);
        Popup popup = new Popup();
        popup.appendChild((Component)popupImage);
        parentContainer.appendChild((Component)popup);
        parentContainer.setTooltip(popup);
        Label mnemonicLabel = new Label(((DefaultSimpleMediaReferenceSelectorModel)getModel()).getMnemonic(currentValue));
        mnemonicLabel.setStyle("padding-right:5px;font-size:10px !important");
        parentContainer.appendChild((Component)mnemonicLabel);
        return parentContainer;
    }


    public void updateMode()
    {
        if(isInitialized() && !isDisabled())
        {
            SimpleSelectorModel.Mode mode = getModel().getMode();
            if(mode.equals(SimpleSelectorModel.Mode.VIEW_MODE))
            {
                if(isImageAsLabel())
                {
                    this.autoCompleteComponent.close();
                    UITools.detachChildren((Component)this.componentContainer);
                    if(getModel().getValue() != null)
                    {
                        if(this.imageLabelContainer != null)
                        {
                            this.componentContainer.appendChild((Component)this.imageLabelContainer);
                            this.imageLabelContainer = null;
                        }
                        else
                        {
                            this.componentContainer.appendChild((Component)prepareImageCounterpart());
                        }
                    }
                    else
                    {
                        this.autoCompleteComponent.setText("");
                        UITools.detachChildren((Component)this.componentContainer);
                        this.componentContainer.appendChild((Component)this.autoCompleteComponent);
                    }
                }
                Events.postEvent("onFinishEdit", (Component)this.autoCompleteComponent, null);
            }
            else if(mode.equals(SimpleSelectorModel.Mode.NORMAL_MODE))
            {
                if(!this.componentContainer.getChildren().contains(this.autoCompleteComponent))
                {
                    UITools.detachChildren((Component)this.componentContainer);
                    this.componentContainer.appendChild((Component)this.autoCompleteComponent);
                }
                this.autoCompleteComponent.focus();
            }
            else if(mode.equals(SimpleSelectorModel.Mode.ADVANCED_MODE))
            {
                showReferenceSelectorModalDialog();
            }
        }
    }


    protected void fireSelectorNormalMode()
    {
        if(!isDisabled())
        {
            if(isImageAsLabel())
            {
                UITools.detachChildren((Component)this.componentContainer);
                this.componentContainer.appendChild((Component)this.autoCompleteComponent);
                this.autoCompleteComponent.setText(computeLabel(getModel().getValue()));
            }
            super.fireSelectorNormalMode();
        }
    }


    public void updateItems()
    {
        if(isInitialized())
        {
            if(getModel().getValue() == null)
            {
                return;
            }
            if(isImageAsLabel())
            {
                UITools.detachChildren((Component)this.componentContainer);
                this.imageLabelContainer = prepareImageCounterpart();
                this.componentContainer.appendChild((Component)this.imageLabelContainer);
            }
        }
    }
}
