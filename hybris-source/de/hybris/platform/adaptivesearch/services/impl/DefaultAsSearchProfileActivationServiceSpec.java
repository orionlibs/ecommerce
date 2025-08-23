package de.hybris.platform.adaptivesearch.services.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.adaptivesearch.context.AsSearchProfileContext;
import de.hybris.platform.adaptivesearch.context.impl.DefaultAsSearchProfileContext;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileActivationMapping;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProfileRegistry;
import de.hybris.platform.adaptivesearch.strategies.impl.DefaultAsSearchProfileActivationMapping;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.testframework.JUnitPlatformSpecification;
import groovy.lang.GroovyObject;
import groovy.lang.Reference;
import groovy.transform.Generated;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
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

@UnitTest
@SpecMetadata(filename = "DefaultAsSearchProfileActivationServiceSpec.groovy", line = 26)
public class DefaultAsSearchProfileActivationServiceSpec extends JUnitPlatformSpecification
{
    private static final int HIGH_PRIORITY = 200;
    private static final int LOW_PRIORITY = 50;
    private static final String INDEX_TYPE = "indexType";
    private static final String DEFAULT_QUERY_CONTEXT = "DEFAULT";
    private static final String UNKNOWN_QUERY_CONTEXT = "UNKNOWN";
    @FieldMetadata(line = 37, name = "searchProfile1Pk", ordinal = 0, initializer = true)
    private PK $spock_finalField_searchProfile1Pk;
    @FieldMetadata(line = 38, name = "searchProfile1", ordinal = 1, initializer = true)
    private AbstractAsSearchProfileModel searchProfile1;
    @FieldMetadata(line = 40, name = "searchProfile2Pk", ordinal = 2, initializer = true)
    private PK $spock_finalField_searchProfile2Pk;
    @FieldMetadata(line = 41, name = "searchProfile2", ordinal = 3, initializer = true)
    private AbstractAsSearchProfileModel searchProfile2;
    @FieldMetadata(line = 43, name = "catalogVersion1", ordinal = 4, initializer = true)
    private CatalogVersionModel catalogVersion1;
    @FieldMetadata(line = 44, name = "catalogVersion2", ordinal = 5, initializer = true)
    private CatalogVersionModel catalogVersion2;
    @FieldMetadata(line = 46, name = "catalogVersions", ordinal = 6, initializer = true)
    private List<CatalogVersionModel> catalogVersions;
    @FieldMetadata(line = 48, name = "modelService", ordinal = 7, initializer = true)
    private ModelService modelService;
    @FieldMetadata(line = 49, name = "sessionService", ordinal = 8, initializer = true)
    private SessionService sessionService;
    @FieldMetadata(line = 50, name = "asSearchProfileRegistry", ordinal = 9, initializer = true)
    private AsSearchProfileRegistry asSearchProfileRegistry;
    @FieldMetadata(line = 52, name = "asSearchProfileActivationService", ordinal = 10, initializer = false)
    private DefaultAsSearchProfileActivationService asSearchProfileActivationService;


