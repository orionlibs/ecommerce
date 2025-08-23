package de.hybris.platform.persistence.polyglot.config.supplier;

import de.hybris.platform.persistence.polyglot.config.PolyglotRepositoriesConfigProvider;
import de.hybris.platform.persistence.polyglot.config.RepositoryConfig;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;

public class PropertiesPolyglotRepositoriesConfigProvider implements PolyglotRepositoriesConfigProvider
{
    private static final Set<String> RESERVED_TYPECODES = Set.of("AtomicType", "ComposedType", "CollectionType", "MapType", "AttributeDescriptor");
    private final List<RepositoryConfig> configs = new ArrayList<>();
    private final ModelService modelService;
    private final TypeService typeService;
    private final PolyglotConfigSupplier properties;


    public PropertiesPolyglotRepositoriesConfigProvider(ModelService modelService, TypeService typeService)
    {
        this(modelService, typeService, (PolyglotConfigSupplier)new PropertiesPolyglotConfigSupplier());
    }


    public PropertiesPolyglotRepositoriesConfigProvider(ModelService modelService, TypeService typeService, PolyglotConfigSupplier properties)
    {
        this.modelService = modelService;
        this.typeService = typeService;
        this.properties = properties;
    }


    @PostConstruct
    public void setUpPolyglotReposFromProperties()
    {
        this.configs.addAll(getRepoConfigs());
    }


    public List<RepositoryConfig> getConfigs()
    {
        return this.configs;
    }


    private List<RepositoryConfig> getRepoConfigs()
    {
        return (List<RepositoryConfig>)this.properties.getRepositoryNames().stream()
                        .map(this::getRepoConfig)
                        .collect(Collectors.toList());
    }


    private RepositoryConfig getRepoConfig(String repoName)
    {
        String beanName = this.properties.getBeanName(repoName);
        if(StringUtils.isEmpty(beanName))
        {
            throw new IllegalArgumentException("Bad configuration parameters: beanName for repository '" + repoName + "' is not defined.");
        }
        Set<PropertyTypeCodeDefinition> typeCodeDefs = this.properties.getTypeCodeDefinitions(repoName);
        if(typeCodeDefs.isEmpty())
        {
            throw new IllegalArgumentException("Bad configuration parameters: type codes for repository '" + repoName + "' are not defined.");
        }
        Set<String> reserved = filterReservedTypes(typeCodeDefs);
        if(!reserved.isEmpty())
        {
            throw new IllegalArgumentException("Configuration of repository '" + repoName + "' contains reserved type codes " + reserved);
        }
        return (RepositoryConfig)new PropertyRepositoryConfig(beanName, typeCodeDefs, this.modelService, this.typeService);
    }


    private Set<String> filterReservedTypes(Set<PropertyTypeCodeDefinition> typeCodeDefs)
    {
        Objects.requireNonNull(RESERVED_TYPECODES);
        return (Set<String>)typeCodeDefs.stream().map(v -> v.typeCode).filter(RESERVED_TYPECODES::contains).collect(Collectors.toSet());
    }
}
