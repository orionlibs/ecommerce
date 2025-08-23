package de.hybris.platform.commercewebservicescommons.dto.quote;

import de.hybris.platform.commercewebservicescommons.dto.comments.CommentWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.order.OrderEntryWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@ApiModel(value = "Quote", description = "Representation of the quote object.")
public class QuoteWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "expirationTime", value = "Expiration time of the quote.", required = true, example = "yyyy-MM-ddTHH:mm:ss+0000")
    private Date expirationTime;
    @ApiModelProperty(name = "code", value = "Code of the quote.", required = true, example = "0003005")
    private String code;
    @ApiModelProperty(name = "name", value = "Name of the quote.", required = true, example = "Quote 0003005")
    private String name;
    @ApiModelProperty(name = "state", value = "Current state of the quote. Possible state values - DRAFT, SUBMITTED, OFFER, CANCELLED, EXPIRED, etc.. The list of the states can be extended.", required = true, example = "CANCELLED")
    private String state;
    @ApiModelProperty(name = "description", value = "Description of the quote.", required = false, example = "Quote description")
    private String description;
    @ApiModelProperty(name = "version", value = "Current version of the quote.", required = true, example = "1")
    private Integer version;
    @ApiModelProperty(name = "threshold", value = "Minimum subtotal value for the quote in the currency of the store.", required = true, example = "25000")
    private Double threshold;
    @ApiModelProperty(name = "cartId", value = "Id of the cart, which is linked to the quote.", required = false, example = "000350")
    private String cartId;
    @ApiModelProperty(name = "creationTime", value = "Date of quote creation.", required = true, example = "yyyy-MM-dd HH:mm:ss+0000")
    private Date creationTime;
    @ApiModelProperty(name = "updatedTime", value = "Date of the last quote update.", required = true, example = "yyyy-MM-dd HH:mm:ss+0000")
    private Date updatedTime;
    @ApiModelProperty(name = "allowedActions", value = "Actions, which are allowed to perform with the quote.")
    private List<String> allowedActions;
    @ApiModelProperty(name = "previousEstimatedTotal", value = "Previously estimated total price of the quote.")
    private PriceWsDTO previousEstimatedTotal;
    @ApiModelProperty(name = "comments", value = "List of quote comments.")
    private List<CommentWsDTO> comments;
    @ApiModelProperty(name = "totalPriceWithTax", value = "Total price of the cart with taxes.")
    private PriceWsDTO totalPriceWithTax;
    @ApiModelProperty(name = "totalPrice", value = "Total price of the cart.")
    private PriceWsDTO totalPrice;
    @ApiModelProperty(name = "entries", value = "Entries of the cart.")
    private List<OrderEntryWsDTO> entries;
    @ApiModelProperty(name = "totalItems", value = "Total number of the items in the quote.", required = true, example = "2")
    private Integer totalItems;
    @ApiModelProperty(name = "quoteDiscounts", value = "Discounts available for the current quote.")
    private PriceWsDTO quoteDiscounts;
    @ApiModelProperty(name = "orderDiscounts", value = "Discounts available for the current order.")
    private PriceWsDTO orderDiscounts;
    @ApiModelProperty(name = "subTotalWithDiscounts", value = "Subtotal of the quote with applied discount.")
    private PriceWsDTO subTotalWithDiscounts;
    @ApiModelProperty(name = "productDiscounts", value = "Discount applied to the product.")
    private PriceWsDTO productDiscounts;


    public void setExpirationTime(Date expirationTime)
    {
        this.expirationTime = expirationTime;
    }


    public Date getExpirationTime()
    {
        return this.expirationTime;
    }


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


    public void setState(String state)
    {
        this.state = state;
    }


    public String getState()
    {
        return this.state;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setVersion(Integer version)
    {
        this.version = version;
    }


    public Integer getVersion()
    {
        return this.version;
    }


    public void setThreshold(Double threshold)
    {
        this.threshold = threshold;
    }


    public Double getThreshold()
    {
        return this.threshold;
    }


    public void setCartId(String cartId)
    {
        this.cartId = cartId;
    }


    public String getCartId()
    {
        return this.cartId;
    }


    public void setCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }


    public Date getCreationTime()
    {
        return this.creationTime;
    }


    public void setUpdatedTime(Date updatedTime)
    {
        this.updatedTime = updatedTime;
    }


    public Date getUpdatedTime()
    {
        return this.updatedTime;
    }


    public void setAllowedActions(List<String> allowedActions)
    {
        this.allowedActions = allowedActions;
    }


    public List<String> getAllowedActions()
    {
        return this.allowedActions;
    }


    public void setPreviousEstimatedTotal(PriceWsDTO previousEstimatedTotal)
    {
        this.previousEstimatedTotal = previousEstimatedTotal;
    }


    public PriceWsDTO getPreviousEstimatedTotal()
    {
        return this.previousEstimatedTotal;
    }


    public void setComments(List<CommentWsDTO> comments)
    {
        this.comments = comments;
    }


    public List<CommentWsDTO> getComments()
    {
        return this.comments;
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


    public void setEntries(List<OrderEntryWsDTO> entries)
    {
        this.entries = entries;
    }


    public List<OrderEntryWsDTO> getEntries()
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


    public void setQuoteDiscounts(PriceWsDTO quoteDiscounts)
    {
        this.quoteDiscounts = quoteDiscounts;
    }


    public PriceWsDTO getQuoteDiscounts()
    {
        return this.quoteDiscounts;
    }


    public void setOrderDiscounts(PriceWsDTO orderDiscounts)
    {
        this.orderDiscounts = orderDiscounts;
    }


    public PriceWsDTO getOrderDiscounts()
    {
        return this.orderDiscounts;
    }


    public void setSubTotalWithDiscounts(PriceWsDTO subTotalWithDiscounts)
    {
        this.subTotalWithDiscounts = subTotalWithDiscounts;
    }


    public PriceWsDTO getSubTotalWithDiscounts()
    {
        return this.subTotalWithDiscounts;
    }


    public void setProductDiscounts(PriceWsDTO productDiscounts)
    {
        this.productDiscounts = productDiscounts;
    }


    public PriceWsDTO getProductDiscounts()
    {
        return this.productDiscounts;
    }
}
