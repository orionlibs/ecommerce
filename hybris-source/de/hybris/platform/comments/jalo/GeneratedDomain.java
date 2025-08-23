package de.hybris.platform.comments.jalo;

import de.hybris.platform.comments.constants.GeneratedCommentsConstants;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedDomain extends GenericItem
{
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String COMPONENTS = "components";
    public static final String COMMENTTYPES = "commentTypes";
    protected static final OneToManyHandler<Component> COMPONENTSHANDLER = new OneToManyHandler(GeneratedCommentsConstants.TC.COMPONENT, true, "domain", null, false, true, 0);
    protected static final OneToManyHandler<CommentType> COMMENTTYPESHANDLER = new OneToManyHandler(GeneratedCommentsConstants.TC.COMMENTTYPE, true, "domain", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
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


    public Collection<CommentType> getCommentTypes(SessionContext ctx)
    {
        return COMMENTTYPESHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<CommentType> getCommentTypes()
    {
        return getCommentTypes(getSession().getSessionContext());
    }


    public void setCommentTypes(SessionContext ctx, Collection<CommentType> value)
    {
        COMMENTTYPESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setCommentTypes(Collection<CommentType> value)
    {
        setCommentTypes(getSession().getSessionContext(), value);
    }


    public void addToCommentTypes(SessionContext ctx, CommentType value)
    {
        COMMENTTYPESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToCommentTypes(CommentType value)
    {
        addToCommentTypes(getSession().getSessionContext(), value);
    }


    public void removeFromCommentTypes(SessionContext ctx, CommentType value)
    {
        COMMENTTYPESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromCommentTypes(CommentType value)
    {
        removeFromCommentTypes(getSession().getSessionContext(), value);
    }


    public Collection<Component> getComponents(SessionContext ctx)
    {
        return COMPONENTSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<Component> getComponents()
    {
        return getComponents(getSession().getSessionContext());
    }


    public void setComponents(SessionContext ctx, Collection<Component> value)
    {
        COMPONENTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setComponents(Collection<Component> value)
    {
        setComponents(getSession().getSessionContext(), value);
    }


    public void addToComponents(SessionContext ctx, Component value)
    {
        COMPONENTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToComponents(Component value)
    {
        addToComponents(getSession().getSessionContext(), value);
    }


    public void removeFromComponents(SessionContext ctx, Component value)
    {
        COMPONENTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromComponents(Component value)
    {
        removeFromComponents(getSession().getSessionContext(), value);
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
