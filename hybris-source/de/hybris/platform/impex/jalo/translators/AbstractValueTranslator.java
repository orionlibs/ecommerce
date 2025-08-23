package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.AtomicType;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import java.util.List;
import org.apache.log4j.Logger;

public abstract class AbstractValueTranslator
{
    private static final Logger LOG = Logger.getLogger(AbstractValueTranslator.class.getName());
    private boolean lastWasUnresolved = false;
    private boolean lastWasEmpty = false;
    private StandardColumnDescriptor columnDescriptor = null;
    private FlexibleSearch flexibleSearch;


    public static AbstractValueTranslator createTranslator(ComposedType targetType, List<AbstractDescriptor.ColumnParams>[] patternLists) throws HeaderValidationException
    {
        if(patternLists != null && patternLists.length > 0)
        {
            return (patternLists.length > 1) ? (AbstractValueTranslator)new AlternativeExpressionTranslator(targetType, (List[])patternLists) :
                            (AbstractValueTranslator)new ItemExpressionTranslator(targetType, patternLists[0]);
        }
        return (AbstractValueTranslator)new ItemPKTranslator(targetType);
    }


    public static AbstractValueTranslator createTranslator(AttributeDescriptor attributeDescriptor, AtomicType targetType, List<AbstractDescriptor.ColumnParams>[] patternLists) throws HeaderValidationException
    {
        if(Object.class.equals(targetType.getJavaClass()))
        {
            if(patternLists != null && patternLists.length > 0)
            {
                return (patternLists.length > 1) ? (AbstractValueTranslator)new AlternativeExpressionTranslator(TypeManager.getInstance().getComposedType(Item.class), (List[])patternLists) :
                                (AbstractValueTranslator)new ItemExpressionTranslator(
                                                TypeManager.getInstance().getComposedType(Item.class), patternLists[0]);
            }
            return (AbstractValueTranslator)new AtomicValueTranslator(attributeDescriptor, targetType.getJavaClass());
        }
        return (AbstractValueTranslator)new AtomicValueTranslator(attributeDescriptor, targetType.getJavaClass());
    }


    public static AbstractValueTranslator createTranslator(AttributeDescriptor attributeDescriptor, CollectionType collectionType, List<AbstractDescriptor.ColumnParams>[] patternLists) throws HeaderValidationException
    {
        MapValueTranslator mapValueTranslator;
        Type elementType = collectionType.getElementType(null);
        AbstractValueTranslator elementTranslator = null;
        if(elementType instanceof AtomicType)
        {
            elementTranslator = createTranslator(attributeDescriptor, (AtomicType)elementType, patternLists);
        }
        else if(elementType instanceof ComposedType)
        {
            elementTranslator = createTranslator((ComposedType)elementType, patternLists);
        }
        else if(elementType instanceof CollectionType)
        {
            elementTranslator = createTranslator(attributeDescriptor, (CollectionType)elementType, patternLists);
        }
        else if(elementType instanceof MapType)
        {
            mapValueTranslator = new MapValueTranslator((MapType)elementType, (List[])patternLists);
        }
        else
        {
            throw new HeaderValidationException("illegal collection element type " + elementType.getCode() + " of " + attributeDescriptor
                            .getEnclosingType()
                            .getCode() + "." + attributeDescriptor.getQualifier() + " - must be either atomic, composed or collection type (specify custom translator for other types)", 5);
        }
        return (AbstractValueTranslator)new CollectionValueTranslator(collectionType, (AbstractValueTranslator)mapValueTranslator);
    }


    protected FlexibleSearch getFlexibleSearch()
    {
        if(this.flexibleSearch == null)
        {
            this.flexibleSearch = FlexibleSearch.getInstance();
        }
        return this.flexibleSearch;
    }


    protected void clearStatus()
    {
        this.lastWasUnresolved = false;
        this.lastWasEmpty = false;
    }


    protected void setError()
    {
        this.lastWasUnresolved = true;
    }


    protected void setEmpty()
    {
        this.lastWasEmpty = true;
    }


    public void validate(StandardColumnDescriptor columnDescriptor) throws HeaderValidationException
    {
    }


    public void init(StandardColumnDescriptor descriptor)
    {
        this.columnDescriptor = descriptor;
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected boolean isDebugEnabled()
    {
        return LOG.isDebugEnabled();
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void debug(String msg)
    {
        LOG.debug(msg);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected boolean isInfoEnabled()
    {
        return LOG.isInfoEnabled();
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void info(String msg)
    {
        LOG.info(msg);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void warn(String msg)
    {
        LOG.warn(msg);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void error(String msg)
    {
        LOG.error(msg);
    }


    public StandardColumnDescriptor getColumnDescriptor()
    {
        return this.columnDescriptor;
    }


    public abstract Object importValue(String paramString, Item paramItem) throws JaloInvalidParameterException;


    public abstract String exportValue(Object paramObject) throws JaloInvalidParameterException;


    public boolean wasUnresolved()
    {
        return this.lastWasUnresolved;
    }


    public boolean wasEmpty()
    {
        return this.lastWasEmpty;
    }
}
