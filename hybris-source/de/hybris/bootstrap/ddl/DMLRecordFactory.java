package de.hybris.bootstrap.ddl;

import bsh.EvalError;
import bsh.Interpreter;
import com.google.common.base.Joiner;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.ddl.model.YColumn;
import de.hybris.bootstrap.ddl.model.YDbModel;
import de.hybris.bootstrap.ddl.model.YRecord;
import de.hybris.bootstrap.ddl.model.YTable;
import de.hybris.bootstrap.ddl.pk.PkFactory;
import de.hybris.bootstrap.typesystem.YAtomicType;
import de.hybris.bootstrap.typesystem.YAttributeDeployment;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YDeployment;
import de.hybris.bootstrap.typesystem.YEnumType;
import de.hybris.bootstrap.typesystem.YEnumValue;
import de.hybris.bootstrap.typesystem.YExtension;
import de.hybris.bootstrap.typesystem.YMapType;
import de.hybris.bootstrap.typesystem.YRelation;
import de.hybris.bootstrap.typesystem.YRelationEnd;
import de.hybris.bootstrap.typesystem.YType;
import de.hybris.bootstrap.typesystem.YTypeSystem;
import de.hybris.bootstrap.typesystem.YTypeSystemElement;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.util.DefaultValueExpressionHolder;
import de.hybris.platform.util.ItemPropertyValue;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Database;
import org.apache.log4j.Logger;

public class DMLRecordFactory
{
    private static final Logger LOG = Logger.getLogger(DMLRecordFactory.class);
    private static final Pattern EM_LITERAL_PATTERN = Pattern.compile("\\s*em\\s*\\(\\s*\\)\\s*\\.\\s*getEnumerationValue\\s*\\([^,]+,\\s*\"([^\"]+)\"\\s*\\)");
    private static final Pattern EM_CONSTANTS_PATTERN = Pattern.compile("\\s*em\\s*\\(\\s*\\)\\s*\\.\\s*getEnumerationValue\\s*\\([^,]+,\\s*([^\\)]+)\\s*\\)");
    private static final String EN = "_en";
    private final Map<String, YRecord> yDeployments = new LinkedHashMap<>();
    protected final Map<String, YRecord> yComposedTypes = new LinkedHashMap<>();
    private final Map<String, YRecord> yAtomicTypes = new LinkedHashMap<>();
    private final Map<String, YRecord> yMapTypes = new LinkedHashMap<>();
    private final Map<String, YRecord> yCollectionTypes = new LinkedHashMap<>();
    private final Map<String, YRecord> yEnumearationValues = new LinkedHashMap<>();
    protected final Map<String, YRecord> yAttributeDescriptors = new LinkedHashMap<>();
    private final Map<String, YRecord> countryCurrencyAndLanguage = new LinkedHashMap<>();
    private final Map<String, YRecord> numberSeriesRecords = new LinkedHashMap<>();
    private final Map<String, YRecord> metaInformationMap = new LinkedHashMap<>();
    private final Map<String, YRecord> securityCredentials = new LinkedHashMap<>();
    private final Map<String, YRecord> mediaFolderMap = new LinkedHashMap<>();
    protected final Map<String, YRecord> propsMap = new LinkedHashMap<>();
    private final YDbModel dbModel;
    private final Database database;
    private final HybrisPlatform platform;
    private final YTypeSystem typeSystem;
    private final Interpreter interpreter = new Interpreter();
    private final InitUserPasswordService initUserPasswordService;
    private final CodeGenerator codeGenerator;
    private final PkFactory pkFactory;
    private final int propertyMaxLength;
    private final String typeSystemName;
    private final String tablePrefix;


    public DMLRecordFactory(YDbModel dbModel, Database database, YTypeSystem typeSystem, HybrisPlatform platform, CodeGenerator codeGenerator, PkFactory pkFactory, int propertyMaxLength, String typeSystemName, String tablePrefix, PropertiesLoader properties)
    {
        this.dbModel = dbModel;
        this.database = database;
        this.typeSystem = typeSystem;
        this.platform = platform;
        this.codeGenerator = codeGenerator;
        this.pkFactory = pkFactory;
        this.propertyMaxLength = propertyMaxLength;
        this.typeSystemName = Objects.<String>requireNonNull(typeSystemName);
        this.tablePrefix = tablePrefix;
        this.initUserPasswordService = new InitUserPasswordService(properties);
    }


    public Collection<YRecord> getYrecords()
    {
        Collection<YRecord> all = getTypeSystemRecords();
        all.addAll(this.numberSeriesRecords.values());
        return all;
    }


