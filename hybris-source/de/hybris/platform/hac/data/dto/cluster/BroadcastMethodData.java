package de.hybris.platform.hac.data.dto.cluster;

import java.util.List;
import java.util.Map;

public class BroadcastMethodData
{
    private String name;
    private Map<String, String> settings;
    private List<ClusterNodeData> nodes;


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public Map<String, String> getSettings()
    {
        return this.settings;
    }


    public void setSettings(Map<String, String> settings)
    {
        this.settings = settings;
    }


    public List<ClusterNodeData> getNodes()
    {
        return this.nodes;
    }


    public void setNodes(List<ClusterNodeData> nodes)
    {
        this.nodes = nodes;
    }
}
