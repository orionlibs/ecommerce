package de.hybris.platform.promotions.util;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.order.price.Discount;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.TaxValue;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.variants.jalo.VariantProduct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.log4j.Logger;

public class Helper
{
    private static final Logger log = Logger.getLogger(Helper.class.getName());


    public static Date buildDateForYear(int year)
    {
        return (new GregorianCalendar(year, 0, 1)).getTime();
    }


    public static String formatCurrencyAmount(SessionContext ctx, Locale locale, Currency currency, double amount)
    {
        if(ctx != null && locale != null)
        {
            if(currency == null)
            {
                currency = ctx.getCurrency();
            }
            NumberFormat localisedNumberFormat = NumberFormat.getCurrencyInstance(locale);
            String currencyIsoCode = currency.getIsocode(ctx);
            Currency javaCurrency = Currency.getInstance(currencyIsoCode);
            if(javaCurrency == null)
            {
                log.warn("formatCurrencyAmount failed to lookup java.util.Currency from [" + currencyIsoCode + "] ensure this is an ISO 4217 code and is supported by the java runtime.");
            }
            else
            {
                localisedNumberFormat.setCurrency(javaCurrency);
            }
            adjustDigits((DecimalFormat)localisedNumberFormat, currency);
            adjustSymbol((DecimalFormat)localisedNumberFormat, currency);
            String result = localisedNumberFormat.format(amount);
            if(log.isDebugEnabled())
            {
                log.debug("formatCurrencyAmount locale=[" + locale + "] currency=[" + currency + "] amount=[" + amount + "] currencyIsoCode=[" + currencyIsoCode + "] javaCurrency=[" + javaCurrency + "] result=[" + result + "]");
            }
            return result;
        }
        return "";
    }


    protected static DecimalFormat adjustDigits(DecimalFormat format, Currency currency)
    {
        int tempDigits = (currency.getDigits() == null) ? 0 : currency.getDigits().intValue();
        int digits = Math.max(0, tempDigits);
        format.setMaximumFractionDigits(digits);
        format.setMinimumFractionDigits(digits);
        if(digits == 0)
        {
            format.setDecimalSeparatorAlwaysShown(false);
        }
        return format;
    }


    protected static DecimalFormat adjustSymbol(DecimalFormat format, Currency currency)
    {
        String symbol = currency.getSymbol();
        if(symbol != null)
        {
            DecimalFormatSymbols symbols = format.getDecimalFormatSymbols();
            String iso = currency.getIsocode();
            boolean changed = false;
            if(!iso.equalsIgnoreCase(symbols.getInternationalCurrencySymbol()))
            {
                symbols.setInternationalCurrencySymbol(iso);
                changed = true;
            }
            if(!symbol.equals(symbols.getCurrencySymbol()))
            {
                symbols.setCurrencySymbol(symbol);
                changed = true;
            }
            if(changed)
            {
                format.setDecimalFormatSymbols(symbols);
            }
        }
        return format;
    }


    protected static BigDecimal getSmallestCurrencyUnit(SessionContext ctx, Currency currency)
    {
        long factor = 1L;
        int currencyDigits = currency.getDigits(ctx).intValue();
        for(int i = 0; i < currencyDigits; i++)
        {
            factor *= 10L;
        }
        return BigDecimal.ONE.setScale(currencyDigits, RoundingMode.HALF_EVEN)
                        .divide(BigDecimal.valueOf(factor), RoundingMode.HALF_EVEN).setScale(currencyDigits, RoundingMode.HALF_EVEN);
    }


    public static BigDecimal roundCurrencyValue(SessionContext ctx, Currency currency, BigDecimal amount)
    {
        return amount.setScale(currency.getDigits(ctx).intValue(), RoundingMode.HALF_EVEN);
    }


    public static BigDecimal roundCurrencyValue(SessionContext ctx, Currency currency, double amount)
    {
        return BigDecimal.valueOf(amount).setScale(currency.getDigits(ctx).intValue(), RoundingMode.HALF_EVEN);
    }


