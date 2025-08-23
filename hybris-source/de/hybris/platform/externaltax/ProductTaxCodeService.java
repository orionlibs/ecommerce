package de.hybris.platform.externaltax;

import de.hybris.platform.basecommerce.model.externaltax.ProductTaxCodeModel;
import java.util.Collection;
import java.util.Map;

public interface ProductTaxCodeService
{
    String lookupTaxCode(String paramString1, String paramString2);


    Map<String, String> lookupTaxCodes(Collection<String> paramCollection, String paramString);


    ProductTaxCodeModel getTaxCodeForProductAndArea(String paramString1, String paramString2);


    Collection<ProductTaxCodeModel> getTaxCodesForProduct(String paramString);
}
