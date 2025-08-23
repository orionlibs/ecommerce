package de.hybris.platform.customercouponservices.email.process.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponservices.model.CouponNotificationModel;
import de.hybris.platform.customercouponservices.model.CouponNotificationProcessModel;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.notificationservices.processor.Processor;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Map;

public class DefaultCouponNotificationEmailProcessor implements Processor
{
    private ModelService modelService;
    private BusinessProcessService businessProcessService;


    protected BusinessProcessService getBusinessProcessService()
    {
        return this.businessProcessService;
    }


    public void setBusinessProcessService(BusinessProcessService businessProcessService)
    {
        this.businessProcessService = businessProcessService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void process(CustomerModel customer, Map<String, ? extends ItemModel> dataMap)
    {
        LanguageModel language = (LanguageModel)dataMap.get("language");
        CouponNotificationModel couponNotification = (CouponNotificationModel)dataMap.get("Couponnotification");
        CouponNotificationProcessModel couponNotificationProcessModel = (CouponNotificationProcessModel)getBusinessProcessService().createProcess("couponNotificationEmailProcess-" + customer.getUid() + "-" + System.currentTimeMillis() + "-" +
                        Thread.currentThread().getId(), "couponNotificationEmailProcess");
        couponNotificationProcessModel.setLanguage(language);
        couponNotificationProcessModel.setCouponNotification(couponNotification);
        ItemModel notifycationType = dataMap.get("notificationType");
        couponNotificationProcessModel
                        .setNotificationType((NotificationType)notifycationType.getProperty("notificationType"));
        getModelService().save(couponNotificationProcessModel);
        getBusinessProcessService().startProcess((BusinessProcessModel)couponNotificationProcessModel);
    }
}
