package de.hybris.platform.commercefacades.product.data;

import de.hybris.platform.commercefacades.promotion.data.PromotionRestrictionData;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class PromotionData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String title;
    private String promotionType;
    private Date startDate;
    private Date endDate;
    private String description;
    private List<String> couldFireMessages;
    private List<String> firedMessages;
    private ImageData productBanner;
    private Boolean enabled;
    private Integer priority;
    private String promotionGroup;
    private Collection<PromotionRestrictionData> restrictions;


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


    public void setProductBanner(ImageData productBanner)
    {
        this.productBanner = productBanner;
    }


    public ImageData getProductBanner()
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


    public void setRestrictions(Collection<PromotionRestrictionData> restrictions)
    {
        this.restrictions = restrictions;
    }


    public Collection<PromotionRestrictionData> getRestrictions()
    {
        return this.restrictions;
    }
}
