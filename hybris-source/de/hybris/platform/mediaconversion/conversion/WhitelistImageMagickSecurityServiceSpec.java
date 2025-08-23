package de.hybris.platform.mediaconversion.conversion;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.testframework.HybrisSpockSpecification;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
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
import org.spockframework.runtime.model.FieldMetadata;
import org.spockframework.runtime.model.SpecMetadata;

@UnitTest
@SpecMetadata(filename = "WhitelistImageMagickSecurityServiceSpec.groovy", line = 10)
public class WhitelistImageMagickSecurityServiceSpec extends HybrisSpockSpecification
{
    @FieldMetadata(line = 13, name = "service", ordinal = 0, initializer = true)
    private ImageMagickSecurityService service;


    @Test
    @FeatureMetadata(line = 28, name = "test valid command", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"command"})
    public void $spock_feature_2_0(Object command)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[1].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[2].callConstructor(ValueRecorder.class), ValueRecorder.class);
        Object result = arrayOfCallSite[3].call(this.service, command);
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "result.isSecure()", Integer.valueOf(34).intValue(), Integer.valueOf(9).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), result),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isSecure")), new Object[0],
                            DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(4).intValue(), Boolean.FALSE)), DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(3).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "result.isSecure()", Integer.valueOf(34).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 55, name = "test invalid command", ordinal = 1, blocks = {@BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"command"})
    public void $spock_feature_2_1(Object command)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[4].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[5].callConstructor(ValueRecorder.class), ValueRecorder.class);
        Object result = arrayOfCallSite[6].call(this.service, command);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "!result.isSecure()", Integer.valueOf(61).intValue(), Integer.valueOf(9).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()),
                            Boolean.valueOf(!DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                            ScriptBytecodeAdapter.invokeMethod0(WhitelistImageMagickSecurityServiceSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), result),
                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isSecure"))))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "!result.isSecure()", Integer.valueOf(61).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 81, name = "test valid command list", ordinal = 2, blocks = {@BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"command"})
    public void $spock_feature_2_2(Object command)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[7].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[8].callConstructor(ValueRecorder.class), ValueRecorder.class);
        Object result = arrayOfCallSite[9].call(this.service, command);
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "result.isSecure()", Integer.valueOf(87).intValue(), Integer.valueOf(9).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), result),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isSecure")), new Object[0],
                            DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(4).intValue(), Boolean.FALSE)), DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(3).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "result.isSecure()", Integer.valueOf(87).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 115, name = "test invalid command list", ordinal = 3, blocks = {@BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"command"})
    public void $spock_feature_2_3(Object command)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[10].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[11].callConstructor(ValueRecorder.class), ValueRecorder.class);
        Object result = arrayOfCallSite[12].call(this.service, command);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "!result.isSecure()", Integer.valueOf(121).intValue(), Integer.valueOf(9).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()),
                            Boolean.valueOf(!DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                            ScriptBytecodeAdapter.invokeMethod0(WhitelistImageMagickSecurityServiceSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), result),
                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isSecure"))))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "!result.isSecure()", Integer.valueOf(121).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }
}
