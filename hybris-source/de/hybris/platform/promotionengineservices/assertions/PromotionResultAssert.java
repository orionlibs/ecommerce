package de.hybris.platform.promotionengineservices.assertions;

import de.hybris.platform.promotionengineservices.promotionengine.report.data.PromotionEngineResult;
import java.util.Objects;
import org.assertj.core.api.AbstractAssert;

public class PromotionResultAssert extends AbstractAssert<PromotionResultAssert, PromotionEngineResult>
{
    public PromotionResultAssert(PromotionEngineResult actual)
    {
        super(actual, PromotionResultAssert.class);
    }


    public static PromotionResultAssert assertThat(PromotionEngineResult actual)
    {
        return new PromotionResultAssert(actual);
    }


    public PromotionResultAssert hasCode(String code)
    {
        isNotNull();
        if(!Objects.equals(((PromotionEngineResult)this.actual).getCode(), code))
        {
            failWithMessage("Expected character's code to be <%s> but was <%s>", new Object[] {code, ((PromotionEngineResult)this.actual).getCode()});
        }
        return this;
    }


    public PromotionResultAssert hasName(String name)
    {
        isNotNull();
        if(!Objects.equals(((PromotionEngineResult)this.actual).getName(), name))
        {
            failWithMessage("Expected character's name to be <%s> but was <%s>", new Object[] {name, ((PromotionEngineResult)this.actual).getName()});
        }
        return this;
    }


    public PromotionResultAssert hasDescription(String description)
    {
        isNotNull();
        if(!Objects.equals(((PromotionEngineResult)this.actual).getDescription(), description))
        {
            failWithMessage("Expected character's description to be <%s> but was <%s>", new Object[] {description, ((PromotionEngineResult)this.actual).getDescription()});
        }
        return this;
    }


    public PromotionResultAssert isFiredPromotion()
    {
        isNotNull();
        if(!Objects.equals(Boolean.valueOf(((PromotionEngineResult)this.actual).isFired()), Boolean.TRUE))
        {
            failWithMessage("Expected isFiredPromotion promotion to be <%s> but was <%s>", new Object[] {Boolean.TRUE,
                            Boolean.valueOf(((PromotionEngineResult)this.actual).isFired())});
        }
        return this;
    }


    public PromotionResultAssert isPotentialPromotion()
    {
        isNotNull();
        if(!Objects.equals(Boolean.valueOf(((PromotionEngineResult)this.actual).isFired()), Boolean.FALSE))
        {
            failWithMessage("Expected isFiredPromotion promotion to be <%s> but was <%s>", new Object[] {Boolean.FALSE,
                            Boolean.valueOf(((PromotionEngineResult)this.actual).isFired())});
        }
        return this;
    }
}
