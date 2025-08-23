package de.hybris.platform.hac.facade;

import de.hybris.platform.cluster.BroadcastMethod;
import de.hybris.platform.cluster.DefaultBroadcastService;
import de.hybris.platform.cluster.PingBroadcastHandler;
import de.hybris.platform.hac.data.dto.cluster.BroadcastMethodData;
import de.hybris.platform.hac.data.dto.cluster.ClusterData;
import de.hybris.platform.hac.data.dto.cluster.NodeData;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.StringEscapeUtils;

public class HacClusterFacade
{
    public ClusterData getNodesInfo()
    {
        ClusterData clusterData = new ClusterData();
        PingBroadcastHandler pingHandler = PingBroadcastHandler.getInstance();
        DefaultBroadcastService broadcastService = DefaultBroadcastService.getInstance();
        clusterData.setClusterEnabled(broadcastService.isClusteringEnabled());
        clusterData.setClusterIslandId(broadcastService.getClusterIslandPK());
        clusterData.setClusterNodeId(broadcastService.getClusterNodeID());
        clusterData.setDynamicClusterNodeId(broadcastService.getDynamicClusterNodeID());
        clusterData.setNodes(getNodesData(pingHandler.getNodes()));
        clusterData.setBroadcastMethods(getBroadcastMethodsData(broadcastService));
        return clusterData;
    }


    public ClusterData clusterPing()
    {
        PingBroadcastHandler pingHandler = PingBroadcastHandler.getInstance();
        pingHandler.pingNodes();
        return getNodesInfo();
    }


    private List<NodeData> getNodesData(Collection<PingBroadcastHandler.NodeInfo> nodes)
    {
        List<NodeData> result = new ArrayList<>();
        for(PingBroadcastHandler.NodeInfo node : nodes)
        {
            NodeData nodeData = new NodeData();
            nodeData.setNodeIP(node.getIP());
            nodeData.setNodeID(node.getNodeID());
            nodeData.setDynamicNodeID(node.getDynamicNodeID());
            nodeData.setMethodName(StringEscapeUtils.escapeHtml(node.getMethodName()));
            result.add(nodeData);
        }
        return result;
    }


    private List<BroadcastMethodData> getBroadcastMethodsData(DefaultBroadcastService broadcastService)
    {
        List<BroadcastMethodData> result = new ArrayList<>();
        for(String methodName : broadcastService.getBroadcastMethodNames())
        {
            BroadcastMethodData methodData = new BroadcastMethodData();
            BroadcastMethod method = broadcastService.getBroadcastMethod(methodName);
            methodData.setSettings(method.getSettings());
            methodData.setName(StringEscapeUtils.escapeHtml(methodName));
        }
        return result;
    }
}
