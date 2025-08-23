package de.hybris.platform.solrfacetsearch.common.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.solrfacetsearch.common.SolrQueryContextProvider;
import de.hybris.platform.testframework.JUnitPlatformSpecification;
import groovy.lang.GroovyObject;
import groovy.transform.Generated;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.runtime.BytecodeInterface8;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.codehaus.groovy.runtime.typehandling.ShortTypeHandling;
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
import org.springframework.context.ApplicationContext;

@UnitTest
@SpecMetadata(filename = "DefaultSolrQueryContextFactorySpec.groovy", line = 17)
public class DefaultSolrQueryContextFactorySpec extends JUnitPlatformSpecification
{
    private static final String QUERY_CONTEXT_PROVIDER_1_BEAN_ID = "queryContextProvider1";
    private static final String QUERY_CONTEXT_PROVIDER_2_BEAN_ID = "queryContextProvider2";
    private static final int HIGH_PRIORITY = 200;
    private static final int LOW_PRIORITY = 50;
    @FieldMetadata(line = 26, name = "applicationContext", ordinal = 0, initializer = true)
    private ApplicationContext applicationContext;
    @FieldMetadata(line = 28, name = "queryContextProvider1", ordinal = 1, initializer = true)
    private SolrQueryContextProvider queryContextProvider1;
    @FieldMetadata(line = 29, name = "queryContextProvider2", ordinal = 2, initializer = true)
    private SolrQueryContextProvider queryContextProvider2;


