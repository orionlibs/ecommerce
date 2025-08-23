package de.hybris.platform.europe1.dynamic;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.enums.ProductDiscountGroup;
import de.hybris.platform.europe1.model.DiscountRowModel;
import java.util.Collection;
import java.util.Comparator;

public class ProductEurope1DiscountsAttributeHandler extends PdtRowEurope1AttributeHandler<DiscountRowModel, ProductModel>
{
    protected String getType()
    {
        return this.typeService.getComposedTypeForClass(DiscountRowModel.class).getCode();
    }


    protected PK getProductGroupPK(ProductModel product)
    {
        PK result = null;
        ProductDiscountGroup productGroup = product.getEurope1PriceFactory_PDG();
        if(productGroup != null)
        {
            EnumerationValueModel pgModel = this.typeService.getEnumerationValue(productGroup.getType(), productGroup.getCode());
            result = pgModel.getPk();
        }
        return result;
    }


    protected Collection<DiscountRowModel> getOwnPdtRowModels(ProductModel product)
    {
        return product.getOwnEurope1Discounts();
    }


    protected void setOwnPdtRowModels(ProductModel product, Collection<DiscountRowModel> discountRowModels)
    {
        product.setOwnEurope1Discounts(discountRowModels);
    }


    protected Comparator<DiscountRowModel> getPdtRowComparator()
    {
        return Comparator.comparing(Extractor::extractUserUid, String::compareToIgnoreCase)
                        .thenComparing(Extractor::extractUgCode, String::compareToIgnoreCase)
                        .thenComparingInt(Extractor::extractProductPresence)
                        .thenComparingInt(Extractor::extractProductIdPresence)
                        .thenComparingInt(Extractor::extractPgPresence)
                        .thenComparing(Extractor::extractDiscountCode, String::compareToIgnoreCase)
                        .thenComparing(Extractor::extractCurrencyIsoCode, String::compareToIgnoreCase)
                        .thenComparing(Extractor::extractPK);
    }
}
