package de.hybris.platform.ruleengineservices.rao.providers.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.order.daos.DeliveryModeDao;
import de.hybris.platform.ruleengineservices.calculation.DeliveryCostEvaluationStrategy;
import de.hybris.platform.ruleengineservices.rao.DeliveryModeRAO;
import de.hybris.platform.ruleengineservices.rao.providers.RAOProvider;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultDeliveryModeRAOProvider implements RAOProvider
{
    private DeliveryModeDao deliveryModeDao;
    private DeliveryCostEvaluationStrategy deliveryCostEvaluationStrategy;
    private Converter<DeliveryModeModel, DeliveryModeRAO> deliveryModeRaoConverter;


    public Set expandFactModel(Object modelFact)
    {
        if(modelFact instanceof AbstractOrderModel)
        {
            AbstractOrderModel orderModel = (AbstractOrderModel)modelFact;
            Collection<DeliveryModeModel> availableDeliveryModes = getDeliveryModeDao().findAllDeliveryModes();
            if(CollectionUtils.isNotEmpty(availableDeliveryModes))
            {
                return (Set)availableDeliveryModes.stream().map(dm -> {
                    DeliveryModeRAO deliveryModeRao = (DeliveryModeRAO)getDeliveryModeRaoConverter().convert(dm);
                    BigDecimal cost = getDeliveryCostEvaluationStrategy().evaluateCost(orderModel, dm);
                    deliveryModeRao.setCost(cost);
                    deliveryModeRao.setCurrencyIsoCode(orderModel.getCurrency().getIsocode());
                    return deliveryModeRao;
                }).collect(Collectors.toSet());
            }
        }
        return Collections.emptySet();
    }


    protected DeliveryModeDao getDeliveryModeDao()
    {
        return this.deliveryModeDao;
    }


    @Required
    public void setDeliveryModeDao(DeliveryModeDao deliveryModeDao)
    {
        this.deliveryModeDao = deliveryModeDao;
    }


    @Required
    public void setDeliveryModeRaoConverter(Converter<DeliveryModeModel, DeliveryModeRAO> deliveryModeRaoConverter)
    {
        this.deliveryModeRaoConverter = deliveryModeRaoConverter;
    }


    protected Converter<DeliveryModeModel, DeliveryModeRAO> getDeliveryModeRaoConverter()
    {
        return this.deliveryModeRaoConverter;
    }


    protected DeliveryCostEvaluationStrategy getDeliveryCostEvaluationStrategy()
    {
        return this.deliveryCostEvaluationStrategy;
    }


    @Required
    public void setDeliveryCostEvaluationStrategy(DeliveryCostEvaluationStrategy deliveryCostEvaluationStrategy)
    {
        this.deliveryCostEvaluationStrategy = deliveryCostEvaluationStrategy;
    }
}
