package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AlternativeExpressionTranslator extends ItemExpressionTranslator
{
    private final List alternativeTranslators;


    public AlternativeExpressionTranslator(ComposedType targetType, List<AbstractDescriptor.ColumnParams>[] itemPatternLists) throws HeaderValidationException
    {
        super(targetType, itemPatternLists[0]);
        List<ItemExpressionTranslator> list = new ArrayList();
        for(int i = 1; i < itemPatternLists.length; i++)
        {
            list.add(new ItemExpressionTranslator(targetType, itemPatternLists[i]));
        }
        this.alternativeTranslators = Collections.unmodifiableList(list);
    }


    protected Object convertToJalo(String valueExpr, Item forItem)
    {
        Object ret = super.convertToJalo(valueExpr, forItem);
        if(wasUnresolved())
        {
            for(Iterator<ItemExpressionTranslator> iter = this.alternativeTranslators.iterator(); iter.hasNext(); )
            {
                ItemExpressionTranslator trans = iter.next();
                ret = trans.convertToJalo(valueExpr, forItem);
                if(!trans.wasUnresolved())
                {
                    clearStatus();
                    break;
                }
            }
        }
        Object empty = getEmptyValue();
        if(empty == ret && (empty == null || empty.equals(ret)))
        {
            setEmpty();
        }
        return ret;
    }


    protected String convertToString(Object value)
    {
        ItemExpressionTranslator trans = getAssignableTranslator(value);
        if(this == trans)
        {
            return convertToStringImpl(value);
        }
        return trans.convertToString(value);
    }


    protected String convertToStringImpl(Object value)
    {
        return super.convertToString(value);
    }


    protected ItemExpressionTranslator getAssignableTranslator(Object value)
    {
        if(getTargetType().isInstance(value))
        {
            return this;
        }
        Collection<String> types = new LinkedList();
        types.add(getTargetType().getCode());
        for(Iterator<ItemExpressionTranslator> iter = this.alternativeTranslators.iterator(); iter.hasNext(); )
        {
            ItemExpressionTranslator itemExpressionTranslator = iter.next();
            if(itemExpressionTranslator.getTargetType().isInstance(value))
            {
                return itemExpressionTranslator;
            }
            types.add(itemExpressionTranslator.getTargetType().getCode());
        }
        throw new IllegalArgumentException("incompatible value " + value + " for type(s) " + types);
    }
}
