/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.ysaprcomsfulfillment.actions.consignment;

import com.sap.hybris.ysaprcomsfulfillment.constants.YsaprcomsfulfillmentConstants;
import de.hybris.platform.processengine.BusinessProcessEvent;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.sap.saporderexchangeoms.model.SapConsignmentProcessModel;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

/**
 * Update the consignment process to done and notify the corresponding order
 * process that it is completed.
 */
public class SapConsignmentProcessEndAction extends AbstractProceduralAction<SapConsignmentProcessModel>
{
    private static final Logger LOG = Logger.getLogger(SapConsignmentProcessEndAction.class);
    private BusinessProcessService businessProcessService;


    protected BusinessProcessService getBusinessProcessService()
    {
        return businessProcessService;
    }


    @Required
    public void setBusinessProcessService(final BusinessProcessService businessProcessService)
    {
        this.businessProcessService = businessProcessService;
    }


    @Override
    public void executeAction(final SapConsignmentProcessModel process)
    {
        LOG.info(String.format("Process: %s in step %s", process.getCode(), getClass().getSimpleName()));
        process.setDone(true);
        save(process);
        LOG.info(String.format("Process: %s wrote DONE marker", process.getCode()));
        final String eventId = new StringBuilder()//
                        .append(process.getParentProcess().getCode())//
                        .append(YsaprcomsfulfillmentConstants.UNDERSCORE)//
                        .append(YsaprcomsfulfillmentConstants.ORDER_ACTION_EVENT_NAME)//
                        .toString();
        final BusinessProcessEvent event = BusinessProcessEvent.builder(eventId)
                        .withChoice(YsaprcomsfulfillmentConstants.CONSIGNMENT_PROCESS_ENDED).build();
        getBusinessProcessService().triggerEvent(event);
        LOG.info(String.format("Process: %s fired event %s", process.getCode(),
                        YsaprcomsfulfillmentConstants.ORDER_ACTION_EVENT_NAME));
    }
}
