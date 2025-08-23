package de.hybris.platform.util;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.JaloSecurityException;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;

public class BidirectionalOneToManyHandler<T extends ExtensibleItem> extends OneToManyHandler<T>
{
    public BidirectionalOneToManyHandler(String targetItemType, boolean partOf, String foreignKeyAttr, String orderNumberAttr, boolean reorderable, boolean asc, int typeOfCollection)
    {
        super(targetItemType, partOf, foreignKeyAttr, orderNumberAttr, reorderable, asc, typeOfCollection);
    }


    public BidirectionalOneToManyHandler(String targetItemType, boolean partOf, String foreignKeyAttr, String orderNumberAttr, boolean reorderable, boolean asc, int typeOfCollection, String conditionQuery)
    {
        super(targetItemType, partOf, foreignKeyAttr, orderNumberAttr, reorderable, asc, typeOfCollection, conditionQuery);
    }


    public void addValue(SessionContext ctx, Item key, T toLink)
    {
        if(useLegacyMode())
        {
            try
            {
                setForeignKeyValue(ctx, key, toLink);
            }
            catch(RuntimeException e)
            {
                throw e;
            }
            catch(Exception e)
            {
                throw new JaloSystemException(e);
            }
        }
        else
        {
            super.addValue(ctx, key, (Item)toLink);
        }
    }


    public BidirectionalOneToManyHandler<T> withRelationCode(String relationCode)
    {
        if(StringUtils.isNotEmpty(relationCode))
        {
            this.reorderingEnabledFlagName = "relation." + relationCode + ".reordered";
        }
        return this;
    }


    protected void setForeignKeyValue(SessionContext ctx, Item key, T toLink) throws JaloSecurityException, JaloBusinessException
    {
        toLink.setProperty(ctx, this.foreignKeyAttr, key);
    }


    public void setValues(SessionContext ctx, Item key, Collection<T> values)
    {
        throw new UnsupportedOperationException("Setting values for relation attribute" + this.foreignKeyAttr + " should not be called from n-side of the relation");
    }
}
