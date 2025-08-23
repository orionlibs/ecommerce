package de.hybris.platform.commercefacades.order.data;

import de.hybris.platform.catalog.enums.ProductInfoStatus;
import de.hybris.platform.commercefacades.comment.data.CommentData;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrderEntryData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer entryNumber;
    private Long quantity;
    private PriceData basePrice;
    private PriceData totalPrice;
    private ProductData product;
    private boolean updateable;
    private DeliveryModeData deliveryMode;
    private PointOfServiceData deliveryPointOfService;
    private List<OrderEntryData> entries;
    private List<ConfigurationInfoData> configurationInfos;
    private Map<ProductInfoStatus, Integer> statusSummaryMap;
    private Collection<Integer> entryGroupNumbers;
    private List<CommentData> comments;
    private String url;
    private long cancellableQty;
    private long returnableQty;
    private PriceData cancelledItemsPrice;
    private PriceData returnedItemsPrice;
    private Set<String> supportedActions;
    private List<OrderEntryData> otherVariants;
    private String contractPrice;
    private String formattedContractPrice;
    private PriceData freightPrice;


    public void setEntryNumber(Integer entryNumber)
    {
        this.entryNumber = entryNumber;
    }


    public Integer getEntryNumber()
    {
        return this.entryNumber;
    }


    public void setQuantity(Long quantity)
    {
        this.quantity = quantity;
    }


    public Long getQuantity()
    {
        return this.quantity;
    }


    public void setBasePrice(PriceData basePrice)
    {
        this.basePrice = basePrice;
    }


    public PriceData getBasePrice()
    {
        return this.basePrice;
    }


    public void setTotalPrice(PriceData totalPrice)
    {
        this.totalPrice = totalPrice;
    }


    public PriceData getTotalPrice()
    {
        return this.totalPrice;
    }


    public void setProduct(ProductData product)
    {
        this.product = product;
    }


    public ProductData getProduct()
    {
        return this.product;
    }


    public void setUpdateable(boolean updateable)
    {
        this.updateable = updateable;
    }


    public boolean isUpdateable()
    {
        return this.updateable;
    }


    public void setDeliveryMode(DeliveryModeData deliveryMode)
    {
        this.deliveryMode = deliveryMode;
    }


    public DeliveryModeData getDeliveryMode()
    {
        return this.deliveryMode;
    }


    public void setDeliveryPointOfService(PointOfServiceData deliveryPointOfService)
    {
        this.deliveryPointOfService = deliveryPointOfService;
    }


    public PointOfServiceData getDeliveryPointOfService()
    {
        return this.deliveryPointOfService;
    }


    public void setEntries(List<OrderEntryData> entries)
    {
        this.entries = entries;
    }


    public List<OrderEntryData> getEntries()
    {
        return this.entries;
    }


    public void setConfigurationInfos(List<ConfigurationInfoData> configurationInfos)
    {
        this.configurationInfos = configurationInfos;
    }


    public List<ConfigurationInfoData> getConfigurationInfos()
    {
        return this.configurationInfos;
    }


    public void setStatusSummaryMap(Map<ProductInfoStatus, Integer> statusSummaryMap)
    {
        this.statusSummaryMap = statusSummaryMap;
    }


    public Map<ProductInfoStatus, Integer> getStatusSummaryMap()
    {
        return this.statusSummaryMap;
    }


    public void setEntryGroupNumbers(Collection<Integer> entryGroupNumbers)
    {
        this.entryGroupNumbers = entryGroupNumbers;
    }


    public Collection<Integer> getEntryGroupNumbers()
    {
        return this.entryGroupNumbers;
    }


    public void setComments(List<CommentData> comments)
    {
        this.comments = comments;
    }


    public List<CommentData> getComments()
    {
        return this.comments;
    }


    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getUrl()
    {
        return this.url;
    }


    public void setCancellableQty(long cancellableQty)
    {
        this.cancellableQty = cancellableQty;
    }


    public long getCancellableQty()
    {
        return this.cancellableQty;
    }


    public void setReturnableQty(long returnableQty)
    {
        this.returnableQty = returnableQty;
    }


    public long getReturnableQty()
    {
        return this.returnableQty;
    }


    public void setCancelledItemsPrice(PriceData cancelledItemsPrice)
    {
        this.cancelledItemsPrice = cancelledItemsPrice;
    }


    public PriceData getCancelledItemsPrice()
    {
        return this.cancelledItemsPrice;
    }


    public void setReturnedItemsPrice(PriceData returnedItemsPrice)
    {
        this.returnedItemsPrice = returnedItemsPrice;
    }


    public PriceData getReturnedItemsPrice()
    {
        return this.returnedItemsPrice;
    }


    public void setSupportedActions(Set<String> supportedActions)
    {
        this.supportedActions = supportedActions;
    }


    public Set<String> getSupportedActions()
    {
        return this.supportedActions;
    }


    public void setOtherVariants(List<OrderEntryData> otherVariants)
    {
        this.otherVariants = otherVariants;
    }


    public List<OrderEntryData> getOtherVariants()
    {
        return this.otherVariants;
    }


    public void setContractPrice(String contractPrice)
    {
        this.contractPrice = contractPrice;
    }


    public String getContractPrice()
    {
        return this.contractPrice;
    }


    public void setFormattedContractPrice(String formattedContractPrice)
    {
        this.formattedContractPrice = formattedContractPrice;
    }


    public String getFormattedContractPrice()
    {
        return this.formattedContractPrice;
    }


    public void setFreightPrice(PriceData freightPrice)
    {
        this.freightPrice = freightPrice;
    }


    public PriceData getFreightPrice()
    {
        return this.freightPrice;
    }
}
