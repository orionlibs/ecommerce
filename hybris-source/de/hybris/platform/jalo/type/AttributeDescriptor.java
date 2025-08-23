package de.hybris.platform.jalo.type;

import bsh.EvalError;
import bsh.Interpreter;
import com.google.common.base.Preconditions;
import de.hybris.platform.core.BeanShellUtils;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.persistence.security.ACLCache;
import de.hybris.platform.util.DefaultValueExpressionHolder;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.MethodUtils;
import org.apache.log4j.Logger;
import org.znerd.xmlenc.LineBreak;
import org.znerd.xmlenc.XMLOutputter;

public class AttributeDescriptor extends Descriptor
{
    private static final Logger log = Logger.getLogger(AttributeDescriptor.class);


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("qualifier", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("attributeType", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute(ENCLOSING_TYPE, allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing parameters " + missing + " to create a attribute descriptor", 0);
        }
        if("".equals(allAttributes.get("qualifier")))
        {
            throw new JaloInvalidParameterException("Got empty qualifier for creating an attribute descriptor", 0);
        }
        if(!(allAttributes.get(ENCLOSING_TYPE) instanceof ComposedType))
        {
            throw new JaloInvalidParameterException("Parameter " + ENCLOSING_TYPE + " should be instance of ComposedType", 0);
        }
        return (Item)((ComposedType)allAttributes.get(ENCLOSING_TYPE)).createAttributeDescriptor((String)allAttributes
                        .get("qualifier"), (Type)allAttributes
                        .get("attributeType"), getIntModifiersFromMap((Map)allAttributes));
    }


    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap copyMap = super.getNonInitialAttributes(ctx, allAttributes);
        copyMap.remove("qualifier");
        copyMap.remove("attributeType");
        copyMap.remove(ENCLOSING_TYPE);
        for(Map.Entry me : modifierMapping.entrySet())
        {
            copyMap.remove(me.getKey());
        }
        return copyMap;
    }


    private static final AttributeAccess _AD_ATTRIBUTETYPE = (AttributeAccess)new Object();
    private static final AttributeAccess _AD_PROPERTY = (AttributeAccess)new Object();
    private static final AttributeAccess _AD_SEARCH = (AttributeAccess)new Object();
    public static final String DEFAULTVALUE = "defaultValue".intern();
    public static final String DEFAULTVALUE_DEFINITIONSTRING = "defaultValueDefinitionString".intern();
    public static final String DESCRIPTION = "description".intern();
    public static final String ENCLOSING_TYPE = "enclosingType".intern();
    public static final String DECLARING_ENCLOSING_TYPE = "declaringEnclosingType".intern();
    public static final String WRITABLE = "writable".intern();
    public static final String READABLE = "readable".intern();
    public static final String REMOVABLE = "removable".intern();
    public static final String OPTIONAL = "optional".intern();
    public static final String SEARCH = "search".intern();
    public static final String PARTOF = "partOf".intern();
    public static final String PRIVATE = "private".intern();
    public static final String PROPERTY = "property".intern();
    public static final String LOCALIZED = "localized".intern();
    public static final String INHERITED = "inherited".intern();
    public static final String UNIQUE = "unique".intern();
    public static final String INITIAL = "initial".intern();
    public static final String TIM_IGNORE = "dontOptimize".intern();
    public static final String MODIFIERS = "modifiers".intern();
    public static final String DATABASECOLUMN = "databaseColumn".intern();
    public static final String PERSISTENCETYPE = "persistenceType".intern();
    public static final String ATTRIBUTE_HANDLER = "attributeHandler".intern();
    public static final String PROPOSED_DATABASE_COLUMN = "proposedDatabaseColumn".intern();
    public static final String PERSISTENCE_CLASS = "persistenceClass".intern();
    public static final String ENCRYPTED = "encrypted".intern();
    public static final String PRIMITIVE = "primitive".intern();
    public static final String HIDDENFORUI = "hiddenForUI";
    public static final String READONLYFORUI = "readOnlyForUI";
    public static final String DONT_COPY = "dontCopy";
    @Deprecated(since = "ages", forRemoval = false)
    public static final String READ = READABLE;
    @Deprecated(since = "ages", forRemoval = false)
    public static final String WRITE = WRITABLE;
    @Deprecated(since = "ages", forRemoval = false)
    public static final String REMOVE = REMOVABLE;
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
    public static final int ALL = 93887;
    public static final int ALL_PUBLIC = 92351;
    private static final Map modifierMapping;