    public static void adjustUnitPrices(SessionContext ctx, PromotionEvaluationContext promoContext, List<PromotionOrderEntryConsumed> consumedEntries, double targetTotal, double originalTotal)
    {
        if(log.isDebugEnabled())
        {
            log.debug("adjustUnitPrices consumedEntries.size=[" + consumedEntries.size() + "] targetTotal=[" + targetTotal + "] originalTotal=[" + originalTotal + "]");
        }
        Currency currency = promoContext.getOrder().getCurrency(ctx);
        double discountRatio = targetTotal / originalTotal;
        for(PromotionOrderEntryConsumed poec : consumedEntries)
        {
            double originalUnitPrice = poec.getUnitPrice(ctx);
            BigDecimal adjustedUnitPrice = roundCurrencyValue(ctx, currency, originalUnitPrice * discountRatio);
            poec.setAdjustedUnitPrice(ctx, adjustedUnitPrice.doubleValue());
        }
        BigDecimal targetTotalDecimal = roundCurrencyValue(ctx, currency, targetTotal);
        BigDecimal newTotalDecimal = calculateOrderEntryAdjustedTotal(ctx, currency, consumedEntries);
        BigDecimal remainderAmountDecimal = targetTotalDecimal.subtract(newTotalDecimal);
        if(remainderAmountDecimal.compareTo(BigDecimal.ZERO) != 0)
        {
            BigDecimal smallestCurrencyUnit = getSmallestCurrencyUnit(ctx, currency);
            long unallocatedRemainderUnits = roundCurrencyValue(ctx, currency, remainderAmountDecimal.abs()).divideToIntegralValue(smallestCurrencyUnit).longValueExact();
            BigDecimal smallestCurrencyUnitAdjustment = smallestCurrencyUnit;
            if(remainderAmountDecimal.compareTo(BigDecimal.ZERO) < 0)
            {
                smallestCurrencyUnitAdjustment = smallestCurrencyUnit.negate();
            }
            SortedSet<PromotionOrderEntryConsumed> orderEntriesSortedByQuantityDescending = new TreeSet<>((a, b) -> b.getQuantity(ctx).compareTo(a.getQuantity(ctx)));
            orderEntriesSortedByQuantityDescending.addAll(consumedEntries);
            for(PromotionOrderEntryConsumed poec : orderEntriesSortedByQuantityDescending)
            {
                long quantity = poec.getQuantity(ctx).longValue();
                if(quantity > 0L && quantity <= unallocatedRemainderUnits)
                {
                    double currentUnitPrice = poec.getAdjustedUnitPrice(ctx).doubleValue();
                    if(currentUnitPrice > 0.0D)
                    {
                        long multiple = unallocatedRemainderUnits / quantity;
                        for(; multiple > 1L; multiple--)
                        {
                            if(smallestCurrencyUnitAdjustment.multiply(BigDecimal.valueOf(multiple))
                                            .add(BigDecimal.valueOf(currentUnitPrice)).doubleValue() >= 0.0D)
                            {
                                break;
                            }
                        }
                        double adjustedUnitPrice = roundCurrencyValue(ctx, currency, smallestCurrencyUnitAdjustment.multiply(BigDecimal.valueOf(multiple)).add(BigDecimal.valueOf(currentUnitPrice))).doubleValue();
                        poec.setAdjustedUnitPrice(ctx, adjustedUnitPrice);
                        unallocatedRemainderUnits -= multiple * quantity;
                        if(unallocatedRemainderUnits == 0L)
                        {
                            break;
                        }
                    }
                }
            }
            if(unallocatedRemainderUnits > 0L)
            {
                for(PromotionOrderEntryConsumed poec : orderEntriesSortedByQuantityDescending)
                {
                    long quantity = poec.getQuantity(ctx).longValue();
                    if(quantity > unallocatedRemainderUnits)
                    {
                        double currentUnitPrice = poec.getAdjustedUnitPrice(ctx).doubleValue();
                        if(currentUnitPrice > 0.0D)
                        {
                            PromotionOrderEntryConsumed splitPoec1 = PromotionsManager.getInstance().createPromotionOrderEntryConsumed(ctx, poec.getCode(ctx), poec.getOrderEntry(ctx), quantity - unallocatedRemainderUnits);
                            PromotionOrderEntryConsumed splitPoec2 = PromotionsManager.getInstance().createPromotionOrderEntryConsumed(ctx, poec.getCode(ctx), poec.getOrderEntry(ctx), unallocatedRemainderUnits);
                            splitPoec1.setAdjustedUnitPrice(ctx, poec.getAdjustedUnitPrice(ctx));
                            BigDecimal adjustedUnitPriceDecimal = smallestCurrencyUnitAdjustment.add(BigDecimal.valueOf(poec.getAdjustedUnitPrice(ctx).doubleValue()));
                            splitPoec2.setAdjustedUnitPrice(ctx,
                                            roundCurrencyValue(ctx, currency, adjustedUnitPriceDecimal).doubleValue());
                            consumedEntries.remove(poec);
                            consumedEntries.add(splitPoec1);
                            consumedEntries.add(splitPoec2);
                            break;
                        }
                    }
                }
            }
            BigDecimal checkTotalDecimal = calculateOrderEntryAdjustedTotal(ctx, currency, consumedEntries);
            if(!checkTotalDecimal.equals(targetTotalDecimal))
            {
                log.error("adjustUnitPrices Failed in checkTotal. targetTotalDecimal=[" + targetTotalDecimal + "] checkTotalDecimal=[" + checkTotalDecimal + "] originalTotal=[" + originalTotal + "]");
            }
        }
    }


