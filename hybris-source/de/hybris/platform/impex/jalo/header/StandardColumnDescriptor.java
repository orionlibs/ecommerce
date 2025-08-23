package de.hybris.platform.impex.jalo.header;

import de.hybris.platform.core.PK;
import de.hybris.platform.impex.jalo.translators.AbstractValueTranslator;
import de.hybris.platform.impex.jalo.translators.MapValueTranslator;
import de.hybris.platform.impex.jalo.util.ImpExUtils;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class StandardColumnDescriptor extends AbstractColumnDescriptor implements Comparable<StandardColumnDescriptor>
{
    private static final Logger log = Logger.getLogger(StandardColumnDescriptor.class.getName());
    private boolean readOnly = false;
    private boolean writable = false;
    private boolean readable = false;
    private boolean initialOnly = false;
    private boolean partOf = false;
    private boolean localized = false;
    private boolean mandatory = false;
    private final AttributeDescriptor ad;
    private final AbstractValueTranslator valueTranslator;
    private final Thread valueTranslatorThread;
    private final Map<Thread, AbstractValueTranslator> threadBoundValueTranslators = Collections.synchronizedMap(new HashMap<>());
    private final boolean defaultValueIsTranslated = false;
    private Object defaultValue = null;
    private Language configuredLanguage = null;


    public StandardColumnDescriptor(int position, HeaderDescriptor header, String expr) throws HeaderValidationException
    {
        super(position, header, expr);
        this.ad = findAttributeDescriptor(header, position, getComposedTypeCode(), getQualifier());
        if(this.ad != null)
        {
            boolean isPK = Item.PK.equalsIgnoreCase(this.ad.getQualifier());
            this.readOnly = (!this.ad.isWritable() && !this.ad.isInitial() && !isPK);
            this.initialOnly = (isPK || (!this.readOnly && !this.ad.isWritable() && this.ad.isInitial()));
            this.partOf = this.ad.isPartOf();
            this.localized = this.ad.isLocalized();
            this.mandatory = (!this.ad.isPrivate() && !this.ad.isOptional());
            this.readable = this.ad.isReadable();
            this.writable = this.ad.isWritable();
            this.valueTranslator = createValueTranslator();
            this.valueTranslatorThread = Thread.currentThread();
        }
        else
        {
            this.readOnly = false;
            this.initialOnly = false;
            this.partOf = false;
            this.localized = false;
            this.mandatory = false;
            this.readable = false;
            this.writable = false;
            this.valueTranslator = null;
            this.valueTranslatorThread = Thread.currentThread();
        }
    }


    public StandardColumnDescriptor(int position, HeaderDescriptor header, String expr, AbstractDescriptor.DescriptorParams params) throws HeaderValidationException
    {
        super(position, header, expr, params);
        this.ad = findAttributeDescriptor(getHeader(), position, getComposedTypeCode(), getQualifier());
        if(this.ad != null)
        {
            this.readOnly = (!this.ad.isWritable() && !this.ad.isInitial());
            this.initialOnly = (!this.readOnly && !this.ad.isWritable() && this.ad.isInitial());
            this.partOf = this.ad.isPartOf();
            this.localized = this.ad.isLocalized();
            this.mandatory = (!this.ad.isPrivate() && !this.ad.isOptional());
            this.valueTranslator = createValueTranslator();
            this.valueTranslatorThread = Thread.currentThread();
        }
        else
        {
            this.readOnly = false;
            this.initialOnly = false;
            this.partOf = false;
            this.localized = false;
            this.mandatory = false;
            this.valueTranslator = null;
            this.valueTranslatorThread = Thread.currentThread();
        }
    }


    public int compareTo(StandardColumnDescriptor other)
    {
        if(this.ad == null || other.ad == null)
        {
            return Integer.compare((this.ad == null) ? 0 : 1, (other.ad == null) ? 0 : 1);
        }
        return this.ad.getQualifier().compareTo(other.ad.getQualifier());
    }


    protected void validate() throws HeaderValidationException
    {
        super.validate();
        if(getAttributeDescriptor() == null)
        {
            throw new HeaderValidationException(getHeader(), "unknown attribute '" +
                            getQualifier() + "' in header '" + getHeader().getDefinitionSrc() + "'", 4);
        }
        if(!isLocalized() && getLanguageIso() != null)
        {
            throw new HeaderValidationException(getHeader(), "misused modifier [lang=...] in header '" +
                            getHeader().getDefinitionSrc() + "' -- '" + getQualifier() + "' isn't a localizable attribute! ", 5);
        }
        if(isUnique() && isReadOnly() && ImpExUtils.isStrictMode(getHeader().getReader().getValidationMode()))
        {
            throw new HeaderValidationException(getHeader(), "unique attribute " +
                            getAttributeDescriptor().getRealAttributeType()
                                            .getCode() + "." + getQualifier() + " is read-only which is not allowed", 5);
        }
        if(isReadOnly() && !isForceWrite() && (getHeader().isInsertMode() || getHeader().isInsertUpdateMode()))
        {
            log.warn("column " + getQualifier() + " of type " + getHeader().getTypeCode() + " is read-only, but you want to insert/update it. If you want to write this read-only attribute explicit, you have to use the forceWrite=true modifier.");
        }
        if(getValueTranslator() == null)
        {
            throw new HeaderValidationException(getHeader(), "no available value translator for attribute " +
                            getQualifier() + " type = " +
                            getAttributeDescriptor().getRealAttributeType().getCode(), 5);
        }
        getValueTranslator().validate(this);
        if(isVirtual())
        {
            try
            {
                getDefaultValue();
            }
            catch(UnresolvedValueException e)
            {
                throw new HeaderValidationException(getHeader(), "virtual column " +
                                getQualifier() + " value could not be translated due to " + e
                                .getMessage(), 0);
            }
        }
    }


    public static AttributeDescriptor findAttributeDescriptor(HeaderDescriptor header, int position, String ownType, String qualifier) throws HeaderValidationException
    {
        if(ownType != null)
        {
            ComposedType ct = null;
            try
            {
                ct = TypeManager.getInstance().getComposedType(ownType);
            }
            catch(JaloItemNotFoundException e)
            {
                throw new HeaderValidationException(header, "attribute type " + ownType + " is unknown (column " + position + ")", 1);
            }
            if(!header.getConfiguredComposedType().isAssignableFrom((Type)ct))
            {
                throw new HeaderValidationException(header, "attribute type " + ownType + " is not assignable from header type " + header
                                .getConfiguredComposedType()
                                .getCode() + "(column " + position + ")", 0);
            }
            try
            {
                return ct.getAttributeDescriptorIncludingPrivate(qualifier);
            }
            catch(JaloItemNotFoundException e)
            {
                throw new HeaderValidationException(header, "attribute " + ownType + "." + qualifier + " is unknown (column " + position + ")", 4);
            }
        }
        List<AttributeDescriptor> descriptors = null;
        Collection<ComposedType> toCheck = Collections.singleton(header.getConfiguredComposedType());
        while(!toCheck.isEmpty())
        {
            Set<ComposedType> nextToCheck = new HashSet();
            for(Iterator<ComposedType> iter = toCheck.iterator(); iter.hasNext(); )
            {
                ComposedType ct = iter.next();
                AttributeDescriptor found = null;
                try
                {
                    found = ct.getAttributeDescriptorIncludingPrivate(qualifier);
                }
                catch(JaloItemNotFoundException jaloItemNotFoundException)
                {
                }
                if(found == null)
                {
                    nextToCheck.addAll(ct.getSubTypes());
                    continue;
                }
                if(descriptors == null)
                {
                    descriptors = new LinkedList();
                }
                descriptors.add(found);
            }
            toCheck = nextToCheck;
        }
        if(descriptors == null || descriptors.isEmpty())
        {
            return null;
        }
        if(descriptors.size() > 1)
        {
            List<AttributeDescriptor> types = new ArrayList<>(descriptors);
            for(ListIterator<AttributeDescriptor> iter = types.listIterator(); iter.hasNext(); )
            {
                AttributeDescriptor ad = iter.next();
                iter.set(ad.getEnclosingType().getCode());
            }
            throw new HeaderValidationException(header, "column " + position + ":" + qualifier + " is ambiguous - declared in more than one type ( found " + types + " )", 3);
        }
        return descriptors.get(0);
    }


    protected AbstractValueTranslator createValueTranslator() throws HeaderValidationException
    {
        String customTranslatorClass = getDescriptorData().getModifier("translator");
        AbstractValueTranslator ret = (customTranslatorClass != null) ? createCustomValueTranslator(customTranslatorClass) : createTypedValueTranslator();
        ret.init(this);
        return ret;
    }


    private AbstractValueTranslator createCustomValueTranslator(String className) throws HeaderValidationException
    {
        try
        {
            Class<?> c = Class.forName(className);
            return (AbstractValueTranslator)c.newInstance();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new HeaderValidationException("invalid custom value translator class '" + className + "' - cannot create due to " + e
                            .getMessage(), 10);
        }
    }


    private AbstractValueTranslator createTypedValueTranslator() throws HeaderValidationException
    {
        AttributeDescriptor ad = getAttributeDescriptor();
        Type attributeType = isLocalized() ? ((MapType)ad.getRealAttributeType()).getReturnType(null) : ad.getRealAttributeType();
        if(attributeType instanceof AtomicType)
        {
            return AbstractValueTranslator.createTranslator(ad, (AtomicType)attributeType, ((AbstractDescriptor.ColumnParams)
                            getDescriptorData()).getItemPatternLists());
        }
        if(attributeType instanceof ComposedType)
        {
            return AbstractValueTranslator.createTranslator((ComposedType)attributeType, ((AbstractDescriptor.ColumnParams)
                            getDescriptorData()).getItemPatternLists());
        }
        if(attributeType instanceof MapType)
        {
            List[] patternLists = ((AbstractDescriptor.ColumnParams)getDescriptorData()).getItemPatternLists();
            if(patternLists != null && patternLists.length > 1)
            {
                throw new HeaderValidationException("alternative patterns are not allowed for MapTypes", 0);
            }
            return (AbstractValueTranslator)new MapValueTranslator((MapType)attributeType, patternLists);
        }
        if(attributeType instanceof CollectionType)
        {
            return AbstractValueTranslator.createTranslator(ad, (CollectionType)attributeType, ((AbstractDescriptor.ColumnParams)
                            getDescriptorData()).getItemPatternLists());
        }
        throw new HeaderValidationException("illegal attribute type " + attributeType
                        .getCode() + " of " + ad.getEnclosingType().getCode() + "." + ad
                        .getQualifier() + " - must be either atomic, composed or collection type (specify custom translator for other types)", 5);
    }


    public final Object importValue(String cellValue, Item forItem) throws UnresolvedValueException
    {
        if(isLocalized() && getLanguageIso() != null)
        {
            try
            {
                getLanguage();
            }
            catch(HeaderValidationException e)
            {
                throw new UnresolvedValueException(e, getQualifier(), cellValue);
            }
        }
        Object ret = getValueTranslator().importValue(cellValue, forItem);
        if(getValueTranslator().wasUnresolved())
        {
            throw new UnresolvedValueException(getQualifier(), cellValue);
        }
        if(getValueTranslator().wasEmpty() && hasDefaultValueDefinition())
        {
            if(isPartOf())
            {
                ret = calculateDefaultValue(forItem);
            }
            else
            {
                ret = getDefaultValue();
            }
        }
        return ret;
    }


    public String exportValue(Object attributeValue) throws UnresolvedValueException
    {
        String ret = getValueTranslator().exportValue(attributeValue);
        if(getValueTranslator().wasUnresolved())
        {
            throw new UnresolvedValueException("could not export value " + attributeValue);
        }
        return ret;
    }


    public boolean hasDefaultValueDefinition()
    {
        String defTxt = getDescriptorData().getModifier("default");
        return (defTxt != null || isVirtual());
    }


    public Object getDefaultValue() throws UnresolvedValueException
    {
        if(!isPartOf() && hasDefaultValueDefinition())
        {
            this.defaultValue = calculateDefaultValue(null);
        }
        return this.defaultValue;
    }


    public Object calculateDefaultValue(Item existing) throws UnresolvedValueException
    {
        Object ret = null;
        String defTxt = getDescriptorData().getModifier("default");
        if(defTxt != null || isVirtual())
        {
            AbstractValueTranslator tr = getValueTranslator();
            ret = tr.importValue(defTxt, existing);
            if(tr.wasUnresolved())
            {
                throw new UnresolvedValueException(getQualifier(), defTxt);
            }
        }
        return ret;
    }


    public AttributeDescriptor getAttributeDescriptor()
    {
        return this.ad;
    }


    public PK getAttributeDescriptorPk()
    {
        return this.ad.getPK();
    }


    public String getComposedTypeCode()
    {
        String ret = ((AbstractDescriptor.ColumnParams)getDescriptorData()).getQualifier();
        int pos = ret.indexOf('.');
        return (pos != -1) ? ret.substring(0, pos).trim() : null;
    }


    public boolean isLocalized()
    {
        return this.localized;
    }


    public boolean isMandatory()
    {
        return this.mandatory;
    }


    public boolean isUnique()
    {
        return "true".equalsIgnoreCase(getDescriptorData().getModifier("unique"));
    }


    public boolean isVirtual()
    {
        return "true".equalsIgnoreCase(getDescriptorData().getModifier("virtual"));
    }


    public boolean isAllowNull()
    {
        return "true".equalsIgnoreCase(getDescriptorData().getModifier("allownull"));
    }


    public boolean isForceWrite()
    {
        return "true".equalsIgnoreCase(getDescriptorData().getModifier("forceWrite"));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getLanguageIso()
    {
        return getLanguageModifier();
    }


    public String getLanguageModifier()
    {
        String ret = getDescriptorData().getModifier("lang");
        return (ret != null && ret.length() > 0) ? ret : null;
    }


    public String getLanguageIsoCode()
    {
        if(getLanguageModifier() == null)
        {
            return null;
        }
        try
        {
            return getLanguage().getIsoCode();
        }
        catch(HeaderValidationException e)
        {
            throw new SystemException(e);
        }
    }


    public boolean isStringAttribute()
    {
        if(isLocalized())
        {
            return String.class.getName()
                            .equalsIgnoreCase(((MapType)
                                            getAttributeDescriptor().getRealAttributeType()).getReturnType().getCode());
        }
        return "java.lang.String".equalsIgnoreCase(getAttributeDescriptor().getRealAttributeType().getCode());
    }


    public boolean isCaseInsensitiveStringAttribute()
    {
        return ("true".equalsIgnoreCase(
                        getDescriptorData().getModifier("ignoreKeyCase")) && isStringAttribute());
    }


    public Language getLanguage() throws HeaderValidationException
    {
        if(this.configuredLanguage == null && isLocalized() && getLanguageIso() != null)
        {
            this.configuredLanguage = findLanguage(getHeader(), getLanguageIso());
        }
        return this.configuredLanguage;
    }


    public static Language findLanguage(HeaderDescriptor header, String value) throws HeaderValidationException
    {
        List<Language> rows;
        if(value == null)
        {
            throw new HeaderValidationException("Given language identifier is empty", 0);
        }
        Language ret = null;
        String searchAttribute = Language.PK;
        try
        {
            Long valuePK = Long.valueOf(Long.parseLong(value));
            rows = FlexibleSearch.getInstance()
                            .search("SELECT {" + Item.PK + "} FROM {" + getTypeManager().getComposedType(Language.class).getCode() + "} WHERE {" + searchAttribute + "} = ?value", Collections.singletonMap("value", valuePK), Collections.singletonList(Language.class), true, true, 0, -1).getResult();
        }
        catch(NumberFormatException e)
        {
            searchAttribute = "isocode";
            rows = FlexibleSearch.getInstance()
                            .search("SELECT {" + Item.PK + "} FROM {" + getTypeManager().getComposedType(Language.class).getCode() + "} WHERE LOWER({" + searchAttribute + "}) = ?value", Collections.singletonMap("value", value.toLowerCase()), Collections.singletonList(Language.class), true, true, 0,
                                            -1).getResult();
        }
        if(rows.isEmpty())
        {
            throw new HeaderValidationException(header, "cannot find language for value '" + value + "'", 9);
        }
        if(rows.size() > 1)
        {
            log.error("found multiple languages for value '" + value + "' : " + rows + " - will be use " + rows.get(0) + " - system may be corrupt");
        }
        ret = rows.get(0);
        return ret;
    }


    private static TypeManager getTypeManager()
    {
        return TypeManager.getInstance();
    }


    public String toString()
    {
        return "StandardColumnDescriptor[" + getValuePosition() + ":" + getQualifier() + "(unique=" + isUnique() + ",virtual=" +
                        isVirtual() + ",lang=" + getLanguageIso() + ",trans=" + (
                        (getValueTranslator() != null) ? getValueTranslator().getClass().getName() : "n/a") + ")]";
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isInitalOnly()
    {
        return isInitialOnly();
    }


    public boolean isInitialOnly()
    {
        return this.initialOnly;
    }


    public boolean isReadOnly()
    {
        return this.readOnly;
    }


    public boolean isReadable()
    {
        return this.readable;
    }


    public boolean isWritable()
    {
        return this.writable;
    }


    public boolean isPartOf()
    {
        return this.partOf;
    }


    public AbstractValueTranslator getValueTranslator()
    {
        AbstractValueTranslator ret = this.valueTranslator;
        Thread caller = Thread.currentThread();
        if(!caller.equals(this.valueTranslatorThread))
        {
            ret = this.threadBoundValueTranslators.get(caller);
            if(ret == null)
            {
                try
                {
                    this.threadBoundValueTranslators.put(caller, ret = createValueTranslator());
                }
                catch(HeaderValidationException e)
                {
                    throw new IllegalStateException("could not create thread bound value translator due to " + e.getMessage());
                }
            }
        }
        return ret;
    }


    public String getQualifier()
    {
        String ret = super.getQualifier();
        int pos = ret.indexOf('.');
        return (pos != -1) ? ret.substring(pos + 1).trim() : ret;
    }


    public String getQualifierForComment()
    {
        if(!isLocalized())
        {
            return getQualifier();
        }
        return getQualifier() + "_" + getQualifier();
    }
}
