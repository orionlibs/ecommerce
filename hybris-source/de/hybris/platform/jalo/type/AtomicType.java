package de.hybris.platform.jalo.type;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Set;
import org.znerd.xmlenc.LineBreak;
import org.znerd.xmlenc.XMLOutputter;

public class AtomicType extends Type
{
    public static final String NULL_TOKEN = "<NULL>";
    public static final String SUPER_TYPE = "superType";
    public static final String SUBTYPES = "subtypes";
    public static final String JAVA_CLASS = "javaClass";


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        String code = (String)allAttributes.get(CODE);
        Class cl = (Class)allAttributes.get("javaClass");
        AtomicType st = (AtomicType)allAttributes.get("superType");
        if(cl != null)
        {
            return (st == null) ? (Item)JaloSession.getCurrentSession().getTypeManager().createAtomicType(cl) :
                            (Item)JaloSession.getCurrentSession().getTypeManager().createAtomicType(st, cl);
        }
        if(st != null && code != null)
        {
            return (Item)JaloSession.getCurrentSession().getTypeManager().createAtomicType(st, code);
        }
        throw new JaloInvalidParameterException("wrong parameters to create a AtomicType - either javaClass or superType together with " + CODE + " have to be present, ", 0);
    }


    protected Item.ItemAttributeMap getNonInitialAttributes(SessionContext ctx, Item.ItemAttributeMap allAttributes)
    {
        Item.ItemAttributeMap copyMap = super.getNonInitialAttributes(ctx, allAttributes);
        copyMap.remove(CODE);
        copyMap.remove("superType");
        copyMap.remove("javaClass");
        return copyMap;
    }


    public static String escape(String raw, char escapedChar)
    {
        if(raw == null)
        {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        int last = 0;
        int escapePos;
        for(escapePos = raw.indexOf(escapedChar); escapePos >= 0; last = escapePos + 1, escapePos = raw.indexOf(escapedChar, last))
        {
            sb.append(raw.substring(last, escapePos)).append("\\").append(escapedChar);
        }
        if(last < raw.length())
        {
            sb.append(raw.substring(last));
        }
        return sb.toString();
    }


    public static int[] getEscapedStringPositions(String escaped, char escapedChar, int startFrom)
    {
        escaped = escaped.trim();
        int start = escaped.indexOf(escapedChar, startFrom);
        while(start > 0 && escaped.charAt(start - 1) == '\\')
        {
            start = escaped.indexOf(escapedChar, start + 1);
        }
        if(start < 0)
        {
            throw new IllegalArgumentException("cannot find begin of escaped string within '" + escaped + "' starting at " + start);
        }
        int end = escaped.indexOf(escapedChar, start + 1);
        while(end >= 0 && escaped.charAt(end - 1) == '\\')
        {
            end = escaped.indexOf(escapedChar, end + 1);
        }
        if(end < 0)
        {
            throw new IllegalArgumentException("cannot find end of escaped string within '" + escaped + "' starting at " + start + " (found begin at " + start + ")");
        }
        return new int[] {start, end};
    }


    public static String unescape(String escaped, char escapeChar)
    {
        StringBuilder sb = new StringBuilder();
        String escapeSequence = "\\" + escapeChar;
        int last = 0;
        int pos = escaped.indexOf(escapeSequence);
        for(; pos >= 0; last = pos + 2, pos = escaped.indexOf(escapeSequence, last))
        {
            sb.append(escaped.substring(last, pos)).append(escapeChar);
        }
        if(last < escaped.length())
        {
            sb.append(escaped.substring(last));
        }
        return sb.toString();
    }


    public String toString(SessionContext ctx, Object value) throws JaloInvalidParameterException
    {
        String valueStr;
        if(value == null)
        {
            return "<NULL>";
        }
        Class<?> myClass = getJavaClass();
        if(String.class.isAssignableFrom(myClass))
        {
            valueStr = (String)value;
        }
        else if(Number.class.isAssignableFrom(myClass) || Boolean.class.isAssignableFrom(myClass) || Date.class
                        .isAssignableFrom(myClass))
        {
            valueStr = value.toString();
        }
        else if(Class.class.isAssignableFrom(myClass))
        {
            valueStr = ((Class)value).getName();
        }
        else if(Character.class.isAssignableFrom(myClass))
        {
            valueStr = ((Character)value).toString();
        }
        else if(Serializable.class.isAssignableFrom(myClass))
        {
            try
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream os = new ObjectOutputStream(baos);
                os.writeObject(value);
                os.close();
                valueStr = Base64.encodeBytes(baos.toByteArray(), 8);
            }
            catch(IOException e)
            {
                throw new JaloSystemException(e);
            }
        }
        else
        {
            throw new JaloInvalidParameterException("atomic type " + getCode() + " (class=" + myClass + ") does not support toString(Object) yet", 0);
        }
        return "\"" + escape(valueStr, '"') + "\"";
    }


    public Object parseValue(SessionContext ctx, String value) throws JaloInvalidParameterException
    {
        if(value == null || "<NULL>".equals(value))
        {
            return null;
        }
        int[] range = getEscapedStringPositions(value, '"', 0);
        String valueStr = unescape(value.substring(range[0] + 1, range[1]), '"');
        Class<?> myClass = getJavaClass();
        if(String.class.isAssignableFrom(myClass))
        {
            return valueStr;
        }
        if(Number.class.isAssignableFrom(myClass) || Boolean.class.isAssignableFrom(myClass) || Date.class
                        .isAssignableFrom(myClass))
        {
            try
            {
                Constructor<?> cons = myClass.getConstructor(new Class[] {String.class});
                return cons.newInstance(new Object[] {valueStr});
            }
            catch(SecurityException e)
            {
                throw new JaloInvalidParameterException(e, 0);
            }
            catch(NoSuchMethodException e)
            {
                throw new JaloInvalidParameterException(e, 0);
            }
            catch(IllegalArgumentException e)
            {
                throw new JaloInvalidParameterException(e, 0);
            }
            catch(InstantiationException e)
            {
                throw new JaloInvalidParameterException(e, 0);
            }
            catch(IllegalAccessException e)
            {
                throw new JaloInvalidParameterException(e, 0);
            }
            catch(InvocationTargetException e)
            {
                throw new JaloInvalidParameterException(e, 0);
            }
        }
        if(Class.class.isAssignableFrom(myClass))
        {
            try
            {
                return Class.forName(valueStr);
            }
            catch(ClassNotFoundException e)
            {
                throw new JaloInvalidParameterException(e, 0);
            }
        }
        if(Character.class.isAssignableFrom(myClass))
        {
            return Character.valueOf(valueStr.trim().charAt(0));
        }
        if(Serializable.class.isAssignableFrom(myClass))
        {
            try
            {
                ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(valueStr));
                ObjectInputStream is = new ObjectInputStream(bais);
                Object ret = is.readObject();
                is.close();
                return ret;
            }
            catch(IOException e)
            {
                throw new JaloSystemException(e);
            }
            catch(ClassNotFoundException e)
            {
                throw new JaloSystemException(e);
            }
        }
        throw new JaloInvalidParameterException("atomic type " + getCode() + " (class=" + myClass + ") does not support parseValue(String) yet", 0);
    }


    public AtomicType getSuperType()
    {
        return getAtomicTypeImpl().getSuperType();
    }


    public Set getSubTypes()
    {
        return getAtomicTypeImpl().getSubTypes();
    }


    public Class getJavaClass()
    {
        return getAtomicTypeImpl().getJavaClass();
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
            xout.startTag("atomictype");
            xout.attribute("class", getJavaClass().getName());
            AtomicType supertype = getSuperType();
            if(supertype != null)
            {
                xout.attribute("extends", supertype.getCode());
            }
            xout.attribute("autocreate", Boolean.toString(isAutocreate()));
            xout.attribute("generate", Boolean.toString(isGenerate()));
            xout.endTag();
        }
        catch(IOException e)
        {
            throw new JaloSystemException(e);
        }
        return xout.getWriter().toString();
    }


    protected AtomicTypeImpl getAtomicTypeImpl()
    {
        return (AtomicTypeImpl)this.impl;
    }


    public boolean isAssignableFrom(Type type)
    {
        return (type instanceof AtomicType && (equals(type) || getJavaClass().isAssignableFrom(((AtomicType)type)
                        .getJavaClass())));
    }


    public boolean isInstance(Object o)
    {
        return (o != null && o.getClass().equals(getJavaClass()));
    }
}
