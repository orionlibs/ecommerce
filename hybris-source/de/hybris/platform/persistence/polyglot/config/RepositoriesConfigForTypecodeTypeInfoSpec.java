package de.hybris.platform.persistence.polyglot.config;

import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import de.hybris.platform.persistence.polyglot.TypeInfoFactory;
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

@SpecMetadata(filename = "RepositoriesConfigForTypecodeTypeInfoSpec.groovy", line = 13)
public class RepositoriesConfigForTypecodeTypeInfoSpec extends HybrisSpockSpecification
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
                        arrayOfCallSite[1].call(arrayOfCallSite[2].call(arrayOfCallSite[3].call(arrayOfCallSite[4].call(arrayOfCallSite[5].call(arrayOfCallSite[6].call(arrayOfCallSite[7].call(arrayOfCallSite[8].call(
                                                        arrayOfCallSite[9].call(arrayOfCallSite[10].call(arrayOfCallSite[11].callConstructor($get$$class$de$hybris$platform$persistence$polyglot$config$PolyglotRepositoriesConfigProviderBuilder()), arrayOfCallSite[12].callGroovyObjectGetProperty(this)),
                                                                        Integer.valueOf(1)), Integer.valueOf(-1), "pk"), Integer.valueOf(-1), "param"), Integer.valueOf(2)), Integer.valueOf(3)), Integer.valueOf(-1), "paramA"), arrayOfCallSite[13].callGroovyObjectGetProperty(this)), Integer.valueOf(3)),
                                        Integer.valueOf(-1), "paramB")), List.class);
        PolyglotRepositoriesConfigProvider provider = (PolyglotRepositoriesConfigProvider)ScriptBytecodeAdapter.castToType(arrayOfCallSite[14].callConstructor(MockedPolyglotRepositoryConfigProvider.class, configs), PolyglotRepositoriesConfigProvider.class);
        Object object = arrayOfCallSite[15].callConstructor(ItemStateRepositoryFactory.class, provider);
        ScriptBytecodeAdapter.setGroovyObjectProperty(object, RepositoriesConfigForTypecodeTypeInfoSpec.class, (GroovyObject)this, "factory");
        return object;
    }


    @Test
    @FeatureMetadata(line = 35, name = "testSupportTypeBasedOnTypecode", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"typecode", "full", "partial", "none", "repositories"})
    public void $spock_feature_2_0(int typecode, boolean full, boolean partial, boolean none, ItemStateRepository... repositories)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ErrorCollector $spock_errorCollector = (ErrorCollector)ScriptBytecodeAdapter.castToType(arrayOfCallSite[18].callGetProperty(ErrorRethrower.class), ErrorCollector.class);
        ValueRecorder $spock_valueRecorder = (ValueRecorder)ScriptBytecodeAdapter.castToType(arrayOfCallSite[19].callConstructor(ValueRecorder.class), ValueRecorder.class);
        TypeInfo typeInfo = (TypeInfo)ScriptBytecodeAdapter.castToType(arrayOfCallSite[20].call(TypeInfoFactory.class, Integer.valueOf(typecode)), TypeInfo.class);
        RepositoryResult repository = (RepositoryResult)ScriptBytecodeAdapter.castToType(arrayOfCallSite[21].call(arrayOfCallSite[22].callGroovyObjectGetProperty(this), typeInfo), RepositoryResult.class);
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.isFullySupported() == full", Integer.valueOf(44).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForTypecodeTypeInfoSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isFullySupported")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Boolean.valueOf(full))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.isFullySupported() == full", Integer.valueOf(44).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.isPartiallySupported() == partial", Integer.valueOf(45).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForTypecodeTypeInfoSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isPartiallySupported")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Boolean.valueOf(partial))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.isPartiallySupported() == partial", Integer.valueOf(45).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.isNotSupported() == none", Integer.valueOf(46).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForTypecodeTypeInfoSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "isNotSupported")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), Boolean.valueOf(none))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.isNotSupported() == none", Integer.valueOf(46).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.getRepositories().size() == repositories.size()", Integer.valueOf(47).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(11).intValue()), Boolean.valueOf(ScriptBytecodeAdapter.compareEqual($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(6).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForTypecodeTypeInfoSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForTypecodeTypeInfoSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "getRepositories")))),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "size")))),
                                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(10).intValue()),
                                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForTypecodeTypeInfoSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(7).intValue()), repositories),
                                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(8).intValue()), "size"))))))));
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.getRepositories().size() == repositories.size()", Integer.valueOf(47).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        try
        {
            SpockRuntime.verifyMethodCondition($spock_errorCollector, $spock_valueRecorder.reset(), "repository.getRepositories().containsAll(repositories)", Integer.valueOf(48).intValue(), Integer.valueOf(9).intValue(), null,
                            $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(3).intValue()),
                                            ScriptBytecodeAdapter.invokeMethod0(RepositoriesConfigForTypecodeTypeInfoSpec.class, $spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(0).intValue()), repository),
                                                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(1).intValue()), "getRepositories")))),
                            ShortTypeHandling.castToString($spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(4).intValue()), "containsAll")),
                            new Object[] {$spock_valueRecorder.record($spock_valueRecorder.startRecordingValue(Integer.valueOf(5).intValue()), repositories)}DefaultTypeTransformation.booleanUnbox($spock_valueRecorder.realizeNas(Integer.valueOf(8).intValue(), Boolean.FALSE)),
                            DefaultTypeTransformation.booleanUnbox(Boolean.FALSE), Integer.valueOf(7).intValue());
            null;
        }
        catch(Throwable throwable)
        {
            SpockRuntime.conditionFailedWithException($spock_errorCollector, $spock_valueRecorder, "repository.getRepositories().containsAll(repositories)", Integer.valueOf(48).intValue(), Integer.valueOf(9).intValue(), null, throwable);
            null;
        }
        finally
        {
        }
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }
}
