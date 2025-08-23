package de.hybris.platform.servicelayer.impex;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.impex.enums.ScriptTypeEnum;
import de.hybris.platform.impex.jalo.exp.generator.ScriptModifier;
import java.util.List;

public class ScriptGenerationConfig
{
    private final ScriptTypeEnum scriptType;
    private final boolean documentId;
    private final boolean includeSystemTypes;
    private final List<LanguageModel> languages;
    private final ScriptModifier scriptModifier;


    public ScriptGenerationConfig(ScriptTypeEnum scriptType, boolean documentId, boolean includeSystemTypes, List<LanguageModel> languages)
    {
        this.scriptType = scriptType;
        this.documentId = documentId;
        this.includeSystemTypes = includeSystemTypes;
        this.languages = languages;
        this.scriptModifier = null;
    }


    public ScriptGenerationConfig(ScriptTypeEnum scriptType, boolean documentId, boolean includeSystemTypes, List<LanguageModel> languages, ScriptModifier scriptModifier)
    {
        this.scriptType = scriptType;
        this.documentId = documentId;
        this.includeSystemTypes = includeSystemTypes;
        this.languages = languages;
        this.scriptModifier = scriptModifier;
    }


    public ScriptTypeEnum getScriptType()
    {
        return this.scriptType;
    }


    public boolean isDocumentId()
    {
        return this.documentId;
    }


    public boolean isIncludeSystemTypes()
    {
        return this.includeSystemTypes;
    }


    public ScriptModifier getScriptModifier()
    {
        return this.scriptModifier;
    }


    public List<LanguageModel> getLanguages()
    {
        return this.languages;
    }
}
