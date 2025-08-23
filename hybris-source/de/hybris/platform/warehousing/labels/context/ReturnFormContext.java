package de.hybris.platform.warehousing.labels.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.DocumentPageModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.returns.model.ReturnProcessModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.data.shipping.ReturnForm;
import de.hybris.platform.warehousing.data.shipping.ReturnFormEntry;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.velocity.tools.generic.NumberTool;

public class ReturnFormContext extends CommonReturnDocumentContext
{
    private ReturnForm returnForm;
    private NumberTool number;
    private boolean showQuantityPurchased;


    public void init(BusinessProcessModel businessProcessModel, DocumentPageModel documentPageModel)
    {
        super.init(businessProcessModel, documentPageModel);
        setNumber(new NumberTool());
        if(businessProcessModel instanceof ConsignmentProcessModel)
        {
            ConsignmentModel consignment = ((ConsignmentProcessModel)businessProcessModel).getConsignment();
            setReturnForm(createReturnFormForConsignment(consignment));
            setShowQuantityPurchased(true);
        }
        else
        {
            ReturnRequestModel returnRequest = ((ReturnProcessModel)businessProcessModel).getReturnRequest();
            setReturnForm(createReturnFormForReturnRequest(returnRequest));
            setShowQuantityPurchased(false);
        }
    }


    protected ReturnForm createReturnFormForConsignment(ConsignmentModel consignment)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("consignment", consignment);
        ServicesUtil.validateParameterNotNullStandardMessage("consignmentEntries", consignment.getConsignmentEntries());
        ReturnForm newReturnForm = null;
        if(CollectionUtils.isNotEmpty(consignment.getConsignmentEntries()))
        {
            newReturnForm = new ReturnForm();
            newReturnForm.setFormEntries((List)consignment.getConsignmentEntries().stream().filter(Objects::nonNull)
                            .map(entry -> createReturnFormEntry(entry.getOrderEntry(), entry.getQuantity(), null))
                            .collect(Collectors.toList()));
        }
        return newReturnForm;
    }


    protected ReturnForm createReturnFormForReturnRequest(ReturnRequestModel returnRequest)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("returnRequest", returnRequest);
        ServicesUtil.validateParameterNotNullStandardMessage("returnEntries", returnRequest.getReturnEntries());
        ReturnForm newReturnForm = null;
        if(CollectionUtils.isNotEmpty(returnRequest.getReturnEntries()))
        {
            newReturnForm = new ReturnForm();
            newReturnForm.setFormEntries((List)returnRequest.getReturnEntries().stream().filter(Objects::nonNull)
                            .map(entry -> createReturnFormEntry(entry.getOrderEntry(), null, entry.getExpectedQuantity()))
                            .collect(Collectors.toList()));
        }
        return newReturnForm;
    }


    protected ReturnFormEntry createReturnFormEntry(AbstractOrderEntryModel orderEntry, Long purchasedQuantity, Long returnedQuantity)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("orderEntry", orderEntry);
        ServicesUtil.validateParameterNotNullStandardMessage("order", orderEntry.getOrder());
        ReturnFormEntry entry = new ReturnFormEntry();
        entry.setProduct(orderEntry.getProduct());
        entry.setBasePrice(orderEntry.getBasePrice());
        entry.setQuantityPurchased(purchasedQuantity);
        entry.setQuantityReturned(returnedQuantity);
        return entry;
    }


    public void setReturnForm(ReturnForm returnForm)
    {
        this.returnForm = returnForm;
    }


    public void setNumber(NumberTool number)
    {
        this.number = number;
    }


    public void setShowQuantityPurchased(boolean flag)
    {
        this.showQuantityPurchased = flag;
    }


    public ReturnForm getReturnForm()
    {
        return this.returnForm;
    }


    public NumberTool getNumber()
    {
        return this.number;
    }


    public boolean getShowQuantityPurchased()
    {
        return this.showQuantityPurchased;
    }
}
