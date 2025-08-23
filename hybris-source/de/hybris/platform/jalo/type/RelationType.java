package de.hybris.platform.jalo.type;

import de.hybris.platform.core.Constants;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.persistence.type.ComposedTypeEJBImpl;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.persistence.type.TypeTools;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.znerd.xmlenc.LineBreak;
import org.znerd.xmlenc.XMLOutputter;

public class RelationType extends ComposedType
{
    public static final String SOURCE_ATTRIBUTE = "sourceAttribute";
    public static final String TARGET_ATTRIBUTE = "targetAttribute";
    public static final String ORDERING_ATTRIBUTE = "orderingAttribute";
    public static final String LOCALIZATION_ATTRIBUTE = "localizationAttribute";
    public static final String SOURCE_TYPE = "sourceType";
    public static final String TARGET_TYPE = "targetType";
    public static final String SOURCE_TYPE_ROLE = "sourceTypeRole";
    public static final String TARGET_TYPE_ROLE = "targetTypeRole";
    public static final String LOCALIZED = "localized";
    private static final int DEFAULT_RELATION_MODIFIERS = 27;


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute(CODE, allAttributes, missing) ? 1 : 0) | (!checkMandatoryAttribute("localized", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing parameters " + missing + ", got " + allAttributes, 0);
        }
        if(!TypeTools.isValidCode((String)allAttributes.get(CODE)))
        {
            throw new JaloInvalidParameterException("type code for relation type '" + allAttributes.get(CODE) + "' is not valid", 0);
        }
        if(allAttributes.containsKey(SUPERTYPE))
        {
            throw new JaloInvalidParameterException("cannot define " + ComposedType.CODE + " for relation types", 0);
        }
        AttributeDescriptor sAttr = (AttributeDescriptor)allAttributes.get("sourceAttribute");
        ComposedType sType = (ComposedType)allAttributes.get("sourceType");
        String sRole = (String)allAttributes.get("sourceTypeRole");
        AttributeDescriptor tAttr = (AttributeDescriptor)allAttributes.get("targetAttribute");
        ComposedType tType = (ComposedType)allAttributes.get("targetType");
        String tRole = (String)allAttributes.get("targetTypeRole");
        boolean localized = Boolean.TRUE.equals(allAttributes.get("localized"));
        String code = (String)allAttributes.get(CODE);
        TypeManager tm = JaloSession.getCurrentSession().getTypeManager();
        try
        {
            RelationDescriptor relationDescriptor1, relationDescriptor2;
            if(sAttr == null)
            {
                if(tRole == null)
                {
                    throw new JaloInvalidParameterException("either sourceAttribute or targetTypeRole is required to create a relation type", 0);
                }
                if(tAttr == null && sType == null)
                {
                    throw new JaloInvalidParameterException("either targetAttribute or sourceType is required to create a relation type", 0);
                }
                if(tAttr == null && tType == null)
                {
                    throw new JaloInvalidParameterException("either targetAttribute or targetType is required to create a relation type", 0);
                }
                if(sType != null)
                {
                    relationDescriptor1 = tm.createRelationDescriptor(code, sType, tRole, (tType != null) ? tType : tAttr.getEnclosingType(), 27, localized);
                }
                else
                {
                    try
                    {
                        relationDescriptor1 = tm.createRelationDescriptor(code, (ComposedType)((CollectionType)tAttr.getRealAttributeType())
                                        .getElementType(), tRole, (tType != null) ? tType : tAttr.getEnclosingType(), 27, localized);
                    }
                    catch(ClassCastException e)
                    {
                        throw new JaloInvalidParameterException("cannot get source type from target attribute " + tAttr, 0);
                    }
                }
            }
            if(tAttr == null)
            {
                if(sRole == null)
                {
                    throw new JaloInvalidParameterException("either targetAttribute or sourceTypeRole is required to create a relation type", 0);
                }
                if(relationDescriptor1 == null && tType == null)
                {
                    throw new JaloInvalidParameterException("either sourceAttribute or targetType is required to create a relation type", 0);
                }
                if(relationDescriptor1 == null && sType == null)
                {
                    throw new JaloInvalidParameterException("either sourceAttribute or sourceType is required to create a relation type", 0);
                }
                if(tType != null)
                {
                    relationDescriptor2 = tm.createRelationDescriptor(code, tType, sRole, (sType != null) ? sType : relationDescriptor1.getEnclosingType(), 27, localized);
                }
                else
                {
                    try
                    {
                        relationDescriptor2 = tm.createRelationDescriptor(code, (ComposedType)((CollectionType)relationDescriptor1.getRealAttributeType())
                                        .getElementType(), sRole, (sType != null) ? sType : relationDescriptor1.getEnclosingType(), 27, localized);
                    }
                    catch(ClassCastException e)
                    {
                        throw new JaloInvalidParameterException("cannot get target type from source attribute null", 0);
                    }
                }
            }
            RelationType rType = tm.createRelationType(code, localized, (AttributeDescriptor)relationDescriptor1, (AttributeDescriptor)relationDescriptor2);
            String extensionName = (String)allAttributes.get("extensionName");
            rType.getSourceAttributeDescriptor().setExtensionName(extensionName);
            rType.getTargetAttributeDescriptor().setExtensionName(extensionName);
            return (Item)rType;
        }
        catch(JaloBusinessException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            throw new JaloSystemException(e);
        }
    }


    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap copyMap = super.getNonInitialAttributes(ctx, allAttributes);
        copyMap.remove(CODE);
        copyMap.remove("localized");
        copyMap.remove("sourceAttribute");
        copyMap.remove("sourceType");
        copyMap.remove("sourceTypeRole");
        copyMap.remove("targetAttribute");
        copyMap.remove("targetType");
        copyMap.remove("targetTypeRole");
        return copyMap;
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
            xout.startTag("relation");
            xout.attribute("code", getCode());
            xout.attribute("autocreate", String.valueOf(isAutocreate()));
            xout.attribute("generate", String.valueOf(isGenerate()));
            xout.attribute("localized", isLocalized() ? Boolean.TRUE.toString() : Boolean.FALSE.toString());
            ItemDeployment depl = ((ComposedTypeRemote)((ComposedTypeEJBImpl)getImplementation()).getRemote()).getDeployment();
            if(isRootRelationType() && !depl.isGeneric())
            {
                xout.attribute("deployment", (getJNDIName() != null) ? getJNDIName() : "");
            }
            xout.startTag("sourceElement");
            if(getSourceTypeRole() != null)
            {
                xout.attribute("qualifier", getSourceTypeRole());
            }
            xout.attribute("type", getSourceType().getCode());
            xout.attribute("cardinality", getSourceTypeCardinality().getCode());
            if(!isSourceNavigable())
            {
                xout.attribute("navigable", Boolean.FALSE.toString());
            }
            if(!Constants.TYPES.RelationDescriptor.equals(getTargetType().getCode()))
            {
                xout.attribute("metatype", getTargetType().getCode());
            }
            appendModfierXMLDefinition(getTargetAttributeDescriptor(), xout);
            if(depl.isGeneric())
            {
                xout.startTag("deployment");
                xout.attribute("table", depl.getDatabaseTableName());
                xout.attribute("typecode", Integer.toString(depl.getTypeCode()));
                xout.endTag();
            }
            if(getTargetAttributeDescriptor() != null)
            {
                Map custProps = getTargetAttributeDescriptor().getXMLCustomProperties();
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
            }
            xout.endTag();
            xout.startTag("targetElement");
            if(getTargetTypeRole() != null)
            {
                xout.attribute("qualifier", getTargetTypeRole());
            }
            xout.attribute("type", getTargetType().getCode());
            xout.attribute("cardinality", getTargetTypeCardinality().getCode());
            if(!isTargetNavigable())
            {
                xout.attribute("navigable", Boolean.FALSE.toString());
            }
            if(!Constants.TYPES.RelationDescriptor.equals(getSourceType().getCode()))
            {
                xout.attribute("metatype", getSourceType().getCode());
            }
            appendModfierXMLDefinition(getSourceAttributeDescriptor(), xout);
            if(getSourceAttributeDescriptor() != null)
            {
                Map custProps = getSourceAttributeDescriptor().getXMLCustomProperties();
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
            }
            xout.endTag();
            xout.endTag();
        }
        catch(IOException e)
        {
            throw new JaloSystemException(e);
        }
        return xout.getWriter().toString();
    }


    protected void appendModfierXMLDefinition(RelationDescriptor des, StringBuilder sb)
    {
        sb.append("    <modifiers");
        if(des != null)
        {
            sb.append(" read=\"").append(des.isReadable()).append("\"");
            sb.append(" write=\"").append(des.isWritable()).append("\"");
            sb.append(" search=\"").append(des.isSearchable()).append("\"");
            sb.append(" optional=\"").append(des.isOptional()).append("\"");
            sb.append(" removable=\"").append(des.isRemovable()).append("\"");
            sb.append(" initial=\"").append(des.isInitial()).append("\"");
            sb.append(" unique=\"").append(des.isUnique()).append("\"");
            sb.append(" private=\"").append(des.isPrivate()).append("\"");
            sb.append(" partof=\"").append(des.isPartOf()).append("\"/>\n");
        }
    }


    protected void appendModfierXMLDefinition(RelationDescriptor des, XMLOutputter xout) throws IOException
    {
        xout.startTag("modifiers");
        if(des != null)
        {
            xout.attribute("read", String.valueOf(des.isReadable()));
            xout.attribute("write", String.valueOf(des.isWritable()));
            xout.attribute("search", String.valueOf(des.isSearchable()));
            xout.attribute("optional", String.valueOf(des.isOptional()));
            xout.attribute("removable", String.valueOf(des.isRemovable()));
            xout.attribute("initial", String.valueOf(des.isInitial()));
            xout.attribute("unique", String.valueOf(des.isUnique()));
            xout.attribute("private", String.valueOf(des.isPrivate()));
            xout.attribute("partof", String.valueOf(des.isPartOf()));
        }
        xout.endTag();
    }


    public boolean isRootRelationType()
    {
        return !getJNDIName().equals(getSuperType().getJNDIName());
    }


    public boolean isLocalized()
    {
        Boolean b = (Boolean)getProperty("localized");
        return (b != null && b.booleanValue());
    }


    public RelationDescriptor getSourceAttributeDescriptor()
    {
        try
        {
            return (RelationDescriptor)getProperty("sourceAttribute");
        }
        catch(ClassCastException e)
        {
            System.err.println("error getting source ad " + getCode() + " -> " + getProperty("sourceAttribute") + " : " + e
                            .getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    public RelationDescriptor getTargetAttributeDescriptor()
    {
        try
        {
            return (RelationDescriptor)getProperty("targetAttribute");
        }
        catch(ClassCastException e)
        {
            System.err.println("error getting target ad " + getCode() + " -> " + getProperty("sourceAttribute") + " : " + e
                            .getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    public boolean isOrdered()
    {
        return (!isOneToMany() || getOrderingAttribute() != null);
    }


    public AttributeDescriptor getOrderingAttribute()
    {
        return (AttributeDescriptor)getProperty("orderingAttribute");
    }


    public AttributeDescriptor getLocalizationAttribute()
    {
        return (AttributeDescriptor)getProperty("localizationAttribute");
    }


    public String getSourceTypeRole()
    {
        RelationDescriptor desc = getTargetAttributeDescriptor();
        return (desc == null) ? null : desc.getQualifier();
    }


    public String getTargetTypeRole()
    {
        RelationDescriptor desc = getSourceAttributeDescriptor();
        return (desc == null) ? null : desc.getQualifier();
    }


    public ComposedType getSourceType()
    {
        ComposedType result = (ComposedType)getProperty("sourceType");
        if(result == null)
        {
            RelationDescriptor desc = getSourceAttributeDescriptor();
            if(desc != null)
            {
                result = desc.getEnclosingType();
            }
        }
        return result;
    }


    public ComposedType getTargetType()
    {
        ComposedType result = (ComposedType)getProperty("targetType");
        if(result == null)
        {
            RelationDescriptor desc = getTargetAttributeDescriptor();
            if(desc != null)
            {
                result = desc.getEnclosingType();
            }
        }
        return result;
    }


    public boolean isOneToMany()
    {
        return (isAbstract() && Item.class.equals(getJaloClass()));
    }


    public boolean isSourceTypeOne()
    {
        RelationDescriptor desc = getTargetAttributeDescriptor();
        return (desc != null && (desc.isProperty() || desc.getPersistenceQualifier() != null));
    }


    public EnumerationValue getSourceTypeCardinality()
    {
        if(isSourceTypeOne())
        {
            return EnumerationManager.getInstance().getEnumerationValue(Constants.ENUMS.CARDINALITYENUM, "one");
        }
        return EnumerationManager.getInstance().getEnumerationValue(Constants.ENUMS.CARDINALITYENUM, "many");
    }


    public boolean isTargetTypeOne()
    {
        RelationDescriptor desc = getSourceAttributeDescriptor();
        return (desc != null && (desc.isProperty() || desc.getPersistenceQualifier() != null));
    }


    public EnumerationValue getTargetTypeCardinality()
    {
        if(isTargetTypeOne())
        {
            return EnumerationManager.getInstance().getEnumerationValue(Constants.ENUMS.CARDINALITYENUM, "one");
        }
        return EnumerationManager.getInstance().getEnumerationValue(Constants.ENUMS.CARDINALITYENUM, "many");
    }


    public boolean isSourceNavigable()
    {
        return (getSourceAttributeDescriptor() != null);
    }


    public boolean isTargetNavigable()
    {
        return (getTargetAttributeDescriptor() != null);
    }
}
