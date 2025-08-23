/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.saprevenuecloudorder.service.impl;

import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundCardPaymentModel;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundOrderCancellationModel;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundOrderItemModel;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundOrderModel;
import de.hybris.platform.sap.sapcpiadapter.model.SAPCpiOutboundPartnerRoleModel;
import de.hybris.platform.sap.sapcpiorderexchange.service.SapCpiOrderOutboundConversionService;
import de.hybris.platform.sap.saprevenuecloudorder.constants.SaprevenuecloudorderConstants;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.subscriptionservices.model.SubscriptionPricePlanModel;
import de.hybris.platform.subscriptionservices.price.SubscriptionCommercePriceService;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import org.apache.log4j.Logger;

/**
 * SAP CPI Subscription Order Outbound Conversion Service
 */
public class SapRevenueCloudOrderConversionService implements SapCpiOrderOutboundConversionService
{
    private ConfigurationService configurationService;
    private CMSSiteService cmsSiteService;
    private CommonI18NService commonI18NService;
    private SubscriptionCommercePriceService commercePriceService;
    private B2BUnitService b2bUnitService;
    private static final Logger LOG = Logger.getLogger(SapRevenueCloudOrderConversionService.class);


    @Override
    public SAPCpiOutboundOrderModel convertOrderToSapCpiOrder(OrderModel orderModel)
    {
        SAPCpiOutboundOrderModel sapCpiOutboundOrder = new SAPCpiOutboundOrderModel();
        if(orderModel == null || orderModel.getEntries().isEmpty())
        {
            return sapCpiOutboundOrder;
        }
        sapCpiOutboundOrder.setOrderId(orderModel.getEntries().get(0).getOrder().getCode());
        sapCpiOutboundOrder.setBaseStoreUid(orderModel.getStore().getUid());
        final CustomerModel customer = ((CustomerModel)orderModel.getUser());
        Set<SAPCpiOutboundPartnerRoleModel> sapCpiOutboundPartnerRoles = new HashSet<>();
        SAPCpiOutboundPartnerRoleModel partnerRole = new SAPCpiOutboundPartnerRoleModel();
        //b2b
        if(customer instanceof B2BCustomerModel)
        {
            B2BCustomerModel b2bCustomer = (B2BCustomerModel)customer;
            B2BUnitModel b2bUnit = (B2BUnitModel)getB2bUnitService().getParent(b2bCustomer);
            B2BUnitModel rootUnit = (B2BUnitModel)getB2bUnitService().getRootUnit(b2bUnit);
            partnerRole.setPartnerId(rootUnit.getUid());
            partnerRole.setRevenueCloudCustomerId(rootUnit.getRevenueCloudCompanyId());
        }
        //b2c
        else
        {
            partnerRole.setPartnerId(customer.getCustomerID());
            partnerRole.setRevenueCloudCustomerId(customer.getRevenueCloudCustomerId());
        }
        partnerRole.setEmail(customer.getUid());
        sapCpiOutboundPartnerRoles.add(partnerRole);
        sapCpiOutboundOrder.setSapCpiOutboundPartnerRoles(sapCpiOutboundPartnerRoles);
        populatePaymentDetails(sapCpiOutboundOrder, orderModel.getPaymentInfo());
        populateOrderItems(sapCpiOutboundOrder, orderModel);
        return sapCpiOutboundOrder;
    }


    @Override
    public List<SAPCpiOutboundOrderCancellationModel> convertCancelOrderToSapCpiCancelOrder(
                    OrderCancelRecordEntryModel orderCancelRecordEntryModel)
    {
        return Collections.emptyList();
    }


