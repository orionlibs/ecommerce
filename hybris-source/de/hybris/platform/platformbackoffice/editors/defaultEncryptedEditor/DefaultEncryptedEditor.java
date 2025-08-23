package de.hybris.platform.platformbackoffice.editors.defaultEncryptedEditor;

import com.hybris.cockpitng.editor.defaultpassword.DefaultPasswordEditor;
import com.hybris.cockpitng.editors.EditorContext;

public class DefaultEncryptedEditor extends DefaultPasswordEditor
{
    protected boolean withConfirmation(EditorContext<String> context)
    {
        return false;
    }
}