    private Collection<YRecord> getTypeSystemRecords()
    {
        Collection<YRecord> all = new ArrayList<>();
        all.addAll(this.yAtomicTypes.values());
        all.addAll(this.yComposedTypes.values());
        all.addAll(this.yMapTypes.values());
        all.addAll(this.yCollectionTypes.values());
        all.addAll(this.yEnumearationValues.values());
        all.addAll(this.yAttributeDescriptors.values());
        all.addAll(this.propsMap.values());
        all.addAll(this.yDeployments.values());
        all.addAll(this.countryCurrencyAndLanguage.values());
        all.addAll(this.metaInformationMap.values());
        all.addAll(this.mediaFolderMap.values());
        all.addAll(this.securityCredentials.values());
        return all;
    }


    protected YRecord addCustomProperty(YTypeSystemElement parentElement, YDeployment deployment, PK itemPK, PK itemTypePK, String name, Object value)
    {
        String propertyKey = generateUniquePropertyKey(parentElement, itemPK, name);
        if(this.propsMap.containsKey(propertyKey))
        {
            throw new IllegalStateException("property row " + itemPK + "." + name + " already exists");
        }
        String propsTableName = deployment.getPropsTableName();
        YDeployment propsDeployment = this.typeSystem.getDeployment("Property." + propsTableName);
        YRecord record = new YRecord(getDatabase().createDynaBeanFor(
                        getDatabase().findTable(DDLGeneratorUtils.adjustForTablePrefix(propsTableName, this.tablePrefix))), propsDeployment, false);
        record.set("ITEMPK", itemPK);
        record.set("ITEMTYPEPK", itemTypePK);
        record.set("LANGPK", PK.NULL_PK);
        record.set("NAME", name.toLowerCase(LocaleHelper.getPersistenceLocale()));
        record.set("REALNAME", name);
        PropsSupport.setValueString(record, value, this.propertyMaxLength);
        this.propsMap.put(propertyKey, record);
        return record;
    }


    protected String generateUniquePropertyKey(YTypeSystemElement parentElement, PK itemPK, String name)
    {
        return Joiner.on("_").join(itemPK, name, new Object[0]);
    }


    private YRecord addDeployment(YComposedType type)
    {
        YDeployment yDeployment = this.typeSystem.getDeployment("YDeployment.YDeployments");
        YRecord record = newRecord(yDeployment);
        YDeployment deployment = type.getDeployment();
        String propTable = deployment.getPropsTableName();
        record.set("PropsTableName", (propTable == null) ? "props" : propTable);
        record.set("Typecode", deployment.getItemTypeCode());
        record.set("Modifiers", DDLGeneratorUtils.getDeploymentModifiers(deployment));
        record.set("TableName", deployment.getTableName());
        if(deployment.getSuperDeployment() != null)
        {
            record.set("SuperName", deployment.getSuperDeployment().getFullName());
        }
        record.set("Name", deployment.getName());
        record.set("PackageName", deployment.getPackageName());
        String extensionName = ((YExtension)deployment.getNamespace()).getExtensionName();
        record.set("ExtensionName", extensionName);
        record.set("TypeSystemName", this.typeSystemName);
        record.set("AuditTableName", deployment.getAuditTableName());
        this.yDeployments.put(deployment.getFullName(), record);
        return record;
    }


    protected void addUserRights()
    {
        long adminGroupPk = addUserGroups("admingroup", "admingroup").getLongValue();
        addUserGroups("employeegroup", "employeegroup");
        long adminUserPk = addUsers("admin", "Administrator", this.initUserPasswordService.readUserPassword("admin"), "Employee", "administrator description").getLongValue();
        addUsers("anonymous", "Anonymous", "", "Customer", "anonymous User");
        addPrincipleRelation(adminUserPk, adminGroupPk);
    }


    private PK addUserGroups(String groupId, String description)
    {
        FactoryYRecord yRecord = newItemRecord("UserGroup");
        yRecord.setAttribute("uid", groupId);
        yRecord.setAttribute("description", description);
        this.securityCredentials.put(groupId, yRecord);
        return yRecord.getPk();
    }


    private PK addUsers(String userId, String userName, String password, String type, String description)
    {
        FactoryYRecord yRecord = newItemRecord(type);
        yRecord.setAttribute("uid", userId);
        yRecord.setAttribute("encodedPassword", password);
        yRecord.setAttribute("passwordencoding", "plain");
        yRecord.setAttribute("name", userName);
        yRecord.setAttribute("description", description);
        if(password == null || password.isBlank())
        {
            yRecord.setAttribute("loginDisabled", Boolean.TRUE);
        }
        this.securityCredentials.put(userId, yRecord);
        return yRecord.getPk();
    }


    private void addPrincipleRelation(long userPk, long userGroupPk)
    {
        YComposedType relationType = (YComposedType)this.typeSystem.getType("PrincipalGroupRelation");
        FactoryYRecord yRecord = newItemRecord(relationType);
        yRecord.setAttribute("qualifier", "PrincipalGroupRelation");
        yRecord.setAttribute("source", userPk);
        yRecord.setAttribute("target", userGroupPk);
        this.securityCredentials.put("" + yRecord.getPk(), yRecord);
    }


