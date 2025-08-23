package de.hybris.bootstrap.typesystem;

import de.hybris.bootstrap.ddl.dbtypesystem.Attribute;
import de.hybris.bootstrap.ddl.dbtypesystem.DbTypeSystem;
import de.hybris.bootstrap.ddl.dbtypesystem.Deployment;
import de.hybris.bootstrap.ddl.dbtypesystem.Type;
import de.hybris.bootstrap.typesystem.dto.AtomicTypeDTO;
import de.hybris.bootstrap.typesystem.dto.AttributeDTO;
import de.hybris.bootstrap.typesystem.dto.AttributeDeploymentDTO;
import de.hybris.bootstrap.typesystem.dto.AttributeModifierDTO;
import de.hybris.bootstrap.typesystem.dto.CollectionTypeDTO;
import de.hybris.bootstrap.typesystem.dto.ComposedTypeDTO;
import de.hybris.bootstrap.typesystem.dto.DBTypeMappingDTO;
import de.hybris.bootstrap.typesystem.dto.DeploymentDTO;
import de.hybris.bootstrap.typesystem.dto.EnumTypeDTO;
import de.hybris.bootstrap.typesystem.dto.EnumValueDTO;
import de.hybris.bootstrap.typesystem.dto.FinderDeploymentDTO;
import de.hybris.bootstrap.typesystem.dto.IndexDTO;
import de.hybris.bootstrap.typesystem.dto.IndexDeploymentDTO;
import de.hybris.bootstrap.typesystem.dto.MapTypeDTO;
import de.hybris.bootstrap.typesystem.dto.RelationTypeDTO;
import de.hybris.bootstrap.util.LocaleHelper;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Logger;

public class PatchedYTypeSystemLoader implements YTypeSystemHandler
{
    private static final Logger LOG = Logger.getLogger(PatchedYTypeSystemLoader.class);
    private final YTypeSystemHandler delegate;
    private final DbTypeSystem dbTypeSystem;
    private final AdditionalPropsDeploymentCreator additionalPropsDeploymentsCreator = new AdditionalPropsDeploymentCreator();
    private final Set<String> rollOutDBBasedDeploymentChanges = new HashSet<>();


    public PatchedYTypeSystemLoader(YTypeSystemHandler delegate, DbTypeSystem dbTypeSystem)
    {
        this.delegate = delegate;
        this.dbTypeSystem = dbTypeSystem;
    }


    public YExtension addExtension(String extensionName, Set<String> requires)
    {
        return this.delegate.addExtension(extensionName, requires);
    }


    public YAtomicType loadAtomicType(AtomicTypeDTO atomicTypeDTO)
    {
        return this.delegate.loadAtomicType(atomicTypeDTO);
    }


    public YCollectionType loadCollectionType(CollectionTypeDTO collectionTypeDTO)
    {
        return this.delegate.loadCollectionType(collectionTypeDTO);
    }


