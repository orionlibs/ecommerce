/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapcpiorderexchange.service.impl;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.sap.orderexchange.constants.OrderCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.OrderEntryCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.PartnerCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.PaymentCsvColumns;
import de.hybris.platform.sap.orderexchange.constants.SalesConditionCsvColumns;
import de.hybris.platform.sap.orderexchange.outbound.RawItemContributor;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiConfig;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiCreditCardPayment;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiOrder;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiOrderAddress;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiOrderCancellation;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiOrderCancellationItem;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiOrderItem;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiOrderPriceComponent;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiPartnerRole;
import de.hybris.platform.sap.sapcpiadapter.data.SapCpiTargetSystem;
import de.hybris.platform.sap.sapcpiorderexchange.exceptions.SapCpiOmmOrderConversionServiceException;
import de.hybris.platform.sap.sapcpiorderexchange.service.SapCpiOrderConversionService;
import de.hybris.platform.sap.sapcpiorderexchange.service.SapCpiOrderDestinationService;
import de.hybris.platform.sap.sapmodel.model.SAPLogicalSystemModel;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SAP CPI OMM Order conversion service converts OrderModel to SapCpiOrder and OrderCancelRecordEntryModel to List<SapCpiOrderCancellation>
 */
public class SapCpiOmmOrderConversionService implements SapCpiOrderConversionService
{
    private static final Logger LOG = LoggerFactory.getLogger(SapCpiOmmOrderConversionService.class);
    private RawItemContributor<OrderModel> sapOrderContributor;
    private RawItemContributor<OrderModel> sapPaymentContributor;
    private RawItemContributor<OrderModel> sapPartnerContributor;
    private RawItemContributor<OrderModel> sapOrderEntryContributor;
    private RawItemContributor<OrderModel> sapSalesConditionsContributor;
    private RawItemContributor<OrderCancelRecordEntryModel> sapOrderCancelRequestContributor;
    private SapCpiOrderDestinationService sapCpiOrderDestinationService;


