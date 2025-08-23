package de.hybris.platform.processengine.definition;

import de.hybris.platform.processengine.constants.GeneratedProcessengineConstants;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessDefinition
{
    private final ProcessDefinitionId id;
    private final Node startNode;
    private final Node onError;
    private final Map<String, Node> nodesById;
    private final Map<String, ContextParameterDeclaration> paramDecl;
    private final String processClass;
    private final String defaultNodeGroup;
    private static final Logger LOG = LoggerFactory.getLogger(ProcessDefinition.class);


    protected ProcessDefinition(String name, Node startNode, Node onError, Map<String, Node> nodesById, Map<String, ContextParameterDeclaration> contextParameterDecl, String processClass, String defaultClusterGroup)
    {
        this(new ProcessDefinitionId(name), startNode, onError, nodesById, contextParameterDecl, processClass, defaultClusterGroup);
    }


    protected ProcessDefinition(ProcessDefinitionId id, Node startNode, Node onError, Map<String, Node> nodesById, Map<String, ContextParameterDeclaration> contextParameterDecl, String processClass, String defaultClusterGroup)
    {
        this.id = id;
        this.startNode = startNode;
        if(this.startNode == null)
        {
            throw new IllegalArgumentException("No start node specified.");
        }
        this.onError = (onError == null) ? defaultErrorNode() : onError;
        this.nodesById = nodesById;
        this.paramDecl = contextParameterDecl;
        if(this.nodesById == null || this.nodesById.isEmpty())
        {
            throw new IllegalArgumentException("No nodes specified.");
        }
        if(this.paramDecl == null)
        {
            throw new IllegalArgumentException("Parameter declaration map must not be null.");
        }
        this.processClass = processClass;
        if(processClass == null)
        {
            throw new IllegalArgumentException("Parameter processClass must not be null.");
        }
        this.defaultNodeGroup = defaultClusterGroup;
    }


    private Node defaultErrorNode()
    {
        return (Node)new EndNode(null, EndNode.Type.ERROR, "An error occurred while executing the process.");
    }


    public ProcessDefinitionId getId()
    {
        return this.id;
    }


    public String getName()
    {
        return this.id.getName();
    }


    public String getVersion()
    {
        return this.id.getVersion();
    }


    public Collection<String> getNodeIds()
    {
        return Collections.unmodifiableCollection(this.nodesById.keySet());
    }


    public Node retrieve(String id)
    {
        Node ret = this.nodesById.get(id);
        if(ret == null)
        {
            throw new NoSuchNodeException(id);
        }
        return ret;
    }


    public void start(BusinessProcessModel process)
    {
        if(!GeneratedProcessengineConstants.Enumerations.ProcessState.CREATED.equals(process.getProcessState().getCode()))
        {
            throw new IllegalArgumentException("Process '" + process.getCode() + "' was already started.");
        }
        if(!StringUtils.equals(process.getProcessDefinitionName(), getName()))
        {
            throw new IllegalArgumentException("Process '" + process.getCode() + "' has a different process defintion ('" + process
                            .getProcessDefinitionName() + "' instead of '" + getName() + "').");
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Starting process: {} on node: {}", process.getCode(), this.startNode.getId());
        }
        process.setProcessDefinitionVersion(getVersion());
        getStartNode().trigger(process);
    }


    public Node getStartNode()
    {
        return this.startNode;
    }


    public Node getOnErrorNode()
    {
        return this.onError;
    }


    public Set<String> allContextParameterNames()
    {
        return this.paramDecl.keySet();
    }


    public ContextParameterDeclaration getContextParameterDeclaration(String name)
    {
        return this.paramDecl.get(name);
    }


    public String getProcessClass()
    {
        return this.processClass;
    }


    public String getDefaultNodeGroup()
    {
        return this.defaultNodeGroup;
    }
}
