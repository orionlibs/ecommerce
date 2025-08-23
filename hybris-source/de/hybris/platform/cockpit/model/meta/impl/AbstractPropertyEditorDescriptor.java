package de.hybris.platform.cockpit.model.meta.impl;

import de.hybris.platform.cockpit.model.meta.PropertyEditorDescriptor;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractPropertyEditorDescriptor implements PropertyEditorDescriptor
{
    private String label;
    private String previewImage;
    private String editorType;


    @Required
    public void setLabel(String label)
    {
        this.label = label;
    }


    public String getLabel()
    {
        return this.label;
    }


    public String getPreviewImage()
    {
        return this.previewImage;
    }


    public void setPreviewImage(String previewImage)
    {
        this.previewImage = previewImage;
    }


    public String getEditorType()
    {
        return this.editorType;
    }


    @Required
    public void setEditorType(String editorType)
    {
        this.editorType = editorType;
    }
}
