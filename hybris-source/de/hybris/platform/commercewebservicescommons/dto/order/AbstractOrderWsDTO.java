package de.hybris.platform.commercewebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PromotionResultWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.AddressWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.PrincipalWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.voucher.VoucherWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "AbstractOrder", description = "Representation of an Abstract Order")
public class AbstractOrderWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Code number of order")
    private String code;
    @ApiModelProperty(name = "net", value = "Flag stating iv value is net-value")
    private Boolean net;
    @ApiModelProperty(name = "totalPriceWithTax", value = "Total price with tax")
    private PriceWsDTO totalPriceWithTax;
    @ApiModelProperty(name = "totalPrice", value = "Total price value")
    private PriceWsDTO totalPrice;
    @ApiModelProperty(name = "totalTax", value = "Total tax price")
    private PriceWsDTO totalTax;
    @ApiModelProperty(name = "subTotal", value = "Subtotal price")
    private PriceWsDTO subTotal;
    @ApiModelProperty(name = "deliveryCost", value = "Delivery cost")
    private PriceWsDTO deliveryCost;
    @ApiModelProperty(name = "entries", value = "List of order entries")
    private List<OrderEntryWsDTO> entries;
    @ApiModelProperty(name = "entryGroups", value = "List of entry groups")
    private List<EntryGroupWsDTO> entryGroups;
    @ApiModelProperty(name = "totalItems")
    private Integer totalItems;
    @ApiModelProperty(name = "deliveryMode", value = "Delivery mode information")
    private DeliveryModeWsDTO deliveryMode;
    @ApiModelProperty(name = "deliveryAddress", value = "Delivery address")
    private AddressWsDTO deliveryAddress;
    @ApiModelProperty(name = "paymentInfo", value = "Payment information")
    private PaymentDetailsWsDTO paymentInfo;
    @ApiModelProperty(name = "appliedOrderPromotions", value = "List of applied order promotions")
    private List<PromotionResultWsDTO> appliedOrderPromotions;
    @ApiModelProperty(name = "appliedProductPromotions", value = "List of applied product promotions")
    private List<PromotionResultWsDTO> appliedProductPromotions;
    @ApiModelProperty(name = "productDiscounts", value = "Product discounts")
    private PriceWsDTO productDiscounts;
    @ApiModelProperty(name = "orderDiscounts", value = "Order discounts")
    private PriceWsDTO orderDiscounts;
    @ApiModelProperty(name = "totalDiscounts", value = "Total discounts")
    private PriceWsDTO totalDiscounts;
    @ApiModelProperty(name = "site", value = "Site")
    private String site;
    @ApiModelProperty(name = "store", value = "Store")
    private String store;
    @ApiModelProperty(name = "guid", value = "Guest user id identifier")
    private String guid;
    @ApiModelProperty(name = "calculated", value = "Flag showing if order is calculated")
    private Boolean calculated;
    @ApiModelProperty(name = "appliedVouchers", value = "List of applied vouchers")
    private List<VoucherWsDTO> appliedVouchers;
    @ApiModelProperty(name = "user", value = "User information")
    private PrincipalWsDTO user;
    @ApiModelProperty(name = "pickupOrderGroups", value = "List of pickup order entry group")
    private List<PickupOrderEntryGroupWsDTO> pickupOrderGroups;
    @ApiModelProperty(name = "deliveryOrderGroups", value = "List of delivery order entries group")
    private List<DeliveryOrderEntryGroupWsDTO> deliveryOrderGroups;
    @ApiModelProperty(name = "pickupItemsQuantity", value = "Quantity of pickup items")
    private Long pickupItemsQuantity;
    @ApiModelProperty(name = "deliveryItemsQuantity", value = "Quantity of delivery items")
    private Long deliveryItemsQuantity;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setNet(Boolean net)
    {
        this.net = net;
    }


    public Boolean getNet()
    {
        return this.net;
    }


    public void setTotalPriceWithTax(PriceWsDTO totalPriceWithTax)
    {
        this.totalPriceWithTax = totalPriceWithTax;
    }


    public PriceWsDTO getTotalPriceWithTax()
    {
        return this.totalPriceWithTax;
    }


    public void setTotalPrice(PriceWsDTO totalPrice)
    {
        this.totalPrice = totalPrice;
    }


    public PriceWsDTO getTotalPrice()
    {
        return this.totalPrice;
    }


    public void setTotalTax(PriceWsDTO totalTax)
    {
        this.totalTax = totalTax;
    }


    public PriceWsDTO getTotalTax()
    {
        return this.totalTax;
    }


    public void setSubTotal(PriceWsDTO subTotal)
    {
        this.subTotal = subTotal;
    }


    public PriceWsDTO getSubTotal()
    {
        return this.subTotal;
    }


    public void setDeliveryCost(PriceWsDTO deliveryCost)
    {
        this.deliveryCost = deliveryCost;
    }


    public PriceWsDTO getDeliveryCost()
    {
        return this.deliveryCost;
    }


    public void setEntries(List<OrderEntryWsDTO> entries)
    {
        this.entries = entries;
    }


    public List<OrderEntryWsDTO> getEntries()
    {
        return this.entries;
    }


    public void setEntryGroups(List<EntryGroupWsDTO> entryGroups)
    {
        this.entryGroups = entryGroups;
    }


    public List<EntryGroupWsDTO> getEntryGroups()
    {
        return this.entryGroups;
    }


    public void setTotalItems(Integer totalItems)
    {
        this.totalItems = totalItems;
    }


    public Integer getTotalItems()
    {
        return this.totalItems;
    }


    public void setDeliveryMode(DeliveryModeWsDTO deliveryMode)
    {
        this.deliveryMode = deliveryMode;
    }


    public DeliveryModeWsDTO getDeliveryMode()
    {
        return this.deliveryMode;
    }


    public void setDeliveryAddress(AddressWsDTO deliveryAddress)
    {
        this.deliveryAddress = deliveryAddress;
    }


    public AddressWsDTO getDeliveryAddress()
    {
        return this.deliveryAddress;
    }


    public void setPaymentInfo(PaymentDetailsWsDTO paymentInfo)
    {
        this.paymentInfo = paymentInfo;
    }


    public PaymentDetailsWsDTO getPaymentInfo()
    {
        return this.paymentInfo;
    }


    public void setAppliedOrderPromotions(List<PromotionResultWsDTO> appliedOrderPromotions)
    {
        this.appliedOrderPromotions = appliedOrderPromotions;
    }


    public List<PromotionResultWsDTO> getAppliedOrderPromotions()
    {
        return this.appliedOrderPromotions;
    }


    public void setAppliedProductPromotions(List<PromotionResultWsDTO> appliedProductPromotions)
    {
        this.appliedProductPromotions = appliedProductPromotions;
    }


    public List<PromotionResultWsDTO> getAppliedProductPromotions()
    {
        return this.appliedProductPromotions;
    }


    public void setProductDiscounts(PriceWsDTO productDiscounts)
    {
        this.productDiscounts = productDiscounts;
    }


    public PriceWsDTO getProductDiscounts()
    {
        return this.productDiscounts;
    }


    public void setOrderDiscounts(PriceWsDTO orderDiscounts)
    {
        this.orderDiscounts = orderDiscounts;
    }


    public PriceWsDTO getOrderDiscounts()
    {
        return this.orderDiscounts;
    }


    public void setTotalDiscounts(PriceWsDTO totalDiscounts)
    {
        this.totalDiscounts = totalDiscounts;
    }


    public PriceWsDTO getTotalDiscounts()
    {
        return this.totalDiscounts;
    }


    public void setSite(String site)
    {
        this.site = site;
    }


    public String getSite()
    {
        return this.site;
    }


    public void setStore(String store)
    {
        this.store = store;
    }


    public String getStore()
    {
        return this.store;
    }


    public void setGuid(String guid)
    {
        this.guid = guid;
    }


    public String getGuid()
    {
        return this.guid;
    }


    public void setCalculated(Boolean calculated)
    {
        this.calculated = calculated;
    }


    public Boolean getCalculated()
    {
        return this.calculated;
    }


    public void setAppliedVouchers(List<VoucherWsDTO> appliedVouchers)
    {
        this.appliedVouchers = appliedVouchers;
    }


    public List<VoucherWsDTO> getAppliedVouchers()
    {
        return this.appliedVouchers;
    }


    public void setUser(PrincipalWsDTO user)
    {
        this.user = user;
    }


    public PrincipalWsDTO getUser()
    {
        return this.user;
    }


    public void setPickupOrderGroups(List<PickupOrderEntryGroupWsDTO> pickupOrderGroups)
    {
        this.pickupOrderGroups = pickupOrderGroups;
    }


    public List<PickupOrderEntryGroupWsDTO> getPickupOrderGroups()
    {
        return this.pickupOrderGroups;
    }


    public void setDeliveryOrderGroups(List<DeliveryOrderEntryGroupWsDTO> deliveryOrderGroups)
    {
        this.deliveryOrderGroups = deliveryOrderGroups;
    }


    public List<DeliveryOrderEntryGroupWsDTO> getDeliveryOrderGroups()
    {
        return this.deliveryOrderGroups;
    }


    public void setPickupItemsQuantity(Long pickupItemsQuantity)
    {
        this.pickupItemsQuantity = pickupItemsQuantity;
    }


    public Long getPickupItemsQuantity()
    {
        return this.pickupItemsQuantity;
    }


    public void setDeliveryItemsQuantity(Long deliveryItemsQuantity)
    {
        this.deliveryItemsQuantity = deliveryItemsQuantity;
    }


    public Long getDeliveryItemsQuantity()
    {
        return this.deliveryItemsQuantity;
    }
}
