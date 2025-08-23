package de.hybris.platform.processengine.definition;

import java.util.Map;

class XMLProcessDefinition extends ProcessDefinition
{
    XMLProcessDefinition(ProcessDefinitionId id, String start, String onError, Map<String, Node> nodesById, Map<String, ContextParameterDeclaration> paramDecl, String processClass, String defaultClusterGroup)
    {
        super(id, nodesById.get(start), (onError == null) ? null : nodesById.get(onError), nodesById, paramDecl, processClass, defaultClusterGroup);
    }
}
