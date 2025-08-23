package de.hybris.platform.servicelayer.internal.jalo;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.servicelayer.ServicelayerBaseSpecification;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.logging.HybrisLogListener;
import de.hybris.platform.util.logging.HybrisLogger;
import groovy.lang.Closure;
import groovy.lang.GroovyObject;
import groovy.lang.Reference;
import org.apache.log4j.Level;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.junit.Test;
import org.spockframework.mock.runtime.InteractionBuilder;
import org.spockframework.mock.runtime.MockController;
import org.spockframework.runtime.ErrorCollector;
import org.spockframework.runtime.ErrorRethrower;
import org.spockframework.runtime.SpecificationContext;
import org.spockframework.runtime.SpockRuntime;
import org.spockframework.runtime.ValueRecorder;
import org.spockframework.runtime.model.BlockKind;
import org.spockframework.runtime.model.BlockMetadata;
import org.spockframework.runtime.model.FeatureMetadata;
import org.spockframework.runtime.model.FieldMetadata;
import org.spockframework.runtime.model.SpecMetadata;

@IntegrationTest
@SpecMetadata(filename = "MediaUrlHashSaltSpec.groovy", line = 19)
public class MediaUrlHashSaltSpec extends ServicelayerBaseSpecification
{
    @FieldMetadata(line = 22, name = "mediaHashSalt", ordinal = 0, initializer = true)
    private PropertyConfigSwitcher $spock_finalField_mediaHashSalt;
    @FieldMetadata(line = 23, name = "illegalValueBehaviour", ordinal = 1, initializer = true)
    private PropertyConfigSwitcher $spock_finalField_illegalValueBehaviour;
    @FieldMetadata(line = 26, name = "servicelayerManager", ordinal = 2, initializer = false)
    private ServicelayerManager servicelayerManager;
    @FieldMetadata(line = 27, name = "listener", ordinal = 3, initializer = false)
    private HybrisLogListener listener;