    static
    {
        Item.ItemAttributeMap<String, Integer> itemAttributeMap = new Item.ItemAttributeMap();
        itemAttributeMap.put(READABLE, Integer.valueOf(1));
        itemAttributeMap.put(WRITABLE, Integer.valueOf(2));
        itemAttributeMap.put(REMOVABLE, Integer.valueOf(4));
        itemAttributeMap.put(OPTIONAL, Integer.valueOf(8));
        itemAttributeMap.put(SEARCH, Integer.valueOf(16));
        itemAttributeMap.put(PARTOF, Integer.valueOf(32));
        itemAttributeMap.put(PRIVATE, Integer.valueOf(128));
        itemAttributeMap.put(PROPERTY, Integer.valueOf(256));
        itemAttributeMap.put(LOCALIZED, Integer.valueOf(512));
        itemAttributeMap.put(INHERITED, Integer.valueOf(1024));
        itemAttributeMap.put(INITIAL, Integer.valueOf(2048));
        itemAttributeMap.put(TIM_IGNORE, Integer.valueOf(8192));
        itemAttributeMap.put(ENCRYPTED, Integer.valueOf(16384));
        itemAttributeMap.put(PRIMITIVE, Integer.valueOf(65536));
        modifierMapping = Collections.unmodifiableMap((Map<? extends String, ? extends Integer>)itemAttributeMap);
    }

    public Object setProperty(SessionContext ctx, String name, Object value)
    {
        Object ret = setPropertyNonRecursively(ctx, name, value);
        for(AttributeDescriptor ad : getAllSubAttributeDescriptors())
        {
            ad.setPropertyNonRecursively(ctx, name, value);
        }
        return ret;
    }


    protected Object setPropertyNonRecursively(SessionContext ctx, String name, Object value)
    {
        return super.setProperty(ctx, name, value);
    }


    public Object setLocalizedProperty(SessionContext ctx, String name, Object value)
    {
        Object ret = setLocalizedPropertyNonRecursively(ctx, name, value);
        for(AttributeDescriptor ad : getAllSubAttributeDescriptors())
        {
            ad.setLocalizedPropertyNonRecursively(ctx, name, value);
        }
        return ret;
    }


    protected Object setLocalizedPropertyNonRecursively(SessionContext ctx, String name, Object value)
    {
        return super.setLocalizedProperty(ctx, name, value);
    }


    public Object removeLocalizedProperty(SessionContext ctx, String name)
    {
        Object ret = removeLocalizedPropertyNonRecursively(ctx, name);
        for(AttributeDescriptor ad : getAllSubAttributeDescriptors())
        {
            ad.removeLocalizedPropertyNonRecursively(ctx, name);
        }
        return ret;
    }


    protected Object removeLocalizedPropertyNonRecursively(SessionContext ctx, String name)
    {
        return super.removeLocalizedProperty(ctx, name);
    }


    public Object removeProperty(SessionContext ctx, String name)
    {
        Object ret = removePropertyNonRecursively(ctx, name);
        for(AttributeDescriptor ad : getAllSubAttributeDescriptors())
        {
            ad.removePropertyNonRecursively(ctx, name);
        }
        return ret;
    }


    protected Object removePropertyNonRecursively(SessionContext ctx, String name)
    {
        return super.removeProperty(ctx, name);
    }


    public Set<AttributeDescriptor> getSubAttributeDescriptors()
    {
        String query = "SELECT {att:" + PK + "} FROM {AttributeDescriptor AS att JOIN ComposedType AS ct ON {att:" + ENCLOSING_TYPE + "}={ct:" + ComposedType.PK + "}} WHERE {ct:" + ComposedType.SUPERTYPE + "}=?encType AND {att:qualifier}=?attQualifier";
        Map<String, Object> params = new HashMap<>();
        params.put("encType", getEnclosingType());
        params.put("attQualifier", getQualifier());
        return new LinkedHashSet<>(
                        FlexibleSearch.getInstance().search(query, params, AttributeDescriptor.class)
                                        .getResult());
    }


    public Set<AttributeDescriptor> getAllSubAttributeDescriptors()
    {
        return getAttributeDescriptorImpl().getAllSubAttributeDescriptors();
    }


    public String getName(SessionContext ctx)
    {
        String name = super.getName(ctx);
        if(name == null && isInherited())
        {
            AttributeDescriptor sfd = getSuperAttributeDescriptor();
            if(sfd != null)
            {
                name = sfd.getName(ctx);
            }
        }
        return name;
    }


