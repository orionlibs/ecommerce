package de.hybris.platform.persistence.flexiblesearch.polyglot;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.flexiblesearch.QueryOptions;
import de.hybris.platform.servicelayer.ServicelayerBaseSpecification;
import groovy.lang.GroovyObject;
import java.util.Optional;
import org.antlr.v4.runtime.misc.ParseCancellationException;
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

@IntegrationTest
@SpecMetadata(filename = "PolyglotQueryCriteriaConversionSpecification.groovy", line = 13)
public class PolyglotQueryCriteriaConversionSpecification extends ServicelayerBaseSpecification
{
    @Test
    @FeatureMetadata(line = 16, name = "check valid polyglot query", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"query", "params"})
    public void $spock_feature_3_0(Object query, Object params)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).setThrownException((Throwable)ScriptBytecodeAdapter.castToType(null, Throwable.class));
        null;
        try
        {
            arrayOfCallSite[0].call(PolyglotPersistenceFlexibleSearchSupport.class, arrayOfCallSite[1].call(arrayOfCallSite[2].call(arrayOfCallSite[3].call(arrayOfCallSite[4].call(QueryOptions.class), query), params)));
        }
        catch(Throwable $spock_ex)
        {
            ((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).setThrownException($spock_ex);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[5].callCurrent((GroovyObject)this);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @FeatureMetadata(line = 49, name = "check query with invalid type", ordinal = 1, blocks = {@BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"query", "params"})
    public void $spock_feature_3_1(Object query, Object params)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).setThrownException((Throwable)ScriptBytecodeAdapter.castToType(null, Throwable.class));
        null;
        try
        {
            arrayOfCallSite[6].call(PolyglotPersistenceFlexibleSearchSupport.class, arrayOfCallSite[7].call(arrayOfCallSite[8].call(arrayOfCallSite[9].call(arrayOfCallSite[10].call(QueryOptions.class), query), params)));
        }
        catch(Throwable $spock_ex)
        {
            ((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).setThrownException($spock_ex);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[11].callCurrent((GroovyObject)this, null, null, FlexibleSearchException.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @FeatureMetadata(line = 61, name = "check invalid polyglot query", ordinal = 2, blocks = {@BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"query", "params"})
    public void $spock_feature_3_2(Object query, Object params)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).setThrownException((Throwable)ScriptBytecodeAdapter.castToType(null, Throwable.class));
        null;
        try
        {
            arrayOfCallSite[12].call(PolyglotPersistenceFlexibleSearchSupport.class, arrayOfCallSite[13].call(arrayOfCallSite[14].call(arrayOfCallSite[15].call(arrayOfCallSite[16].call(QueryOptions.class), query), params)));
        }
        catch(Throwable $spock_ex)
        {
            ((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).setThrownException($spock_ex);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[17].callCurrent((GroovyObject)this, null, null, ParseCancellationException.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @FeatureMetadata(line = 112, name = "check no polyglot query returns null", ordinal = 3, blocks = {@BlockMetadata(kind = BlockKind.EXPECT, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"query", "params"})
    public void $spock_feature_3_3(Object query, Object params)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[18].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[19].callConstructor(ValueRecorder.class), ValueRecorder.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "PolyglotPersistenceFlexibleSearchSupport.tryToConvertToPolyglotCriteria(QueryOptions.newBuilder().withQuery(query).withValues(params).build()) == Optional.empty()", Integer.valueOf(114).intValue(),
                            Integer.valueOf(9).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(23).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual(
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(18).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethodN(PolyglotQueryCriteriaConversionSpecification.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), PolyglotPersistenceFlexibleSearchSupport.class),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "tryToConvertToPolyglotCriteria")),
                                                                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(16).intValue()), ScriptBytecodeAdapter.invokeMethod0(PolyglotQueryCriteriaConversionSpecification.class,
                                                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(13).intValue()), ScriptBytecodeAdapter.invokeMethodN(PolyglotQueryCriteriaConversionSpecification.class,
                                                                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(9).intValue()), ScriptBytecodeAdapter.invokeMethodN(PolyglotQueryCriteriaConversionSpecification.class,
                                                                                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()),
                                                                                                                                            ScriptBytecodeAdapter.invokeMethod0(PolyglotQueryCriteriaConversionSpecification.class,
                                                                                                                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()), QueryOptions.class),
                                                                                                                                                            ShortTypeHandling.castToString(
                                                                                                                                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()), "newBuilder")))),
                                                                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(6).intValue()), "withQuery")),
                                                                                                                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(7).intValue()), query)})),
                                                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(10).intValue()), "withValues")),
                                                                                                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(11).intValue()), params)})),
                                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(14).intValue()), "build"))))})),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(22).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(PolyglotQueryCriteriaConversionSpecification.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(19).intValue()), Optional.class),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(20).intValue()), "empty"))))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "PolyglotPersistenceFlexibleSearchSupport.tryToConvertToPolyglotCriteria(QueryOptions.newBuilder().withQuery(query).withValues(params).build()) == Optional.empty()", Integer.valueOf(114).intValue(),
                            Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }
}
