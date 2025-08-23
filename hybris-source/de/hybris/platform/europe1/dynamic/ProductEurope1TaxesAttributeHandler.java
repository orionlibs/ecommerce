package de.hybris.platform.europe1.dynamic;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.enums.ProductTaxGroup;
import de.hybris.platform.europe1.model.TaxRowModel;
import java.util.Collection;
import java.util.Comparator;

public class ProductEurope1TaxesAttributeHandler extends PdtRowEurope1AttributeHandler<TaxRowModel, ProductModel>
{
    protected String getType()
    {
        return this.typeService.getComposedTypeForClass(TaxRowModel.class).getCode();
    }


    public Collection<TaxRowModel> getOwnPdtRowModels(ProductModel product)
    {
        return product.getOwnEurope1Taxes();
    }


    public void setOwnPdtRowModels(ProductModel product, Collection<TaxRowModel> taxRowModels)
    {
        product.setOwnEurope1Taxes(taxRowModels);
    }


    protected PK getProductGroupPK(ProductModel product)
    {
        PK result = null;
        ProductTaxGroup productGroup = product.getEurope1PriceFactory_PTG();
        if(productGroup != null)
        {
            EnumerationValueModel pgModel = this.typeService.getEnumerationValue(productGroup.getType(), productGroup.getCode());
            result = pgModel.getPk();
        }
        return result;
    }


    protected Comparator<TaxRowModel> getPdtRowComparator()
    {
        return Comparator.comparing(Extractor::extractUserUid, String::compareToIgnoreCase)
                        .thenComparing(Extractor::extractUgCode, String::compareToIgnoreCase)
                        .thenComparingInt(Extractor::extractProductPresence)
                        .thenComparingInt(Extractor::extractPgPresence)
                        .thenComparing(Extractor::extractTaxCode, String::compareToIgnoreCase)
                        .thenComparing(Extractor::extractPK);
    }
}