    public void prepareMediaFolders()
    {
        prepareMediaFolder("root", null);
        prepareMediaFolder("hmc", "hmc");
        prepareMediaFolder("cronjob", "cronjob");
        prepareMediaFolder("impex", "impex");
    }


    private void prepareMediaFolder(String qualifier, String path)
    {
        YComposedType mediaFolderType = (YComposedType)this.typeSystem.getType("MediaFolder");
        FactoryYRecord yRecord = newItemRecord(mediaFolderType);
        yRecord.setAttribute("qualifier", qualifier);
        yRecord.setAttribute("path", path);
        this.mediaFolderMap.put(qualifier + qualifier, yRecord);
    }


    private int getItemTypeCode(YComposedType yComposedType)
    {
        int itemTypeCode = (yComposedType.getDeployment() == null) ? 0 : yComposedType.getDeployment().getItemTypeCode();
        return yComposedType.isAbstract() ? (-1 * itemTypeCode) : itemTypeCode;
    }


    protected YRecord addRelationType(YRelation yRelation)
    {
        FactoryYRecord rec = addComposedType((YComposedType)yRelation);
        rec.setAttribute("sourceType", yRelation.getSourceEnd().getType());
        rec.setAttribute("targetType", yRelation.getTargetEnd().getType());
        rec.setAttribute("sourceNavigable", yRelation.getSourceEnd().isNavigable());
        rec.setAttribute("targetNavigable", yRelation.getTargetEnd().isNavigable());
        rec.setAttribute("localized", yRelation.isLocalized());
        YAttributeDescriptor srcAttr = yRelation.getTargetEnd().getMappedAttribute();
        if(srcAttr != null)
        {
            rec.setAttribute("sourceAttribute", this.pkFactory.getOrCreatePK(srcAttr.getEnclosingType(), srcAttr));
        }
        YAttributeDescriptor tgtAttr = yRelation.getSourceEnd().getMappedAttribute();
        if(tgtAttr != null)
        {
            rec.setAttribute("targetAttribute", this.pkFactory.getOrCreatePK(tgtAttr.getEnclosingType(), tgtAttr));
        }
        YAttributeDescriptor oAttr = yRelation.getOrderingAttribute();
        if(oAttr != null)
        {
            rec.setAttribute("orderingAttribute", this.pkFactory.getOrCreatePK(oAttr.getEnclosingType(), oAttr));
        }
        YAttributeDescriptor lAttr = yRelation.getLocalizationAttribute();
        if(lAttr != null)
        {
            rec.setAttribute("localizationAttribute", this.pkFactory.getOrCreatePK(lAttr.getEnclosingType(), lAttr));
        }
        addDeployment((YComposedType)yRelation);
        return (YRecord)rec;
    }


    protected YRecord addEnumType(YEnumType enumType)
    {
        FactoryYRecord rec = addComposedType((YComposedType)enumType);
        rec.setAttribute("dynamic", enumType.isDynamic());
        rec.setAttribute("comparationAttribute", 0);
        return (YRecord)rec;
    }


