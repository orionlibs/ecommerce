/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.saps4omservices.order.populator;

import com.microsoft.sqlserver.jdbc.StringUtils;
import de.hybris.platform.b2b.model.B2BCostCenterModel;
import de.hybris.platform.b2b.model.B2BCustomerModel;
import de.hybris.platform.b2b.model.B2BUnitModel;
import de.hybris.platform.b2b.services.B2BCostCenterService;
import de.hybris.platform.b2b.services.B2BUnitService;
import de.hybris.platform.commerceservices.customer.CustomerService;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.enums.DeliveryStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.saps4omservices.dto.PartnerData;
import de.hybris.platform.saps4omservices.dto.PricingElementData;
import de.hybris.platform.saps4omservices.dto.SAPS4OMItemData;
import de.hybris.platform.saps4omservices.dto.SAPS4OMResponseData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSapS4OMOrderPopulator implements Populator<SAPS4OMResponseData, OrderModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSapS4OMOrderPopulator.class);
    private static final String PARTNER_FUNCTION_CONTACT_PERSON = "CP";
    private static final String OVERALL_TOTAL_DELIVERY_STATUS_B = "B";
    private static final String OVERALL_TOTAL_DELIVERY_STATUS_C = "C";
    private static final String OVERALL_SD_PROCESS_STATUS_C = "C";
    private UserService userService;
    private CustomerService customerService;
    private ProductService productService;
    private BaseStoreService baseStoreService;
    private CommonI18NService commonI18NService;
    private B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService;
    private B2BCostCenterService<B2BCostCenterModel, B2BCustomerModel> b2bCostCenterService;


    @Override
    public void populate(SAPS4OMResponseData source, OrderModel target) throws ConversionException
    {
        populateCommon(source, target);
        populateOrderStatus(source, target);
        populateDeliveryStatus(source, target);
        populateDeliveryAddress(source, target);
        populateDeliveryMode(source, target);
        populatePrice(source, target);
        populateCurrency(source, target);
        populateOrderEntry(source, target);
        target.setConsignments(Collections.emptySet());
        target.setSapOrders(Collections.emptySet());
    }


    protected void populateCommon(SAPS4OMResponseData source, OrderModel target)
    {
        target.setCode(source.getSalesOrder());
        Locale locale = getCommonI18NService().getLocaleForIsoCode(getCommonI18NService().getCurrentLanguage().getIsocode());
        PartnerData partnerData = null;
        if(source.getPartner() != null && source.getPartner().getSalesOrderPartners() != null)
        {
            LOG.debug("Populate user info for order with locale {}", locale);
            BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
            String contactPersonPartnerFunction = baseStore.getSAPConfiguration().getContactPersonPartnerFunctionCode(locale);
            partnerData = source.getPartner().getSalesOrderPartners().stream()
                            .filter(partner -> Objects.equals(Optional.ofNullable(contactPersonPartnerFunction).orElse(PARTNER_FUNCTION_CONTACT_PERSON), partner.getPartnerFunction()))
                            .findAny().orElse(null);
        }
        if(partnerData != null)
        {
            LOG.debug("Partner data found for partner function {}", partnerData.getPartnerFunction());
            target.setUser(getCustomerService().getCustomerByCustomerId(partnerData.getContactPerson()));
        }
        else
        {
            LOG.debug("Partner data not found. Falling back to current user");
            target.setUser(getUserService().getCurrentUser());
        }
        if(!StringUtils.isEmpty(source.getPurchaseOrderByCustomer()))
        {
            target.setProperty("purchaseOrderNumber", source.getPurchaseOrderByCustomer());
        }
        if(!StringUtils.isEmpty(source.getSalesOrderDate()))
        {
            LOG.debug("Populate date for order");
            Date salesOrderDate = new Date(Long.parseLong(source.getSalesOrderDate().replaceAll(".*?(\\d+).*", "$1")));
            target.setDate(salesOrderDate);
        }
    }


    protected void populateOrderEntry(SAPS4OMResponseData source, OrderModel target)
    {
        List<AbstractOrderEntryModel> orderEntryList = new ArrayList<>();
        Double itemsPriceTotal = 0.0;
        PricingElementData pricingElement = null;
        List<SAPS4OMItemData> salesOrderItems = source.getItems() != null && source.getItems().getSalesOrderItems() != null
                        ? source.getItems().getSalesOrderItems() : Collections.emptyList();
        B2BCostCenterModel costCenter = fetchCostCenter(target);
        LOG.debug("Cost center code {}", costCenter.getCode());
        for(SAPS4OMItemData item : salesOrderItems)
        {
            OrderEntryModel orderEntry = new OrderEntryModel();
            orderEntry.setProduct(getProductService().getProductForCode(item.getMaterial()));
            if(!StringUtils.isEmpty(item.getRequestedQuantity()))
            {
                LOG.debug("Populate quantity {} for order entry with product {}", item.getRequestedQuantity(), orderEntry.getProduct().getCode());
                orderEntry.setQuantity(Long.valueOf(item.getRequestedQuantity()));
            }
            pricingElement = fetchItemPricingElement(item);
            orderEntry.setBasePrice(fetchConditionRateValue(pricingElement));
            orderEntry.setTotalPrice(fetchConditionAmount(pricingElement));
            orderEntry.setCostCenter(costCenter);
            itemsPriceTotal = itemsPriceTotal + orderEntry.getTotalPrice();
            if(!StringUtils.isEmpty(item.getSalesOrderItem()))
            {
                LOG.debug("Populate entry number {}", item.getSalesOrderItem());
                orderEntry.setEntryNumber(Integer.valueOf(item.getSalesOrderItem()));
            }
            orderEntry.setOrder(target);
            orderEntryList.add(orderEntry);
        }
        target.setEntries(orderEntryList);
        target.setSubtotal(itemsPriceTotal);
    }


    protected void populateOrderStatus(final SAPS4OMResponseData source, final OrderModel target)
    {
        final String overallStatus = source.getOverallSDProcessStatus();
        LOG.debug("Overall status for order is {}", overallStatus);
        if(OVERALL_SD_PROCESS_STATUS_C.equals(overallStatus))
        {
            target.setStatus(OrderStatus.COMPLETED);
        }
        else
        {
            target.setStatus(OrderStatus.CREATED);
        }
        LOG.debug("Populate order status as {}", target.getStatus());
    }


    protected void populateDeliveryStatus(SAPS4OMResponseData source, OrderModel target)
    {
        LOG.debug("Populate delivery status when Order status is {}", target.getStatus());
        if(target.getStatus() == OrderStatus.CREATED)
        {
            final DeliveryStatus status = getShippingstatus(source);
            target.setDeliveryStatus(status);
        }
        else if(target.getStatus() == OrderStatus.COMPLETED)
        {
            target.setDeliveryStatus(DeliveryStatus.SHIPPED);
        }
        LOG.debug("Delivery status is {}", target.getDeliveryStatus());
    }


    protected DeliveryStatus getShippingstatus(final SAPS4OMResponseData source)
    {
        final String shippingStatus = source.getOverallTotalDeliveryStatus();
        if(shippingStatus == null)
        {
            return DeliveryStatus.NOTSHIPPED;
        }
        switch(shippingStatus)
        {
            case OVERALL_TOTAL_DELIVERY_STATUS_C:
                return DeliveryStatus.SHIPPED;
            case OVERALL_TOTAL_DELIVERY_STATUS_B:
                return DeliveryStatus.PARTSHIPPED;
            default:
                return DeliveryStatus.NOTSHIPPED;
        }
    }


    private void populateDeliveryAddress(SAPS4OMResponseData source, OrderModel target)
    {
        String soldToParty = source.getSoldToParty();
        B2BUnitModel parentB2BUnit = getB2bUnitService().getUnitForUid(soldToParty);
        LOG.debug("Fetch shipping address from parent unit with uid {}", parentB2BUnit.getUid());
        target.setUnit(parentB2BUnit);
        Set<B2BUnitModel> subB2BUnits = getB2bUnitService().getBranch(parentB2BUnit);
        for(final B2BUnitModel unit : subB2BUnits)
        {
            unit.getAddresses().stream().filter(AddressModel::getShippingAddress)
                            .findFirst()
                            .ifPresent(target::setDeliveryAddress);
        }
    }


    protected void populateDeliveryMode(SAPS4OMResponseData source, OrderModel target)
    {
        LOG.debug("Populate delivery mode for order from base store with shipping condition {}", source.getShippingCondition());
        BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
        if(!StringUtils.isEmpty(source.getShippingCondition()) && baseStore.getSAPConfiguration() != null)
        {
            baseStore.getSAPConfiguration().getSapDeliveryModes()
                            .stream()
                            .filter(deliveryMode -> source.getShippingCondition().equals(deliveryMode.getDeliveryValue()))
                            .findFirst().ifPresent(deliveryMode -> target.setDeliveryMode(deliveryMode.getDeliveryMode()));
        }
    }


    protected void populatePrice(SAPS4OMResponseData source, OrderModel target)
    {
        if(!StringUtils.isEmpty(source.getTotalNetAmount()))
        {
            LOG.debug("Populate total price for order with order net amount as {}", source.getTotalNetAmount());
            target.setTotalPrice(Double.valueOf(source.getTotalNetAmount()));
        }
        target.setDeliveryCost(fetchShippingPrice(source));
    }


    protected Double fetchShippingPrice(SAPS4OMResponseData source)
    {
        Double shippingPrice = 0.0;
        PricingElementData pricingElement = null;
        BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
        if(source.getPricingElements() != null && source.getPricingElements().getSalesOrderPricingElements() != null && baseStore.getSAPConfiguration() != null)
        {
            LOG.debug("Populate delivery cost for order with delivery condition type as {}", baseStore.getSAPConfiguration().getSaps4deliverycostconditiontype());
            pricingElement = source.getPricingElements().getSalesOrderPricingElements().stream()
                            .filter(data -> baseStore.getSAPConfiguration().getSaps4deliverycostconditiontype().equals(data.getConditionType()))
                            .findAny().orElse(null);
            shippingPrice = shippingPrice + fetchConditionRateValue(pricingElement);
        }
        return shippingPrice;
    }


    protected PricingElementData fetchItemPricingElement(SAPS4OMItemData source)
    {
        PricingElementData pricingElement = null;
        BaseStoreModel baseStore = getBaseStoreService().getCurrentBaseStore();
        if(baseStore.getSAPConfiguration() != null && baseStore.getSAPConfiguration().getSaps4itempriceconditiontype() != null)
        {
            LOG.debug("Populate item price for order entry with item price condition type as {}", baseStore.getSAPConfiguration().getSaps4itempriceconditiontype());
            pricingElement = source.getPricingElements().getSalesOrderPricingElements().stream()
                            .filter(data -> baseStore.getSAPConfiguration().getSaps4itempriceconditiontype().equals(data.getConditionType()))
                            .findAny().orElse(null);
        }
        return pricingElement;
    }


    protected B2BCostCenterModel fetchCostCenter(OrderModel target)
    {
        List<B2BCostCenterModel> b2bCostCenters = new ArrayList<>();
        if(target.getUnit() != null && target.getCurrency() != null)
        {
            b2bCostCenters = getB2bCostCenterService().getCostCentersForUnitBranch(target.getUnit(), target.getCurrency());
        }
        return Optional.ofNullable(b2bCostCenters).orElse(Collections.emptyList()).stream().findFirst().orElse(null);
    }


    protected void populateCurrency(SAPS4OMResponseData source, OrderModel target)
    {
        if(source.getTotalNetAmount() != null && source.getTransactionCurrency() != null)
        {
            LOG.debug("Populate curreny info for order with transaction curreny as {}", source.getTransactionCurrency());
            final CurrencyModel currency = getCommonI18NService().getCurrency(source.getTransactionCurrency());
            if(currency == null)
            {
                throw new IllegalArgumentException("Order currency must not be null");
            }
            target.setCurrency(currency);
        }
    }


    protected double fetchConditionRateValue(PricingElementData pricingElement)
    {
        if(pricingElement != null && pricingElement.getConditionRateValue() != null)
        {
            return Double.valueOf(pricingElement.getConditionRateValue());
        }
        return 0.0;
    }


    protected double fetchConditionAmount(PricingElementData pricingElement)
    {
        if(pricingElement != null && pricingElement.getConditionAmount() != null)
        {
            return Double.valueOf(pricingElement.getConditionAmount());
        }
        return 0.0;
    }


    public UserService getUserService()
    {
        return userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public CustomerService getCustomerService()
    {
        return customerService;
    }


    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }


    public ProductService getProductService()
    {
        return productService;
    }


    public void setProductService(ProductService productService)
    {
        this.productService = productService;
    }


    public CommonI18NService getCommonI18NService()
    {
        return commonI18NService;
    }


    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public BaseStoreService getBaseStoreService()
    {
        return baseStoreService;
    }


    public void setBaseStoreService(BaseStoreService baseStoreService)
    {
        this.baseStoreService = baseStoreService;
    }


    public B2BUnitService<B2BUnitModel, B2BCustomerModel> getB2bUnitService()
    {
        return b2bUnitService;
    }


    public void setB2bUnitService(B2BUnitService<B2BUnitModel, B2BCustomerModel> b2bUnitService)
    {
        this.b2bUnitService = b2bUnitService;
    }


    public B2BCostCenterService<B2BCostCenterModel, B2BCustomerModel> getB2bCostCenterService()
    {
        return b2bCostCenterService;
    }


    public void setB2bCostCenterService(B2BCostCenterService<B2BCostCenterModel, B2BCustomerModel> b2bCostCenterService)
    {
        this.b2bCostCenterService = b2bCostCenterService;
    }
}
