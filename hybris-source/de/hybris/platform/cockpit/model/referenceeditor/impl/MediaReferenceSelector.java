package de.hybris.platform.cockpit.model.referenceeditor.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zhtml.Img;
import org.zkoss.zhtml.Td;
import org.zkoss.zhtml.Tr;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListitemRenderer;

public class MediaReferenceSelector extends ReferenceSelector
{
    private static final Logger log = LoggerFactory.getLogger(MediaReferenceSelector.class);
    private String imageHeightInAutocompleteList = "30px";
    private String imageHeight = "20px";


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


    protected ListitemRenderer notConfirmedListItemrenderer()
    {
        return (ListitemRenderer)new Object(this);
    }


    protected Tr createLabelRepresentation()
    {
        Tr tableRow = new Tr();
        Td firstCell = new Td();
        firstCell.setSclass("firstCell");
        firstCell.setStyle("text-align:center;vertical-align:middle");
        Td secondCell = new Td();
        secondCell.setSclass("secondCell");
        if(getModel().getValue() != null)
        {
            for(Object localValue : getModel().getItems())
            {
                Img imageLabel = new Img();
                imageLabel.setDynamicProperty("src", ((DefaultMediaReferenceSelectorModel)getModel())
                                .getDownloadUrlCachingFree(localValue));
                imageLabel.setDynamicProperty("alt", getModel().getItemLabel(localValue));
                imageLabel.setDynamicProperty("height", this.imageHeight);
                firstCell.appendChild((Component)imageLabel);
                firstCell.setParent((Component)tableRow);
                Label mnemonicLabel = new Label(((DefaultMediaReferenceSelectorModel)getModel()).getMnemonic(localValue));
                mnemonicLabel.setStyle("padding-right:5px;font-size:10px !important");
                firstCell.appendChild((Component)mnemonicLabel);
            }
        }
        secondCell.setParent((Component)tableRow);
        return tableRow;
    }


    public void showComponentPopup()
    {
        this.labelContainer.setVisible(Boolean.FALSE.booleanValue());
        this.component.setReadonly(Boolean.FALSE.booleanValue());
        this.component.setText("");
        this.component.setReadonly(Boolean.TRUE.booleanValue());
        this.component.setVisible(Boolean.TRUE.booleanValue());
        this.component.open();
        focusElement((HtmlBasedComponent)this.autoCompleteInputField);
    }
}
