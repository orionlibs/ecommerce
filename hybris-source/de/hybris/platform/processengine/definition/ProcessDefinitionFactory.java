package de.hybris.platform.processengine.definition;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Set;
import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;

public interface ProcessDefinitionFactory
{
    ProcessDefinition getProcessDefinition(ProcessDefinitionId paramProcessDefinitionId);


    @Deprecated(since = "ages", forRemoval = true)
    ProcessDefinition getProcessDefinition(String paramString);


    Set<String> getAllProcessDefinitionsNames();


    void add(ProcessDefinition paramProcessDefinition);


    String add(String paramString) throws IOException;


    String add(URL paramURL);


    String add(File paramFile) throws IOException;


    String add(Resource paramResource) throws IOException;


    String add(InputSource paramInputSource);


    boolean remove(ProcessDefinitionId paramProcessDefinitionId);


    @Deprecated(since = "ages", forRemoval = true)
    boolean remove(String paramString);


    boolean isProcessWaitingOnTask(ProcessDefinitionId paramProcessDefinitionId, String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    boolean isProcessWaitingOnTask(String paramString1, String paramString2);
}
