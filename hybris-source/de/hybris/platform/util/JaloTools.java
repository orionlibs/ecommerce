package de.hybris.platform.util;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.enumeration.EnumerationType;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.znerd.xmlenc.LineBreak;
import org.znerd.xmlenc.XMLOutputter;

public abstract class JaloTools
{
    public static double getTypedPrimitiveDouble(Object value, double defValue) throws JaloInvalidParameterException
    {
        if(value == null)
        {
            return defValue;
        }
        if(!(value instanceof Double))
        {
            throw new JaloInvalidParameterException("Expected value as java.lang.Double object but it's " + value
                            .getClass() + ".", 0);
        }
        return ((Double)value).doubleValue();
    }


    public static boolean getTypedPrimitiveBoolean(Object value, boolean defValue) throws JaloInvalidParameterException
    {
        if(value == null)
        {
            return defValue;
        }
        if(!(value instanceof Boolean))
        {
            throw new JaloInvalidParameterException("Expected value as java.lang.Boolean object but it's " + value
                            .getClass() + ".", 0);
        }
        return ((Boolean)value).booleanValue();
    }


    public static long getTypedPrimitiveLong(Object value, long defValue) throws JaloInvalidParameterException
    {
        if(value == null)
        {
            return defValue;
        }
        if(!(value instanceof Long))
        {
            throw new JaloInvalidParameterException("Expected value as java.lang.Long object but it's " + value.getClass() + ".", 0);
        }
        return ((Long)value).longValue();
    }


    public static int getTypedPrimitiveInteger(Object value, int defValue) throws JaloInvalidParameterException
    {
        if(value == null)
        {
            return defValue;
        }
        if(!(value instanceof Integer))
        {
            throw new JaloInvalidParameterException("Expected value as java.lang.Integer object but it's " + value
                            .getClass() + ".", 0);
        }
        return ((Integer)value).intValue();
    }


    public static byte getTypedPrimitiveByte(Object value, byte defValue) throws JaloInvalidParameterException
    {
        if(value == null)
        {
            return defValue;
        }
        if(!(value instanceof Byte))
        {
            throw new JaloInvalidParameterException("Expected value as java.lang.Byte object but it's " + value.getClass() + ".", 0);
        }
        return ((Byte)value).byteValue();
    }


    public static char getTypedPrimitiveChar(Object value, char defValue) throws JaloInvalidParameterException
    {
        if(value == null)
        {
            return defValue;
        }
        if(!(value instanceof Character))
        {
            throw new JaloInvalidParameterException("Expected value as java.lang.Character object but it's " + value.getClass() + ".", 0);
        }
        return ((Character)value).charValue();
    }


    public static float getTypedPrimitiveFloat(Object value, float defValue) throws JaloInvalidParameterException
    {
        if(value == null)
        {
            return defValue;
        }
        if(!(value instanceof Float))
        {
            throw new JaloInvalidParameterException("Expected value as java.lang.Float object but it's " + value.getClass() + ".", 0);
        }
        return ((Float)value).floatValue();
    }


    public static short getTypedPrimitiveShort(Object value, short defValue) throws JaloInvalidParameterException
    {
        if(value == null)
        {
            return defValue;
        }
        if(!(value instanceof Short))
        {
            throw new JaloInvalidParameterException("Expected value as java.lang.Short object but it's " + value.getClass() + ".", 0);
        }
        return ((Short)value).shortValue();
    }


