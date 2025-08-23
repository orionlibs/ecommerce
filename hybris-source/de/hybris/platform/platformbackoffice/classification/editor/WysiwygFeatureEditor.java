package de.hybris.platform.platformbackoffice.classification.editor;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editors.EditorContext;
import de.hybris.platform.platformbackoffice.classification.ClassificationInfo;
import org.apache.commons.lang3.BooleanUtils;

public class WysiwygFeatureEditor extends FeatureEditor
{
    protected static final String EDITOR_PARAM_ENABLE_WYSIWYG = "enableWysiwyg";


    protected Editor prepareEditor(EditorContext<ClassificationInfo> context)
    {
        Editor editor = super.prepareEditor(context);
        editor.addParameter("valueEditor", "com.hybris.cockpitng.editor.wysiwyg");
        ClassificationInfo initialValue = (ClassificationInfo)context.getInitialValue();
        addEditorParamWhenWysiwygEnabled(editor, initialValue);
        return editor;
    }


    private void addEditorParamWhenWysiwygEnabled(Editor editor, ClassificationInfo initialValue)
    {
        if(isWysiwygEnabled(initialValue).booleanValue())
        {
            editor.addParameter("enableWysiwyg", Boolean.valueOf(true));
        }
    }


    private Boolean isWysiwygEnabled(ClassificationInfo classificationInfo)
    {
        return Boolean.valueOf((classificationInfo != null && classificationInfo.getAssignment() != null &&
                        BooleanUtils.isTrue(classificationInfo.getAssignment().getEnableWysiwygEditor())));
    }
}
