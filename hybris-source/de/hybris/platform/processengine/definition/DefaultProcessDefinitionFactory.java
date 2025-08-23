package de.hybris.platform.processengine.definition;

import com.google.common.collect.ImmutableSet;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.xml.sax.InputSource;

public class DefaultProcessDefinitionFactory implements ProcessDefinitionFactory, InitializingBean, ApplicationContextAware
{
    private static final Log LOG = LogFactory.getLog(DefaultProcessDefinitionFactory.class.getName());
    private ProcessDefinitionsCache definitionsCache = null;
    private ApplicationContext applicationContext;
    private XMLProcessDefinitionsReader xmlDefinitionsReader;


    public ProcessDefinition getProcessDefinition(ProcessDefinitionId id)
    {
        ProcessDefinition ret = getProcessDefinitions().find(id);
        if(ret == null)
        {
            throw new NoSuchProcessDefinitionException(id.toString());
        }
        return ret;
    }


    public ProcessDefinition getProcessDefinition(String processDefinitionName)
    {
        return getProcessDefinition(new ProcessDefinitionId(processDefinitionName));
    }


    public String add(String location) throws IOException
    {
        Resource resource = this.applicationContext.getResource(location);
        return add(resource);
    }


    public void add(ProcessDefinition definition)
    {
        getProcessDefinitions().addActiveProcessDefinitions(new ProcessDefinition[] {definition});
    }


    public String add(URL definitionURL)
    {
        try
        {
            return add((Resource)new UrlResource(definitionURL));
        }
        catch(IOException e)
        {
            throw new InvalidProcessDefinitionException(e.getMessage(), e);
        }
    }


    public String add(File definitionFile) throws IOException
    {
        return add(definitionFile.toURI().toURL());
    }


    public String add(Resource resource) throws IOException
    {
        InputStream inputStream = null;
        try
        {
            inputStream = resource.getInputStream();
            return add(new InputSource(inputStream));
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
    }


    public String add(InputSource inputSource)
    {
        ProcessDefinition processDefinition = this.xmlDefinitionsReader.from(inputSource);
        add(processDefinition);
        return processDefinition.getName();
    }


    public boolean remove(ProcessDefinitionId id)
    {
        return (getProcessDefinitions().remove(Objects.<ProcessDefinitionId>requireNonNull(id)) != null);
    }


    public boolean remove(String processName)
    {
        return remove(new ProcessDefinitionId(Objects.<String>requireNonNull(processName)));
    }


    public void afterPropertiesSet() throws Exception
    {
    }


    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }


    public void setDefinitionsCache(ProcessDefinitionsCache definitionsCache)
    {
        this.definitionsCache = definitionsCache;
    }


    public void setXmlDefinitionsReader(XMLProcessDefinitionsReader xmlDefinitionsReader)
    {
        this.xmlDefinitionsReader = xmlDefinitionsReader;
    }


    public boolean isProcessWaitingOnTask(ProcessDefinitionId id, String action)
    {
        Node curr = getProcessDefinition(id).retrieve(action);
        if(curr == null)
        {
            return false;
        }
        return curr instanceof WaitNode;
    }


    public boolean isProcessWaitingOnTask(String processDefinitionName, String action)
    {
        return isProcessWaitingOnTask(new ProcessDefinitionId(processDefinitionName), action);
    }


    public Set<String> getAllProcessDefinitionsNames()
    {
        ImmutableSet.Builder<String> resultBuilder = ImmutableSet.builder();
        for(ProcessDefinitionId id : getProcessDefinitions().getAllIds())
        {
            resultBuilder.add(id.toString());
        }
        return (Set<String>)resultBuilder.build();
    }


    private ProcessDefinitionsCache getProcessDefinitions()
    {
        return this.definitionsCache;
    }
}
