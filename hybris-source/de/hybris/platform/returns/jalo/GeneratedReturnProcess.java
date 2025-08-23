package de.hybris.platform.returns.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.processengine.jalo.BusinessProcess;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedReturnProcess extends BusinessProcess
{
    public static final String RETURNREQUEST = "returnRequest";
    protected static final BidirectionalOneToManyHandler<GeneratedReturnProcess> RETURNREQUESTHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.RETURNPROCESS, false, "returnRequest", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(BusinessProcess.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("returnRequest", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        RETURNREQUESTHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public ReturnRequest getReturnRequest(SessionContext ctx)
    {
        return (ReturnRequest)getProperty(ctx, "returnRequest");
    }


    public ReturnRequest getReturnRequest()
    {
        return getReturnRequest(getSession().getSessionContext());
    }


    public void setReturnRequest(SessionContext ctx, ReturnRequest value)
    {
        RETURNREQUESTHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setReturnRequest(ReturnRequest value)
    {
        setReturnRequest(getSession().getSessionContext(), value);
    }
}
