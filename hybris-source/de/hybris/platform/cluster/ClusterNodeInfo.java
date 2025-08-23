package de.hybris.platform.cluster;

public class ClusterNodeInfo
{
    private final int id;
    private final String name;
    private final long createdTS;
    private final long lastPingTS;


    public ClusterNodeInfo(int id, String name)
    {
        this(id, name, System.currentTimeMillis());
    }


    public ClusterNodeInfo(int id, String name, long createdTS)
    {
        this(id, name, createdTS, createdTS);
    }


    public ClusterNodeInfo(int id, String name, long createdTS, long lastPingTS)
    {
        this.id = id;
        this.name = name;
        this.createdTS = createdTS;
        this.lastPingTS = lastPingTS;
    }


    public int getId()
    {
        return this.id;
    }


    public String getName()
    {
        return this.name;
    }


    public long getCreatedTS()
    {
        return this.createdTS;
    }


    public long getLastPingTS()
    {
        return this.lastPingTS;
    }


    public String toString()
    {
        return "ClusterNode[id:" + this.id + ",name:" + this.name + ",created:" + this.createdTS + ",lastPing:" + this.lastPingTS + "(age:" +
                        System.currentTimeMillis() - this.lastPingTS + "ms)]";
    }
}
