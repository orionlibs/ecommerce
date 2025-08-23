package de.hybris.platform.processengine.definition;

public class NoSuchNodeException extends RuntimeException
{
    private final String nodeId;


    NoSuchNodeException(String nodeId)
    {
        super("Invalid process definition. Unknown node id '" + nodeId + "'.");
        this.nodeId = nodeId;
    }


    public String getNodeId()
    {
        return this.nodeId;
    }
}
