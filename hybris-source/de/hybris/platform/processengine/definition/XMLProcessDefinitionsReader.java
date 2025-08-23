package de.hybris.platform.processengine.definition;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.processengine.definition.xml.Action;
import de.hybris.platform.processengine.definition.xml.Case;
import de.hybris.platform.processengine.definition.xml.Choice;
import de.hybris.platform.processengine.definition.xml.ContextParameter;
import de.hybris.platform.processengine.definition.xml.End;
import de.hybris.platform.processengine.definition.xml.Join;
import de.hybris.platform.processengine.definition.xml.Notify;
import de.hybris.platform.processengine.definition.xml.ObjectFactory;
import de.hybris.platform.processengine.definition.xml.Parameter;
import de.hybris.platform.processengine.definition.xml.ParameterUse;
import de.hybris.platform.processengine.definition.xml.Process;
import de.hybris.platform.processengine.definition.xml.Script;
import de.hybris.platform.processengine.definition.xml.ScriptAction;
import de.hybris.platform.processengine.definition.xml.Split;
import de.hybris.platform.processengine.definition.xml.TargetNode;
import de.hybris.platform.processengine.definition.xml.Transition;
import de.hybris.platform.processengine.definition.xml.UserGroupType;
import de.hybris.platform.processengine.definition.xml.Wait;
import de.hybris.platform.scripting.engine.ScriptExecutable;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.scripting.engine.content.ScriptContent;
import de.hybris.platform.scripting.engine.content.impl.SimpleScriptContent;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.security.SecureXmlUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamReader;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.io.Resource;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLProcessDefinitionsReader
{
    private static final Log LOG = LogFactory.getLog(XMLProcessDefinitionsReader.class.getName());
    public static final String UNKNOWN_VERSION = Long.toString(Long.MAX_VALUE);
    private final ScriptingLanguagesService scriptingLanguagesService;
    private volatile Schema schema;


    public XMLProcessDefinitionsReader(ScriptingLanguagesService scriptingLanguagesService)
    {
        this.scriptingLanguagesService = Objects.<ScriptingLanguagesService>requireNonNull(scriptingLanguagesService);
    }


    private Schema getSchema() throws SAXException
    {
        if(this.schema == null)
        {
            synchronized(this)
            {
                if(this.schema == null)
                {
                    this.schema = loadSchema();
                }
            }
        }
        return this.schema;
    }


    private Schema loadSchema() throws SAXException
    {
        SchemaFactory fac = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema", "com.sun.org.apache.xerces.internal.jaxp.validation.XMLSchemaFactory", null);
        fac.setProperty("http://javax.xml.XMLConstants/property/accessExternalSchema", "");
        fac.setProperty("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
        return fac.newSchema(getClass().getResource("/processengine/processdefinition.xsd"));
    }


    public ProcessDefinition from(Resource resource) throws IOException
    {
        Preconditions.checkNotNull(resource);
        InputStream inputStream = null;
        try
        {
            inputStream = resource.getInputStream();
            return from(new InputSource(inputStream));
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
    }


    public ProcessDefinition from(InputSource inputSource)
    {
        Preconditions.checkNotNull(inputSource);
        try
        {
            String content = getContentFrom(inputSource);
            return from(new InputSource(new StringReader(content)), DigestUtils.sha256Hex(content));
        }
        catch(IOException e)
        {
            Logger.getLogger(getClass()).fatal("Failed to load content: " + e.getMessage(), e);
            throw new IllegalStateException("Internal error.", e);
        }
    }


    public ProcessDefinition from(Resource resource, String version) throws IOException
    {
        Preconditions.checkNotNull(resource);
        Preconditions.checkNotNull(version);
        InputStream inputStream = null;
        try
        {
            inputStream = resource.getInputStream();
            return from(new InputSource(inputStream), version);
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
    }


    public ProcessDefinition from(InputSource inputSource, String version)
    {
        Preconditions.checkNotNull(inputSource);
        Preconditions.checkNotNull(version);
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class.getPackage().getName());
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            unmarshaller.setSchema(getSchema());
            XMLStreamReader reader = SecureXmlUtils.getSecureXmlFactory().createXMLStreamReader(inputSource.getCharacterStream());
            try
            {
                Process process = (Process)unmarshaller.unmarshal(reader);
                return (ProcessDefinition)new XMLProcessDefinition(buildId(process, version), process.getStart(), process.getOnError(),
                                buildNodesById(process), buildContextParameterDecl(process), process
                                .getProcessClass(), process
                                .getDefaultNodeGroup());
            }
            finally
            {
                SecureXmlUtils.closeReaderQuietly(reader);
            }
        }
        catch(SAXException | javax.xml.stream.XMLStreamException e)
        {
            Logger.getLogger(getClass()).fatal("Failed to load schema: " + e.getMessage(), e);
            throw new IllegalStateException("Internal error.", e);
        }
        catch(JAXBException e)
        {
            throw new InvalidProcessDefinitionException(e.getMessage(), e);
        }
    }


    private String getContentFrom(InputSource inputSource) throws IOException
    {
        Preconditions.checkNotNull(inputSource);
        Preconditions.checkArgument((inputSource.getCharacterStream() != null || inputSource.getByteStream() != null));
        if(inputSource.getCharacterStream() != null)
        {
            return IOUtils.toString(inputSource.getCharacterStream());
        }
        if(inputSource.getEncoding() != null)
        {
            return IOUtils.toString(inputSource.getByteStream(), inputSource.getEncoding());
        }
        return IOUtils.toString(inputSource.getByteStream());
    }


    private ProcessDefinitionId buildId(Process process, String version)
    {
        Preconditions.checkNotNull(process);
        Preconditions.checkNotNull(version);
        return new ProcessDefinitionId(process.getName(), version);
    }


    private Map<String, Node> buildNodesById(Process process)
    {
        ImmutableMap.Builder<String, Node> result = ImmutableMap.builder();
        for(Object n : process.getNodes())
        {
            result.putAll(createNodes(process, n));
        }
        return (Map<String, Node>)result.build();
    }


    private Map<String, ContextParameterDeclaration> buildContextParameterDecl(Process process)
    {
        Map<String, ContextParameterDeclaration> ret = new TreeMap<>();
        for(ContextParameter p : process.getContextParameter())
        {
            try
            {
                ContextParameterDeclaration cpd = new ContextParameterDeclaration(p.getName(), (p.getUse() == ParameterUse.REQUIRED), p.getType());
                ret.put(cpd.getName(), cpd);
            }
            catch(ClassNotFoundException e)
            {
                throw new InvalidProcessDefinitionException("Invalid type '" + p.getType() + "' is not known.", e);
            }
        }
        return Collections.unmodifiableMap(ret);
    }


    private Map<String, Node> createNodes(Process process, Object obj)
    {
        if(obj instanceof Action)
        {
            return createActionNode(process, obj);
        }
        if(obj instanceof ScriptAction)
        {
            return createScriptActionNode(obj);
        }
        if(obj instanceof End)
        {
            return createEndNode(obj);
        }
        if(obj instanceof Join)
        {
            return createJoinNode(obj);
        }
        if(obj instanceof Split)
        {
            return createSplitNode(obj);
        }
        if(obj instanceof Wait)
        {
            return createWaitNode(obj);
        }
        if(obj instanceof Notify)
        {
            return createNotifyNodes(obj);
        }
        throw new UnsupportedOperationException("Unrecognized node '" + obj.getClass().getName() + "'.");
    }


    private Map<String, Node> createScriptActionNode(Object obj)
    {
        ScriptAction action = (ScriptAction)obj;
        Script script = action.getScript();
        SimpleScriptContent simpleScriptContent = new SimpleScriptContent(script.getType(), script.getValue());
        ScriptExecutable executableScript = this.scriptingLanguagesService.getExecutableByContent((ScriptContent)simpleScriptContent);
        boolean canJoinPreviousNode = getDefaultValueForCanJoinPreviousNode(action.isCanJoinPreviousNode());
        ScriptActionNode actionNode = new ScriptActionNode(action.getId(), action.getNode(), action.getNodeGroup(), executableScript, getActionDefinitionContext(action.getTransition(), action
                        .getParameter()), canJoinPreviousNode);
        return (Map<String, Node>)ImmutableMap.of(action.getId(), actionNode);
    }


    private Map<String, Node> createEndNode(Object obj)
    {
        End end = (End)obj;
        return (Map<String, Node>)ImmutableMap.of(end.getId(), new EndNode(end
                        .getId(), EndNode.Type.valueOf(end.getState().name()), end
                        .getValue()));
    }


    private Map<String, Node> createJoinNode(Object obj)
    {
        Join join = (Join)obj;
        return (Map<String, Node>)ImmutableMap.of(join.getId(), new JoinNode(join.getId(), join.getThen()));
    }


    private Map<String, Node> createSplitNode(Object obj)
    {
        Split split = (Split)obj;
        split.getTargetNode();
        Set<String> setSuccesors = new TreeSet<>();
        for(TargetNode t : split.getTargetNode())
        {
            setSuccesors.add(t.getName());
        }
        return (Map<String, Node>)ImmutableMap.of(split.getId(), new SplitNode(split.getId(), setSuccesors));
    }


    private Map<String, Node> createWaitNode(Object obj)
    {
        WaitNode.TimeoutConfiguration timeout;
        Wait wait = (Wait)obj;
        Map<String, String> choices = (Map<String, String>)((List)Optional.<Case>ofNullable(wait.getCase()).map(Case::getChoice).orElse(ImmutableList.of())).stream().collect(Collectors.toMap(Choice::getId, Choice::getThen));
        String event = Optional.<Case>ofNullable(wait.getCase()).map(Case::getEvent).orElse(wait.getEvent());
        if(wait.getTimeout() != null)
        {
            Duration duration = Duration.parse(wait.getTimeout().getDelay().toString());
            timeout = WaitNode.TimeoutConfiguration.of(duration, wait.getTimeout().getThen());
        }
        else
        {
            timeout = WaitNode.TimeoutConfiguration.none();
        }
        WaitNode waitNode = new WaitNode(wait.getId(), event, wait.getThen(), wait.isPrependProcessCode(), (Map)ImmutableMap.copyOf(choices), timeout);
        return (Map<String, Node>)ImmutableMap.of(wait.getId(), waitNode);
    }


    private Map<String, Node> createNotifyNodes(Object obj)
    {
        ImmutableMap.Builder<String, Node> result = ImmutableMap.builder();
        Notify notify = (Notify)obj;
        WaitNode wait = new WaitNode("V_" + notify.getId(), "V_" + notify.getId(), notify.getThen(), true, (Map)ImmutableMap.of(), WaitNode.TimeoutConfiguration.none());
        result.put("V_" + notify.getId(), wait);
        List<UserGroupType> userGroup = new ArrayList<>(notify.getUserGroup());
        result.put(notify.getId(), new NotifyUserGroupNode(notify.getId(), userGroup, wait));
        return (Map<String, Node>)result.build();
    }


    private Map<String, Node> createActionNode(Process process, Object obj)
    {
        Action action = (Action)obj;
        try
        {
            boolean canJoinPreviousNode = getDefaultValueForCanJoinPreviousNode(action.isCanJoinPreviousNode());
            ActionNode actionNode = new ActionNode(action.getId(), action.getNode(), action.getNodeGroup(), action.getBean(), getActionDefinitionContext(action.getTransition(), action
                            .getParameter()), canJoinPreviousNode);
            return (Map<String, Node>)ImmutableMap.of(action.getId(), actionNode);
        }
        catch(NoSuchBeanDefinitionException ex)
        {
            LOG.error("Failed to resolve action bean [" + action.getBean() + "] for Action [" + action.getId() + "] in process [" + process
                            .getName() + "]", (Throwable)ex);
            throw ex;
        }
    }


    private ActionDefinitionContext getActionDefinitionContext(List<Transition> transactions, List<Parameter> parameters)
    {
        return
                        ActionDefinitionContext.builder()
                                        .withParameters(extractParameters(parameters))
                                        .withTransitions(extractTransitions(transactions))
                                        .build();
    }


    private static boolean getDefaultValueForCanJoinPreviousNode(Boolean canJoinPreviousNode)
    {
        if(canJoinPreviousNode != null)
        {
            return canJoinPreviousNode.booleanValue();
        }
        return Config.getBoolean("processengine.process.canjoinpreviousnode.default", true);
    }


    private Map<String, String> extractTransitions(List<Transition> transition)
    {
        return (Map<String, String>)transition.stream().collect(Collectors.toMap(Transition::getName, Transition::getTo, (v1, v2) -> v2));
    }


    private Map<String, String> extractParameters(List<Parameter> parameters)
    {
        return (Map<String, String>)parameters.stream().collect(Collectors.toMap(Parameter::getName, Parameter::getValue, (v1, v2) -> v2));
    }
}
