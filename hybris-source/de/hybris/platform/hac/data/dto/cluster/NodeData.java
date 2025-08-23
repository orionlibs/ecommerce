package de.hybris.platform.hac.data.dto.cluster;

public class NodeData
{
    private String nodeIP;
    private int nodeID;
    private long dynamicNodeID;
    private String methodName;


    public String getNodeIP()
    {
        return this.nodeIP;
    }


    public void setNodeIP(String nodeIP)
    {
        this.nodeIP = nodeIP;
    }


    public int getNodeID()
    {
        return this.nodeID;
    }


    public void setNodeID(int nodeID)
    {
        this.nodeID = nodeID;
    }


    public long getDynamicNodeID()
    {
        return this.dynamicNodeID;
    }


    public void setDynamicNodeID(long dynamicNodeID)
    {
        this.dynamicNodeID = dynamicNodeID;
    }


    public String getMethodName()
    {
        return this.methodName;
    }


    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }
}
