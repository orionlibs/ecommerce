package de.hybris.platform.personalizationfacades.customersegmentation;

import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationfacades.data.CustomerSegmentationData;
import de.hybris.platform.personalizationfacades.data.SegmentData;
import java.util.List;

public interface CustomerSegmentationFacade
{
    SearchPageData<CustomerSegmentationData> getCustomerSegmentations(String paramString1, String paramString2, String paramString3, SearchPageData<?> paramSearchPageData);


    CustomerSegmentationData getCustomerSegmentation(String paramString);


    CustomerSegmentationData createCustomerSegmentation(CustomerSegmentationData paramCustomerSegmentationData);


    CustomerSegmentationData updateCustomerSegmentation(CustomerSegmentationData paramCustomerSegmentationData);


    void deleteCustomerSegmentation(String paramString);


    List<SegmentData> getSegmentsForCurrentUser();
}
