package de.hybris.platform.personalizationfacades.customization;

import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationfacades.data.CustomizationData;
import java.util.List;
import java.util.Map;

public interface CustomizationFacade
{
    List<CustomizationData> getCustomizations(String paramString1, String paramString2);


    SearchPageData<CustomizationData> getCustomizations(String paramString1, String paramString2, Map<String, String> paramMap, SearchPageData<?> paramSearchPageData);


    CustomizationData getCustomization(String paramString1, String paramString2, String paramString3);


    CustomizationData createCustomization(CustomizationData paramCustomizationData, String paramString1, String paramString2);


    CustomizationData createCustomizationWithRelatedObjects(CustomizationData paramCustomizationData, String paramString1, String paramString2);


    CustomizationData updateCustomization(String paramString1, CustomizationData paramCustomizationData, String paramString2, String paramString3);


    CustomizationData updateCustomizationWithRelatedObjects(String paramString1, CustomizationData paramCustomizationData, String paramString2, String paramString3);


    void removeCustomization(String paramString1, String paramString2, String paramString3);
}
