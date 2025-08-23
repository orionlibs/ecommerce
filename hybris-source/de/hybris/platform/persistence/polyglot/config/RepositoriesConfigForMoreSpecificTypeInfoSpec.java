package de.hybris.platform.persistence.polyglot.config;

import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import de.hybris.platform.testframework.HybrisSpockSpecification;
import groovy.lang.GroovyObject;
import java.util.List;
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

@SpecMetadata(filename = "RepositoriesConfigForMoreSpecificTypeInfoSpec.groovy", line = 14)
public class RepositoriesConfigForMoreSpecificTypeInfoSpec extends HybrisSpockSpecification
{
    @Shared
    @FieldMetadata(line = 16, name = "factory", ordinal = 0, initializer = false)
    protected volatile ItemStateRepositoryFactory $spock_sharedField_factory;
    @Shared
    @FieldMetadata(line = 18, name = "repoA", ordinal = 1, initializer = true)
    protected volatile ItemStateRepository $spock_sharedField_repoA;
    @Shared
    @FieldMetadata(line = 20, name = "repoB", ordinal = 2, initializer = true)
    protected volatile ItemStateRepository $spock_sharedField_repoB;


    private Object setup()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        List configs = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[0].call(
                        arrayOfCallSite[1].call(arrayOfCallSite[2].call(arrayOfCallSite[3].call(arrayOfCallSite[4].call(arrayOfCallSite[5].call(arrayOfCallSite[6].call(arrayOfCallSite[7].call(arrayOfCallSite[8].call(arrayOfCallSite[9].call(arrayOfCallSite[10].call(arrayOfCallSite[11].call(
                                                        arrayOfCallSite[12].call(arrayOfCallSite[13].call(arrayOfCallSite[14].call(arrayOfCallSite[15].call(arrayOfCallSite[16].call(arrayOfCallSite[17].call(arrayOfCallSite[18].call(arrayOfCallSite[19].call(arrayOfCallSite[20].call(arrayOfCallSite[21].call(
                                                                                        arrayOfCallSite[22].call(arrayOfCallSite[23].call(arrayOfCallSite[24].call(arrayOfCallSite[25].call(
                                                                                                        arrayOfCallSite[26].call(arrayOfCallSite[27].callConstructor($get$$class$de$hybris$platform$persistence$polyglot$config$PolyglotRepositoriesConfigProviderBuilder()), arrayOfCallSite[28].callGroovyObjectGetProperty(this)),
                                                                                                        Integer.valueOf(1)), Integer.valueOf(-1), "2"), Integer.valueOf(2)), Integer.valueOf(-1), "3"), Integer.valueOf(3)), Integer.valueOf(-1), "4"), Integer.valueOf(4)), Integer.valueOf(5)), Integer.valueOf(-1), "4"),
                                                                        Integer.valueOf(6)), Integer.valueOf(-1), "5"), Integer.valueOf(7)), Integer.valueOf(-1), "6"), arrayOfCallSite[29].callGroovyObjectGetProperty(this)), Integer.valueOf(1)), Integer.valueOf(-1), "3"), Integer.valueOf(3)), Integer.valueOf(-1),
                                        "11"), Integer.valueOf(11)), Integer.valueOf(12)), Integer.valueOf(-1), "11"), Integer.valueOf(6)), Integer.valueOf(-1), "12"), Integer.valueOf(7)), Integer.valueOf(-1), "6")), List.class);
        PolyglotRepositoriesConfigProvider provider = (PolyglotRepositoriesConfigProvider)ScriptBytecodeAdapter.castToType(arrayOfCallSite[30].callConstructor(MockedPolyglotRepositoryConfigProvider.class, configs), PolyglotRepositoriesConfigProvider.class);
        Object object = arrayOfCallSite[31].callConstructor(ItemStateRepositoryFactory.class, provider);
        ScriptBytecodeAdapter.setGroovyObjectProperty(object, RepositoriesConfigForMoreSpecificTypeInfoSpec.class, (GroovyObject)this, "factory");
        return object;
    }


    @Test
    @FeatureMetadata(line = 45, name = "testSupportTypeBasedOnMoreSpecificTypeInfo", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"typecode", "depth", "full", "partial", "none", "repositories"})
    public void $spock_feature_2_0(int typecode, int depth, boolean full, boolean partial, boolean none, ItemStateRepository... repositories)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[34].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[35].callConstructor(ValueRecorder.class), ValueRecorder.class);
        TypeInfo typeInfo = (TypeInfo)ScriptBytecodeAdapter.castToType(arrayOfCallSite[36].callConstructor(MoreSpecificTypeInfo.class, this, Integer.valueOf(typecode), Integer.valueOf(depth)), TypeInfo.class);
        RepositoryResult repository = (RepositoryResult)ScriptBytecodeAdapter.castToType(arrayOfCallSite[37].call(arrayOfCallSite[38].callGroovyObjectGetProperty(this), typeInfo), RepositoryResult.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.isFullySupported() == full", Integer.valueOf(54).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForMoreSpecificTypeInfoSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isFullySupported")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Boolean.valueOf(full))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.isFullySupported() == full", Integer.valueOf(54).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.isPartiallySupported() == partial", Integer.valueOf(55).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForMoreSpecificTypeInfoSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isPartiallySupported")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Boolean.valueOf(partial))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.isPartiallySupported() == partial", Integer.valueOf(55).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.isNotSupported() == none", Integer.valueOf(56).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForMoreSpecificTypeInfoSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isNotSupported")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Boolean.valueOf(none))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.isNotSupported() == none", Integer.valueOf(56).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.getRepositories().size() == repositories.size()", Integer.valueOf(57).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(11).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(6).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForMoreSpecificTypeInfoSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForMoreSpecificTypeInfoSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "getRepositories")))),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(10).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForMoreSpecificTypeInfoSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(7).intValue()), repositories),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(8).intValue()), "size"))))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.getRepositories().size() == repositories.size()", Integer.valueOf(57).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.getRepositories().containsAll(repositories)", Integer.valueOf(58).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForMoreSpecificTypeInfoSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "getRepositories")))),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "containsAll")),
                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), repositories)}DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(8).intValue(), Boolean.FALSE)),
                            DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(7).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.getRepositories().containsAll(repositories)", Integer.valueOf(58).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }
}
