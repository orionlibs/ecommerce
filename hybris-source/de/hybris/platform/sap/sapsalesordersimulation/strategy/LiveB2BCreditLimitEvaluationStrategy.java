/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapsalesordersimulation.strategy;

import de.hybris.platform.b2b.enums.MerchantCheckStatus;
import de.hybris.platform.b2b.model.B2BCreditCheckResultModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BMerchantCheckResultModel;
import de.hybris.platform.b2b.strategies.impl.DefaultB2BCreditLimitEvaluationStrategy;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.sap.core.configuration.model.SAPConfigurationModel;
import de.hybris.platform.sap.sapsalesordersimulation.service.SalesOrderSimulationService;
import de.hybris.platform.sap.sapsalesordersimulation.service.SapSimulateSalesOrderEnablementService;
import de.hybris.platform.store.BaseStoreModel;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

public class LiveB2BCreditLimitEvaluationStrategy extends DefaultB2BCreditLimitEvaluationStrategy
{
    private static final Logger LOG = Logger.getLogger(DefaultB2BCreditLimitEvaluationStrategy.class);
    private SalesOrderSimulationService salesOrderSimulationService;
    private SapSimulateSalesOrderEnablementService sapSimulateSalesOrderEnablementService;


    @Override
    public Set<B2BMerchantCheckResultModel> evaluate(final AbstractOrderModel order, final B2BCustomerModel customer)
    {
        boolean isCreditCheckActiveForOrder = false;
        final BaseStoreModel basestore = order.getStore();
        final SAPConfigurationModel sapconfiguration = basestore.getSAPConfiguration();
        if(sapconfiguration != null && sapconfiguration.getSaplivecreditcheckactive())
        {
            isCreditCheckActiveForOrder = true;
        }
        if(isCreditCheckActiveForOrder)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Evaluating LiveB2BCreditLimitEvaluationStrategy for Customer: " + customer.getUid());
            }
            boolean creditExceeded = getSalesOrderSimulationService().checkCreditLimitExceeded(order, customer);
            final Set<B2BMerchantCheckResultModel> merchantCheckResults = new HashSet<>();
            final B2BCreditCheckResultModel creditLimitResult = this.getModelService()
                            .create(B2BCreditCheckResultModel.class);
            creditLimitResult.setCurrency(order.getCurrency());
            if(creditExceeded)
            {
                creditLimitResult.setStatus(MerchantCheckStatus.REJECTED);
            }
            else
            {
                creditLimitResult.setStatus(MerchantCheckStatus.APPROVED);
            }
            merchantCheckResults.add(creditLimitResult);
            return merchantCheckResults;
        }
        else
        {
            return super.evaluate(order, customer);
        }
    }


    protected SalesOrderSimulationService getSalesOrderSimulationService()
    {
        return salesOrderSimulationService;
    }


    public void setSalesOrderSimulationService(SalesOrderSimulationService salesOrderSimulationService)
    {
        this.salesOrderSimulationService = salesOrderSimulationService;
    }


    protected SapSimulateSalesOrderEnablementService getSapSimulateSalesOrderEnablementService()
    {
        return sapSimulateSalesOrderEnablementService;
    }


    public void setSapSimulateSalesOrderEnablementService(
                    SapSimulateSalesOrderEnablementService sapSimulateSalesOrderEnablementService)
    {
        this.sapSimulateSalesOrderEnablementService = sapSimulateSalesOrderEnablementService;
    }
}
