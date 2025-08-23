package de.hybris.platform.warehousing.labels.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.DocumentPageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.warehousing.inventoryevent.service.InventoryEventService;
import de.hybris.platform.warehousing.model.AllocationEventModel;
import java.util.Collection;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class PickSlipContext extends CommonPrintLabelContext
{
    private Set<ConsignmentEntryModel> consignmentEntries;
    private AddressModel shippingAddress;
    private InventoryEventService inventoryEventService;


    public void init(ConsignmentProcessModel businessProcessModel, DocumentPageModel documentPageModel)
    {
        super.init(businessProcessModel, documentPageModel);
        ConsignmentModel consignment = businessProcessModel.getConsignment();
        this.consignmentEntries = consignment.getConsignmentEntries();
        this.shippingAddress = consignment.getShippingAddress();
    }


    public String extractBin(ConsignmentEntryModel consignmentEntryModel)
    {
        String binLocation = "";
        StringBuilder bins = new StringBuilder();
        Collection<AllocationEventModel> events = this.inventoryEventService.getAllocationEventsForConsignmentEntry(consignmentEntryModel);
        events.stream().filter(e -> (e.getStockLevel().getBin() != null))
                        .forEach(e -> bins.append(e.getStockLevel().getBin()).append(","));
        if(bins.length() > 0)
        {
            binLocation = bins.substring(0, bins.length() - 1);
        }
        return binLocation;
    }


    public String getProductImageURL(ConsignmentEntryModel consignmentEntryModel)
    {
        String path = null;
        MediaModel media = consignmentEntryModel.getOrderEntry().getProduct().getThumbnail();
        if(media != null)
        {
            path = media.getDownloadURL();
        }
        return path;
    }


    public AddressModel getShippingAddress()
    {
        return this.shippingAddress;
    }


    public Set<ConsignmentEntryModel> getConsignmentEntries()
    {
        return this.consignmentEntries;
    }


    protected InventoryEventService getInventoryEventService()
    {
        return this.inventoryEventService;
    }


    @Required
    public void setInventoryEventService(InventoryEventService inventoryEventService)
    {
        this.inventoryEventService = inventoryEventService;
    }
}
