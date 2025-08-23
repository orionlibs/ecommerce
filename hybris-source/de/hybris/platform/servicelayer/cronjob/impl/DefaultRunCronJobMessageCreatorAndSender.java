package de.hybris.platform.servicelayer.cronjob.impl;

import de.hybris.platform.cluster.legacy.LegacyBroadcastHandler;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.cluster.ClusterService;
import de.hybris.platform.servicelayer.cronjob.RunCronJobMessageCreatorAndSender;
import de.hybris.platform.servicelayer.tenant.TenantService;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRunCronJobMessageCreatorAndSender implements RunCronJobMessageCreatorAndSender
{
    private static final String PREFIX = "CRONJOB";
    private static final String COMMAND_START = "START";
    private static final String DELIMITER = "|";
    private TenantService tenantService;
    private ClusterService clusterService;
    private LegacyBroadcastHandler legacyBroadcastHandler;


    @Required
    public void setTenantService(TenantService tenantService)
    {
        this.tenantService = tenantService;
    }


    @Required
    public void setClusterService(ClusterService clusterService)
    {
        this.clusterService = clusterService;
    }


    @Required
    public void setLegacyBroadcastHandler(LegacyBroadcastHandler legacyBroadcastHandler)
    {
        this.legacyBroadcastHandler = legacyBroadcastHandler;
    }


    private String createCronJobBroadcastMessage(int remoteClusterId, PK cronJobPK)
    {
        StringBuilder text = new StringBuilder();
        text.append("START").append("|");
        text.append(this.clusterService.getClusterId()).append("|");
        text.append(remoteClusterId).append("|");
        text.append(this.tenantService.getCurrentTenantId()).append("|");
        text.append(cronJobPK.toString());
        return text.toString();
    }


    protected String createCronJobBroadcastMessagePrefix()
    {
        return "CRONJOB_" + this.clusterService.getClusterIslandId();
    }


    public final void createAndSendMessage(int remoteClusterId, PK cronJobPK)
    {
        String message = createCronJobBroadcastMessage(remoteClusterId, cronJobPK);
        String messagePrefix = createCronJobBroadcastMessagePrefix();
        this.legacyBroadcastHandler.sendCustomPacket(messagePrefix, message);
    }
}
