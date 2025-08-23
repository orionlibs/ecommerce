package de.hybris.platform.promotionengineservices.sampleaddonspocktest;

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
@SpecMetadata(filename = "PromotionRulesSampleAddOnSpec.groovy", line = 18)
public class PromotionRulesSampleAddOnSpec extends AbstractPromotionEngineAddOnServicesSpockTest
{
    @Test
    @Unroll
    @FeatureMetadata(line = 21, name = "#testId : Adding #productsToAddToCart to Cart(#forCurrency) with #promotionRules promotions should have discoount and amount to #promotedAmount with the promotions and #baseAmount without it", ordinal = 0, blocks = {
                    @BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"testId", "productsToAddToCart", "promotionRules",
                    "gifts", "promotedAmount", "baseAmount", "forCurrency"})
    public void $spock_feature_5_0(Object testId, Object productsToAddToCart, Object promotionRules, Object gifts, Object promotedAmount, Object baseAmount, Object forCurrency)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[0].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[1].callConstructor(ValueRecorder.class), ValueRecorder.class);
        arrayOfCallSite[2].callCurrent((GroovyObject)this, promotionRules, "promotions-module-junit");
        Reference cart = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[3].callCurrent((GroovyObject)this, "cart", forCurrency), CartModel.class));
        arrayOfCallSite[4].call(productsToAddToCart, new __spock_feature_5_0_closure1(this, this, cart));
        double cartTotalPriceWithoutPromo = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[5].callGetProperty(cart.get()));
        arrayOfCallSite[6].callCurrent((GroovyObject)this, arrayOfCallSite[7].call(cart.get()));
        double cartTotalAfterPromo = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[8].callGetProperty(cart.get()));
        boolean giftsPresentAsExpected = DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[9].call(gifts, new __spock_feature_5_0_closure2(this, this, cart)));
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cartTotalPriceWithoutPromo == baseAmount", Integer.valueOf(43).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cartTotalPriceWithoutPromo)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cartTotalPriceWithoutPromo == baseAmount", Integer.valueOf(43).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cartTotalAfterPromo == promotedAmount", Integer.valueOf(44).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cartTotalAfterPromo)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), promotedAmount)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cartTotalAfterPromo == promotedAmount", Integer.valueOf(44).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "giftsPresentAsExpected == true", Integer.valueOf(45).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Boolean.valueOf(giftsPresentAsExpected)),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), Boolean.valueOf(true))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "giftsPresentAsExpected == true", Integer.valueOf(45).intValue(), Integer.valueOf(3).intValue(), null, throwable);
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
    @FeatureMetadata(line = 87, name = "#testId : Adding PowerToolsproductsToAddToCart to Cart(#forCurrency) with #promotionRules promotions should have discoount and amount to #promotedAmount with the promotions and #baseAmount without it", ordinal = 1, blocks = {
                    @BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"testId", "productsToAddToCart", "promotionRules",
                    "gifts", "promotedAmount", "baseAmount", "forCurrency"})
    public void $spock_feature_5_1(Object testId, Object productsToAddToCart, Object promotionRules, Object gifts, Object promotedAmount, Object baseAmount, Object forCurrency)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[10].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[11].callConstructor(ValueRecorder.class), ValueRecorder.class);
        arrayOfCallSite[12].callCurrent((GroovyObject)this, promotionRules, "promotions-module-junit");
        Reference cart = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[13].callCurrent((GroovyObject)this, "cart", forCurrency), CartModel.class));
        arrayOfCallSite[14].call(productsToAddToCart, new __spock_feature_5_1_closure3(this, this, cart));
        double cartTotalPriceWithoutPromo = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[15].callGetProperty(cart.get()));
        arrayOfCallSite[16].callCurrent((GroovyObject)this, arrayOfCallSite[17].call(cart.get()));
        double cartTotalAfterPromo = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[18].callGetProperty(cart.get()));
        boolean giftsPresentAsExpected = DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[19].call(gifts, new __spock_feature_5_1_closure4(this, this, cart)));
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cartTotalPriceWithoutPromo == baseAmount", Integer.valueOf(109).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cartTotalPriceWithoutPromo)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cartTotalPriceWithoutPromo == baseAmount", Integer.valueOf(109).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cartTotalAfterPromo == promotedAmount", Integer.valueOf(110).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cartTotalAfterPromo)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), promotedAmount)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cartTotalAfterPromo == promotedAmount", Integer.valueOf(110).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "giftsPresentAsExpected == true", Integer.valueOf(111).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Boolean.valueOf(giftsPresentAsExpected)),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), Boolean.valueOf(true))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "giftsPresentAsExpected == true", Integer.valueOf(111).intValue(), Integer.valueOf(3).intValue(), null, throwable);
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