    protected static BigDecimal calculateOrderEntryAdjustedTotal(SessionContext ctx, Currency currency, List<PromotionOrderEntryConsumed> entries)
    {
        BigDecimal total = BigDecimal.ZERO;
        for(PromotionOrderEntryConsumed poec : entries)
        {
            total = total.add(BigDecimal.valueOf(poec.getAdjustedEntryPrice(ctx)));
        }
        return roundCurrencyValue(ctx, currency, total);
    }


    public static Product getBaseProduct(SessionContext ctx, Product product)
    {
        if(product instanceof CompositeProduct)
        {
            return ((CompositeProduct)product).getCompositeParentProduct(ctx);
        }
        if(product instanceof VariantProduct)
        {
            return ((VariantProduct)product).getBaseProduct(ctx);
        }
        return null;
    }


    public static List<Product> getBaseProducts(SessionContext ctx, Product product)
    {
        List<Product> result = new ArrayList<>();
        getBaseProducts(ctx, product, result);
        return result;
    }


    protected static void getBaseProducts(SessionContext ctx, Product product, List<Product> result)
    {
        if(product != null && result != null)
        {
            Product baseProduct = null;
            if(product instanceof CompositeProduct)
            {
                baseProduct = ((CompositeProduct)product).getCompositeParentProduct(ctx);
            }
            else if(product instanceof VariantProduct)
            {
                baseProduct = ((VariantProduct)product).getBaseProduct(ctx);
            }
            if(baseProduct != null && !baseProduct.equals(product) && !result.contains(baseProduct))
            {
                result.add(baseProduct);
                getBaseProducts(ctx, baseProduct, result);
            }
        }
    }


    public static DiscountValue findGlobalDiscountValue(SessionContext ctx, AbstractOrder order, String discountValueCode)
    {
        Collection<DiscountValue> discounts = order.getGlobalDiscountValues(ctx);
        for(DiscountValue dv : discounts)
        {
            if(discountValueCode.equals(dv.getCode()))
            {
                return dv;
            }
        }
        return null;
    }


    public static String dumpOrder(SessionContext ctx, AbstractOrder order)
    {
        StringBuilder builder = new StringBuilder();
        dumpOrder(ctx, order, builder);
        return builder.toString();
    }


