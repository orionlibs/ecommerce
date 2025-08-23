package de.hybris.platform.persistence.polyglot.config.supplier;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.RelationDescriptor;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence;
import de.hybris.platform.persistence.polyglot.config.DefaultRepositoryConfig;
import de.hybris.platform.persistence.polyglot.config.MoreSpecificCondition;
import de.hybris.platform.persistence.polyglot.config.PolyglotRepoSupportType;
import de.hybris.platform.persistence.polyglot.config.RepositoryConfig;
import de.hybris.platform.persistence.polyglot.config.TypeInfo;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class PropertyRepositoryConfig implements RepositoryConfig
{
    private static final List<Integer> RESERVED_TYPECODES = Arrays.asList(new Integer[] {Integer.valueOf(81), Integer.valueOf(82),
                    Integer.valueOf(83), Integer.valueOf(84),
                    Integer.valueOf(87)});
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyRepositoryConfig.class);
    private final ModelService modelService;
    private final TypeService typeService;
    private final Map<Integer, Set<MoreSpecificCondition>> config = new HashMap<>();
    private String beanName;
    private Set<PropertyTypeCodeDefinition> typeCodeDefs;
    private final Supplier<RepositoryConfig> repositoryConfig = (Supplier<RepositoryConfig>)Suppliers.memoize(this::initialize);


    PropertyRepositoryConfig(String beanName, Set<PropertyTypeCodeDefinition> typeCodeDefs, ModelService modelService, TypeService typeService)
    {
        this.beanName = beanName;
        this.typeCodeDefs = typeCodeDefs;
        this.modelService = modelService;
        this.typeService = typeService;
    }


    public ItemStateRepository getRepository()
    {
        return ((RepositoryConfig)this.repositoryConfig.get()).getRepository();
    }


    public Map<Integer, Set<MoreSpecificCondition>> getConditions()
    {
        return (Map<Integer, Set<MoreSpecificCondition>>)ImmutableMap.copyOf(this.config);
    }


    public PolyglotRepoSupportType isSupportedBy(TypeInfo typeInfo)
    {
        if(RESERVED_TYPECODES.contains(Integer.valueOf(typeInfo.getTypeCode())))
        {
            return PolyglotRepoSupportType.NONE;
        }
        return ((RepositoryConfig)this.repositoryConfig.get()).isSupportedBy(typeInfo);
    }


    private RepositoryConfig initialize()
    {
        try
        {
            Map<Boolean, List<PropertyTypeCodeDefinition>> partOfTypeCodeDefinitions = (Map<Boolean, List<PropertyTypeCodeDefinition>>)this.typeCodeDefs.stream().collect(Collectors.partitioningBy(v -> StringUtils.isNotEmpty(v.qualifier)));
            addTypeCodeDefinitions(partOfTypeCodeDefinitions.get(Boolean.valueOf(false)));
            addPartOfTypeCodeDefinitions(partOfTypeCodeDefinitions.get(Boolean.valueOf(true)));
            ItemStateRepository repo = (ItemStateRepository)Registry.getApplicationContext().getBean(this.beanName, ItemStateRepository.class);
            cleanAfterInitialization();
            return (RepositoryConfig)new DefaultRepositoryConfig(this.config, repo);
        }
        catch(Exception e)
        {
            LOGGER.error("TypeCode translation error ", e);
            throw e;
        }
    }


    private void addTypeCodeDefinitions(List<PropertyTypeCodeDefinition> typeCodeDefinitions)
    {
        typeCodeDefinitions
                        .stream()
                        .filter(typeCodeDef -> hasComposedType(typeCodeDef.typeCode))
                        .map(v -> v.typeCode)
                        .map(this::getComposedType)
                        .filter(this::hasOwnDeployment)
                        .forEach(t -> addTypeToPolyglotConfiguration(t, this::addTypeToConfig));
    }


    private void addPartOfTypeCodeDefinitions(List<PropertyTypeCodeDefinition> partOfDefinitions)
    {
        partOfDefinitions.forEach(def -> addPartOfDefinition(def, partOfDefinitions));
    }


    private void addTypeToPolyglotConfiguration(ComposedType type, Consumer<ComposedType> addTypeToConfig)
    {
        addParents(type);
        addTypeAndSubTypes(type, addTypeToConfig);
    }


    private boolean addPartOfDefinition(PropertyTypeCodeDefinition typeCodeDef, List<PropertyTypeCodeDefinition> partOfDefinitions)
    {
        try
        {
            ComposedType partOfType = getComposedType(typeCodeDef.typeCode);
            MoreSpecificCondition newCondition = typeCodeDef2Condition(typeCodeDef);
            Set<MoreSpecificCondition> conditions = this.config.get(Integer.valueOf(partOfType.getItemTypeCode()));
            if(conditions != null && conditions.stream().anyMatch(cond -> cond.equals(newCondition)))
            {
                return true;
            }
            return ((Boolean)getPartOfSourceTypeForQualifier(partOfType, typeCodeDef.qualifier)
                            .map(sourceType -> Boolean.valueOf(addPartOfDefinitionToPolyglotConfig(partOfDefinitions, partOfType, sourceType, newCondition, typeCodeDef)))
                            .orElseThrow(() -> new IllegalStateException("No relation found for type definition: " + typeCodeDef.toString()))).booleanValue();
        }
        catch(UnknownIdentifierException e)
        {
            return false;
        }
    }


    private Optional<ComposedType> getPartOfSourceTypeForQualifier(ComposedType type, String qualifier)
    {
        return type
                        .getRelations().stream()
                        .filter(rType -> ((Boolean)Optional.<RelationDescriptor>ofNullable(rType.getTargetAttributeDescriptor()).map(()).orElse(Boolean.valueOf(false))).booleanValue())
                        .filter(rType -> rType.getSourceAttributeDescriptor().isPartOf())
                        .map(RelationType::getSourceType)
                        .findFirst()
                        .or(() -> type.hasAttribute(qualifier) ? getPartOfSourceTypeForComposedType(type.getAttributeDescriptor(qualifier).getAttributeType(), type) : Optional.empty());
    }


    private Optional<ComposedType> getPartOfSourceTypeForComposedType(Type sourceType, ComposedType target)
    {
        if(sourceType instanceof ComposedType)
        {
            return Optional.<ComposedType>of((ComposedType)sourceType)
                            .filter(t -> t.getDeclaredAttributeDescriptors().stream().anyMatch(()));
        }
        return Optional.empty();
    }


    private boolean isTypeComposedOf(Type attributeType, ComposedType type)
    {
        if(attributeType instanceof CollectionType)
        {
            return ((CollectionType)attributeType).getElementType().equals(type);
        }
        if(attributeType instanceof ComposedType)
        {
            return attributeType.equals(type);
        }
        return false;
    }


    private boolean addPartOfDefinitionToPolyglotConfig(List<PropertyTypeCodeDefinition> partOfDefinitions, ComposedType partOfType, ComposedType sourceType, MoreSpecificCondition newCondition, PropertyTypeCodeDefinition def)
    {
        if(isRelationTypeOrRelationTypeSubtypesInPolyglot(newCondition, sourceType) ||
                        checkIfRelationPointsToOtherPartOfDefinition(sourceType, (List<PropertyTypeCodeDefinition>)partOfDefinitions
                                        .stream()
                                        .filter(partOfDef -> !partOfDef.typeCode.equals(partOfType.getCode()))
                                        .collect(Collectors.toList())))
        {
            addTypeToPolyglotConfiguration(partOfType, t -> addTypeToConfig(t, createSpecificCondition(getIdentity(t.getPK().getLong().longValue()), newCondition.getQualifier(), newCondition.getTargetIdentity())));
            return true;
        }
        throw new IllegalStateException("No relation to fully supported type found for typeCode definition " + def);
    }


    private MoreSpecificCondition createSpecificCondition(Identity srcIdentity, String qualifier, Identity targetIdentity)
    {
        return new MoreSpecificCondition(srcIdentity, qualifier, targetIdentity);
    }


    private boolean checkIfRelationPointsToOtherPartOfDefinition(ComposedType target, List<PropertyTypeCodeDefinition> partOfDefinitions)
    {
        return ((Boolean)partOfDefinitions
                        .stream()
                        .filter(partOfDef -> isPointingToPartOfTypeCode(target, partOfDef.typeCode))
                        .findFirst()
                        .map(partOfDef -> Boolean.valueOf(addPartOfDefinition(partOfDef, partOfDefinitions)))
                        .orElse(Boolean.valueOf(false))).booleanValue();
    }


    private boolean isPointingToPartOfTypeCode(ComposedType target, String partOfTypeCode)
    {
        return (target.getCode().equals(partOfTypeCode) || target
                        .getAllSubTypes().stream().anyMatch(subType -> subType.getCode().equals(partOfTypeCode)));
    }


    private boolean isRelationTypeOrRelationTypeSubtypesInPolyglot(MoreSpecificCondition newCondition, ComposedType sourceType)
    {
        return (isRelationTypeInPolyglot(newCondition, sourceType) || sourceType
                        .getAllSubTypes().stream().anyMatch(subType -> isRelationTypeInPolyglot(newCondition, subType)));
    }


    private boolean isRelationTypeInPolyglot(MoreSpecificCondition newCondition, ComposedType sourceType)
    {
        return (this.config.get(Integer.valueOf(sourceType.getItemTypeCode())) != null && (
                        PolyglotPersistence.unknownIdentity().equals(newCondition.getTargetIdentity()) ||
                                        getIdentity(sourceType.getPK().getLong().longValue()).equals(newCondition.getTargetIdentity())));
    }


    private MoreSpecificCondition typeCodeDef2Condition(PropertyTypeCodeDefinition def)
    {
        long targetTypePk = (StringUtils.isEmpty(def.qualifierType) ?
                        getPartOfSourceTypeForQualifier(getComposedType(
                                        def.typeCode), def.qualifier).<Long>map(sourceType -> sourceType.getPK().getLong()).orElse(Long.valueOf(-1L)) :
                        getComposedType(def.qualifierType).getPK().getLong()).longValue();
        long sourceTypePk = getComposedType(def.typeCode).getPK().getLong().longValue();
        return createSpecificCondition(getIdentity(sourceTypePk), def.qualifier, getIdentity(targetTypePk));
    }


    private void addParents(ComposedType type)
    {
        type.getAllSuperTypes()
                        .forEach(parent -> addTypeToConfig(parent, createSpecificCondition(getIdentity(parent.getPK().getLong().longValue()), "pk", null)));
    }


    private void addTypeAndSubTypes(ComposedType type, Consumer<ComposedType> addTypeToConfig)
    {
        addTypeToConfig.accept(type);
        type.getAllSubTypes().forEach(addTypeToConfig);
    }


    private void addTypeToConfig(ComposedType type)
    {
        addTypeToConfig(type, null);
    }


    private void addTypeToConfig(ComposedType type, MoreSpecificCondition cond)
    {
        Set<MoreSpecificCondition> set = this.config.computeIfAbsent(Integer.valueOf(type.getItemTypeCode()), java.util.HashSet::new);
        if(cond != null)
        {
            set.add(cond);
        }
    }


    private boolean hasComposedType(String code)
    {
        try
        {
            this.modelService.getSource(this.typeService.getComposedTypeForCode(code));
        }
        catch(Exception e)
        {
            LOGGER.warn("No ComposedType type for type " + code + " found. This type is being skipped.");
            return false;
        }
        return true;
    }


    private boolean hasOwnDeployment(ComposedType composedType)
    {
        if(composedType.hasOwnDeployment())
        {
            return true;
        }
        throw new IllegalArgumentException("Type with code '" + composedType
                        .getCode() + "' has not defined deployment.");
    }


    private ComposedType getComposedType(String code)
    {
        try
        {
            return (ComposedType)this.modelService.getSource(this.typeService.getComposedTypeForCode(code));
        }
        catch(Exception e)
        {
            throw e;
        }
    }


    private Identity getIdentity(long specyficType)
    {
        if(specyficType >= 0L)
        {
            return PolyglotPersistence.identityFromLong(specyficType);
        }
        return PolyglotPersistence.unknownIdentity();
    }


    private void cleanAfterInitialization()
    {
        this.typeCodeDefs = null;
        this.beanName = null;
    }
}
