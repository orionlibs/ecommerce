package de.hybris.platform.europe1.dynamic;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.enums.ProductPriceGroup;
import de.hybris.platform.europe1.model.PriceRowModel;
import java.util.Collection;
import java.util.Comparator;

public class ProductEurope1PricesAttributeHandler extends PdtRowEurope1AttributeHandler<PriceRowModel, ProductModel>
{
    protected String getType()
    {
        return this.typeService.getComposedTypeForClass(PriceRowModel.class).getCode();
    }


    protected Collection<PriceRowModel> getOwnPdtRowModels(ProductModel product)
    {
        return product.getOwnEurope1Prices();
    }


    protected void setOwnPdtRowModels(ProductModel product, Collection<PriceRowModel> priceRowModels)
    {
        product.setOwnEurope1Prices(priceRowModels);
    }


    protected PK getProductGroupPK(ProductModel product)
    {
        PK result = null;
        ProductPriceGroup productGroup = product.getEurope1PriceFactory_PPG();
        if(productGroup != null)
        {
            EnumerationValueModel pgModel = this.typeService.getEnumerationValue(productGroup.getType(), productGroup.getCode());
            result = pgModel.getPk();
        }
        return result;
    }


    protected Comparator<PriceRowModel> getPdtRowComparator()
    {
        return Comparator.comparing(Extractor::extractUserUid, String::compareToIgnoreCase)
                        .thenComparing(Extractor::extractPdtRowCode, String::compareToIgnoreCase)
                        .thenComparingInt(Extractor::extractProductPresence)
                        .thenComparingInt(Extractor::extractProductIdPresence)
                        .thenComparingInt(Extractor::extractPgPresence)
                        .thenComparing(Extractor::extractCurrencyIsoCode, String::compareToIgnoreCase)
                        .thenComparingInt(Extractor::extractNetPresence)
                        .thenComparing(Extractor::extractUnitCode, String::compareToIgnoreCase)
                        .thenComparingLong(Extractor::extractMinqtd)
                        .thenComparing(Extractor::extractPK);
    }
}
