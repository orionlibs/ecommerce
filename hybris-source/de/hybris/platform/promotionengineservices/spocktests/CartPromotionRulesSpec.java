package de.hybris.platform.promotionengineservices.spocktests;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.order.CartModel;
import groovy.lang.GroovyObject;
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
@SpecMetadata(filename = "CartPromotionRulesSpec.groovy", line = 21)
public class CartPromotionRulesSpec extends AbstractPromotionEngineServicesSpockTest
{
    @Test
    @Unroll
    @FeatureMetadata(line = 24, name = "#testId : Adding #productsToAddToCart to Cart(#forCurrency) should amount to #promotedAmount with #promotionRules promotion and #baseAmount without it", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}),
                    @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"testId", "productsToAddToCart", "promotionRules", "promotedAmount", "baseAmount", "forCurrency"})
    public void $spock_feature_5_0(Object testId, Object productsToAddToCart, Object promotionRules, Object promotedAmount, Object baseAmount, Object forCurrency)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[0].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[1].callConstructor(ValueRecorder.class), ValueRecorder.class);
        Object user = "";
        arrayOfCallSite[2].callCurrent((GroovyObject)this, promotionRules, "promotions-module-junit");
        boolean clearCart = true;
        arrayOfCallSite[3].callCurrent((GroovyObject)this, user, forCurrency);
        if(clearCart)
        {
            CartModel newCart = (CartModel)ScriptBytecodeAdapter.castToType(arrayOfCallSite[4].call(arrayOfCallSite[5].callCurrent((GroovyObject)this)), CartModel.class);
            arrayOfCallSite[6].call(arrayOfCallSite[7].callCurrent((GroovyObject)this), newCart);
        }
        arrayOfCallSite[8].call(productsToAddToCart, new __spock_feature_5_0_closure1(this, this));
        arrayOfCallSite[9].callCurrent((GroovyObject)this, arrayOfCallSite[10].callCurrent((GroovyObject)this));
        CartModel cart = (CartModel)ScriptBytecodeAdapter.castToType(arrayOfCallSite[11].call(arrayOfCallSite[12].callCurrent((GroovyObject)this)), CartModel.class);
        double cartTotalPriceWithoutPromo = DefaultTypeTransformation.doubleUnbox(arrayOfCallSite[13].callGetProperty(cart));
        arrayOfCallSite[14].callCurrent((GroovyObject)this, arrayOfCallSite[15].call(cart));
        arrayOfCallSite[16].callCurrent((GroovyObject)this);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cartTotalPriceWithoutPromo == baseAmount", Integer.valueOf(61).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Double.valueOf(cartTotalPriceWithoutPromo)),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), baseAmount)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cartTotalPriceWithoutPromo == baseAmount", Integer.valueOf(61).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "cart.totalPrice == promotedAmount", Integer.valueOf(62).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual(
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), arrayOfCallSite[17].callGetProperty($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), cart))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()), promotedAmount)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "cart.totalPrice == promotedAmount", Integer.valueOf(62).intValue(), Integer.valueOf(3).intValue(), null, throwable);
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
