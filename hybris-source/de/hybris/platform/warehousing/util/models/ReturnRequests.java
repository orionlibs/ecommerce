package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.basecommerce.enums.RefundReason;
import de.hybris.platform.basecommerce.enums.ReturnAction;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.warehousing.util.builder.RefundEntryModelBuilder;
import de.hybris.platform.warehousing.util.builder.ReturnRequestModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Required;

public class ReturnRequests extends AbstractItems<ReturnRequestModel>
{
    public static final String CODE_CAMERA_RETURN = "camera-return";
    public static final BigDecimal REFUND_AMOUNT = new BigDecimal(50.0D);
    public static final Long REFUND_EXPECTED_QUANTITY = Long.valueOf(1L);
    private Orders orders;
    private WarehousingDao<ConsignmentModel> warehousingConsignmentDao;
    private WarehousingDao<ReturnRequestModel> warehousingReturnRequestDao;


    public ReturnRequestModel Camera_OnlineReturn(Long quantity)
    {
        return (ReturnRequestModel)getOrSaveAndReturn(() -> (ReturnRequestModel)getWarehousingReturnRequestDao().getByCode("camera-return"), () -> {
            RefundEntryModel entry = Camera(quantity, ReturnAction.HOLD);
            ReturnRequestModel returnRequest = ReturnRequestModelBuilder.aModel().withCode("camera-return").withReturnEntries(new ReturnEntryModel[] {(ReturnEntryModel)entry}).withOrder((OrderModel)entry.getOrderEntry().getOrder()).build();
            entry.setReturnRequest(returnRequest);
            return returnRequest;
        });
    }


    public RefundEntryModel Camera(Long quantity, ReturnAction returnAction)
    {
        return RefundEntryModelBuilder.aModel().withOrderEntry(getOrders().Camera_Shipped(quantity).getEntries().get(0))
                        .withReason(RefundReason.WRONGDESCRIPTION).withAmount(REFUND_AMOUNT)
                        .withExpectedQTY(REFUND_EXPECTED_QUANTITY).withStatus(ReturnStatus.RECEIVED)
                        .withAction(returnAction).build();
    }


    protected WarehousingDao<ConsignmentModel> getWarehousingConsignmentDao()
    {
        return this.warehousingConsignmentDao;
    }


    @Required
    public void setWarehousingConsignmentDao(WarehousingDao<ConsignmentModel> warehousingConsignmentDao)
    {
        this.warehousingConsignmentDao = warehousingConsignmentDao;
    }


    protected WarehousingDao<ReturnRequestModel> getWarehousingReturnRequestDao()
    {
        return this.warehousingReturnRequestDao;
    }


    @Required
    public void setWarehousingReturnRequestDao(WarehousingDao<ReturnRequestModel> warehousingReturnRequestDao)
    {
        this.warehousingReturnRequestDao = warehousingReturnRequestDao;
    }


    protected Orders getOrders()
    {
        return this.orders;
    }


    @Required
    public void setOrders(Orders orders)
    {
        this.orders = orders;
    }
}
