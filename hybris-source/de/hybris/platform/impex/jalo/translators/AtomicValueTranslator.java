package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.core.PK;
import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.util.Base64;
import de.hybris.platform.util.CSVUtils;
import de.hybris.platform.util.DecimalNumberParser;
import de.hybris.platform.util.StandardDateRange;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.migration.MigrationUtilities;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.log4j.Logger;

public class AtomicValueTranslator extends SingleValueTranslator
{
    private static final Logger LOG = Logger.getLogger(AtomicValueTranslator.class);
    private final AttributeDescriptor attributeDescriptor;
    private final Class myClass;
    private DateFormat myDateFormat;
    private String myDateFormatString;
    private NumberFormat myNumberFormat;
    private String myNumberFormatString;
    private Locale myLocale;
    private final DecimalNumberParser decimalNumberParser = new DecimalNumberParser();


    public AtomicValueTranslator(Class javaClass)
    {
        this(null, javaClass);
    }


    public AtomicValueTranslator(AttributeDescriptor attributeDescriptor, Class javaClass)
    {
        this.attributeDescriptor = attributeDescriptor;
        this.myClass = javaClass;
        this.myLocale = JaloSession.getCurrentSession().getSessionContext().getLocale();
    }


    public void setLocale(Locale locale)
    {
        this.myLocale = locale;
    }


    public void setDateFormat(DateFormat format)
    {
        this.myDateFormat = format;
    }


    public void setDateFormat(String format)
    {
        if(Date.class.isAssignableFrom(this.myClass) && format != null)
        {
            try
            {
                setDateFormat(Utilities.getSimpleDateFormat(format, this.myLocale));
                this.myDateFormatString = format;
            }
            catch(IllegalArgumentException e)
            {
                setDateFormat((DateFormat)null);
            }
        }
        else if(StandardDateRange.class.isAssignableFrom(this.myClass) && format != null)
        {
            try
            {
                setDateFormat(getDefaultDateFormat());
                ((SimpleDateFormat)this.myDateFormat).applyPattern(format);
                this.myDateFormatString = format;
            }
            catch(IllegalArgumentException e)
            {
                setDateFormat((DateFormat)null);
            }
        }
        else
        {
            setDateFormat(getDefaultDateFormat());
        }
    }


    public void setNumberFormat(NumberFormat format)
    {
        this.myNumberFormat = format;
    }


    public void setNumberFormat(String format)
    {
        if(Number.class.isAssignableFrom(this.myClass) && format != null)
        {
            try
            {
                setNumberFormat(Utilities.getDecimalFormat(format, this.myLocale));
                this.myNumberFormatString = format;
            }
            catch(IllegalArgumentException e)
            {
                setNumberFormat((NumberFormat)null);
            }
        }
        else
        {
            setNumberFormat(getDefaultNumberFormat());
        }
    }


    protected AttributeDescriptor getAttributeDescriptor()
    {
        return this.attributeDescriptor;
    }


    protected String getDateFormatString(AbstractDescriptor columnDescriptor)
    {
        String format = (columnDescriptor != null) ? columnDescriptor.getDescriptorData().getModifier("dateformat") : null;
        return (format != null && format.length() > 0) ? format : null;
    }


    protected String getNumberFormatString(AbstractDescriptor columnDescriptor)
    {
        String format = (columnDescriptor != null) ? columnDescriptor.getDescriptorData().getModifier("numberformat") : null;
        return (format != null && format.length() > 0) ? format : null;
    }


    protected DateFormat getDateFormat()
    {
        return (this.myDateFormat != null) ? this.myDateFormat : getDefaultDateFormat();
    }


    protected NumberFormat getNumberFormat()
    {
        return (this.myNumberFormat != null) ? this.myNumberFormat : getDefaultNumberFormat();
    }


    public NumberFormat getDefaultNumberFormat()
    {
        if(Integer.class.isAssignableFrom(this.myClass))
        {
            return Utilities.getIntegerInstance(this.myLocale);
        }
        return Utilities.getNumberInstance(this.myLocale);
    }


    public DateFormat getDefaultDateFormat()
    {
        return Utilities.getDateTimeInstance(2, 2, this.myLocale);
    }


    public void init(StandardColumnDescriptor columnDescriptor)
    {
        super.init(columnDescriptor);
        this.myLocale = columnDescriptor.getHeader().getReader().getLocale();
        String format = null;
        format = getDateFormatString((AbstractDescriptor)columnDescriptor);
        setDateFormat(format);
        format = getNumberFormatString((AbstractDescriptor)columnDescriptor);
        setNumberFormat(format);
    }


