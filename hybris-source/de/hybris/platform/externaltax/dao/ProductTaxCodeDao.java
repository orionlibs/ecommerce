package de.hybris.platform.externaltax.dao;

import de.hybris.platform.basecommerce.model.externaltax.ProductTaxCodeModel;
import java.util.Collection;
import java.util.List;

public interface ProductTaxCodeDao
{
    String getTaxCodeForProductAndAreaDirect(String paramString1, String paramString2);


    List<List<String>> getTaxCodesForProductsAndAreaDirect(Collection<String> paramCollection, String paramString);


    ProductTaxCodeModel getTaxCodeForProductAndArea(String paramString1, String paramString2);


    List<ProductTaxCodeModel> getTaxCodesForProduct(String paramString);
}
