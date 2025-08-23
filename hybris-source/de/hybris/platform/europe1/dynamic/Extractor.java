package de.hybris.platform.europe1.dynamic;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.order.price.TaxModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.europe1.model.AbstractDiscountRowModel;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.europe1.model.TaxRowModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nullable;

class Extractor
{
    private static final String STRING_LAST_ENTRY = "ZZZZZZZZZZZZZZZZZZ";
    private static final Integer PRESENT = Integer.valueOf(1);
    private static final Integer ABSENT = Integer.valueOf(0);


    private Extractor()
    {
        throw new AssertionError();
    }


    static Integer extractProductPresence(@Nullable PDTRowModel o)
    {
        return Optional.<PDTRowModel>ofNullable(o)
                        .map(PDTRowModel::getProduct)
                        .<Integer>map(toPresent())
                        .orElse(ABSENT);
    }


    static Integer extractProductIdPresence(@Nullable PDTRowModel o)
    {
        return Optional.<PDTRowModel>ofNullable(o)
                        .map(PDTRowModel::getProductId)
                        .<Integer>map(toPresent())
                        .orElse(ABSENT);
    }


    static Integer extractPgPresence(@Nullable PDTRowModel o)
    {
        return Optional.<PDTRowModel>ofNullable(o)
                        .map(PDTRowModel::getPg)
                        .<Integer>map(toPresent())
                        .orElse(ABSENT);
    }


    static Integer extractNetPresence(@Nullable PriceRowModel o)
    {
        return Optional.<PriceRowModel>ofNullable(o)
                        .map(PriceRowModel::getNet)
                        .<Integer>map(toPresent())
                        .orElse(ABSENT);
    }


    private static Function<Object, Integer> toPresent()
    {
        return o -> PRESENT;
    }


    static String extractUserUid(@Nullable PDTRowModel o)
    {
        return Optional.<PDTRowModel>ofNullable(o)
                        .map(PDTRowModel::getUser)
                        .map(PrincipalModel::getUid)
                        .orElse("ZZZZZZZZZZZZZZZZZZ");
    }


    static String extractPdtRowCode(@Nullable PriceRowModel o)
    {
        return Optional.<PriceRowModel>ofNullable(o)
                        .map(PDTRowModel::getUg)
                        .map(HybrisEnumValue::getCode)
                        .orElse("ZZZZZZZZZZZZZZZZZZ");
    }


    static String extractCurrencyIsoCode(@Nullable PriceRowModel o)
    {
        return Optional.<PriceRowModel>ofNullable(o)
                        .map(PriceRowModel::getCurrency)
                        .map(C2LItemModel::getIsocode)
                        .orElse("ZZZZZZZZZZZZZZZZZZ");
    }


    static String extractCurrencyIsoCode(@Nullable DiscountRowModel o)
    {
        return Optional.<DiscountRowModel>ofNullable(o)
                        .map(AbstractDiscountRowModel::getCurrency)
                        .map(C2LItemModel::getIsocode)
                        .orElse("");
    }


    static String extractUnitCode(@Nullable PriceRowModel o)
    {
        return Optional.<PriceRowModel>ofNullable(o)
                        .map(PriceRowModel::getUnit)
                        .map(UnitModel::getCode)
                        .orElse("ZZZZZZZZZZZZZZZZZZ");
    }


    static Long extractMinqtd(@Nullable PriceRowModel o)
    {
        return Optional.<PriceRowModel>ofNullable(o)
                        .map(PriceRowModel::getMinqtd)
                        .orElse(Long.valueOf(0L));
    }


    static PK extractPK(@Nullable AbstractItemModel o)
    {
        return Optional.<AbstractItemModel>ofNullable(o)
                        .map(AbstractItemModel::getPk)
                        .orElse(PK.NULL_PK);
    }


    static String extractUgCode(@Nullable PDTRowModel o)
    {
        return Optional.<PDTRowModel>ofNullable(o)
                        .map(PDTRowModel::getUg)
                        .map(HybrisEnumValue::getCode)
                        .orElse("ZZZZZZZZZZZZZZZZZZ");
    }


    static String extractDiscountCode(@Nullable DiscountRowModel o)
    {
        return Optional.<DiscountRowModel>ofNullable(o)
                        .map(AbstractDiscountRowModel::getDiscount)
                        .map(DiscountModel::getCode)
                        .orElse("ZZZZZZZZZZZZZZZZZZ");
    }


    static String extractTaxCode(@Nullable TaxRowModel o)
    {
        return Optional.<TaxRowModel>ofNullable(o)
                        .map(TaxRowModel::getTax)
                        .map(TaxModel::getCode)
                        .orElse("");
    }
}
