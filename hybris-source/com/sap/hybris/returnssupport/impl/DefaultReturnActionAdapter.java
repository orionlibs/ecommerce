/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.returnssupport.impl;

import de.hybris.platform.processengine.BusinessProcessEvent;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.processengine.model.ProcessTaskModel;
import de.hybris.platform.returns.ReturnActionAdapter;
import de.hybris.platform.returns.model.ReturnRequestModel;
import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

/**
 * Specific adapter implementation for {@link ReturnActionAdapter}
 */
public class DefaultReturnActionAdapter implements ReturnActionAdapter
{
    protected static final String CONFIRM_OR_CANCEL_REFUND_ACTION_EVENT_NAME = "ConfirmOrCancelRefundEvent";
    protected static final String FAIL_CAPTURE_ACTION_EVENT_NAME = "FailCaptureActionEvent";
    protected static final String APPROVE_CANCEL_GOODS_EVENT_NAME = "ApproveOrCancelGoodsEvent";
    protected static final String WAIT_FOR_FAIL_CAPTURE_ACTION = "waitForFailCaptureAction";
    protected static final String WAIT_FOR_CONFIRM_OR_CANCEL_REFUND_ACTION = "waitForConfirmOrCancelReturnAction";
    protected static final String WAIT_FOR_GOODS_ACTION = "waitForGoodsAction";
    protected static final String FAIL_CAPTURE_EVENT = "FailCaptureActionEvent";
    protected static final String APPROVAL_CHOICE = "approveReturn";
    protected static final String ACCEPT_GOODS_CHOICE = "acceptGoods";
    protected static final String CANCEL_REFUND_CHOICE = "cancelReturn";
    protected static final String BY_PASS_CAPTURE = "bypassCapture";
    protected static final String TAX_REVERSE_EVENT_NAME = "FailTaxReverseEvent";
    private BusinessProcessService businessProcessService;


    @Override
    public void requestReturnApproval(final ReturnRequestModel returnRequest)
    {
        validateReturnRequest(returnRequest);
        returnRequest.getReturnProcess().stream()
                        .filter(process -> process.getCode().startsWith(returnRequest.getOrder().getStore().getCreateReturnProcessCode()))
                        .forEach(filteredProcess ->
                        {
                            getBusinessProcessService().triggerEvent(
                                            BusinessProcessEvent
                                                            .builder(
                                                                            filteredProcess.getCode() + "_"
                                                                                            + CONFIRM_OR_CANCEL_REFUND_ACTION_EVENT_NAME).withChoice(APPROVAL_CHOICE)
                                                            .withEventTriggeringInTheFutureDisabled().build());
                        });
    }


    @Override
    public void requestReturnReception(final ReturnRequestModel returnRequest)
    {
        validateReturnRequest(returnRequest);
        returnRequest.getReturnProcess().stream()
                        .filter(process -> process.getCode().startsWith(returnRequest.getOrder().getStore().getCreateReturnProcessCode()))
                        .forEach(filteredProcess ->
                        {
                            getBusinessProcessService().triggerEvent(
                                            BusinessProcessEvent
                                                            .builder(
                                                                            filteredProcess.getCode() + "_"
                                                                                            + APPROVE_CANCEL_GOODS_EVENT_NAME).withChoice(ACCEPT_GOODS_CHOICE)
                                                            .withEventTriggeringInTheFutureDisabled().build());
                        });
    }


    @Override
    public void requestReturnCancellation(final ReturnRequestModel returnRequest)
    {
        validateReturnRequest(returnRequest);
        returnRequest.getReturnProcess().stream()
                        .filter(process -> process.getCode().startsWith(returnRequest.getOrder().getStore().getCreateReturnProcessCode()))
                        .forEach(filteredProcess ->
                        {
                            String event = null;
                            final Collection<ProcessTaskModel> currentTasks = filteredProcess.getCurrentTasks();
                            Assert.isTrue(CollectionUtils.isNotEmpty(currentTasks),
                                            String.format("No available process tasks found for the ReturnRequest to be cancelled [%s]", returnRequest.getCode()));
                            if(currentTasks.stream().anyMatch(task -> WAIT_FOR_FAIL_CAPTURE_ACTION.equals(task.getAction())))
                            {
                                event = FAIL_CAPTURE_ACTION_EVENT_NAME;
                            }
                            else if(currentTasks.stream().anyMatch(task -> WAIT_FOR_CONFIRM_OR_CANCEL_REFUND_ACTION.equals(task.getAction())))
                            {
                                event = CONFIRM_OR_CANCEL_REFUND_ACTION_EVENT_NAME;
                            }
                            else if(currentTasks.stream().anyMatch(task -> WAIT_FOR_GOODS_ACTION.equals(task.getAction())))
                            {
                                event = APPROVE_CANCEL_GOODS_EVENT_NAME;
                            }
                            getBusinessProcessService()
                                            .triggerEvent(BusinessProcessEvent.builder(filteredProcess.getCode() + "_" + event)
                                                            .withChoice(CANCEL_REFUND_CHOICE)
                                                            .withEventTriggeringInTheFutureDisabled()
                                                            .build());
                        });
    }


    @Override
    public void requestManualPaymentReversalForReturnRequest(final ReturnRequestModel returnRequest)
    {
        validateReturnRequest(returnRequest);
        returnRequest.getReturnProcess().stream()
                        .filter(process -> process.getCode().startsWith(returnRequest.getOrder().getStore().getCreateReturnProcessCode()))
                        .forEach(filteredProcess ->
                        {
                            final BusinessProcessEvent businessProcessEvent = BusinessProcessEvent
                                            .builder(filteredProcess.getCode() + "_" + FAIL_CAPTURE_EVENT).withChoice(BY_PASS_CAPTURE)
                                            .withEventTriggeringInTheFutureDisabled().build();
                            getBusinessProcessService().triggerEvent(businessProcessEvent);
                        });
    }


    @Override
    public void requestManualTaxReversalForReturnRequest(final ReturnRequestModel returnRequest)
    {
        validateReturnRequest(returnRequest);
        returnRequest.getReturnProcess().stream()
                        .filter(process -> process.getCode().startsWith(returnRequest.getOrder().getStore().getCreateReturnProcessCode()))
                        .forEach(filteredProcess -> getBusinessProcessService().triggerEvent(filteredProcess.getCode() + "_" + TAX_REVERSE_EVENT_NAME));
    }


    /**
     * Validates {@link ReturnRequestModel} before triggering an event in returns workflow
     * @param returnRequest
     */
    protected void validateReturnRequest(final ReturnRequestModel returnRequest)
    {
        Assert.notNull(returnRequest, "ReturnRequest cannot be null");
        Assert.isTrue(CollectionUtils.isNotEmpty(returnRequest.getReturnProcess()),
                        String.format("No return process found for the ReturnRequest [%s]", returnRequest.getCode()));
        Assert.notNull(returnRequest.getOrder(), String.format("Order can not be null for the requested ReturnRequest [%s]", returnRequest.getCode()));
        Assert.notNull(returnRequest.getOrder().getStore(), String.format("Store can not be null for the requested ReturnRequest [%s]", returnRequest.getCode()));
    }


    protected BusinessProcessService getBusinessProcessService()
    {
        return businessProcessService;
    }


    @Required
    public void setBusinessProcessService(BusinessProcessService businessProcessService)
    {
        this.businessProcessService = businessProcessService;
    }
}
