package de.hybris.platform.processengine.helpers;

import de.hybris.platform.processengine.enums.ProcessState;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ProcessDefinitionBuilder
{
    private Optional<ProcessDefinitionBuilder> parent = Optional.empty();
    private String header;
    private String footer;
    private List<String> endNodes = new ArrayList<>();
    private List<String> nodes = new ArrayList<>();


    private ProcessDefinitionBuilder(String header, String footer)
    {
        setHeader(header);
        setFooter(footer);
    }


    private ProcessDefinitionBuilder addNode(String node, List<String> endNodes)
    {
        this.nodes.add(node);
        if(!endNodes.isEmpty())
        {
            this.endNodes.addAll(endNodes);
        }
        return this;
    }


    private String composeWithBody()
    {
        return getBody(new StringBuilder(getHeader())).append(getFooter()).toString();
    }


    private ProcessDefinitionBuilder saveNode()
    {
        return this.parent.<ProcessDefinitionBuilder>map(p -> p.addNode(composeWithBody(), getEndNodes())).orElse(this);
    }


    protected StringBuilder getBody(StringBuilder sb)
    {
        Objects.requireNonNull(sb);
        this.nodes.forEach(sb::append);
        Objects.requireNonNull(sb);
        this.endNodes.forEach(sb::append);
        return sb;
    }


    public String build()
    {
        return saveNode().composeWithBody();
    }


    public static ProcessDefinitionBuilder builder(String procName, String startNode)
    {
        return new ProcessDefinitionBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<process xmlns=\"http://www.hybris.de/xsd/processdefinition\" name=\"" + procName + "\" start=\"" + startNode + "\">\n", "</process>");
    }


    public ActionNodeBuilder withActionNode(String id, String beanName)
    {
        return new ActionNodeBuilder(saveNode(), id, beanName);
    }


    public WaitNodeBuilder withWaitNode(String id, String then)
    {
        return new WaitNodeBuilder(saveNode(), id, then, Optional.empty());
    }


    public WaitNodeBuilder withWaitNode(String id, String then, Boolean prependProcessCode)
    {
        return new WaitNodeBuilder(saveNode(), id, then, Optional.of(prependProcessCode));
    }


    public ScriptActionNodeBuilder withScriptActionNode(String id)
    {
        return new ScriptActionNodeBuilder(saveNode(), id);
    }


    public final ProcessDefinitionBuilder addEndNode(String id, ProcessState state, String message)
    {
        this.endNodes.add("\t<end id=\"" + id + "\" state=\"" + state.getCode() + "\">" + message + "</end>\n");
        return this;
    }


    public Optional<ProcessDefinitionBuilder> getParent()
    {
        return this.parent;
    }


    public void setParent(Optional<ProcessDefinitionBuilder> parent)
    {
        this.parent = parent;
    }


    public String getHeader()
    {
        return this.header;
    }


    public void setHeader(String header)
    {
        this.header = header;
    }


    public String getFooter()
    {
        return this.footer;
    }


    public void setFooter(String footer)
    {
        this.footer = footer;
    }


    public List<String> getEndNodes()
    {
        return this.endNodes;
    }


    private ProcessDefinitionBuilder()
    {
    }
}
