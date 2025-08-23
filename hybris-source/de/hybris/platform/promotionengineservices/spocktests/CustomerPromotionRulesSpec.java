package de.hybris.platform.promotionengineservices.spocktests;

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
@SpecMetadata(filename = "CustomerPromotionRulesSpec.groovy", line = 21)
public class CustomerPromotionRulesSpec extends AbstractPromotionEngineServicesSpockTest
{
    @Test
    @Unroll
    @FeatureMetadata(line = 24, name = "#testId : Adding #productsToAddToCart to Cart(#forCurrency) for #customerId of #customerGroup with #promotionRules promotions should amount to #promotedAmount with the promotions and #baseAmount without it", ordinal = 0, blocks = {
                    @BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"testId", "productsToAddToCart", "promotionRules",
                    "customerId", "customerGroup", "promotedAmount", "baseAmount", "forCurrency"})
    public void $spock_feature_5_0(Object testId, Object productsToAddToCart, Object promotionRules, Object customerId, Object customerGroup, Object promotedAmount, Object baseAmount, Object forCurrency)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[0].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[1].callConstructor(ValueRecorder.class), ValueRecorder.class);
        arrayOfCallSite[2].callCurrent((GroovyObject)this, promotionRules, "promotions-module-junit");
        arrayOfCallSite[3].callCurrent((GroovyObject)this, customerId, customerGroup);
        Reference cart = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[4].callCurrent((GroovyObject)this, "cart", forCurrency), CartModel.class));
        arrayOfCallSite[5].callCurrent((GroovyObject)this, customerId, arrayOfCallSite[6].call(cart.get()));
        arrayOfCallSite[7].call(productsToAddToCart, new __spock_feature_5_0_closure1(this, this, cart));
        double cartTotalPriceWithoutPromo = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[8].callGetProperty(cart.get()));
        arrayOfCallSite[9].callCurrent((GroovyObject)this, arrayOfCallSite[10].call(cart.get()));
        double cartTotalAfterPromo = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[11].callGetProperty(cart.get()));
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cartTotalPriceWithoutPromo == baseAmount", Integer.valueOf(44).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cartTotalPriceWithoutPromo)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cartTotalPriceWithoutPromo == baseAmount", Integer.valueOf(44).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cartTotalAfterPromo == promotedAmount", Integer.valueOf(45).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cartTotalAfterPromo)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), promotedAmount)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cartTotalAfterPromo == promotedAmount", Integer.valueOf(45).intValue(), Integer.valueOf(3).intValue(), null, throwable);
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