    protected void populatePaymentDetails(SAPCpiOutboundOrderModel sapCpiOutboundOrder,
                    PaymentInfoModel paymentInfoModel)
    {
        SAPCpiOutboundCardPaymentModel sapCpiOutboundCardPayment = new SAPCpiOutboundCardPaymentModel();
        Set<SAPCpiOutboundCardPaymentModel> payments = new HashSet<>();
        if(paymentInfoModel instanceof CreditCardPaymentInfoModel)
        {
            sapCpiOutboundCardPayment.setPaymentProvider(SaprevenuecloudorderConstants.CARD_PAYMENT_TYPE);
            sapCpiOutboundCardPayment
                            .setSubscriptionId(((CreditCardPaymentInfoModel)paymentInfoModel).getSubscriptionId());
        }
        else
        {
            sapCpiOutboundCardPayment.setPaymentProvider(getConfigurationService().getConfiguration()
                            .getString(SaprevenuecloudorderConstants.DEFAULT_PAYMENT_TYPE));
        }
        payments.add(sapCpiOutboundCardPayment);
        sapCpiOutboundOrder.setSapCpiOutboundCardPayments(payments);
    }


    protected void populateOrderItems(SAPCpiOutboundOrderModel sapCpiOutboundOrder, OrderModel order)
    {
        Set<SAPCpiOutboundOrderItemModel> items = new HashSet<>();
        order.getEntries().forEach(entry -> {
            SAPCpiOutboundOrderItemModel item = new SAPCpiOutboundOrderItemModel();
            item.setProductCode(entry.getProduct().getSubscriptionCode());
            item.setQuantity(entry.getQuantity().toString());
            item.setUnit(entry.getUnit().getCode());
            item.setEntryNumber(entry.getEntryNumber().toString());
            try
            {
                getCmsSiteService().setCurrentSiteAndCatalogVersions((CMSSiteModel)order.getSite(), true);
            }
            catch(CMSItemNotFoundException e)
            {
                LOG.error(e);
            }
            getCommonI18NService().setCurrentCurrency(order.getCurrency());
            final SubscriptionPricePlanModel pricePlanModel = getCommercePriceService()
                            .getSubscriptionPricePlanForProduct(entry.getProduct());
            if(pricePlanModel != null)
            {
                item.setPricePlanId(pricePlanModel.getPricePlanId());
            }
            final TimeZone tzUTC = TimeZone.getTimeZone(ZoneId.of(SaprevenuecloudorderConstants.UTC));
            // take current time of base store
            String validFromDate = ZonedDateTime.now().withZoneSameInstant(tzUTC.toZoneId())
                            .format(SaprevenuecloudorderConstants.ISO8601_FORMATTER);
            item.setSubscriptionValidFrom(validFromDate);
            if(entry.getProduct() != null && entry.getProduct().getSubscriptionValidTerm() != null)
            {
                item.setOverwriteContractTerm(Boolean.TRUE.toString());
                item.setSubscriptionValidTerm(entry.getProduct().getSubscriptionValidTerm().toString());
            }
            items.add(item);
        });
        sapCpiOutboundOrder.setSapCpiOutboundOrderItems(items);
    }


    /**
     * @return the configurationService
     */
    public ConfigurationService getConfigurationService()
    {
        return configurationService;
    }


    /**
     * @param configurationService the configurationService to set
     */
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    /**
     * @return the cmsSiteService
     */
    public CMSSiteService getCmsSiteService()
    {
        return cmsSiteService;
    }


    /**
     * @param cmsSiteService the cmsSiteService to set
     */
    public void setCmsSiteService(CMSSiteService cmsSiteService)
    {
        this.cmsSiteService = cmsSiteService;
    }


    /**
     * @return the commonI18NService
     */
    public CommonI18NService getCommonI18NService()
    {
        return commonI18NService;
    }


    /**
     * @param commonI18NService the commonI18NService to set
     */
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    /**
     * @return the commercePriceService
     */
    public SubscriptionCommercePriceService getCommercePriceService()
    {
        return commercePriceService;
    }


    /**
     * @param commercePriceService the commercePriceService to set
     */
    public void setCommercePriceService(SubscriptionCommercePriceService commercePriceService)
    {
        this.commercePriceService = commercePriceService;
    }


    /**
     * @return the b2bUnitService
     */
    public B2BUnitService getB2bUnitService()
    {
        return b2bUnitService;
    }


    /**
     * @param b2bUnitService the b2bUnitService to set
     */
    public void setB2bUnitService(B2BUnitService b2bUnitService)
    {
        this.b2bUnitService = b2bUnitService;
    }
}
