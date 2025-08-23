package de.hybris.platform.solrfacetsearch.common.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.common.SolrQueryContextProvider;
import de.hybris.platform.testframework.JUnitPlatformSpecification;
import groovy.transform.Generated;
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
@SpecMetadata(filename = "DefaultSolrQueryContextProviderDefinitionSpec.groovy", line = 14)
public class DefaultSolrQueryContextProviderDefinitionSpec extends JUnitPlatformSpecification
{
    private static final int HIGH_PRIORITY = 200;
    private static final int DEFAULT_PRIORITY = 100;
    private static final int LOW_PRIORITY = 50;
    @FieldMetadata(line = 21, name = "queryContextProvider1", ordinal = 0, initializer = true)
    private SolrQueryContextProvider queryContextProvider1;
    @FieldMetadata(line = 22, name = "queryContextProvider2", ordinal = 1, initializer = true)
    private SolrQueryContextProvider queryContextProvider2;


    @Test
    @FeatureMetadata(line = 24, name = "Query context provider definitions are equal", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.EXPECT, texts = {})}, parameterNames = {})
    public void $spock_feature_1_0()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[2].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[3].callConstructor(ValueRecorder.class), ValueRecorder.class);
        DefaultSolrQueryContextProviderDefinition queryContextProviderDefinition1 = (DefaultSolrQueryContextProviderDefinition)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[4].callConstructor(DefaultSolrQueryContextProviderDefinition.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(DEFAULT_PRIORITY), "queryContextProvider", this.queryContextProvider1})),
                        DefaultSolrQueryContextProviderDefinition.class);
        DefaultSolrQueryContextProviderDefinition queryContextProviderDefinition2 = (DefaultSolrQueryContextProviderDefinition)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[5].callConstructor(DefaultSolrQueryContextProviderDefinition.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(DEFAULT_PRIORITY), "queryContextProvider", this.queryContextProvider1})),
                        DefaultSolrQueryContextProviderDefinition.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "queryContextProviderDefinition1.compareTo(queryContextProviderDefinition2) == 0", Integer.valueOf(31).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(6).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethodN(DefaultSolrQueryContextProviderDefinitionSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), queryContextProviderDefinition1),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "compareTo")),
                                                                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()), queryContextProviderDefinition2)})),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Integer.valueOf(0))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "queryContextProviderDefinition1.compareTo(queryContextProviderDefinition2) == 0", Integer.valueOf(31).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "queryContextProviderDefinition1.equals(queryContextProviderDefinition2)", Integer.valueOf(32).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), queryContextProviderDefinition1),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "equals")),
                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()), queryContextProviderDefinition2)}DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(5).intValue(), Boolean.FALSE)),
                            DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(4).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "queryContextProviderDefinition1.equals(queryContextProviderDefinition2)", Integer.valueOf(32).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "queryContextProviderDefinition1.hashCode() == queryContextProviderDefinition2.hashCode()", Integer.valueOf(33).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(8).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(DefaultSolrQueryContextProviderDefinitionSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), queryContextProviderDefinition1),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "hashCode")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(7).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(DefaultSolrQueryContextProviderDefinitionSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), queryContextProviderDefinition2),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), "hashCode"))))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "queryContextProviderDefinition1.hashCode() == queryContextProviderDefinition2.hashCode()", Integer.valueOf(33).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 36, name = "Query context provider definitions are not equal 1", ordinal = 1, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.EXPECT, texts = {})}, parameterNames = {})
    public void $spock_feature_1_1()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[6].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[7].callConstructor(ValueRecorder.class), ValueRecorder.class);
        DefaultSolrQueryContextProviderDefinition queryContextProviderDefinition1 = (DefaultSolrQueryContextProviderDefinition)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[8].callConstructor(DefaultSolrQueryContextProviderDefinition.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(LOW_PRIORITY), "queryContextProvider", this.queryContextProvider1})),
                        DefaultSolrQueryContextProviderDefinition.class);
        DefaultSolrQueryContextProviderDefinition queryContextProviderDefinition2 = (DefaultSolrQueryContextProviderDefinition)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[9].callConstructor(DefaultSolrQueryContextProviderDefinition.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(HIGH_PRIORITY), "queryContextProvider", this.queryContextProvider1})),
                        DefaultSolrQueryContextProviderDefinition.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "!queryContextProviderDefinition1.equals(queryContextProviderDefinition2)", Integer.valueOf(43).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(!DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()),
                                            ScriptBytecodeAdapter.invokeMethodN(DefaultSolrQueryContextProviderDefinitionSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), queryContextProviderDefinition1),
                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "equals")),
                                                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()), queryContextProviderDefinition2)}))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "!queryContextProviderDefinition1.equals(queryContextProviderDefinition2)", Integer.valueOf(43).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 46, name = "Query context provider definitions are not equal 2", ordinal = 2, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.EXPECT, texts = {})}, parameterNames = {})
    public void $spock_feature_1_2()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[10].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[11].callConstructor(ValueRecorder.class), ValueRecorder.class);
        DefaultSolrQueryContextProviderDefinition queryContextProviderDefinition1 = (DefaultSolrQueryContextProviderDefinition)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[12].callConstructor(DefaultSolrQueryContextProviderDefinition.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(DEFAULT_PRIORITY), "queryContextProvider", this.queryContextProvider1})),
                        DefaultSolrQueryContextProviderDefinition.class);
        DefaultSolrQueryContextProviderDefinition queryContextProviderDefinition2 = (DefaultSolrQueryContextProviderDefinition)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[13].callConstructor(DefaultSolrQueryContextProviderDefinition.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(DEFAULT_PRIORITY), "queryContextProvider", this.queryContextProvider2})),
                        DefaultSolrQueryContextProviderDefinition.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "!queryContextProviderDefinition1.equals(queryContextProviderDefinition2)", Integer.valueOf(53).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(!DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()),
                                            ScriptBytecodeAdapter.invokeMethodN(DefaultSolrQueryContextProviderDefinitionSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), queryContextProviderDefinition1),
                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "equals")),
                                                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()), queryContextProviderDefinition2)}))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "!queryContextProviderDefinition1.equals(queryContextProviderDefinition2)", Integer.valueOf(53).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Generated
    public SolrQueryContextProvider getQueryContextProvider1()
    {
        return this.queryContextProvider1;
    }


    @Generated
    public void setQueryContextProvider1(SolrQueryContextProvider paramSolrQueryContextProvider)
    {
        this.queryContextProvider1 = paramSolrQueryContextProvider;
    }


    @Generated
    public SolrQueryContextProvider getQueryContextProvider2()
    {
        return this.queryContextProvider2;
    }


    @Generated
    public void setQueryContextProvider2(SolrQueryContextProvider paramSolrQueryContextProvider)
    {
        this.queryContextProvider2 = paramSolrQueryContextProvider;
    }
}
