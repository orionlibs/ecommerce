package de.hybris.platform.comments.jalo;

import de.hybris.platform.comments.constants.GeneratedCommentsConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCommentType extends GenericItem
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String METATYPE = "metaType";
    public static final String DOMAIN = "domain";
    public static final String COMMENT = "comment";
    protected static final BidirectionalOneToManyHandler<GeneratedCommentType> DOMAINHANDLER = new BidirectionalOneToManyHandler(GeneratedCommentsConstants.TC.COMMENTTYPE, false, "domain", null, false, true, 0);
    protected static final OneToManyHandler<Comment> COMMENTHANDLER = new OneToManyHandler(GeneratedCommentsConstants.TC.COMMENT, false, "commentType", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("metaType", Item.AttributeMode.INITIAL);
        tmp.put("domain", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    protected void setCode(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'code' is not changeable", 0);
        }
        setProperty(ctx, "code", value);
    }


    protected void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    Collection<Comment> getComment(SessionContext ctx)
    {
        return COMMENTHANDLER.getValues(ctx, (Item)this);
    }


    Collection<Comment> getComment()
    {
        return getComment(getSession().getSessionContext());
    }


    void setComment(SessionContext ctx, Collection<Comment> value)
    {
        COMMENTHANDLER.setValues(ctx, (Item)this, value);
    }


    void setComment(Collection<Comment> value)
    {
        setComment(getSession().getSessionContext(), value);
    }


    void addToComment(SessionContext ctx, Comment value)
    {
        COMMENTHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    void addToComment(Comment value)
    {
        addToComment(getSession().getSessionContext(), value);
    }


    void removeFromComment(SessionContext ctx, Comment value)
    {
        COMMENTHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    void removeFromComment(Comment value)
    {
        removeFromComment(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        DOMAINHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Domain getDomain(SessionContext ctx)
    {
        return (Domain)getProperty(ctx, "domain");
    }


    public Domain getDomain()
    {
        return getDomain(getSession().getSessionContext());
    }


    protected void setDomain(SessionContext ctx, Domain value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'domain' is not changeable", 0);
        }
        DOMAINHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setDomain(Domain value)
    {
        setDomain(getSession().getSessionContext(), value);
    }


    public ComposedType getMetaType(SessionContext ctx)
    {
        return (ComposedType)getProperty(ctx, "metaType");
    }


    public ComposedType getMetaType()
    {
        return getMetaType(getSession().getSessionContext());
    }


    public void setMetaType(SessionContext ctx, ComposedType value)
    {
        setProperty(ctx, "metaType", value);
    }


    public void setMetaType(ComposedType value)
    {
        setMetaType(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        setProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }
}
