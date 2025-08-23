package de.hybris.platform.couponservices.converters;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.couponservices.AbstractCouponAwareCartIT;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import javax.annotation.Resource;
import org.fest.assertions.Assertions;
import org.fest.assertions.ListAssert;
import org.junit.Test;

@IntegrationTest
public class CouponAwareCartRAOConverterIT extends AbstractCouponAwareCartIT
{
    @Resource
    private AbstractPopulatingConverter<CartModel, CartRAO> cartRaoConverter;


    @Test
    public void testConvertOk()
    {
        CartRAO cartRao = (CartRAO)this.cartRaoConverter.convert(getCartTestContextBuilder().getCartModel());
        ((ListAssert)Assertions.assertThat(cartRao.getCoupons()).isNotEmpty()).hasSize(1);
        Assertions.assertThat(cartRao.getCoupons().get(0)).isEqualTo(getExpectedCouponRAO());
    }
}
