package de.hybris.platform.servicelayer.impex.impl;

import com.google.common.collect.ImmutableSet;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.impex.enums.ScriptTypeEnum;
import de.hybris.platform.impex.jalo.exp.generator.AbstractScriptGenerator;
import de.hybris.platform.impex.jalo.exp.generator.ExportScriptGenerator;
import de.hybris.platform.impex.jalo.exp.generator.HeaderLibraryGenerator;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.servicelayer.impex.ExportScriptGenerationService;
import de.hybris.platform.servicelayer.impex.ScriptGenerationConfig;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExportScriptGenerationService implements ExportScriptGenerationService
{
    private ModelService modelService;


    public String generateScript(ScriptGenerationConfig config)
    {
        AbstractScriptGenerator generator = createGenerator(config.getScriptType());
        initGenerator(generator, config);
        return generator.generateScript();
    }


    protected AbstractScriptGenerator createGenerator(ScriptTypeEnum scriptType)
    {
        if(scriptType.equals(ScriptTypeEnum.EXPORTSCRIPT))
        {
            return (AbstractScriptGenerator)new ExportScriptGenerator();
        }
        return (AbstractScriptGenerator)new HeaderLibraryGenerator();
    }


    protected void initGenerator(AbstractScriptGenerator generator, ScriptGenerationConfig config)
    {
        generator.useDocumentID(config.isDocumentId());
        generator.includeSystemTypes(config.isIncludeSystemTypes());
        if(config.getScriptModifier() != null)
        {
            generator.registerScriptModifier(config.getScriptModifier());
        }
        generator.setLanguages(convertLanguagesToJalo(config.getLanguages()));
    }


    private Set<Language> convertLanguagesToJalo(List<LanguageModel> languages)
    {
        ImmutableSet.Builder<Language> languageBuilder = new ImmutableSet.Builder();
        for(LanguageModel lang : languages)
        {
            languageBuilder.add(this.modelService.getSource(lang));
        }
        return (Set<Language>)languageBuilder.build();
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