    public YComposedType loadComposedType(ComposedTypeDTO composedTypeDTO)
    {
        Type existingType = this.dbTypeSystem.findTypeByCode(composedTypeDTO.getCode());
        String xmlDeploymentName = composedTypeDTO.getDeploymentName();
        String effectiveDeploymentName = xmlDeploymentName;
        if(existingType != null)
        {
            boolean defaultDbBasedDeployment = isDefaultDeployment(existingType);
            String dbBasedDeploymentName = getDBDefinedDeployment(existingType);
            if(deploymentNameIsChanged(xmlDeploymentName, dbBasedDeploymentName) &&
                            !this.rollOutDBBasedDeploymentChanges.contains(dbBasedDeploymentName))
            {
                if(!defaultDbBasedDeployment)
                {
                    effectiveDeploymentName = dbBasedDeploymentName;
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Adjusting deployment name for type " + composedTypeDTO
                                        .getCode() + " from (" + xmlDeploymentName + ")->(" + effectiveDeploymentName + ")");
                    }
                    Deployment existingDeployment = existingType.getDeployment();
                    if(getSystem().getDeployment(effectiveDeploymentName) == null)
                    {
                        DeploymentDTO deploymentDTO = toExistingDeploymentDTO(existingDeployment);
                        loadDeployment(deploymentDTO);
                    }
                }
                else if(!isDefaultDeploymentName(xmlDeploymentName))
                {
                    effectiveDeploymentName = null;
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Adjusting deployment name for type " + composedTypeDTO
                                        .getCode() + " from (" + xmlDeploymentName + ")->(null)");
                    }
                }
            }
        }
        ComposedTypeDTO transformedDto = composedTypeDTO.withDeploymentName(effectiveDeploymentName);
        return this.delegate.loadComposedType(transformedDto);
    }


    private boolean deploymentNameIsChanged(String xmlDeploymentName, String dbBasedDeploymentName)
    {
        return !dbBasedDeploymentName.equalsIgnoreCase(xmlDeploymentName);
    }


    private boolean isDefaultDeployment(Type existingType)
    {
        return (existingType.getDeployment() == null || isDefaultDeploymentName(existingType.getDeployment().getFullName()));
    }


    private boolean isDefaultDeploymentName(String deploymentName)
    {
        return "de.hybris.platform.persistence.GenericItem".equalsIgnoreCase(deploymentName);
    }


    private String getDBDefinedDeployment(Type existingType)
    {
        return isDefaultDeployment(existingType) ? "de.hybris.platform.persistence.GenericItem" :
                        existingType.getDeployment().getFullName();
    }


    public YMapType loadMapType(MapTypeDTO mapTypeDTO)
    {
        return this.delegate.loadMapType(mapTypeDTO);
    }


    public YRelation loadRelation(RelationTypeDTO relationTypeDTO)
    {
        return this.delegate.loadRelation(relationTypeDTO);
    }


    public YEnumType loadEnumType(EnumTypeDTO enumTypeDTO)
    {
        return this.delegate.loadEnumType(enumTypeDTO);
    }


    public YAttributeDescriptor loadAttribute(AttributeDTO attributeDTO)
    {
        AttributeDTO effectiveAttribute = attributeDTO;
        AttributeModifierDTO effectiveModifiers = attributeDTO.getModifiers();
        Type dbType = this.dbTypeSystem.findTypeByCode(attributeDTO.getEnclosingTypeCode());
        if(dbType != null)
        {
            Attribute dbAttribute = dbType.getAttribute(effectiveAttribute.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
            if(dbAttribute != null)
            {
                if(effectiveAttribute.isUnique() && !dbAttribute.isUnique())
                {
                    logAttributeChange("unique =" + dbAttribute.isUnique(), attributeDTO);
                    effectiveAttribute = effectiveAttribute.withUniqueAttribute(dbAttribute.isUnique());
                }
                AttributeModifierDTO dbModifiers = new AttributeModifierDTO(dbAttribute.getModifiers().intValue());
                if(effectiveModifiers.isDontOptimize() != dbModifiers.isDontOptimize())
                {
                    logAttributeChange("dontoptimize =" + dbModifiers.isDontOptimize(), attributeDTO);
                    effectiveAttribute = effectiveAttribute.withModifiers(effectiveModifiers.withOptimize(
                                    !dbModifiers.isDontOptimize()));
                }
                String effectivePersistenceQualifier = calculatePersistenceQualifier(effectiveAttribute);
                YAttributeDescriptor.PersistenceType dbPersistenceType = dbAttribute.calculatePersistenceType();
                if(!columnNamesMatch(dbAttribute, effectivePersistenceQualifier) && dbPersistenceType != YAttributeDescriptor.PersistenceType.CMP)
                {
                    logAttributeChange("persistencequalifer =" + dbAttribute.getColumnName(), attributeDTO);
                    effectiveAttribute = effectiveAttribute.withPersistenceQualifier(dbAttribute.getColumnName());
                }
                YAttributeDescriptor.PersistenceType xmlPersistenceType = effectiveAttribute.getPersistenceType();
                if(!attributeDTO.isRedeclare() && dbPersistenceType != xmlPersistenceType &&
                                !isLegalPersistenceTypeChange(dbPersistenceType, xmlPersistenceType))
                {
                    logAttributeChange("persistencetype =" + dbPersistenceType, attributeDTO);
                    effectiveAttribute = effectiveAttribute.withPersistenceType(dbPersistenceType);
                }
            }
        }
        return this.delegate.loadAttribute(effectiveAttribute);
    }


    private boolean isLegalPersistenceTypeChange(YAttributeDescriptor.PersistenceType dbPersistenceType, YAttributeDescriptor.PersistenceType xmlPersistenceType)
    {
        if((dbPersistenceType == YAttributeDescriptor.PersistenceType.JALO && xmlPersistenceType == YAttributeDescriptor.PersistenceType.DYNAMIC) || (dbPersistenceType == YAttributeDescriptor.PersistenceType.PROPERTY && xmlPersistenceType == YAttributeDescriptor.PersistenceType.DYNAMIC))
        {
            return true;
        }
        return false;
    }


    private String calculatePersistenceQualifier(AttributeDTO attributeDTO)
    {
        if(attributeDTO.getPersistenceQualifier() == null)
        {
            if(attributeDTO.getPersistenceType() == YAttributeDescriptor.PersistenceType.PROPERTY && attributeDTO
                            .getQualifier() != null)
            {
                return "p_" + attributeDTO.getQualifier();
            }
            if(attributeDTO.getPersistenceType() == YAttributeDescriptor.PersistenceType.DYNAMIC || attributeDTO
                            .getPersistenceType() == YAttributeDescriptor.PersistenceType.JALO)
            {
                return null;
            }
            LOG.debug("Persistence qualifier for attribute " + attributeDTO.getEnclosingTypeCode() + "." + attributeDTO
                            .getQualifier() + " ( persistence type  = " + attributeDTO.getPersistenceType() + " ),  could not be calculated");
            return null;
        }
        return attributeDTO.getPersistenceQualifier();
    }


    private boolean columnNamesMatch(Attribute dbAttribute, String effectivePersistenceQualifier)
    {
        boolean defaultDbColumnMatchModel = (dbAttribute.getColumnName() == null && effectivePersistenceQualifier == null);
        boolean customDbColumnMatchModel = (dbAttribute.getColumnName() != null && dbAttribute.getColumnName().equalsIgnoreCase(effectivePersistenceQualifier));
        return (defaultDbColumnMatchModel || customDbColumnMatchModel);
    }


    public YIndex loadIndex(IndexDTO indexDTO)
    {
        return this.delegate.loadIndex(indexDTO);
    }


    public YEnumValue loadEnumValue(EnumValueDTO enumValueDTO)
    {
        return this.delegate.loadEnumValue(enumValueDTO);
    }


    public YDBTypeMapping loadDBTypeMapping(DBTypeMappingDTO dbTypeMappingDTO)
    {
        return this.delegate.loadDBTypeMapping(dbTypeMappingDTO);
    }


    public YDeployment loadDeployment(DeploymentDTO xmlDeploymentDTO)
    {
        DeploymentDTO effectiveDeployment;
        Deployment existingDeployment = this.dbTypeSystem.findDeploymentByTypeCode(xmlDeploymentDTO.getTypeCode());
        if(existingDeployment != null)
        {
            if(isDataPreservingTypeMigration(xmlDeploymentDTO, existingDeployment))
            {
                LOG.warn("Can not preserve extension name <" + existingDeployment.getExtensionName() + "> for existing DB deployment " + existingDeployment
                                .getFullName() + " - given extension <" + existingDeployment
                                .getExtensionName() + "> extension could not be found ");
                effectiveDeployment = toExistingDeploymentDTOWithOverriddenExtensionAndName(existingDeployment, xmlDeploymentDTO);
                this.rollOutDBBasedDeploymentChanges.add(existingDeployment.getFullName());
            }
            else if(isExtensionChanged(xmlDeploymentDTO, existingDeployment))
            {
                effectiveDeployment = toExistingDeploymentDTOWithOverriddenExtension(existingDeployment, xmlDeploymentDTO);
            }
            else
            {
                effectiveDeployment = toExistingDeploymentDTO(existingDeployment);
            }
        }
        else
        {
            effectiveDeployment = xmlDeploymentDTO;
        }
        this.additionalPropsDeploymentsCreator.markDeploymentAsLoaded(effectiveDeployment);
        return this.delegate.loadDeployment(effectiveDeployment);
    }


    private boolean isDataPreservingTypeMigration(DeploymentDTO xmlDeploymentDTO, Deployment existingDeployment)
    {
        return (existingDeployment.getTableName().equalsIgnoreCase(xmlDeploymentDTO.getTableName()) && existingDeployment
                        .getTypeCode() == xmlDeploymentDTO.getTypeCode() &&
                        isExtensionChanged(xmlDeploymentDTO, existingDeployment));
    }


    private boolean isExtensionChanged(DeploymentDTO xmlDeploymentDTO, Deployment existingDeployment)
    {
        return !existingDeployment.getExtensionName().equalsIgnoreCase(xmlDeploymentDTO.getExtensionName());
    }


    private DeploymentDTO toExistingDeploymentDTO(Deployment existingDeployment)
    {
        return new DeploymentDTO(existingDeployment
                        .getExtensionName(), existingDeployment
                        .getPackageName(), existingDeployment
                        .getName(), existingDeployment
                        .getSuperDeployment(), existingDeployment
                        .getTypeCode(), existingDeployment
                        .isAbstract(), existingDeployment
                        .isGeneric(), existingDeployment
                        .isFinal(), existingDeployment
                        .getTableName(), existingDeployment
                        .getPropsTableName(), existingDeployment
                        .isNonItemDeployment());
    }


    private DeploymentDTO toExistingDeploymentDTOWithOverriddenExtension(Deployment existingDeployment, DeploymentDTO overriddenXmlDeploymentDTO)
    {
        return new DeploymentDTO(overriddenXmlDeploymentDTO
                        .getExtensionName(), existingDeployment
                        .getPackageName(), existingDeployment
                        .getName(), existingDeployment
                        .getSuperDeployment(), existingDeployment
                        .getTypeCode(), existingDeployment
                        .isAbstract(), existingDeployment
                        .isGeneric(), existingDeployment
                        .isFinal(), existingDeployment
                        .getTableName(), existingDeployment
                        .getPropsTableName(), existingDeployment
                        .isNonItemDeployment());
    }


    private DeploymentDTO toExistingDeploymentDTOWithOverriddenExtensionAndName(Deployment existingDeployment, DeploymentDTO overriddenXmlDeploymentDTO)
    {
        return new DeploymentDTO(overriddenXmlDeploymentDTO
                        .getExtensionName(), existingDeployment
                        .getPackageName(), overriddenXmlDeploymentDTO
                        .getName(), existingDeployment
                        .getSuperDeployment(), existingDeployment
                        .getTypeCode(), existingDeployment
                        .isAbstract(), existingDeployment
                        .isGeneric(), existingDeployment
                        .isFinal(), existingDeployment
                        .getTableName(), existingDeployment
                        .getPropsTableName(), existingDeployment
                        .isNonItemDeployment());
    }


    public YDeployment registerDeploymentForType(String codeOfType, String deployment)
    {
        return this.delegate.registerDeploymentForType(codeOfType, deployment);
    }


    public YIndexDeployment loadIndexDeployment(IndexDeploymentDTO indexDeploymentDTO)
    {
        this.additionalPropsDeploymentsCreator.markIndexDeploymentAsLoaded(indexDeploymentDTO);
        return this.delegate.loadIndexDeployment(indexDeploymentDTO);
    }


    public YAttributeDeployment loadAttributeDeployment(AttributeDeploymentDTO attributeDeploymentDTO)
    {
        this.additionalPropsDeploymentsCreator.markAttributeDeploymentAsLoaded(attributeDeploymentDTO);
        return this.delegate.loadAttributeDeployment(attributeDeploymentDTO);
    }


    public YFinder loadFinderDeployment(FinderDeploymentDTO finderDeploymentDTO)
    {
        return this.delegate.loadFinderDeployment(finderDeploymentDTO);
    }


    public void finish()
    {
        this.additionalPropsDeploymentsCreator.loadDeployments(this);
        this.delegate.finish();
    }


    public void validate()
    {
        this.delegate.validate();
    }


    public YTypeSystem getSystem()
    {
        return this.delegate.getSystem();
    }


    private void logAttributeChange(String msg, AttributeDTO attributeDTO)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("Preserving the DB modifier (%s) for [%s.%s]", new Object[] {msg, attributeDTO.getEnclosingTypeCode(), attributeDTO
                            .getQualifier()}));
        }
    }
}
