package de.hybris.platform.hac.data.dto.cluster;

public class ClusterNodeData
{
    private String lastSeenFormattedDate;
    private String lastAttemptFormattedDate;
    private String serverAddress;
    private int clusterNodeId;


    public String getLastSeenFormattedDate()
    {
        return this.lastSeenFormattedDate;
    }


    public void setLastSeenFormattedDate(String lastSeenFormattedDate)
    {
        this.lastSeenFormattedDate = lastSeenFormattedDate;
    }


    public String getLastAttemptFormattedDate()
    {
        return this.lastAttemptFormattedDate;
    }


    public void setLastAttemptFormattedDate(String lastAttemptFormattedDate)
    {
        this.lastAttemptFormattedDate = lastAttemptFormattedDate;
    }


    public String getServerAddress()
    {
        return this.serverAddress;
    }


    public void setServerAddress(String serverAddress)
    {
        this.serverAddress = serverAddress;
    }


    public int getClusterNodeId()
    {
        return this.clusterNodeId;
    }


    public void setClusterNodeId(int clusterNodeId)
    {
        this.clusterNodeId = clusterNodeId;
    }
}
