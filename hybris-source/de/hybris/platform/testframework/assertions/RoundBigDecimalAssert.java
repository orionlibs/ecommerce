package de.hybris.platform.testframework.assertions;

import java.math.BigDecimal;
import java.math.RoundingMode;
import org.fest.assertions.Assertions;
import org.fest.assertions.GenericAssert;

@Deprecated(since = "2011", forRemoval = true)
public class RoundBigDecimalAssert extends GenericAssert<RoundBigDecimalAssert, BigDecimal>
{
    private int digits;
    private boolean scaling;


    public RoundBigDecimalAssert(BigDecimal actual)
    {
        super(RoundBigDecimalAssert.class, actual);
    }


    public static RoundBigDecimalAssert assertThat(BigDecimal actual)
    {
        return new RoundBigDecimalAssert(actual);
    }


    public RoundBigDecimalAssert scalingTo(int digits)
    {
        this.scaling = true;
        this.digits = digits;
        Assertions.assertThat(digits).isGreaterThanOrEqualTo(0);
        return this;
    }


    public RoundBigDecimalAssert isEqualTo(BigDecimal other)
    {
        Assertions.assertThat(this.scaling ? ((BigDecimal)this.actual).setScale(this.digits, RoundingMode.FLOOR) : (BigDecimal)this.actual).isEqualTo(other);
        return this;
    }
}
