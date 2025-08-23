package de.hybris.platform.couponservices.rao.providers.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.couponservices.AbstractCouponAwareCartIT;
import de.hybris.platform.couponservices.util.CouponAwareCartTestContextBuilder;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.providers.impl.AbstractExpandedRAOProvider;
import java.util.Collections;
import java.util.Set;
import javax.annotation.Resource;
import org.fest.assertions.Assertions;
import org.fest.assertions.CollectionAssert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@IntegrationTest
public class CouponAwareCartRaoProviderIT extends AbstractCouponAwareCartIT
{
    private static final Logger logger = LoggerFactory.getLogger(CouponAwareCartRaoProviderIT.class);
    @Resource
    private AbstractExpandedRAOProvider cartRaoProvider;


    @Test
    public void testExpandFactModelExpandWithCoupons()
    {
        logger.debug("Provider class: {}", this.cartRaoProvider.getClass());
        Set<?> facts = this.cartRaoProvider.expandFactModel(getCartTestContextBuilder().getCartModel(),
                        Collections.singletonList("EXPAND_COUPONS"));
        CouponAwareCartTestContextBuilder couponAwareCartTestContextBuilder = getCartTestContextBuilder();
        CartRAO cartRAO = couponAwareCartTestContextBuilder.getCartRAO();
        cartRAO.setCode(couponAwareCartTestContextBuilder.getCartModel().getCode());
        ((CollectionAssert)Assertions.assertThat(facts).isNotEmpty()).containsOnly(new Object[] {cartRAO, getExpectedCouponRAO()});
    }


    @Test
    public void testExpandFactModelExpand()
    {
        Set<?> facts = this.cartRaoProvider.expandFactModel(getCartTestContextBuilder().getCartModel());
        CouponAwareCartTestContextBuilder couponAwareCartTestContextBuilder = getCartTestContextBuilder();
        CartRAO cartRAO = couponAwareCartTestContextBuilder.getCartRAO();
        cartRAO.setCode(couponAwareCartTestContextBuilder.getCartModel().getCode());
        ((CollectionAssert)Assertions.assertThat(facts).isNotEmpty()).containsOnly(new Object[] {cartRAO, getExpectedCouponRAO()});
    }
}