    public void validate(StandardColumnDescriptor columnDescriptor) throws HeaderValidationException
    {
        super.validate(columnDescriptor);
        if(!isSupportedClass(this.myClass))
        {
            LOG.debug("Current: " + columnDescriptor);
            LOG.debug("Data: " + columnDescriptor.getDescriptorData());
            LOG.debug("Def: " + columnDescriptor.getDefinitionSrc());
            throw new HeaderValidationException(columnDescriptor.getHeader(), "unsupported atomic value translator class " + this.myClass, 5);
        }
        if(Date.class.isAssignableFrom(this.myClass) && getDateFormatString((AbstractDescriptor)columnDescriptor) != null && this.myDateFormat == null)
        {
            throw new HeaderValidationException(columnDescriptor.getHeader(), "invalid date format '" +
                            getDateFormatString(columnDescriptor) + "'", 0);
        }
        if(StandardDateRange.class.isAssignableFrom(this.myClass) && getDateFormatString((AbstractDescriptor)columnDescriptor) != null && this.myDateFormat == null)
        {
            throw new HeaderValidationException(columnDescriptor.getHeader(), "invalid daterange format '" +
                            getDateFormatString(columnDescriptor) + "'", 0);
        }
        if(Number.class.isAssignableFrom(this.myClass) && getNumberFormatString((AbstractDescriptor)columnDescriptor) != null && this.myNumberFormat == null)
        {
            throw new HeaderValidationException(columnDescriptor.getHeader(), "invalid number format '" +
                            getNumberFormatString(columnDescriptor) + "'", 0);
        }
    }


    protected boolean isSupportedClass(Class<?> myClass)
    {
        return (myClass.equals(Object.class) || myClass
                        .equals(Map.class) || PK.class
                        .isAssignableFrom(myClass) || String.class.isAssignableFrom(myClass) || Number.class
                        .isAssignableFrom(myClass) || Boolean.class.isAssignableFrom(myClass) || Date.class
                        .isAssignableFrom(myClass) || Class.class
                        .isAssignableFrom(myClass) || Character.class.isAssignableFrom(myClass) || Serializable.class
                        .isAssignableFrom(myClass));
    }


    protected String convertToString(Object value)
    {
        String valueStr;
        if(String.class.isAssignableFrom(this.myClass))
        {
            valueStr = (String)value;
        }
        else if(PK.class.isAssignableFrom(this.myClass))
        {
            valueStr = ((PK)value).toString();
        }
        else
        {
            if(Date.class.isAssignableFrom(this.myClass))
            {
                try
                {
                    DateFormat dateFormat = getDateFormat();
                    synchronized(dateFormat)
                    {
                        return dateFormat.format(value);
                    }
                }
                catch(IllegalArgumentException e)
                {
                    throw new JaloInvalidParameterException("cannot format date '" + value + "'" + (
                                    (this.myDateFormatString != null) ? (" with format " + this.myDateFormatString) : "") + " due to " + e
                                    .getMessage(), 0);
                }
            }
            if(StandardDateRange.class.isAssignableFrom(this.myClass))
            {
                try
                {
                    List<String> elements = new ArrayList<>();
                    DateFormat dateFormat = getDateFormat();
                    synchronized(dateFormat)
                    {
                        elements.add(dateFormat.format(((StandardDateRange)value).getStart()));
                        elements.add(dateFormat.format(((StandardDateRange)value).getEnd()));
                    }
                    valueStr = CSVUtils.joinAndEscape(elements, null, ',', false);
                }
                catch(Exception e)
                {
                    throw new JaloInvalidParameterException("cannot format daterange '" + value + "' due to " + e.getMessage(), 0);
                }
            }
            else
            {
                if(Boolean.class.isAssignableFrom(this.myClass))
                {
                    return Boolean.TRUE.equals(value) ? "true" : "false";
                }
                if(Number.class.isAssignableFrom(this.myClass))
                {
                    try
                    {
                        return getNumberFormat().format(value);
                    }
                    catch(IllegalArgumentException e)
                    {
                        throw new JaloInvalidParameterException("cannot format number '" + value + "'" + (
                                        (this.myNumberFormatString != null) ? (" with format " + this.myNumberFormatString) : "") + " due to " + e
                                        .getMessage(), 0);
                    }
                }
                if(Class.class.isAssignableFrom(this.myClass))
                {
                    valueStr = ((Class)value).getName();
                }
                else if(Character.class.isAssignableFrom(this.myClass))
                {
                    valueStr = ((Character)value).toString();
                }
                else if(Serializable.class.isAssignableFrom(this.myClass) || Object.class.isAssignableFrom(this.myClass))
                {
                    try
                    {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(baos);
                        objectOutputStream.writeObject(value);
                        objectOutputStream.close();
                        valueStr = Base64.encodeBytes(baos.toByteArray(), 8);
                    }
                    catch(NotSerializableException e)
                    {
                        LOG.error("" + this.myClass + " isn't serializable!: " + this.myClass);
                        valueStr = null;
                    }
                    catch(IOException e)
                    {
                        throw new JaloSystemException(e);
                    }
                }
                else
                {
                    throw new JaloInvalidParameterException("cannot export " + value + " since atomic type " + this.myClass + " is not supported yet", 0);
                }
            }
        }
        return valueStr;
    }


