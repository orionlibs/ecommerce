package de.hybris.platform.jalo.enumeration;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.znerd.xmlenc.LineBreak;
import org.znerd.xmlenc.XMLOutputter;

@Deprecated(since = "ages", forRemoval = false)
public class EnumerationType extends ComposedType
{
    private static final Logger log = Logger.getLogger(EnumerationType.class);
    public static final String COMPARATION_ATTRIBUTE = "comparationAttribute";
    public static final String IS_SORTED = "isSorted";
    public static final String IS_RESORTABLE = "isResortable";
    public static final String VALUES = "values";
    public static final String VALUE_TYPE = "valueType";


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!checkMandatoryAttribute(ComposedType.CODE, allAttributes, missing))
        {
            throw new JaloInvalidParameterException("missing parameters " + missing + ", got " + allAttributes, 0);
        }
        if(checkMandatoryAttribute(ComposedType.SUPERTYPE, allAttributes, new HashSet()))
        {
            throw new JaloInvalidParameterException("cannot define " + ComposedType.SUPERTYPE + " for enumeration types", 0);
        }
        ComposedType valueType = (ComposedType)allAttributes.get("valueType");
        EnumerationType newOne = (valueType != null)
                        ? JaloSession.getCurrentSession().getEnumerationManager().createEnumerationType((PK)allAttributes.get(Item.PK), (String)allAttributes.get(ComposedType.CODE), valueType)
                        : JaloSession.getCurrentSession().getEnumerationManager().createDefaultEnumerationType((String)allAttributes.get(ComposedType.CODE));
        return (Item)newOne;
    }


    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap copyMap = super.getNonInitialAttributes(ctx, allAttributes);
        copyMap.remove(PK);
        copyMap.remove(CODE);
        copyMap.remove("valueType");
        return copyMap;
    }


    protected Item changeTypeIfNecessary(Item newOne, ComposedType requestedType) throws JaloInvalidParameterException
    {
        if(!requestedType.equals(newOne.getComposedType()))
        {
            log.warn("Creating enumeration types with meta types other than EnumerationMetaType (ignored)");
        }
        return newOne;
    }


    public String toString(SessionContext ctx, Object value) throws JaloInvalidParameterException
    {
        if(value != null && EnumerationValue.class.isAssignableFrom(getJaloClass()))
        {
            EnumerationValue ev = (EnumerationValue)value;
            return "\"" + AtomicType.escape(getCode() + "." + getCode(), '"') + "\"";
        }
        return super.toString(ctx, value);
    }


    public Object parseValue(SessionContext ctx, String value) throws JaloInvalidParameterException
    {
        if(value != null && !"<NULL>".equals(value) && EnumerationValue.class.isAssignableFrom(getJaloClass()))
        {
            int[] range = AtomicType.getEscapedStringPositions(value, '"', 0);
            String valueStr = AtomicType.unescape(value.substring(range[0] + 1, range[1]), '"');
            int pos = valueStr.indexOf('.');
            if(pos < 0)
            {
                throw new JaloInvalidParameterException("wrong enumeration value string '" + valueStr + "', expected <Code of type>'.'<Code of enum>", 0);
            }
            try
            {
                return EnumerationManager.getInstance()
                                .getEnumerationValue(valueStr.substring(0, pos), valueStr.substring(pos + 1));
            }
            catch(JaloItemNotFoundException e)
            {
                throw new JaloInvalidParameterException(e, 0);
            }
        }
        return super.parseValue(ctx, value);
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
        return exportXMLDefinition(xmlOut, getExtensionName());
    }


    public String exportXMLDefinition(XMLOutputter xout, String forExtension)
    {
        try
        {
            String ownExtension = getExtensionName();
            boolean asTypeExtension = (ownExtension != forExtension && (ownExtension == null || !ownExtension.equals(forExtension)));
            xout.startTag("enumtype");
            xout.attribute("code", getCode());
            xout.attribute("autocreate", !asTypeExtension ? String.valueOf(isAutocreate()) : "false");
            xout.attribute("generate", !asTypeExtension ? String.valueOf(isGenerate()) : "false");
            xout.attribute("jaloclass", getJaloClass().getName());
            try
            {
                if(isDefaultEnum())
                {
                    for(EnumerationValue ev : getValues())
                    {
                        String evExtension = ev.getExtensionName();
                        if(forExtension == evExtension || (forExtension != null && forExtension.equals(evExtension)))
                        {
                            ev.exportXMLDefinition(xout);
                        }
                    }
                }
                else
                {
                    xout.comment("values are " + getJaloClass().getName() + " instances and cannot be declared inside items.xml");
                }
            }
            catch(JaloInvalidParameterException e)
            {
                throw new JaloSystemException(e);
            }
            xout.endTag();
        }
        catch(IOException e)
        {
            throw new JaloSystemException(e);
        }
        return xout.getWriter().toString();
    }


    public ComposedType getValueType()
    {
        return getSuperType();
    }


    public boolean isSorted()
    {
        return (getComparationAttribute() != null);
    }


    public boolean isResortable()
    {
        AttributeDescriptor comp = getComparationAttribute();
        Type type = (comp != null) ? comp.getRealAttributeType() : null;
        return (comp != null && type instanceof AtomicType && Integer.class.isAssignableFrom(((AtomicType)type).getJavaClass()) && comp
                        .isWritable());
    }


    public List<EnumerationValue> getValues()
    {
        return getValues(true);
    }


    protected List<EnumerationValue> getValues(boolean ordered)
    {
        RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
        try
        {
            StringBuilder q = new StringBuilder();
            q.append("SELECT {").append(PK).append("} FROM {").append(getCode()).append("}");
            if(ordered)
            {
                q.append(" ORDER BY ");
                AttributeDescriptor comp = getComparationAttribute();
                q.append("{").append((comp != null) ? comp.getQualifier() : CREATION_TIME).append("} ASC ");
            }
            List<EnumerationValue> list = FlexibleSearch.getInstance().search(q.toString(), Collections.EMPTY_MAP, Collections.singletonList(getJaloClass()), false, true, 0, -1).getResult();
            if(theUpdate != null)
            {
                theUpdate.close();
            }
            return list;
        }
        catch(Throwable throwable)
        {
            if(theUpdate != null)
            {
                try
                {
                    theUpdate.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    protected void setValues(List<?> values) throws JaloInvalidParameterException
    {
        RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
        try
        {
            Set<EnumerationValue> toRemove = new HashSet<>(getValues(false));
            if(values != null)
            {
                toRemove.removeAll(values);
            }
            try
            {
                removeItemCollection(toRemove);
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloInternalException(e);
            }
            if(values != null && !values.isEmpty())
            {
                if(isResortable())
                {
                    sortValues(values);
                }
                else
                {
                    checkValues(values);
                }
            }
            if(theUpdate != null)
            {
                theUpdate.close();
            }
        }
        catch(Throwable throwable)
        {
            if(theUpdate != null)
            {
                try
                {
                    theUpdate.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    protected void checkValues(List<Item> values) throws JaloInvalidParameterException
    {
        Set wrong = null;
        for(int i = 0, s = values.size(); i < s; i++)
        {
            if(!equals(((Item)values.get(i)).getComposedType()))
            {
                if(wrong == null)
                {
                    wrong = new HashSet();
                }
                wrong.add(values.get(i));
            }
        }
        if(wrong != null)
        {
            throw new JaloInvalidParameterException("values " + wrong + " of " + values + " doesnt belong to enum type " + this, 0);
        }
    }


    public void sortValues(List<?> values) throws JaloInvalidParameterException
    {
        if(!isResortable())
        {
            throw new JaloInvalidParameterException("enum type " + this + " is not resortable because of comparation attribute " +
                            getComparationAttribute(), 0);
        }
        Set<EnumerationValue> all = new HashSet<>(getValues());
        Set<?> toSort = new HashSet(values);
        if(!all.containsAll(toSort))
        {
            throw new JaloInvalidParameterException("value list doesnt contain all value or has too much values ( all values = " + all + ", to sort = " + toSort + " )", 0);
        }
        checkValues(values);
        boolean isDefault = isDefaultEnum();
        AttributeDescriptor comp = isDefault ? null : getComparationAttribute();
        String quali = (comp != null) ? comp.getQualifier() : null;
        for(int i = 0, s = values.size(); i < s; i++)
        {
            Item item = (Item)values.get(i);
            if(isDefault)
            {
                ((EnumerationValue)item).setSequenceNumber(i);
            }
            else
            {
                try
                {
                    item.setAttribute(quali, Integer.valueOf(i));
                }
                catch(JaloSecurityException e)
                {
                    throw new JaloInvalidParameterException(e, e.getErrorCode());
                }
                catch(JaloBusinessException e)
                {
                    throw new JaloInvalidParameterException(e, e.getErrorCode());
                }
            }
        }
    }


    public boolean isDefaultEnum()
    {
        return EnumerationValue.class.isAssignableFrom(getJaloClass());
    }


    public void setComparationAttribute(AttributeDescriptor descr) throws JaloInvalidParameterException
    {
        boolean isDefault = isDefaultEnum();
        AttributeDescriptor comp = descr;
        if(isDefault)
        {
            if(isDefault && comp != null)
            {
                throw new JaloInvalidParameterException("default enumeration type " + this + " cannot have a custom comparation attribute descriptor", 0);
            }
        }
        else
        {
            if(comp != null)
            {
                if(!equals(comp.getEnclosingType()))
                {
                    try
                    {
                        comp = getAttributeDescriptorIncludingPrivate(comp.getQualifier());
                    }
                    catch(JaloItemNotFoundException e)
                    {
                        throw new JaloInvalidParameterException("attribute " + descr + " does not belong to enumeration type " + this + " - so it cannot become comparation attribute", 0);
                    }
                }
                if(!comp.isSearchable())
                {
                    throw new JaloInvalidParameterException("attribute " + descr + " is not searchable so it cannot become comparation attribute", 0);
                }
            }
            setProperty("comparationAttribute", comp);
        }
    }


    public AttributeDescriptor getComparationAttribute()
    {
        try
        {
            return isDefaultEnum() ? getAttributeDescriptorIncludingPrivate("sequenceNumber") :
                            (AttributeDescriptor)getProperty("comparationAttribute");
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloInternalException(e);
        }
    }


    public void setExtensionName(String extName)
    {
        super.setExtensionName(extName);
        for(EnumerationValue ev : getValues())
        {
            ev.setExtensionName(extName);
        }
    }
}
