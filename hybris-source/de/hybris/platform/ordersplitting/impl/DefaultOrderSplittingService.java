package de.hybris.platform.ordersplitting.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.numberseries.NumberSeriesManager;
import de.hybris.platform.ordersplitting.ConsignmentCreationException;
import de.hybris.platform.ordersplitting.ConsignmentService;
import de.hybris.platform.ordersplitting.OrderSplittingService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.strategy.SplittingStrategy;
import de.hybris.platform.ordersplitting.strategy.impl.OrderEntryGroup;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;

public class DefaultOrderSplittingService implements OrderSplittingService
{
    private static final Logger LOG = Logger.getLogger(DefaultOrderSplittingService.class);
    private List<SplittingStrategy> strategiesList = new LinkedList<>();
    private ModelService modelService;
    private ConsignmentService consignmentService;


    public List<SplittingStrategy> getStrategiesList()
    {
        return this.strategiesList;
    }


    public void setStrategiesList(List<SplittingStrategy> strategiesList)
    {
        this.strategiesList = strategiesList;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public List<ConsignmentModel> splitOrderForConsignment(AbstractOrderModel order, List<AbstractOrderEntryModel> orderEntryList) throws ConsignmentCreationException
    {
        List<ConsignmentModel> listConsignmentModel = splitOrderForConsignmentNotPersist(order, orderEntryList);
        for(ConsignmentModel consignment : listConsignmentModel)
        {
            this.modelService.save(consignment);
        }
        this.modelService.save(order);
        return listConsignmentModel;
    }


    public List<ConsignmentModel> splitOrderForConsignmentNotPersist(AbstractOrderModel order, List<AbstractOrderEntryModel> orderEntryList) throws ConsignmentCreationException
    {
        String orderCode;
        List<OrderEntryGroup> splitedList = new ArrayList<>();
        OrderEntryGroup tmpOrderEntryList = new OrderEntryGroup();
        tmpOrderEntryList.addAll(orderEntryList);
        splitedList.add(tmpOrderEntryList);
        if(this.strategiesList == null || this.strategiesList.isEmpty())
        {
            LOG.warn("No splitting strategies were configured!");
        }
        for(SplittingStrategy strategy : this.strategiesList)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Applying order splitting strategy : [" + strategy.getClass().getName() + "]");
            }
            splitedList = strategy.perform(splitedList);
        }
        if(order == null)
        {
            orderCode = getUniqueNumber("ORDER", 10, "GEN0001");
        }
        else
        {
            orderCode = order.getCode();
        }
        List<ConsignmentModel> consignmentList = new ArrayList<>();
        char prefixCode = 'a';
        for(OrderEntryGroup orderEntryResultList : splitedList)
        {
            ConsignmentModel cons = this.consignmentService.createConsignment(order, "" + prefixCode + prefixCode, (List)orderEntryResultList);
            prefixCode = (char)(prefixCode + 1);
            for(SplittingStrategy strategy : this.strategiesList)
            {
                strategy.afterSplitting(orderEntryResultList, cons);
            }
            consignmentList.add(cons);
        }
        return consignmentList;
    }


    public void setConsignmentService(ConsignmentService consignmentService)
    {
        this.consignmentService = consignmentService;
    }


    protected String getUniqueNumber(String code, int digits, String startValue)
    {
        try
        {
            NumberSeriesManager.getInstance().getNumberSeries(code);
        }
        catch(JaloInvalidParameterException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Invalid Parameter Exception" + e);
            }
            NumberSeriesManager.getInstance().createNumberSeries(code, startValue, 1, digits, null);
        }
        return NumberSeriesManager.getInstance().getUniqueNumber(code, digits);
    }
}