    protected Object convertToJalo(String valueExpr, Item forItem)
    {
        if(String.class.isAssignableFrom(this.myClass))
        {
            return valueExpr;
        }
        if(PK.class.isAssignableFrom(this.myClass))
        {
            if(MigrationUtilities.isOldPK(valueExpr))
            {
                return MigrationUtilities.convertOldPK(valueExpr);
            }
            return PK.parse(valueExpr);
        }
        if(Date.class.isAssignableFrom(this.myClass))
        {
            try
            {
                DateFormat dateFormat = getDateFormat();
                synchronized(dateFormat)
                {
                    return dateFormat.parse(valueExpr);
                }
            }
            catch(ParseException e)
            {
                throw new JaloInvalidParameterException("cannot parse date '" + valueExpr + "' with " + (
                                (this.myDateFormat != null) ? "specified" : "default") + " pattern" + (
                                (getDateFormat() instanceof SimpleDateFormat) ? (" '" + (
                                                (SimpleDateFormat)getDateFormat()).toPattern() + "'") :
                                                " ") + " due to " + e.getMessage(), 0);
            }
        }
        if(StandardDateRange.class.isAssignableFrom(this.myClass))
        {
            List<String> elements = CSVUtils.splitAndUnescape(valueExpr, new char[] {','}, false);
            if(elements.size() == 2)
            {
                try
                {
                    Date startdate, enddate;
                    String start = elements.get(0);
                    String end = elements.get(1);
                    if(start != null)
                    {
                        start = start.trim();
                    }
                    if(end != null)
                    {
                        end = end.trim();
                    }
                    if(start != null && start.length() > 0)
                    {
                        DateFormat dateFormat = getDateFormat();
                        synchronized(dateFormat)
                        {
                            startdate = dateFormat.parse(start);
                        }
                    }
                    else
                    {
                        startdate = new Date(0L);
                    }
                    if(end != null && end.length() > 0)
                    {
                        DateFormat dateFormat = getDateFormat();
                        synchronized(dateFormat)
                        {
                            enddate = dateFormat.parse(end);
                        }
                    }
                    else
                    {
                        enddate = new Date(System.currentTimeMillis() + 3179520000000L);
                    }
                    Calendar calendar = Utilities.getDefaultCalendar();
                    calendar.setTime(enddate);
                    if(startdate.equals(enddate) && calendar.get(11) == 0 && calendar.get(12) == 0 && calendar
                                    .get(13) == 0)
                    {
                        calendar.set(11, 23);
                        calendar.set(12, 59);
                        calendar.set(13, 59);
                        enddate = calendar.getTime();
                    }
                    if(startdate.equals(enddate))
                    {
                        throw new JaloInvalidParameterException("startdate and enddate are equal '" + valueExpr + "' Aborting!", 0);
                    }
                    return new StandardDateRange(startdate, enddate);
                }
                catch(ParseException e)
                {
                    Date startdate;
                    throw new JaloInvalidParameterException("cannot parse daterange '" + valueExpr + "' due to " + startdate.getMessage(), 0);
                }
            }
            throw new JaloInvalidParameterException("cannot parse daterange '" + valueExpr + "' because did not found daterange separator char ','", 0);
        }
        if(Boolean.class.isAssignableFrom(this.myClass))
        {
            if("true".equalsIgnoreCase(valueExpr) || "wahr".equalsIgnoreCase(valueExpr) || "ja".equalsIgnoreCase(valueExpr) || "1"
                            .equals(valueExpr) || "y".equalsIgnoreCase(valueExpr) || "+".equals(valueExpr))
            {
                return Boolean.TRUE;
            }
            if("false".equalsIgnoreCase(valueExpr) || "falsch".equalsIgnoreCase(valueExpr) || "nein"
                            .equalsIgnoreCase(valueExpr) || "0".equals(valueExpr) || "n".equalsIgnoreCase(valueExpr) || "-"
                            .equals(valueExpr))
            {
                return Boolean.FALSE;
            }
            throw new JaloInvalidParameterException("cannot convert '" + valueExpr + "' into boolean value", 0);
        }
        if(Number.class.isAssignableFrom(this.myClass))
        {
            try
            {
                NumberFormat numberFormat = getNumberFormat();
                Number number = null;
                synchronized(numberFormat)
                {
                    number = numberFormat.parse(valueExpr);
                }
                if(!this.myClass.equals(number.getClass()))
                {
                    boolean isConvertingToDecimalType = false;
                    if(Integer.class.equals(this.myClass))
                    {
                        number = Integer.valueOf(number.intValue());
                    }
                    else if(Double.class.equals(this.myClass))
                    {
                        isConvertingToDecimalType = true;
                        number = Double.valueOf(number.doubleValue());
                    }
                    else if(Byte.class.equals(this.myClass))
                    {
                        number = Byte.valueOf(number.byteValue());
                    }
                    else if(Float.class.equals(this.myClass))
                    {
                        isConvertingToDecimalType = true;
                        number = Float.valueOf(number.floatValue());
                    }
                    else if(Long.class.equals(this.myClass))
                    {
                        number = Long.valueOf(number.longValue());
                    }
                    else if(Short.class.equals(this.myClass))
                    {
                        number = Short.valueOf(number.shortValue());
                    }
                    else if(BigDecimal.class.equals(this.myClass))
                    {
                        isConvertingToDecimalType = true;
                        synchronized(numberFormat)
                        {
                            DecimalFormat format = (DecimalFormat)numberFormat;
                            format.setParseBigDecimal(true);
                            number = format.parse(valueExpr, new ParsePosition(0));
                        }
                    }
                    else
                    {
                        LOG.warn("Unsupported number type " + this.myClass.getName() + " for number " + valueExpr + ", will use " + number
                                        .getClass().getName());
                    }
                    if(!isConvertingToDecimalType && this.decimalNumberParser.isValidNumber(valueExpr))
                    {
                        logDetectedOverflowOrTruncation(valueExpr, number);
                    }
                }
                return number;
            }
            catch(ParseException e)
            {
                throw new JaloInvalidParameterException("cannot parse number '" + valueExpr + "' with format " + (
                                (this.myNumberFormat != null) ? "specified" : "default") + " pattern" + (
                                (getNumberFormat() instanceof DecimalFormat) ? (" '" + (
                                                (DecimalFormat)getNumberFormat()).toPattern() + "'") :
                                                " ") + " due to " + e.getMessage(), 0);
            }
            catch(NumberFormatException e)
            {
                throw new JaloInvalidParameterException("cannot parse number '" + valueExpr + "' with format " + (
                                (this.myNumberFormat != null) ? "specified" : "default") + " pattern" + (
                                (getNumberFormat() instanceof DecimalFormat) ? (" '" + (
                                                (DecimalFormat)getNumberFormat()).toPattern() + "'") :
                                                " ") + " due to " + e.getMessage(), 0);
            }
        }
        if(Class.class.isAssignableFrom(this.myClass))
        {
            try
            {
                return Class.forName(valueExpr);
            }
            catch(ClassNotFoundException e)
            {
                throw new JaloInvalidParameterException(e, 0);
            }
        }
        if(Character.class.isAssignableFrom(this.myClass))
        {
            return Character.valueOf(valueExpr.trim().charAt(0));
        }
        if(Serializable.class.isAssignableFrom(this.myClass) || Object.class.isAssignableFrom(this.myClass))
        {
            try
            {
                ByteArrayInputStream bais = new ByteArrayInputStream(Base64.decode(valueExpr));
                ObjectInputStream objectInputStream = new ObjectInputStream(bais);
                Object ret = objectInputStream.readObject();
                objectInputStream.close();
                if(holdsUnresolvableItems(ret))
                {
                    setError();
                    return null;
                }
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
        throw new JaloInvalidParameterException("cannot translate " + valueExpr + " since atomic type " + this.myClass + " is not supported yet", 0);
    }


    protected boolean holdsUnresolvableItems(Object object)
    {
        if(!(object instanceof java.util.Collection))
        {
            if(!(object instanceof Map))
            {
                if(object instanceof Item)
                {
                    return ((Item)object).isAlive();
                }
            }
        }
        return false;
    }


    private void logDetectedOverflowOrTruncation(String valueExpr, Number number)
    {
        String strippedValueExpr = this.decimalNumberParser.stripDecimalPart(valueExpr);
        if(!number.toString().equals(strippedValueExpr))
        {
            LOG.warn("Overflow during translation:\t provided input number: " + valueExpr + "\t was converted to: " + number);
        }
        else if(!strippedValueExpr.equals(valueExpr) && !this.decimalNumberParser.hasOnlyZeroesAsDecimalPart(valueExpr))
        {
            LOG.warn("Truncation during translation:\t provided input number: " + valueExpr + "\t was converted to: " + number);
        }
    }
}