    private Object setup()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[0].callConstructor(InteractionBuilder.class, Integer.valueOf(55), Integer.valueOf(3), "searchProfile1.getPk() >> searchProfile1Pk"), InteractionBuilder.class)).addEqualTarget(this.searchProfile1)
                                        .addEqualMethodName("getPk").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[1].callGroovyObjectGetProperty(this)).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[2].callConstructor(InteractionBuilder.class, Integer.valueOf(56), Integer.valueOf(3), "modelService.get(searchProfile1Pk) >> searchProfile1"), InteractionBuilder.class)).addEqualTarget(this.modelService)
                                        .addEqualMethodName("get").setArgListKind(true, false).addEqualArg(arrayOfCallSite[3].callGroovyObjectGetProperty(this)).addConstantResponse(this.searchProfile1).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[4].callConstructor(InteractionBuilder.class, Integer.valueOf(58), Integer.valueOf(3), "searchProfile2.getPk() >> searchProfile2Pk"), InteractionBuilder.class)).addEqualTarget(this.searchProfile2)
                                        .addEqualMethodName("getPk").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[5].callGroovyObjectGetProperty(this)).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[6].callConstructor(InteractionBuilder.class, Integer.valueOf(59), Integer.valueOf(3), "modelService.get(searchProfile2Pk) >> searchProfile2"), InteractionBuilder.class)).addEqualTarget(this.modelService)
                                        .addEqualMethodName("get").setArgListKind(true, false).addEqualArg(arrayOfCallSite[7].callGroovyObjectGetProperty(this)).addConstantResponse(this.searchProfile2).build());
        null;
        Object object = arrayOfCallSite[8].callConstructor(DefaultAsSearchProfileActivationService.class);
        this.asSearchProfileActivationService = (DefaultAsSearchProfileActivationService)ScriptBytecodeAdapter.castToType(object, DefaultAsSearchProfileActivationService.class);
        arrayOfCallSite[9].call(this.asSearchProfileActivationService, this.modelService);
        arrayOfCallSite[10].call(this.asSearchProfileActivationService, this.sessionService);
        return arrayOfCallSite[11].call(this.asSearchProfileActivationService, this.asSearchProfileRegistry);
    }


    @Test
    @FeatureMetadata(line = 67, name = "Set current search profiles", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_0()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).enterScope();
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(((InteractionBuilder)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[22].callConstructor(InteractionBuilder.class, Integer.valueOf(73), Integer.valueOf(3), "1 * sessionService.setAttribute(DefaultAsSearchProfileActivationService.CURRENT_SEARCH_PROFILES, List.of(searchProfile1Pk, searchProfile2Pk))"),
                        InteractionBuilder.class)).setFixedCount(Integer.valueOf(1)).addEqualTarget(this.sessionService).addEqualMethodName("setAttribute").setArgListKind(true, false).addEqualArg(arrayOfCallSite[23].callGetProperty(DefaultAsSearchProfileActivationService.class))
                        .addEqualArg(arrayOfCallSite[24].call(List.class, arrayOfCallSite[25].callGroovyObjectGetProperty(this), arrayOfCallSite[26].callGroovyObjectGetProperty(this))).build());
        null;
        arrayOfCallSite[27].call(this.asSearchProfileActivationService, arrayOfCallSite[28].call(Arrays.class, this.searchProfile1, this.searchProfile2));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 76, name = "Get empty current search profiles", ordinal = 1, blocks = {@BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_1()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[29].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[30].callConstructor(ValueRecorder.class), ValueRecorder.class);
        Optional searchProfilesResult = (Optional)ScriptBytecodeAdapter.castToType(arrayOfCallSite[31].call(this.asSearchProfileActivationService), Optional.class);
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "searchProfilesResult.isEmpty()", Integer.valueOf(83).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), searchProfilesResult), ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isEmpty")),
                            new Object[0], DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(4).intValue(), Boolean.FALSE)), DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(3).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "searchProfilesResult.isEmpty()", Integer.valueOf(83).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 86, name = "Get current search profiles", ordinal = 2, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_2()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[32].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[33].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(((InteractionBuilder)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[34].callConstructor(InteractionBuilder.class, Integer.valueOf(89), Integer.valueOf(3), "sessionService.getAttribute(DefaultAsSearchProfileActivationService.CURRENT_SEARCH_PROFILES) >> List.of(searchProfile1Pk, searchProfile2Pk)"),
                        InteractionBuilder.class)).addEqualTarget(this.sessionService).addEqualMethodName("getAttribute").setArgListKind(true, false).addEqualArg(arrayOfCallSite[35].callGetProperty(DefaultAsSearchProfileActivationService.class))
                        .addConstantResponse(arrayOfCallSite[36].call(List.class, arrayOfCallSite[37].callGroovyObjectGetProperty(this), arrayOfCallSite[38].callGroovyObjectGetProperty(this))).build());
        null;
        Optional searchProfilesResult = (Optional)ScriptBytecodeAdapter.castToType(arrayOfCallSite[39].call(this.asSearchProfileActivationService), Optional.class);
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "searchProfilesResult.isPresent()", Integer.valueOf(96).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), searchProfilesResult), ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isPresent")),
                            new Object[0], DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(4).intValue(), Boolean.FALSE)), DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(3).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "searchProfilesResult.isPresent()", Integer.valueOf(96).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "assertThat(searchProfilesResult.get()).hasSize(2).containsExactly(searchProfile1, searchProfile2)", Integer.valueOf(97).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(10).intValue()), ScriptBytecodeAdapter.invokeMethodN(DefaultAsSearchProfileActivationServiceSpec.class,
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(6).intValue()), arrayOfCallSite[40].callStatic(Assertions.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(DefaultAsSearchProfileActivationServiceSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), searchProfilesResult),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()), "get")))))),
                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(7).intValue()), "hasSize")),
                                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(8).intValue()), Integer.valueOf(2))})),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(11).intValue()), "containsExactly")),
                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(12).intValue()), this.searchProfile1), $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(13).intValue()), this.searchProfile2)}
                            DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(16).intValue(), Boolean.FALSE)), DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(15).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "assertThat(searchProfilesResult.get()).hasSize(2).containsExactly(searchProfile1, searchProfile2)", Integer.valueOf(97).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 100, name = "Clear current search profiles", ordinal = 3, blocks = {@BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_3()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).enterScope();
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[41].callConstructor(InteractionBuilder.class, Integer.valueOf(106), Integer.valueOf(3), "1 * sessionService.removeAttribute(DefaultAsSearchProfileActivationService.CURRENT_SEARCH_PROFILES)"),
                                        InteractionBuilder.class)).setFixedCount(Integer.valueOf(1)).addEqualTarget(this.sessionService).addEqualMethodName("removeAttribute").setArgListKind(true, false).addEqualArg(arrayOfCallSite[42].callGetProperty(DefaultAsSearchProfileActivationService.class))
                                        .build());
        null;
        arrayOfCallSite[43].call(this.asSearchProfileActivationService);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 109, name = "Get search profiles activation groups from current search profiles", ordinal = 4, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_4()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[44].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[45].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(((InteractionBuilder)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[46].callConstructor(InteractionBuilder.class, Integer.valueOf(112), Integer.valueOf(3), "sessionService.getAttribute(DefaultAsSearchProfileActivationService.CURRENT_SEARCH_PROFILES) >> List.of(searchProfile1Pk, searchProfile2Pk)"),
                        InteractionBuilder.class)).addEqualTarget(this.sessionService).addEqualMethodName("getAttribute").setArgListKind(true, false).addEqualArg(arrayOfCallSite[47].callGetProperty(DefaultAsSearchProfileActivationService.class))
                        .addConstantResponse(arrayOfCallSite[48].call(List.class, arrayOfCallSite[49].callGroovyObjectGetProperty(this), arrayOfCallSite[50].callGroovyObjectGetProperty(this))).build());
        null;
        AsSearchProfileContext context = (AsSearchProfileContext)ScriptBytecodeAdapter.castToType(arrayOfCallSite[51].call(arrayOfCallSite[52].call(arrayOfCallSite[53].call(arrayOfCallSite[54].call(DefaultAsSearchProfileContext.class), INDEX_TYPE), this.catalogVersions)),
                        AsSearchProfileContext.class);
        List searchProfileActivationGroups = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[55].call(this.asSearchProfileActivationService, context), List.class);
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "assertThat(searchProfileActivationGroups).hasSize(1)", Integer.valueOf(119).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                            arrayOfCallSite[56].callStatic(Assertions.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), searchProfileActivationGroups))),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "hasSize")),
                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Integer.valueOf(1))}DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(8).intValue(), Boolean.FALSE)),
                            DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(7).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "assertThat(searchProfileActivationGroups).hasSize(1)", Integer.valueOf(119).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 122, name = "Get empty search profiles activation groups when no activation strategy is configured", ordinal = 5, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_5()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[57].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[58].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[59].callConstructor(InteractionBuilder.class, Integer.valueOf(125), Integer.valueOf(3), "asSearchProfileRegistry.getSearchProfileActivationMappings() >> List.of()"),
                                        InteractionBuilder.class)).addEqualTarget(this.asSearchProfileRegistry).addEqualMethodName("getSearchProfileActivationMappings").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[60].call(List.class)).build());
        null;
        AsSearchProfileContext context = (AsSearchProfileContext)ScriptBytecodeAdapter.castToType(arrayOfCallSite[61].call(arrayOfCallSite[62].call(arrayOfCallSite[63].call(arrayOfCallSite[64].call(DefaultAsSearchProfileContext.class), INDEX_TYPE), this.catalogVersions)),
                        AsSearchProfileContext.class);
        List searchProfileActivationGroups = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[65].call(this.asSearchProfileActivationService, context), List.class);
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "assertThat(searchProfileActivationGroups).isEmpty()", Integer.valueOf(132).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                            arrayOfCallSite[66].callStatic(Assertions.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), searchProfileActivationGroups))),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "isEmpty")), new Object[0],
                            DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(7).intValue(), Boolean.FALSE)), DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(6).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "assertThat(searchProfileActivationGroups).isEmpty()", Integer.valueOf(132).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 135, name = "Get search profiles activation groups from activation strategy using search profiles", ordinal = 6, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_6()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[67].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[68].callConstructor(ValueRecorder.class), ValueRecorder.class);
        Object object = new Object(this);
        AsSearchProfileActivationMapping activationMapping = (AsSearchProfileActivationMapping)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[69].callConstructor(DefaultAsSearchProfileActivationMapping.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(HIGH_PRIORITY), "activationStrategy", object})), AsSearchProfileActivationMapping.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[70].callConstructor(InteractionBuilder.class, Integer.valueOf(146), Integer.valueOf(3), "asSearchProfileRegistry.getSearchProfileActivationMappings() >> List.of(activationMapping)"),
                                        InteractionBuilder.class)).addEqualTarget(this.asSearchProfileRegistry).addEqualMethodName("getSearchProfileActivationMappings").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[71].call(List.class, activationMapping)).build());
        null;
        AsSearchProfileContext context = (AsSearchProfileContext)ScriptBytecodeAdapter.castToType(arrayOfCallSite[72].call(arrayOfCallSite[73].call(arrayOfCallSite[74].call(arrayOfCallSite[75].call(DefaultAsSearchProfileContext.class), INDEX_TYPE), this.catalogVersions)),
                        AsSearchProfileContext.class);
        List searchProfileActivationGroups = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[76].call(this.asSearchProfileActivationService, context), List.class);
        try
        {
            SpockRuntime.verifyMethodCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "assertThat(searchProfileActivationGroups).hasSize(1)", Integer.valueOf(154).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                            arrayOfCallSite[77].callStatic(Assertions.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), searchProfileActivationGroups))),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "hasSize")),
                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Integer.valueOf(1))}DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(8).intValue(), Boolean.FALSE)),
                            DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(7).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "assertThat(searchProfileActivationGroups).hasSize(1)", Integer.valueOf(154).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[78].callCurrent((GroovyObject)this, arrayOfCallSite[79].call(searchProfileActivationGroups, Integer.valueOf(0)), new __spock_feature_1_6_closure1(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 163, name = "Get search profiles activation groups from activation strategy using group", ordinal = 7, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_7()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[80].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[81].callConstructor(ValueRecorder.class), ValueRecorder.class);
        Object object = new Object(this);
        AsSearchProfileActivationMapping activationMapping = (AsSearchProfileActivationMapping)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[82].callConstructor(DefaultAsSearchProfileActivationMapping.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(HIGH_PRIORITY), "activationStrategy", object})), AsSearchProfileActivationMapping.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[83].callConstructor(InteractionBuilder.class, Integer.valueOf(174), Integer.valueOf(3), "asSearchProfileRegistry.getSearchProfileActivationMappings() >> List.of(activationMapping)"),
                                        InteractionBuilder.class)).addEqualTarget(this.asSearchProfileRegistry).addEqualMethodName("getSearchProfileActivationMappings").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[84].call(List.class, activationMapping)).build());
        null;
        AsSearchProfileContext context = (AsSearchProfileContext)ScriptBytecodeAdapter.castToType(arrayOfCallSite[85].call(arrayOfCallSite[86].call(arrayOfCallSite[87].call(arrayOfCallSite[88].call(DefaultAsSearchProfileContext.class), INDEX_TYPE), this.catalogVersions)),
                        AsSearchProfileContext.class);
        List searchProfileActivationGroups = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[89].call(this.asSearchProfileActivationService, context), List.class);
        try
        {
            SpockRuntime.verifyMethodCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "assertThat(searchProfileActivationGroups).hasSize(1)", Integer.valueOf(182).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                            arrayOfCallSite[90].callStatic(Assertions.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), searchProfileActivationGroups))),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "hasSize")),
                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Integer.valueOf(1))}DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(8).intValue(), Boolean.FALSE)),
                            DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(7).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "assertThat(searchProfileActivationGroups).hasSize(1)", Integer.valueOf(182).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[91].callCurrent((GroovyObject)this, arrayOfCallSite[92].call(searchProfileActivationGroups, Integer.valueOf(0)), new __spock_feature_1_7_closure2(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 190, name = "Get search profiles activation groups from mutiple activation strategies", ordinal = 8, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_8()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[93].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[94].callConstructor(ValueRecorder.class), ValueRecorder.class);
        Object object1 = new Object(this);
        Object object2 = new Object(this);
        AsSearchProfileActivationMapping activationMapping1 = (AsSearchProfileActivationMapping)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[95].callConstructor(DefaultAsSearchProfileActivationMapping.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(HIGH_PRIORITY), "activationStrategy", object1})), AsSearchProfileActivationMapping.class);
        AsSearchProfileActivationMapping activationMapping2 = (AsSearchProfileActivationMapping)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[96].callConstructor(DefaultAsSearchProfileActivationMapping.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(LOW_PRIORITY), "activationStrategy", object2})), AsSearchProfileActivationMapping.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[97].callConstructor(InteractionBuilder.class, Integer.valueOf(209), Integer.valueOf(3), "asSearchProfileRegistry.getSearchProfileActivationMappings() >> List.of(activationMapping1, activationMapping2)"),
                                        InteractionBuilder.class)).addEqualTarget(this.asSearchProfileRegistry).addEqualMethodName("getSearchProfileActivationMappings").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[98].call(List.class, activationMapping1, activationMapping2))
                                        .build());
        null;
        AsSearchProfileContext context = (AsSearchProfileContext)ScriptBytecodeAdapter.castToType(arrayOfCallSite[99].call(arrayOfCallSite[100].call(arrayOfCallSite[101].call(arrayOfCallSite[102].call(DefaultAsSearchProfileContext.class), INDEX_TYPE), this.catalogVersions)),
                        AsSearchProfileContext.class);
        List searchProfileActivationGroups = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[103].call(this.asSearchProfileActivationService, context), List.class);
        try
        {
            SpockRuntime.verifyMethodCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "assertThat(searchProfileActivationGroups).hasSize(2)", Integer.valueOf(217).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                            arrayOfCallSite[104].callStatic(Assertions.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), searchProfileActivationGroups))),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "hasSize")),
                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Integer.valueOf(2))}DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(8).intValue(), Boolean.FALSE)),
                            DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(7).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "assertThat(searchProfileActivationGroups).hasSize(2)", Integer.valueOf(217).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[105].callCurrent((GroovyObject)this, arrayOfCallSite[106].call(searchProfileActivationGroups, Integer.valueOf(0)), new __spock_feature_1_8_closure3(this, this, $spock_errorCollector));
        arrayOfCallSite[107].callCurrent((GroovyObject)this, arrayOfCallSite[108].call(searchProfileActivationGroups, Integer.valueOf(1)), new __spock_feature_1_8_closure4(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 230, name = "Get search profiles activation groups with query context 1", ordinal = 9, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_9()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[109].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[110].callConstructor(ValueRecorder.class), ValueRecorder.class);
        Object object = new Object(this);
        AsSearchProfileActivationMapping activationMapping = (AsSearchProfileActivationMapping)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[111].callConstructor(DefaultAsSearchProfileActivationMapping.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(HIGH_PRIORITY), "activationStrategy", object})), AsSearchProfileActivationMapping.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[112].callConstructor(InteractionBuilder.class, Integer.valueOf(241), Integer.valueOf(3), "asSearchProfileRegistry.getSearchProfileActivationMappings() >> List.of(activationMapping)"),
                                        InteractionBuilder.class)).addEqualTarget(this.asSearchProfileRegistry).addEqualMethodName("getSearchProfileActivationMappings").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[113].call(List.class, activationMapping)).build());
        null;
        AsSearchProfileContext context = (AsSearchProfileContext)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[114].call(arrayOfCallSite[115].call(arrayOfCallSite[116].call(arrayOfCallSite[117].call(arrayOfCallSite[118].call(DefaultAsSearchProfileContext.class), arrayOfCallSite[119].call(List.class, DEFAULT_QUERY_CONTEXT)), INDEX_TYPE), this.catalogVersions)),
                        AsSearchProfileContext.class);
        List searchProfileActivationGroups = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[120].call(this.asSearchProfileActivationService, context), List.class);
        try
        {
            SpockRuntime.verifyMethodCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "assertThat(searchProfileActivationGroups).hasSize(1)", Integer.valueOf(249).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                            arrayOfCallSite[121].callStatic(Assertions.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), searchProfileActivationGroups))),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "hasSize")),
                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Integer.valueOf(1))}DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(8).intValue(), Boolean.FALSE)),
                            DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(7).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "assertThat(searchProfileActivationGroups).hasSize(1)", Integer.valueOf(249).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[122].callCurrent((GroovyObject)this, arrayOfCallSite[123].call(searchProfileActivationGroups, Integer.valueOf(0)), new __spock_feature_1_9_closure5(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 257, name = "Get search profiles activation groups with query context 2", ordinal = 10, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_10()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[124].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[125].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[126].callConstructor(InteractionBuilder.class, Integer.valueOf(260), Integer.valueOf(3), "searchProfile1.getQueryContext() >> DEFAULT_QUERY_CONTEXT"), InteractionBuilder.class)).addEqualTarget(
                                        this.searchProfile1).addEqualMethodName("getQueryContext").setArgListKind(true, false).addConstantResponse(DEFAULT_QUERY_CONTEXT).build());
        null;
        Object object = new Object(this);
        AsSearchProfileActivationMapping activationMapping = (AsSearchProfileActivationMapping)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[127].callConstructor(DefaultAsSearchProfileActivationMapping.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(HIGH_PRIORITY), "activationStrategy", object})), AsSearchProfileActivationMapping.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[128].callConstructor(InteractionBuilder.class, Integer.valueOf(270), Integer.valueOf(3), "asSearchProfileRegistry.getSearchProfileActivationMappings() >> List.of(activationMapping)"),
                                        InteractionBuilder.class)).addEqualTarget(this.asSearchProfileRegistry).addEqualMethodName("getSearchProfileActivationMappings").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[129].call(List.class, activationMapping)).build());
        null;
        AsSearchProfileContext context = (AsSearchProfileContext)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[130].call(arrayOfCallSite[131].call(arrayOfCallSite[132].call(arrayOfCallSite[133].call(arrayOfCallSite[134].call(DefaultAsSearchProfileContext.class), arrayOfCallSite[135].call(List.class, DEFAULT_QUERY_CONTEXT)), INDEX_TYPE), this.catalogVersions)),
                        AsSearchProfileContext.class);
        List searchProfileActivationGroups = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[136].call(this.asSearchProfileActivationService, context), List.class);
        try
        {
            SpockRuntime.verifyMethodCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "assertThat(searchProfileActivationGroups).hasSize(1)", Integer.valueOf(278).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                            arrayOfCallSite[137].callStatic(Assertions.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), searchProfileActivationGroups))),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "hasSize")),
                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Integer.valueOf(1))}DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(8).intValue(), Boolean.FALSE)),
                            DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(7).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "assertThat(searchProfileActivationGroups).hasSize(1)", Integer.valueOf(278).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[138].callCurrent((GroovyObject)this, arrayOfCallSite[139].call(searchProfileActivationGroups, Integer.valueOf(0)), new __spock_feature_1_10_closure6(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 286, name = "Get search profiles activation groups with query context 3", ordinal = 11, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_11()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[140].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[141].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[142].callConstructor(InteractionBuilder.class, Integer.valueOf(289), Integer.valueOf(3), "searchProfile1.getQueryContext() >> UNKNOWN_QUERY_CONTEXT"), InteractionBuilder.class)).addEqualTarget(
                                        this.searchProfile1).addEqualMethodName("getQueryContext").setArgListKind(true, false).addConstantResponse(UNKNOWN_QUERY_CONTEXT).build());
        null;
        Object object = new Object(this);
        AsSearchProfileActivationMapping activationMapping = (AsSearchProfileActivationMapping)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[143].callConstructor(DefaultAsSearchProfileActivationMapping.class, ScriptBytecodeAdapter.createMap(new Object[] {"priority", Integer.valueOf(HIGH_PRIORITY), "activationStrategy", object})), AsSearchProfileActivationMapping.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[144].callConstructor(InteractionBuilder.class, Integer.valueOf(299), Integer.valueOf(3), "asSearchProfileRegistry.getSearchProfileActivationMappings() >> List.of(activationMapping)"),
                                        InteractionBuilder.class)).addEqualTarget(this.asSearchProfileRegistry).addEqualMethodName("getSearchProfileActivationMappings").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[145].call(List.class, activationMapping)).build());
        null;
        AsSearchProfileContext context = (AsSearchProfileContext)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[146].call(arrayOfCallSite[147].call(arrayOfCallSite[148].call(arrayOfCallSite[149].call(arrayOfCallSite[150].call(DefaultAsSearchProfileContext.class), arrayOfCallSite[151].call(List.class, DEFAULT_QUERY_CONTEXT)), INDEX_TYPE), this.catalogVersions)),
                        AsSearchProfileContext.class);
        List searchProfileActivationGroups = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[152].call(this.asSearchProfileActivationService, context), List.class);
        try
        {
            SpockRuntime.verifyMethodCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "assertThat(searchProfileActivationGroups).hasSize(1)", Integer.valueOf(307).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                            arrayOfCallSite[153].callStatic(Assertions.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), searchProfileActivationGroups))),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "hasSize")),
                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Integer.valueOf(1))}DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(8).intValue(), Boolean.FALSE)),
                            DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(7).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "assertThat(searchProfileActivationGroups).hasSize(1)", Integer.valueOf(307).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[154].callCurrent((GroovyObject)this, arrayOfCallSite[155].call(searchProfileActivationGroups, Integer.valueOf(0)), new __spock_feature_1_11_closure7(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Generated
    public static String getINDEX_TYPE()
    {
        return INDEX_TYPE;
    }


    @Generated
    public static String getDEFAULT_QUERY_CONTEXT()
    {
        return DEFAULT_QUERY_CONTEXT;
    }


    @Generated
    public static String getUNKNOWN_QUERY_CONTEXT()
    {
        return UNKNOWN_QUERY_CONTEXT;
    }


    @Generated
    public final PK get$spock_finalField_searchProfile1Pk()
    {
        return this.$spock_finalField_searchProfile1Pk;
    }


    @Generated
    public AbstractAsSearchProfileModel getSearchProfile1()
    {
        return this.searchProfile1;
    }


    @Generated
    public void setSearchProfile1(AbstractAsSearchProfileModel paramAbstractAsSearchProfileModel)
    {
        this.searchProfile1 = paramAbstractAsSearchProfileModel;
    }


    @Generated
    public final PK get$spock_finalField_searchProfile2Pk()
    {
        return this.$spock_finalField_searchProfile2Pk;
    }


    @Generated
    public AbstractAsSearchProfileModel getSearchProfile2()
    {
        return this.searchProfile2;
    }


    @Generated
    public void setSearchProfile2(AbstractAsSearchProfileModel paramAbstractAsSearchProfileModel)
    {
        this.searchProfile2 = paramAbstractAsSearchProfileModel;
    }


    @Generated
    public CatalogVersionModel getCatalogVersion1()
    {
        return this.catalogVersion1;
    }


    @Generated
    public void setCatalogVersion1(CatalogVersionModel paramCatalogVersionModel)
    {
        this.catalogVersion1 = paramCatalogVersionModel;
    }


    @Generated
    public CatalogVersionModel getCatalogVersion2()
    {
        return this.catalogVersion2;
    }


    @Generated
    public void setCatalogVersion2(CatalogVersionModel paramCatalogVersionModel)
    {
        this.catalogVersion2 = paramCatalogVersionModel;
    }


    @Generated
    public List<CatalogVersionModel> getCatalogVersions()
    {
        return this.catalogVersions;
    }


    @Generated
    public void setCatalogVersions(List<CatalogVersionModel> paramList)
    {
        this.catalogVersions = paramList;
    }


    @Generated
    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Generated
    public void setModelService(ModelService paramModelService)
    {
        this.modelService = paramModelService;
    }


    @Generated
    public SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Generated
    public void setSessionService(SessionService paramSessionService)
    {
        this.sessionService = paramSessionService;
    }


    @Generated
    public AsSearchProfileRegistry getAsSearchProfileRegistry()
    {
        return this.asSearchProfileRegistry;
    }


    @Generated
    public void setAsSearchProfileRegistry(AsSearchProfileRegistry paramAsSearchProfileRegistry)
    {
        this.asSearchProfileRegistry = paramAsSearchProfileRegistry;
    }


    @Generated
    public DefaultAsSearchProfileActivationService getAsSearchProfileActivationService()
    {
        return this.asSearchProfileActivationService;
    }


    @Generated
    public void setAsSearchProfileActivationService(DefaultAsSearchProfileActivationService paramDefaultAsSearchProfileActivationService)
    {
        this.asSearchProfileActivationService = paramDefaultAsSearchProfileActivationService;
    }
}
