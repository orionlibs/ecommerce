package de.hybris.platform.couponservices.spocktests;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.CartModel;
import groovy.lang.GroovyObject;
import groovy.lang.Reference;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.junit.Test;
import org.spockframework.mock.runtime.MockController;
import org.spockframework.runtime.ErrorCollector;
import org.spockframework.runtime.ErrorRethrower;
import org.spockframework.runtime.SpecificationContext;
import org.spockframework.runtime.SpockRuntime;
import org.spockframework.runtime.ValueRecorder;
import org.spockframework.runtime.model.BlockKind;
import org.spockframework.runtime.model.BlockMetadata;
import org.spockframework.runtime.model.FeatureMetadata;
import org.spockframework.runtime.model.SpecMetadata;
import spock.lang.Unroll;

@IntegrationTest
@SpecMetadata(filename = "CouponPromotionRulesSpec.groovy", line = 21)
public class CouponPromotionRulesSpec extends AbstractCouponServicesSpockTest
{
    @Test
    @Unroll
    @FeatureMetadata(line = 24, name = "#testId : Adding #productsToAddToCart to Cart(#forCurrency) and redemption of #couponCodes should amount to #promotedAmount with #promotionRules promotion and #baseAmount without it. The coupon should be used - #couponShouldBeUsed", ordinal = 0, blocks = {
                    @BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"testId", "productsToAddToCart", "promotionRules",
                    "couponCodes", "couponShouldBeUsed", "promotedAmount", "baseAmount", "forCurrency"})
    public void $spock_feature_6_0(Object testId, Object productsToAddToCart, Object promotionRules, Object couponCodes, Object couponShouldBeUsed, Object promotedAmount, Object baseAmount, Object forCurrency)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[0].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[1].callConstructor(ValueRecorder.class), ValueRecorder.class);
        arrayOfCallSite[2].callCurrent((GroovyObject)this, promotionRules, "promotions-module-junit");
        Reference cart1 = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[3].callCurrent((GroovyObject)this, "cart1", forCurrency), CartModel.class));
        arrayOfCallSite[4].call(productsToAddToCart, new __spock_feature_6_0_closure1(this, this, cart1));
        double cart1TotalPriceWithoutPromo = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[5].callGetProperty(cart1.get()));
        arrayOfCallSite[6].call(couponCodes, new __spock_feature_6_0_closure2(this, this, cart1));
        arrayOfCallSite[7].callCurrent((GroovyObject)this, arrayOfCallSite[8].call(cart1.get()));
        double cart1TotalPriceWithRedeemedCoupons = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[9].callGetProperty(cart1.get()));
        boolean couponIsUsed = DefaultTypeTransformation.booleanUnbox(DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[10].callGetProperty(couponCodes)) ? Boolean.valueOf(false) : arrayOfCallSite[11].call(couponCodes, new __spock_feature_6_0_closure3(this, this, cart1)));
        arrayOfCallSite[12].call(couponCodes, new __spock_feature_6_0_closure4(this, this, cart1));
        arrayOfCallSite[13].callCurrent((GroovyObject)this, arrayOfCallSite[14].call(cart1.get()));
        double cart1TotalPriceAfterReleasedCoupons = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[15].callGetProperty(cart1.get()));
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart1TotalPriceWithoutPromo == baseAmount", Integer.valueOf(54).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart1TotalPriceWithoutPromo)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart1TotalPriceWithoutPromo == baseAmount", Integer.valueOf(54).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart1TotalPriceWithRedeemedCoupons == promotedAmount", Integer.valueOf(55).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart1TotalPriceWithRedeemedCoupons)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), promotedAmount)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart1TotalPriceWithRedeemedCoupons == promotedAmount", Integer.valueOf(55).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart1TotalPriceAfterReleasedCoupons == baseAmount", Integer.valueOf(56).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart1TotalPriceAfterReleasedCoupons)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart1TotalPriceAfterReleasedCoupons == baseAmount", Integer.valueOf(56).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "couponIsUsed == couponShouldBeUsed", Integer.valueOf(57).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Boolean.valueOf(couponIsUsed)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), couponShouldBeUsed)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "couponIsUsed == couponShouldBeUsed", Integer.valueOf(57).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @Unroll
    @FeatureMetadata(line = 88, name = "#testId : Adding #productsToAddToCart to 2 Carts(#forCurrency) and redemption of #couponCodes for both with #promotionRules promotion should amount to #promotedAmount1 for cart1, #promotedAmount2 for cart2 and #baseAmount1, #baseAmount2 without it", ordinal = 1, blocks = {
                    @BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"testId", "productsToAddToCart", "promotionRules",
                    "couponCodes", "promotedAmount1", "baseAmount1", "promotedAmount2", "baseAmount2", "forCurrency"})
    public void $spock_feature_6_1(Object testId, Object productsToAddToCart, Object promotionRules, Object couponCodes, Object promotedAmount1, Object baseAmount1, Object promotedAmount2, Object baseAmount2, Object forCurrency)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[16].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[17].callConstructor(ValueRecorder.class), ValueRecorder.class);
        arrayOfCallSite[18].callCurrent((GroovyObject)this, promotionRules, "promotions-module-junit");
        Reference cart1 = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[19].callCurrent((GroovyObject)this, "cart1", forCurrency), CartModel.class));
        Reference cart2 = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[20].callCurrent((GroovyObject)this, "cart2", forCurrency), CartModel.class));
        arrayOfCallSite[21].call(productsToAddToCart, new __spock_feature_6_1_closure5(this, this, cart1, cart2));
        double cart1TotalPriceWithoutPromo = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[22].callGetProperty(cart1.get()));
        arrayOfCallSite[23].call(couponCodes, new __spock_feature_6_1_closure6(this, this, cart1));
        arrayOfCallSite[24].callCurrent((GroovyObject)this, arrayOfCallSite[25].call(cart1.get()));
        double cart1TotalPriceWithRedeemedCoupons = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[26].callGetProperty(cart1.get()));
        double cart2TotalPriceWithoutPromo = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[27].callGetProperty(cart2.get()));
        arrayOfCallSite[28].call(couponCodes, new __spock_feature_6_1_closure7(this, this, cart2));
        arrayOfCallSite[29].callCurrent((GroovyObject)this, arrayOfCallSite[30].call(cart2.get()));
        double cart2TotalPriceWithRedeemedCoupons = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[31].callGetProperty(cart2.get()));
        arrayOfCallSite[32].call(couponCodes, new __spock_feature_6_1_closure8(this, this, cart1));
        arrayOfCallSite[33].callCurrent((GroovyObject)this, arrayOfCallSite[34].call(cart1.get()));
        double cart1TotalPriceAfterReleasedCoupons = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[35].callGetProperty(cart1.get()));
        arrayOfCallSite[36].call(couponCodes, new __spock_feature_6_1_closure9(this, this, cart2));
        arrayOfCallSite[37].callCurrent((GroovyObject)this, arrayOfCallSite[38].call(cart2.get()));
        double cart2TotalPriceAfterReleasedCoupons = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[39].callGetProperty(cart2.get()));
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart1TotalPriceWithoutPromo == baseAmount1", Integer.valueOf(130).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart1TotalPriceWithoutPromo)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount1)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart1TotalPriceWithoutPromo == baseAmount1", Integer.valueOf(130).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart1TotalPriceWithRedeemedCoupons == promotedAmount1", Integer.valueOf(131).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart1TotalPriceWithRedeemedCoupons)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), promotedAmount1)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart1TotalPriceWithRedeemedCoupons == promotedAmount1", Integer.valueOf(131).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart1TotalPriceAfterReleasedCoupons == baseAmount1", Integer.valueOf(132).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart1TotalPriceAfterReleasedCoupons)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount1)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart1TotalPriceAfterReleasedCoupons == baseAmount1", Integer.valueOf(132).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart2TotalPriceWithoutPromo == baseAmount2", Integer.valueOf(133).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart2TotalPriceWithoutPromo)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount2)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart2TotalPriceWithoutPromo == baseAmount2", Integer.valueOf(133).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart2TotalPriceWithRedeemedCoupons == promotedAmount2", Integer.valueOf(134).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart2TotalPriceWithRedeemedCoupons)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), promotedAmount2)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart2TotalPriceWithRedeemedCoupons == promotedAmount2", Integer.valueOf(134).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart2TotalPriceAfterReleasedCoupons == baseAmount2", Integer.valueOf(135).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart2TotalPriceAfterReleasedCoupons)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount2)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart2TotalPriceAfterReleasedCoupons == baseAmount2", Integer.valueOf(135).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @Unroll
    @FeatureMetadata(line = 144, name = "#testId : Adding #productsToAddToCart to 2 Carts(#forCurrency) and redemption of #couponCodes for both with #promotionRules promotion should give free gifts #gifts1 for cart1, #gifts2 for cart2 and have the same prices #baseAmount1 for cart1, #baseAmount2 for cart2", ordinal = 2, blocks = {
                    @BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"testId", "productsToAddToCart", "promotionRules",
                    "couponCodes", "gifts1", "baseAmount1", "gifts2", "baseAmount2", "forCurrency"})
    public void $spock_feature_6_2(Object testId, Object productsToAddToCart, Object promotionRules, Object couponCodes, Object gifts1, Object baseAmount1, Object gifts2, Object baseAmount2, Object forCurrency)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[40].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[41].callConstructor(ValueRecorder.class), ValueRecorder.class);
        arrayOfCallSite[42].callCurrent((GroovyObject)this, promotionRules, "promotions-module-junit");
        Reference cart1 = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[43].callCurrent((GroovyObject)this, "cart1", forCurrency), CartModel.class));
        Reference cart2 = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[44].callCurrent((GroovyObject)this, "cart2", forCurrency), CartModel.class));
        arrayOfCallSite[45].call(productsToAddToCart, new __spock_feature_6_2_closure10(this, this, cart1, cart2));
        double cart1TotalPriceWithoutPromo = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[46].callGetProperty(cart1.get()));
        arrayOfCallSite[47].call(couponCodes, new __spock_feature_6_2_closure11(this, this, cart1));
        arrayOfCallSite[48].callCurrent((GroovyObject)this, arrayOfCallSite[49].call(cart1.get()));
        double cart1TotalPriceWithRedeemedCoupons = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[50].callGetProperty(cart1.get()));
        double cart2TotalPriceWithoutPromo = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[51].callGetProperty(cart2.get()));
        arrayOfCallSite[52].call(couponCodes, new __spock_feature_6_2_closure12(this, this, cart2));
        arrayOfCallSite[53].callCurrent((GroovyObject)this, arrayOfCallSite[54].call(cart2.get()));
        double cart2TotalPriceWithRedeemedCoupons = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[55].callGetProperty(cart2.get()));
        boolean gifts1PresentAsExpected = DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[56].call(gifts1, new __spock_feature_6_2_closure13(this, this, cart1)));
        boolean gifts2PresentAsExpected = DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[57].call(gifts2, new __spock_feature_6_2_closure14(this, this, cart2)));
        arrayOfCallSite[58].call(couponCodes, new __spock_feature_6_2_closure15(this, this, cart1));
        arrayOfCallSite[59].callCurrent((GroovyObject)this, arrayOfCallSite[60].call(cart1.get()));
        double cart1TotalPriceAfterReleasedCoupons = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[61].callGetProperty(cart1.get()));
        arrayOfCallSite[62].call(couponCodes, new __spock_feature_6_2_closure16(this, this, cart2));
        arrayOfCallSite[63].callCurrent((GroovyObject)this, arrayOfCallSite[64].call(cart2.get()));
        double cart2TotalPriceAfterReleasedCoupons = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[65].callGetProperty(cart2.get()));
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart1TotalPriceWithoutPromo == baseAmount1", Integer.valueOf(194).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart1TotalPriceWithoutPromo)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount1)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart1TotalPriceWithoutPromo == baseAmount1", Integer.valueOf(194).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart1TotalPriceWithRedeemedCoupons == baseAmount1", Integer.valueOf(195).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart1TotalPriceWithRedeemedCoupons)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount1)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart1TotalPriceWithRedeemedCoupons == baseAmount1", Integer.valueOf(195).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart1TotalPriceAfterReleasedCoupons == baseAmount1", Integer.valueOf(196).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart1TotalPriceAfterReleasedCoupons)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount1)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart1TotalPriceAfterReleasedCoupons == baseAmount1", Integer.valueOf(196).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart2TotalPriceWithoutPromo == baseAmount2", Integer.valueOf(197).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart2TotalPriceWithoutPromo)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount2)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart2TotalPriceWithoutPromo == baseAmount2", Integer.valueOf(197).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart2TotalPriceWithRedeemedCoupons == baseAmount2", Integer.valueOf(198).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart2TotalPriceWithRedeemedCoupons)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount2)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart2TotalPriceWithRedeemedCoupons == baseAmount2", Integer.valueOf(198).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart2TotalPriceAfterReleasedCoupons == baseAmount2", Integer.valueOf(199).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart2TotalPriceAfterReleasedCoupons)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount2)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart2TotalPriceAfterReleasedCoupons == baseAmount2", Integer.valueOf(199).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "gifts1PresentAsExpected == true", Integer.valueOf(200).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Boolean.valueOf(gifts1PresentAsExpected)),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), Boolean.valueOf(true))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "gifts1PresentAsExpected == true", Integer.valueOf(200).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "gifts2PresentAsExpected == true", Integer.valueOf(201).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Boolean.valueOf(gifts2PresentAsExpected)),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), Boolean.valueOf(true))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "gifts2PresentAsExpected == true", Integer.valueOf(201).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @Unroll
    @FeatureMetadata(line = 215, name = "#testId : Adding #productsToAddToCart to Cart(#forCurrency) and try to redeem of #couponCodes twice should amount to #promotedAmount with #promotionRules promotion and #baseAmount without it", ordinal = 3, blocks = {
                    @BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"testId", "productsToAddToCart", "promotionRules",
                    "couponCodes", "promotedAmount", "baseAmount", "forCurrency"})
    public void $spock_feature_6_3(Object testId, Object productsToAddToCart, Object promotionRules, Object couponCodes, Object promotedAmount, Object baseAmount, Object forCurrency)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[66].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[67].callConstructor(ValueRecorder.class), ValueRecorder.class);
        arrayOfCallSite[68].callCurrent((GroovyObject)this, promotionRules, "promotions-module-junit");
        Reference cart1 = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[69].callCurrent((GroovyObject)this, "cart1", forCurrency), CartModel.class));
        arrayOfCallSite[70].call(productsToAddToCart, new __spock_feature_6_3_closure17(this, this, cart1));
        double cart1TotalPriceWithoutPromo = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[71].callGetProperty(cart1.get()));
        arrayOfCallSite[72].call(couponCodes, new __spock_feature_6_3_closure18(this, this, cart1));
        arrayOfCallSite[73].callCurrent((GroovyObject)this, arrayOfCallSite[74].call(cart1.get()));
        double cart1TotalPriceWithRedeemedCoupons = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[75].callGetProperty(cart1.get()));
        arrayOfCallSite[76].call(couponCodes, new __spock_feature_6_3_closure19(this, this, cart1));
        arrayOfCallSite[77].callCurrent((GroovyObject)this, arrayOfCallSite[78].call(cart1.get()));
        double cart1TotalPriceWithRedeemedCoupons2 = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[79].callGetProperty(cart1.get()));
        arrayOfCallSite[80].call(couponCodes, new __spock_feature_6_3_closure20(this, this, cart1));
        arrayOfCallSite[81].callCurrent((GroovyObject)this, arrayOfCallSite[82].call(cart1.get()));
        double cart1TotalPriceAfterReleasedCoupons = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[83].callGetProperty(cart1.get()));
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart1TotalPriceWithoutPromo == baseAmount", Integer.valueOf(248).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart1TotalPriceWithoutPromo)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart1TotalPriceWithoutPromo == baseAmount", Integer.valueOf(248).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart1TotalPriceWithRedeemedCoupons == promotedAmount", Integer.valueOf(249).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart1TotalPriceWithRedeemedCoupons)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), promotedAmount)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart1TotalPriceWithRedeemedCoupons == promotedAmount", Integer.valueOf(249).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart1TotalPriceAfterReleasedCoupons == baseAmount", Integer.valueOf(250).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart1TotalPriceAfterReleasedCoupons)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart1TotalPriceAfterReleasedCoupons == baseAmount", Integer.valueOf(250).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart1TotalPriceWithRedeemedCoupons2 == promotedAmount", Integer.valueOf(251).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cart1TotalPriceWithRedeemedCoupons2)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), promotedAmount)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart1TotalPriceWithRedeemedCoupons2 == promotedAmount", Integer.valueOf(251).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    static
    {
        __$swapInit();
    }
}