    private Object setup()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(((InteractionBuilder)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[0].callConstructor(InteractionBuilder.class, Integer.valueOf(32), Integer.valueOf(3), "applicationContext.getBean(QUERY_CONTEXT_PROVIDER_1_BEAN_ID, SolrQueryContextProvider.class) >> queryContextProvider1"), InteractionBuilder.class)).addEqualTarget(
                        this.applicationContext).addEqualMethodName("getBean").setArgListKind(true, false).addEqualArg(QUERY_CONTEXT_PROVIDER_1_BEAN_ID).addEqualArg(SolrQueryContextProvider.class).addConstantResponse(this.queryContextProvider1).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(((InteractionBuilder)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[1].callConstructor(InteractionBuilder.class, Integer.valueOf(33), Integer.valueOf(3), "applicationContext.getBean(QUERY_CONTEXT_PROVIDER_2_BEAN_ID, SolrQueryContextProvider.class) >> queryContextProvider2"), InteractionBuilder.class)).addEqualTarget(
                        this.applicationContext).addEqualMethodName("getBean").setArgListKind(true, false).addEqualArg(QUERY_CONTEXT_PROVIDER_2_BEAN_ID).addEqualArg(SolrQueryContextProvider.class).addConstantResponse(this.queryContextProvider2).build());
        return null;
    }


    public DefaultSolrQueryContextFactory createQueryContextFactory()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        DefaultSolrQueryContextFactory queryContextFactory = (DefaultSolrQueryContextFactory)ScriptBytecodeAdapter.castToType(arrayOfCallSite[2].callConstructor(DefaultSolrQueryContextFactory.class), DefaultSolrQueryContextFactory.class);
        arrayOfCallSite[3].call(queryContextFactory, this.applicationContext);
        arrayOfCallSite[4].call(queryContextFactory);
        return queryContextFactory;
    }


    @Test
    @FeatureMetadata(line = 45, name = "No query context provider configured", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_0()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[8].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[9].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[10].callConstructor(InteractionBuilder.class, Integer.valueOf(48), Integer.valueOf(3), "applicationContext.getBeansOfType(DefaultSolrQueryContextProviderDefinition) >> Map.of()"),
                                        InteractionBuilder.class)).addEqualTarget(this.applicationContext).addEqualMethodName("getBeansOfType").setArgListKind(true, false).addEqualArg(DefaultSolrQueryContextProviderDefinition.class).addConstantResponse(arrayOfCallSite[11].call(Map.class)).build());
        null;
        DefaultSolrQueryContextFactory queryContextFactory = null;
        if(!__$stMC && !BytecodeInterface8.disabledStandardMetaClass())
        {
            DefaultSolrQueryContextFactory defaultSolrQueryContextFactory = createQueryContextFactory();
        }
        else
        {
            Object object = arrayOfCallSite[12].callCurrent((GroovyObject)this);
            queryContextFactory = (DefaultSolrQueryContextFactory)ScriptBytecodeAdapter.castToType(object, DefaultSolrQueryContextFactory.class);
        }
        List queryContexts = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[13].call(queryContextFactory), List.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "queryContexts != null", Integer.valueOf(56).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), queryContexts),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "queryContexts != null", Integer.valueOf(56).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "queryContexts.isEmpty()", Integer.valueOf(57).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), queryContexts), ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isEmpty")),
                            new Object[0], DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(4).intValue(), Boolean.FALSE)), DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(3).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "queryContexts.isEmpty()", Integer.valueOf(57).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 60, name = "Single query context provider configured", ordinal = 1, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_1()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[14].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[15].callConstructor(ValueRecorder.class), ValueRecorder.class);
        DefaultSolrQueryContextProviderDefinition queryContextProviderDefinition = (DefaultSolrQueryContextProviderDefinition)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[16].callConstructor(DefaultSolrQueryContextProviderDefinition.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(HIGH_PRIORITY), "queryContextProvider", this.queryContextProvider1})),
                        DefaultSolrQueryContextProviderDefinition.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(((InteractionBuilder)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[17].callConstructor(InteractionBuilder.class, Integer.valueOf(64), Integer.valueOf(3), "applicationContext.getBeansOfType(DefaultSolrQueryContextProviderDefinition) >> Map.of(QUERY_CONTEXT_PROVIDER_1_BEAN_ID, queryContextProviderDefinition)"),
                        InteractionBuilder.class)).addEqualTarget(this.applicationContext).addEqualMethodName("getBeansOfType").setArgListKind(true, false).addEqualArg(DefaultSolrQueryContextProviderDefinition.class)
                        .addConstantResponse(arrayOfCallSite[18].call(Map.class, QUERY_CONTEXT_PROVIDER_1_BEAN_ID, queryContextProviderDefinition)).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[19].callConstructor(InteractionBuilder.class, Integer.valueOf(65), Integer.valueOf(3), "queryContextProvider1.getQueryContexts() >> List.of(\"context1\", \"context2\")"),
                                        InteractionBuilder.class)).addEqualTarget(this.queryContextProvider1).addEqualMethodName("getQueryContexts").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[20].call(List.class, "context1", "context2")).build());
        null;
        DefaultSolrQueryContextFactory queryContextFactory = null;
        if(!__$stMC && !BytecodeInterface8.disabledStandardMetaClass())
        {
            DefaultSolrQueryContextFactory defaultSolrQueryContextFactory = createQueryContextFactory();
        }
        else
        {
            Object object = arrayOfCallSite[21].callCurrent((GroovyObject)this);
            queryContextFactory = (DefaultSolrQueryContextFactory)ScriptBytecodeAdapter.castToType(object, DefaultSolrQueryContextFactory.class);
        }
        List queryContexts = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[22].call(queryContextFactory), List.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "queryContexts != null", Integer.valueOf(73).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), queryContexts),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "queryContexts != null", Integer.valueOf(73).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "queryContexts == List.of(\"context1\", \"context2\")", Integer.valueOf(74).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(7).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), queryContexts),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(6).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethodN(DefaultSolrQueryContextFactorySpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), List.class),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()), "of")),
                                                                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()), "context1"),
                                                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "context2")}))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "queryContexts == List.of(\"context1\", \"context2\")", Integer.valueOf(74).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 77, name = "Multiple query context providers configured are executed in the correct order 1", ordinal = 2, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_2()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[23].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[24].callConstructor(ValueRecorder.class), ValueRecorder.class);
        DefaultSolrQueryContextProviderDefinition queryContextProviderDefinition1 = (DefaultSolrQueryContextProviderDefinition)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[25].callConstructor(DefaultSolrQueryContextProviderDefinition.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(HIGH_PRIORITY), "queryContextProvider", this.queryContextProvider1})),
                        DefaultSolrQueryContextProviderDefinition.class);
        DefaultSolrQueryContextProviderDefinition queryContextProviderDefinition2 = (DefaultSolrQueryContextProviderDefinition)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[26].callConstructor(DefaultSolrQueryContextProviderDefinition.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(LOW_PRIORITY), "queryContextProvider", this.queryContextProvider2})),
                        DefaultSolrQueryContextProviderDefinition.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class))
                        .addInteraction(((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[27].callConstructor(InteractionBuilder.class, Integer.valueOf(82), Integer.valueOf(3),
                                                        "applicationContext.getBeansOfType(DefaultSolrQueryContextProviderDefinition) >> Map.ofEntries(\n\t\t\t\tentry(QUERY_CONTEXT_PROVIDER_1_BEAN_ID, queryContextProviderDefinition1),\n\t\t\t\tentry(QUERY_CONTEXT_PROVIDER_2_BEAN_ID, queryContextProviderDefinition2))"),
                                        InteractionBuilder.class)).addEqualTarget(this.applicationContext).addEqualMethodName("getBeansOfType").setArgListKind(true, false).addEqualArg(DefaultSolrQueryContextProviderDefinition.class).addConstantResponse(
                                                        arrayOfCallSite[28].call(Map.class, arrayOfCallSite[29].callStatic(Map.class, QUERY_CONTEXT_PROVIDER_1_BEAN_ID, queryContextProviderDefinition1), arrayOfCallSite[30].callStatic(Map.class, QUERY_CONTEXT_PROVIDER_2_BEAN_ID, queryContextProviderDefinition2)))
                                        .build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[31].callConstructor(InteractionBuilder.class, Integer.valueOf(86), Integer.valueOf(3), "queryContextProvider1.getQueryContexts() >> List.of(\"context1\", \"context2\")"),
                                        InteractionBuilder.class)).addEqualTarget(this.queryContextProvider1).addEqualMethodName("getQueryContexts").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[32].call(List.class, "context1", "context2")).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[33].callConstructor(InteractionBuilder.class, Integer.valueOf(87), Integer.valueOf(3), "queryContextProvider2.getQueryContexts() >> List.of(\"context3\", \"context4\")"),
                                        InteractionBuilder.class)).addEqualTarget(this.queryContextProvider2).addEqualMethodName("getQueryContexts").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[34].call(List.class, "context3", "context4")).build());
        null;
        DefaultSolrQueryContextFactory queryContextFactory = null;
        if(!__$stMC && !BytecodeInterface8.disabledStandardMetaClass())
        {
            DefaultSolrQueryContextFactory defaultSolrQueryContextFactory = createQueryContextFactory();
        }
        else
        {
            Object object = arrayOfCallSite[35].callCurrent((GroovyObject)this);
            queryContextFactory = (DefaultSolrQueryContextFactory)ScriptBytecodeAdapter.castToType(object, DefaultSolrQueryContextFactory.class);
        }
        List queryContexts = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[36].call(queryContextFactory), List.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "queryContexts != null", Integer.valueOf(95).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), queryContexts),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "queryContexts != null", Integer.valueOf(95).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "queryContexts == List.of(\"context1\", \"context2\", \"context3\", \"context4\")", Integer.valueOf(96).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(9).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), queryContexts),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(8).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethodN(DefaultSolrQueryContextFactorySpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), List.class),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()), "of")),
                                                                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()), "context1"),
                                                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "context2"),
                                                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), "context3"),
                                                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(6).intValue()), "context4")}))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "queryContexts == List.of(\"context1\", \"context2\", \"context3\", \"context4\")", Integer.valueOf(96).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 99, name = "Multiple query context providers configured are executed in the correct order 2", ordinal = 3, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_3()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[37].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[38].callConstructor(ValueRecorder.class), ValueRecorder.class);
        DefaultSolrQueryContextProviderDefinition queryContextProviderDefinition1 = (DefaultSolrQueryContextProviderDefinition)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[39].callConstructor(DefaultSolrQueryContextProviderDefinition.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(LOW_PRIORITY), "queryContextProvider", this.queryContextProvider1})),
                        DefaultSolrQueryContextProviderDefinition.class);
        DefaultSolrQueryContextProviderDefinition queryContextProviderDefinition2 = (DefaultSolrQueryContextProviderDefinition)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[40].callConstructor(DefaultSolrQueryContextProviderDefinition.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(HIGH_PRIORITY), "queryContextProvider", this.queryContextProvider2})),
                        DefaultSolrQueryContextProviderDefinition.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class))
                        .addInteraction(((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[41].callConstructor(InteractionBuilder.class, Integer.valueOf(104), Integer.valueOf(3),
                                                        "applicationContext.getBeansOfType(DefaultSolrQueryContextProviderDefinition) >> Map.ofEntries(\n\t\t\t\tentry(QUERY_CONTEXT_PROVIDER_1_BEAN_ID, queryContextProviderDefinition1),\n\t\t\t\tentry(QUERY_CONTEXT_PROVIDER_2_BEAN_ID, queryContextProviderDefinition2))"),
                                        InteractionBuilder.class)).addEqualTarget(this.applicationContext).addEqualMethodName("getBeansOfType").setArgListKind(true, false).addEqualArg(DefaultSolrQueryContextProviderDefinition.class).addConstantResponse(
                                                        arrayOfCallSite[42].call(Map.class, arrayOfCallSite[43].callStatic(Map.class, QUERY_CONTEXT_PROVIDER_1_BEAN_ID, queryContextProviderDefinition1), arrayOfCallSite[44].callStatic(Map.class, QUERY_CONTEXT_PROVIDER_2_BEAN_ID, queryContextProviderDefinition2)))
                                        .build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[45].callConstructor(InteractionBuilder.class, Integer.valueOf(108), Integer.valueOf(3), "queryContextProvider1.getQueryContexts() >> List.of(\"context1\", \"context2\")"),
                                        InteractionBuilder.class)).addEqualTarget(this.queryContextProvider1).addEqualMethodName("getQueryContexts").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[46].call(List.class, "context1", "context2")).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[47].callConstructor(InteractionBuilder.class, Integer.valueOf(109), Integer.valueOf(3), "queryContextProvider2.getQueryContexts() >> List.of(\"context3\", \"context4\")"),
                                        InteractionBuilder.class)).addEqualTarget(this.queryContextProvider2).addEqualMethodName("getQueryContexts").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[48].call(List.class, "context3", "context4")).build());
        null;
        DefaultSolrQueryContextFactory queryContextFactory = null;
        if(!__$stMC && !BytecodeInterface8.disabledStandardMetaClass())
        {
            DefaultSolrQueryContextFactory defaultSolrQueryContextFactory = createQueryContextFactory();
        }
        else
        {
            Object object = arrayOfCallSite[49].callCurrent((GroovyObject)this);
            queryContextFactory = (DefaultSolrQueryContextFactory)ScriptBytecodeAdapter.castToType(object, DefaultSolrQueryContextFactory.class);
        }
        List queryContexts = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[50].call(queryContextFactory), List.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "queryContexts != null", Integer.valueOf(117).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), queryContexts),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "queryContexts != null", Integer.valueOf(117).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "queryContexts == List.of(\"context3\", \"context4\", \"context1\", \"context2\")", Integer.valueOf(118).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(9).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), queryContexts),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(8).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethodN(DefaultSolrQueryContextFactorySpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), List.class),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()), "of")),
                                                                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()), "context3"),
                                                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "context4"),
                                                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), "context1"),
                                                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(6).intValue()), "context2")}))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "queryContexts == List.of(\"context3\", \"context4\", \"context1\", \"context2\")", Integer.valueOf(118).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 121, name = "Duplicate query contexts are removed", ordinal = 4, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_4()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[51].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[52].callConstructor(ValueRecorder.class), ValueRecorder.class);
        DefaultSolrQueryContextProviderDefinition queryContextProviderDefinition1 = (DefaultSolrQueryContextProviderDefinition)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[53].callConstructor(DefaultSolrQueryContextProviderDefinition.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(HIGH_PRIORITY), "queryContextProvider", this.queryContextProvider1})),
                        DefaultSolrQueryContextProviderDefinition.class);
        DefaultSolrQueryContextProviderDefinition queryContextProviderDefinition2 = (DefaultSolrQueryContextProviderDefinition)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[54].callConstructor(DefaultSolrQueryContextProviderDefinition.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(LOW_PRIORITY), "queryContextProvider", this.queryContextProvider2})),
                        DefaultSolrQueryContextProviderDefinition.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class))
                        .addInteraction(((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[55].callConstructor(InteractionBuilder.class, Integer.valueOf(126), Integer.valueOf(3),
                                                        "applicationContext.getBeansOfType(DefaultSolrQueryContextProviderDefinition) >> Map.ofEntries(\n\t\t\t\tentry(QUERY_CONTEXT_PROVIDER_1_BEAN_ID, queryContextProviderDefinition1),\n\t\t\t\tentry(QUERY_CONTEXT_PROVIDER_2_BEAN_ID, queryContextProviderDefinition2))"),
                                        InteractionBuilder.class)).addEqualTarget(this.applicationContext).addEqualMethodName("getBeansOfType").setArgListKind(true, false).addEqualArg(DefaultSolrQueryContextProviderDefinition.class).addConstantResponse(
                                                        arrayOfCallSite[56].call(Map.class, arrayOfCallSite[57].callStatic(Map.class, QUERY_CONTEXT_PROVIDER_1_BEAN_ID, queryContextProviderDefinition1), arrayOfCallSite[58].callStatic(Map.class, QUERY_CONTEXT_PROVIDER_2_BEAN_ID, queryContextProviderDefinition2)))
                                        .build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[59].callConstructor(InteractionBuilder.class, Integer.valueOf(130), Integer.valueOf(3), "queryContextProvider1.getQueryContexts() >> List.of(\"context1\", \"context2\")"),
                                        InteractionBuilder.class)).addEqualTarget(this.queryContextProvider1).addEqualMethodName("getQueryContexts").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[60].call(List.class, "context1", "context2")).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[61].callConstructor(InteractionBuilder.class, Integer.valueOf(131), Integer.valueOf(3), "queryContextProvider2.getQueryContexts() >> List.of(\"context1\", \"context3\")"),
                                        InteractionBuilder.class)).addEqualTarget(this.queryContextProvider2).addEqualMethodName("getQueryContexts").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[62].call(List.class, "context1", "context3")).build());
        null;
        DefaultSolrQueryContextFactory queryContextFactory = null;
        if(!__$stMC && !BytecodeInterface8.disabledStandardMetaClass())
        {
            DefaultSolrQueryContextFactory defaultSolrQueryContextFactory = createQueryContextFactory();
        }
        else
        {
            Object object = arrayOfCallSite[63].callCurrent((GroovyObject)this);
            queryContextFactory = (DefaultSolrQueryContextFactory)ScriptBytecodeAdapter.castToType(object, DefaultSolrQueryContextFactory.class);
        }
        List queryContexts = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[64].call(queryContextFactory), List.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "queryContexts != null", Integer.valueOf(139).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), queryContexts),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "queryContexts != null", Integer.valueOf(139).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "queryContexts == List.of(\"context1\", \"context2\", \"context3\")", Integer.valueOf(140).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(8).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), queryContexts),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(7).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethodN(DefaultSolrQueryContextFactorySpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), List.class),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()), "of")),
                                                                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()), "context1"),
                                                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "context2"),
                                                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), "context3")}))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "queryContexts == List.of(\"context1\", \"context2\", \"context3\")", Integer.valueOf(140).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Generated
    public static String getQUERY_CONTEXT_PROVIDER_1_BEAN_ID()
    {
        return QUERY_CONTEXT_PROVIDER_1_BEAN_ID;
    }


    @Generated
    public static String getQUERY_CONTEXT_PROVIDER_2_BEAN_ID()
    {
        return QUERY_CONTEXT_PROVIDER_2_BEAN_ID;
    }


    @Generated
    public ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    @Generated
    public void setApplicationContext(ApplicationContext paramApplicationContext)
    {
        this.applicationContext = paramApplicationContext;
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