    @Override
    public SapCpiOrder convertOrderToSapCpiOrder(OrderModel orderModel)
    {
        SapCpiOrder sapCpiOrder = new SapCpiOrder();
        sapOrderContributor.createRows(orderModel).stream().findFirst().ifPresent(row -> {
            sapCpiOrder.setSapCpiConfig(mapOrderConfigInfo(orderModel));
            setSapCpiOrderFromRawItem(sapCpiOrder, row);
            sapCpiOrder.setTransactionType(orderModel.getStore().getSAPConfiguration().getSapcommon_transactionType());
            sapCpiOrder.setSalesOrganization(orderModel.getStore().getSAPConfiguration().getSapcommon_salesOrganization());
            sapCpiOrder.setDistributionChannel(orderModel.getStore().getSAPConfiguration().getSapcommon_distributionChannel());
            sapCpiOrder.setDivision(orderModel.getStore().getSAPConfiguration().getSapcommon_division());
            orderModel.getStore().getSAPConfiguration().getSapDeliveryModes().stream()
                            .filter(entry -> entry.getDeliveryMode().getCode().contentEquals(orderModel.getDeliveryMode().getCode()))
                            .findFirst().ifPresent(entry -> sapCpiOrder.setShippingCondition(entry.getDeliveryValue()));
            sapCpiOrder.setSapCpiOrderItems(mapOrderItems(orderModel));
            sapCpiOrder.setSapCpiPartnerRoles(mapOrderPartners(orderModel));
            sapCpiOrder.setSapCpiOrderAddresses(mapOrderAddresses(orderModel));
            sapCpiOrder.setSapCpiOrderPriceComponents(mapOrderPrices(orderModel));
            sapCpiOrder.setSapCpiCreditCardPayments(mapCreditCards(orderModel));
        });
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("SCPI OMM order object: %n %s",
                            ReflectionToStringBuilder.toString(sapCpiOrder, ToStringStyle.MULTI_LINE_STYLE)));
        }
        return sapCpiOrder;
    }


    private void setSapCpiOrderFromRawItem(final SapCpiOrder sapCpiOrder, final Map<String, Object> rawITem)
    {
        sapCpiOrder.setOrderId(mapAttribute(OrderCsvColumns.ORDER_ID, rawITem));
        sapCpiOrder.setBaseStoreUid(mapAttribute(OrderCsvColumns.BASE_STORE, rawITem));
        sapCpiOrder.setCreationDate(mapDateAttribute(OrderCsvColumns.DATE, rawITem));
        sapCpiOrder.setCurrencyIsoCode(mapAttribute(OrderCsvColumns.ORDER_CURRENCY_ISO_CODE, rawITem));
        sapCpiOrder.setPaymentMode(mapAttribute(OrderCsvColumns.PAYMENT_MODE, rawITem));
        sapCpiOrder.setDeliveryMode(mapAttribute(OrderCsvColumns.DELIVERY_MODE, rawITem));
        sapCpiOrder.setChannel(mapAttribute(OrderCsvColumns.CHANNEL, rawITem));
        sapCpiOrder.setPurchaseOrderNumber(mapAttribute(OrderCsvColumns.PURCHASE_ORDER_NUMBER, rawITem));
    }


    protected SapCpiConfig mapOrderConfigInfo(OrderModel orderModel)
    {
        SapCpiTargetSystem sapCpiTargetSystem = new SapCpiTargetSystem();
        SAPLogicalSystemModel sapLogicalSystem = getSapCpiOrderDestinationService().readSapLogicalSystem();
        sapCpiTargetSystem.setSenderName(sapLogicalSystem.getSenderName());
        sapCpiTargetSystem.setSenderPort(sapLogicalSystem.getSenderPort());
        sapCpiTargetSystem.setReceiverName(sapLogicalSystem.getSapLogicalSystemName());
        sapCpiTargetSystem.setReceiverPort(sapLogicalSystem.getSapLogicalSystemName());
        Objects.requireNonNull(sapLogicalSystem.getSapHTTPDestination(), "No HTTP destination is maintained in back-office for order replication to SCPI!");
        sapCpiTargetSystem.setUsername(sapLogicalSystem.getSapHTTPDestination().getUserid());
        sapCpiTargetSystem.setUrl(getSapCpiOrderDestinationService().determineUrlDestination(sapLogicalSystem));
        sapCpiTargetSystem.setClient(getSapCpiOrderDestinationService().extractSapClient(sapLogicalSystem));
        SapCpiConfig sapCpiConfig = new SapCpiConfig();
        sapCpiConfig.setSapCpiTargetSystem(sapCpiTargetSystem);
        return sapCpiConfig;
    }


    protected List<SapCpiOrderItem> mapOrderItems(OrderModel orderModel)
    {
        final List<SapCpiOrderItem> sapCpiOrderItems = new ArrayList<>();
        sapOrderEntryContributor.createRows(orderModel).forEach(row -> {
            final SapCpiOrderItem sapCpiOrderItem = new SapCpiOrderItem();
            sapCpiOrderItem.setOrderId(mapAttribute(OrderCsvColumns.ORDER_ID, row));
            sapCpiOrderItem.setEntryNumber(mapAttribute(OrderEntryCsvColumns.ENTRY_NUMBER, row));
            sapCpiOrderItem.setQuantity(mapAttribute(OrderEntryCsvColumns.QUANTITY, row));
            sapCpiOrderItem.setProductCode(mapAttribute(OrderEntryCsvColumns.PRODUCT_CODE, row));
            sapCpiOrderItem.setUnit(mapAttribute(OrderEntryCsvColumns.ENTRY_UNIT_CODE, row));
            sapCpiOrderItem.setProductName(mapAttribute(OrderEntryCsvColumns.PRODUCT_NAME, row));
            sapCpiOrderItems.add(sapCpiOrderItem);
        });
        return sapCpiOrderItems;
    }


    protected List<SapCpiPartnerRole> mapOrderPartners(OrderModel orderModel)
    {
        final List<SapCpiPartnerRole> sapCpiPartnerRoles = new ArrayList<>();
        sapPartnerContributor.createRows(orderModel).forEach(row -> {
            SapCpiPartnerRole sapCpiPartnerRole = new SapCpiPartnerRole();
            sapCpiPartnerRole.setOrderId(mapAttribute(OrderCsvColumns.ORDER_ID, row));
            sapCpiPartnerRole.setDocumentAddressId(mapAttribute(PartnerCsvColumns.DOCUMENT_ADDRESS_ID, row));
            sapCpiPartnerRole.setPartnerId(mapAttribute(PartnerCsvColumns.PARTNER_CODE, row));
            sapCpiPartnerRole.setPartnerRoleCode(mapAttribute(PartnerCsvColumns.PARTNER_ROLE_CODE, row));
            sapCpiPartnerRoles.add(sapCpiPartnerRole);
        });
        return sapCpiPartnerRoles;
    }


    protected List<SapCpiOrderAddress> mapOrderAddresses(OrderModel orderModel)
    {
        final List<SapCpiOrderAddress> sapCpiOrderAddresses = new ArrayList<>();
        sapPartnerContributor.createRows(orderModel).forEach(row -> {
            SapCpiOrderAddress sapCpiOrderAddress = new SapCpiOrderAddress();
            sapCpiOrderAddress.setOrderId(mapAttribute(OrderCsvColumns.ORDER_ID, row));
            sapCpiOrderAddress.setDocumentAddressId(mapAttribute(PartnerCsvColumns.DOCUMENT_ADDRESS_ID, row));
            sapCpiOrderAddress.setEmail(mapAttribute(PartnerCsvColumns.EMAIL, row));
            sapCpiOrderAddress.setFaxNumber(mapAttribute(PartnerCsvColumns.FAX, row));
            sapCpiOrderAddress.setLanguageIsoCode(mapAttribute(PartnerCsvColumns.LANGUAGE_ISO_CODE, row));
            sapCpiOrderAddress.setTelNumber(mapAttribute(PartnerCsvColumns.TEL_NUMBER, row));
            sapCpiOrderAddress.setTitleCode(mapAttribute(PartnerCsvColumns.TITLE, row));
            setCpiLocation(sapCpiOrderAddress, row);
            setCpiName(sapCpiOrderAddress, row);
            if(sapCpiOrderAddress.getDocumentAddressId() != null && !sapCpiOrderAddress.getDocumentAddressId().isEmpty())
            {
                sapCpiOrderAddresses.add(sapCpiOrderAddress);
            }
        });
        return sapCpiOrderAddresses;
    }


    private void setCpiName(final SapCpiOrderAddress sapCpiOrderAddress, final Map<String, Object> row)
    {
        sapCpiOrderAddress.setFirstName(mapAttribute(PartnerCsvColumns.FIRST_NAME, row));
        sapCpiOrderAddress.setLastName(mapAttribute(PartnerCsvColumns.LAST_NAME, row));
        sapCpiOrderAddress.setMiddleName(mapAttribute(PartnerCsvColumns.MIDDLE_NAME, row));
        sapCpiOrderAddress.setMiddleName2(mapAttribute(PartnerCsvColumns.MIDDLE_NAME2, row));
    }


    private void setCpiLocation(final SapCpiOrderAddress sapCpiOrderAddress, final Map<String, Object> row)
    {
        sapCpiOrderAddress.setApartment(mapAttribute(PartnerCsvColumns.APPARTMENT, row));
        sapCpiOrderAddress.setBuilding(mapAttribute(PartnerCsvColumns.BUILDING, row));
        sapCpiOrderAddress.setCity(mapAttribute(PartnerCsvColumns.CITY, row));
        sapCpiOrderAddress.setCountryIsoCode(mapAttribute(PartnerCsvColumns.COUNTRY_ISO_CODE, row));
        sapCpiOrderAddress.setDistrict(mapAttribute(PartnerCsvColumns.DISTRICT, row));
        sapCpiOrderAddress.setHouseNumber(mapAttribute(PartnerCsvColumns.HOUSE_NUMBER, row));
        sapCpiOrderAddress.setPobox(mapAttribute(PartnerCsvColumns.POBOX, row));
        sapCpiOrderAddress.setPostalCode(mapAttribute(PartnerCsvColumns.POSTAL_CODE, row));
        sapCpiOrderAddress.setRegionIsoCode(mapAttribute(PartnerCsvColumns.REGION_ISO_CODE, row));
        sapCpiOrderAddress.setStreet(mapAttribute(PartnerCsvColumns.STREET, row));
    }


    protected List<SapCpiCreditCardPayment> mapCreditCards(OrderModel orderModel)
    {
        final List<SapCpiCreditCardPayment> sapCpiCreditCardPayments = new ArrayList<>();
        final int BIT_COUNT = 32;
        try
        {
            sapPaymentContributor.createRows(orderModel).forEach(row -> {
                SapCpiCreditCardPayment sapCpiCreditCardPayment = new SapCpiCreditCardPayment();
                sapCpiCreditCardPayment.setOrderId(mapAttribute(OrderCsvColumns.ORDER_ID, row));
                sapCpiCreditCardPayment.setCcOwner(mapAttribute(PaymentCsvColumns.CC_OWNER, row));
                sapCpiCreditCardPayment.setPaymentProvider(mapAttribute(PaymentCsvColumns.PAYMENT_PROVIDER, row));
                sapCpiCreditCardPayment.setSubscriptionId(mapAttribute(PaymentCsvColumns.SUBSCRIPTION_ID, row));
                String requestId = mapAttribute(PaymentCsvColumns.REQUEST_ID, row);
                requestId = Pattern.compile("^\\d+$").matcher(requestId).matches()
                                ? BigInteger.valueOf(Long.parseLong(requestId)).toString(BIT_COUNT).toUpperCase()
                                : requestId;
                sapCpiCreditCardPayment.setRequestId(requestId);
                setSapCpiCreditCardPaymentValidityDate(sapCpiCreditCardPayment, row);
                sapCpiCreditCardPayments.add(sapCpiCreditCardPayment);
            });
        }
        catch(RuntimeException ex)
        {
            throw new SapCpiOmmOrderConversionServiceException(String.format("Error occurs while setting the payment information for the order [%s]!", orderModel.getCode()), ex);
        }
        return sapCpiCreditCardPayments;
    }


    private void setSapCpiCreditCardPaymentValidityDate(final SapCpiCreditCardPayment sapCpiCreditCardPayment,
                    final Map<String, Object> rawItem)
    {
        final String month = mapAttribute(PaymentCsvColumns.VALID_TO_MONTH, rawItem);
        sapCpiCreditCardPayment.setValidToMonth(month);
        final String year = mapAttribute(PaymentCsvColumns.VALID_TO_YEAR, rawItem);
        if(year != null && month != null)
        {
            final YearMonth yearMonth = YearMonth.of(Integer.parseInt(year), Integer.parseInt(month));
            sapCpiCreditCardPayment.setValidToYear(yearMonth.atEndOfMonth().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        }
        else
        {
            sapCpiCreditCardPayment.setValidToYear(null);
        }
    }


    protected List<SapCpiOrderPriceComponent> mapOrderPrices(OrderModel orderModel)
    {
        final List<SapCpiOrderPriceComponent> sapCpiOrderPriceComponents = new ArrayList<>();
        sapSalesConditionsContributor.createRows(orderModel).forEach(row -> {
            SapCpiOrderPriceComponent sapCpiOrderPriceComponent = new SapCpiOrderPriceComponent();
            sapCpiOrderPriceComponent.setOrderId(mapAttribute(OrderCsvColumns.ORDER_ID, row));
            sapCpiOrderPriceComponent.setEntryNumber(mapAttribute(SalesConditionCsvColumns.CONDITION_ENTRY_NUMBER, row));
            sapCpiOrderPriceComponent.setConditionCode(mapAttribute(SalesConditionCsvColumns.CONDITION_CODE, row));
            sapCpiOrderPriceComponent.setConditionCounter(mapAttribute(SalesConditionCsvColumns.CONDITION_COUNTER, row));
            sapCpiOrderPriceComponent.setCurrencyIsoCode(mapAttribute(SalesConditionCsvColumns.CONDITION_CURRENCY_ISO_CODE, row));
            sapCpiOrderPriceComponent.setPriceQuantity(mapAttribute(SalesConditionCsvColumns.CONDITION_PRICE_QUANTITY, row));
            sapCpiOrderPriceComponent.setUnit(mapAttribute(SalesConditionCsvColumns.CONDITION_UNIT_CODE, row));
            sapCpiOrderPriceComponent.setValue(mapAttribute(SalesConditionCsvColumns.CONDITION_VALUE, row));
            sapCpiOrderPriceComponent.setAbsolute(mapAttribute(SalesConditionCsvColumns.ABSOLUTE, row));
            sapCpiOrderPriceComponents.add(sapCpiOrderPriceComponent);
        });
        return sapCpiOrderPriceComponents;
    }


    @Override
    public List<SapCpiOrderCancellation> convertCancelOrderToSapCpiCancelOrder(
                    OrderCancelRecordEntryModel orderCancelRecordEntryModel)
    {
        SapCpiOrderCancellation sapCpiOrderCancellation = new SapCpiOrderCancellation();
        final List<Map<String, Object>> cancellationRows = sapOrderCancelRequestContributor.createRows(orderCancelRecordEntryModel);
        cancellationRows.stream().findFirst().ifPresent(cancellationHeader -> {
            sapCpiOrderCancellation
                            .setSapCpiConfig(mapOrderConfigInfo(orderCancelRecordEntryModel.getModificationRecord().getOrder()));
            sapCpiOrderCancellation.setOrderId(mapAttribute(OrderCsvColumns.ORDER_ID, cancellationHeader));
            sapCpiOrderCancellation.setRejectionReason(mapAttribute(OrderEntryCsvColumns.REJECTION_REASON, cancellationHeader));
            List<SapCpiOrderCancellationItem> sapCpiOrderCancellationItems = new ArrayList<>();
            cancellationRows.forEach(cancellationItem -> {
                SapCpiOrderCancellationItem sapCpiOrderCancellationItem = new SapCpiOrderCancellationItem();
                sapCpiOrderCancellationItem.setEntryNumber(mapAttribute(OrderEntryCsvColumns.ENTRY_NUMBER, cancellationItem));
                sapCpiOrderCancellationItem.setProductCode(mapAttribute(OrderEntryCsvColumns.PRODUCT_CODE, cancellationItem));
                sapCpiOrderCancellationItems.add(sapCpiOrderCancellationItem);
            });
            sapCpiOrderCancellation.setSapCpiOrderCancellationItems(sapCpiOrderCancellationItems);
        });
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("SCPI cancel order object: %n %s",
                            ReflectionToStringBuilder.toString(sapCpiOrderCancellation, ToStringStyle.MULTI_LINE_STYLE)));
        }
        return Arrays.asList(sapCpiOrderCancellation);
    }


    protected String mapAttribute(String attribute, Map<String, Object> row)
    {
        return row.get(attribute) != null ? row.get(attribute).toString() : null;
    }


    protected String mapDateAttribute(String attribute, Map<String, Object> row)
    {
        if(row.get(attribute) != null && row.get(attribute) instanceof Date)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            return sdf.format((Date)row.get(attribute));
        }
        return null;
    }


    protected RawItemContributor<OrderModel> getSapOrderContributor()
    {
        return sapOrderContributor;
    }


    public void setSapOrderContributor(RawItemContributor<OrderModel> sapOrderContributor)
    {
        this.sapOrderContributor = sapOrderContributor;
    }


    protected RawItemContributor<OrderModel> getSapPaymentContributor()
    {
        return sapPaymentContributor;
    }


    public void setSapPaymentContributor(RawItemContributor<OrderModel> sapPaymentContributor)
    {
        this.sapPaymentContributor = sapPaymentContributor;
    }


    protected RawItemContributor<OrderModel> getSapPartnerContributor()
    {
        return sapPartnerContributor;
    }


    public void setSapPartnerContributor(RawItemContributor<OrderModel> sapPartnerContributor)
    {
        this.sapPartnerContributor = sapPartnerContributor;
    }


    protected RawItemContributor<OrderModel> getSapOrderEntryContributor()
    {
        return sapOrderEntryContributor;
    }


    public void setSapOrderEntryContributor(RawItemContributor<OrderModel> sapOrderEntryContributor)
    {
        this.sapOrderEntryContributor = sapOrderEntryContributor;
    }


    protected RawItemContributor<OrderModel> getSapSalesConditionsContributor()
    {
        return sapSalesConditionsContributor;
    }


    public void setSapSalesConditionsContributor(RawItemContributor<OrderModel> sapSalesConditionsContributor)
    {
        this.sapSalesConditionsContributor = sapSalesConditionsContributor;
    }


    protected RawItemContributor<OrderCancelRecordEntryModel> getSapOrderCancelRequestContributor()
    {
        return sapOrderCancelRequestContributor;
    }


    public void setSapOrderCancelRequestContributor(
                    RawItemContributor<OrderCancelRecordEntryModel> sapOrderCancelRequestContributor)
    {
        this.sapOrderCancelRequestContributor = sapOrderCancelRequestContributor;
    }


    protected SapCpiOrderDestinationService getSapCpiOrderDestinationService()
    {
        return sapCpiOrderDestinationService;
    }


    public void setSapCpiOrderDestinationService(SapCpiOrderDestinationService sapCpiOrderDestinationService)
    {
        this.sapCpiOrderDestinationService = sapCpiOrderDestinationService;
    }
}
