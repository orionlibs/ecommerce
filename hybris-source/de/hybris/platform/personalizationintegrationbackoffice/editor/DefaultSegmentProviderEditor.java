package de.hybris.platform.personalizationintegrationbackoffice.editor;

import com.hybris.cockpitng.editor.defaultmultireferenceeditor.DefaultMultiReferenceEditor;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

public class DefaultSegmentProviderEditor extends DefaultMultiReferenceEditor<String>
{
    private static final Pattern REGEX_EDITOR_PATTERN = Pattern.compile("^(Collection|List|Set)\\((java.lang.String)\\)$");


    protected void initializeSelectedItemsCollection(String collectionType)
    {
        super.initializeSelectedItemsCollection(StringUtils.upperCase(collectionType));
    }


    protected Pattern getRegexEditorPattern()
    {
        return REGEX_EDITOR_PATTERN;
    }


    public boolean allowNestedObjectCreation()
    {
        return false;
    }
}