    public Map getAllNames()
    {
        Map<Object, Object> ret = new HashMap<>();
        AttributeDescriptor sfd = getSuperAttributeDescriptor();
        if(sfd != null)
        {
            ret.putAll(sfd.getAllNames());
        }
        ret.putAll(super.getAllNames());
        return ret;
    }


    protected void checkTypeChange(Type newType) throws JaloInvalidParameterException
    {
        Type prev = isInherited() ? getSuperAttributeDescriptor().getAttributeType() : getAttributeType();
        if(!prev.isAssignableFrom(newType))
        {
            throw new JaloInvalidParameterException("new type " + newType + " of " + this + " is not assignable from old type " + prev + " - cannot change type", 0);
        }
    }


    public void setAttributeType(Type type) throws JaloInvalidParameterException
    {
        checkTypeChange(type);
        changeAttributeTypeInternal(type, 0, false);
        for(AttributeDescriptor ad : getAllSubAttributeDescriptors())
        {
            ad.changeAttributeTypeInternal(type, 0, false);
        }
    }


    private final void changeAttributeTypeInternal(Type type, int modifiers, boolean redeclare)
    {
        try
        {
            (new Object(this, "attributeType", redeclare, type, modifiers))
                            .set(null);
        }
        catch(de.hybris.platform.jalo.Item.JaloCachedComputationException e)
        {
            Throwable cause = e.getCause();
            if(cause instanceof JaloInvalidParameterException)
            {
                throw (JaloInvalidParameterException)cause;
            }
            throw new JaloSystemException(cause);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void redeclareAttributeType(Type type, int modifiers) throws JaloInvalidParameterException
    {
        checkTypeChange(type);
        changeAttributeTypeInternal(type, modifiers, true);
    }


    public ComposedType getDeclaringEnclosingType()
    {
        return getAttributeDescriptorImpl().getDeclaringEnclosingType();
    }


    public ComposedType getEnclosingType()
    {
        return (ComposedType)(new Object(this, ENCLOSING_TYPE))
                        .get(null);
    }


    public int getModifiers()
    {
        return ((Integer)(new Object(this, MODIFIERS))
                        .get(null)).intValue();
    }


    public void setModifiers(int modifiers)
    {
        (new Object(this, MODIFIERS, modifiers))
                        .set(null);
    }


    public boolean isReadable()
    {
        return ((getModifiers() & 0x1) != 0);
    }


    public void setReadable(boolean readable) throws JaloInvalidParameterException
    {
        (new Object(this, MODIFIERS, readable))
                        .set(null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isWriteable()
    {
        return isWritable();
    }


    public boolean isWritable()
    {
        return ((getModifiers() & 0x2) != 0);
    }


    public void setWritable(boolean writable) throws JaloInvalidParameterException
    {
        (new Object(this, MODIFIERS, writable))
                        .set(null);
    }


    public boolean isRemovable()
    {
        return ((getModifiers() & 0x4) != 0);
    }


    public void setRemovable(boolean removable) throws JaloInvalidParameterException
    {
        (new Object(this, MODIFIERS, removable))
                        .set(null);
    }


    public boolean isOptional()
    {
        return ((getModifiers() & 0x8) != 0);
    }


    public void setOptional(boolean optional) throws JaloInvalidParameterException
    {
        (new Object(this, MODIFIERS, optional))
                        .set(null);
    }


    public boolean isSearchable()
    {
        return ((getModifiers() & 0x10) != 0);
    }


    public void setSearchable(boolean searchable) throws JaloInvalidParameterException
    {
        if(isSearchable() != searchable)
        {
            checkSearchableChange(searchable);
            (new Object(this, MODIFIERS, searchable))
                            .set(null);
        }
    }


    protected void checkSearchableChange(boolean newSearchable) throws JaloInvalidParameterException
    {
        if(!isSearchable() && newSearchable)
        {
            if(!isProperty() && (getDatabaseColumn() == null || getPersistenceClass() == null))
            {
                throw new JaloInvalidParameterException("attribute " + this + " cannot be made searchable because of missing persistence information", 0);
            }
        }
    }


    public boolean isPartOf()
    {
        return ((getModifiers() & 0x20) != 0);
    }


    public boolean getDontOptimize()
    {
        return ((getModifiers() & 0x2000) != 0);
    }


    public boolean isEncrypted()
    {
        return ((getModifiers() & 0x4000) != 0);
    }


    public boolean isPrimitive()
    {
        return ((getModifiers() & 0x10000) != 0);
    }


    public void setPartOf(boolean partOf) throws JaloInvalidParameterException
    {
        if(isPartOf() != partOf)
        {
            checkPartOfChange(partOf);
            (new Object(this, MODIFIERS, partOf))
                            .set(null);
        }
    }


    protected void checkPartOfChange(boolean newPartOf) throws JaloInvalidParameterException
    {
        if(!isPartOf() && newPartOf)
        {
            Type valueType = getRealAttributeType();
            if(!(valueType instanceof ComposedType) && (!(valueType instanceof CollectionType) ||
                            !(((CollectionType)valueType).getElementType(null) instanceof ComposedType)))
            {
                throw new JaloInvalidParameterException("cannot set attribute " + this + " to partOf since it holds no items", 0);
            }
        }
    }


    public boolean isPrivate()
    {
        return ((getModifiers() & 0x80) != 0);
    }


    public void setPrivate(boolean priv)
    {
        (new Object(this, MODIFIERS, priv))
                        .set(null);
    }


    public void setDontOptimize(boolean dontopt)
    {
        (new Object(this, MODIFIERS, dontopt))
                        .set(null);
    }


    public void setEncrypted(boolean encrypt)
    {
        if(isEncrypted() != encrypt)
        {
            (new Object(this, MODIFIERS, encrypt))
                            .set(null);
        }
    }


    public void setPrimitive(boolean primitive)
    {
        if(isPrimitive() != primitive)
        {
            if(primitive)
            {
                Type myType = getAttributeType();
                if(!(myType instanceof AtomicType))
                {
                    throw new JaloSystemException("Can not mark attribute descriptor " + getEnclosingType().getCode() + "." +
                                    getQualifier() + " as primitive because it is not atomic type");
                }
                if(MethodUtils.getPrimitiveType(((AtomicType)myType).getJavaClass()) == null)
                {
                    throw new JaloSystemException("Can not mark attribute descriptor " + getEnclosingType().getCode() + "." +
                                    getQualifier() + " as primitive because it has no corresponding primitive java class");
                }
                if(primitive && isOptional())
                {
                    throw new JaloSystemException("Can not mark attribute descriptor " + getEnclosingType().getCode() + "." +
                                    getQualifier() + " as primitive because it has no corresponding primitive java class");
                }
            }
            (new Object(this, MODIFIERS, primitive))
                            .set(null);
        }
    }


    public boolean isLocalized()
    {
        return ((getModifiers() & 0x200) != 0);
    }


    public void setLocalized(boolean localized) throws JaloInvalidParameterException
    {
        if(isLocalized() != localized)
        {
            checkLocalizedChange(localized);
            (new Object(this, MODIFIERS, localized))
                            .set(null);
        }
    }


    protected void checkLocalizedChange(boolean newLoc) throws JaloInvalidParameterException
    {
        boolean typeProvidesLocalization = false;
        Type valueType = getRealAttributeType();
        if(valueType instanceof MapType)
        {
            Type argType = ((MapType)valueType).getArgumentType();
            if(argType instanceof ComposedType && Language.class.isAssignableFrom(((ComposedType)argType).getJaloClass()))
            {
                typeProvidesLocalization = true;
            }
        }
        if(newLoc && !typeProvidesLocalization)
        {
            throw new JaloInvalidParameterException("cannot set attribute " + this + " localized since value type doesnt allow", 0);
        }
    }


    public boolean isInherited()
    {
        return ((getModifiers() & 0x400) != 0);
    }


    public boolean isInitial()
    {
        return ((getModifiers() & 0x800) != 0);
    }


    public void setInitial(boolean initial)
    {
        if(isInitial() != initial)
        {
            (new Object(this, MODIFIERS, initial))
                            .set(null);
        }
    }


    public boolean isProperty()
    {
        return ((getModifiers() & 0x100) != 0);
    }


    public void setProperty(boolean isProperty) throws JaloInvalidParameterException
    {
        if(isProperty() != isProperty)
        {
            checkPropertyChange(isProperty);
            (new Object(this, MODIFIERS, isProperty))
                            .set(null);
        }
    }


    protected void checkPropertyChange(boolean newProp) throws JaloInvalidParameterException
    {
        if(!isProperty() && newProp)
        {
            if(!ExtensibleItem.class.isAssignableFrom(getEnclosingType().getJaloClass()))
            {
                throw new JaloInvalidParameterException("cannot set property flag for attribute " + this + " since enclosing type is not ExtensibleItem subtype", 0);
            }
        }
    }


    public boolean isUnique()
    {
        return Boolean.TRUE.equals(getProperty(UNIQUE));
    }


    public void setUnique(boolean unique)
    {
        setProperty(UNIQUE, unique ? Boolean.TRUE : Boolean.FALSE);
    }


    public boolean isHiddenForUI()
    {
        return Boolean.TRUE.equals(getProperty("hiddenForUI"));
    }


    public void setHiddenForUI(boolean hiddenForUI)
    {
        setProperty("hiddenForUI", hiddenForUI ? Boolean.TRUE : Boolean.FALSE);
    }


    public boolean isReadOnlyForUI()
    {
        return Boolean.TRUE.equals(getProperty("readOnlyForUI"));
    }


    public void setReadOnlyForUI(boolean readOnlyForUI)
    {
        setProperty("readOnlyForUI", readOnlyForUI ? Boolean.TRUE : Boolean.FALSE);
    }


    public void setAttributeHandler(String attributeHandler)
    {
        setProperty(ATTRIBUTE_HANDLER, attributeHandler);
    }


    public String getAttributeHandler()
    {
        return (String)getProperty(ATTRIBUTE_HANDLER);
    }


    public String getDefaultValueDefinitionString()
    {
        return (String)getProperty(DEFAULTVALUE_DEFINITIONSTRING);
    }


    public Object getDefaultValue()
    {
        return getDefaultValue(getSession().getSessionContext());
    }


    public Object getDefaultValue(SessionContext ctx)
    {
        Object ret = getProperty(null, DEFAULTVALUE);
        ret = interpretAndReplaceDefaultValue(ret);
        if(ret != null && isLocalized())
        {
            try
            {
                Map map = (Map)ret;
                ret = (ctx != null && ctx.getLanguage() != null) ? map.get(ctx.getLanguage()) : map;
            }
            catch(ClassCastException e)
            {
                setProperty(null, DEFAULTVALUE, null);
                ret = null;
            }
        }
        if(ret == null && isPrimitive())
        {
            Preconditions.checkArgument(getAttributeType(ctx) instanceof AtomicType);
            ret = ConvertUtils.convert((String)null, ((AtomicType)getAttributeType(ctx)).getJavaClass());
        }
        return (ret != null) ? ret : getAttributeType().getDefaultValue(ctx);
    }


    protected Object interpretAndReplaceDefaultValue(Object current)
    {
        Object ret = current;
        if(current instanceof DefaultValueExpressionHolder)
        {
            String expr = ((DefaultValueExpressionHolder)current).getExpression();
            Interpreter interpreter = BeanShellUtils.createInterpreter();
            try
            {
                ret = interpreter.eval(expr);
            }
            catch(EvalError e)
            {
                log.error("Error translating default value '" + expr + "' of attribute " + this + " - replacing by NULL!");
                ret = null;
            }
            setProperty(null, DEFAULTVALUE, ret);
        }
        return ret;
    }


    public void setDefaultValue(Object value) throws JaloInvalidParameterException
    {
        setDefaultValue(getSession().getSessionContext(), value);
    }


    public void setDefaultValue(SessionContext ctx, Object value) throws JaloInvalidParameterException
    {
        checkDefaultValueAssignability(ctx, value);
        if(isLocalized())
        {
            Map<?, ?> toSet = null;
            if(ctx != null && ctx.getLanguage() != null)
            {
                Object o = getProperty(null, DEFAULTVALUE);
                toSet = (o instanceof Map) ? (Map)o : null;
                toSet = (toSet != null) ? new HashMap<>(toSet) : new HashMap<>();
                if(value != null)
                {
                    toSet.put(ctx.getLanguage(), value);
                }
                else
                {
                    toSet.remove(ctx.getLanguage());
                }
            }
            else
            {
                if(!(value instanceof Map))
                {
                    throw new JaloInvalidParameterException("value must be Map<Language,?> when calling setDefaultValue() without context languge", 0);
                }
                toSet = (Map<?, ?>)value;
            }
            setProperty(null, DEFAULTVALUE, toSet);
        }
        else
        {
            setProperty(null, DEFAULTVALUE, value);
        }
        removeProperty(ctx, DEFAULTVALUE_DEFINITIONSTRING);
    }


    protected void checkDefaultValueAssignability(SessionContext ctx, Object newVal) throws JaloInvalidParameterException
    {
        Type valueType = getAttributeType(ctx);
        Language l = (ctx != null) ? ctx.getLanguage() : null;
        if(newVal != null && !valueType.isInstance(newVal))
        {
            throw new JaloInvalidParameterException(
                            (isLocalized() ? ("[" + ((l != null) ? l.getIsoCode() : "<all>") + "]") : "") + "cannot set default value of " + (isLocalized() ? ("[" + ((l != null) ? l.getIsoCode() : "<all>") + "]") : "") + " to " + this + "(class:" + newVal + ") since value is not instance of "
                                            + newVal
                                            .getClass()
                                            .getName(),
                            0);
        }
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("AttributeDescriptor.getDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, DESCRIPTION);
    }


    public void setDescription(String description)
    {
        setDescription(getSession().getSessionContext(), description);
    }


    public void setDescription(SessionContext ctx, String description)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("AttributeDescriptor.setDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, DESCRIPTION, description);
    }


    public Map getAllDescriptions()
    {
        return getAllDescriptions(getSession().getSessionContext());
    }


    public Map getAllDescriptions(SessionContext ctx)
    {
        return (Map)getLocalizedProperty(getAllValuesSessionContext(ctx), DESCRIPTION);
    }


    public void setAllDescriptions(Map descriptions)
    {
        setAllDescriptions(getSession().getSessionContext(), descriptions);
    }


    public void setAllDescriptions(SessionContext ctx, Map descriptions)
    {
        setLocalizedProperty(getAllValuesSessionContext(ctx), DESCRIPTION, descriptions);
    }


    public AttributeDescriptor getSelectionOf()
    {
        return getAttributeDescriptorImpl().getSelectionOf();
    }


    public void setSelectionOf(AttributeDescriptor descriptor) throws JaloInvalidParameterException
    {
        getAttributeDescriptorImpl().setSelectionOf(descriptor);
        for(Iterator<AttributeDescriptor> iter = getAllSubAttributeDescriptors().iterator(); iter.hasNext(); )
        {
            AttributeDescriptor subAttribute = iter.next();
            subAttribute.getAttributeDescriptorImpl().setSelectionOf(descriptor);
        }
    }


    protected void checkSelectionOfDescriptor(AttributeDescriptor newSelect) throws JaloInvalidParameterException
    {
        if(newSelect != null)
        {
            if(equals(newSelect))
            {
                throw new JaloInvalidParameterException("cannot create self selection of attribute " + this, 0);
            }
            if(!getEnclosingType().equals(newSelect.getEnclosingType()))
            {
                throw new JaloInvalidParameterException("cannot set selection-of of attribute " + this + " to " + newSelect + " since enclosing types are different", 0);
            }
            Type myValueType = isLocalized() ? ((MapType)getRealAttributeType()).getReturnType() : getRealAttributeType();
            Type selectionValueType = newSelect.isLocalized() ? ((MapType)newSelect.getRealAttributeType()).getReturnType() : newSelect.getRealAttributeType();
            if(selectionValueType instanceof CollectionType)
            {
                if(!myValueType.isAssignableFrom(((CollectionType)selectionValueType).getElementType()))
                {
                    throw new JaloInvalidParameterException("cannot set selection-of of attribute " + this + " to " + newSelect + " since value types " + myValueType + " and " + selectionValueType + " are incompatible", 0);
                }
            }
            else if(!myValueType.isAssignableFrom(selectionValueType))
            {
                throw new JaloInvalidParameterException("cannot set selection-of of attribute " + this + " to " + newSelect + " since value types " + myValueType + " and " + selectionValueType + " are incompatible", 0);
            }
        }
    }


    public String getDatabaseColumn()
    {
        return (String)(new Object(this, DATABASECOLUMN))
                        .get(null);
    }


    public void setDatabaseColumn(String col)
    {
        (new Object(this, DATABASECOLUMN, col))
                        .set(null);
    }


    public String getProposedDatabaseColumn()
    {
        return getAttributeDescriptorImpl().getProposedDatabaseColumn();
    }


    public Class getPersistenceClass()
    {
        return getAttributeDescriptorImpl().getPersistenceClass();
    }


    public AtomicType getPersistenceType()
    {
        return getAttributeDescriptorImpl().getPersistenceType();
    }


    protected AttributeDescriptorImpl getAttributeDescriptorImpl()
    {
        return (AttributeDescriptorImpl)this.impl;
    }


    protected int getIntModifiersFromMap(Map allAttributes)
    {
        int result = 0;
        for(Iterator<Map.Entry> it = allAttributes.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = it.next();
            Integer i = (Integer)modifierMapping.get(entry.getKey());
            if(i != null && entry.getValue() instanceof Boolean && ((Boolean)entry.getValue()).booleanValue())
            {
                result |= i.intValue();
            }
        }
        return result;
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(getEnclosingType().getCode()).append(".").append(getQualifier());
        sb.append(":").append(getRealAttributeType().getCode());
        sb.append("[").append(toString(getModifiers())).append("]");
        return sb.toString();
    }


    public static String toString(int modifiers)
    {
        StringBuilder sb = new StringBuilder();
        if((modifiers & 0x400) != 0)
        {
            sb.append('/');
        }
        if((modifiers & 0x1) != 0)
        {
            sb.append('r');
        }
        if((modifiers & 0x2) != 0)
        {
            sb.append('w');
        }
        if((modifiers & 0x8) != 0)
        {
            sb.append('o');
        }
        if((modifiers & 0x200) != 0)
        {
            sb.append('l');
        }
        if((modifiers & 0x10) != 0)
        {
            sb.append('S');
        }
        if((modifiers & 0x20) != 0)
        {
            sb.append('P');
        }
        if((modifiers & 0x800) != 0)
        {
            sb.append('C');
        }
        if((modifiers & 0x4) != 0)
        {
            sb.append('R');
        }
        if((modifiers & 0x80) != 0)
        {
            sb.append('#');
        }
        if((modifiers & 0x100) != 0)
        {
            sb.append('p');
        }
        if((modifiers & 0x2000) != 0)
        {
            sb.append('d');
        }
        return sb.toString();
    }


    public String getPersistenceQualifier()
    {
        return ((AttributeDescriptorImpl)getImplementation()).getPersistenceQualifier();
    }


    public boolean isRedeclared()
    {
        AttributeDescriptor superAttribute = getSuperAttributeDescriptor();
        int changeableModifiers = 2203;
        return (superAttribute != null && ((
                        getModifiers() & 0x89B) != (superAttribute.getModifiers() & 0x89B) ||
                        !getRealAttributeType().equals(superAttribute.getRealAttributeType())));
    }


    protected Map getXMLCustomProperties()
    {
        return Collections.EMPTY_MAP;
    }


    public String getXMLDefinition()
    {
        XMLOutputter xmlOut;
        try
        {
            xmlOut = new XMLOutputter(new StringWriter(), "UTF-8");
            xmlOut.setEscaping(true);
            xmlOut.setLineBreak(LineBreak.UNIX);
            xmlOut.setIndentation("\t");
            xmlOut.setQuotationMark('"');
        }
        catch(UnsupportedEncodingException e)
        {
            throw new JaloSystemException(e);
        }
        return exportXMLDefinition(xmlOut);
    }


    public String exportXMLDefinition(XMLOutputter xout)
    {
        try
        {
            String persistenceType, persistenceQualifier;
            xout.startTag("attribute");
            xout.attribute("generate", String.valueOf(isGenerate()));
            xout.attribute("autocreate", String.valueOf(isAutocreate()));
            if(isRedeclared())
            {
                xout.attribute("redeclare", Boolean.TRUE.toString());
            }
            xout.attribute("qualifier", getQualifier());
            xout.attribute("type", getRealAttributeType().getCode());
            if(!AttributeDescriptor.class.equals(getClass()))
            {
                xout.attribute("metatype", getComposedType().getCode());
            }
            if(isProperty())
            {
                persistenceType = "property";
                persistenceQualifier = "";
            }
            else if(getPersistenceQualifier() != null)
            {
                persistenceType = "cmp";
                persistenceQualifier = getPersistenceQualifier();
            }
            else
            {
                persistenceType = "jalo";
                persistenceQualifier = "";
            }
            exportXMLDefinitionDefaultValue(xout);
            xout.startTag("persistence");
            xout.attribute("type", persistenceType);
            xout.attribute("qualifier", persistenceQualifier);
            xout.endTag();
            xout.startTag("modifiers");
            xout.attribute("read", String.valueOf(isReadable()));
            xout.attribute("write", String.valueOf(isWritable()));
            xout.attribute("search", String.valueOf(isSearchable()));
            xout.attribute("encrypted", String.valueOf(isEncrypted()));
            xout.attribute("optional", String.valueOf(isOptional()));
            xout.attribute("removable", String.valueOf(isRemovable()));
            xout.attribute("initial", String.valueOf(isInitial()));
            xout.attribute("unique", String.valueOf(isUnique()));
            xout.attribute("private", String.valueOf(isPrivate()));
            xout.attribute("partof", String.valueOf(isPartOf()));
            xout.endTag();
            Map custProps = getXMLCustomProperties();
            if(custProps != null && !custProps.isEmpty())
            {
                xout.startTag("custom-properties");
                for(Iterator<Map.Entry> it = custProps.entrySet().iterator(); it.hasNext(); )
                {
                    Map.Entry e = it.next();
                    xout.startTag("property");
                    xout.attribute("name", (String)e.getKey());
                    xout.startTag("value");
                    xout.pcdata((String)e.getValue());
                    xout.endTag();
                    xout.endTag();
                }
                xout.endTag();
            }
            xout.endTag();
        }
        catch(IOException e)
        {
            throw new JaloSystemException(e);
        }
        return xout.getWriter().toString();
    }


    protected void exportXMLDefinitionDefaultValue(XMLOutputter xout) throws IOException
    {
        Object defaultvalue = getDefaultValue();
        String defaultvalueDef = getDefaultValueDefinitionString();
        if(defaultvalue != null)
        {
            if(getRealAttributeType() instanceof AtomicType)
            {
                if(defaultvalue instanceof String || defaultvalue instanceof Boolean)
                {
                    writeDefaultValueTagAtomic(xout, defaultvalue.getClass().getName(), "\"" + defaultvalue.toString() + "\"");
                }
                else if(defaultvalue instanceof Integer || defaultvalue instanceof Double)
                {
                    writeDefaultValueTagAtomic(xout, defaultvalue.getClass().getName(), defaultvalue.toString());
                }
                else if(defaultvalue instanceof Date)
                {
                    writeDefaultValueTagAtomic(xout, defaultvalue.getClass().getName(),
                                    String.valueOf(((Date)defaultvalue).getTime()));
                    xout.comment(" date is: '" + defaultvalue.toString() + "' ");
                }
                else
                {
                    xout.comment(" could not export defaultvalue '" + defaultvalue.toString() + "' ");
                }
            }
            else
            {
                xout.comment(" could not export defaultvalue '" + defaultvalue.toString() + "' ");
            }
        }
        else if(defaultvalueDef != null)
        {
            xout.startTag("defaultvalue");
            xout.pcdata(defaultvalueDef);
            xout.endTag();
        }
    }


    protected void writeDefaultValueTagAtomic(XMLOutputter xout, String atomictype, String value)
    {
        try
        {
            xout.startTag("defaultvalue");
            xout.pcdata("new " + atomictype + "( " + value + " )");
            xout.endTag();
        }
        catch(IOException e)
        {
            throw new JaloSystemException(e);
        }
    }


    private final AttributeDescriptor getSuperAttributeDescriptor()
    {
        try
        {
            ComposedType superEnclosingType = getEnclosingType().getSuperType();
            return (superEnclosingType != null) ? superEnclosingType.getAttributeDescriptor(getQualifier()) : null;
        }
        catch(JaloItemNotFoundException jaloItemNotFoundException)
        {
            return null;
        }
    }


    public boolean checkTypePermission(UserRight right)
    {
        boolean ret = checkTypePermission((Principal)getSession().getSessionContext().getUser(), right);
        return ret;
    }


    public boolean checkTypePermission(Principal p, UserRight right)
    {
        if(p == null || right == null)
        {
            throw new JaloInvalidParameterException("principal or right was null (principal=" + p + ", right=" + right + ")", 0);
        }
        if(p.isAdmin())
        {
            return true;
        }
        int match = -1;
        ComposedType enclosing = null;
        AttributeDescriptor ad = this;
        do
        {
            match = ad.checkItemPermission(p, right);
            if(match != -1)
            {
                continue;
            }
            enclosing = ad.getEnclosingType();
            match = enclosing.checkItemPermission(p, right);
            if(match != -1)
            {
                continue;
            }
            ad = ad.getSuperAttributeDescriptor();
        }
        while(match == -1 && ad != null);
        if(match == -1 && enclosing != null)
        {
            for(enclosing = enclosing.getSuperType(); enclosing != null; enclosing = enclosing.getSuperType())
            {
                match = enclosing.checkItemPermission(p, right);
                if(match != -1)
                {
                    break;
                }
            }
        }
        return ACLCache.translatePermissionToBoolean(match);
    }


    public Boolean isRuntime()
    {
        TypeInfoMap persistenceInfo = getTenant().getPersistenceManager().getPersistenceInfo(getDeclaringEnclosingType().getCode());
        return Boolean.valueOf((3 == persistenceInfo.getPropertyType(getQualifier()) &&
                        isProperty() &&
                        getDatabaseColumn() == null &&
                        !getDontOptimize()));
    }
}
