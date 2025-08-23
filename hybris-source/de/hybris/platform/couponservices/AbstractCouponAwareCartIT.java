package de.hybris.platform.couponservices;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.couponservices.rao.CouponRAO;
import de.hybris.platform.couponservices.util.CouponAwareCartTestContextBuilder;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.ServicelayerTest;
import java.util.Collections;
import org.junit.Before;

@IntegrationTest
public class AbstractCouponAwareCartIT extends ServicelayerTest
{
    protected static final String COUPON_CODE = "testCouponCode";
    protected static final String COUPON_ID = "testCouponId";
    private CouponRAO expectedCouponRAO;
    private CouponAwareCartTestContextBuilder contextBuilder;


    @Before
    public void setUp() throws ImpExException
    {
        importCsv("/couponservices/test/DefaultCouponForProServiceTest.impex", "utf-8");
        importCsv("/couponservices/test/couponAwareTest.impex", "utf-8");
        this.contextBuilder = new CouponAwareCartTestContextBuilder();
        this.contextBuilder = (CouponAwareCartTestContextBuilder)this.contextBuilder.withCouponCodes(Collections.singleton("testCouponCode"));
        this.expectedCouponRAO = new CouponRAO();
        this.expectedCouponRAO.setCouponCode("testCouponCode");
        this.expectedCouponRAO.setCouponId("testCouponCode");
    }


    protected CouponRAO getExpectedCouponRAO()
    {
        return this.expectedCouponRAO;
    }


    protected CouponAwareCartTestContextBuilder getCartTestContextBuilder()
    {
        return this.contextBuilder;
    }
}
