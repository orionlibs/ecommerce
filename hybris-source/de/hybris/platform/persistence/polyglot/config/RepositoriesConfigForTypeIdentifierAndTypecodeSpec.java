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

@SpecMetadata(filename = "RepositoriesConfigForTypeIdentifierAndTypecodeSpec.groovy", line = 13)
public class RepositoriesConfigForTypeIdentifierAndTypecodeSpec extends HybrisSpockSpecification
{
    @Shared
    @FieldMetadata(line = 15, name = "factory", ordinal = 0, initializer = false)
    protected volatile ItemStateRepositoryFactory $spock_sharedField_factory;
    @Shared
    @FieldMetadata(line = 17, name = "repoA", ordinal = 1, initializer = true)
    protected volatile ItemStateRepository $spock_sharedField_repoA;
    @Shared
    @FieldMetadata(line = 19, name = "repoB", ordinal = 2, initializer = true)
    protected volatile ItemStateRepository $spock_sharedField_repoB;


    private Object setup()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        List configs = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[0].call(
                        arrayOfCallSite[1].call(arrayOfCallSite[2].call(arrayOfCallSite[3].call(arrayOfCallSite[4].call(arrayOfCallSite[5].call(arrayOfCallSite[6].call(arrayOfCallSite[7].call(arrayOfCallSite[8].call(arrayOfCallSite[9].call(arrayOfCallSite[10].call(arrayOfCallSite[11].call(
                                                        arrayOfCallSite[12].call(arrayOfCallSite[13].call(arrayOfCallSite[14].call(
                                                                                        arrayOfCallSite[15].call(arrayOfCallSite[16].callConstructor($get$$class$de$hybris$platform$persistence$polyglot$config$PolyglotRepositoriesConfigProviderBuilder()), arrayOfCallSite[17].callGroovyObjectGetProperty(this)), Integer.valueOf(1)),
                                                                        Long.valueOf($const$0), ""), Integer.valueOf(2)), Long.valueOf($const$1), ""), Integer.valueOf(3)), Long.valueOf($const$2), ""), Integer.valueOf(4)), Long.valueOf($const$3), "a"), Integer.valueOf(5)),
                                        arrayOfCallSite[18].callGroovyObjectGetProperty(this)), Integer.valueOf(3)), Long.valueOf($const$4), ""), Integer.valueOf(4)), Long.valueOf($const$3), "b")), List.class);
        PolyglotRepositoriesConfigProvider provider = (PolyglotRepositoriesConfigProvider)ScriptBytecodeAdapter.castToType(arrayOfCallSite[19].callConstructor(MockedPolyglotRepositoryConfigProvider.class, configs), PolyglotRepositoriesConfigProvider.class);
        Object object = arrayOfCallSite[20].callConstructor(ItemStateRepositoryFactory.class, provider);
        ScriptBytecodeAdapter.setGroovyObjectProperty(object, RepositoriesConfigForTypeIdentifierAndTypecodeSpec.class, (GroovyObject)this, "factory");
        return object;
    }


    @Test
    @FeatureMetadata(line = 38, name = "testSupportTypeBasedOnType", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"typecode", "identity", "full", "partial", "none", "repositories"})
    public void $spock_feature_2_0(int typecode, long identity, boolean full, boolean partial, boolean none, ItemStateRepository... repositories)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[23].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[24].callConstructor(ValueRecorder.class), ValueRecorder.class);
        TypeInfo typeInfo = (TypeInfo)ScriptBytecodeAdapter.castToType(arrayOfCallSite[25].callConstructor(TypeIdentifierTypeInfo.class, this, Integer.valueOf(typecode), Long.valueOf(identity)), TypeInfo.class);
        RepositoryResult repository = (RepositoryResult)ScriptBytecodeAdapter.castToType(arrayOfCallSite[26].call(arrayOfCallSite[27].callGroovyObjectGetProperty(this), typeInfo), RepositoryResult.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.isFullySupported() == full", Integer.valueOf(47).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForTypeIdentifierAndTypecodeSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isFullySupported")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Boolean.valueOf(full))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.isFullySupported() == full", Integer.valueOf(47).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.isPartiallySupported() == partial", Integer.valueOf(48).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForTypeIdentifierAndTypecodeSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isPartiallySupported")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Boolean.valueOf(partial))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.isPartiallySupported() == partial", Integer.valueOf(48).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.isNotSupported() == none", Integer.valueOf(49).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForTypeIdentifierAndTypecodeSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isNotSupported")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Boolean.valueOf(none))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.isNotSupported() == none", Integer.valueOf(49).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.getRepositories().size() == repositories.size()", Integer.valueOf(50).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(11).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(6).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForTypeIdentifierAndTypecodeSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForTypeIdentifierAndTypecodeSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "getRepositories")))),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(10).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForTypeIdentifierAndTypecodeSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(7).intValue()), repositories),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(8).intValue()), "size"))))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.getRepositories().size() == repositories.size()", Integer.valueOf(50).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.getRepositories().containsAll(repositories)", Integer.valueOf(51).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForTypeIdentifierAndTypecodeSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "getRepositories")))),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "containsAll")),
                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), repositories)}DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(8).intValue(), Boolean.FALSE)),
                            DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(7).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.getRepositories().containsAll(repositories)", Integer.valueOf(51).intValue(), Integer.valueOf(9).intValue(), null, throwable);
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
        __$swapInit();
    }
}
