package de.hybris.platform.platformbackoffice.widgets.scriptgenerator;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.impex.enums.ScriptTypeEnum;
import de.hybris.platform.impex.model.ImpExMediaModel;
import java.util.ArrayList;
import java.util.List;

public class ExportScriptGeneratorForm
{
    private MediaModel media;
    private String script;
    private boolean documentId = true;
    private boolean includeSystemTypes = false;
    private String scriptModifier;
    private ScriptTypeEnum scriptType = ScriptTypeEnum.EXPORTSCRIPT;
    private ImpExMediaModel impExMedia;
    private List<LanguageModel> languages = new ArrayList<>();


    public List<LanguageModel> getLanguages()
    {
        return this.languages;
    }


    public void setLanguages(List<LanguageModel> languages)
    {
        this.languages = languages;
    }


    public ImpExMediaModel getImpExMedia()
    {
        return this.impExMedia;
    }


    public void setImpExMedia(ImpExMediaModel impExMedia)
    {
        this.impExMedia = impExMedia;
    }


    public ScriptTypeEnum getScriptType()
    {
        return this.scriptType;
    }


    public void setScriptType(ScriptTypeEnum scriptType)
    {
        this.scriptType = scriptType;
    }


    public MediaModel getMedia()
    {
        return this.media;
    }


    public void setMedia(MediaModel media)
    {
        this.media = media;
    }


    public String getScript()
    {
        return this.script;
    }


    public void setScript(String script)
    {
        this.script = script;
    }


    public boolean isDocumentId()
    {
        return this.documentId;
    }


    public void setDocumentId(boolean documentId)
    {
        this.documentId = documentId;
    }


    public boolean isIncludeSystemTypes()
    {
        return this.includeSystemTypes;
    }


    public void setIncludeSystemTypes(boolean includeSystemTypes)
    {
        this.includeSystemTypes = includeSystemTypes;
    }


    public String getScriptModifier()
    {
        return this.scriptModifier;
    }


    public void setScriptModifier(String scriptModifier)
    {
        this.scriptModifier = scriptModifier;
    }
}
