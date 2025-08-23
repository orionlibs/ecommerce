package de.hybris.platform.cmscockpit.components.editor.impl;

import de.hybris.platform.cmscockpit.components.editor.MediaReferencePreviewModel;
import de.hybris.platform.cmscockpit.components.editor.UIMediaReferencePreview;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.editor.impl.AbstractUIEditor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Map;
import org.zkoss.zk.ui.HtmlBasedComponent;

public class DefaultMediaPreviewUIEditor extends AbstractUIEditor
{
    private static final String EDITOR_TYPE = "cmsImagePreview";
    private DefaultMediaReferencePreview mediaReference;
    private final DefaultMediaReferencePreviewModel model = new DefaultMediaReferencePreviewModel();
    private DefaultMediaReferencePreviewController controller;


    public HtmlBasedComponent createViewComponent(Object initialValue, Map<String, ? extends Object> parameters, EditorListener listener)
    {
        String attributeQualifier = (String)parameters.get("attributeQualifier");
        TypedObject parentObject = (TypedObject)parameters.get("parentObject");
        this.mediaReference = new DefaultMediaReferencePreview(attributeQualifier, parentObject);
        this.model.setSource((TypedObject)initialValue);
        this.controller = new DefaultMediaReferencePreviewController((MediaReferencePreviewModel)this.model, (UIMediaReferencePreview)this.mediaReference, listener);
        this.controller.initialize();
        this.mediaReference.setModel((MediaReferencePreviewModel)this.model);
        return (HtmlBasedComponent)this.mediaReference;
    }


    public boolean isInline()
    {
        return false;
    }


    public String getEditorType()
    {
        return "cmsImagePreview";
    }
}