    protected FactoryYRecord addComposedType(YComposedType yComposedType)
    {
        if(this.yComposedTypes.containsKey(yComposedType.getCode()))
        {
            throw new IllegalStateException("Composed type " + yComposedType.getCode() + " alreay added to RecordFactory.");
        }
        YComposedType metaType = yComposedType.getMetaType();
        FactoryYRecord row = newItemRecord(metaType, this.pkFactory.getOrCreatePK(yComposedType));
        row.setAttribute("autocreate", yComposedType.isAutocreate());
        row.setAttribute("generate", yComposedType.isGenerate());
        row.setAttribute("code", yComposedType.getCode());
        row.set("InternalCodeLowerCase", yComposedType.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()));
        if(yComposedType.getSuperType() != null)
        {
            row.setAttribute("superType", yComposedType.getSuperType());
        }
        row.setAttribute("inheritancePathString", collectInheritancePathString(yComposedType));
        row.setAttribute("jaloClass", getJaloClassForComposedType(yComposedType));
        String extensionName = ((YExtension)yComposedType.getNamespace()).getExtensionName();
        row.setAttribute("extensionName", extensionName);
        String jndiName = yComposedType.getDeployment().getFullName();
        row.setAttribute("jndiName", jndiName);
        row.set("ItemTypeCode", getItemTypeCode(yComposedType));
        if(yComposedType.isJaloOnly())
        {
            row.setAttribute("jaloonly", yComposedType.isJaloOnly());
        }
        row.set("removable", !hasOwnDeployment(yComposedType));
        row.set("propertyTableStatus", true);
        row.setAttribute("singleton", yComposedType.isSingleton());
        row.setAttribute("legacyPersistence", yComposedType.isLegacyPersistence());
        addCustomProperties(yComposedType, metaType, row);
        this.yComposedTypes.put(yComposedType.getCode(), row);
        addDeployment(yComposedType);
        return row;
    }


    private void addCustomProperties(YComposedType yComposedType, YComposedType metaType, FactoryYRecord row)
    {
        Map<String, String> customProps = yComposedType.getCustomProps();
        for(Map.Entry<String, String> entry : customProps.entrySet())
        {
            parseAndAddCustomPropertyValue((YTypeSystemElement)yComposedType, (YRecord)row, metaType, row.getPk(), row.getTypePK(), entry.getKey(), entry
                            .getValue());
        }
    }


    private String getJaloClassForComposedType(YComposedType yComposedType)
    {
        if(yComposedType instanceof YEnumType || yComposedType instanceof YRelation)
        {
            return yComposedType.getJaloClassName();
        }
        return this.codeGenerator.getJaloClassName(yComposedType);
    }


    private boolean hasOwnDeployment(YComposedType type)
    {
        YDeployment deployment = type.getOwnDeployment();
        return (deployment != null);
    }


    private Object evaluateRawValue(String key, String expr)
    {
        try
        {
            return this.interpreter.eval(expr);
        }
        catch(EvalError e)
        {
            LOG.error("cannot value custom property '" + key + "'='" + expr + "' - " + e.getMessage(), (Throwable)e);
            return null;
        }
    }


    private void parseAndAddCustomPropertyValue(YTypeSystemElement parentElement, YRecord row, YComposedType itemType, PK itemPK, PK itemTypePK, String key, String expr)
    {
        String dbCol = null;
        YAttributeDescriptor attr = itemType.getAttributeIncludingSuperType(key);
        if(attr != null)
        {
            YColumn col = ((YTable)getDatabase().findTable(DDLGeneratorUtils.adjustForTablePrefix(itemType.getDeployment().getTableName(), this.tablePrefix))).findMappedColumn(attr);
            if(col != null)
            {
                dbCol = col.getName();
            }
        }
        if(dbCol != null)
        {
            row.set(dbCol, evaluateRawValue(key, expr));
        }
        else
        {
            addCustomProperty(parentElement, itemType.getDeployment(), itemPK, itemTypePK, key, evaluateRawValue(key, expr));
        }
    }


    private String collectInheritancePathString(YComposedType type)
    {
        StringBuilder inheritancePathString = new StringBuilder(",");
        for(YComposedType t = type; t != null; t = t.getSuperType())
        {
            inheritancePathString.insert(0, "," + this.pkFactory.getOrCreatePK(t));
        }
        return inheritancePathString.toString();
    }


    private String collectInheritancePathString(YAtomicType type)
    {
        StringBuilder inheritancePathString = new StringBuilder(",");
        for(YAtomicType t = type; t != null; t = t.getSuperType())
        {
            inheritancePathString.insert(0, "," + this.pkFactory.getOrCreatePK(t));
        }
        return inheritancePathString.toString();
    }


    private String collectAttributeInheritancePathString(YComposedType enclosingType, YAttributeDescriptor attr)
    {
        StringBuilder inheritancePathString = new StringBuilder(",");
        YComposedType declaringOne = attr.getDeclaringType();
        for(YComposedType t = enclosingType; t != null; t = t.getSuperType())
        {
            inheritancePathString.insert(0, "," + this.pkFactory.getOrCreatePK(t));
            if(declaringOne.equals(t))
            {
                break;
            }
        }
        return inheritancePathString.toString();
    }


    protected void prepareNumberSeries()
    {
        YDeployment deployment = this.typeSystem.getDeployment("NumberSeries.NumberSeries");
        Map<String, Long> numberSeries = this.pkFactory.getCurrentNumberSeries();
        for(Map.Entry<String, Long> e : numberSeries.entrySet())
        {
            YRecord nSeries = newRecord(deployment);
            nSeries.set("serieskey", e.getKey());
            nSeries.set("seriestype", 1);
            nSeries.set("currentValue", Long.toString(((Long)e.getValue()).longValue()));
            this.numberSeriesRecords.put(e.getKey(), nSeries);
        }
    }


    protected void prepareMetaInformation(String systemName)
    {
        YDeployment deployment = this.typeSystem.getDeployment("MetaInformation");
        YRecord metaInformation = newRecord(deployment);
        int itemTypeCode = deployment.getItemTypeCode();
        metaInformation.setPK(getFixedPk(itemTypeCode));
        setDefaultColumns(metaInformation);
        metaInformation.set(getColumnName(deployment.getAttributeDeployment("typePkString")), this.pkFactory
                        .createNewPK(itemTypeCode));
        metaInformation.set(getColumnName(deployment.getAttributeDeployment("systemPKInternal")), getUUIDPk(itemTypeCode));
        metaInformation.set(getColumnName(deployment.getAttributeDeployment("initializedFlagInternal")), true);
        metaInformation.set(getColumnName(deployment.getAttributeDeployment("systemNameInternal")), systemName);
        metaInformation.set(getColumnName(deployment.getAttributeDeployment("licenceAdminFactorInternal")), 0);
        this.metaInformationMap.put(systemName, metaInformation);
    }


    private PK getFixedPk(int itemTypeCode)
    {
        return PK.createFixedUUIDPK(itemTypeCode, 1L);
    }


    private PK getUUIDPk(int itemTypeCode)
    {
        return PK.createUUIDPK(itemTypeCode);
    }


    protected void addMapType(YMapType yMapType)
    {
        if(this.yMapTypes.containsKey(yMapType.getCode()))
        {
            throw new IllegalStateException("maptype " + yMapType.getCode() + " already exists in REcordFactory");
        }
        YComposedType metaType = yMapType.getMetaType();
        FactoryYRecord mapType = newItemRecord(metaType, this.pkFactory.getOrCreatePK(yMapType));
        mapType.setAttribute("code", yMapType.getCode());
        mapType.set("InternalCodeLowerCase", yMapType.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()));
        mapType.setAttribute("argumentType", yMapType.getArgumentType());
        mapType.setAttribute("returnType", yMapType.getReturnType());
        mapType.setAttribute("generate", yMapType.isGenerate());
        mapType
                        .setBinaryAttribute("defaultValue",
                                        tryToInterpretDefaultValue(yMapType.getDefaultValueDefinition(), (YType)yMapType, null));
        mapType.setAttribute("autocreate", yMapType.isAutocreate());
        mapType.setAttribute("extensionName", ((YExtension)yMapType.getNamespace()).getExtensionName());
        this.yMapTypes.put(yMapType.getCode(), mapType);
    }


    protected void addCollectionType(YCollectionType yCollectionType)
    {
        if(this.yCollectionTypes.containsKey(yCollectionType.getCode()))
        {
            return;
        }
        YComposedType metaType = yCollectionType.getMetaType();
        FactoryYRecord collectiontype = newItemRecord(metaType, this.pkFactory.getOrCreatePK(yCollectionType));
        collectiontype.setAttribute("owner", null);
        collectiontype.setAttribute("code", yCollectionType.getCode());
        collectiontype.set("InternalCodeLowerCase", yCollectionType.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()));
        collectiontype.setAttribute("elementType", yCollectionType.getElementType());
        collectiontype.setAttribute("typeOfCollectionInternal", yCollectionType.getTypeOfCollection().getTypeCode());
        collectiontype.setAttribute("generate", yCollectionType.isGenerate());
        collectiontype.setBinaryAttribute("defaultValue",
                        tryToInterpretDefaultValue(yCollectionType.getDefaultValueDefinition(), (YType)yCollectionType, null));
        collectiontype.setAttribute("autocreate", yCollectionType.isAutocreate());
        collectiontype.setAttribute("extensionName", ((YExtension)yCollectionType.getNamespace()).getExtensionName());
        this.yCollectionTypes.put(yCollectionType.getCode(), collectiontype);
    }


    protected void addEnumerationValues(YEnumType yEnumType)
    {
        int position = 0;
        for(YEnumValue ev : yEnumType.getValues())
        {
            addEnumerationValue(ev, position++);
        }
    }


    private void addEnumerationValue(YEnumValue yEnumValue, int sequenceNr)
    {
        String key = getKey(yEnumValue);
        if(this.yEnumearationValues.containsKey(key))
        {
            throw new IllegalStateException();
        }
        FactoryYRecord enumerationvalue = newItemRecord((YComposedType)yEnumValue.getEnumType(), this.pkFactory.getOrCreatePK(yEnumValue));
        enumerationvalue.setAttribute("owner", null);
        enumerationvalue.setAttribute("sequenceNumber", sequenceNr);
        enumerationvalue.setAttribute("code", yEnumValue.getCode());
        enumerationvalue.set("codeLowerCase", yEnumValue.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()));
        enumerationvalue.setAttribute("extensionName", ((YExtension)yEnumValue.getNamespace()).getExtensionName());
        enumerationvalue.setAttribute("icon", null);
        this.yEnumearationValues.put(key, enumerationvalue);
    }


    protected String getKey(YEnumValue yEnumValue)
    {
        return yEnumValue.getEnumTypeCode() + "." + yEnumValue.getEnumTypeCode();
    }


    protected FactoryYRecord addAttributeDescriptor(YComposedType enclosingType, YAttributeDescriptor yAttributeDescriptor)
    {
        String descriptorKey = getDescriptorKey(enclosingType, yAttributeDescriptor);
        if(this.yAttributeDescriptors.containsKey(descriptorKey))
        {
            throw new IllegalStateException("Attribute descriptor " + descriptorKey + " already exists in RecordFatory");
        }
        FactoryYRecord row = newItemRecord(yAttributeDescriptor.getMetaType(), this.pkFactory
                        .getOrCreatePK(enclosingType, yAttributeDescriptor));
        row.setAttribute("owner", enclosingType);
        row.setAttribute("extensionname", ((YExtension)yAttributeDescriptor.getNamespace()).getExtensionName());
        row.setAttribute("autocreate", yAttributeDescriptor.isAutocreate());
        row.setAttribute("generate", yAttributeDescriptor.isGenerate());
        row.setBinaryAttribute("defaultValue",
                        tryToInterpretDefaultValue(yAttributeDescriptor.getDefaultValueDefinition(), yAttributeDescriptor.getType(), yAttributeDescriptor));
        row.setAttribute("defaultValueDefinitionString", yAttributeDescriptor.getDefaultValueDefinition());
        row.setAttribute("attributeType", yAttributeDescriptor.getType());
        row.setAttribute("databaseColumn", getColumnNameAttributeValue(enclosingType, yAttributeDescriptor));
        row.setAttribute("enclosingType", enclosingType);
        row.setAttribute("qualifier", yAttributeDescriptor.getQualifier());
        row.set("QualifierLowerCaseInternal", yAttributeDescriptor.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
        boolean inherited = (!yAttributeDescriptor.isDeclared() || !enclosingType.equals(yAttributeDescriptor.getEnclosingType()));
        setModifiers(enclosingType, yAttributeDescriptor, row, inherited);
        String inheritancePathString = collectAttributeInheritancePathString(enclosingType, yAttributeDescriptor);
        row.set("InheritancePathString", inheritancePathString);
        if(yAttributeDescriptor.isUniqueModifier())
        {
            row.setAttribute("unique", yAttributeDescriptor.isUniqueModifier());
        }
        row.setAttribute("persistenceQualifier", getPersistenceQualifier(yAttributeDescriptor));
        row.setAttribute("persistenceType", getPersistenceType(yAttributeDescriptor));
        row.setAttribute("attributeHandler", yAttributeDescriptor.getDynamicAttributeHandler());
        YAttributeDescriptor sAttr = yAttributeDescriptor.getSelectionOf();
        if(sAttr != null)
        {
            row.setAttribute("selectionOf", this.pkFactory.getOrCreatePK(enclosingType, sAttr));
        }
        if(yAttributeDescriptor.isRelationEndAttribute())
        {
            appendRelationDescriptorAttributes(row, yAttributeDescriptor);
        }
        Map<String, String> customProps = yAttributeDescriptor.getCustomProps();
        for(Map.Entry<String, String> entry : customProps.entrySet())
        {
            parseAndAddCustomPropertyValue((YTypeSystemElement)yAttributeDescriptor, (YRecord)row, row.getType(), row.getPk(), row.getTypePK(), entry.getKey(), entry
                            .getValue());
        }
        this.yAttributeDescriptors.put(descriptorKey, row);
        return row;
    }


    private YAtomicType getPersistenceType(YAttributeDescriptor yAttributeDescriptor)
    {
        if(yAttributeDescriptor.getPersistenceType() != YAttributeDescriptor.PersistenceType.CMP && (yAttributeDescriptor
                        .getPersistenceType() == YAttributeDescriptor.PersistenceType.JALO || yAttributeDescriptor
                        .getPersistenceType() == YAttributeDescriptor.PersistenceType.DYNAMIC || (yAttributeDescriptor
                        .isRelationEndAttribute() && !yAttributeDescriptor.isProperty())))
        {
            return null;
        }
        return yAttributeDescriptor.getColumnType();
    }


    void setModifiers(YComposedType enclosingType, YAttributeDescriptor yAttributeDescriptor, FactoryYRecord row, boolean inherited)
    {
        int originalModifiers = yAttributeDescriptor.getConfiguredModifiers();
        int modifiers = yAttributeDescriptor.getModifiers();
        if((originalModifiers & 0x2000) == 0 && yAttributeDescriptor.isRelationEndAttribute())
        {
            modifiers &= 0xFFFFDFFF;
        }
        if(inherited)
        {
            modifiers |= 0x400;
            row.setAttribute("modifiers", modifiers);
            YAttributeDescriptor superAttributeDescriptor = findSuperAttributeDescriptor(enclosingType, yAttributeDescriptor);
            if(superAttributeDescriptor != null)
            {
                row.set("SuperAttributeDescriptorPK", this.pkFactory
                                .getOrCreatePK(superAttributeDescriptor.getEnclosingType(), superAttributeDescriptor));
            }
            else
            {
                LOG.error("Couldn't find super attribute of " + yAttributeDescriptor + " !!!");
            }
        }
        else
        {
            row.setAttribute("modifiers", modifiers);
        }
        row.set("isHidden", yAttributeDescriptor.isPrivate());
        row.set("isProperty", yAttributeDescriptor.isProperty());
    }


    private String getColumnNameAttributeValue(YComposedType enclosingType, YAttributeDescriptor yAttributeDescriptor)
    {
        String dbCol = null;
        if(yAttributeDescriptor.isPersistable())
        {
            YColumn mappedColumn = this.dbModel.findMappedColumn(enclosingType, yAttributeDescriptor);
            if(mappedColumn != null)
            {
                dbCol = this.platform.getColumnName((Column)mappedColumn);
            }
            else
            {
                dbCol = this.dbModel.computeColumnNameForAttributeInType(yAttributeDescriptor, enclosingType);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("--- Attribute " + enclosingType.getCode() + "." + yAttributeDescriptor.getQualifier() + " is not mapped to any column. Using virtual column name '" + dbCol + "' instead for compatibility!");
                }
            }
        }
        return dbCol;
    }


    private String getPersistenceQualifier(YAttributeDescriptor yAttributeDescriptor)
    {
        if(yAttributeDescriptor.getPersistenceType() == YAttributeDescriptor.PersistenceType.CMP && yAttributeDescriptor
                        .hasNonGenericPersistenceQualifier())
        {
            return yAttributeDescriptor.getPersistenceQualifier();
        }
        return null;
    }


    private YAttributeDescriptor findSuperAttributeDescriptor(YComposedType enclosingType, YAttributeDescriptor attr)
    {
        if(attr.isDeclared())
        {
            if(enclosingType.equals(attr.getEnclosingType()))
            {
                return null;
            }
            return attr;
        }
        return attr.getDeclaringAttribute();
    }


    protected void appendRelationDescriptorAttributes(FactoryYRecord row, YAttributeDescriptor yAttributeDescriptor)
    {
        YRelationEnd relationEnd = yAttributeDescriptor.getRelationEnd();
        row.setAttribute("isSource", relationEnd.getOppositeEnd().isSource());
        row.setAttribute("ordered", relationEnd.isOrdered());
        row.setAttribute("relationName", relationEnd.getRelationCode());
        row.setAttribute("relationType", relationEnd.getRelation());
    }


    protected String getColumnName(YComposedType composedType, String qualifier)
    {
        YAttributeDescriptor attr = composedType.getAttributeIncludingSuperType(qualifier);
        if(attr == null)
        {
            throw new IllegalStateException("Type " + composedType.getCode() + " has no attribute " + qualifier);
        }
        YColumn mappedColumn = this.dbModel.findMappedColumn(composedType, attr);
        if(mappedColumn == null)
        {
            throw new IllegalStateException("No mapped column for attribute " + composedType.getCode() + "." + qualifier);
        }
        return mappedColumn.getName();
    }


    private String getColumnName(YAttributeDeployment attributeDeployment)
    {
        if(attributeDeployment == null)
        {
            return null;
        }
        return this.dbModel.getColumnName(attributeDeployment);
    }


    protected FactoryYRecord newItemRecord(String type)
    {
        return newItemRecord((YComposedType)this.typeSystem.getType(type));
    }


    protected FactoryYRecord newItemRecord(YComposedType type)
    {
        return newItemRecord(type, this.pkFactory.createNewPK(type));
    }


    protected FactoryYRecord newItemRecord(YComposedType type, PK ownPK)
    {
        YDeployment ldeployment = type.getDeployment();
        FactoryYRecord rec = new FactoryYRecord(this, getDatabase().createDynaBeanFor(
                        DDLGeneratorUtils.adjustForTablePrefix(ldeployment.getTableName(), this.tablePrefix), false), type, ldeployment);
        rec.setPK(ownPK);
        rec.setTypePK(this.pkFactory.getOrCreatePK(type));
        setDefaultColumns((YRecord)rec);
        return rec;
    }


    protected YRecord newRecord(YDeployment deployment)
    {
        return (YRecord)new FactoryYRecord(this, getDatabase().createDynaBeanFor(
                        DDLGeneratorUtils.adjustForTablePrefix(deployment.getTableName(), this.tablePrefix), false), null, deployment);
    }


    protected void prepareDefaultLanguage()
    {
        YComposedType yLanguage = (YComposedType)this.typeSystem.getType("language");
        FactoryYRecord english = newItemRecord(yLanguage);
        english.setAttribute("isocode", "en");
        english.setAttribute("active", 1);
        this.countryCurrencyAndLanguage.put("_en", english);
    }


    protected void prepareDefaultCurrency()
    {
        YComposedType yCurrency = (YComposedType)this.typeSystem.getType("currency");
        FactoryYRecord currency = newItemRecord(yCurrency);
        currency.setAttribute("isocode", "---");
        currency.setAttribute("base", 1);
        currency.setAttribute("active", 1);
        currency.setAttribute("symbol", "---");
        currency.setAttribute("digits", 2);
        currency.setAttribute("conversion", Double.valueOf(1.0D));
        this.countryCurrencyAndLanguage.put(yCurrency.getCode(), currency);
    }


    protected void addAtomicType(YAtomicType yAtomicType)
    {
        if(this.yAtomicTypes.containsKey(yAtomicType.getCode()))
        {
            throw new IllegalStateException("atomic type " + yAtomicType.getCode() + " already exists in REcordFactory");
        }
        YComposedType atomicTypeType = yAtomicType.getMetaType();
        FactoryYRecord atomicType = newItemRecord(atomicTypeType, this.pkFactory.getOrCreatePK(yAtomicType));
        atomicType.setAttribute("owner", null);
        atomicType.setAttribute("code", yAtomicType.getCode());
        atomicType.set("InternalCodeLowerCase", yAtomicType.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()));
        if(yAtomicType.getSuperType() != null)
        {
            atomicType.setAttribute("superType", this.pkFactory.getOrCreatePK(yAtomicType.getSuperType()));
        }
        else
        {
            atomicType.setAttribute("superType", 0);
        }
        atomicType.setAttribute("javaClass", yAtomicType.getJavaClassName());
        atomicType.setAttribute("generate", yAtomicType.isGenerate());
        atomicType.setBinaryAttribute("defaultValue",
                        tryToInterpretDefaultValue(yAtomicType.getDefaultValueDefinition(), (YType)yAtomicType, null));
        atomicType.setAttribute("autocreate", yAtomicType.isAutocreate());
        atomicType.setAttribute("extensionName", ((YExtension)yAtomicType.getNamespace()).getExtensionName());
        atomicType.set("InheritancePathString", collectInheritancePathString(yAtomicType));
        this.yAtomicTypes.put(yAtomicType.getCode(), atomicType);
    }


    private Object tryToInterpretDefaultValue(String definition, YType type, YAttributeDescriptor attribute)
    {
        Object realValue = null;
        if(definition != null)
        {
            if(type instanceof YEnumType)
            {
                YEnumValue value = findEnumValueForDefaultValue((YEnumType)type, definition);
                if(value != null)
                {
                    realValue = new ItemPropertyValue(this.pkFactory.getOrCreatePK(value));
                }
            }
            if(realValue == null)
            {
                try
                {
                    realValue = interpretDefaultValueExpression(definition);
                }
                catch(CannotInterpretException e1)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Cannot evaluate default value '" + definition + "'" + (
                                        (attribute != null) ? (" of attribute " + attribute) : (" of type " + type)) + " - wrapping it for platform to evaluate it!");
                    }
                    realValue = new DefaultValueExpressionHolder(definition);
                }
            }
        }
        return realValue;
    }


    private YEnumValue findEnumValueForDefaultValue(YEnumType attrType, String expr)
    {
        YEnumValue ev = getValue(attrType, expr);
        if(ev == null)
        {
            Matcher matcher = EM_LITERAL_PATTERN.matcher(expr);
            if(matcher.matches())
            {
                String code = matcher.group(1);
                ev = getValue(attrType, code);
            }
            else
            {
                matcher = EM_CONSTANTS_PATTERN.matcher(expr);
                if(matcher.matches())
                {
                    String constant = matcher.group(1);
                    int dot = constant.lastIndexOf('.');
                    ev = getValue(attrType, (dot >= 0) ? constant.substring(dot + 1).trim() : constant.trim());
                }
            }
        }
        return ev;
    }


    private Object interpretDefaultValueExpression(String expr) throws CannotInterpretException
    {
        try
        {
            return this.interpreter.eval(expr);
        }
        catch(EvalError e)
        {
            throw new CannotInterpretException(e);
        }
    }


    private YEnumValue getValue(YEnumType type, String definition)
    {
        for(YEnumValue ev : type.getValues())
        {
            if(ev.getCode().equalsIgnoreCase(definition))
            {
                return ev;
            }
        }
        return null;
    }


    private void setDefaultColumns(YRecord yRecord)
    {
        yRecord.set("hjmpTS", "0");
        yRecord.set("createdTS", getCurrentTime());
        yRecord.set("modifiedTS", getCurrentTime());
        yRecord.set("aCLTS", "0");
        yRecord.set("propTS", "0");
    }


    protected Date getCurrentTime()
    {
        return new Date();
    }


    protected String getDescriptorKey(YComposedType enclosingType, YAttributeDescriptor yAttributeDescriptor)
    {
        return enclosingType.getCode() + "." + enclosingType.getCode();
    }


    protected Database getDatabase()
    {
        return this.database;
    }


    protected YTypeSystem getTypeSystem()
    {
        return this.typeSystem;
    }


    protected boolean exists(YEnumValue yEnumValue)
    {
        return this.yEnumearationValues.containsKey(getKey(yEnumValue));
    }
}