    protected static void dumpOrder(SessionContext ctx, AbstractOrder order, StringBuilder builder)
    {
        builder.append("## DUMP ORDER -");
        if(order == null)
        {
            builder.append("order is NULL");
        }
        else if(!order.isAlive())
        {
            builder.append("order was removed or is not valid anymore.");
        }
        else
        {
            builder.append(" type: ").append(order.getClass().getSimpleName());
            builder.append(" code: ").append(order.getCode(ctx));
            builder.append(" calculated: ").append(order.isCalculated(ctx));
            builder.append("\r\n");
            List<AbstractOrderEntry> entries = order.getEntries();
            if(entries != null && !entries.isEmpty())
            {
                builder.append("## OrderEntries:\r\n");
                for(AbstractOrderEntry orderEntry : entries)
                {
                    dumpOrderEntry(ctx, orderEntry, builder);
                }
            }
            Collection<Discount> discounts = order.getDiscounts();
            if(discounts != null && !discounts.isEmpty())
            {
                builder.append("## Discounts:\r\n");
                for(Discount discount : discounts)
                {
                    builder.append("##   discount: ").append(discount.getCode()).append(" value: ").append(discount.getValue())
                                    .append("\r\n");
                }
            }
            List<DiscountValue> globalDiscounts = order.getGlobalDiscountValues();
            if(globalDiscounts != null && !globalDiscounts.isEmpty())
            {
                builder.append("## Global Discounts:\r\n");
                for(DiscountValue discount : globalDiscounts)
                {
                    builder.append("##   discount: ").append(discount.getCode()).append(" value: ").append(discount.getValue())
                                    .append("\r\n");
                }
            }
            Collection<TaxValue> taxValues = order.getTotalTaxValues(ctx);
            if(taxValues != null && !taxValues.isEmpty())
            {
                builder.append("## Tax Values:\r\n");
                for(TaxValue taxValue : taxValues)
                {
                    builder.append("##   taxValue: ").append(taxValue.getCode()).append(" value: ").append(taxValue.getValue())
                                    .append("\r\n");
                }
            }
            builder.append("## Totals -");
            builder.append(" subtotal: ").append(order.getSubtotal(ctx));
            builder.append(" totalDiscounts: ").append(order.getTotalDiscounts(ctx));
            builder.append(" deliveryCosts: ").append(order.getDeliveryCosts(ctx));
            builder.append(" totalTax: ").append(order.getTotalTax(ctx));
            builder.append(" total: ").append(order.getTotal(ctx));
            builder.append("\r\n");
        }
    }


    protected static void dumpOrderEntry(SessionContext ctx, AbstractOrderEntry orderEntry, StringBuilder builder)
    {
        builder.append("##   [").append(orderEntry.getEntryNumber()).append("] ");
        builder.append(orderEntry.getProduct(ctx).getCode(ctx));
        builder.append(' ').append(orderEntry.getUnit(ctx).getName(ctx)).append(": ").append(orderEntry.getQuantity(ctx));
        builder.append(" baseprice: ").append(orderEntry.getBasePrice(ctx));
        builder.append(" totalprice: ").append(orderEntry.getTotalPrice(ctx));
        builder.append(" calculated: ").append(orderEntry.isCalculated(ctx));
        builder.append(" giveAway: ").append(orderEntry.isGiveAway(ctx));
        builder.append(" rejected: ").append(orderEntry.isRejected(ctx));
        builder.append("\r\n");
        String info = orderEntry.getInfo(ctx);
        if(info != null && info.length() > 0)
        {
            builder.append("##       info: ").append(info).append("\r\n");
        }
        List<DiscountValue> discounts = orderEntry.getDiscountValues(ctx);
        if(discounts != null && !discounts.isEmpty())
        {
            for(DiscountValue discount : discounts)
            {
                builder.append("##       discount: ").append(discount.getCode()).append(" value: ").append(discount.getValue())
                                .append("\r\n");
            }
        }
        Collection<TaxValue> taxValues = orderEntry.getTaxValues(ctx);
        if(taxValues != null && !taxValues.isEmpty())
        {
            for(TaxValue taxValue : taxValues)
            {
                builder.append("##       taxValue: ").append(taxValue.getCode()).append(" value: ").append(taxValue.getValue())
                                .append("\r\n");
            }
        }
    }


    public static String join(Collection items)
    {
        return join(items, ", ");
    }


    public static String join(Collection items, String delimiter)
    {
        if(items == null)
        {
            return null;
        }
        if(items.isEmpty())
        {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        boolean first = true;
        for(Object item : items)
        {
            if(first)
            {
                first = false;
            }
            else
            {
                builder.append(delimiter);
            }
            builder.append(item);
        }
        return builder.toString();
    }


    public static Date getDateNowRoundedToMinute()
    {
        Calendar now = Utilities.getDefaultCalendar();
        now.set(14, 0);
        now.set(13, 0);
        return now.getTime();
    }
}
