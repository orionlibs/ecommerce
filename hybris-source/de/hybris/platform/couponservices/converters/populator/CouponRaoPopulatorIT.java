package de.hybris.platform.couponservices.converters.populator;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.couponservices.AbstractCouponAwareCartIT;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import javax.annotation.Resource;
import org.fest.assertions.Assertions;
import org.junit.Test;

@IntegrationTest
public class CouponRaoPopulatorIT extends AbstractCouponAwareCartIT
{
    @Resource
    private CouponRaoPopulator couponRaoPopulator;


    @Test
    public void testPopulateOk() throws ConversionException
    {
        CartRAO cartRao = new CartRAO();
        this.couponRaoPopulator.populate((AbstractOrderModel)getCartTestContextBuilder().getCartModel(), cartRao);
        Assertions.assertThat(cartRao.getCoupons()).hasSize(1);
        Assertions.assertThat(cartRao.getCoupons().get(0)).isEqualTo(getExpectedCouponRAO());
    }
}
