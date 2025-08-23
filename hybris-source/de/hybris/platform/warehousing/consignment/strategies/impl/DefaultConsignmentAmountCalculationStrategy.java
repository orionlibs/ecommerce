package de.hybris.platform.warehousing.consignment.strategies.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.TaxValue;
import de.hybris.platform.warehousing.consignment.strategies.ConsignmentAmountCalculationStrategy;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

public class DefaultConsignmentAmountCalculationStrategy implements ConsignmentAmountCalculationStrategy
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultConsignmentAmountCalculationStrategy.class);


    public BigDecimal calculateCaptureAmount(ConsignmentModel consignment)
    {
        BigDecimal result;
        ServicesUtil.validateParameterNotNullStandardMessage("consignment", consignment);
        BigDecimal orderAmountWithoutShipping = calculateOrderAmountWithoutDeliveryCostAndDeliveryTax(consignment);
        BigDecimal alreadyCapturedAmount = calculateAlreadyCapturedAmount(consignment);
        if(alreadyCapturedAmount.equals(orderAmountWithoutShipping))
        {
            result = BigDecimal.ZERO;
        }
        else if(alreadyCapturedAmount.compareTo(orderAmountWithoutShipping) < 0)
        {
            result = calculateAmountToCapture(consignment, alreadyCapturedAmount, orderAmountWithoutShipping);
        }
        else
        {
            throw new IllegalStateException("Consignment: " + consignment
                            .getCode() + " is trying to capture an amount greater than the order total");
        }
        LOGGER.debug("Calculated {} to be captured for consignment {}", result, consignment.getCode());
        return result;
    }


    public BigDecimal calculateAlreadyCapturedAmount(ConsignmentModel consignment)
    {
        return consignment.getOrder().getConsignments().stream()
                        .filter(consignmentModel -> !CollectionUtils.isEmpty(consignmentModel.getPaymentTransactionEntries()))
                        .map(this::calculateAmountCaptured).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }


    public BigDecimal calculateTotalOrderAmount(ConsignmentModel consignment)
    {
        return BigDecimal.valueOf(consignment.getOrder().getTotalPrice().doubleValue()).add(BigDecimal.valueOf(consignment.getOrder().getTotalTax().doubleValue()));
    }


    public BigDecimal calculateDiscountAmount(ConsignmentModel consignment)
    {
        BigDecimal result;
        if(isOnlyOrLastConsignment(consignment))
        {
            result = BigDecimal.valueOf(consignment.getOrder().getTotalDiscounts().doubleValue()).subtract(consignment.getOrder().getConsignments().stream()
                            .filter(consignmentModel -> !consignment.getCode().equals(consignmentModel.getCode()))
                            .map(this::calculateDiscountAmountForConsignment).reduce(BigDecimal::add).orElse(BigDecimal.ZERO));
        }
        else
        {
            result = calculateDiscountAmountForConsignment(consignment);
        }
        return result.setScale(consignment.getOrder().getCurrency().getDigits().intValue(), RoundingMode.HALF_UP);
    }


    public BigDecimal calculateConsignmentEntryAmount(ConsignmentEntryModel consignmentEntry, boolean includeTaxes)
    {
        BigDecimal consignmentTotalWithoutTax = consignmentEntry.getOrderEntry().getQuantity().equals(Long.valueOf(0L))
                        ? BigDecimal.ZERO
                        : BigDecimal.valueOf(consignmentEntry.getQuantity().longValue()).multiply(BigDecimal.valueOf(consignmentEntry.getOrderEntry().getTotalPrice().doubleValue())).divide(BigDecimal.valueOf(consignmentEntry.getOrderEntry().getQuantity().longValue()), consignmentEntry
                                        .getOrderEntry().getOrder().getCurrency().getDigits().intValue(), RoundingMode.HALF_UP);
        BigDecimal result = consignmentTotalWithoutTax;
        if(includeTaxes && !BigDecimal.ZERO.equals(consignmentTotalWithoutTax))
        {
            result = consignmentTotalWithoutTax.add(calculateConsignmentEntryTaxAmount(consignmentEntry));
        }
        return result;
    }


    protected BigDecimal calculateDiscountAmountForConsignment(ConsignmentModel consignment)
    {
        BigDecimal consignmentAmountWithoutTax = calculateConsignmentAmount(consignment, false);
        return consignmentAmountWithoutTax.multiply(BigDecimal.valueOf(consignment.getOrder().getTotalDiscounts().doubleValue()))
                        .divide(BigDecimal.valueOf(consignment.getOrder().getTotalPrice().doubleValue()).subtract(BigDecimal.valueOf(consignment.getOrder().getDeliveryCost().doubleValue())), consignment
                                        .getOrder().getCurrency().getDigits().intValue(), RoundingMode.HALF_UP);
    }


    protected boolean isOnlyOrLastConsignment(ConsignmentModel consignment)
    {
        return (consignment.getOrder().getConsignments().size() == 1 || consignment.getOrder().getConsignments().stream().filter(consignmentModel ->
                                        (!consignmentModel.getCode().equalsIgnoreCase(consignment.getCode()) && !ConsignmentStatus.CANCELLED.equals(consignmentModel.getStatus())))
                        .noneMatch(consignmentModel -> CollectionUtils.isEmpty(consignmentModel.getPaymentTransactionEntries())));
    }


    protected BigDecimal calculateAmountToCapture(ConsignmentModel consignment, BigDecimal alreadyCapturedAmount, BigDecimal totalOrderAmount)
    {
        BigDecimal result;
        if(isOnlyOrLastConsignment(consignment))
        {
            result = totalOrderAmount.subtract(alreadyCapturedAmount);
        }
        else
        {
            BigDecimal consignmentAmountWithTax = calculateConsignmentAmount(consignment, true).subtract(calculateDiscountAmountForConsignment(consignment));
            if(consignmentAmountWithTax.add(alreadyCapturedAmount).compareTo(totalOrderAmount) > 0)
            {
                result = totalOrderAmount.subtract(alreadyCapturedAmount);
            }
            else
            {
                result = consignmentAmountWithTax;
            }
        }
        return result;
    }


    protected BigDecimal calculateConsignmentAmount(ConsignmentModel consignment, boolean includeTaxes)
    {
        return consignment.getConsignmentEntries().stream()
                        .map(consignmentEntry -> calculateConsignmentEntryAmount(consignmentEntry, includeTaxes)).reduce(BigDecimal::add)
                        .orElse(BigDecimal.ZERO);
    }


    protected BigDecimal calculateConsignmentEntryTaxAmount(ConsignmentEntryModel consignmentEntry)
    {
        BigDecimal result;
        if(consignmentEntry.getQuantity().longValue() == 0L)
        {
            result = BigDecimal.ZERO;
        }
        else
        {
            BigDecimal taxAmountPerEntry = BigDecimal.valueOf(((TaxValue)consignmentEntry.getOrderEntry().getTaxValues().stream().findFirst()
                            .orElseThrow(() -> new IllegalStateException("No Tax value found for product: " + consignmentEntry.getOrderEntry().getProduct().getCode())))
                            .getValue());
            BigDecimal orderEntryQuantity = BigDecimal.valueOf(consignmentEntry.getOrderEntry().getQuantity().longValue()).add(BigDecimal.valueOf(((OrderEntryModel)consignmentEntry.getOrderEntry()).getQuantityUnallocated().longValue()));
            result = taxAmountPerEntry.divide(orderEntryQuantity, consignmentEntry.getConsignment().getOrder().getCurrency().getDigits().intValue(), RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(consignmentEntry.getQuantity().longValue()));
        }
        return result;
    }


    protected BigDecimal calculateAmountCaptured(ConsignmentModel consignment)
    {
        Optional<PaymentTransactionEntryModel> paymentTransactionEntry = consignment.getPaymentTransactionEntries().stream().findFirst();
        return paymentTransactionEntry.isPresent() ? ((PaymentTransactionEntryModel)paymentTransactionEntry.get()).getAmount() : null;
    }


    protected BigDecimal calculateOrderAmountWithoutDeliveryCostAndDeliveryTax(ConsignmentModel consignment)
    {
        return calculateTotalOrderAmount(consignment).subtract(BigDecimal.valueOf(consignment.getOrder().getDeliveryCost().doubleValue())).subtract(BigDecimal.valueOf(((TaxValue)consignment
                        .getOrder().getTotalTaxValues().stream().findFirst().orElseGet(() -> new TaxValue("", 0.0D, true, "")))
                        .getValue()));
    }
}
