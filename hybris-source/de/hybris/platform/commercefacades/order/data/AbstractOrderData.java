package de.hybris.platform.commercefacades.order.data;

import de.hybris.platform.commercefacades.comment.data.CommentData;
import de.hybris.platform.commercefacades.order.EntryGroupData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PromotionResultData;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.PrincipalData;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AbstractOrderData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private String description;
    private Date expirationTime;
    private boolean net;
    private PriceData totalPriceWithTax;
    private PriceData totalPrice;
    private PriceData totalTax;
    private PriceData subTotal;
    private PriceData subTotalWithoutQuoteDiscounts;
    private PriceData deliveryCost;
    private List<OrderEntryData> entries;
    private Integer totalItems;
    private DeliveryModeData deliveryMode;
    private AddressData deliveryAddress;
    private CCPaymentInfoData paymentInfo;
    private List<PromotionResultData> appliedOrderPromotions;
    private List<PromotionResultData> appliedProductPromotions;
    private PriceData productDiscounts;
    private PriceData orderDiscounts;
    private PriceData quoteDiscounts;
    private Double quoteDiscountsRate;
    private String quoteDiscountsType;
    private PriceData totalDiscounts;
    private PriceData totalDiscountsWithQuoteDiscounts;
    private PriceData subTotalWithDiscounts;
    private String site;
    private String store;
    private String guid;
    private boolean calculated;
    private List<String> appliedVouchers;
    private PrincipalData user;
    private List<PickupOrderEntryGroupData> pickupOrderGroups;
    private List<DeliveryOrderEntryGroupData> deliveryOrderGroups;
    private Long pickupItemsQuantity;
    private Long deliveryItemsQuantity;
    private Integer totalUnitCount;
    private List<CommentData> comments;
    private List<EntryGroupData> rootGroups;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setExpirationTime(Date expirationTime)
    {
        this.expirationTime = expirationTime;
    }


    public Date getExpirationTime()
    {
        return this.expirationTime;
    }


    public void setNet(boolean net)
    {
        this.net = net;
    }


    public boolean isNet()
    {
        return this.net;
    }


    public void setTotalPriceWithTax(PriceData totalPriceWithTax)
    {
        this.totalPriceWithTax = totalPriceWithTax;
    }


    public PriceData getTotalPriceWithTax()
    {
        return this.totalPriceWithTax;
    }


    public void setTotalPrice(PriceData totalPrice)
    {
        this.totalPrice = totalPrice;
    }


    public PriceData getTotalPrice()
    {
        return this.totalPrice;
    }


    public void setTotalTax(PriceData totalTax)
    {
        this.totalTax = totalTax;
    }


    public PriceData getTotalTax()
    {
        return this.totalTax;
    }


    public void setSubTotal(PriceData subTotal)
    {
        this.subTotal = subTotal;
    }


    public PriceData getSubTotal()
    {
        return this.subTotal;
    }


    public void setSubTotalWithoutQuoteDiscounts(PriceData subTotalWithoutQuoteDiscounts)
    {
        this.subTotalWithoutQuoteDiscounts = subTotalWithoutQuoteDiscounts;
    }


    public PriceData getSubTotalWithoutQuoteDiscounts()
    {
        return this.subTotalWithoutQuoteDiscounts;
    }


    public void setDeliveryCost(PriceData deliveryCost)
    {
        this.deliveryCost = deliveryCost;
    }


    public PriceData getDeliveryCost()
    {
        return this.deliveryCost;
    }


    public void setEntries(List<OrderEntryData> entries)
    {
        this.entries = entries;
    }


    public List<OrderEntryData> getEntries()
    {
        return this.entries;
    }


    public void setTotalItems(Integer totalItems)
    {
        this.totalItems = totalItems;
    }


    public Integer getTotalItems()
    {
        return this.totalItems;
    }


    public void setDeliveryMode(DeliveryModeData deliveryMode)
    {
        this.deliveryMode = deliveryMode;
    }


    public DeliveryModeData getDeliveryMode()
    {
        return this.deliveryMode;
    }


    public void setDeliveryAddress(AddressData deliveryAddress)
    {
        this.deliveryAddress = deliveryAddress;
    }


    public AddressData getDeliveryAddress()
    {
        return this.deliveryAddress;
    }


    public void setPaymentInfo(CCPaymentInfoData paymentInfo)
    {
        this.paymentInfo = paymentInfo;
    }


    public CCPaymentInfoData getPaymentInfo()
    {
        return this.paymentInfo;
    }


    public void setAppliedOrderPromotions(List<PromotionResultData> appliedOrderPromotions)
    {
        this.appliedOrderPromotions = appliedOrderPromotions;
    }


    public List<PromotionResultData> getAppliedOrderPromotions()
    {
        return this.appliedOrderPromotions;
    }


    public void setAppliedProductPromotions(List<PromotionResultData> appliedProductPromotions)
    {
        this.appliedProductPromotions = appliedProductPromotions;
    }


    public List<PromotionResultData> getAppliedProductPromotions()
    {
        return this.appliedProductPromotions;
    }


    public void setProductDiscounts(PriceData productDiscounts)
    {
        this.productDiscounts = productDiscounts;
    }


    public PriceData getProductDiscounts()
    {
        return this.productDiscounts;
    }


    public void setOrderDiscounts(PriceData orderDiscounts)
    {
        this.orderDiscounts = orderDiscounts;
    }


    public PriceData getOrderDiscounts()
    {
        return this.orderDiscounts;
    }


    public void setQuoteDiscounts(PriceData quoteDiscounts)
    {
        this.quoteDiscounts = quoteDiscounts;
    }


    public PriceData getQuoteDiscounts()
    {
        return this.quoteDiscounts;
    }


    public void setQuoteDiscountsRate(Double quoteDiscountsRate)
    {
        this.quoteDiscountsRate = quoteDiscountsRate;
    }


    public Double getQuoteDiscountsRate()
    {
        return this.quoteDiscountsRate;
    }


    public void setQuoteDiscountsType(String quoteDiscountsType)
    {
        this.quoteDiscountsType = quoteDiscountsType;
    }


    public String getQuoteDiscountsType()
    {
        return this.quoteDiscountsType;
    }


    public void setTotalDiscounts(PriceData totalDiscounts)
    {
        this.totalDiscounts = totalDiscounts;
    }


    public PriceData getTotalDiscounts()
    {
        return this.totalDiscounts;
    }


    public void setTotalDiscountsWithQuoteDiscounts(PriceData totalDiscountsWithQuoteDiscounts)
    {
        this.totalDiscountsWithQuoteDiscounts = totalDiscountsWithQuoteDiscounts;
    }


    public PriceData getTotalDiscountsWithQuoteDiscounts()
    {
        return this.totalDiscountsWithQuoteDiscounts;
    }


    @Deprecated(since = "6.3", forRemoval = true)
    public void setSubTotalWithDiscounts(PriceData subTotalWithDiscounts)
    {
        this.subTotalWithDiscounts = subTotalWithDiscounts;
    }


    @Deprecated(since = "6.3", forRemoval = true)
    public PriceData getSubTotalWithDiscounts()
    {
        return this.subTotalWithDiscounts;
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


    public void setCalculated(boolean calculated)
    {
        this.calculated = calculated;
    }


    public boolean isCalculated()
    {
        return this.calculated;
    }


    public void setAppliedVouchers(List<String> appliedVouchers)
    {
        this.appliedVouchers = appliedVouchers;
    }


    public List<String> getAppliedVouchers()
    {
        return this.appliedVouchers;
    }


    public void setUser(PrincipalData user)
    {
        this.user = user;
    }


    public PrincipalData getUser()
    {
        return this.user;
    }


    public void setPickupOrderGroups(List<PickupOrderEntryGroupData> pickupOrderGroups)
    {
        this.pickupOrderGroups = pickupOrderGroups;
    }


    public List<PickupOrderEntryGroupData> getPickupOrderGroups()
    {
        return this.pickupOrderGroups;
    }


    public void setDeliveryOrderGroups(List<DeliveryOrderEntryGroupData> deliveryOrderGroups)
    {
        this.deliveryOrderGroups = deliveryOrderGroups;
    }


    public List<DeliveryOrderEntryGroupData> getDeliveryOrderGroups()
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


    public void setTotalUnitCount(Integer totalUnitCount)
    {
        this.totalUnitCount = totalUnitCount;
    }


    public Integer getTotalUnitCount()
    {
        return this.totalUnitCount;
    }


    public void setComments(List<CommentData> comments)
    {
        this.comments = comments;
    }


    public List<CommentData> getComments()
    {
        return this.comments;
    }


    public void setRootGroups(List<EntryGroupData> rootGroups)
    {
        this.rootGroups = rootGroups;
    }


    public List<EntryGroupData> getRootGroups()
    {
        return this.rootGroups;
    }
}
