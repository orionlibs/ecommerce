package de.hybris.platform.warehousing.labels.context;

import de.hybris.platform.acceleratorservices.document.context.AbstractDocumentContext;
import de.hybris.platform.acceleratorservices.model.cms2.pages.DocumentPageModel;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.returns.model.ReturnProcessModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import de.hybris.platform.warehousing.sourcing.context.PosSelectionStrategy;
import org.apache.velocity.tools.generic.DateTool;
import org.apache.velocity.tools.generic.EscapeTool;
import org.springframework.beans.factory.annotation.Required;

public class CommonReturnDocumentContext extends AbstractDocumentContext<BusinessProcessModel>
{
    private DateTool date;
    private AbstractOrderModel order;
    private AddressModel address;
    private PosSelectionStrategy posSelectionStrategy;
    private EscapeTool escapeTool;


    public void init(BusinessProcessModel businessProcessModel, DocumentPageModel documentPageModel)
    {
        WarehouseModel warehouse;
        validateProcessType(businessProcessModel);
        super.init(businessProcessModel, documentPageModel);
        setDate(new DateTool());
        AbstractOrderModel orderForProcess = getOrder(businessProcessModel);
        if(businessProcessModel instanceof ConsignmentProcessModel)
        {
            warehouse = ((ConsignmentProcessModel)businessProcessModel).getConsignment().getWarehouse();
        }
        else
        {
            warehouse = ((ReturnProcessModel)businessProcessModel).getReturnRequest().getReturnWarehouse();
        }
        setAddress(getPosAddress(orderForProcess, warehouse));
        setOrder(orderForProcess);
        this.escapeTool = new EscapeTool();
    }


    public String escapeHtml(String stringToEscape)
    {
        return this.escapeTool.html(stringToEscape);
    }


    protected AddressModel getPosAddress(AbstractOrderModel order, WarehouseModel returnWarehouse)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("order", order);
        ServicesUtil.validateParameterNotNullStandardMessage("returnWarehouse", returnWarehouse);
        PointOfServiceModel pointOfService = getPosSelectionStrategy().getPointOfService(order, returnWarehouse);
        AddressModel posAddress = null;
        if(pointOfService != null && pointOfService.getAddress() != null)
        {
            posAddress = pointOfService.getAddress();
        }
        return posAddress;
    }


    protected AbstractOrderModel getOrder(BusinessProcessModel businessProcessModel)
    {
        OrderModel orderModel;
        validateProcessType(businessProcessModel);
        if(businessProcessModel instanceof ConsignmentProcessModel)
        {
            ConsignmentModel consignment = ((ConsignmentProcessModel)businessProcessModel).getConsignment();
            ServicesUtil.validateParameterNotNullStandardMessage("consignment", consignment);
            AbstractOrderModel orderForProcess = consignment.getOrder();
        }
        else
        {
            ReturnRequestModel returnRequest = ((ReturnProcessModel)businessProcessModel).getReturnRequest();
            ServicesUtil.validateParameterNotNullStandardMessage("returnRequest", returnRequest);
            orderModel = returnRequest.getOrder();
        }
        return (AbstractOrderModel)orderModel;
    }


    protected BaseSiteModel getSite(BusinessProcessModel businessProcessModel)
    {
        AbstractOrderModel orderForProcess = getOrder(businessProcessModel);
        ServicesUtil.validateParameterNotNullStandardMessage("orderForProcess", orderForProcess);
        return orderForProcess.getSite();
    }


    protected LanguageModel getDocumentLanguage(BusinessProcessModel businessProcessModel)
    {
        BaseSiteModel site = getSite(businessProcessModel);
        ServicesUtil.validateParameterNotNullStandardMessage("site", site);
        return site.getDefaultLanguage();
    }


    protected void validateProcessType(BusinessProcessModel businessProcessModel)
    {
        if(!(businessProcessModel instanceof ConsignmentProcessModel) && !(businessProcessModel instanceof ReturnProcessModel))
        {
            throw new IllegalArgumentException("businessProcessModel is not an instance of ConsignmentProcessModel nor ReturnProcessModel");
        }
    }


    public void setDate(DateTool date)
    {
        this.date = date;
    }


    public void setOrder(AbstractOrderModel order)
    {
        this.order = order;
    }


    public void setAddress(AddressModel address)
    {
        this.address = address;
    }


    public AbstractOrderModel getOrder()
    {
        return this.order;
    }


    public DateTool getDate()
    {
        return this.date;
    }


    public AddressModel getAddress()
    {
        return this.address;
    }


    protected PosSelectionStrategy getPosSelectionStrategy()
    {
        return this.posSelectionStrategy;
    }


    @Required
    public void setPosSelectionStrategy(PosSelectionStrategy posSelectionStrategy)
    {
        this.posSelectionStrategy = posSelectionStrategy;
    }
}