    private Object setup()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Object object1 = arrayOfCallSite[0].call(ServicelayerManager.class);
        this.servicelayerManager = (ServicelayerManager)ScriptBytecodeAdapter.castToType(object1, ServicelayerManager.class);
        Object object2 = arrayOfCallSite[1].callCurrent((GroovyObject)this, "listener", HybrisLogListener.class, HybrisLogListener.class);
        this.listener = (HybrisLogListener)ScriptBytecodeAdapter.castToType(object2, HybrisLogListener.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[2].callConstructor(InteractionBuilder.class, Integer.valueOf(34), Integer.valueOf(9), "listener.isEnabledFor(_ as Level) >> true"), InteractionBuilder.class)).addEqualTarget(this.listener)
                                        .addEqualMethodName("isEnabledFor").setArgListKind(true, false).addEqualArg(arrayOfCallSite[3].callGroovyObjectGetProperty(this)).typeLastArg(Level.class).addConstantResponse(Boolean.valueOf(true)).build());
        null;
        return arrayOfCallSite[4].call(HybrisLogger.class, this.listener);
    }


    private Object cleanup() throws JaloSecurityException
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[5].call(arrayOfCallSite[6].callGroovyObjectGetProperty(this));
        arrayOfCallSite[7].call(arrayOfCallSite[8].callGroovyObjectGetProperty(this));
        return arrayOfCallSite[9].call(HybrisLogger.class, this.listener);
    }


    @Test
    @FeatureMetadata(line = 47, name = "should #result.errorEffect when checking the #salt salt before #operation and behaviour is \"#error\"", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"salt", "operation", "error", "result"})
    public void $spock_feature_3_0(Salt salt, Operation operation, String error, Result result)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[12].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[13].callConstructor(ValueRecorder.class), ValueRecorder.class);
        arrayOfCallSite[14].call(arrayOfCallSite[15].callGroovyObjectGetProperty(this), error);
        arrayOfCallSite[16].call(arrayOfCallSite[17].callGroovyObjectGetProperty(this), arrayOfCallSite[18].call(salt));
        Object ex = null;
        try
        {
            arrayOfCallSite[19].call(this.servicelayerManager, arrayOfCallSite[20].callGetProperty(JspContext.class), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual(operation, arrayOfCallSite[21].callGetProperty(Operation.class))));
        }
        catch(Exception e)
        {
            Exception exception1 = e;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "result == Result.FAIL ? ex != null : ex == null", Integer.valueOf(63).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(11).intValue()), DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), result),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                                                            arrayOfCallSite[22].callGetProperty($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), Result.class)))))))
                                            ? $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(7).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), ex),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(6).intValue()), null))))
                                            : $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(10).intValue()),
                                                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(8).intValue()), ex),
                                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(9).intValue()), null))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "result == Result.FAIL ? ex != null : ex == null", Integer.valueOf(63).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 82, name = "should #result.warningEffect when checking the #salt salt before #operation and behaviour is \"#warn\"", ordinal = 1, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"salt", "operation", "warn", "result"})
    public void $spock_feature_3_1(Salt salt, Operation operation, String warn, Result result)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[59].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        arrayOfCallSite[60].call(arrayOfCallSite[61].callGroovyObjectGetProperty(this), warn);
        arrayOfCallSite[62].call(arrayOfCallSite[63].callGroovyObjectGetProperty(this), arrayOfCallSite[64].call(salt));
        Object expectedNumberOfLogs = Integer.valueOf(ScriptBytecodeAdapter.compareEqual(result, arrayOfCallSite[65].callGetProperty(Result.class)) ? 0 : 1);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).enterScope();
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(((InteractionBuilder)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[66].callConstructor(InteractionBuilder.class, Integer.valueOf(96), Integer.valueOf(9), "expectedNumberOfLogs * listener.log({\n            it.level == Level.WARN && it.message.contains(\"media.default.storage.location.hash.salt\")\n        })"),
                        InteractionBuilder.class)).setFixedCount(expectedNumberOfLogs).addEqualTarget(this.listener).addEqualMethodName("log").setArgListKind(true, false).addCodeArg((Closure)new __spock_feature_3_1_closure1(this, this, $spock_errorCollector)).build());
        null;
        ((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).setThrownException((Throwable)ScriptBytecodeAdapter.castToType(null, Throwable.class));
        null;
        try
        {
            arrayOfCallSite[67].call(this.servicelayerManager, arrayOfCallSite[68].callGetProperty(JspContext.class), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual(operation, arrayOfCallSite[69].callGetProperty(Operation.class))));
        }
        catch(Throwable $spock_ex)
        {
            ((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).setThrownException($spock_ex);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
        arrayOfCallSite[70].callCurrent((GroovyObject)this);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 118, name = "should have no effect when checking the #salt salt before #operation and behaviour is \"#info\"", ordinal = 2, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"salt", "operation", "info"})
    public void $spock_feature_3_2(Salt salt, Operation operation, String info)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[113].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        arrayOfCallSite[114].call(arrayOfCallSite[115].callGroovyObjectGetProperty(this), info);
        arrayOfCallSite[116].call(arrayOfCallSite[117].callGroovyObjectGetProperty(this), arrayOfCallSite[118].call(salt));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).enterScope();
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(((InteractionBuilder)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[119].callConstructor(InteractionBuilder.class, Integer.valueOf(130), Integer.valueOf(9), "0 * listener.log({\n            it.level == Level.WARN && it.message.contains(\"media.default.storage.location.hash.salt\")\n        })"),
                        InteractionBuilder.class)).setFixedCount(Integer.valueOf(0)).addEqualTarget(this.listener).addEqualMethodName("log").setArgListKind(true, false).addCodeArg((Closure)new __spock_feature_3_2_closure2(this, this, $spock_errorCollector)).build());
        null;
        ((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).setThrownException((Throwable)ScriptBytecodeAdapter.castToType(null, Throwable.class));
        null;
        try
        {
            arrayOfCallSite[120].call(this.servicelayerManager, arrayOfCallSite[121].callGetProperty(JspContext.class), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual(operation, arrayOfCallSite[122].callGetProperty(Operation.class))));
        }
        catch(Throwable $spock_ex)
        {
            ((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).setThrownException($spock_ex);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
        arrayOfCallSite[123].callCurrent((GroovyObject)this);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }
}
