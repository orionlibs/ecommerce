package de.hybris.platform.platformbackoffice.classification;

import com.hybris.cockpitng.editor.instant.labelprovider.impl.AbstractInstantEditorLabelProvider;
import com.hybris.cockpitng.editors.EditorUtils;
import com.hybris.cockpitng.labels.LabelService;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationInfoLabelProvider extends AbstractInstantEditorLabelProvider
{
    private LabelService labelService;


    public boolean canHandle(String editorType)
    {
        return EditorUtils.getFeatureEditorType().equals(editorType);
    }


    public String getLabel(String editorType, Object value)
    {
        if(value instanceof ClassificationInfo)
        {
            ClassificationInfo classificationInfo = (ClassificationInfo)value;
            return (classificationInfo.getValue() == null) ? "" :
                            getLabelService().getObjectLabel(classificationInfo.getValue());
        }
        return "";
    }


    protected LabelService getLabelService()
    {
        return this.labelService;
    }


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }
}
