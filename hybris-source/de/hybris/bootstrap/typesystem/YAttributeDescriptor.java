package de.hybris.bootstrap.typesystem;

import de.hybris.bootstrap.ddl.DatabaseSettings;
import de.hybris.bootstrap.typesystem.xml.ModelTagListener;
import de.hybris.bootstrap.util.LocaleHelper;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class YAttributeDescriptor extends YDescriptor
{
    private static final Logger LOG = Logger.getLogger(YAttributeDescriptor.class);
    public static final int READ_FLAG = 1;
    public static final int WRITE_FLAG = 2;
    public static final int REMOVE_FLAG = 4;
    public static final int OPTIONAL_FLAG = 8;
    public static final int SEARCH_FLAG = 16;
    public static final int PARTOF_FLAG = 32;
    public static final int PRIVATE_FLAG = 128;
    public static final int PROPERTY_FLAG = 256;
    public static final int LOCALIZED_FLAG = 512;
    public static final int INHERITED_FLAG = 1024;
    public static final int INITIAL_FLAG = 2048;
    public static final int TIM_IGNORE_FLAG = 8192;
    public static final int ENCRYPTED_FLAG = 16384;
    public static final int PRIMITIVE_FLAG = 65536;
    private final String enclosingTypeCode;
    private boolean redeclared;
    private ModelTagListener.ModelData modelData;
    private boolean inherited;


    public boolean isRedeclaredOneToManyRelationEnd()
    {
        return (isRelationEndAttribute() && isRedeclared() &&
                        getRelationEnd().getOppositeEnd().getCardinality() == YRelationEnd.Cardinality.ONE);
    }


    private PersistenceType persistenceType = PersistenceType.JALO;
    private PersistenceType configuredPersistenceType;
    private String persistenceQualifier;
    private YAtomicType columnType;
    private String selectionOfQualifier;
    private String defaultValueDefinition;
    private String description;
    private Map<String, String> dbColumnDefinitions;
    private String configuredColumnName;
    private String attributeHandler;
    private boolean relationEndChecked;
    private YRelationEnd relationEnd;
    private YAttributeDescriptor superAttribute;
    private Boolean localizedFlag;
    private Integer adjustedModifiers;
    private Boolean adjustedUnique;
    private String conditionQuery;


    public YAttributeDescriptor(YNamespace container, String enclosingTypeCode, String qualifier, String typeCode)
    {
        super(container, qualifier, typeCode);
        this.enclosingTypeCode = enclosingTypeCode;
    }


    public YAttributeDescriptor(String enclosingType, YAttributeDescriptor inheritedFrom)
    {
        this(inheritedFrom.getNamespace(), enclosingType, inheritedFrom.getQualifier(), inheritedFrom.getTypeCode());
        this.inherited = true;
        this.relationEnd = inheritedFrom.relationEnd;
        this.selectionOfQualifier = inheritedFrom.selectionOfQualifier;
        this.persistenceQualifier = inheritedFrom.persistenceQualifier;
        this.persistenceType = inheritedFrom.persistenceType;
        this.defaultValueDefinition = inheritedFrom.defaultValueDefinition;
        this.description = inheritedFrom.description;
        this.configuredColumnName = inheritedFrom.configuredColumnName;
        this.attributeHandler = inheritedFrom.attributeHandler;
        this.conditionQuery = inheritedFrom.conditionQuery;
        setModifiers(inheritedFrom.getConfiguredModifiers());
        setMetaTypeCode(inheritedFrom.getMetaTypeCode());
        addCustomProperties(inheritedFrom.getCustomProps());
        setLoaderInfo(inheritedFrom.getLoaderInfo());
    }


    public void resetCaches()
    {
        super.resetCaches();
        this.columnType = null;
        this.adjustedModifiers = null;
        this.localizedFlag = null;
        this.relationEnd = null;
        this.relationEndChecked = false;
        this.superAttribute = null;
    }


    protected void redeclare(String typeCode)
    {
        super.redeclare(typeCode);
        setRedeclared(true);
    }


    protected void redeclare(String typeCode, int modifiers)
    {
        super.redeclare(typeCode, modifiers);
        setRedeclared(true);
    }


    public Map<String, String> getCustomProps()
    {
        Map<String, String> allProps = new HashMap<>();
        for(YAttributeDescriptor superAttr : getAllSuperAttributes())
        {
            Map<String, String> map = superAttr.getOwnCustomProps();
            if(MapUtils.isNotEmpty(map))
            {
                allProps.putAll(map);
            }
        }
        Map<String, String> props = getOwnCustomProps();
        if(MapUtils.isNotEmpty(props))
        {
            allProps.putAll(props);
        }
        return allProps;
    }


    List<YAttributeDescriptor> getAllSuperAttributes()
    {
        List<YAttributeDescriptor> ret = null;
        for(YAttributeDescriptor st = getSuperAttribute(); st != null; st = st.getSuperAttribute())
        {
            if(ret == null)
            {
                ret = new ArrayList<>(5);
            }
            ret.add(st);
        }
        if(ret != null)
        {
            Collections.reverse(ret);
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }


    public Map<String, String> getOwnCustomProps()
    {
        return super.getCustomProps();
    }


    public String toString()
    {
        return getEnclosingTypeCode() + "." + getEnclosingTypeCode() + "[" + super.toString() + "]";
    }


    protected void connectToRelationEnd(YRelationEnd end)
    {
        this.relationEnd = end;
    }


    public YRelationEnd getRelationEnd()
    {
        if(this.relationEnd == null && !this.relationEndChecked)
        {
            this.relationEnd = getTypeSystem().getAttributeRelationEnd(getEnclosingTypeCode(), getQualifier());
            if(this.relationEnd == null && !isDeclared())
            {
                this.relationEnd = getSuperAttribute().getRelationEnd();
            }
            this.relationEndChecked = true;
        }
        return this.relationEnd;
    }


    public String getConditionQuery()
    {
        if(this.conditionQuery == null && getRelationEnd() != null)
        {
            this.conditionQuery = getRelationEnd().getConditionQuery();
        }
        return this.conditionQuery;
    }


    public boolean isRelationEndAttribute()
    {
        return (getRelationEnd() != null);
    }


    protected String getDefaultMetaTypeCode()
    {
        return "AttributeDescriptor";
    }


    public YComposedType getMetaType()
    {
        if(getMetaTypeCode() != null || isDeclared())
        {
            return super.getMetaType();
        }
        YAttributeDescriptor superAD = getSuperAttribute();
        if(superAD == null)
        {
            throw new IllegalStateException("invalid attribute " + this + " - no super attribute though attribute is inherited or redeclared");
        }
        return superAD.getMetaType();
    }


    public void validate()
    {
        super.validate();
        if(!isValidQualifier(getQualifier()))
        {
            throw new IllegalStateException("invalid attribute qualifier: " + this + "!!");
        }
        getEnclosingType();
        if(getEnclosingType().getAttribute(getQualifier()) != this)
        {
            throw new IllegalStateException("invalid attribute " + this + " since it seems not to be assigned to enclosing type " +
                            getEnclosingType());
        }
        if(isPrimitive() && !(getType() instanceof YAtomicType))
        {
            throw new IllegalStateException("Attribute " + getEnclosingTypeCode() + "." + getQualifier() + " can not be created as primitive attribute because it's type is not an AtomicType See: " + this);
        }
        if(isPrimitive() && ((YAtomicType)getType()).getPrimitiveJavaClass() == null)
        {
            throw new IllegalStateException("Attribute " + getEnclosingTypeCode() + "." + getQualifier() + " can not be created as primitive attribute because it's type " + ((YAtomicType)
                            getType()).getPrimitiveJavaClass() + " has no corresponding primitive java class See: " + this);
        }
        if(isUniqueModifier() && !isSearchable())
        {
            if(!PersistenceType.JALO.equals(getPersistenceType()))
            {
                throw new IllegalStateException("Attribute " + getEnclosingTypeCode() + "." + getQualifier() + " is unique and not searchable. Please declare it in your items.xml file as not unique (unique='false') or as searchable (search='true'). See: " + this);
            }
            LOG.warn("Attribute " + getEnclosingTypeCode() + "." + getQualifier() + " is unique and its persistence type is set to 'jalo'. Please declare it in your items.xml file as not unique (unique='false') or remove unique declaration since jalo attributes can not be declared unique. See: "
                            + this);
        }
        if(!isOptional() && isLocalized())
        {
            LOG.warn("Attribute " + getEnclosingTypeCode() + "." + getQualifier() + " is marked mandatory yet is also a localized attribute. We advise setting its optional field to 'true' to avoid getting errors when a new language is added for which this attribute has no localized value.  See: "
                            + this);
        }
        if(isRedeclared() && !getSuperAttribute().getType().isAssignableFrom(getType()))
        {
            LOG.warn("Attribute: '" + this + "' is not properly redeclared. Its type: '" + getType().getCode() + "' is not compatible to type: '" +
                            getSuperAttribute().getType()
                                            .getCode() + "' of super attribute: '" +
                            getSuperAttribute() + "'.");
        }
        getSelectionOf();
        if(!getTypeSystem().isBuildMode())
        {
            Set<YComposedType> subtypes = getEnclosingType().getSubtypes();
            while(!subtypes.isEmpty())
            {
                Set<YComposedType> nextSubtypes = new HashSet<>();
                for(YComposedType subtype : subtypes)
                {
                    YAttributeDescriptor subtypeAD = subtype.getAttribute(getQualifier());
                    if(subtypeAD == null)
                    {
                        throw new IllegalStateException("invalid attribute " + this + " due to missing sub attribute " + subtype
                                        .getCode() + "." + getQualifier());
                    }
                    if(!subtypeAD.isInherited())
                    {
                        continue;
                    }
                    nextSubtypes.addAll(subtype.getSubtypes());
                }
                subtypes = nextSubtypes;
            }
            if(isPersistable())
            {
                getAttributeDeploymentOrFail();
            }
        }
        else
        {
            Set<YComposedType> subtypes = getEnclosingType().getSubtypes();
            while(!subtypes.isEmpty())
            {
                Set<YComposedType> nextSubtypes = new HashSet<>();
                for(YComposedType subtype : subtypes)
                {
                    YAttributeDescriptor subtypeAD = subtype.getAttribute(getQualifier());
                    if(subtypeAD != null && subtypeAD.isDeclared())
                    {
                        throw new IllegalStateException("Attribute " + subtypeAD + " duplicates inherited attribute " + this + ". Remove it or specify it as redeclared.");
                    }
                    nextSubtypes.addAll(subtype.getSubtypes());
                }
                subtypes = nextSubtypes;
            }
        }
        checkModelMethods(getGetters(), "Getter");
        checkModelMethods(getSetters(), "Setter");
    }


    private void checkModelMethods(List<ModelTagListener.ModelDataMethod> methods, String type)
    {
        boolean defaultSet = false;
        for(ModelTagListener.ModelDataMethod method : methods)
        {
            if(method.defaultAttribute)
            {
                if(defaultSet)
                {
                    throw new IllegalStateException("Only one default " + type + " permitted! See: " + this);
                }
                defaultSet = true;
            }
        }
    }


    private boolean isValidQualifier(String qualifier)
    {
        CharacterIterator iter = new StringCharacterIterator(qualifier);
        int idx = 0;
        boolean valid = true;
        for(char c = iter.first(); c != Character.MAX_VALUE && valid; c = iter.next(), idx++)
        {
            valid = ((idx == 0 && ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'))) || (idx != 0 && ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || Character.isDigit(c) || c == '_' || c == '-')));
        }
        return valid;
    }


    public boolean isDeclared()
    {
        return (!isInherited() && !isRedeclared());
    }


    public boolean isInherited()
    {
        return this.inherited;
    }


    public String getPersistenceQualifier()
    {
        return (isRedeclared() || (this.persistenceQualifier == null && !isDeclared())) ? getSuperAttribute().getPersistenceQualifier() : (
                        (this.persistenceQualifier != null) ? this.persistenceQualifier : (getEnclosingTypeCode() + "." + getEnclosingTypeCode()));
    }


    public boolean hasNonGenericPersistenceQualifier()
    {
        return isRedeclared() ? getSuperAttribute().hasNonGenericPersistenceQualifier() : ((this.persistenceQualifier != null));
    }


    public void setPersistenceQualifier(String persistenceQualifier)
    {
        if(isRedeclared())
        {
            throw new IllegalStateException("cannot change persistence qualifier in redeclared attribute " + this);
        }
        this.persistenceQualifier = persistenceQualifier;
    }


    public PersistenceType getPersistenceType()
    {
        if(isRedeclared() || (this.persistenceType == null && !isRelationAttribute() && !isDeclared()))
        {
            return getSuperAttribute().getPersistenceType();
        }
        if(getRelationEnd() != null)
        {
            if(getRelationEnd().getCardinality() == YRelationEnd.Cardinality.MANY)
            {
                return PersistenceType.JALO;
            }
            if(this.persistenceType == PersistenceType.CMP && hasNonGenericPersistenceQualifier())
            {
                return PersistenceType.CMP;
            }
            return PersistenceType.PROPERTY;
        }
        return this.persistenceType;
    }


    public boolean isPersistable()
    {
        PersistenceType persistenceType = getPersistenceType();
        if(persistenceType == PersistenceType.JALO || persistenceType == PersistenceType.DYNAMIC ||
                        getEnclosingType().isJaloOnly())
        {
            return false;
        }
        int modifiers = getModifiers();
        return ((modifiers & 0x2000) == 0);
    }


    public YAtomicType getColumnType()
    {
        if(this.columnType == null)
        {
            this.columnType = getColumnType(getType());
        }
        return this.columnType;
    }


    protected YAtomicType getColumnType(YType type)
    {
        if(type instanceof YAtomicType)
        {
            return (YAtomicType)type;
        }
        if(type instanceof YComposedType)
        {
            return (YAtomicType)getTypeSystem().getType("de.hybris.platform.util.ItemPropertyValue");
        }
        if(type instanceof YMapType)
        {
            return ((YMapType)type).isLocalizationMap() ? getColumnType(((YMapType)type).getReturnType()) :
                            (YAtomicType)getTypeSystem().getType("java.lang.Object");
        }
        if(type instanceof YCollectionType)
        {
            if(((YCollectionType)type).getElementType() instanceof YComposedType)
            {
                return (YAtomicType)getTypeSystem().getType("de.hybris.platform.util.ItemPropertyValueCollection");
            }
            return (YAtomicType)getTypeSystem().getType("java.lang.Object");
        }
        return (YAtomicType)getTypeSystem().getType("java.lang.Object");
    }


    public YAttributeDeployment getAttributeDeploymentOrFail()
    {
        YAttributeDeployment deployment = null;
        if(isPersistable())
        {
            deployment = getEnclosingType().getDeployment().getAttributeDeployment(getPersistenceQualifier());
            if(deployment == null)
            {
                throw new IllegalStateException("invalid persistable attribute " + this + " due to missing attribute deployment within deployment " +
                                getEnclosingType()
                                                .getDeployment());
            }
        }
        return deployment;
    }


    public YAttributeDeployment tryGetAttributeDeployment()
    {
        try
        {
            return getAttributeDeploymentOrFail();
        }
        catch(IllegalStateException e)
        {
            return null;
        }
    }


    protected boolean isRelationAttribute()
    {
        return (getRelationEnd() != null);
    }


    public int getConfiguredModifiers()
    {
        return super.getModifiers();
    }


    public int getModifiers()
    {
        if(this.adjustedModifiers == null)
        {
            int modifiers = getConfiguredModifiers();
            switch(null.$SwitchMap$de$hybris$bootstrap$typesystem$YAttributeDescriptor$PersistenceType[getPersistenceType().ordinal()])
            {
                case 1:
                    modifiers &= 0xFFFFFEFF;
                    if(!isRelationAttribute() || getRelationEnd().getRelation().isAbstract())
                    {
                        modifiers &= 0xFFFFFFEF;
                        modifiers |= 0x2000;
                    }
                    break;
                case 2:
                    modifiers &= 0xFFFFFEFF;
                    break;
                case 3:
                    modifiers |= 0x100;
                    break;
                case 4:
                    modifiers = modifiers & 0xFFFFFFEF | 0x2000;
                    break;
                default:
                    throw new IllegalStateException("unexpected persistence type " + getPersistenceType());
            }
            if(isLocalized())
            {
                modifiers |= 0x200;
            }
            else
            {
                modifiers &= 0xFFFFFDFF;
            }
            if(isDeclared())
            {
                modifiers &= 0xFFFFFBFF;
            }
            else
            {
                modifiers |= 0x400;
            }
            if(!isDeclared())
            {
                modifiers |= getDeclaringAttribute().getConfiguredModifiers() & 0x2024;
            }
            this.adjustedModifiers = Integer.valueOf(modifiers);
        }
        return this.adjustedModifiers.intValue();
    }


    public boolean isLocalized()
    {
        if(this.localizedFlag == null)
        {
            this
                            .localizedFlag = (getPersistenceType() != PersistenceType.CMP && ((getRelationEnd() != null && getRelationEnd().getRelation().isLocalized()) || (getType() instanceof YMapType && ((YMapType)getType()).isLocalizationMap()))) ? Boolean.TRUE : Boolean.FALSE;
        }
        return this.localizedFlag.booleanValue();
    }


    public void setPersistenceType(PersistenceType persistenceType)
    {
        if(isRedeclared())
        {
            throw new IllegalStateException("cannot change persistence type in redeclared attribute " + this);
        }
        this.persistenceType = (persistenceType != null) ? persistenceType : PersistenceType.JALO;
    }


    public String getSelectionOfQualifier()
    {
        return this.selectionOfQualifier;
    }


    public YAttributeDescriptor getSelectionOf()
    {
        YAttributeDescriptor sel = null;
        if(getSelectionOfQualifier() != null)
        {
            sel = getEnclosingType().getAttribute(getSelectionOfQualifier());
            if(sel == null)
            {
                if(getTypeSystem().isBuildMode())
                {
                    for(YComposedType st = getEnclosingType().getSuperType(); st != null && sel == null; st = st.getSuperType())
                    {
                        sel = st.getAttribute(getSelectionOfQualifier());
                    }
                }
                if(sel == null)
                {
                    throw new IllegalStateException("invalid attribute " + this + " due to unresolvable selection-of '" +
                                    getSelectionOfQualifier() + "'");
                }
            }
        }
        return sel;
    }


    public void setSelectionOfQualifier(String selectionOfQualifier)
    {
        this.selectionOfQualifier = selectionOfQualifier;
    }


    public String getEnclosingTypeCode()
    {
        return this.enclosingTypeCode;
    }


    public boolean isRedeclared()
    {
        return this.redeclared;
    }


    protected void clearPersistenceInfos()
    {
        this.persistenceQualifier = null;
        this.persistenceType = null;
        this.adjustedModifiers = null;
        this.relationEnd = null;
        this.adjustedUnique = null;
    }


    public void setInherited(boolean isInherited)
    {
        this.inherited = isInherited;
        if(this.inherited)
        {
            clearPersistenceInfos();
        }
    }


    public void setRedeclared(boolean isRedeclared)
    {
        this.redeclared = isRedeclared;
        if(this.redeclared)
        {
            clearPersistenceInfos();
        }
    }


    public void setModelData(ModelTagListener.ModelData modelData)
    {
        this.modelData = modelData;
    }


    public boolean isGenerateInModel()
    {
        return (this.modelData == null) ? true : this.modelData.generate;
    }


    public YAttributeDescriptor getDeclaringAttribute()
    {
        if(isDeclared())
        {
            return this;
        }
        return getDeclaringType().getAttribute(getQualifier());
    }


    public YComposedType getDeclaringType()
    {
        if(!isDeclared())
        {
            for(YComposedType t = getEnclosingType().getSuperType(); t != null; t = t.getSuperType())
            {
                YAttributeDescriptor attDesc = t.getAttribute(getQualifier());
                if(attDesc != null && attDesc.isDeclared())
                {
                    return t;
                }
            }
            throw new IllegalStateException("invalid attribute descriptor " + this + " due to missing declaring type");
        }
        return getEnclosingType();
    }


    public YAttributeDescriptor getSuperAttribute()
    {
        if(this.superAttribute == null)
        {
            if(isInherited() || isRedeclared())
            {
                YComposedType superEnclosing = getEnclosingType().getSuperType();
                this.superAttribute = superEnclosing.getAttribute(getQualifier());
                if(this.superAttribute == null && getTypeSystem().isBuildMode())
                {
                    for(superEnclosing = superEnclosing.getSuperType(); this.superAttribute == null && superEnclosing != null;
                                    superEnclosing = superEnclosing.getSuperType())
                    {
                        this.superAttribute = superEnclosing.getAttribute(getQualifier());
                    }
                }
                if(this.superAttribute == null)
                {
                    throw new IllegalStateException("invalid attribute " + this + " - no super attribute though attribute is inherited or redeclared");
                }
            }
        }
        return this.superAttribute;
    }


    public YComposedType getEnclosingType()
    {
        YType type = getTypeSystem().getType(getEnclosingTypeCode());
        if(!(type instanceof YComposedType))
        {
            throw new IllegalStateException("invalid attribute " + this + " due to missing enclosing type '" +
                            getEnclosingTypeCode() + "'");
        }
        return (YComposedType)type;
    }


    public String getDefaultValueDefinition()
    {
        return this.defaultValueDefinition;
    }


    public void setDefaultValueDefinition(String defaultValueDefinition)
    {
        this.defaultValueDefinition = defaultValueDefinition;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public Map<String, String> getDbColumnDefinitions()
    {
        return (this.dbColumnDefinitions != null) ? this.dbColumnDefinitions : Collections.EMPTY_MAP;
    }


    public void addDbColumnDefinitions(Map<String, String> dbColumnDefinitions)
    {
        if(this.dbColumnDefinitions == null)
        {
            this.dbColumnDefinitions = (Map<String, String>)new CaseInsensitiveMap();
        }
        this.dbColumnDefinitions.putAll(dbColumnDefinitions);
    }


    public String getRealColumnName()
    {
        return this.configuredColumnName;
    }


    public void setRealColumnName(String configuredColumnName)
    {
        this.configuredColumnName = configuredColumnName;
    }


    public boolean isOptional()
    {
        return ((getModifiers() & 0x8) == 8);
    }


    public boolean isSearchable()
    {
        return ((getModifiers() & 0x10) == 16);
    }


    public boolean isPartOf()
    {
        return ((getModifiers() & 0x20) == 32);
    }


    public boolean isWritable()
    {
        return ((getModifiers() & 0x2) == 2);
    }


    public boolean isInitial()
    {
        return ((getModifiers() & 0x800) == 2048);
    }


    public boolean isReadable()
    {
        return ((getModifiers() & 0x1) == 1);
    }


    public boolean isPrivate()
    {
        return ((getModifiers() & 0x80) == 128);
    }


    public boolean isProperty()
    {
        return ((getModifiers() & 0x100) == 256);
    }


    public boolean isEncrypted()
    {
        return ((getModifiers() & 0x4000) == 16384);
    }


    public boolean isPrimitive()
    {
        return ((getModifiers() & 0x10000) == 65536);
    }


    public boolean isBooleanAttribute()
    {
        YType realType = isLocalized() ? ((YMapType)getType()).getReturnType() : getType();
        return (realType instanceof YAtomicType && Boolean.class.equals(((YAtomicType)realType).getJavaClass()));
    }


    public List<ModelTagListener.ModelDataMethod> getSetters()
    {
        if(this.modelData == null)
        {
            return Collections.EMPTY_LIST;
        }
        return this.modelData.getSetters();
    }


    public List<ModelTagListener.ModelDataMethod> getGetters()
    {
        if(this.modelData == null)
        {
            return Collections.EMPTY_LIST;
        }
        return this.modelData.getGetters();
    }


    public void setAttributeHandler(String attributeHandler)
    {
        this.attributeHandler = attributeHandler;
    }


    public String getAttributeHandlerIncludingSuperTypes()
    {
        String ownHandler = getConfiguredAttributeHandler();
        if(StringUtils.isBlank(ownHandler) && !isDeclared())
        {
            return getSuperAttribute().getAttributeHandlerIncludingSuperTypes();
        }
        return ownHandler;
    }


    public String getConfiguredAttributeHandler()
    {
        return this.attributeHandler;
    }


    public boolean isUniqueModifier()
    {
        if(this.adjustedUnique == null)
        {
            boolean configured = super.isUniqueModifier();
            if(isDeclared())
            {
                this.adjustedUnique = Boolean.valueOf(configured);
            }
            else
            {
                boolean declaredUnique = getDeclaringAttribute().isUniqueModifier();
                if(!declaredUnique && configured && isRedeclared())
                {
                    YDeployment superDepl = getDeclaringAttribute().getEnclosingType().getDeployment();
                    YDeployment myDepl = getEnclosingType().getDeployment();
                    if(!superDepl.equals(myDepl))
                    {
                        this.adjustedUnique = Boolean.valueOf(configured);
                    }
                    else
                    {
                        this.adjustedUnique = Boolean.valueOf(declaredUnique);
                    }
                }
                else
                {
                    this.adjustedUnique = Boolean.valueOf(declaredUnique);
                }
            }
        }
        return this.adjustedUnique.booleanValue();
    }


    public void setConfiguredPersistenceType(PersistenceType configuredPersistenceType)
    {
        this.configuredPersistenceType = configuredPersistenceType;
    }


    public String getDynamicAttributeHandler()
    {
        String handler;
        if(getPersistenceType() != PersistenceType.DYNAMIC)
        {
            return null;
        }
        if(this.configuredPersistenceType != null)
        {
            handler = getConfiguredAttributeHandler();
        }
        else
        {
            handler = getAttributeHandlerIncludingSuperTypes();
        }
        if(StringUtils.isBlank(handler))
        {
            handler = getEnclosingTypeCode() + "_" + getEnclosingTypeCode() + "AttributeHandler";
        }
        return handler;
    }


    public String getColumnName(DatabaseSettings databaseSettings)
    {
        YAttributeDeployment attributeDeployment = tryGetAttributeDeployment();
        if(attributeDeployment != null)
        {
            return attributeDeployment.getColumnName(databaseSettings);
        }
        String colName = getPersistenceQualifier();
        if(isFixedPropertyColumnName(colName))
        {
            return colName.toLowerCase(LocaleHelper.getPersistenceLocale());
        }
        return ("p_" + getQualifier()).toLowerCase(LocaleHelper.getPersistenceLocale());
    }


    public boolean hasFixedColumnName()
    {
        YAttributeDeployment attributeDeployment = tryGetAttributeDeployment();
        return (attributeDeployment != null || isFixedPropertyColumnName(getPersistenceQualifier()));
    }


    boolean isFixedPropertyColumnName(String persistenceQualifier)
    {
        return (StringUtils.isNotBlank(persistenceQualifier) && !persistenceQualifier.contains("."));
    }
}
