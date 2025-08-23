package de.hybris.platform.customercouponservices.sitemsg.process.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.notificationservices.enums.SiteMessageType;
import de.hybris.platform.notificationservices.model.SiteMessageModel;
import de.hybris.platform.notificationservices.processor.Processor;
import de.hybris.platform.notificationservices.strategies.SendSiteMessageStrategy;
import java.util.Map;
import org.apache.log4j.Logger;

public class CouponNotificationSiteMessageProcessor implements Processor
{
    private static final Logger LOG = Logger.getLogger(CouponNotificationSiteMessageProcessor.class);
    private Map<SiteMessageType, SendSiteMessageStrategy> sendSiteMessageStrategies;


    public void process(CustomerModel customer, Map<String, ? extends ItemModel> dataMap)
    {
        SiteMessageModel message = (SiteMessageModel)dataMap.get("siteMessage");
        sendMessage(customer, message);
        LOG.info("Send site message(uid = " + message.getUid() + ")[" + message.getNotificationType() + "] finished");
    }


    protected void sendMessage(CustomerModel customer, SiteMessageModel message)
    {
        SendSiteMessageStrategy strategy = getSendSiteMessageStrategies().get(message.getType());
        if(strategy == null)
        {
            LOG.warn("No SendSiteMessageStrategy found, message[uid=" + message.getUid() + ", type=" + message.getType() + ", notificationType=" + message
                            .getNotificationType() + "] won't be sent.");
            return;
        }
        strategy.sendMessage(customer, message);
    }


    protected Map<SiteMessageType, SendSiteMessageStrategy> getSendSiteMessageStrategies()
    {
        return this.sendSiteMessageStrategies;
    }


    public void setSendSiteMessageStrategies(Map<SiteMessageType, SendSiteMessageStrategy> sendSiteMessageStrategies)
    {
        this.sendSiteMessageStrategies = sendSiteMessageStrategies;
    }
}