    public static PriceValue convertPriceIfNecessary(PriceValue pv, boolean toNet, Currency toCurrency, Collection taxValues)
    {
        boolean doRound = false;
        double convertedPrice = pv.getValue();
        if(pv.isNet() != toNet)
        {
            convertedPrice = pv.getOtherPrice(taxValues).getValue();
            doRound = true;
        }
        String iso = pv.getCurrencyIso();
        if(iso != null && !iso.equals(toCurrency.getIsoCode()))
        {
            try
            {
                Currency basePriceCurrency = JaloSession.getCurrentSession().getC2LManager().getCurrencyByIsoCode(iso);
                convertedPrice = basePriceCurrency.convert(toCurrency, convertedPrice);
                doRound = true;
            }
            catch(JaloItemNotFoundException e)
            {
                log.warn("Cannot convert from currency '" + iso + "' to currency '" + toCurrency.getIsoCode() + "' since '" + iso + "' doesn't exist any more - ignored");
            }
        }
        return new PriceValue(toCurrency.getIsoCode(), doRound ? toCurrency.round(convertedPrice) : convertedPrice, toNet);
    }


    public static void exportSystem(JaloSession session, XMLOutputter xmlDef, String extensionName)
    {
        try
        {
            Collection types = null;
            xmlDef.setEscaping(true);
            xmlDef.setLineBreak(LineBreak.UNIX);
            xmlDef.setIndentation("\t");
            xmlDef.setQuotationMark('"');
            xmlDef.declaration();
            xmlDef.whitespace("");
            xmlDef.comment("========================================================================");
            xmlDef.comment("=== hybris platform extension '" + extensionName + "' items.xml");
            xmlDef.comment("========================================================================");
            xmlDef.whitespace("");
            xmlDef.startTag("items");
            xmlDef.attribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            xmlDef.attribute("xsi:noNamespaceSchemaLocation", "items.xsd");
            types = getAtomicTypes(session, extensionName);
            if(!types.isEmpty())
            {
                xmlDef.startTag("atomictypes");
                for(Iterator<AtomicType> it = types.iterator(); it.hasNext(); )
                {
                    ((AtomicType)it.next()).exportXMLDefinition(xmlDef);
                }
                xmlDef.endTag();
            }
            types = getCollectionTypes(session, extensionName);
            if(!types.isEmpty())
            {
                xmlDef.startTag("collectiontypes");
                for(Iterator<CollectionType> it = types.iterator(); it.hasNext(); )
                {
                    ((CollectionType)it.next()).exportXMLDefinition(xmlDef);
                }
                xmlDef.endTag();
            }
            types = getEnumerationTypes(session, extensionName);
            if(!types.isEmpty())
            {
                xmlDef.startTag("enumtypes");
                for(Iterator<EnumerationType> it = types.iterator(); it.hasNext(); )
                {
                    ((EnumerationType)it.next()).exportXMLDefinition(xmlDef, extensionName);
                }
                xmlDef.endTag();
            }
            types = getMapTypes(session, extensionName);
            if(!types.isEmpty())
            {
                xmlDef.startTag("maptypes");
                for(Iterator<MapType> it = types.iterator(); it.hasNext(); )
                {
                    ((MapType)it.next()).exportXMLDefinition(xmlDef);
                }
                xmlDef.endTag();
            }
            types = getRelationTypes(session, extensionName);
            if(!types.isEmpty())
            {
                xmlDef.startTag("relations");
                for(Iterator<RelationType> it = types.iterator(); it.hasNext(); )
                {
                    ((RelationType)it.next()).exportXMLDefinition(xmlDef);
                }
                xmlDef.endTag();
            }
            types = getItemTypesOrdered(session, extensionName);
            if(!types.isEmpty())
            {
                xmlDef.startTag("itemtypes");
                for(Iterator<ComposedType> it = types.iterator(); it.hasNext(); )
                {
                    ((ComposedType)it.next()).exportXMLDefinition(xmlDef, extensionName);
                }
                xmlDef.endTag();
            }
            xmlDef.endTag();
            xmlDef.endDocument();
        }
        catch(IOException e)
        {
            throw new JaloSystemException(e);
        }
    }


