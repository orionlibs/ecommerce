package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.typesystem.YAtomicType;
import de.hybris.bootstrap.typesystem.YAttributeDeployment;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YDBTypeMapping;
import de.hybris.bootstrap.typesystem.YDeployment;
import de.hybris.bootstrap.typesystem.YEnumType;
import de.hybris.bootstrap.typesystem.YEnumValue;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YFinder;
import de.hybris.bootstrap.typesystem.YIndex;
import de.hybris.bootstrap.typesystem.YIndexDeployment;
import de.hybris.bootstrap.typesystem.YMapType;
import de.hybris.bootstrap.typesystem.YRelation;
import de.hybris.bootstrap.typesystem.YRelationEnd;
import de.hybris.bootstrap.typesystem.YTypeSystemHandler;
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
import de.hybris.bootstrap.xml.DefaultTagListener;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.Parser;
import de.hybris.bootstrap.xml.TagListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class HybrisTypeSystemParser extends Parser
{
    private static final Logger LOG = Logger.getLogger(HybrisTypeSystemParser.class.getName());
    private final boolean CLOB_SELF_HEALING_ENABLED;
    private final HybrisTypeSystemTagListener types_tl = new HybrisTypeSystemTagListener(this);
    private final DeploymentModelTagListener depl_tl = new DeploymentModelTagListener(this);
    private final TypeSystemDeploymentsTagListener typesDepl_tl = new TypeSystemDeploymentsTagListener(this);
    private final YTypeSystemHandler handler;
    private String currentExtensionName;
    private String currentFileName;


    public HybrisTypeSystemParser(YTypeSystemHandler handler, boolean buildMode)
    {
        super(null, null);
        this.handler = handler;
        this.CLOB_SELF_HEALING_ENABLED = !"false".equalsIgnoreCase(System.getenv("hybris_adjustMissingCLOBs"));
    }


    public YTypeSystemHandler getHandler()
    {
        return this.handler;
    }


    public void parseExtensionSystem(String extName, InputStream stream) throws ParseAbortException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("*******************************");
            LOG.debug("* Parsing items.xml for extension " + extName);
        }
        if(stream == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("no items.xml found for " + extName);
            }
        }
        else
        {
            try
            {
                this.currentFileName = extName + "-items.xml";
                setCurrentExtensionName(extName);
                parse(stream, "utf-8", (TagListener)this.types_tl);
            }
            finally
            {
                this.currentFileName = null;
                setCurrentExtensionName(null);
                try
                {
                    stream.close();
                }
                catch(IOException e)
                {
                    LOG.debug("Error while closing stream", e);
                }
            }
        }
    }


    public void parseExtensionDeploymentsFromSystem(YExtension extension, InputStream stream) throws ParseAbortException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("****************************************************");
            LOG.debug("* Parsing items.xml (Only Deployments) for extension " + extension.getExtensionName());
        }
        if(stream == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("no items.xml found for " + extension.getExtensionName());
            }
        }
        else
        {
            try
            {
                this.currentFileName = extension.getExtensionName() + "-items.xml";
                setCurrentExtensionName(extension.getExtensionName());
                parse(stream, "utf-8", (TagListener)this.typesDepl_tl);
            }
            finally
            {
                this.currentFileName = null;
                setCurrentExtensionName(null);
                try
                {
                    stream.close();
                }
                catch(IOException e)
                {
                    LOG.debug("Error while closing stream", e);
                }
            }
        }
    }


    public void parseExtensionDeployments(YExtension extension, InputStream stream) throws ParseAbortException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("*********************************************");
            LOG.debug("* Parsing advanced-deployment.xml for extension " + extension.getExtensionName());
        }
        if(stream == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("no advanced-deployment.xml found for " + extension.getExtensionName());
            }
        }
        else
        {
            String extName = extension.getExtensionName();
            try
            {
                this.currentFileName = extName + "-advanced-deployment.xml";
                setCurrentExtensionName(extName);
                parse(stream, "utf-8", (TagListener)this.depl_tl);
            }
            finally
            {
                this.currentFileName = null;
                setCurrentExtensionName(null);
                try
                {
                    stream.close();
                }
                catch(IOException e)
                {
                    LOG.debug("Error while closing stream", e);
                }
            }
        }
    }


    public void startLoadingExtensionDeployments()
    {
    }


    public void startLoadingExtension()
    {
        getHandler().addExtension(getCurrentExtensionName(), Collections.EMPTY_SET);
    }


    public void finishedLoadingExtensionDeployments()
    {
    }


    public void finishedLoadingExtension()
    {
    }


    protected void setCurrentExtensionName(String name)
    {
        this.currentExtensionName = name;
    }


    protected String getCurrentExtensionName()
    {
        return this.currentExtensionName;
    }


    protected String getCurrentXMLLocation()
    {
        TagListener tagListener = getCurrentTagListener();
        if(!(tagListener instanceof DefaultTagListener) || ((DefaultTagListener)tagListener).getStartLineNumber() >= 0)
        {
            return this.currentFileName + ":" + this.currentFileName + "(" + ((DefaultTagListener)tagListener).getStartLineNumber() + ")";
        }
        return this.currentFileName + ":n/a";
    }


    public void loadNewAtomicType(String className, String superType, boolean autocreate, boolean generate)
    {
        AtomicTypeDTO atomicTypeDTO = new AtomicTypeDTO(getCurrentExtensionName(), className, superType, autocreate, generate);
        YAtomicType type = getHandler().loadAtomicType(atomicTypeDTO);
        if(type != null)
        {
            type.setLoaderInfo(getCurrentXMLLocation());
        }
    }


    protected YCollectionType.TypeOfCollection parseTypeOfCollection(String typeCode, String typeOfCollection)
    {
        YCollectionType.TypeOfCollection toc;
        if(typeOfCollection == null || typeOfCollection.length() == 0)
        {
            toc = null;
        }
        else if("set".equalsIgnoreCase(typeOfCollection))
        {
            toc = YCollectionType.TypeOfCollection.SET;
        }
        else if("list".equalsIgnoreCase(typeOfCollection))
        {
            toc = YCollectionType.TypeOfCollection.LIST;
        }
        else if("collection".equalsIgnoreCase(typeOfCollection))
        {
            toc = YCollectionType.TypeOfCollection.COLLECTION;
        }
        else
        {
            throw new IllegalArgumentException("invalid type of collection '" + typeOfCollection + "' for type '" + typeCode + "' in extension " +
                            getCurrentExtensionName());
        }
        return toc;
    }


    protected YRelationEnd.Cardinality parseCardinality(String typeCode, String card)
    {
        YRelationEnd.Cardinality ret;
        if(card == null || card.length() == 0)
        {
            ret = null;
        }
        else if("one".equalsIgnoreCase(card))
        {
            ret = YRelationEnd.Cardinality.ONE;
        }
        else if("many".equalsIgnoreCase(card))
        {
            ret = YRelationEnd.Cardinality.MANY;
        }
        else
        {
            throw new IllegalArgumentException("invalid cardinality '" + card + "' for type '" + typeCode + "' in extension " +
                            getCurrentExtensionName() + " - expected 'one' or 'many'");
        }
        return ret;
    }


    public void loadNewCollectionType(String code, String elementTypeCode, String typeOfCollection, boolean autocreate, boolean generate)
    {
        CollectionTypeDTO collectionTypeDTO = new CollectionTypeDTO(getCurrentExtensionName(), code, elementTypeCode, parseTypeOfCollection(code, typeOfCollection), autocreate, generate);
        YCollectionType type = getHandler().loadCollectionType(collectionTypeDTO);
        if(type != null)
        {
            type.setLoaderInfo(getCurrentXMLLocation());
        }
    }


    public void loadNewEnumValue(String enumTypeCode, String valueCode, int sequenceNumber, boolean asDefault, String description)
    {
        EnumValueDTO enumValueDTO = new EnumValueDTO(getCurrentExtensionName(), enumTypeCode, valueCode, sequenceNumber, asDefault, description);
        YEnumValue type = getHandler().loadEnumValue(enumValueDTO);
        if(type != null)
        {
            type.setLoaderInfo(getCurrentXMLLocation());
        }
    }


    public void loadNewEnumType(String code, String jaloClass, boolean autocreate, boolean generate, boolean dynamic, String typeDescription, String modelPackageName, String deprecatedSince)
    {
        EnumTypeDTO enumTypeDTO = new EnumTypeDTO(getCurrentExtensionName(), code, jaloClass, autocreate, generate, typeDescription);
        YEnumType type = getHandler().loadEnumType(enumTypeDTO);
        if(type != null)
        {
            type.setDynamic(dynamic);
            type.setLoaderInfo(getCurrentXMLLocation());
            if(modelPackageName != null)
            {
                type.setModelPackage(modelPackageName);
            }
            type.setDeprecatedSince(deprecatedSince);
        }
    }


    public void loadNewMapType(String code, String argumentTypeCode, String returnTypeCode, boolean autocreate, boolean generate)
    {
        MapTypeDTO mapTypeDTO = new MapTypeDTO(getCurrentExtensionName(), code, argumentTypeCode, returnTypeCode, autocreate, generate);
        YMapType type = getHandler().loadMapType(mapTypeDTO);
        if(type != null)
        {
            type.setLoaderInfo(getCurrentXMLLocation());
        }
    }


    public void loadNewRelationType(String code, String metaType, String jaloClassName, String srcRole, String srcType, boolean srcNavigable, int srcModifiers, boolean srcUniquemodifier, String srcCardinality, boolean srcOrdered, String srcCollType, Map<String, String> srcProps, String srcMetaType,
                    String srcDescription, ModelTagListener.ModelData srcModelData, String tgtRole, String tgtType, boolean tgtNavigable, int tgtModifiers, boolean tgtUniquemodifier, String tgtCardinality, boolean tgtOrdered, String tgtCollType, Map<String, String> tgtProps, String tgtMetaType,
                    String tgtDescription, ModelTagListener.ModelData tgtModelData, String oldDeployment, String newDeployment, boolean localized, boolean autocreate, boolean generate)
    {
        if(oldDeployment != null && newDeployment != null)
        {
            throw new IllegalStateException("Old and new style deployment definition found for relation " + code + " at " +
                            getCurrentXMLLocation()
                            + ".\nPlease use old-style\n   <relation code=\"XY\" .. deployment=\"de.persistence.XY\"/>\nor new-style\n   <relation code=\"XY\" .. >\n      <deployment table=\"XYs\" typecode=\"123\"/>\n   </relation>\nbut never both variants at same time.\nRecommended is to use the second variant (new-style).");
        }
        if(!srcNavigable && ("one".equals(tgtCardinality) || "one".equals(srcCardinality)))
        {
            throw new IllegalStateException("You can not mark sourceElement of relation " + code + " as non-navigable because it is a 1:n relation. It is only supported for n:m relations. " +
                            getCurrentXMLLocation() + ".\nIn case you want to model it non-navigable you have to set navigable to false explicitly.");
        }
        if(srcRole == null && srcNavigable)
        {
            throw new IllegalStateException("No qualifier provided for sourceElement of relation " + code + " at " +
                            getCurrentXMLLocation() + ".\nIn case you want to model this relation side as non-navigable you have to set navigable to false explicitly.");
        }
        if(srcRole != null && !srcNavigable)
        {
            throw new IllegalStateException("Qualifier provided for sourceElement of relation " + code + " although you have set navigable to false at " +
                            getCurrentXMLLocation() + ".\n");
        }
        if(!tgtNavigable && ("one".equals(srcCardinality) || "one".equals(tgtCardinality)))
        {
            throw new IllegalStateException("You can not mark targetElement of relation " + code + " as non-navigable because it is a 1:n relation. It is only supported for n:m relations. " +
                            getCurrentXMLLocation() + ".\nIn case you want to model it non-navigable you have to set navigable to false explicitly.");
        }
        if(tgtRole == null && tgtNavigable)
        {
            throw new IllegalStateException("No qualifier provided for targetElement of relation " + code + " at " +
                            getCurrentXMLLocation() + ".\nIn case you want to model this relation side as non-navigable you have to set navigable to false explicitly.");
        }
        if(tgtRole != null && !tgtNavigable)
        {
            throw new IllegalStateException("Qualifier provided for targetElement of relation " + code + " although you have set navigable to false at " +
                            getCurrentXMLLocation() + ".\n");
        }
        if(!srcNavigable && !tgtNavigable)
        {
            throw new IllegalStateException("Both sides of relation " + code + " are marked as non-navigable. " +
                            getCurrentXMLLocation() + ".\n");
        }
        if("one".equals(srcCardinality) && srcCollType != null)
        {
            LOG.warn("The source attribute in the relation '" + code + "' is of ONE cardinality but it has a collection type as '" + srcCollType + "', this will be ignored. See: " +
                            getCurrentXMLLocation());
        }
        if("one".equals(tgtCardinality) && tgtCollType != null)
        {
            LOG.warn("The target attribute in the relation '" + code + "' is of ONE cardinality but it has a collection type as '" + tgtCollType + "', this will be ignored. See: " +
                            getCurrentXMLLocation());
        }
        RelationTypeDTO relationTypeDTO = new RelationTypeDTO(getCurrentExtensionName(), code, metaType, jaloClassName, srcRole, srcType, srcNavigable, srcModifiers, srcUniquemodifier, parseCardinality(code, srcCardinality), srcOrdered, parseTypeOfCollection(code, srcCollType), srcProps,
                        srcMetaType, srcDescription, srcModelData, tgtRole, tgtType, tgtNavigable, tgtModifiers, tgtUniquemodifier, parseCardinality(code, tgtCardinality), tgtOrdered, parseTypeOfCollection(code, tgtCollType), tgtProps, tgtMetaType, tgtDescription, tgtModelData,
                        (oldDeployment != null) ? oldDeployment : newDeployment, localized, autocreate, generate);
        YRelation realtion = getHandler().loadRelation(relationTypeDTO);
        if(realtion != null)
        {
            realtion.setLoaderInfo(getCurrentXMLLocation());
            realtion.getSourceEnd().setLoaderInfo(getCurrentXMLLocation());
            realtion.getTargetEnd().setLoaderInfo(getCurrentXMLLocation());
        }
        if(srcCardinality == null || tgtCardinality == null)
        {
            LOG.warn("     ");
            LOG.warn("*****************************************************************************************");
            LOG.warn(" No cardinality set at either one or both relation ends of relation");
            LOG.warn("   " + code + " at " + getCurrentXMLLocation() + ".");
            LOG.warn(" As default cardinality \"many\" will be used!");
            LOG.warn(" Please specify the cardinalities at the sourceElement/targetElement tags of the relation.");
            LOG.warn(" For example: <sourceElement qualifier=\"user\" type=\"User\" cardinality=\"one\">.");
            LOG.warn(" In further releases this attribute will be mandatory.");
            LOG.warn("*****************************************************************************************");
            LOG.warn("    ");
        }
    }


    public void loadNewItemAttribute(String enclosingTypeCode, String qualifier, String type, int modifiers, boolean redeclare, String selectionOfQualifier, String persistenceTypeStr, String persistenceQualifier, Map<String, String> persistenceMapping, String attributeHandler,
                    String defaultValueDef, String description, Map<String, String> props, String metaType, boolean autocreate, boolean generate, ModelTagListener.ModelData modelData, boolean unique)
    {
        int wrapperModifiers;
        if(!redeclare && (persistenceTypeStr == null || persistenceTypeStr.length() == 0))
        {
            LOG.warn("     ");
            LOG.warn("*****************************************************************************************");
            LOG.warn(" Found attribute without persistence definition : " + enclosingTypeCode + "." + qualifier);
            LOG.warn("   at " + getCurrentXMLLocation() + ".");
            LOG.warn(" As default persistence type \"jalo\" will be used!");
            LOG.warn(" Please specify the persistence type for this attribute.");
            LOG.warn(" For example: <persistence type=\"property\"/>.");
            LOG.warn(" In further releases this attribute will be mandatory.");
            LOG.warn("*****************************************************************************************");
            LOG.warn("    ");
        }
        String wrapperType = null;
        if(boolean.class.getName().equals(type))
        {
            wrapperType = Boolean.class.getName();
        }
        else if(float.class.getName().equals(type))
        {
            wrapperType = Float.class.getName();
        }
        else if(long.class.getName().equals(type))
        {
            wrapperType = Long.class.getName();
        }
        else if(int.class.getName().equals(type))
        {
            wrapperType = Integer.class.getName();
        }
        else if(short.class.getName().equals(type))
        {
            wrapperType = Short.class.getName();
        }
        else if(byte.class.getName().equals(type))
        {
            wrapperType = Byte.class.getName();
        }
        else if(double.class.getName().equals(type))
        {
            wrapperType = Double.class.getName();
        }
        else if(char.class.getName().equals(type))
        {
            wrapperType = Character.class.getName();
        }
        if(wrapperType == null)
        {
            wrapperModifiers = modifiers;
        }
        else
        {
            wrapperModifiers = modifiers | 0x10000;
        }
        Map<String, String> adjustedPersistenceMappings = selfHealPersistenceMappings(getCurrentExtensionName(), enclosingTypeCode, qualifier, persistenceMapping);
        AttributeDTO attributeDTO = new AttributeDTO(getCurrentExtensionName(), enclosingTypeCode, qualifier, (wrapperType == null) ? type : wrapperType, new AttributeModifierDTO(wrapperModifiers), redeclare, selectionOfQualifier,
                        YAttributeDescriptor.PersistenceType.getPersistenceType(persistenceTypeStr), persistenceQualifier, adjustedPersistenceMappings, defaultValueDef, description, props, metaType, autocreate, generate, modelData, unique, attributeHandler);
        YAttributeDescriptor attr = getHandler().loadAttribute(attributeDTO);
        if(attr != null)
        {
            attr.setLoaderInfo(getCurrentXMLLocation());
        }
    }


    private static final Set<String> DB_TYPES = new LinkedHashSet<>(Arrays.asList(new String[] {"sap", "hsqldb", "mysql", "oracle", "postgresql", "sqlserver"}));


    protected Map<String, String> selfHealPersistenceMappings(String extension, String type, String attribute, Map<String, String> parsedMappings)
    {
        Map<String, String> adjusted = parsedMappings;
        if(this.CLOB_SELF_HEALING_ENABLED)
        {
            if(fixWrongHanaCLOBColumn(parsedMappings))
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Wrong HANA (N)CLOB mapping for attribute " + type + "." + attribute + " from extension " + extension + " - changing to hana=NCLOB");
                }
            }
            if(hasCLOBColumn(parsedMappings))
            {
                boolean hasDefaultLongStringMapping = hasUniversalLongStringMapping(parsedMappings);
                adjusted = new LinkedHashMap<>(parsedMappings);
                for(String missingDB : getMissingDatabaseType(parsedMappings, DB_TYPES))
                {
                    if(!hasDefaultLongStringMapping || requiresExplicitCLOBInsteadOfLONG_STRING(missingDB))
                    {
                        String defaultCLOB = getDefaultCLOBTypeFor(missingDB);
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug("Missing db specific CLOB mapping for attribute " + type + "." + attribute + " from extension " + extension + " - adding " + missingDB + "=" + defaultCLOB);
                        }
                        adjusted.put(missingDB, defaultCLOB);
                    }
                }
            }
        }
        return adjusted;
    }


    protected boolean requiresExplicitCLOBInsteadOfLONG_STRING(String dbName)
    {
        switch(dbName)
        {
            case "sap":
            case "oracle":
                return true;
            case "hsqldb":
            case "mysql":
            case "sqlserver":
            case "postgresql":
                return false;
        }
        throw new IllegalArgumentException("Unsupported database type '" + dbName + "'");
    }


    protected String getDefaultCLOBTypeFor(String dbName)
    {
        switch(dbName)
        {
            case "sap":
                return "NCLOB";
            case "hsqldb":
                return "LONGVARCHAR";
            case "mysql":
                return "TEXT";
            case "oracle":
                return "CLOB";
            case "sqlserver":
                return "NVARCHAR(MAX)";
            case "postgresql":
                return "TEXT";
        }
        throw new IllegalArgumentException("Unsupported database type '" + dbName + "'");
    }


    protected boolean fixWrongHanaCLOBColumn(Map<String, String> parsedMappings)
    {
        if(MapUtils.isNotEmpty(parsedMappings))
        {
            for(Map.Entry<String, String> e : parsedMappings.entrySet())
            {
                String database = e.getKey();
                String columnType = e.getValue();
                if("sap".equalsIgnoreCase(database) && StringUtils.isNotBlank(columnType) && "CLOB"
                                .equalsIgnoreCase(columnType.trim()))
                {
                    e.setValue("NCLOB");
                    return true;
                }
            }
        }
        return false;
    }


    protected boolean hasCLOBColumn(Map<String, String> parsedMappings)
    {
        if(MapUtils.isNotEmpty(parsedMappings))
        {
            for(Map.Entry<String, String> e : parsedMappings.entrySet())
            {
                String columnType = e.getValue();
                if(StringUtils.isNotBlank(columnType))
                {
                    columnType = columnType.trim();
                    if(columnType.matches("(?i)N?CLOB") || columnType
                                    .matches("(?i)TEXT") || columnType
                                    .matches("(?i)NVARCHAR\\s*\\(\\s*MAX\\s*\\)"))
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    protected boolean hasUniversalLongStringMapping(Map<String, String> parsedMappings)
    {
        if(MapUtils.isNotEmpty(parsedMappings))
        {
            for(Map.Entry<String, String> e : parsedMappings.entrySet())
            {
                String database = e.getKey();
                String type = e.getValue();
                if(StringUtils.isBlank(database) && "HYBRIS.LONG_STRING".equalsIgnoreCase(type))
                {
                    return true;
                }
            }
        }
        return false;
    }


    protected Set<String> getMissingDatabaseType(Map<String, String> parsedMappings, Set<String> requiredDatabaseTypesLowerCase)
    {
        Set<String> missing = new LinkedHashSet<>(requiredDatabaseTypesLowerCase);
        if(MapUtils.isNotEmpty(parsedMappings))
        {
            for(Map.Entry<String, String> e : parsedMappings.entrySet())
            {
                String database = e.getKey();
                if(StringUtils.isNotBlank(database))
                {
                    missing.remove(database.trim().toLowerCase(LocaleHelper.getPersistenceLocale()));
                }
            }
        }
        return missing;
    }


    public void loadNewIndex(String typeCode, String indexName, String creationMode, boolean unique, Map<String, Boolean> keyLowerMap, boolean remove, boolean replace)
    {
        loadNewIndex(typeCode, indexName, creationMode, unique, keyLowerMap, remove, replace, null);
    }


    public void loadNewIndex(String typeCode, String indexName, String creationMode, boolean unique, Map<String, Boolean> keyLowerMap, boolean remove, boolean replace, Collection<String> includeLowerMap)
    {
        IndexDTO indexDTO = new IndexDTO(getCurrentExtensionName(), typeCode, indexName, creationMode, unique, keyLowerMap, remove, replace, includeLowerMap);
        YIndex index = getHandler().loadIndex(indexDTO);
        if(index != null)
        {
            index.setLoaderInfo(getCurrentXMLLocation());
        }
    }


    protected String adjustSuperType(String code, String superType)
    {
        if("Item".equalsIgnoreCase(code))
        {
            return superType;
        }
        if(superType == null || superType.length() == 0)
        {
            return "GenericItem";
        }
        return superType;
    }


    public void loadNewItemType(String code, String superTypeCode, String jaloClassName, boolean isAbstract, boolean isSingleton, boolean isJaloOnly, String metaType, String oldDeployment, String newDeployment, boolean autocreate, boolean generate, Map<String, String> props,
                    ModelTagListener.ModelData modelData, String typeDescription, boolean legacyPersistence, String deprecatedSince)
    {
        if(generate && (jaloClassName == null || jaloClassName.length() == 0))
        {
            LOG.debug("     \n*****************************************************************************************\n Found generated item type without jalo class definition : " + code + " at " +
                            getCurrentXMLLocation() + "\n A class " + code + " will be generated at the jalo package of mentioned extension !\n Please specify the jalo class at the item type tag.\n*****************************************************************************************\n    ");
        }
        if(oldDeployment != null && newDeployment != null)
        {
            throw new IllegalStateException("Old and new style deployment definition found for type " + code + " at " +
                            getCurrentXMLLocation()
                            + ".\nPlease use old-style\n   <itemtype code=\"XY\" .. deployment=\"de.persistence.XY\"/>\nor new-style\n   <itemtype code=\"XY\" .. >\n      <deployment table=\"XYs\" typecode=\"123\"/>\n   </itemtype>\nbut never both variants at same time.\nIf you extend GenericItem please always use the second one (new-style).");
        }
        ComposedTypeDTO composedTypeDTO = new ComposedTypeDTO(getCurrentExtensionName(), code, adjustSuperType(code, superTypeCode), jaloClassName, isAbstract, isSingleton, isJaloOnly, metaType, (oldDeployment != null) ? oldDeployment : newDeployment, autocreate, generate, props, modelData,
                        typeDescription, legacyPersistence);
        YComposedType type = getHandler().loadComposedType(composedTypeDTO);
        if(type != null)
        {
            type.setLoaderInfo(getCurrentXMLLocation());
            type.setDeprecatedSince(deprecatedSince);
        }
    }


    public void addDBTypeMapping(String dbName, String primKey, String nullStr, String notNullStr, Map<String, String> typeMappings)
    {
        YDBTypeMapping dbm = getHandler().loadDBTypeMapping(new DBTypeMappingDTO(
                        getCurrentExtensionName(), dbName, primKey, nullStr, notNullStr, typeMappings));
        if(dbm != null)
        {
            dbm.setLoaderInfo(getCurrentXMLLocation());
        }
    }


    public void addPlainTableDeployment(String packageName, String name, String tableName)
    {
        YDeployment depl = getHandler().loadDeployment(new DeploymentDTO(
                        getCurrentExtensionName(), packageName, name, null, 0, false, false, false, tableName, null, true));
        if(depl != null)
        {
            depl.setLoaderInfo(getCurrentXMLLocation());
        }
    }


    public void addDeployment(String packageName, String name, String superDeployment, int typeCode, boolean isAbstract, boolean isGeneric, boolean isFinal, String tableName, String propsTableName)
    {
        YDeployment depl = getHandler().loadDeployment(new DeploymentDTO(
                        getCurrentExtensionName(), packageName, name, superDeployment, typeCode, isAbstract, isGeneric, isFinal, tableName, propsTableName, false));
        if(depl != null)
        {
            depl.setLoaderInfo(getCurrentXMLLocation());
        }
    }


    public void registerDeploymentForType(String codeOfType, String deployment)
    {
        getHandler().registerDeploymentForType(codeOfType, deployment);
    }


    public void addDeploymentIndex(String beanName, String name, boolean unique, boolean sqlserverclustered, Map<String, Boolean> fields)
    {
        IndexDeploymentDTO indexDeploymentDTO = new IndexDeploymentDTO(getCurrentExtensionName(), beanName, name, unique, sqlserverclustered, fields);
        YIndexDeployment idx = getHandler().loadIndexDeployment(indexDeploymentDTO);
        if(idx != null)
        {
            idx.setLoaderInfo(getCurrentXMLLocation());
        }
    }


    protected String convertAttributeTypes(String attrType)
    {
        if(attrType.equalsIgnoreCase("HYBRIS.PK"))
        {
            return "de.hybris.platform.util.ItemPropertyValue";
        }
        if(attrType.equalsIgnoreCase("HYBRIS.COMMA_SEPARATED_PKS"))
        {
            return "de.hybris.platform.util.ItemPropertyValueCollection";
        }
        if(attrType.equalsIgnoreCase("HYBRIS.LONG_STRING"))
        {
            return "de.hybris.platform.util.ItemPropertyValueCollection";
        }
        if(attrType.equalsIgnoreCase("HYBRIS.JSON"))
        {
            return "de.hybris.platform.util.ItemPropertyValueCollection";
        }
        return attrType;
    }


    public void addDeploymentAttribute(String beanName, String qualifier, String type, boolean isPK, Map<String, BeanTagListener.DeploymentAttributeMapping> columnMappings)
    {
        YAttributeDeployment depl = getHandler().loadAttributeDeployment(new AttributeDeploymentDTO(
                        getCurrentExtensionName(), beanName, qualifier, convertAttributeTypes(type), isPK, columnMappings));
        if(depl != null)
        {
            depl.setLoaderInfo(getCurrentXMLLocation());
        }
    }


    protected Map<String, String> normalizeQueries(Map<String, String> finderQueries)
    {
        if(finderQueries == null)
        {
            return null;
        }
        if(finderQueries.isEmpty())
        {
            return finderQueries;
        }
        Map<String, String> ret = new HashMap<>(finderQueries.size());
        for(Map.Entry<String, String> e : finderQueries.entrySet())
        {
            ret.put(e.getKey(), (e.getValue() != null) ? ((String)e.getValue()).replace("\n", " ").replace("\r", " ") : null);
        }
        return ret;
    }


    public void addDeploymentFinder(String beanName, String name, List<String> sig, Map<String, String> dbMethodMappings, boolean cache)
    {
        YFinder finder = getHandler().loadFinderDeployment(new FinderDeploymentDTO(
                        getCurrentExtensionName(), beanName, name, sig, normalizeQueries(dbMethodMappings), cache));
        if(finder != null)
        {
            finder.setLoaderInfo(getCurrentXMLLocation());
        }
    }
}
