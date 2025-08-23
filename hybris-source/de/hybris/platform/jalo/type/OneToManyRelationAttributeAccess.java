package de.hybris.platform.jalo.type;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;

public class OneToManyRelationAttributeAccess implements AttributeAccess
{
    private final String qualifier;
    private final OneToManyHandler<Item> handler;


    public OneToManyRelationAttributeAccess(RelationType rt, RelationDescriptor rd)
    {
        if(rd.isProperty())
        {
            this.qualifier = rd.getQualifier();
            this.handler = null;
        }
        else
        {
            this.qualifier = null;
            boolean isSource = rd.isSource();
            int typeOfCollection = rd.isLocalized() ? ((CollectionType)((MapType)rd.getRealAttributeType()).getReturnType()).getTypeOfCollection() : ((CollectionType)rd.getRealAttributeType()).getTypeOfCollection();
            this
                            .handler = new OneToManyHandler(isSource ? rt.getTargetType().getCode() : rt.getSourceType().getCode(), rd.isPartOf(), isSource ? rt.getSourceTypeRole() : rt.getTargetTypeRole(), null, false, false, typeOfCollection);
        }
    }


    public Object getValue(SessionContext ctx, Item item) throws JaloTypeException, JaloInvalidParameterException, JaloSecurityException
    {
        return (this.handler != null) ? this.handler.getValues(ctx, item) : ((ExtensibleItem)item).getProperty(ctx, this.qualifier);
    }


    public void setValue(SessionContext ctx, Item item, Object value) throws JaloTypeException, JaloInvalidParameterException, JaloSecurityException, JaloBusinessException
    {
        if(this.handler != null)
        {
            this.handler.setValues(ctx, item, (Collection)value);
        }
        else
        {
            ((ExtensibleItem)item).setProperty(ctx, this.qualifier, value);
        }
    }
}
