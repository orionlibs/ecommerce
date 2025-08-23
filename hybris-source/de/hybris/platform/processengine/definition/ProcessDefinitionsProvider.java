package de.hybris.platform.processengine.definition;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Longs;
import de.hybris.platform.processengine.model.DynamicProcessDefinitionModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.xml.sax.InputSource;

public class ProcessDefinitionsProvider implements ApplicationContextAware
{
    private static final String LATEST_DEFINITION_QUERY = "select {PK} from {DynamicProcessDefinition} where {code} = ?code and {active} = ?active";
    private static final String DEFINITION_QUERY = "select {PK} from {DynamicProcessDefinition} where {code} = ?code and {version} = ?version";
    private ApplicationContext applicationContext;
    private final XMLProcessDefinitionsReader xmlDefinitionsReader;
    private final FlexibleSearchService flexibleSearchService;


    public ProcessDefinitionsProvider(XMLProcessDefinitionsReader xmlDefinitionsReader, FlexibleSearchService flexibleSearchService)
    {
        this.xmlDefinitionsReader = xmlDefinitionsReader;
        this.flexibleSearchService = flexibleSearchService;
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = Objects.<ApplicationContext>requireNonNull(applicationContext);
    }


    public Iterable<ProcessDefinition> getInitialActiveDefinitions()
    {
        ImmutableList.Builder<ProcessDefinition> resultBuilder = ImmutableList.builder();
        try
        {
            for(ProcessDefinitionResource resource : getProcessDefinitionResources().values())
            {
                ProcessDefinition processDefinition = this.xmlDefinitionsReader.from(resource.getResource());
                resultBuilder.add(processDefinition);
            }
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
        return (Iterable<ProcessDefinition>)resultBuilder.build();
    }


    public ProcessDefinitionId getLatestDefinitionIdFor(ProcessDefinitionId id)
    {
        Preconditions.checkNotNull(id);
        List<DynamicProcessDefinitionModel> result = this.flexibleSearchService.search("select {PK} from {DynamicProcessDefinition} where {code} = ?code and {active} = ?active", (Map)ImmutableMap.of("code", id.getName(), "active", Boolean.TRUE)).getResult();
        Preconditions.checkState((result.size() <= 1));
        return (result.size() == 0) ? null : new ProcessDefinitionId(((DynamicProcessDefinitionModel)result.get(0)).getCode(), ((DynamicProcessDefinitionModel)result
                        .get(0)).getVersion().toString());
    }


    public ProcessDefinition getDefinition(ProcessDefinitionId id)
    {
        Preconditions.checkNotNull(id);
        if(id.isActive())
        {
            ProcessDefinitionId latestId = getLatestDefinitionIdFor(id);
            if(latestId == null)
            {
                return null;
            }
            Preconditions.checkState(id.isHistorical());
            return getDefinition(latestId);
        }
        Long version = Longs.tryParse(id.getVersion());
        List<DynamicProcessDefinitionModel> definitions = this.flexibleSearchService.search("select {PK} from {DynamicProcessDefinition} where {code} = ?code and {version} = ?version", (Map)ImmutableMap.of("code", id.getName(), "version", (version == null) ? Long.valueOf(Long.MIN_VALUE) : version))
                        .getResult();
        for(DynamicProcessDefinitionModel definition : definitions)
        {
            if(definition != null)
            {
                StringReader definitionReader = new StringReader(definition.getContent());
                try
                {
                    ProcessDefinition result = this.xmlDefinitionsReader.from(new InputSource(definitionReader), definition
                                    .getVersion().toString());
                    Preconditions.checkState(id.equals(result.getId()), "" + id + " is not equal to " + id);
                    ProcessDefinition processDefinition1 = result;
                    definitionReader.close();
                    return processDefinition1;
                }
                catch(Throwable throwable)
                {
                    try
                    {
                        definitionReader.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
            }
        }
        return null;
    }


    private Map<String, ProcessDefinitionResource> getProcessDefinitionResources()
    {
        return this.applicationContext.getBeansOfType(ProcessDefinitionResource.class);
    }
}
