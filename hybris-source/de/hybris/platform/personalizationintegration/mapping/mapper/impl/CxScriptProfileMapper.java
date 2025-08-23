package de.hybris.platform.personalizationintegration.mapping.mapper.impl;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationintegration.mapping.MappingData;
import de.hybris.platform.personalizationintegration.mapping.SegmentMappingData;
import de.hybris.platform.personalizationintegration.model.CxMapperScriptModel;
import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptExecutionResult;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.scripting.engine.content.ScriptContent;
import de.hybris.platform.scripting.engine.content.impl.ModelScriptContent;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class CxScriptProfileMapper<T> implements Populator<T, MappingData>
{
    private static final Logger LOG = LoggerFactory.getLogger(CxScriptProfileMapper.class);
    private FlexibleSearchService flexibleSearchService;
    private ScriptingLanguagesService scriptingLanguagesService;
    private String scriptGroup;


    public void populate(T source, MappingData target)
    {
        Map<String, Object> inputParameters = createInputParameters(source);
        List<CxMapperScriptModel> scripts = findMapperScripts();
        List<SegmentMappingData> segmentMappingList = target.getSegments();
        scripts.forEach(s -> segmentMappingList.addAll(executeMapperScrip(s, inputParameters)));
    }


    protected Map<String, Object> createInputParameters(T source)
    {
        Map<String, Object> inputParameters = new HashMap<>();
        inputParameters.put("externalData", source);
        return inputParameters;
    }


    protected List<CxMapperScriptModel> findMapperScripts()
    {
        CxMapperScriptModel example = new CxMapperScriptModel();
        example.setActive(Boolean.TRUE);
        example.setDisabled(false);
        example.setGroup(this.scriptGroup);
        return this.flexibleSearchService.getModelsByExample(example);
    }


    protected List<SegmentMappingData> executeMapperScrip(CxMapperScriptModel script, Map<String, Object> inputParameters)
    {
        ModelScriptContent modelScriptContent = new ModelScriptContent((ScriptModel)script);
        ScriptExecutable executable = this.scriptingLanguagesService.getExecutableByContent((ScriptContent)modelScriptContent);
        ScriptExecutionResult result = executable.execute(inputParameters);
        if(result.getScriptResult() instanceof List)
        {
            return (List<SegmentMappingData>)result.getScriptResult();
        }
        LOG.error("script {} didn't returned a result of the expected type List", script.getCode());
        return Collections.emptyList();
    }


    protected FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    protected ScriptingLanguagesService getScriptingLanguagesService()
    {
        return this.scriptingLanguagesService;
    }


    protected String getScriptGroup()
    {
        return this.scriptGroup;
    }


    @Required
    public void setScriptingLanguagesService(ScriptingLanguagesService scriptingLanguagesService)
    {
        this.scriptingLanguagesService = scriptingLanguagesService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setScriptGroup(String scriptGroup)
    {
        this.scriptGroup = scriptGroup;
    }
}
