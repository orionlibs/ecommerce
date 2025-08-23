package de.hybris.platform.commercewebservicescommons.dto.order;

import com.olympus.oca.commerce.dto.order.HeavyOrderQuestionsCartWsDTO;
import de.hybris.platform.b2bacceleratorfacades.order.data.B2BPaymentTypeData;
import de.hybris.platform.b2bwebservicescommons.dto.company.B2BCostCenterWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.product.PromotionResultWsDTO;
import de.hybris.platform.commercewebservicescommons.dto.user.PrincipalWsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import java.util.List;

@ApiModel(value = "Cart", description = "Representation of a Cart")
public class CartWsDTO extends AbstractOrderWsDTO
{
    @ApiModelProperty(name = "totalUnitCount", value = "Total unit count")
    private Integer totalUnitCount;
    @ApiModelProperty(name = "potentialOrderPromotions", value = "List of potential order promotions for cart")
    private List<PromotionResultWsDTO> potentialOrderPromotions;
    @ApiModelProperty(name = "potentialProductPromotions", value = "List of potential product promotions for cart")
    private List<PromotionResultWsDTO> potentialProductPromotions;
    @ApiModelProperty(name = "name", value = "Name of the cart")
    private String name;
    @ApiModelProperty(name = "description", value = "Description of the cart")
    private String description;
    @ApiModelProperty(name = "expirationTime", value = "Date of cart expiration time")
    private Date expirationTime;
    @ApiModelProperty(name = "saveTime", value = "Date of saving cart")
    private Date saveTime;
    @ApiModelProperty(name = "savedBy", value = "Information about person who saved cart")
    private PrincipalWsDTO savedBy;
    @ApiModelProperty(name = "costCenter")
    private B2BCostCenterWsDTO costCenter;
    @ApiModelProperty(name = "paymentType")
    private B2BPaymentTypeData paymentType;
    @ApiModelProperty(name = "purchaseOrderNumber")
    private String purchaseOrderNumber;
    @ApiModelProperty(name = "heavyOrder", value = "Identifier to distinguish if the cart is a heavy order")
    private String heavyOrder;
    @ApiModelProperty(name = "heavyOrderQuestions")
    private HeavyOrderQuestionsCartWsDTO heavyOrderQuestions;
    @ApiModelProperty(name = "shipByGround", value = "Identifier to distinguish if the cart has products that to have to be shipped by ground")
    private String shipByGround;


    public void setTotalUnitCount(Integer totalUnitCount)
    {
        this.totalUnitCount = totalUnitCount;
    }


    public Integer getTotalUnitCount()
    {
        return this.totalUnitCount;
    }


    public void setPotentialOrderPromotions(List<PromotionResultWsDTO> potentialOrderPromotions)
    {
        this.potentialOrderPromotions = potentialOrderPromotions;
    }


    public List<PromotionResultWsDTO> getPotentialOrderPromotions()
    {
        return this.potentialOrderPromotions;
    }


    public void setPotentialProductPromotions(List<PromotionResultWsDTO> potentialProductPromotions)
    {
        this.potentialProductPromotions = potentialProductPromotions;
    }


    public List<PromotionResultWsDTO> getPotentialProductPromotions()
    {
        return this.potentialProductPromotions;
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


    public void setSaveTime(Date saveTime)
    {
        this.saveTime = saveTime;
    }


    public Date getSaveTime()
    {
        return this.saveTime;
    }


    public void setSavedBy(PrincipalWsDTO savedBy)
    {
        this.savedBy = savedBy;
    }


    public PrincipalWsDTO getSavedBy()
    {
        return this.savedBy;
    }


    public void setCostCenter(B2BCostCenterWsDTO costCenter)
    {
        this.costCenter = costCenter;
    }


    public B2BCostCenterWsDTO getCostCenter()
    {
        return this.costCenter;
    }


    public void setPaymentType(B2BPaymentTypeData paymentType)
    {
        this.paymentType = paymentType;
    }


    public B2BPaymentTypeData getPaymentType()
    {
        return this.paymentType;
    }


    public void setPurchaseOrderNumber(String purchaseOrderNumber)
    {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }


    public String getPurchaseOrderNumber()
    {
        return this.purchaseOrderNumber;
    }


    public void setHeavyOrder(String heavyOrder)
    {
        this.heavyOrder = heavyOrder;
    }


    public String getHeavyOrder()
    {
        return this.heavyOrder;
    }


    public void setHeavyOrderQuestions(HeavyOrderQuestionsCartWsDTO heavyOrderQuestions)
    {
        this.heavyOrderQuestions = heavyOrderQuestions;
    }


    public HeavyOrderQuestionsCartWsDTO getHeavyOrderQuestions()
    {
        return this.heavyOrderQuestions;
    }


    public void setShipByGround(String shipByGround)
    {
        this.shipByGround = shipByGround;
    }


    public String getShipByGround()
    {
        return this.shipByGround;
    }
}
