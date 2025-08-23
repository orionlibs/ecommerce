package de.hybris.platform.customersupportbackoffice.editor.simpleselect;

import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.editor.commonreferenceeditor.AbstractReferenceEditor;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLayout;
import com.hybris.cockpitng.editor.commonreferenceeditor.ReferenceEditorLogic;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.ListitemRenderer;

public class SimpleSelectLayout<T, K> extends ReferenceEditorLayout<T>
{
    protected static final String CSS_REFERENCE_EDITOR_REMOVE_BTN = "ye-default-reference-editor-remove-button";
    protected static final String CSS_REFERENCE_EDITOR_SELECTED_ITEM_LABEL = "ye-default-reference-editor-selected-item-label";
    protected static final String CSS_REFERENCE_EDITOR_SELECTED_ITEM_CONTAINER = "ye-default-reference-editor-selected-item-container";
    protected static final String YTESTID_REMOVE_BUTTON = "reference-editor-remove-button";
    protected static final String YTESTID_REFERENCE_ENTRY = "reference-editor-reference";
    private final AbstractReferenceEditor<T, K> referenceEditor;


    public SimpleSelectLayout(AbstractReferenceEditor<T, K> referenceEditorInterface, Base configuration)
    {
        super((ReferenceEditorLogic)referenceEditorInterface, configuration);
        this.referenceEditor = referenceEditorInterface;
    }


    protected ListitemRenderer<T> createSelectedItemsListItemRenderer()
    {
        return (item, data, index) -> {
            Label label = new Label(this.referenceEditor.getStringRepresentationOfObject(data));
            label.setSclass("ye-default-reference-editor-selected-item-label");
            YTestTools.modifyYTestId((Component)label, "reference-editor-reference");
            label.setMultiline(true);
            Div removeImage = new Div();
            removeImage.setSclass("ye-default-reference-editor-remove-button");
            YTestTools.modifyYTestId((Component)removeImage, "reference-editor-remove-button");
            removeImage.setVisible(this.referenceEditor.isEditable());
            removeImage.addEventListener("onClick", ());
            Div layout = new Div();
            layout.setSclass("ye-default-reference-editor-selected-item-container");
            UITools.modifySClass((HtmlBasedComponent)label, "ye-editor-disabled", true);
            layout.appendChild((Component)label);
            if(!this.referenceEditor.isDisableRemoveReference())
            {
                layout.appendChild((Component)removeImage);
                UITools.modifySClass((HtmlBasedComponent)layout, "ye-remove-enabled", true);
            }
            Listcell cell = new Listcell();
            if(!this.referenceEditor.isDisableDisplayingDetails())
            {
                cell.addEventListener("onDoubleClick", ());
            }
            cell.appendChild((Component)layout);
            cell.setParent((Component)item);
        };
    }
}
