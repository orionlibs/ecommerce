package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCMSInverseRestriction extends AbstractRestriction
{
    public static final String ORIGINALRESTRICTION = "originalRestriction";
    protected static final BidirectionalOneToManyHandler<GeneratedCMSInverseRestriction> ORIGINALRESTRICTIONHANDLER = new BidirectionalOneToManyHandler(GeneratedCms2Constants.TC.CMSINVERSERESTRICTION, false, "originalRestriction", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractRestriction.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("originalRestriction", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        ORIGINALRESTRICTIONHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public AbstractRestriction getOriginalRestriction(SessionContext ctx)
    {
        return (AbstractRestriction)getProperty(ctx, "originalRestriction");
    }


    public AbstractRestriction getOriginalRestriction()
    {
        return getOriginalRestriction(getSession().getSessionContext());
    }


    public void setOriginalRestriction(SessionContext ctx, AbstractRestriction value)
    {
        ORIGINALRESTRICTIONHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setOriginalRestriction(AbstractRestriction value)
    {
        setOriginalRestriction(getSession().getSessionContext(), value);
    }
}
