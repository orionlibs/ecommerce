package de.hybris.platform.persistence.polyglot.config;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence;
import de.hybris.platform.persistence.polyglot.TypeInfoFactory;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;
import de.hybris.platform.persistence.polyglot.search.dialect.CriteriaExtractor;
import de.hybris.platform.persistence.polyglot.search.dialect.PolyglotDialect;
import de.hybris.platform.servicelayer.ServicelayerBaseSpecification;
import groovy.lang.GroovyObject;
import java.util.List;
import java.util.Map;
import org.codehaus.groovy.runtime.GStringImpl;
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
import spock.lang.Shared;

@IntegrationTest
@SpecMetadata(filename = "CartLikeCriteriaTypeInfoIntegrationSpec.groovy", line = 23)
public class CartLikeCriteriaTypeInfoIntegrationSpec extends ServicelayerBaseSpecification
{
    @Shared
    @FieldMetadata(line = 26, name = "factory", ordinal = 0, initializer = false)
    protected volatile ItemStateRepositoryFactory $spock_sharedField_factory;
    @Shared
    @FieldMetadata(line = 28, name = "repoA", ordinal = 1, initializer = true)
    protected volatile ItemStateRepository $spock_sharedField_repoA;
    @Shared
    @FieldMetadata(line = 31, name = "typeNameConverter", ordinal = 2, initializer = false)
    protected volatile CriteriaExtractor.TypeNameConverter $spock_sharedField_typeNameConverter;


    private Object setup()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        List configs = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[0].call(
                        arrayOfCallSite[1].call(arrayOfCallSite[2].call(arrayOfCallSite[3].call(arrayOfCallSite[4].call(arrayOfCallSite[5].call(arrayOfCallSite[6].call(arrayOfCallSite[7].call(arrayOfCallSite[8].call(arrayOfCallSite[9].call(arrayOfCallSite[10].call(arrayOfCallSite[11].call(
                                                        arrayOfCallSite[12].call(arrayOfCallSite[13].call(arrayOfCallSite[14].call(arrayOfCallSite[15].call(arrayOfCallSite[16].call(arrayOfCallSite[17].call(
                                                                                        arrayOfCallSite[18].call(arrayOfCallSite[19].callConstructor($get$$class$de$hybris$platform$persistence$polyglot$config$PolyglotRepositoriesConfigProviderBuilder()), arrayOfCallSite[20].callGroovyObjectGetProperty(this)), Integer.valueOf(1)),
                                                                        Integer.valueOf(11), "pk"), Integer.valueOf(14), "pk"), Integer.valueOf(15), "c"), Integer.valueOf(2)), Integer.valueOf(-1), "c"), Integer.valueOf(3)), Integer.valueOf(4)), Integer.valueOf(5)), Integer.valueOf(51), "a"), Integer.valueOf(6)),
                                        Integer.valueOf(61), "b"), Integer.valueOf(8)), Integer.valueOf(81), "a"), Integer.valueOf(81), "b"), Integer.valueOf(9)), Integer.valueOf(-1), "a")), List.class);
        PolyglotRepositoriesConfigProvider provider = (PolyglotRepositoriesConfigProvider)ScriptBytecodeAdapter.castToType(arrayOfCallSite[21].callConstructor(MockedPolyglotRepositoryConfigProvider.class, configs), PolyglotRepositoriesConfigProvider.class);
        Object object1 = arrayOfCallSite[22].callConstructor(ItemStateRepositoryFactory.class, provider);
        ScriptBytecodeAdapter.setGroovyObjectProperty(object1, CartLikeCriteriaTypeInfoIntegrationSpec.class, (GroovyObject)this, "factory");
        Object object2 = new Object(this);
        ScriptBytecodeAdapter.setGroovyObjectProperty(object2, CartLikeCriteriaTypeInfoIntegrationSpec.class, (GroovyObject)this, "typeNameConverter");
        return object2;
    }


    @Test
    @FeatureMetadata(line = 72, name = "testSupportTypeBasedOnSearchCriteria", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"query", "params", "full", "partial", "none", "repositories"})
    public void $spock_feature_3_0(String query, Map params, boolean full, boolean partial, boolean none, ItemStateRepository... repositories)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[29].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[30].callConstructor(ValueRecorder.class), ValueRecorder.class);
        Criteria.CriteriaBuilder cb = (Criteria.CriteriaBuilder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[31].call(PolyglotDialect.class, query, arrayOfCallSite[32].callGroovyObjectGetProperty(this)), Criteria.CriteriaBuilder.class);
        Map entries = (Map)ScriptBytecodeAdapter.castToType(arrayOfCallSite[33].call(params, new __spock_feature_3_0_closure1(this, this)), Map.class);
        arrayOfCallSite[34].call(cb, entries);
        Object criteria = arrayOfCallSite[35].call(cb);
        TypeInfo typeInfo = (TypeInfo)ScriptBytecodeAdapter.castToType(arrayOfCallSite[36].call(TypeInfoFactory.class, criteria), TypeInfo.class);
        arrayOfCallSite[37].callCurrent((GroovyObject)this, new GStringImpl(new Object[] {query, params}, new String[] {"", "; ", ""}));
        RepositoryResult repository = (RepositoryResult)ScriptBytecodeAdapter.castToType(arrayOfCallSite[38].call(arrayOfCallSite[39].callGroovyObjectGetProperty(this), typeInfo), RepositoryResult.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.isFullySupported() == full", Integer.valueOf(86).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(CartLikeCriteriaTypeInfoIntegrationSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isFullySupported")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Boolean.valueOf(full))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.isFullySupported() == full", Integer.valueOf(86).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.isPartiallySupported() == partial", Integer.valueOf(87).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(CartLikeCriteriaTypeInfoIntegrationSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isPartiallySupported")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Boolean.valueOf(partial))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.isPartiallySupported() == partial", Integer.valueOf(87).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.isNotSupported() == none", Integer.valueOf(88).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(CartLikeCriteriaTypeInfoIntegrationSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isNotSupported")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Boolean.valueOf(none))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.isNotSupported() == none", Integer.valueOf(88).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.getRepositories().size() == repositories.size()", Integer.valueOf(89).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(11).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(6).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(CartLikeCriteriaTypeInfoIntegrationSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                                                            ScriptBytecodeAdapter.invokeMethod0(CartLikeCriteriaTypeInfoIntegrationSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "getRepositories")))),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(10).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(CartLikeCriteriaTypeInfoIntegrationSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(7).intValue()), repositories),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(8).intValue()), "size"))))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.getRepositories().size() == repositories.size()", Integer.valueOf(89).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.getRepositories().containsAll(repositories)", Integer.valueOf(90).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                            ScriptBytecodeAdapter.invokeMethod0(CartLikeCriteriaTypeInfoIntegrationSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "getRepositories")))),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "containsAll")),
                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), repositories)}DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(8).intValue(), Boolean.FALSE)),
                            DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(7).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.getRepositories().containsAll(repositories)", Integer.valueOf(90).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    public Identity itemPkWithTypeCode(int typeCode)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return (Identity)ScriptBytecodeAdapter.castToType(arrayOfCallSite[23].call(PolyglotPersistence.class, arrayOfCallSite[24].call(arrayOfCallSite[25].call(PK.class, Integer.valueOf(typeCode)))), Identity.class);
    }


    public Identity identity(String l)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return (Identity)ScriptBytecodeAdapter.castToType(arrayOfCallSite[26].call(PolyglotPersistence.class, arrayOfCallSite[27].call(Long.class, l)), Identity.class);
    }
}
