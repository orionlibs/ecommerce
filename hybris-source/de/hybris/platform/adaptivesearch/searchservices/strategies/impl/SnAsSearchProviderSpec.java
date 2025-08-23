package de.hybris.platform.adaptivesearch.searchservices.strategies.impl;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.searchservices.admin.data.SnExpressionInfo;
import de.hybris.platform.searchservices.admin.data.SnField;
import de.hybris.platform.searchservices.admin.data.SnFieldTypeInfo;
import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;
import de.hybris.platform.searchservices.admin.service.SnCommonConfigurationService;
import de.hybris.platform.searchservices.admin.service.SnFieldTypeRegistry;
import de.hybris.platform.searchservices.admin.service.SnIndexConfigurationService;
import de.hybris.platform.searchservices.admin.service.SnIndexTypeService;
import de.hybris.platform.searchservices.core.service.SnContextFactory;
import de.hybris.platform.searchservices.core.service.SnQualifierTypeFactory;
import de.hybris.platform.searchservices.core.service.SnSessionService;
import de.hybris.platform.searchservices.enums.SnFieldType;
import de.hybris.platform.searchservices.search.data.SnMatchQuery;
import de.hybris.platform.searchservices.search.service.SnSearchService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.JUnitPlatformSpecification;
import groovy.lang.GroovyObject;
import groovy.lang.Reference;
import groovy.transform.Generated;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
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
@SpecMetadata(filename = "SnAsSearchProviderSpec.groovy", line = 34)
public class SnAsSearchProviderSpec extends JUnitPlatformSpecification
{
    protected static final String INDEX_CONFIGURATION_CODE = "indexConfigurationId";
    protected static final String INDEX_CONFIGURATION_NAME_EN = "indexConfigurationName";
    protected static final Map<Locale, String> INDEX_CONFIGURATION_NAME;
    protected static final String INDEX_TYPE_CODE = "indexTypeId";
    protected static final String INDEX_TYPE_NAME_EN = "indexTypeName";
    protected static final Map<Locale, String> INDEX_TYPE_NAME;
    protected static final String INDEX_PROPERTY_CODE = "indexPropertyId";
    protected static final String INDEX_PROPERTY_NAME_EN = "indexPropertyName";
    protected static final Map<Locale, String> INDEX_PROPERTY_NAME;
    protected static final String INDEX_TYPE_ITEM_TYPE = "Product";
    protected static final String EXPRESSION_EXPRESSION = "expression";
    protected static final String EXPRESSION_NAME_EN = "expressionName";
    protected static final Map<Locale, String> EXPRESSION_NAME;
    @FieldMetadata(line = 62, name = "typeService", ordinal = 0, initializer = true)
    private TypeService typeService;
    @FieldMetadata(line = 63, name = "i18nService", ordinal = 1, initializer = true)
    private I18NService i18nService;
    @FieldMetadata(line = 64, name = "commonI18NService", ordinal = 2, initializer = true)
    private CommonI18NService commonI18NService;
    @FieldMetadata(line = 65, name = "catalogVersionService", ordinal = 3, initializer = true)
    private CatalogVersionService catalogVersionService;
    @FieldMetadata(line = 66, name = "snSessionService", ordinal = 4, initializer = true)
    private SnSessionService snSessionService;
    @FieldMetadata(line = 67, name = "snFieldTypeRegistry", ordinal = 5, initializer = true)
    private SnFieldTypeRegistry snFieldTypeRegistry;
    @FieldMetadata(line = 68, name = "snIndexConfigurationService", ordinal = 6, initializer = true)
    private SnIndexConfigurationService snIndexConfigurationService;
    @FieldMetadata(line = 69, name = "snIndexTypeService", ordinal = 7, initializer = true)
    private SnIndexTypeService snIndexTypeService;
    @FieldMetadata(line = 70, name = "snCommonConfigurationService", ordinal = 8, initializer = true)
    private SnCommonConfigurationService snCommonConfigurationService;
    @FieldMetadata(line = 71, name = "snQualifierTypeFactory", ordinal = 9, initializer = true)
    private SnQualifierTypeFactory snQualifierTypeFactory;
    @FieldMetadata(line = 72, name = "snContextFactory", ordinal = 10, initializer = true)
    private SnContextFactory snContextFactory;
    @FieldMetadata(line = 73, name = "snSearchService", ordinal = 11, initializer = true)
    private SnSearchService snSearchService;
    @FieldMetadata(line = 75, name = "snAsSearchProvider", ordinal = 12, initializer = false)
    private SnAsSearchProvider snAsSearchProvider;