    private static List getAtomicTypes(JaloSession session, String extensionName)
    {
        return session.getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {" + session
                                                        .getTypeManager()
                                                        .getComposedType(AtomicType.class)
                                                        .getCode() + "} WHERE {extensionName}" + (
                                                        (extensionName == null) ? " IS NULL " : ("='" + extensionName + "' ")) + "ORDER BY {" + AtomicType.CODE + "} ASC", Collections.EMPTY_MAP,
                                        Arrays.asList((Class<?>[][])new Class[] {AtomicType.class}, ), true, true, 0, -1)
                        .getResult();
    }


    private static List getCollectionTypes(JaloSession session, String extensionName)
    {
        return session.getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {" + session
                                                        .getTypeManager()
                                                        .getComposedType(CollectionType.class)
                                                        .getCode() + "} WHERE {extensionName}" + (
                                                        (extensionName == null) ? " IS NULL " : ("='" + extensionName + "' ")) + "ORDER BY {" + CollectionType.CODE + "} ASC", Collections.EMPTY_MAP,
                                        Arrays.asList((Class<?>[][])new Class[] {CollectionType.class}, ), true, true, 0, -1)
                        .getResult();
    }


    private static List getMapTypes(JaloSession session, String extensionName)
    {
        return session.getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" + session
                                                        .getTypeManager().getComposedType(MapType.class).getCode() + "} WHERE {extensionName}" + (
                                                        (extensionName == null) ? " IS NULL " : ("='" + extensionName + "' ")) + "ORDER BY {" + MapType.CODE + "} ASC", Collections.EMPTY_MAP,
                                        Arrays.asList((Class<?>[][])new Class[] {MapType.class}, ), true, true, 0, -1)
                        .getResult();
    }


    private static List getEnumerationTypes(JaloSession session, String extensionName)
    {
        return session.getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" +
                                                        TypeManager.getInstance().getComposedType(EnumerationType.class).getCode() + " AS t} WHERE {extensionName}" + (
                                                        (extensionName == null) ? " IS NULL" : ("='" + extensionName + "'")) + " OR EXISTS ({{SELECT {" + Item.PK + "} FROM {" +
                                                        TypeManager.getInstance().getComposedType(EnumerationValue.class).getCode() + " AS ev} WHERE {ev." + Item.TYPE + "}={t." + Item.PK + "} AND {ev.extensionName}" + (
                                                        (extensionName == null) ? " IS NULL " : ("='" + extensionName + "' ")) + "}}) ORDER BY {" + MapType.CODE + "} ASC", Collections.EMPTY_MAP,
                                        Arrays.asList((Class<?>[][])new Class[] {EnumerationType.class}, ), true, true, 0, -1)
                        .getResult();
    }


    private static List getRelationTypes(JaloSession session, String extensionName)
    {
        return session.getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {" + session
                                                        .getTypeManager()
                                                        .getComposedType(RelationType.class)
                                                        .getCode() + "} WHERE {extensionName}" + (
                                                        (extensionName == null) ? " IS NULL " : ("='" + extensionName + "' ")) + "ORDER BY {" + RelationType.CODE + "} ASC", Collections.EMPTY_MAP,
                                        Arrays.asList((Class<?>[][])new Class[] {RelationType.class}, ), true, true, 0, -1)
                        .getResult();
    }


    private static List getItemTypesOrdered(JaloSession session, String extensionName)
    {
        List<ComposedType> ret = new ArrayList();
        ComposedType root = session.getTypeManager().getComposedType(Item.class);
        if(extensionName == null)
        {
            ret.add(root);
        }
        Collection<ComposedType> toProcess = Collections.singleton(root);
        while(toProcess != null && !toProcess.isEmpty())
        {
            Collection<ComposedType> nextToProcess = new ArrayList();
            for(Iterator<ComposedType> it = toProcess.iterator(); it.hasNext(); )
            {
                ComposedType t = it.next();
                Collection subtypes = t.getSubTypes();
                for(Iterator<ComposedType> it2 = subtypes.iterator(); it2.hasNext(); )
                {
                    ComposedType tsub = it2.next();
                    if(tsub instanceof EnumerationType || tsub instanceof RelationType)
                    {
                        continue;
                    }
                    nextToProcess.add(tsub);
                    String subExtensionName = tsub.getExtensionName();
                    if(extensionName == subExtensionName || (extensionName != null && extensionName.equals(subExtensionName)))
                    {
                        ret.add(tsub);
                        continue;
                    }
                    if(extensionName != null)
                    {
                        Iterator<AttributeDescriptor> it3;
                        label36:
                        for(it3 = tsub.getAttributeDescriptors().iterator(); it3.hasNext(); )
                        {
                            AttributeDescriptor a = it3.next();
                            if(extensionName.equals(a.getExtensionName()))
                            {
                                if(a.isInherited())
                                {
                                    if(a.isRedeclared())
                                    {
                                        break label36;
                                    }
                                    continue;
                                }
                                ret.add(tsub);
                            }
                        }
                    }
                }
            }
            toProcess = nextToProcess;
        }
        return ret;
    }


    public static void exportTypeLocalizations(JaloSession jaloSession, Writer wr, String extensionName)
    {
        exportTypeLocalization(getAtomicTypes(jaloSession, extensionName), wr, extensionName, false);
        exportTypeLocalization(getCollectionTypes(jaloSession, extensionName), wr, extensionName, false);
        exportTypeLocalization(getEnumerationTypes(jaloSession, extensionName), wr, extensionName, false);
        exportTypeLocalization(getMapTypes(jaloSession, extensionName), wr, extensionName, false);
        exportTypeLocalization(getRelationTypes(jaloSession, extensionName), wr, extensionName, false);
        exportTypeLocalization(getItemTypesOrdered(jaloSession, extensionName), wr, extensionName, false);
    }


    public static void exportTypeLocalization(Collection types, Writer wr, String extensionName, boolean ignoreExtensionName)
    {
        PrintWriter out = new PrintWriter(wr);
        for(Iterator<Type> it = types.iterator(); it.hasNext(); )
        {
            boolean typeHeaderWritten = false;
            Type type = it.next();
            String typeExtensionName = type.getExtensionName();
            boolean isMyType = (ignoreExtensionName || typeExtensionName == null || extensionName.equals(typeExtensionName));
            if(isMyType)
            {
                writeHeaderType(out, type);
                typeHeaderWritten = true;
                String name = type.getName();
                name = (name == null) ? (name = "") : convertProperty(name);
                String description = type.getDescription();
                description = (description == null) ? (description = "") : convertProperty(description);
                out.println("type." + type.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()) + ".name=" + name);
                out.println("type." + type.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()) + ".description=" + description);
                out.println();
            }
            if(type instanceof ComposedType)
            {
                ComposedType ctype = (ComposedType)type;
                ComposedType superType = ((ComposedType)type).getSuperType();
                for(Iterator<AttributeDescriptor> iterator1 = ctype.getDeclaredAttributeDescriptors().iterator(); iterator1.hasNext(); )
                {
                    AttributeDescriptor ad = iterator1.next();
                    String attributeExtensionName = ad.getExtensionName();
                    if(ignoreExtensionName || (extensionName == null && attributeExtensionName == null) || (extensionName != null && extensionName
                                    .equals(attributeExtensionName)))
                    {
                        if(!typeHeaderWritten)
                        {
                            writeHeaderType(out, type);
                            typeHeaderWritten = true;
                        }
                        exportAttribute(ad, out);
                    }
                }
                for(Iterator<AttributeDescriptor> attribute_it = ctype.getInheritedAttributeDescriptors().iterator(); attribute_it.hasNext(); )
                {
                    AttributeDescriptor ad = attribute_it.next();
                    if(superType != null)
                    {
                        AttributeDescriptor superAd = null;
                        try
                        {
                            superAd = superType.getAttributeDescriptorIncludingPrivate(ad.getQualifier());
                        }
                        catch(JaloItemNotFoundException jaloItemNotFoundException)
                        {
                        }
                        if(superAd != null)
                        {
                            if(superAd.getName() == null ||
                                            !superAd.getName().equals(ad.getName()))
                            {
                                if(!typeHeaderWritten)
                                {
                                    writeHeaderType(out, type);
                                    typeHeaderWritten = true;
                                }
                                exportAttribute(ad, out);
                            }
                        }
                    }
                }
            }
            out.println();
        }
    }


    private static void exportAttribute(AttributeDescriptor ad, PrintWriter out)
    {
        String dname = ad.getName();
        dname = (dname == null) ? (dname = "") : convertProperty(dname);
        out.println("type." + ad.getEnclosingType().getCode().toLowerCase(LocaleHelper.getPersistenceLocale()) + "." + ad.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()) + ".name=" + dname);
        String ddescription = ad.getDescription();
        ddescription = (ddescription == null) ? (ddescription = "") : convertProperty(ddescription);
        out.println("type." + ad.getEnclosingType().getCode().toLowerCase(LocaleHelper.getPersistenceLocale()) + "." + ad.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()) + ".description=" + ddescription);
    }


    private static void writeHeaderType(PrintWriter out, Type type)
    {
        out.println("### Localization for type " + type.getCode());
        out.println();
    }


    private static final List ESCAPE_CHARS = Arrays.asList(new String[] {"\"", "'", "\n", "\r", "\f", "\\", "\t", "\b"});
    private static final List ESCAPE_ALIASES = Arrays.asList(new String[] {"\"", "'", "n", "r", "f", "\\", "t", "b"});
    private static final char[] HEX = new char[] {
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'A', 'B', 'C', 'D', 'E', 'F'};
    private static final String PROPERTY_CONTROL_CHARS = "=:#!";
    private static final Logger log = Logger.getLogger(JaloTools.class);


    public static final String encodeJAVA(String input)
    {
        StringBuilder result = new StringBuilder();
        if(input != null)
        {
            for(int i = 0; i < input.length(); i++)
            {
                char asByte;
                String c = new String(new char[] {asByte = input.charAt(i)});
                int pos = ESCAPE_CHARS.indexOf(c);
                if(pos != -1)
                {
                    result.append("\\").append(ESCAPE_ALIASES.get(pos));
                }
                else if(asByte >= '')
                {
                    byte hi = (byte)((asByte & 0xF0) >> 4);
                    byte lo = (byte)(asByte & 0xF);
                    result.append("\\x").append(HEX[hi]).append(HEX[lo]);
                }
                else
                {
                    result.append(c);
                }
            }
        }
        return result.toString();
    }


    private static String convertProperty(String theString)
    {
        int len = theString.length();
        StringBuilder outBuffer = new StringBuilder(len * 2);
        for(int x = 0; x < len; x++)
        {
            char aChar = theString.charAt(x);
            switch(aChar)
            {
                case '\\':
                    outBuffer.append('\\');
                    outBuffer.append('\\');
                    break;
                case '\t':
                    outBuffer.append('\\');
                    outBuffer.append('t');
                    break;
                case '\n':
                    outBuffer.append('\\');
                    outBuffer.append('n');
                    break;
                case '\r':
                    outBuffer.append('\\');
                    outBuffer.append('r');
                    break;
                case '\f':
                    outBuffer.append('\\');
                    outBuffer.append('f');
                    break;
                default:
                    if(aChar < ' ' || aChar > '~')
                    {
                        outBuffer.append('\\');
                        outBuffer.append('u');
                        outBuffer.append(HEX[aChar >> 12 & 0xF]);
                        outBuffer.append(HEX[aChar >> 8 & 0xF]);
                        outBuffer.append(HEX[aChar >> 4 & 0xF]);
                        outBuffer.append(HEX[aChar & 0xF]);
                        break;
                    }
                    if("=:#!".indexOf(aChar) != -1)
                    {
                        outBuffer.append('\\');
                    }
                    outBuffer.append(aChar);
                    break;
            }
        }
        return outBuffer.toString();
    }
}
