package de.hybris.platform.ruleengineservices.calculation.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.hybris.order.calculation.domain.AbstractCharge;
import de.hybris.order.calculation.domain.AbstractDiscount;
import de.hybris.order.calculation.domain.LineItem;
import de.hybris.order.calculation.domain.LineItemDiscount;
import de.hybris.order.calculation.domain.Order;
import de.hybris.order.calculation.domain.OrderCharge;
import de.hybris.order.calculation.domain.OrderDiscount;
import de.hybris.order.calculation.money.AbstractAmount;
import de.hybris.order.calculation.money.Currency;
import de.hybris.order.calculation.money.Money;
import de.hybris.order.calculation.money.Percentage;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ruleengineservices.calculation.MinimumAmountValidationStrategy;
import de.hybris.platform.ruleengineservices.calculation.NumberedLineItem;
import de.hybris.platform.ruleengineservices.calculation.NumberedLineItemLookupStrategy;
import de.hybris.platform.ruleengineservices.calculation.RuleEngineCalculationService;
import de.hybris.platform.ruleengineservices.rao.AbstractActionedRAO;
import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DeliveryModeRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.FreeProductRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.rao.ShipmentRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.RAOConsumptionSupport;
import de.hybris.platform.ruleengineservices.util.CurrencyUtils;
import de.hybris.platform.ruleengineservices.util.OrderUtils;
import de.hybris.platform.ruleengineservices.util.RaoUtils;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleEngineCalculationService implements RuleEngineCalculationService
{
    private static final int DECLARATIVE_UNROUNDED_PRECISION = 10;
    public static final String CART_NULL_DESC = "cart rao must not be null";
    public static final String AMOUNT_NULL_DESC = "amount must not be null";
    private Converter<AbstractOrderRAO, Order> abstractOrderRaoToOrderConverter;
    private MinimumAmountValidationStrategy minimumAmountValidationStrategy;
    private OrderUtils orderUtils;
    private CurrencyUtils currencyUtils;
    private RaoUtils raoUtils;
    private RAOConsumptionSupport consumptionSupport;
    private NumberedLineItemLookupStrategy lineItemLookupStrategy;
    private Populator<ProductModel, OrderEntryRAO> orderEntryRaoProductPopulator;


    public DiscountRAO addOrderLevelDiscount(CartRAO cartRao, boolean absolute, BigDecimal amount)
    {
        ServicesUtil.validateParameterNotNull(cartRao, "cart rao must not be null");
        ServicesUtil.validateParameterNotNull(amount, "amount must not be null");
        Order cart = (Order)getAbstractOrderRaoToOrderConverter().convert(cartRao);
        OrderDiscount discount = createOrderDiscount(cart, absolute, amount);
        DiscountRAO discountRAO = createDiscountRAO((AbstractDiscount)discount);
        getRaoUtils().addAction((AbstractActionedRAO)cartRao, (AbstractRuleActionRAO)discountRAO);
        recalculateTotals((AbstractOrderRAO)cartRao, cart);
        return discountRAO;
    }


    public BigDecimal getCurrentPrice(Set<OrderEntryRAO> orderEntryForDiscounts, Map<Integer, Integer> discountedOrderEntryMap)
    {
        Order cart = (Order)getAbstractOrderRaoToOrderConverter().convert(((OrderEntryRAO)orderEntryForDiscounts.iterator().next()).getOrder());
        return orderEntryForDiscounts.stream().map(orderEntry -> {
            NumberedLineItem li = getLineItemLookupStrategy().lookup(cart, orderEntry);
            li.setGiveAwayUnits(orderEntry.getQuantity() - ((Integer)discountedOrderEntryMap.get(orderEntry.getEntryNumber())).intValue());
            return li;
        }).map(lineItem -> lineItem.getSubTotal().getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public DiscountRAO addOrderEntryLevelDiscount(OrderEntryRAO orderEntryRao, boolean absolute, BigDecimal amount)
    {
        ServicesUtil.validateParameterNotNull(orderEntryRao, "order entry rao must not be null");
        ServicesUtil.validateParameterNotNull(orderEntryRao.getOrder(), "corresponding entry cart rao must not be null");
        ServicesUtil.validateParameterNotNull(amount, "amount must not be null");
        return addOrderEntryLevelDiscount(orderEntryRao, absolute, amount,
                        getConsumptionSupport().getConsumedQuantityForOrderEntry(orderEntryRao));
    }


    protected DiscountRAO addOrderEntryLevelDiscount(OrderEntryRAO orderEntryRao, boolean absolute, BigDecimal amount, int consumedQty)
    {
        Preconditions.checkArgument((consumedQty >= 0), "consumed quantity can't be negative");
        Order cart = (Order)getAbstractOrderRaoToOrderConverter().convert(orderEntryRao.getOrder());
        NumberedLineItem lineItem = getLineItemLookupStrategy().lookup(cart, orderEntryRao);
        int qty = orderEntryRao.getQuantity() - consumedQty;
        BigDecimal adjustedAmount = absolute ? amount.multiply(BigDecimal.valueOf(qty)) : convertPercentageDiscountToAbsoluteDiscount(amount, qty, lineItem);
        DiscountRAO discountRAO = createAbsoluteDiscountRAO((LineItem)lineItem, adjustedAmount, qty, true);
        getRaoUtils().addAction((AbstractActionedRAO)orderEntryRao, (AbstractRuleActionRAO)discountRAO);
        AbstractOrderRAO cartRao = orderEntryRao.getOrder();
        recalculateTotals(cartRao, cart);
        return discountRAO;
    }


    public Set<DiscountRAO> addOrderEntryLevelDiscount(OrderEntryRAO orderEntryRAO, boolean absolute, BigDecimal amount, boolean perUnit)
    {
        ServicesUtil.validateParameterNotNull(orderEntryRAO, "order entry rao must not be null");
        ServicesUtil.validateParameterNotNull(orderEntryRAO.getOrder(), "corresponding entry cart rao must not be null");
        ServicesUtil.validateParameterNotNull(amount, "amount must not be null");
        return addOrderEntryLevelDiscountWithPerUnitFlag(orderEntryRAO, absolute, amount,
                        getConsumptionSupport().getConsumedQuantityForOrderEntry(orderEntryRAO), perUnit);
    }


    protected Set<DiscountRAO> addOrderEntryLevelDiscountWithPerUnitFlag(OrderEntryRAO orderEntryRao, boolean absolute, BigDecimal amount, int consumedQty, boolean perUnit)
    {
        Preconditions.checkArgument((consumedQty >= 0), "consumed quantity can't be negative");
        Order cart = (Order)getAbstractOrderRaoToOrderConverter().convert(orderEntryRao.getOrder());
        NumberedLineItem lineItem = getLineItemLookupStrategy().lookup(cart, orderEntryRao);
        int qty = orderEntryRao.getQuantity() - consumedQty;
        BigDecimal adjustedAmount = absolute ? (perUnit ? amount.multiply(BigDecimal.valueOf(qty)) : amount) : convertPercentageDiscountToAbsoluteDiscount(amount, qty, lineItem);
        Set<DiscountRAO> discountRAOSet = createAbsoluteDiscountRAO((LineItem)lineItem, adjustedAmount, qty);
        discountRAOSet.forEach(discountRAO -> getRaoUtils().addAction((AbstractActionedRAO)orderEntryRao, (AbstractRuleActionRAO)discountRAO));
        AbstractOrderRAO cartRao = orderEntryRao.getOrder();
        recalculateTotals(cartRao, cart);
        return discountRAOSet;
    }


    public DiscountRAO addFixedPriceEntryDiscount(OrderEntryRAO orderEntryRao, BigDecimal fixedPrice)
    {
        ServicesUtil.validateParameterNotNull(orderEntryRao, "cart rao must not be null");
        ServicesUtil.validateParameterNotNull(fixedPrice, "fixedPrice must not be null");
        ServicesUtil.validateParameterNotNull(orderEntryRao.getOrder(), "Order must not be null");
        ServicesUtil.validateParameterNotNull(orderEntryRao.getPrice(), "Product price is null");
        if(orderEntryRao.getPrice().compareTo(fixedPrice) > 0)
        {
            BigDecimal price = orderEntryRao.getPrice();
            BigDecimal discountAmount = price.subtract(fixedPrice);
            BigDecimal scaledDiscountAmount = getCurrencyUtils().applyRounding(discountAmount, orderEntryRao
                            .getCurrencyIsoCode());
            return addOrderEntryLevelDiscount(orderEntryRao, true, scaledDiscountAmount);
        }
        return null;
    }


    public FreeProductRAO addFreeProductsToCart(CartRAO cartRao, ProductModel product, int quantity)
    {
        Optional<OrderEntryRAO> oeOptional = cartRao.getEntries().stream().filter(e -> (e.isGiveAway() && e.getProductCode().equals(product.getCode()))).findFirst();
        OrderEntryRAO orderEntryRao = oeOptional.orElseGet(OrderEntryRAO::new);
        orderEntryRao.setGiveAway(true);
        orderEntryRao.setBasePrice(BigDecimal.ZERO);
        orderEntryRao.setPrice(BigDecimal.ZERO);
        orderEntryRao.setCurrencyIsoCode(cartRao.getCurrencyIsoCode());
        orderEntryRao.setQuantity(orderEntryRao.getQuantity() + quantity);
        orderEntryRao.setOrder((AbstractOrderRAO)cartRao);
        getOrderEntryRaoProductPopulator().populate(product, orderEntryRao);
        if(cartRao.getEntries() == null)
        {
            cartRao.setEntries(new LinkedHashSet());
        }
        if(!oeOptional.isPresent())
        {
            cartRao.getEntries().add(orderEntryRao);
        }
        ensureOrderEntryRAOEntryNumbers((AbstractOrderRAO)cartRao);
        FreeProductRAO result = new FreeProductRAO();
        result.setQuantityAdded(quantity);
        getRaoUtils().addAction((AbstractActionedRAO)cartRao, (AbstractRuleActionRAO)result);
        result.setAddedOrderEntry(orderEntryRao);
        return result;
    }


    protected void ensureOrderEntryRAOEntryNumbers(AbstractOrderRAO abstractOrderRao)
    {
        if(abstractOrderRao != null && abstractOrderRao.getEntries() != null)
        {
            List<OrderEntryRAO> nullEntries = Lists.newArrayList();
            Set<OrderEntryRAO> abstractOrderRaoEntries = abstractOrderRao.getEntries();
            Objects.requireNonNull(nullEntries);
            abstractOrderRaoEntries.stream().filter(e -> Objects.isNull(e.getEntryNumber())).forEach(nullEntries::add);
            int max = abstractOrderRaoEntries.stream().filter(e -> Objects.nonNull(e.getEntryNumber())).mapToInt(OrderEntryRAO::getEntryNumber).max().orElse(-1);
            if(CollectionUtils.isNotEmpty(nullEntries))
            {
                for(OrderEntryRAO orderEntryRAO : nullEntries)
                {
                    max = (max != -1) ? (max + 1) : 1;
                    orderEntryRAO.setEntryNumber(Integer.valueOf(max));
                }
            }
        }
    }


    public ShipmentRAO changeDeliveryMode(CartRAO cartRao, DeliveryModeRAO mode)
    {
        ServicesUtil.validateParameterNotNull(cartRao, "cart rao must not be null");
        ServicesUtil.validateParameterNotNull(mode, "mode must not be null");
        ServicesUtil.validateParameterNotNull(mode.getCost(), "mode cost must not be null");
        ServicesUtil.validateParameterNotNull(mode.getCode(), "mode code must not be null");
        Order cart = (Order)getAbstractOrderRaoToOrderConverter().convert(cartRao);
        removeShippingCharges(cart);
        if(BigDecimal.ZERO.compareTo(mode.getCost()) < 0)
        {
            OrderCharge shipping = createShippingCharge(cart, true, mode.getCost());
            cart.addCharge(shipping);
        }
        recalculateTotals((AbstractOrderRAO)cartRao, cart);
        ShipmentRAO shipmentRAO = createShipmentRAO(mode);
        getRaoUtils().addAction((AbstractActionedRAO)cartRao, (AbstractRuleActionRAO)shipmentRAO);
        return shipmentRAO;
    }


    public void calculateTotals(AbstractOrderRAO cartRao)
    {
        Order cart = (Order)getAbstractOrderRaoToOrderConverter().convert(cartRao);
        recalculateTotals(cartRao, cart);
    }


    protected OrderEntryRAO findOrderEntryRAO(AbstractOrderRAO order, NumberedLineItem lineItem)
    {
        ServicesUtil.validateParameterNotNull(order, "order must not be null");
        ServicesUtil.validateParameterNotNull(lineItem, "lineItem must not be null");
        if(order.getEntries() != null)
        {
            for(OrderEntryRAO rao : order.getEntries())
            {
                if(rao.getEntryNumber() != null && rao.getEntryNumber().equals(lineItem.getEntryNumber()))
                {
                    return rao;
                }
            }
        }
        return null;
    }


    protected void recalculateTotals(AbstractOrderRAO cartRao, Order cart)
    {
        cartRao.setSubTotal(cart.getSubTotal().getAmount());
        cartRao.setTotal(cart.getTotal().subtract(cart.getTotalCharge()).getAmount());
        cartRao.setTotalIncludingCharges(cart.getTotal().getAmount());
        cartRao.setDeliveryCost(cart.getTotalChargeOfType(AbstractCharge.ChargeType.SHIPPING).getAmount());
        cartRao.setPaymentCost(cart.getTotalChargeOfType(AbstractCharge.ChargeType.PAYMENT).getAmount());
        if(!CollectionUtils.isEmpty(cartRao.getEntries()))
        {
            for(OrderEntryRAO entryRao : cartRao.getEntries())
            {
                NumberedLineItem lineItem = getLineItemLookupStrategy().lookup(cart, entryRao);
                entryRao.setTotalPrice(lineItem.getTotal(cart).getAmount());
            }
        }
    }


    protected OrderDiscount createOrderDiscount(Order cart, boolean absolute, BigDecimal amount)
    {
        Currency currency = cart.getCurrency();
        BigDecimal adjustedDiscountAmount = absolute ? amount : convertPercentageDiscountToAbsoluteDiscount(amount, cart);
        Money money1 = new Money(adjustedDiscountAmount, currency);
        OrderDiscount discount = new OrderDiscount((AbstractAmount)money1);
        if(getMinimumAmountValidationStrategy().isOrderLowerLimitValid(cart, discount))
        {
            cart.addDiscount(discount);
            return discount;
        }
        Money money2 = new Money(BigDecimal.ZERO, currency);
        OrderDiscount zeroDiscount = new OrderDiscount((AbstractAmount)money2);
        cart.addDiscount(zeroDiscount);
        return zeroDiscount;
    }


    protected DiscountRAO createDiscountRAO(AbstractDiscount discount)
    {
        ServicesUtil.validateParameterNotNull(discount, "OrderDiscount must not be null.");
        DiscountRAO discountRAO = new DiscountRAO();
        if(discount.getAmount() instanceof Money)
        {
            Money money = (Money)discount.getAmount();
            discountRAO.setValue(money.getAmount());
            discountRAO.setCurrencyIsoCode(money.getCurrency().getIsoCode());
        }
        else
        {
            throw new IllegalArgumentException("OrderDiscount must have Money or Percentage amount set.");
        }
        if(discount instanceof LineItemDiscount)
        {
            LineItemDiscount lineItemDiscount = (LineItemDiscount)discount;
            discountRAO.setAppliedToQuantity(lineItemDiscount.getApplicableUnits());
            discountRAO.setPerUnit(true);
        }
        return discountRAO;
    }


    protected LineItemDiscount createLineItemDiscount(LineItem lineItem, boolean absolute, BigDecimal amount, boolean perUnit)
    {
        Currency currency = lineItem.getBasePrice().getCurrency();
        AbstractAmount discountAmount = absolute ? (AbstractAmount)new Money(amount, currency) : (AbstractAmount)new Percentage(amount);
        LineItemDiscount discount = new LineItemDiscount(discountAmount, perUnit);
        return validateLineItemDiscount(lineItem, absolute, currency, discount);
    }


    protected LineItemDiscount createLineItemDiscount(LineItem lineItem, boolean absolute, BigDecimal amount, boolean perUnit, int applicableUnits)
    {
        Currency currency = lineItem.getBasePrice().getCurrency();
        BigDecimal adjustedAmount = amount;
        if(absolute)
        {
            if(applicableUnits >= 1)
            {
                adjustedAmount = amount.divide(BigDecimal.valueOf(applicableUnits), 10, 1);
            }
            else
            {
                adjustedAmount = BigDecimal.ZERO;
            }
        }
        AbstractAmount discountAmount = absolute ? (AbstractAmount)new Money(adjustedAmount, currency) : (AbstractAmount)new Percentage(adjustedAmount);
        LineItemDiscount discount = new LineItemDiscount(discountAmount, perUnit, applicableUnits);
        return validateLineItemDiscount(lineItem, absolute, currency, discount);
    }


    protected LineItemDiscount validateLineItemDiscount(LineItem lineItem, boolean absolute, Currency currency, LineItemDiscount discount)
    {
        if(getMinimumAmountValidationStrategy().isLineItemLowerLimitValid(lineItem, discount))
        {
            lineItem.addDiscount(discount);
            return discount;
        }
        AbstractAmount zeroDiscountAmount = absolute ? (AbstractAmount)new Money(BigDecimal.ZERO, currency) : (AbstractAmount)Percentage.ZERO;
        LineItemDiscount zeroDiscount = new LineItemDiscount(zeroDiscountAmount);
        lineItem.addDiscount(zeroDiscount);
        return zeroDiscount;
    }


    protected LineItemDiscount createLineItemDiscount(LineItem lineItem, boolean absolute, BigDecimal amount)
    {
        return createLineItemDiscount(lineItem, absolute, amount, absolute);
    }


    protected void removeShippingCharges(Order cart)
    {
        ServicesUtil.validateParameterNotNull(cart, "cart must not be null.");
        for(OrderCharge charge : cart.getCharges())
        {
            if(AbstractCharge.ChargeType.SHIPPING.equals(charge.getChargeType()))
            {
                cart.removeCharge(charge);
            }
        }
    }


    protected OrderCharge createShippingCharge(Order cart, boolean absolute, BigDecimal value)
    {
        OrderCharge shippingCharge = getOrderUtils().createShippingCharge(cart.getCurrency(), absolute, value);
        cart.addCharge(shippingCharge);
        return shippingCharge;
    }


    protected ShipmentRAO createShipmentRAO(DeliveryModeRAO mode)
    {
        ServicesUtil.validateParameterNotNull(mode, "mode must not be null.");
        ShipmentRAO shipmentRao = new ShipmentRAO();
        shipmentRao.setMode(mode);
        return shipmentRao;
    }


    protected Converter<AbstractOrderRAO, Order> getAbstractOrderRaoToOrderConverter()
    {
        return this.abstractOrderRaoToOrderConverter;
    }


    @Required
    public void setAbstractOrderRaoToOrderConverter(Converter<AbstractOrderRAO, Order> abstractOrderRaoToOrderConverter)
    {
        this.abstractOrderRaoToOrderConverter = abstractOrderRaoToOrderConverter;
    }


    public List<DiscountRAO> addFixedPriceEntriesDiscount(CartRAO cartRao, Map<Integer, Integer> selectedOrderEntryMap, Set<OrderEntryRAO> selectedOrderEntryRaos, BigDecimal fixedPrice)
    {
        ServicesUtil.validateParameterNotNull(cartRao, "cartRao must not be null");
        ServicesUtil.validateParameterNotNull(cartRao.getEntries(), "cartRao.entries must not be null");
        ServicesUtil.validateParameterNotNull(selectedOrderEntryMap, "selectedOrderEntryMap must not be null");
        ServicesUtil.validateParameterNotNull(selectedOrderEntryRaos, "selectedOrderEntryRaos must not be null");
        for(OrderEntryRAO orderEntryRAO : selectedOrderEntryRaos)
        {
            ServicesUtil.validateParameterNotNull(orderEntryRAO, "orderEntryRao must not be null");
            if(!cartRao.getEntries().contains(orderEntryRAO))
            {
                throw new IllegalArgumentException("orderEntryRao from given set of selectedOrderEntryRaos:" + orderEntryRAO
                                .toString() + " must be part of the given cartRAO.entries!");
            }
        }
        Order cart = (Order)getAbstractOrderRaoToOrderConverter().convert(cartRao);
        List<DiscountRAO> result = Lists.newArrayList();
        for(OrderEntryRAO orderEntryRao : selectedOrderEntryRaos)
        {
            BigDecimal unitPrice = orderEntryRao.getPrice();
            Integer quantityToDiscount = selectedOrderEntryMap.get(orderEntryRao.getEntryNumber());
            BigDecimal totalEntryDiscountAmount = unitPrice.multiply(BigDecimal.valueOf(quantityToDiscount.intValue())).subtract(fixedPrice.multiply(BigDecimal.valueOf(quantityToDiscount.intValue())));
            BigDecimal roundedTotalEntryDiscountAmount = getCurrencyUtils().applyRounding(totalEntryDiscountAmount, orderEntryRao
                            .getCurrencyIsoCode());
            NumberedLineItem lineItem = getLineItemLookupStrategy().lookup(cart, orderEntryRao);
            DiscountRAO discountRAO = createAbsoluteDiscountRAO((LineItem)lineItem, roundedTotalEntryDiscountAmount, quantityToDiscount
                            .intValue(), true);
            getRaoUtils().addAction((AbstractActionedRAO)orderEntryRao, (AbstractRuleActionRAO)discountRAO);
            calculateTotals((AbstractOrderRAO)cartRao);
            result.add(discountRAO);
        }
        return result;
    }


    private DiscountRAO createDiscountRAOWithInfo(BigDecimal adjustedAmount, Currency currency, int appliedToQuantity, LineItem lineItem, boolean perUnit)
    {
        Money money1 = new Money(adjustedAmount, currency);
        LineItemDiscount discount = new LineItemDiscount((AbstractAmount)money1, true, appliedToQuantity);
        discount = validateLineItemDiscount(lineItem, true, currency, discount);
        DiscountRAO discountRAO = new DiscountRAO();
        discountRAO.setPerUnit(perUnit);
        discountRAO.setAppliedToQuantity(appliedToQuantity);
        Money money = (Money)discount.getAmount();
        discountRAO.setValue((money.getAmount().compareTo(BigDecimal.ZERO) == 0) ? BigDecimal.ZERO : money.getAmount());
        discountRAO.setCurrencyIsoCode(money.getCurrency().getIsoCode());
        return discountRAO;
    }


    protected DiscountRAO createAbsoluteDiscountRAO(LineItem lineItem, BigDecimal amount, int applicableUnits, boolean perUnit)
    {
        int appliedToQuantity = perUnit ? applicableUnits : lineItem.getNumberOfUnits();
        Currency currency = lineItem.getBasePrice().getCurrency();
        BigDecimal adjustedAmount = amount.divide(BigDecimal.valueOf(appliedToQuantity), 10, RoundingMode.DOWN);
        return createDiscountRAOWithInfo(adjustedAmount, currency, appliedToQuantity, lineItem, perUnit);
    }


    protected Set<DiscountRAO> createAbsoluteDiscountRAO(LineItem lineItem, BigDecimal amount, int applicableUnits)
    {
        Set<DiscountRAO> discounts = Sets.newHashSet();
        int appliedToQuantity = applicableUnits;
        Currency currency = lineItem.getBasePrice().getCurrency();
        ImmutableTriple<BigDecimal, BigDecimal, Integer> biasDiscountInfoWithCurrency = getBiasDiscountInfoWithCurrency(currency, amount, appliedToQuantity);
        BigDecimal adjustedAmount = (BigDecimal)biasDiscountInfoWithCurrency.getLeft();
        BigDecimal biasDiscount = (BigDecimal)biasDiscountInfoWithCurrency.getMiddle();
        int biasItemNumber = ((Integer)biasDiscountInfoWithCurrency.getRight()).intValue();
        if(biasItemNumber > 0)
        {
            discounts.add(createDiscountRAOWithInfo(biasDiscount, currency, biasItemNumber, lineItem, true));
        }
        appliedToQuantity -= biasItemNumber;
        if(appliedToQuantity > 0)
        {
            discounts.add(createDiscountRAOWithInfo(adjustedAmount, currency, appliedToQuantity, lineItem, true));
        }
        return discounts;
    }


    protected ImmutableTriple<BigDecimal, BigDecimal, Integer> getBiasDiscountInfoWithCurrency(Currency currency, BigDecimal amount, int appliedToQuantity)
    {
        BigDecimal adjustedAmount = amount.divide(BigDecimal.valueOf(appliedToQuantity), 10, RoundingMode.DOWN);
        BigDecimal discountPerUnitWithCurr = (new Money(adjustedAmount, currency)).getAmount();
        BigDecimal biasDiscountPerUnitWithCurr = discountPerUnitWithCurr.add(BigDecimal.valueOf(1L, currency.getDigits()));
        BigDecimal totalDiscountWithCurr = discountPerUnitWithCurr.multiply(BigDecimal.valueOf(appliedToQuantity));
        int biasItemNumber = amount.subtract(totalDiscountWithCurr).divide(BigDecimal.valueOf(1L, currency.getDigits())).toBigInteger().intValue();
        biasItemNumber = Math.max(0, biasItemNumber);
        return ImmutableTriple.of(adjustedAmount, biasDiscountPerUnitWithCurr, Integer.valueOf(biasItemNumber));
    }


    public List<DiscountRAO> addOrderEntryLevelDiscount(Map<Integer, Integer> selectedOrderEntryMap, Set<OrderEntryRAO> selectedOrderEntryRaos, boolean absolute, BigDecimal amount)
    {
        ServicesUtil.validateParameterNotNull(selectedOrderEntryMap, "selectedOrderEntryMap must not be null");
        ServicesUtil.validateParameterNotNull(selectedOrderEntryRaos, "selectedOrderEntryRaos must not be null");
        ServicesUtil.validateParameterNotNull(amount, "amount must not be null");
        List<DiscountRAO> result = Lists.newArrayList();
        for(OrderEntryRAO orderEntryRao : selectedOrderEntryRaos)
        {
            int qty = ((Integer)selectedOrderEntryMap.get(orderEntryRao.getEntryNumber())).intValue();
            Order cart = (Order)getAbstractOrderRaoToOrderConverter().convert(orderEntryRao.getOrder());
            NumberedLineItem lineItem = getLineItemLookupStrategy().lookup(cart, orderEntryRao);
            BigDecimal adjustedAmount = absolute ? amount.multiply(BigDecimal.valueOf(qty)) : convertPercentageDiscountToAbsoluteDiscount(amount, qty, lineItem);
            DiscountRAO discountRAO = createAbsoluteDiscountRAO((LineItem)lineItem, adjustedAmount, qty, true);
            getRaoUtils().addAction((AbstractActionedRAO)orderEntryRao, (AbstractRuleActionRAO)discountRAO);
            result.add(discountRAO);
            CartRAO cartRao = (CartRAO)orderEntryRao.getOrder();
            recalculateTotals((AbstractOrderRAO)cartRao, cart);
        }
        return result;
    }


    protected BigDecimal convertPercentageDiscountToAbsoluteDiscount(BigDecimal percentageAmount, int quantityToConsume, NumberedLineItem orderLineItem)
    {
        BigDecimal valueToDiscount;
        List<LineItemDiscount> lineItemDiscounts = orderLineItem.getDiscounts();
        int numItemsDiscounted = lineItemDiscounts.stream().mapToInt(LineItemDiscount::getApplicableUnits).sum();
        int availableItems = orderLineItem.getNumberOfUnits() - numItemsDiscounted;
        if(quantityToConsume <= availableItems)
        {
            valueToDiscount = orderLineItem.getBasePrice().getAmount().multiply(BigDecimal.valueOf(quantityToConsume));
        }
        else
        {
            BigDecimal availableItemsValueToDiscount = orderLineItem.getBasePrice().getAmount().multiply(BigDecimal.valueOf(availableItems));
            BigDecimal residualItemsValueToDiscount = orderLineItem.getBasePrice().getAmount().multiply(BigDecimal.valueOf(numItemsDiscounted)).subtract(orderLineItem.getTotalDiscount().getAmount()).multiply(BigDecimal.valueOf((quantityToConsume - availableItems) / numItemsDiscounted));
            valueToDiscount = availableItemsValueToDiscount.add(residualItemsValueToDiscount);
        }
        BigDecimal fraction = percentageAmount.divide(BigDecimal.valueOf(100.0D), 10, RoundingMode.DOWN);
        return valueToDiscount.multiply(fraction);
    }


    protected BigDecimal convertPercentageDiscountToAbsoluteDiscount(BigDecimal percentageAmount, Order cart)
    {
        BigDecimal valueToDiscount = cart.getSubTotal().subtract(cart.getTotalDiscount()).getAmount();
        return valueToDiscount.multiply(percentageAmount).divide(BigDecimal.valueOf(100.0D), RoundingMode.DOWN);
    }


    protected MinimumAmountValidationStrategy getMinimumAmountValidationStrategy()
    {
        return this.minimumAmountValidationStrategy;
    }


    @Required
    public void setMinimumAmountValidationStrategy(MinimumAmountValidationStrategy minimumAmountValidationStrategy)
    {
        this.minimumAmountValidationStrategy = minimumAmountValidationStrategy;
    }


    protected OrderUtils getOrderUtils()
    {
        return this.orderUtils;
    }


    @Required
    public void setOrderUtils(OrderUtils orderUtils)
    {
        this.orderUtils = orderUtils;
    }


    protected CurrencyUtils getCurrencyUtils()
    {
        return this.currencyUtils;
    }


    @Required
    public void setCurrencyUtils(CurrencyUtils currencyUtils)
    {
        this.currencyUtils = currencyUtils;
    }


    protected RaoUtils getRaoUtils()
    {
        return this.raoUtils;
    }


    @Required
    public void setRaoUtils(RaoUtils raoUtils)
    {
        this.raoUtils = raoUtils;
    }


    protected RAOConsumptionSupport getConsumptionSupport()
    {
        return this.consumptionSupport;
    }


    @Required
    public void setConsumptionSupport(RAOConsumptionSupport consumptionSupport)
    {
        this.consumptionSupport = consumptionSupport;
    }


    protected NumberedLineItemLookupStrategy getLineItemLookupStrategy()
    {
        return this.lineItemLookupStrategy;
    }


    @Required
    public void setLineItemLookupStrategy(NumberedLineItemLookupStrategy lineItemLookupStrategy)
    {
        this.lineItemLookupStrategy = lineItemLookupStrategy;
    }


    protected Populator<ProductModel, OrderEntryRAO> getOrderEntryRaoProductPopulator()
    {
        return this.orderEntryRaoProductPopulator;
    }


    @Required
    public void setOrderEntryRaoProductPopulator(Populator<ProductModel, OrderEntryRAO> orderEntryRaoProductPopulator)
    {
        this.orderEntryRaoProductPopulator = orderEntryRaoProductPopulator;
    }
}
