package de.hybris.platform.promotionengineservices.promotionengine.report.data;

import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.util.DiscountValue;
import java.io.Serializable;

public class PromotionEngineResult implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private String description;
    private DiscountValue discountValue;
    private PromotionResultModel promotionResult;
    private boolean fired;


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


    public void setDiscountValue(DiscountValue discountValue)
    {
        this.discountValue = discountValue;
    }


    public DiscountValue getDiscountValue()
    {
        return this.discountValue;
    }


    public void setPromotionResult(PromotionResultModel promotionResult)
    {
        this.promotionResult = promotionResult;
    }


    public PromotionResultModel getPromotionResult()
    {
        return this.promotionResult;
    }


    public void setFired(boolean fired)
    {
        this.fired = fired;
    }


    public boolean isFired()
    {
        return this.fired;
    }
}
