package de.hybris.platform.warehousing.cancellation.impl;

import de.hybris.platform.commerceservices.util.GuidKeyGenerator;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ordercancel.OrderCancelEntry;
import de.hybris.platform.ordercancel.OrderCancelException;
import de.hybris.platform.ordercancel.OrderCancelService;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.ordercancel.model.OrderEntryCancelRecordEntryModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.warehousing.cancellation.CancellationException;
import de.hybris.platform.warehousing.cancellation.OmsOrderCancelService;
import de.hybris.platform.warehousing.comment.WarehousingCommentService;
import de.hybris.platform.warehousing.data.cancellation.OmsUnallocatedCancellationRemainder;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentContext;
import de.hybris.platform.warehousing.data.comment.WarehousingCommentEventType;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultOmsOrderCancelService implements OmsOrderCancelService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOmsOrderCancelService.class);
    protected static final String COMMENT_SUBJECT = "Cancel order entry";
    private WarehousingCommentService orderEntryCommentService;
    private GuidKeyGenerator guidKeyGenerator;
    private ModelService modelService;
    private OrderCancelService orderCancelService;
    private UserService userService;


    public List<OrderCancelEntry> processOrderCancel(OrderCancelRecordEntryModel orderCancelRecordEntryModel) throws OrderCancelException
    {
        OrderModel order = orderCancelRecordEntryModel.getModificationRecord().getOrder();
        LOGGER.info("Processing cancel order with code : {}", order.getCode());
        List<OrderEntryCancelRecordEntryModel> orderEntryCancellationRecords = (List<OrderEntryCancelRecordEntryModel>)orderCancelRecordEntryModel.getOrderEntriesModificationEntries().stream().filter(entry -> entry instanceof OrderEntryCancelRecordEntryModel)
                        .map(cancelEntry -> (OrderEntryCancelRecordEntryModel)cancelEntry).collect(Collectors.toList());
        checkIncomingCancellationRecordsMatchOrderEntries(order, orderEntryCancellationRecords);
        checkCancellationQuantitiesOnConsignments(order, orderEntryCancellationRecords);
        List<OmsUnallocatedCancellationRemainder> unallocatedCancellationRemainders = cancelUnallocatedQuantities(orderEntryCancellationRecords);
        return extractCancellationEntriesForAllocatedQuantities(unallocatedCancellationRemainders);
    }


    protected void checkCancellationQuantitiesOnConsignments(OrderModel order, List<OrderEntryCancelRecordEntryModel> orderCancelRecordEntries)
    {
        Map<AbstractOrderEntryModel, Long> allCancelableEntries = getOrderCancelService().getAllCancelableEntries(order, (PrincipalModel)getUserService().getCurrentUser());
        if(allCancelableEntries.isEmpty() || orderCancelRecordEntries.stream()
                        .anyMatch(orderCancelRecordEntry -> (((allCancelableEntries.get(orderCancelRecordEntry.getOrderEntry()) == null) ? 0L : ((Long)allCancelableEntries.get(orderCancelRecordEntry.getOrderEntry())).longValue()) < orderCancelRecordEntry.getCancelRequestQuantity().intValue())))
        {
            throw new CancellationException("Requested order cancellation can not be processed because you are trying to cancel for more than the available quantity.");
        }
    }


    protected void checkIncomingCancellationRecordsMatchOrderEntries(OrderModel order, List<OrderEntryCancelRecordEntryModel> orderEntryCancellationRecords)
    {
        orderEntryCancellationRecords.stream().forEach(orderEntryCancellationRecord -> order.getEntries().stream().filter(()).findFirst().orElseThrow(()));
    }


    protected List<OmsUnallocatedCancellationRemainder> cancelUnallocatedQuantities(List<OrderEntryCancelRecordEntryModel> orderEntryCancellationRecords)
    {
        List<OmsUnallocatedCancellationRemainder> remainders = new ArrayList<>();
        orderEntryCancellationRecords.stream()
                        .forEach(orderEntryCancellationRecord -> cancelUnallocatedAndAddRemainder(remainders, orderEntryCancellationRecord));
        getModelService().saveAll(orderEntryCancellationRecords);
        return remainders;
    }


    protected void cancelUnallocatedAndAddRemainder(List<OmsUnallocatedCancellationRemainder> remainders, OrderEntryCancelRecordEntryModel orderEntryCancellationRecord)
    {
        if(orderEntryCancellationRecord.getCancelRequestQuantity().intValue() > orderEntryCancellationRecord.getOrderEntry().getQuantity().longValue())
        {
            throw new CancellationException("Cannot cancel more than the original order entry quantity.");
        }
        Integer requestedCancelQuantity = Optional.<Integer>ofNullable(orderEntryCancellationRecord.getCancelRequestQuantity()).orElse(Integer.valueOf(0));
        Integer unallocatedQuantity = Integer.valueOf(orderEntryCancellationRecord.getOrderEntry().getQuantityUnallocated().intValue());
        Integer quantityToCancel = (requestedCancelQuantity.intValue() > unallocatedQuantity.intValue()) ? unallocatedQuantity : requestedCancelQuantity;
        Integer cancellationRemainder = Integer.valueOf(requestedCancelQuantity.intValue() - unallocatedQuantity.intValue());
        if(quantityToCancel.intValue() > 0)
        {
            Long previousOrderEntryQuantity = orderEntryCancellationRecord.getOrderEntry().getQuantity();
            Integer previousCancelledQty = (orderEntryCancellationRecord.getCancelledQuantity() != null) ? orderEntryCancellationRecord.getCancelledQuantity() : Integer.valueOf(0);
            Integer newCancelledQty = Integer.valueOf(previousCancelledQty.intValue() + quantityToCancel.intValue());
            orderEntryCancellationRecord.setCancelledQuantity(newCancelledQty);
            orderEntryCancellationRecord.getOrderEntry().setQuantity(Long.valueOf(previousOrderEntryQuantity.longValue() - quantityToCancel.intValue()));
            if(!Objects.isNull(orderEntryCancellationRecord.getNotes()))
            {
                WarehousingCommentContext commentContext = new WarehousingCommentContext();
                commentContext.setCommentType(WarehousingCommentEventType.CANCEL_ORDER_COMMENT);
                commentContext.setItem((ItemModel)orderEntryCancellationRecord.getOrderEntry());
                commentContext.setSubject("Cancel order entry");
                commentContext.setText(orderEntryCancellationRecord.getNotes());
                String code = "cancellation_" + getGuidKeyGenerator().generate().toString();
                getOrderEntryCommentService().createAndSaveComment(commentContext, code);
            }
        }
        if(cancellationRemainder.intValue() > 0)
        {
            OmsUnallocatedCancellationRemainder unallocatedCancellationRemainder = new OmsUnallocatedCancellationRemainder();
            unallocatedCancellationRemainder.setOrderEntryCancellationRecord(orderEntryCancellationRecord);
            unallocatedCancellationRemainder.setRemainingQuantity(cancellationRemainder);
            remainders.add(unallocatedCancellationRemainder);
        }
    }


    protected List<OrderCancelEntry> extractCancellationEntriesForAllocatedQuantities(List<OmsUnallocatedCancellationRemainder> unallocatedCancellationRemainders)
    {
        List<OrderCancelEntry> orderCancelEntries = new ArrayList<>();
        for(OmsUnallocatedCancellationRemainder unallocatedCancellationRemainder : unallocatedCancellationRemainders)
        {
            OrderCancelEntry newCancellationEntry = new OrderCancelEntry((AbstractOrderEntryModel)unallocatedCancellationRemainder.getOrderEntryCancellationRecord().getOrderEntry(), unallocatedCancellationRemainder.getRemainingQuantity().longValue(),
                            unallocatedCancellationRemainder.getOrderEntryCancellationRecord().getNotes(), unallocatedCancellationRemainder.getOrderEntryCancellationRecord().getCancelReason());
            orderCancelEntries.add(newCancellationEntry);
        }
        return orderCancelEntries;
    }


    protected WarehousingCommentService getOrderEntryCommentService()
    {
        return this.orderEntryCommentService;
    }


    @Required
    public void setOrderEntryCommentService(WarehousingCommentService orderEntryCommentService)
    {
        this.orderEntryCommentService = orderEntryCommentService;
    }


    protected GuidKeyGenerator getGuidKeyGenerator()
    {
        return this.guidKeyGenerator;
    }


    @Required
    public void setGuidKeyGenerator(GuidKeyGenerator guidKeyGenerator)
    {
        this.guidKeyGenerator = guidKeyGenerator;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected OrderCancelService getOrderCancelService()
    {
        return this.orderCancelService;
    }


    @Required
    public void setOrderCancelService(OrderCancelService orderCancelService)
    {
        this.orderCancelService = orderCancelService;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
