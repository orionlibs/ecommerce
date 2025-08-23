package de.hybris.platform.processengine.definition;

import de.hybris.platform.processengine.spring.Action;
import de.hybris.platform.scripting.engine.ScriptExecutable;
import java.util.Map;
import java.util.Objects;

public class ScriptActionNode extends ActionNode
{
    ScriptActionNode(String nodeId, Integer clusterNodeId, String clusterNodeGroup, ScriptExecutable executableScript, ActionDefinitionContext context, boolean canJoinPreviousNode)
    {
        super(nodeId, clusterNodeId, clusterNodeGroup, (Action)new ScriptActionAdapter(executableScript, (
                        (Map)Objects.<Map>requireNonNull(context.getTransitions())).keySet()), context, canJoinPreviousNode);
    }
}
