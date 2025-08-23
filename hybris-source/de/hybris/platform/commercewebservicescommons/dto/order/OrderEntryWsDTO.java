package de.hybris.platform.commercewebservicescommons.dto.order;

import de.hybris.platform.commercewebservicescommons.dto.comments.CommentWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.ProductWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.store.PointOfServiceWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "OrderEntry", description = "Representation of an Order entry")
public class OrderEntryWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "entryNumber", value = "Entry number of the order entry")
    private Integer entryNumber;
    @ApiModelProperty(name = "quantity", value = "Quantity number of items in order entry")
    private Long quantity;
    @ApiModelProperty(name = "basePrice", value = "Base price of order entry item")
    private PriceWsDTO basePrice;
    @ApiModelProperty(name = "totalPrice", value = "Total price of order entry item")
    private PriceWsDTO totalPrice;
    @ApiModelProperty(name = "product", value = "Product details of order entry")
    private ProductWsDTO product;
    @ApiModelProperty(name = "updateable", value = "Flag defining if order entry item is updateable")
    private Boolean updateable;
    @ApiModelProperty(name = "deliveryMode", value = "Delivery mode")
    private DeliveryModeWsDTO deliveryMode;
    @ApiModelProperty(name = "configurationInfos", value = "Configuration info of order entry")
    private List<ConfigurationInfoWsDTO> configurationInfos;
    @ApiModelProperty(name = "statusSummaryList", value = "List of aggregated status information per entry, relevant if the entry is configurable and its configuration contains one or many issues in different severities. Note that configurators typically raise such issues only in case the parent document is changeable. In this case the issues (depending on their severity) need to be fixed before a checkout can be done. This means this segment can be present for a cart entry, for order entries it will always be empty")
    private List<StatusSummaryWsDTO> statusSummaryList;
    @ApiModelProperty(name = "deliveryPointOfService", value = "Point of service associated with order entry")
    private PointOfServiceWsDTO deliveryPointOfService;
    @ApiModelProperty(name = "cancelledItemsPrice", value = "Total price of cancelled items which belong to the order entry item")
    private PriceWsDTO cancelledItemsPrice;
    @ApiModelProperty(name = "cancellableQuantity", value = "Quantity number of cancellable items in order entry", example = "5")
    private Long cancellableQuantity;
    @ApiModelProperty(name = "returnedItemsPrice", value = "Total price of returned items which belong to the order entry item")
    private PriceWsDTO returnedItemsPrice;
    @ApiModelProperty(name = "returnableQuantity", value = "Quantity number of returnable items in order entry", example = "5")
    private Long returnableQuantity;
    @ApiModelProperty(name = "comments", value = "List of order entry comments.")
    private List<CommentWsDTO> comments;
    @ApiModelProperty(name = "entries")
    private List<OrderEntryWsDTO> entries;
    @ApiModelProperty(name = "otherVariants")
    private List<OrderEntryWsDTO> otherVariants;
    @ApiModelProperty(name = "contractPrice")
    private String contractPrice;
    @ApiModelProperty(name = "formattedContractPrice")
    private String formattedContractPrice;
    @ApiModelProperty(name = "freightPrice")
    private PriceWsDTO freightPrice;


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


    public void setBasePrice(PriceWsDTO basePrice)
    {
        this.basePrice = basePrice;
    }


    public PriceWsDTO getBasePrice()
    {
        return this.basePrice;
    }


    public void setTotalPrice(PriceWsDTO totalPrice)
    {
        this.totalPrice = totalPrice;
    }


    public PriceWsDTO getTotalPrice()
    {
        return this.totalPrice;
    }


    public void setProduct(ProductWsDTO product)
    {
        this.product = product;
    }


    public ProductWsDTO getProduct()
    {
        return this.product;
    }


    public void setUpdateable(Boolean updateable)
    {
        this.updateable = updateable;
    }


    public Boolean getUpdateable()
    {
        return this.updateable;
    }


    public void setDeliveryMode(DeliveryModeWsDTO deliveryMode)
    {
        this.deliveryMode = deliveryMode;
    }


    public DeliveryModeWsDTO getDeliveryMode()
    {
        return this.deliveryMode;
    }


    public void setConfigurationInfos(List<ConfigurationInfoWsDTO> configurationInfos)
    {
        this.configurationInfos = configurationInfos;
    }


    public List<ConfigurationInfoWsDTO> getConfigurationInfos()
    {
        return this.configurationInfos;
    }


    public void setStatusSummaryList(List<StatusSummaryWsDTO> statusSummaryList)
    {
        this.statusSummaryList = statusSummaryList;
    }


    public List<StatusSummaryWsDTO> getStatusSummaryList()
    {
        return this.statusSummaryList;
    }


    public void setDeliveryPointOfService(PointOfServiceWsDTO deliveryPointOfService)
    {
        this.deliveryPointOfService = deliveryPointOfService;
    }


    public PointOfServiceWsDTO getDeliveryPointOfService()
    {
        return this.deliveryPointOfService;
    }


    public void setCancelledItemsPrice(PriceWsDTO cancelledItemsPrice)
    {
        this.cancelledItemsPrice = cancelledItemsPrice;
    }


    public PriceWsDTO getCancelledItemsPrice()
    {
        return this.cancelledItemsPrice;
    }


    public void setCancellableQuantity(Long cancellableQuantity)
    {
        this.cancellableQuantity = cancellableQuantity;
    }


    public Long getCancellableQuantity()
    {
        return this.cancellableQuantity;
    }


    public void setReturnedItemsPrice(PriceWsDTO returnedItemsPrice)
    {
        this.returnedItemsPrice = returnedItemsPrice;
    }


    public PriceWsDTO getReturnedItemsPrice()
    {
        return this.returnedItemsPrice;
    }


    public void setReturnableQuantity(Long returnableQuantity)
    {
        this.returnableQuantity = returnableQuantity;
    }


    public Long getReturnableQuantity()
    {
        return this.returnableQuantity;
    }


    public void setComments(List<CommentWsDTO> comments)
    {
        this.comments = comments;
    }


    public List<CommentWsDTO> getComments()
    {
        return this.comments;
    }


    public void setEntries(List<OrderEntryWsDTO> entries)
    {
        this.entries = entries;
    }


    public List<OrderEntryWsDTO> getEntries()
    {
        return this.entries;
    }


    public void setOtherVariants(List<OrderEntryWsDTO> otherVariants)
    {
        this.otherVariants = otherVariants;
    }


    public List<OrderEntryWsDTO> getOtherVariants()
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


    public void setFreightPrice(PriceWsDTO freightPrice)
    {
        this.freightPrice = freightPrice;
    }


    public PriceWsDTO getFreightPrice()
    {
        return this.freightPrice;
    }
}
