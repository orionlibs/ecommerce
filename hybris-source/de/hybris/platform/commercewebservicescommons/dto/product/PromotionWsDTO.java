package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@ApiModel(value = "Promotion", description = "Representation of a Promotion")
public class PromotionWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Code of the promotion")
    private String code;
    @ApiModelProperty(name = "title", value = "Promotion title")
    private String title;
    @ApiModelProperty(name = "promotionType", value = "Type of the promotion")
    private String promotionType;
    @ApiModelProperty(name = "startDate", value = "The initial date of the promotion")
    private Date startDate;
    @ApiModelProperty(name = "endDate", value = "Last date of validity of the promotion")
    private Date endDate;
    @ApiModelProperty(name = "description", value = "Description of the promotion")
    private String description;
    @ApiModelProperty(name = "couldFireMessages", value = "Message about promotion which is displayed when planning potential promotion. This field has higher priority over promotion description")
    private List<String> couldFireMessages;
    @ApiModelProperty(name = "firedMessages", value = "Message fired while the promotion is active. This is info how much you will get when applying the promotion")
    private List<String> firedMessages;
    @ApiModelProperty(name = "productBanner", value = "Image banner of the promotion")
    private ImageWsDTO productBanner;
    @ApiModelProperty(name = "enabled", value = "Boolean flag if promotion is enabled")
    private Boolean enabled;
    @ApiModelProperty(name = "priority", value = "Priority index as numeric value of the promotion. Higher number means higher priority")
    private Integer priority;
    @ApiModelProperty(name = "promotionGroup", value = "Group of the promotion")
    private String promotionGroup;
    @ApiModelProperty(name = "restrictions", value = "List of promotion restrictions")
    private Collection<PromotionRestrictionWsDTO> restrictions;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setTitle(String title)
    {
        this.title = title;
    }


    public String getTitle()
    {
        return this.title;
    }


    public void setPromotionType(String promotionType)
    {
        this.promotionType = promotionType;
    }


    public String getPromotionType()
    {
        return this.promotionType;
    }


    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }


    public Date getStartDate()
    {
        return this.startDate;
    }


    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }


    public Date getEndDate()
    {
        return this.endDate;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setCouldFireMessages(List<String> couldFireMessages)
    {
        this.couldFireMessages = couldFireMessages;
    }


    public List<String> getCouldFireMessages()
    {
        return this.couldFireMessages;
    }


    public void setFiredMessages(List<String> firedMessages)
    {
        this.firedMessages = firedMessages;
    }


    public List<String> getFiredMessages()
    {
        return this.firedMessages;
    }


    public void setProductBanner(ImageWsDTO productBanner)
    {
        this.productBanner = productBanner;
    }


    public ImageWsDTO getProductBanner()
    {
        return this.productBanner;
    }


    public void setEnabled(Boolean enabled)
    {
        this.enabled = enabled;
    }


    public Boolean getEnabled()
    {
        return this.enabled;
    }


    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }


    public Integer getPriority()
    {
        return this.priority;
    }


    public void setPromotionGroup(String promotionGroup)
    {
        this.promotionGroup = promotionGroup;
    }


    public String getPromotionGroup()
    {
        return this.promotionGroup;
    }


    public void setRestrictions(Collection<PromotionRestrictionWsDTO> restrictions)
    {
        this.restrictions = restrictions;
    }


    public Collection<PromotionRestrictionWsDTO> getRestrictions()
    {
        return this.restrictions;
    }
}
