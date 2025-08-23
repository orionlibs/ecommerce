package de.hybris.platform.core.threadregistry;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.testframework.HybrisSpockSpecification;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
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
import org.spockframework.runtime.model.SpecMetadata;

@UnitTest
@SpecMetadata(filename = "OperationInfoSpec.groovy", line = 10)
public class OperationInfoSpec extends HybrisSpockSpecification
{
    @Test
    @FeatureMetadata(line = 13, name = "check relation between suspendable and can handle db failures", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.EXPECT, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"canBeSuspended", "canHandleDbFailures",
                    "operationInfo"})
    public void $spock_feature_2_0(boolean canBeSuspended, boolean canHandleDbFailures, OperationInfo operationInfo)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[0].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[1].callConstructor(ValueRecorder.class), ValueRecorder.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "operationInfo.canBeSuspended() == canBeSuspended", Integer.valueOf(17).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(OperationInfoSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), operationInfo),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "canBeSuspended")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Boolean.valueOf(canBeSuspended))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "operationInfo.canBeSuspended() == canBeSuspended", Integer.valueOf(17).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "operationInfo.canHandleDbFailures() == canHandleDbFailures", Integer.valueOf(18).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(OperationInfoSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), operationInfo),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "canHandleDbFailures")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Boolean.valueOf(canHandleDbFailures))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "operationInfo.canHandleDbFailures() == canHandleDbFailures", Integer.valueOf(18).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }
}
