package de.hybris.platform.persistence.polyglot.search.criteria;

import de.hybris.platform.persistence.polyglot.model.Key;
import de.hybris.platform.persistence.polyglot.model.Reference;
import de.hybris.platform.persistence.polyglot.model.SerializableValue;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;
import de.hybris.platform.persistence.polyglot.view.ItemStateView;
import java.util.Comparator;
import org.apache.commons.lang3.ObjectUtils;

public class ItemStateComparatorCreator
{
    public static Comparator<ItemStateView> getItemStateComparator(Criteria criteria)
    {
        Comparator<ItemStateView> cmp = Comparator.comparingInt(s -> 1);
        if(criteria == null)
        {
            return cmp;
        }
        Criteria.OrderBy orderBy = criteria.getOrderBy();
        for(int i = 0; i < orderBy.getElementCount(); i++)
        {
            Criteria.OrderByElement e = orderBy.getElement(i);
            Comparator<ItemStateView> toCompose = getComparatorWithNullHandling(e.getKey());
            if(e.getDirection() == Criteria.OrderByElement.Direction.DESC)
            {
                toCompose = toCompose.reversed();
            }
            cmp = cmp.thenComparing(toCompose);
        }
        return cmp;
    }


    private static <T extends ItemStateView> Comparator<T> getComparatorWithNullHandling(SingleAttributeKey key)
    {
        return Comparator.comparing(s -> toComparable(s.get((Key)key)), ObjectUtils::compare);
    }


    private static Comparable toComparable(Object object)
    {
        if(object instanceof Reference)
        {
            return (Comparable)((Reference)object).getIdentity();
        }
        if(object instanceof SerializableValue)
        {
            return (Comparable)((SerializableValue)object).getSerializableObject();
        }
        return (Comparable)object;
    }
}
