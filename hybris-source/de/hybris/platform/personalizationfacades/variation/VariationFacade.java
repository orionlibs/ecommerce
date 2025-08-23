package de.hybris.platform.personalizationfacades.variation;

import de.hybris.platform.personalizationfacades.data.VariationData;
import java.util.List;

public interface VariationFacade
{
    VariationData getVariation(String paramString1, String paramString2, String paramString3, String paramString4);


    List<VariationData> getVariations(String paramString1, String paramString2, String paramString3);


    VariationData createVariation(String paramString1, VariationData paramVariationData, String paramString2, String paramString3);


    VariationData updateVariation(String paramString1, String paramString2, VariationData paramVariationData, String paramString3, String paramString4);


    void deleteVariation(String paramString1, String paramString2, String paramString3, String paramString4);
}