    private Object setup()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[0].callConstructor(InteractionBuilder.class, Integer.valueOf(78), Integer.valueOf(3), "i18nService.getCurrentLocale() >> Locale.ENGLISH"), InteractionBuilder.class)).addEqualTarget(this.i18nService)
                                        .addEqualMethodName("getCurrentLocale").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[1].callGetProperty(Locale.class)).build());
        null;
        Object object = arrayOfCallSite[2].callConstructor(SnAsSearchProvider.class);
        this.snAsSearchProvider = (SnAsSearchProvider)ScriptBytecodeAdapter.castToType(object, SnAsSearchProvider.class);
        arrayOfCallSite[3].call(this.snAsSearchProvider, this.typeService);
        arrayOfCallSite[4].call(this.snAsSearchProvider, this.i18nService);
        arrayOfCallSite[5].call(this.snAsSearchProvider, this.commonI18NService);
        arrayOfCallSite[6].call(this.snAsSearchProvider, this.catalogVersionService);
        arrayOfCallSite[7].call(this.snAsSearchProvider, this.snSessionService);
        arrayOfCallSite[8].call(this.snAsSearchProvider, this.snFieldTypeRegistry);
        arrayOfCallSite[9].call(this.snAsSearchProvider, this.snIndexConfigurationService);
        arrayOfCallSite[10].call(this.snAsSearchProvider, this.snIndexTypeService);
        arrayOfCallSite[11].call(this.snAsSearchProvider, this.snCommonConfigurationService);
        arrayOfCallSite[12].call(this.snAsSearchProvider, this.snQualifierTypeFactory);
        arrayOfCallSite[13].call(this.snAsSearchProvider, this.snContextFactory);
        arrayOfCallSite[14].call(this.snAsSearchProvider, this.snSearchService);
        return arrayOfCallSite[15].call(this.snAsSearchProvider);
    }


    @Test
    @FeatureMetadata(line = 97, name = "Get empty index configurations", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_0()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[28].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[29].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[30].callConstructor(InteractionBuilder.class, Integer.valueOf(100), Integer.valueOf(3), "snIndexConfigurationService.getAllIndexConfigurations() >> List.of()"), InteractionBuilder.class)).addEqualTarget(
                                        this.snIndexConfigurationService).addEqualMethodName("getAllIndexConfigurations").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[31].call(List.class)).build());
        null;
        List resultIndexConfigurations = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[32].call(this.snAsSearchProvider), List.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultIndexConfigurations != null", Integer.valueOf(106).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexConfigurations),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultIndexConfigurations != null", Integer.valueOf(106).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultIndexConfigurations.size() == 0", Integer.valueOf(107).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexConfigurations),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(0))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultIndexConfigurations.size() == 0", Integer.valueOf(107).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 110, name = "Get index configurations", ordinal = 1, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_1()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[33].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[34].callConstructor(ValueRecorder.class), ValueRecorder.class);
        SnIndexConfiguration indexConfiguration = (SnIndexConfiguration)ScriptBytecodeAdapter.castToType(arrayOfCallSite[35].callConstructor(SnIndexConfiguration.class, ScriptBytecodeAdapter.createMap(new Object[] {"id", INDEX_CONFIGURATION_CODE, "name", INDEX_CONFIGURATION_NAME})),
                        SnIndexConfiguration.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[36].callConstructor(InteractionBuilder.class, Integer.valueOf(114), Integer.valueOf(3), "snIndexConfigurationService.getAllIndexConfigurations() >> List.of(indexConfiguration)"),
                                        InteractionBuilder.class)).addEqualTarget(this.snIndexConfigurationService).addEqualMethodName("getAllIndexConfigurations").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[37].call(List.class, indexConfiguration)).build());
        null;
        List resultIndexConfigurations = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[38].call(this.snAsSearchProvider), List.class);
        try
        {
            SpockRuntime.verifyCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "resultIndexConfigurations != null", Integer.valueOf(120).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexConfigurations),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "resultIndexConfigurations != null", Integer.valueOf(120).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "resultIndexConfigurations.size() == 1", Integer.valueOf(121).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexConfigurations),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(1))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "resultIndexConfigurations.size() == 1", Integer.valueOf(121).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[39].callCurrent((GroovyObject)this, arrayOfCallSite[40].call(resultIndexConfigurations, Integer.valueOf(0)), new __spock_feature_1_1_closure1(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 129, name = "Get non existing index configuration for code", ordinal = 2, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_2()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[41].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[42].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[43].callConstructor(InteractionBuilder.class, Integer.valueOf(132), Integer.valueOf(3), "snIndexConfigurationService.getIndexConfigurationForId(INDEX_CONFIGURATION_CODE) >> Optional.empty()"),
                                        InteractionBuilder.class)).addEqualTarget(this.snIndexConfigurationService).addEqualMethodName("getIndexConfigurationForId").setArgListKind(true, false).addEqualArg(INDEX_CONFIGURATION_CODE).addConstantResponse(arrayOfCallSite[44].call(Optional.class))
                                        .build());
        null;
        Optional resultIndexConfigurationOptional = (Optional)ScriptBytecodeAdapter.castToType(arrayOfCallSite[45].call(this.snAsSearchProvider, INDEX_CONFIGURATION_CODE), Optional.class);
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultIndexConfigurationOptional.isEmpty()", Integer.valueOf(138).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexConfigurationOptional),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isEmpty")), new Object[0],
                            DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(4).intValue(), Boolean.FALSE)), DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(3).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultIndexConfigurationOptional.isEmpty()", Integer.valueOf(138).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 141, name = "Get index configuration for code", ordinal = 3, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_3()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[46].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[47].callConstructor(ValueRecorder.class), ValueRecorder.class);
        SnIndexConfiguration indexConfiguration = (SnIndexConfiguration)ScriptBytecodeAdapter.castToType(arrayOfCallSite[48].callConstructor(SnIndexConfiguration.class, ScriptBytecodeAdapter.createMap(new Object[] {"id", INDEX_CONFIGURATION_CODE, "name", INDEX_CONFIGURATION_NAME})),
                        SnIndexConfiguration.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(((InteractionBuilder)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[49].callConstructor(InteractionBuilder.class, Integer.valueOf(145), Integer.valueOf(3), "snIndexConfigurationService.getIndexConfigurationForId(INDEX_CONFIGURATION_CODE) >> Optional.of(indexConfiguration)"), InteractionBuilder.class)).addEqualTarget(
                        this.snIndexConfigurationService).addEqualMethodName("getIndexConfigurationForId").setArgListKind(true, false).addEqualArg(INDEX_CONFIGURATION_CODE).addConstantResponse(arrayOfCallSite[50].call(Optional.class, indexConfiguration)).build());
        null;
        Optional resultIndexConfigurationOptional = (Optional)ScriptBytecodeAdapter.castToType(arrayOfCallSite[51].call(this.snAsSearchProvider, INDEX_CONFIGURATION_CODE), Optional.class);
        try
        {
            SpockRuntime.verifyMethodCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "resultIndexConfigurationOptional.isPresent()", Integer.valueOf(151).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexConfigurationOptional),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isPresent")), new Object[0],
                            DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(4).intValue(), Boolean.FALSE)), DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(3).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "resultIndexConfigurationOptional.isPresent()", Integer.valueOf(151).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[52].callCurrent((GroovyObject)this, arrayOfCallSite[53].call(resultIndexConfigurationOptional), new __spock_feature_1_3_closure2(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 159, name = "Get empty index types", ordinal = 4, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_4()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[54].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[55].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[56].callConstructor(InteractionBuilder.class, Integer.valueOf(162), Integer.valueOf(3), "snIndexTypeService.getAllIndexTypes() >> List.of()"), InteractionBuilder.class)).addEqualTarget(
                                        this.snIndexTypeService).addEqualMethodName("getAllIndexTypes").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[57].call(List.class)).build());
        null;
        List resultIndexTypes = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[58].call(this.snAsSearchProvider), List.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultIndexTypes != null", Integer.valueOf(168).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexTypes),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultIndexTypes != null", Integer.valueOf(168).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultIndexTypes.size() == 0", Integer.valueOf(169).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexTypes),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(0))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultIndexTypes.size() == 0", Integer.valueOf(169).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 172, name = "Get index types", ordinal = 5, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_5()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[59].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[60].callConstructor(ValueRecorder.class), ValueRecorder.class);
        SnIndexType indexType = (SnIndexType)ScriptBytecodeAdapter.castToType(arrayOfCallSite[61].callConstructor(SnIndexType.class, ScriptBytecodeAdapter.createMap(new Object[] {"id", INDEX_TYPE_CODE, "name", INDEX_TYPE_NAME, "itemComposedType", INDEX_TYPE_ITEM_TYPE})), SnIndexType.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[62].callConstructor(InteractionBuilder.class, Integer.valueOf(176), Integer.valueOf(3), "snIndexTypeService.getAllIndexTypes() >> List.of(indexType)"), InteractionBuilder.class)).addEqualTarget(
                                        this.snIndexTypeService).addEqualMethodName("getAllIndexTypes").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[63].call(List.class, indexType)).build());
        null;
        ComposedTypeModel composedType = (ComposedTypeModel)ScriptBytecodeAdapter.castToType(arrayOfCallSite[64].callCurrent((GroovyObject)this, "composedType", ComposedTypeModel.class), ComposedTypeModel.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[65].callConstructor(InteractionBuilder.class, Integer.valueOf(179), Integer.valueOf(3), "composedType.getCode() >> INDEX_TYPE_ITEM_TYPE"), InteractionBuilder.class)).addEqualTarget(composedType)
                                        .addEqualMethodName("getCode").setArgListKind(true, false).addConstantResponse(INDEX_TYPE_ITEM_TYPE).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[66].callConstructor(InteractionBuilder.class, Integer.valueOf(180), Integer.valueOf(3), "composedType.getCatalogItemType() >> Boolean.TRUE"), InteractionBuilder.class)).addEqualTarget(composedType)
                                        .addEqualMethodName("getCatalogItemType").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[67].callGetProperty(Boolean.class)).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[68].callConstructor(InteractionBuilder.class, Integer.valueOf(182), Integer.valueOf(3), "typeService.getComposedTypeForCode(INDEX_TYPE_ITEM_TYPE) >> composedType"),
                                        InteractionBuilder.class)).addEqualTarget(this.typeService).addEqualMethodName("getComposedTypeForCode").setArgListKind(true, false).addEqualArg(INDEX_TYPE_ITEM_TYPE).addConstantResponse(composedType).build());
        null;
        List resultIndexTypes = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[69].call(this.snAsSearchProvider), List.class);
        try
        {
            SpockRuntime.verifyCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "resultIndexTypes != null", Integer.valueOf(188).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexTypes),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "resultIndexTypes != null", Integer.valueOf(188).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "resultIndexTypes.size() == 1", Integer.valueOf(189).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexTypes),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(1))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "resultIndexTypes.size() == 1", Integer.valueOf(189).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[70].callCurrent((GroovyObject)this, arrayOfCallSite[71].call(resultIndexTypes, Integer.valueOf(0)), new __spock_feature_1_5_closure3(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 199, name = "Get non existing index type for code", ordinal = 6, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_6()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[72].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[73].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[74].callConstructor(InteractionBuilder.class, Integer.valueOf(202), Integer.valueOf(3), "snIndexTypeService.getIndexTypeForId(INDEX_TYPE_CODE) >> Optional.empty()"),
                                        InteractionBuilder.class)).addEqualTarget(this.snIndexTypeService).addEqualMethodName("getIndexTypeForId").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[75].call(Optional.class)).build());
        null;
        Optional resultIndexTypeOptional = (Optional)ScriptBytecodeAdapter.castToType(arrayOfCallSite[76].call(this.snAsSearchProvider, INDEX_TYPE_CODE), Optional.class);
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultIndexTypeOptional.isEmpty()", Integer.valueOf(208).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexTypeOptional), ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isEmpty")),
                            new Object[0], DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(4).intValue(), Boolean.FALSE)), DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(3).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultIndexTypeOptional.isEmpty()", Integer.valueOf(208).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 211, name = "Get index type for code", ordinal = 7, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_7()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[77].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[78].callConstructor(ValueRecorder.class), ValueRecorder.class);
        SnIndexType indexType = (SnIndexType)ScriptBytecodeAdapter.castToType(arrayOfCallSite[79].callConstructor(SnIndexType.class, ScriptBytecodeAdapter.createMap(new Object[] {"id", INDEX_TYPE_CODE, "name", INDEX_TYPE_NAME, "itemComposedType", INDEX_TYPE_ITEM_TYPE})), SnIndexType.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[80].callConstructor(InteractionBuilder.class, Integer.valueOf(215), Integer.valueOf(3), "snIndexTypeService.getIndexTypeForId(INDEX_TYPE_CODE) >> Optional.of(indexType)"),
                                        InteractionBuilder.class)).addEqualTarget(this.snIndexTypeService).addEqualMethodName("getIndexTypeForId").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[81].call(Optional.class, indexType)).build());
        null;
        ComposedTypeModel composedType = (ComposedTypeModel)ScriptBytecodeAdapter.castToType(arrayOfCallSite[82].callCurrent((GroovyObject)this, "composedType", ComposedTypeModel.class), ComposedTypeModel.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[83].callConstructor(InteractionBuilder.class, Integer.valueOf(218), Integer.valueOf(3), "composedType.getCode() >> INDEX_TYPE_ITEM_TYPE"), InteractionBuilder.class)).addEqualTarget(composedType)
                                        .addEqualMethodName("getCode").setArgListKind(true, false).addConstantResponse(INDEX_TYPE_ITEM_TYPE).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[84].callConstructor(InteractionBuilder.class, Integer.valueOf(219), Integer.valueOf(3), "composedType.getCatalogItemType() >> Boolean.TRUE"), InteractionBuilder.class)).addEqualTarget(composedType)
                                        .addEqualMethodName("getCatalogItemType").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[85].callGetProperty(Boolean.class)).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[86].callConstructor(InteractionBuilder.class, Integer.valueOf(221), Integer.valueOf(3), "typeService.getComposedTypeForCode(INDEX_TYPE_ITEM_TYPE) >> composedType"),
                                        InteractionBuilder.class)).addEqualTarget(this.typeService).addEqualMethodName("getComposedTypeForCode").setArgListKind(true, false).addEqualArg(INDEX_TYPE_ITEM_TYPE).addConstantResponse(composedType).build());
        null;
        Optional resultIndexTypeOptional = (Optional)ScriptBytecodeAdapter.castToType(arrayOfCallSite[87].call(this.snAsSearchProvider, INDEX_TYPE_CODE), Optional.class);
        try
        {
            SpockRuntime.verifyMethodCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "resultIndexTypeOptional.isPresent()", Integer.valueOf(227).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexTypeOptional),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isPresent")), new Object[0],
                            DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(4).intValue(), Boolean.FALSE)), DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(3).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "resultIndexTypeOptional.isPresent()", Integer.valueOf(227).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[88].callCurrent((GroovyObject)this, arrayOfCallSite[89].call(resultIndexTypeOptional), new __spock_feature_1_7_closure4(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 237, name = "Get empty index properties", ordinal = 8, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_8()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[90].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[91].callConstructor(ValueRecorder.class), ValueRecorder.class);
        SnIndexType indexType = (SnIndexType)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[92].callConstructor(SnIndexType.class, ScriptBytecodeAdapter.createMap(new Object[] {"id", INDEX_TYPE_CODE, "name", INDEX_TYPE_NAME, "itemComposedType", INDEX_TYPE_ITEM_TYPE, "fields", arrayOfCallSite[93].call(Map.class)})), SnIndexType.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[94].callConstructor(InteractionBuilder.class, Integer.valueOf(241), Integer.valueOf(3), "snIndexTypeService.getIndexTypeForId(INDEX_TYPE_CODE) >> Optional.of(indexType)"),
                                        InteractionBuilder.class)).addEqualTarget(this.snIndexTypeService).addEqualMethodName("getIndexTypeForId").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[95].call(Optional.class, indexType)).build());
        null;
        ComposedTypeModel composedType = (ComposedTypeModel)ScriptBytecodeAdapter.castToType(arrayOfCallSite[96].callCurrent((GroovyObject)this, "composedType", ComposedTypeModel.class), ComposedTypeModel.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[97].callConstructor(InteractionBuilder.class, Integer.valueOf(244), Integer.valueOf(3), "composedType.getCode() >> INDEX_TYPE_ITEM_TYPE"), InteractionBuilder.class)).addEqualTarget(composedType)
                                        .addEqualMethodName("getCode").setArgListKind(true, false).addConstantResponse(INDEX_TYPE_ITEM_TYPE).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[98].callConstructor(InteractionBuilder.class, Integer.valueOf(245), Integer.valueOf(3), "composedType.getCatalogItemType() >> Boolean.TRUE"), InteractionBuilder.class)).addEqualTarget(composedType)
                                        .addEqualMethodName("getCatalogItemType").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[99].callGetProperty(Boolean.class)).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[100].callConstructor(InteractionBuilder.class, Integer.valueOf(247), Integer.valueOf(3), "typeService.getComposedTypeForCode(INDEX_TYPE_ITEM_TYPE) >> composedType"),
                                        InteractionBuilder.class)).addEqualTarget(this.typeService).addEqualMethodName("getComposedTypeForCode").setArgListKind(true, false).addEqualArg(INDEX_TYPE_ITEM_TYPE).addConstantResponse(composedType).build());
        null;
        List resultIndexProperties = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[101].call(this.snAsSearchProvider, INDEX_TYPE_CODE), List.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultIndexProperties != null", Integer.valueOf(253).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexProperties),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultIndexProperties != null", Integer.valueOf(253).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultIndexProperties.size() == 0", Integer.valueOf(254).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexProperties),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(0))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultIndexProperties.size() == 0", Integer.valueOf(254).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 257, name = "Get index properties", ordinal = 9, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_9()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[102].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[103].callConstructor(ValueRecorder.class), ValueRecorder.class);
        SnField field = (SnField)ScriptBytecodeAdapter.castToType(arrayOfCallSite[104].callConstructor(SnField.class, ScriptBytecodeAdapter.createMap(new Object[] {"id", INDEX_PROPERTY_CODE, "name", INDEX_PROPERTY_NAME, "fieldType", arrayOfCallSite[105].callGetProperty(SnFieldType.class)})),
                        SnField.class);
        SnIndexType indexType = (SnIndexType)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[106].callConstructor(SnIndexType.class, ScriptBytecodeAdapter.createMap(new Object[] {"id", INDEX_TYPE_CODE, "name", INDEX_TYPE_NAME, "itemComposedType", INDEX_TYPE_ITEM_TYPE, "fields", arrayOfCallSite[107].call(Map.class, INDEX_PROPERTY_CODE, field)})),
                        SnIndexType.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[108].callConstructor(InteractionBuilder.class, Integer.valueOf(262), Integer.valueOf(3), "snIndexTypeService.getIndexTypeForId(INDEX_TYPE_CODE) >> Optional.of(indexType)"),
                                        InteractionBuilder.class)).addEqualTarget(this.snIndexTypeService).addEqualMethodName("getIndexTypeForId").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[109].call(Optional.class, indexType)).build());
        null;
        ComposedTypeModel composedType = (ComposedTypeModel)ScriptBytecodeAdapter.castToType(arrayOfCallSite[110].callCurrent((GroovyObject)this, "composedType", ComposedTypeModel.class), ComposedTypeModel.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[111].callConstructor(InteractionBuilder.class, Integer.valueOf(265), Integer.valueOf(3), "composedType.getCode() >> INDEX_TYPE_ITEM_TYPE"), InteractionBuilder.class)).addEqualTarget(composedType)
                                        .addEqualMethodName("getCode").setArgListKind(true, false).addConstantResponse(INDEX_TYPE_ITEM_TYPE).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[112].callConstructor(InteractionBuilder.class, Integer.valueOf(266), Integer.valueOf(3), "composedType.getCatalogItemType() >> Boolean.TRUE"), InteractionBuilder.class)).addEqualTarget(composedType)
                                        .addEqualMethodName("getCatalogItemType").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[113].callGetProperty(Boolean.class)).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[114].callConstructor(InteractionBuilder.class, Integer.valueOf(268), Integer.valueOf(3), "typeService.getComposedTypeForCode(INDEX_TYPE_ITEM_TYPE) >> composedType"),
                                        InteractionBuilder.class)).addEqualTarget(this.typeService).addEqualMethodName("getComposedTypeForCode").setArgListKind(true, false).addEqualArg(INDEX_TYPE_ITEM_TYPE).addConstantResponse(composedType).build());
        null;
        SnFieldTypeInfo fieldTypeInfo = (SnFieldTypeInfo)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[115].callConstructor(SnFieldTypeInfo.class, ScriptBytecodeAdapter.createMap(new Object[] {"valueType", String.class, "supportedQueryTypes", arrayOfCallSite[116].call(List.class, arrayOfCallSite[117].callGetProperty(SnMatchQuery.class))})),
                        SnFieldTypeInfo.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[118].callConstructor(InteractionBuilder.class, Integer.valueOf(272), Integer.valueOf(3), "snFieldTypeRegistry.getFieldTypeInfo(SnFieldType.STRING) >> fieldTypeInfo"),
                                        InteractionBuilder.class)).addEqualTarget(this.snFieldTypeRegistry).addEqualMethodName("getFieldTypeInfo").setArgListKind(true, false).addEqualArg(arrayOfCallSite[119].callGetProperty(SnFieldType.class)).addConstantResponse(fieldTypeInfo).build());
        null;
        List resultIndexProperties = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[120].call(this.snAsSearchProvider, INDEX_TYPE_CODE), List.class);
        try
        {
            SpockRuntime.verifyCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "resultIndexProperties != null", Integer.valueOf(278).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexProperties),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "resultIndexProperties != null", Integer.valueOf(278).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "resultIndexProperties.size() == 1", Integer.valueOf(279).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexProperties),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(1))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "resultIndexProperties.size() == 1", Integer.valueOf(279).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[121].callCurrent((GroovyObject)this, arrayOfCallSite[122].call(resultIndexProperties, Integer.valueOf(0)), new __spock_feature_1_9_closure5(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 289, name = "Get non existing index property for code", ordinal = 10, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_10()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[123].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[124].callConstructor(ValueRecorder.class), ValueRecorder.class);
        SnIndexType indexType = (SnIndexType)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[125].callConstructor(SnIndexType.class, ScriptBytecodeAdapter.createMap(new Object[] {"id", INDEX_TYPE_CODE, "name", INDEX_TYPE_NAME, "itemComposedType", INDEX_TYPE_ITEM_TYPE, "fields", arrayOfCallSite[126].call(Map.class)})), SnIndexType.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[127].callConstructor(InteractionBuilder.class, Integer.valueOf(293), Integer.valueOf(3), "snIndexTypeService.getIndexTypeForId(INDEX_TYPE_CODE) >> Optional.of(indexType)"),
                                        InteractionBuilder.class)).addEqualTarget(this.snIndexTypeService).addEqualMethodName("getIndexTypeForId").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[128].call(Optional.class, indexType)).build());
        null;
        ComposedTypeModel composedType = (ComposedTypeModel)ScriptBytecodeAdapter.castToType(arrayOfCallSite[129].callCurrent((GroovyObject)this, "composedType", ComposedTypeModel.class), ComposedTypeModel.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[130].callConstructor(InteractionBuilder.class, Integer.valueOf(296), Integer.valueOf(3), "composedType.getCode() >> INDEX_TYPE_ITEM_TYPE"), InteractionBuilder.class)).addEqualTarget(composedType)
                                        .addEqualMethodName("getCode").setArgListKind(true, false).addConstantResponse(INDEX_TYPE_ITEM_TYPE).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[131].callConstructor(InteractionBuilder.class, Integer.valueOf(297), Integer.valueOf(3), "composedType.getCatalogItemType() >> Boolean.TRUE"), InteractionBuilder.class)).addEqualTarget(composedType)
                                        .addEqualMethodName("getCatalogItemType").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[132].callGetProperty(Boolean.class)).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[133].callConstructor(InteractionBuilder.class, Integer.valueOf(299), Integer.valueOf(3), "typeService.getComposedTypeForCode(INDEX_TYPE_ITEM_TYPE) >> composedType"),
                                        InteractionBuilder.class)).addEqualTarget(this.typeService).addEqualMethodName("getComposedTypeForCode").setArgListKind(true, false).addEqualArg(INDEX_TYPE_ITEM_TYPE).addConstantResponse(composedType).build());
        null;
        Optional resultIndexPropertyOptional = (Optional)ScriptBytecodeAdapter.castToType(arrayOfCallSite[134].call(this.snAsSearchProvider, INDEX_TYPE_CODE, INDEX_PROPERTY_CODE), Optional.class);
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultIndexPropertyOptional.isEmpty()", Integer.valueOf(305).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexPropertyOptional),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isEmpty")), new Object[0],
                            DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(4).intValue(), Boolean.FALSE)), DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(3).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultIndexPropertyOptional.isEmpty()", Integer.valueOf(305).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 308, name = "Get index property for code", ordinal = 11, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_11()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[135].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[136].callConstructor(ValueRecorder.class), ValueRecorder.class);
        SnField field = (SnField)ScriptBytecodeAdapter.castToType(arrayOfCallSite[137].callConstructor(SnField.class, ScriptBytecodeAdapter.createMap(new Object[] {"id", INDEX_PROPERTY_CODE, "name", INDEX_PROPERTY_NAME, "fieldType", arrayOfCallSite[138].callGetProperty(SnFieldType.class)})),
                        SnField.class);
        SnIndexType indexType = (SnIndexType)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[139].callConstructor(SnIndexType.class, ScriptBytecodeAdapter.createMap(new Object[] {"id", INDEX_TYPE_CODE, "name", INDEX_TYPE_NAME, "itemComposedType", INDEX_TYPE_ITEM_TYPE, "fields", arrayOfCallSite[140].call(Map.class, INDEX_PROPERTY_CODE, field)})),
                        SnIndexType.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[141].callConstructor(InteractionBuilder.class, Integer.valueOf(313), Integer.valueOf(3), "snIndexTypeService.getIndexTypeForId(INDEX_TYPE_CODE) >> Optional.of(indexType)"),
                                        InteractionBuilder.class)).addEqualTarget(this.snIndexTypeService).addEqualMethodName("getIndexTypeForId").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[142].call(Optional.class, indexType)).build());
        null;
        ComposedTypeModel composedType = (ComposedTypeModel)ScriptBytecodeAdapter.castToType(arrayOfCallSite[143].callCurrent((GroovyObject)this, "composedType", ComposedTypeModel.class), ComposedTypeModel.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[144].callConstructor(InteractionBuilder.class, Integer.valueOf(316), Integer.valueOf(3), "composedType.getCode() >> INDEX_TYPE_ITEM_TYPE"), InteractionBuilder.class)).addEqualTarget(composedType)
                                        .addEqualMethodName("getCode").setArgListKind(true, false).addConstantResponse(INDEX_TYPE_ITEM_TYPE).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[145].callConstructor(InteractionBuilder.class, Integer.valueOf(317), Integer.valueOf(3), "composedType.getCatalogItemType() >> Boolean.TRUE"), InteractionBuilder.class)).addEqualTarget(composedType)
                                        .addEqualMethodName("getCatalogItemType").setArgListKind(true, false).addConstantResponse(arrayOfCallSite[146].callGetProperty(Boolean.class)).build());
        null;
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[147].callConstructor(InteractionBuilder.class, Integer.valueOf(319), Integer.valueOf(3), "typeService.getComposedTypeForCode(INDEX_TYPE_ITEM_TYPE) >> composedType"),
                                        InteractionBuilder.class)).addEqualTarget(this.typeService).addEqualMethodName("getComposedTypeForCode").setArgListKind(true, false).addEqualArg(INDEX_TYPE_ITEM_TYPE).addConstantResponse(composedType).build());
        null;
        SnFieldTypeInfo fieldTypeInfo = (SnFieldTypeInfo)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[148].callConstructor(SnFieldTypeInfo.class, ScriptBytecodeAdapter.createMap(new Object[] {"valueType", String.class, "supportedQueryTypes", arrayOfCallSite[149].call(List.class, arrayOfCallSite[150].callGetProperty(SnMatchQuery.class))})),
                        SnFieldTypeInfo.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[151].callConstructor(InteractionBuilder.class, Integer.valueOf(323), Integer.valueOf(3), "snFieldTypeRegistry.getFieldTypeInfo(SnFieldType.STRING) >> fieldTypeInfo"),
                                        InteractionBuilder.class)).addEqualTarget(this.snFieldTypeRegistry).addEqualMethodName("getFieldTypeInfo").setArgListKind(true, false).addEqualArg(arrayOfCallSite[152].callGetProperty(SnFieldType.class)).addConstantResponse(fieldTypeInfo).build());
        null;
        Optional resultIndexPropertyOptional = (Optional)ScriptBytecodeAdapter.castToType(arrayOfCallSite[153].call(this.snAsSearchProvider, INDEX_TYPE_CODE, INDEX_PROPERTY_CODE), Optional.class);
        try
        {
            SpockRuntime.verifyMethodCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "resultIndexPropertyOptional.isPresent()", Integer.valueOf(329).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultIndexPropertyOptional),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isPresent")), new Object[0],
                            DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(4).intValue(), Boolean.FALSE)), DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(3).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "resultIndexPropertyOptional.isPresent()", Integer.valueOf(329).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[154].callCurrent((GroovyObject)this, arrayOfCallSite[155].call(resultIndexPropertyOptional), new __spock_feature_1_11_closure6(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 339, name = "Get empty supported catalog versions", ordinal = 12, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_12()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[156].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[157].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[158].callConstructor(InteractionBuilder.class, Integer.valueOf(342), Integer.valueOf(3), "snCommonConfigurationService.getCatalogVersions(INDEX_TYPE_CODE) >> List.of()"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("getCatalogVersions").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[159].call(List.class)).build());
        null;
        List resultCatalogVersions = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[160].call(this.snAsSearchProvider, INDEX_CONFIGURATION_CODE, INDEX_TYPE_CODE), List.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultCatalogVersions != null", Integer.valueOf(348).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultCatalogVersions),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultCatalogVersions != null", Integer.valueOf(348).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultCatalogVersions.size() == 0", Integer.valueOf(349).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultCatalogVersions),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(0))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultCatalogVersions.size() == 0", Integer.valueOf(349).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 352, name = "Get supported catalog versions", ordinal = 13, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_13()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[161].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[162].callConstructor(ValueRecorder.class), ValueRecorder.class);
        CatalogVersionModel catalogVersion = (CatalogVersionModel)ScriptBytecodeAdapter.castToType(arrayOfCallSite[163].callCurrent((GroovyObject)this, "catalogVersion", CatalogVersionModel.class), CatalogVersionModel.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[164].callConstructor(InteractionBuilder.class, Integer.valueOf(356), Integer.valueOf(3), "snCommonConfigurationService.getCatalogVersions(INDEX_TYPE_CODE) >> List.of(catalogVersion)"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("getCatalogVersions").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[165].call(List.class, catalogVersion)).build());
        null;
        List resultCatalogVersions = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[166].call(this.snAsSearchProvider, INDEX_CONFIGURATION_CODE, INDEX_TYPE_CODE), List.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultCatalogVersions != null", Integer.valueOf(362).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultCatalogVersions),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultCatalogVersions != null", Integer.valueOf(362).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultCatalogVersions.size() == 1", Integer.valueOf(363).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultCatalogVersions),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(1))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultCatalogVersions.size() == 1", Integer.valueOf(363).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultCatalogVersions[0].is(catalogVersion)", Integer.valueOf(364).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()), arrayOfCallSite[167].call($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultCatalogVersions),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), Integer.valueOf(0)))),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()), "is")), new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), catalogVersion)}
                            DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(7).intValue(), Boolean.FALSE)), DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(6).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultCatalogVersions[0].is(catalogVersion)", Integer.valueOf(364).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 367, name = "Get empty supported languages", ordinal = 14, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_14()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[168].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[169].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[170].callConstructor(InteractionBuilder.class, Integer.valueOf(370), Integer.valueOf(3), "snCommonConfigurationService.getLanguages(INDEX_TYPE_CODE) >> List.of()"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("getLanguages").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[171].call(List.class)).build());
        null;
        List resultLanguages = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[172].call(this.snAsSearchProvider, INDEX_CONFIGURATION_CODE, INDEX_TYPE_CODE), List.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultLanguages != null", Integer.valueOf(376).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultLanguages),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultLanguages != null", Integer.valueOf(376).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultLanguages.size() == 0", Integer.valueOf(377).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultLanguages),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(0))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultLanguages.size() == 0", Integer.valueOf(377).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 380, name = "Get supported languages", ordinal = 15, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_15()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[173].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[174].callConstructor(ValueRecorder.class), ValueRecorder.class);
        CatalogVersionModel language = (CatalogVersionModel)ScriptBytecodeAdapter.castToType(arrayOfCallSite[175].callCurrent((GroovyObject)this, "language", CatalogVersionModel.class), CatalogVersionModel.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[176].callConstructor(InteractionBuilder.class, Integer.valueOf(384), Integer.valueOf(3), "snCommonConfigurationService.getLanguages(INDEX_TYPE_CODE) >> List.of(language)"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("getLanguages").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[177].call(List.class, language)).build());
        null;
        List resultLanguages = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[178].call(this.snAsSearchProvider, INDEX_CONFIGURATION_CODE, INDEX_TYPE_CODE), List.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultLanguages != null", Integer.valueOf(390).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultLanguages),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultLanguages != null", Integer.valueOf(390).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultLanguages.size() == 1", Integer.valueOf(391).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultLanguages),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(1))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultLanguages.size() == 1", Integer.valueOf(391).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultLanguages[0].is(language)", Integer.valueOf(392).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()), arrayOfCallSite[179].call($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultLanguages),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), Integer.valueOf(0)))),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()), "is")), new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), language)}
                            DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(7).intValue(), Boolean.FALSE)), DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(6).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultLanguages[0].is(language)", Integer.valueOf(392).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 395, name = "Get empty supported currencies", ordinal = 16, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_16()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[180].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[181].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[182].callConstructor(InteractionBuilder.class, Integer.valueOf(398), Integer.valueOf(3), "snCommonConfigurationService.getCurrencies(INDEX_TYPE_CODE) >> List.of()"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("getCurrencies").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[183].call(List.class)).build());
        null;
        List resultCurrencies = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[184].call(this.snAsSearchProvider, INDEX_CONFIGURATION_CODE, INDEX_TYPE_CODE), List.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultCurrencies != null", Integer.valueOf(404).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultCurrencies),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultCurrencies != null", Integer.valueOf(404).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultCurrencies.size() == 0", Integer.valueOf(405).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultCurrencies),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(0))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultCurrencies.size() == 0", Integer.valueOf(405).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 408, name = "Get supported currencies", ordinal = 17, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_17()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[185].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[186].callConstructor(ValueRecorder.class), ValueRecorder.class);
        CurrencyModel currency = (CurrencyModel)ScriptBytecodeAdapter.castToType(arrayOfCallSite[187].callCurrent((GroovyObject)this, "currency", CurrencyModel.class), CurrencyModel.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[188].callConstructor(InteractionBuilder.class, Integer.valueOf(412), Integer.valueOf(3), "snCommonConfigurationService.getCurrencies(INDEX_TYPE_CODE) >> List.of(currency)"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("getCurrencies").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[189].call(List.class, currency)).build());
        null;
        List resultCurrencies = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[190].call(this.snAsSearchProvider, INDEX_CONFIGURATION_CODE, INDEX_TYPE_CODE), List.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultCurrencies != null", Integer.valueOf(418).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultCurrencies),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultCurrencies != null", Integer.valueOf(418).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultCurrencies.size() == 1", Integer.valueOf(419).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultCurrencies),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(1))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultCurrencies.size() == 1", Integer.valueOf(419).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "resultCurrencies[0].is(currency)", Integer.valueOf(420).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()), arrayOfCallSite[191].call($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultCurrencies),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), Integer.valueOf(0)))),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()), "is")), new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), currency)}
                            DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(7).intValue(), Boolean.FALSE)), DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(6).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "resultCurrencies[0].is(currency)", Integer.valueOf(420).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 423, name = "Get empty supported facet expressions", ordinal = 18, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_18()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[192].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[193].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[194].callConstructor(InteractionBuilder.class, Integer.valueOf(426), Integer.valueOf(3), "snCommonConfigurationService.getFacetExpressions(INDEX_TYPE_CODE) >> List.of()"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("getFacetExpressions").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[195].call(List.class)).build());
        null;
        List expressionInfo = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[196].call(this.snAsSearchProvider, INDEX_TYPE_CODE), List.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "expressionInfo != null", Integer.valueOf(432).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), expressionInfo),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "expressionInfo != null", Integer.valueOf(432).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "expressionInfo.size() == 0", Integer.valueOf(433).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), expressionInfo),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(0))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "expressionInfo.size() == 0", Integer.valueOf(433).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 436, name = "Get supported facet expressions", ordinal = 19, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_19()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[197].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[198].callConstructor(ValueRecorder.class), ValueRecorder.class);
        SnExpressionInfo expressionInfo = (SnExpressionInfo)ScriptBytecodeAdapter.castToType(arrayOfCallSite[199].callConstructor(SnExpressionInfo.class, ScriptBytecodeAdapter.createMap(new Object[] {"expression", EXPRESSION_EXPRESSION, "name", EXPRESSION_NAME})), SnExpressionInfo.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[200].callConstructor(InteractionBuilder.class, Integer.valueOf(440), Integer.valueOf(3), "snCommonConfigurationService.getFacetExpressions(INDEX_TYPE_CODE) >> List.of(expressionInfo)"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("getFacetExpressions").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[201].call(List.class, expressionInfo)).build());
        null;
        List resultExpressions = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[202].call(this.snAsSearchProvider, INDEX_TYPE_CODE), List.class);
        try
        {
            SpockRuntime.verifyCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "resultExpressions != null", Integer.valueOf(446).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultExpressions),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "resultExpressions != null", Integer.valueOf(446).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "resultExpressions.size() == 1", Integer.valueOf(447).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultExpressions),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(1))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "resultExpressions.size() == 1", Integer.valueOf(447).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[203].callCurrent((GroovyObject)this, arrayOfCallSite[204].call(resultExpressions, Integer.valueOf(0)), new __spock_feature_1_19_closure7(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 455, name = "Is valid facet expression", ordinal = 20, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_20()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[205].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[206].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[207].callConstructor(InteractionBuilder.class, Integer.valueOf(458), Integer.valueOf(3), "snCommonConfigurationService.isValidFacetExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION) >> true"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("isValidFacetExpression").setArgListKind(true, false).addEqualArg(INDEX_PROPERTY_CODE).addEqualArg(EXPRESSION_EXPRESSION).addConstantResponse(Boolean.valueOf(true))
                                        .build());
        null;
        boolean result = DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[208].call(this.snAsSearchProvider, INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION));
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "result == true", Integer.valueOf(464).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Boolean.valueOf(result)),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), Boolean.valueOf(true))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "result == true", Integer.valueOf(464).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 467, name = "Is invalid facet expression", ordinal = 21, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_21()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[209].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[210].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[211].callConstructor(InteractionBuilder.class, Integer.valueOf(470), Integer.valueOf(3), "snCommonConfigurationService.isValidFacetExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION) >> false"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("isValidFacetExpression").setArgListKind(true, false).addEqualArg(INDEX_PROPERTY_CODE).addEqualArg(EXPRESSION_EXPRESSION)
                                        .addConstantResponse(Boolean.valueOf(false)).build());
        null;
        boolean result = DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[212].call(this.snAsSearchProvider, INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION));
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "result == false", Integer.valueOf(476).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Boolean.valueOf(result)),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), Boolean.valueOf(false))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "result == false", Integer.valueOf(476).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 479, name = "Get empty supported sort expressions", ordinal = 22, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_22()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[213].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[214].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[215].callConstructor(InteractionBuilder.class, Integer.valueOf(482), Integer.valueOf(3), "snCommonConfigurationService.getSortExpressions(INDEX_TYPE_CODE) >> List.of()"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("getSortExpressions").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[216].call(List.class)).build());
        null;
        List expressionInfo = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[217].call(this.snAsSearchProvider, INDEX_TYPE_CODE), List.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "expressionInfo != null", Integer.valueOf(488).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), expressionInfo),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "expressionInfo != null", Integer.valueOf(488).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "expressionInfo.size() == 0", Integer.valueOf(489).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), expressionInfo),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(0))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "expressionInfo.size() == 0", Integer.valueOf(489).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 492, name = "Get supported sort expressions", ordinal = 23, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_23()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[218].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[219].callConstructor(ValueRecorder.class), ValueRecorder.class);
        SnExpressionInfo expressionInfo = (SnExpressionInfo)ScriptBytecodeAdapter.castToType(arrayOfCallSite[220].callConstructor(SnExpressionInfo.class, ScriptBytecodeAdapter.createMap(new Object[] {"expression", EXPRESSION_EXPRESSION, "name", EXPRESSION_NAME})), SnExpressionInfo.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[221].callConstructor(InteractionBuilder.class, Integer.valueOf(496), Integer.valueOf(3), "snCommonConfigurationService.getSortExpressions(INDEX_TYPE_CODE) >> List.of(expressionInfo)"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("getSortExpressions").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[222].call(List.class, expressionInfo)).build());
        null;
        List resultExpressions = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[223].call(this.snAsSearchProvider, INDEX_TYPE_CODE), List.class);
        try
        {
            SpockRuntime.verifyCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "resultExpressions != null", Integer.valueOf(502).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultExpressions),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "resultExpressions != null", Integer.valueOf(502).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "resultExpressions.size() == 1", Integer.valueOf(503).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultExpressions),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(1))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "resultExpressions.size() == 1", Integer.valueOf(503).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[224].callCurrent((GroovyObject)this, arrayOfCallSite[225].call(resultExpressions, Integer.valueOf(0)), new __spock_feature_1_23_closure8(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 511, name = "Is valid sort expression", ordinal = 24, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_24()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[226].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[227].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[228].callConstructor(InteractionBuilder.class, Integer.valueOf(514), Integer.valueOf(3), "snCommonConfigurationService.isValidSortExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION) >> true"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("isValidSortExpression").setArgListKind(true, false).addEqualArg(INDEX_PROPERTY_CODE).addEqualArg(EXPRESSION_EXPRESSION).addConstantResponse(Boolean.valueOf(true))
                                        .build());
        null;
        boolean result = DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[229].call(this.snAsSearchProvider, INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION));
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "result == true", Integer.valueOf(520).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Boolean.valueOf(result)),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), Boolean.valueOf(true))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "result == true", Integer.valueOf(520).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 523, name = "Is invalid sort expression", ordinal = 25, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_25()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[230].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[231].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[232].callConstructor(InteractionBuilder.class, Integer.valueOf(526), Integer.valueOf(3), "snCommonConfigurationService.isValidSortExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION) >> false"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("isValidSortExpression").setArgListKind(true, false).addEqualArg(INDEX_PROPERTY_CODE).addEqualArg(EXPRESSION_EXPRESSION).addConstantResponse(Boolean.valueOf(false))
                                        .build());
        null;
        boolean result = DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[233].call(this.snAsSearchProvider, INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION));
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "result == false", Integer.valueOf(532).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Boolean.valueOf(result)),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), Boolean.valueOf(false))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "result == false", Integer.valueOf(532).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 535, name = "Get empty supported group expressions", ordinal = 26, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_26()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[234].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[235].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[236].callConstructor(InteractionBuilder.class, Integer.valueOf(538), Integer.valueOf(3), "snCommonConfigurationService.getGroupExpressions(INDEX_TYPE_CODE) >> List.of()"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("getGroupExpressions").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[237].call(List.class)).build());
        null;
        List expressionInfo = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[238].call(this.snAsSearchProvider, INDEX_TYPE_CODE), List.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "expressionInfo != null", Integer.valueOf(544).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), expressionInfo),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "expressionInfo != null", Integer.valueOf(544).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "expressionInfo.size() == 0", Integer.valueOf(545).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), expressionInfo),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(0))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "expressionInfo.size() == 0", Integer.valueOf(545).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 548, name = "Get supported group expressions", ordinal = 27, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_27()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[239].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[240].callConstructor(ValueRecorder.class), ValueRecorder.class);
        SnExpressionInfo expressionInfo = (SnExpressionInfo)ScriptBytecodeAdapter.castToType(arrayOfCallSite[241].callConstructor(SnExpressionInfo.class, ScriptBytecodeAdapter.createMap(new Object[] {"expression", EXPRESSION_EXPRESSION, "name", EXPRESSION_NAME})), SnExpressionInfo.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[242].callConstructor(InteractionBuilder.class, Integer.valueOf(552), Integer.valueOf(3), "snCommonConfigurationService.getGroupExpressions(INDEX_TYPE_CODE) >> List.of(expressionInfo)"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("getGroupExpressions").setArgListKind(true, false).addEqualArg(INDEX_TYPE_CODE).addConstantResponse(arrayOfCallSite[243].call(List.class, expressionInfo)).build());
        null;
        List resultExpressions = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[244].call(this.snAsSearchProvider, INDEX_TYPE_CODE), List.class);
        try
        {
            SpockRuntime.verifyCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "resultExpressions != null", Integer.valueOf(558).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                                            Boolean.valueOf(ScriptBytecodeAdapter.compareNotEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultExpressions),
                                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), null)))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "resultExpressions != null", Integer.valueOf(558).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder.reset(), "resultExpressions.size() == 1", Integer.valueOf(559).intValue(), Integer.valueOf(3).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(SnAsSearchProviderSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), resultExpressions),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Integer.valueOf(1))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException((ErrorCollector)$spock_errorCollector.get(), $spock_valueRecorder, "resultExpressions.size() == 1", Integer.valueOf(559).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[245].callCurrent((GroovyObject)this, arrayOfCallSite[246].call(resultExpressions, Integer.valueOf(0)), new __spock_feature_1_27_closure9(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 567, name = "Is valid group expression", ordinal = 28, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_28()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[247].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[248].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[249].callConstructor(InteractionBuilder.class, Integer.valueOf(570), Integer.valueOf(3), "snCommonConfigurationService.isValidGroupExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION) >> true"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("isValidGroupExpression").setArgListKind(true, false).addEqualArg(INDEX_PROPERTY_CODE).addEqualArg(EXPRESSION_EXPRESSION).addConstantResponse(Boolean.valueOf(true))
                                        .build());
        null;
        boolean result = DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[250].call(this.snAsSearchProvider, INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION));
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "result == true", Integer.valueOf(576).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Boolean.valueOf(result)),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), Boolean.valueOf(true))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "result == true", Integer.valueOf(576).intValue(), Integer.valueOf(3).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 579, name = "Is invalid group expression", ordinal = 29, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_1_29()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[251].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[252].callConstructor(ValueRecorder.class), ValueRecorder.class);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).addInteraction(
                        ((InteractionBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[253].callConstructor(InteractionBuilder.class, Integer.valueOf(582), Integer.valueOf(3), "snCommonConfigurationService.isValidGroupExpression(INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION) >> false"),
                                        InteractionBuilder.class)).addEqualTarget(this.snCommonConfigurationService).addEqualMethodName("isValidGroupExpression").setArgListKind(true, false).addEqualArg(INDEX_PROPERTY_CODE).addEqualArg(EXPRESSION_EXPRESSION)
                                        .addConstantResponse(Boolean.valueOf(false)).build());
        null;
        boolean result = DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[254].call(this.snAsSearchProvider, INDEX_PROPERTY_CODE, EXPRESSION_EXPRESSION));
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "result == false", Integer.valueOf(588).intValue(), Integer.valueOf(3).intValue(), null, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(2).intValue()),
                            Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), Boolean.valueOf(result)),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), Boolean.valueOf(false))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "result == false", Integer.valueOf(588).intValue(), Integer.valueOf(3).intValue(), null, throwable);
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
        Map<Locale, String> map1 = ScriptBytecodeAdapter.createMap(new Object[] {$getCallSiteArray()[255].callGetProperty(Locale.class), INDEX_CONFIGURATION_NAME_EN});
        INDEX_CONFIGURATION_NAME = map1;
        Map<Locale, String> map2 = ScriptBytecodeAdapter.createMap(new Object[] {$getCallSiteArray()[256].callGetProperty(Locale.class), INDEX_TYPE_NAME_EN});
        INDEX_TYPE_NAME = map2;
        Map<Locale, String> map3 = ScriptBytecodeAdapter.createMap(new Object[] {$getCallSiteArray()[257].callGetProperty(Locale.class), INDEX_PROPERTY_NAME_EN});
        INDEX_PROPERTY_NAME = map3;
        Map<Locale, String> map4 = ScriptBytecodeAdapter.createMap(new Object[] {$getCallSiteArray()[258].callGetProperty(Locale.class), EXPRESSION_NAME_EN});
        EXPRESSION_NAME = map4;
    }

    @Generated
    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Generated
    public void setTypeService(TypeService paramTypeService)
    {
        this.typeService = paramTypeService;
    }


    @Generated
    public I18NService getI18nService()
    {
        return this.i18nService;
    }


    @Generated
    public void setI18nService(I18NService paramI18NService)
    {
        this.i18nService = paramI18NService;
    }


    @Generated
    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Generated
    public void setCommonI18NService(CommonI18NService paramCommonI18NService)
    {
        this.commonI18NService = paramCommonI18NService;
    }


    @Generated
    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Generated
    public void setCatalogVersionService(CatalogVersionService paramCatalogVersionService)
    {
        this.catalogVersionService = paramCatalogVersionService;
    }


    @Generated
    public SnSessionService getSnSessionService()
    {
        return this.snSessionService;
    }


    @Generated
    public void setSnSessionService(SnSessionService paramSnSessionService)
    {
        this.snSessionService = paramSnSessionService;
    }


    @Generated
    public SnFieldTypeRegistry getSnFieldTypeRegistry()
    {
        return this.snFieldTypeRegistry;
    }


    @Generated
    public void setSnFieldTypeRegistry(SnFieldTypeRegistry paramSnFieldTypeRegistry)
    {
        this.snFieldTypeRegistry = paramSnFieldTypeRegistry;
    }


    @Generated
    public SnIndexConfigurationService getSnIndexConfigurationService()
    {
        return this.snIndexConfigurationService;
    }


    @Generated
    public void setSnIndexConfigurationService(SnIndexConfigurationService paramSnIndexConfigurationService)
    {
        this.snIndexConfigurationService = paramSnIndexConfigurationService;
    }


    @Generated
    public SnIndexTypeService getSnIndexTypeService()
    {
        return this.snIndexTypeService;
    }


    @Generated
    public void setSnIndexTypeService(SnIndexTypeService paramSnIndexTypeService)
    {
        this.snIndexTypeService = paramSnIndexTypeService;
    }


    @Generated
    public SnCommonConfigurationService getSnCommonConfigurationService()
    {
        return this.snCommonConfigurationService;
    }


    @Generated
    public void setSnCommonConfigurationService(SnCommonConfigurationService paramSnCommonConfigurationService)
    {
        this.snCommonConfigurationService = paramSnCommonConfigurationService;
    }


    @Generated
    public SnQualifierTypeFactory getSnQualifierTypeFactory()
    {
        return this.snQualifierTypeFactory;
    }


    @Generated
    public void setSnQualifierTypeFactory(SnQualifierTypeFactory paramSnQualifierTypeFactory)
    {
        this.snQualifierTypeFactory = paramSnQualifierTypeFactory;
    }


    @Generated
    public SnContextFactory getSnContextFactory()
    {
        return this.snContextFactory;
    }


    @Generated
    public void setSnContextFactory(SnContextFactory paramSnContextFactory)
    {
        this.snContextFactory = paramSnContextFactory;
    }


    @Generated
    public SnSearchService getSnSearchService()
    {
        return this.snSearchService;
    }


    @Generated
    public void setSnSearchService(SnSearchService paramSnSearchService)
    {
        this.snSearchService = paramSnSearchService;
    }


    @Generated
    public SnAsSearchProvider getSnAsSearchProvider()
    {
        return this.snAsSearchProvider;
    }


    @Generated
    public void setSnAsSearchProvider(SnAsSearchProvider paramSnAsSearchProvider)
    {
        this.snAsSearchProvider = paramSnAsSearchProvider;
    }
}
