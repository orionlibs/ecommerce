package de.hybris.bootstrap.typesystem;

import de.hybris.bootstrap.ddl.sql.IndexCreationMode;
import de.hybris.bootstrap.typesystem.dto.AtomicTypeDTO;
import de.hybris.bootstrap.typesystem.dto.AttributeDTO;
import de.hybris.bootstrap.typesystem.dto.AttributeDeploymentDTO;
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
import de.hybris.bootstrap.typesystem.xml.BeanTagListener;
import de.hybris.bootstrap.typesystem.xml.ModelTagListener;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class YTypeSystemLoader implements YTypeSystemHandler
{
    private final YTypeSystem system;
    private static final Logger LOG = Logger.getLogger(YTypeSystemLoader.class);
    private final boolean update;


    public YTypeSystemLoader(boolean buildMode)
    {
        this(new YTypeSystem(buildMode), false);
    }


    public YTypeSystemLoader(YTypeSystem system)
    {
        this(system, false);
    }


    public YTypeSystemLoader(YTypeSystem system, boolean updateMode)
    {
        this.system = system;
        this.update = updateMode;
    }


    protected YExtension getExtension(String extName)
    {
        YExtension ret = this.system.getExtension(extName);
        if(ret == null)
        {
            throw new IllegalArgumentException("extension '" + extName + "' not found within " + this.system);
        }
        return ret;
    }


    public YAtomicType loadAtomicType(AtomicTypeDTO atomicTypeDTO)
    {
        if(atomicTypeDTO.isAutocreate())
        {
            YExtension ext;
            YAtomicType type = new YAtomicType((YNamespace)(ext = getExtension(atomicTypeDTO.getExtensionName())), atomicTypeDTO.getCode(), atomicTypeDTO.getSuperType());
            type.setAutocreate(atomicTypeDTO.isAutocreate());
            type.setGenerate(atomicTypeDTO.isGenerate());
            ext.registerTypeSystemElement((YNameSpaceElement)type);
            return type;
        }
        return null;
    }


    public YAttributeDescriptor loadAttribute(AttributeDTO dto)
    {
        if(dto.isAutocreate())
        {
            YExtension ext = getExtension(dto.getExtensionName());
            YAttributeDescriptor attr = new YAttributeDescriptor((YNamespace)ext, dto.getEnclosingTypeCode(), dto.getQualifier(), dto.getType());
            attr.setAttributeHandler(dto.getAttributeHandler());
            attr.setRedeclared(dto.isRedeclare());
            attr.setModifiers(dto.getModifiers().asInt());
            attr.setSelectionOfQualifier(dto.getSelectionOfQualifier());
            attr.setDefaultValueDefinition(dto.getDefaultValueDef());
            attr.setDescription(dto.getDescription());
            attr.addCustomProperties(dto.getProps());
            attr.setModelData(dto.getModelData());
            attr.setConfiguredPersistenceType(dto.getPersistenceType());
            if(!dto.isRedeclare())
            {
                attr.setPersistenceQualifier(dto.getPersistenceQualifier());
                attr.setPersistenceType(dto.getPersistenceType());
                if(dto.getPersistenceMapping() != null)
                {
                    attr.addDbColumnDefinitions(dto.getPersistenceMapping());
                }
            }
            attr.setAutocreate(dto.isAutocreate());
            attr.setGenerate(dto.isGenerate());
            attr.setMetaTypeCode(dto.getMetaType());
            attr.setUniqueModifier(dto.isUnique());
            ext.registerTypeSystemElement((YNameSpaceElement)attr);
            return attr;
        }
        return null;
    }


    public YAttributeDeployment loadAttributeDeployment(AttributeDeploymentDTO attributeDeploymentDTO)
    {
        YExtension ext = getExtension(attributeDeploymentDTO.getExtensionName());
        YAttributeDeployment column = new YAttributeDeployment((YNamespace)ext, attributeDeploymentDTO.getBeanName(), attributeDeploymentDTO.getQualifier(), attributeDeploymentDTO.getType(), YAttributeDeployment.TableType.CORE);
        column.setPrimaryKey(attributeDeploymentDTO.isPK());
        if(attributeDeploymentDTO.getColumnMappings() != null)
        {
            for(Map.Entry<String, BeanTagListener.DeploymentAttributeMapping> entry : (Iterable<Map.Entry<String, BeanTagListener.DeploymentAttributeMapping>>)attributeDeploymentDTO
                            .getColumnMappings().entrySet())
            {
                String dbName = entry.getKey();
                column.addPersistenceMapping(dbName, ((BeanTagListener.DeploymentAttributeMapping)entry.getValue()).getColumnName(), ((BeanTagListener.DeploymentAttributeMapping)entry.getValue()).getTypeDef(), ((BeanTagListener.DeploymentAttributeMapping)entry
                                .getValue()).isNullable());
            }
        }
        ext.registerTypeSystemElement((YNameSpaceElement)column);
        return column;
    }


    public YCollectionType loadCollectionType(CollectionTypeDTO dto)
    {
        if(dto.isAutocreate())
        {
            YExtension ext = getExtension(dto.getExtensionName());
            YCollectionType type = new YCollectionType((YNamespace)ext, dto.getCode(), dto.getElementType(), dto.getCollType());
            type.setAutocreate(dto.isAutocreate());
            type.setGenerate(dto.isGenerate());
            ext.registerTypeSystemElement((YNameSpaceElement)type);
            return type;
        }
        return null;
    }


    public YComposedType loadComposedType(ComposedTypeDTO dto)
    {
        YExtension ext = getExtension(dto.getExtensionName());
        if(dto.isAutocreate())
        {
            YComposedType type = new YComposedType((YNamespace)ext, dto.getCode(), dto.getSuperTypeCode(), dto.getJaloClassName());
            type.setAbstract(dto.isAbstract());
            type.setSingleton(dto.isSingleton());
            type.setJaloOnly(dto.isJaloOnly());
            type.setLegacyPersistence(dto.isLegacyPersistence());
            type.setDeploymentName(dto.getDeploymentName());
            if(dto.getTypeDescription() != null)
            {
                type.setTypeDescription(dto.getTypeDescription());
            }
            if(dto.getProps() != null)
            {
                type.addCustomProperties(dto.getProps());
            }
            type.setAutocreate(dto.isAutocreate());
            type.setGenerate(dto.isGenerate());
            type.setMetaTypeCode(dto.getMetaType());
            modelGeneration(type, dto.getModelData());
            ext.registerTypeSystemElement((YNameSpaceElement)type);
            return type;
        }
        YComposedType existingType = (YComposedType)ext.getTypeSystem().getType(dto.getCode());
        if(existingType != null)
        {
            if(dto.getProps() != null)
            {
                existingType.addCustomProperties(dto.getProps());
            }
            modelGeneration(existingType, dto.getModelData());
        }
        return null;
    }


    private void modelGeneration(YComposedType type, ModelTagListener.ModelData modelData)
    {
        boolean generateModelInclSuperTypes = modelData.generate;
        YComposedType superType = type.getSuperType();
        while(generateModelInclSuperTypes && superType != null)
        {
            generateModelInclSuperTypes = superType.isGenerateModel();
            superType = superType.getSuperType();
        }
        type.setGenerateModel(generateModelInclSuperTypes);
        type.addConstructorSignatures(modelData.getConstructors());
    }


    public YDBTypeMapping loadDBTypeMapping(DBTypeMappingDTO dto)
    {
        YExtension ext = getExtension(dto.getExtensionName());
        YDBTypeMapping newOne = new YDBTypeMapping((YNamespace)ext, dto.getDbName());
        newOne.setPrimaryKey(dto.getPrimKey());
        newOne.setIsNull(dto.getNullStr());
        newOne.setIsNotNull(dto.getNotNullStr());
        newOne.addTypeMappings(dto.getTypeMappings());
        ext.registerTypeSystemElement((YNameSpaceElement)newOne);
        return newOne;
    }


    public YEnumType loadEnumType(EnumTypeDTO dto)
    {
        YExtension extension = getExtension(dto.getExtensionName());
        if(dto.isAutocreate())
        {
            YEnumType type = new YEnumType((YNamespace)extension, dto.getCode());
            type.setTypeDescription(dto.getTypeDescription());
            type.setAutocreate(dto.isAutocreate());
            type.setGenerate(dto.isGenerate());
            extension.registerTypeSystemElement((YNameSpaceElement)type);
            return type;
        }
        if(extension.getTypeSystem().getType(dto.getCode()) != null)
        {
            return (YEnumType)extension.getTypeSystem().getType(dto.getCode());
        }
        return null;
    }


    public YEnumValue loadEnumValue(EnumValueDTO dto)
    {
        YExtension ext = getExtension(dto.getExtensionName());
        YEnumValue type = new YEnumValue((YNamespace)ext, dto.getEnumTypeCode(), dto.getValueCode());
        type.setPosition(dto.getSequenceNumber());
        type.setDefault(dto.isAsDefault());
        type.setDescription(dto.getDescription());
        ext.registerTypeSystemElement((YNameSpaceElement)type);
        return type;
    }


    public YFinder loadFinderDeployment(FinderDeploymentDTO dto)
    {
        YExtension ext = getExtension(dto.getExtensionName());
        YFinder finder = new YFinder((YNamespace)ext, dto.getBeanName(), dto.getName(), dto.getSig());
        if(dto.getDbMethodMappings() != null)
        {
            for(Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)dto.getDbMethodMappings().entrySet())
            {
                finder.addQueryMapping(entry.getKey(), entry.getValue());
            }
        }
        finder.setCached(dto.isCache());
        ext.registerTypeSystemElement((YNameSpaceElement)finder);
        return finder;
    }


    public YIndex loadIndex(IndexDTO dto)
    {
        YExtension ext = getExtension(dto.getExtensionName());
        YIndex index = new YIndex((YNamespace)ext, dto.getTypeCode(), dto.getIndexName(), getIndexCreationMode(dto.getCreationMode()), dto.isUnique(), dto.isRemove(), dto.isReplace());
        index.addIndexedAttributes(dto.getKeyLowerMap());
        index.addIncludeAttributes(dto.getIncludeCollection());
        ext.registerTypeSystemElement((YNameSpaceElement)index);
        return index;
    }


    private IndexCreationMode getIndexCreationMode(String indexCreationMode)
    {
        return StringUtils.isBlank(indexCreationMode) ? IndexCreationMode.ALL : IndexCreationMode.valueOf(indexCreationMode
                        .toUpperCase(Locale.US));
    }


    public YIndexDeployment loadIndexDeployment(IndexDeploymentDTO dto)
    {
        YExtension ext = getExtension(dto.getExtensionName());
        YIndexDeployment idx = new YIndexDeployment((YNamespace)ext, dto.getItemDeploymentName(), dto.getName());
        idx.setUnique(dto.isUnique());
        idx.setSQLServerClustered(dto.isSqlserverclustered());
        for(Map.Entry<String, Boolean> entry : (Iterable<Map.Entry<String, Boolean>>)dto.getFields().entrySet())
        {
            idx.addIndexedAttribute(entry.getKey(), Boolean.TRUE.equals(entry.getValue()));
        }
        ext.registerTypeSystemElement((YNameSpaceElement)idx);
        return idx;
    }


    public YDeployment loadDeployment(DeploymentDTO dto)
    {
        YDeployment depl = getSystem().getDeployment(dto.getName());
        if(depl == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Load deployment " + dto.getName());
            }
            return loadDeploymentInternal(dto.getExtensionName(), dto.getPackageName(), dto.getName(), dto.getSuperDeployment(), dto
                            .getTypeCode(), dto.isAbstract(), dto.isGeneric(), dto.isFinal(), dto.getTableName(), dto
                            .getPropsTableName(), dto
                            .isNonItemDeployment());
        }
        if(this.update)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Update deployment " + dto.getName());
            }
            updateDeployment(depl, dto.getTypeCode(), dto.getTableName());
            return depl;
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Ignore deployment " + dto.getName());
        }
        updateDeployment(depl, dto.getTypeCode(), dto.getTableName());
        return depl;
    }


    protected YDeployment loadDeploymentInternal(String extensionName, String packageName, String name, String superDeployment, int typeCode, boolean isAbstract, boolean isGeneric, boolean isFinal, String tableName, String propsTableName, boolean nonItemDepl)
    {
        YExtension ext = getExtension(extensionName);
        YDeployment newOne = new YDeployment((YNamespace)ext, packageName, name, nonItemDepl ? null : superDeployment, tableName, nonItemDepl ? 0 : typeCode);
        if(nonItemDepl)
        {
            newOne.setNonItemDeployment(true);
        }
        else
        {
            newOne.setAbstract(isAbstract);
            newOne.setGeneric(isGeneric);
            newOne.setFinal(isFinal);
            if(propsTableName != null && propsTableName.length() > 0)
            {
                newOne.setPropsTableName(propsTableName);
            }
        }
        ext.registerTypeSystemElement((YNameSpaceElement)newOne);
        return newOne;
    }


    private void updateDeployment(YDeployment depl, int typeCode, String tableName)
    {
        if(depl.getItemTypeCode() != typeCode)
        {
            LOG.info("Modification of the typecode of deployment " + depl
                            .getFullName() + " from " + depl
                            .getItemTypeCode() + " to " + typeCode + " won't be performed, be aware that the old deployment will be still used (even it is not reflected in the code base).\n");
        }
        if((tableName != null && !tableName.toLowerCase(Locale.ENGLISH).equals(depl.getTableName())) || (tableName == null && depl
                        .getTableName() != null))
        {
            LOG.info("Modification of the table name of deployment " + depl
                            .getFullName() + " from " + depl
                            .getTableName() + " to " + tableName + " won't be performed, be aware that the old deployment will be still used (even it is not reflected in the code base)");
        }
    }


    public YDeployment registerDeploymentForType(String codeOfType, String deployment)
    {
        YDeployment depl = getSystem().getDeployment(deployment);
        if(depl == null)
        {
            return null;
        }
        YType rawType = getSystem().getType(codeOfType);
        YComposedType type = null;
        if(!(rawType instanceof YComposedType))
        {
            if(getSystem().isBuildMode())
            {
                throw new IllegalStateException("No suitable type found for deployment " + deployment + ", will ignore deployment");
            }
        }
        else
        {
            type = (YComposedType)rawType;
            YDeployment existent = type.getOwnDeployment();
            if(existent != null && !depl.equals(existent))
            {
                LOG.warn("Duplicate deployment specified for type " + codeOfType + ": deployment1=" + deployment + " deployment2=" + existent
                                .getName() + "!!");
            }
            type.setDeploymentName(deployment);
            type.getNamespace().registerDeployment(type);
        }
        return depl;
    }


    public YMapType loadMapType(MapTypeDTO dto)
    {
        if(dto.isAutocreate())
        {
            YExtension ext = getExtension(dto.getExtensionName());
            YMapType type = new YMapType((YNamespace)ext, dto.getCode(), dto.getArgumentType(), dto.getReturnType());
            type.setAutocreate(dto.isAutocreate());
            type.setGenerate(dto.isGenerate());
            ext.registerTypeSystemElement((YNameSpaceElement)type);
            return type;
        }
        return null;
    }


    public YRelation loadRelation(RelationTypeDTO dto)
    {
        if(dto.isAutocreate())
        {
            YExtension ext = getExtension(dto.getExtensionName());
            boolean oneToMany = (dto.getSrcCard() == YRelationEnd.Cardinality.ONE || dto.getTgtCard() == YRelationEnd.Cardinality.ONE);
            YRelation rel = oneToMany ? new YRelation((YNamespace)ext, dto.getCode(), "Item", null) : new YRelation((YNamespace)ext, dto.getCode());
            rel.setMetaTypeCode(dto.getMetaType());
            rel.setAutocreate(dto.isAutocreate());
            rel.setGenerate(dto.isGenerate());
            rel.setLocalized(dto.isLocalized());
            if(dto.getDeployment() != null)
            {
                rel.setDeploymentName(dto.getDeployment());
            }
            YRelationEnd src = new YRelationEnd((YNamespace)ext, dto.getCode(), dto.getSrcRole(), dto.getSrcType(), dto.isSrcNavigable(), true);
            if(dto.getSrcCard() != null)
            {
                src.setCardinality(dto.getSrcCard());
            }
            src.setOrdered(dto.isSrcOrdered());
            if(dto.getSrcMetaType() != null)
            {
                src.setMetaTypeCode(dto.getSrcMetaType());
            }
            src.setModifiers(dto.getSrcModifiers());
            if(dto.getSrcCollType() != null)
            {
                src.setCollectionType(dto.getSrcCollType());
            }
            if(dto.getSrcProps() != null)
            {
                src.addCustomProperties(dto.getSrcProps());
            }
            src.setUniqueModifier(dto.isSrcUniqueModifier());
            src.setDescription(dto.getSrcDescription());
            src.setModelData(dto.getSrcModelData());
            rel.setSourceEnd(src);
            YRelationEnd tgt = new YRelationEnd((YNamespace)ext, dto.getCode(), dto.getTgtRole(), dto.getTgtType(), dto.isTgtNavigable(), false);
            if(dto.getTgtCard() != null)
            {
                tgt.setCardinality(dto.getTgtCard());
            }
            tgt.setOrdered(dto.isTgtOrdered());
            if(dto.getTgtMetaType() != null)
            {
                tgt.setMetaTypeCode(dto.getTgtMetaType());
            }
            tgt.setModifiers(dto.getTgtModifiers());
            if(dto.getTgtCollType() != null)
            {
                tgt.setCollectionType(dto.getTgtCollType());
            }
            if(dto.getTgtProps() != null)
            {
                tgt.addCustomProperties(dto.getTgtProps());
            }
            tgt.setUniqueModifier(dto.isTgtUniqueModifier());
            tgt.setDescription(dto.getTgtDescription());
            tgt.setModelData(dto.getTgtModelData());
            rel.setTargetEnd(tgt);
            if(oneToMany)
            {
                rel.setAbstract(true);
            }
            ext.registerTypeSystemElement((YNameSpaceElement)rel);
            return rel;
        }
        return null;
    }


    public YExtension addExtension(String extensionName, Set<String> requires)
    {
        YExtension ret = this.system.getExtension(extensionName);
        if(ret == null)
        {
            this.system.addExtension(ret = new YExtension(this.system, extensionName, requires));
        }
        return ret;
    }


    public YTypeSystem getSystem()
    {
        return this.system;
    }


    public void finish()
    {
        getSystem().finalizeTypeSystem();
    }


    public void validate()
    {
        getSystem().validate();
    }
}
