package de.hybris.platform.warehousing.labels.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.DocumentPageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import java.util.Set;

public class PackContext extends CommonPrintLabelContext
{
    private Set<ConsignmentEntryModel> consignmentEntries;


    public void init(ConsignmentProcessModel businessProcessModel, DocumentPageModel documentPageModel)
    {
        super.init(businessProcessModel, documentPageModel);
        ConsignmentModel consignment = businessProcessModel.getConsignment();
        this.consignmentEntries = consignment.getConsignmentEntries();
    }


    public Set<ConsignmentEntryModel> getConsignmentEntries()
    {
        return this.consignmentEntries;
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
}
