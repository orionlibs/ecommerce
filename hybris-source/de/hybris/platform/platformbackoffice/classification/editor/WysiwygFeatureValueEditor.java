package de.hybris.platform.platformbackoffice.classification.editor;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editors.EditorContext;
import de.hybris.platform.classification.features.FeatureValue;

public class WysiwygFeatureValueEditor extends FeatureValueEditor
{
    private static final String EDITOR_ID_WYSIWYG = "com.hybris.cockpitng.editor.wysiwyg";


    protected Editor prepareEditor(EditorContext<FeatureValue> context)
    {
        Editor editor = super.prepareEditor(context);
        enableWysiwygWhenParamSet(context, editor);
        return editor;
    }


    private void enableWysiwygWhenParamSet(EditorContext<FeatureValue> context, Editor editor)
    {
        if(isWysiwygEnabled(context))
        {
            editor.setDefaultEditor("com.hybris.cockpitng.editor.wysiwyg");
        }
    }


    private boolean isWysiwygEnabled(EditorContext<FeatureValue> context)
    {
        return context.getParameterAsBoolean("enableWysiwyg", false);
    }
}
