package de.hybris.platform.comments.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class DomainModel extends ItemModel
{
    public static final String _TYPECODE = "Domain";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String COMPONENTS = "components";
    public static final String COMMENTTYPES = "commentTypes";


    public DomainModel()
    {
    }


    public DomainModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public DomainModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "commentTypes", type = Accessor.Type.GETTER)
    public Collection<CommentTypeModel> getCommentTypes()
    {
        return (Collection<CommentTypeModel>)getPersistenceContext().getPropertyValue("commentTypes");
    }


    @Accessor(qualifier = "components", type = Accessor.Type.GETTER)
    public Collection<ComponentModel> getComponents()
    {
        return (Collection<ComponentModel>)getPersistenceContext().getPropertyValue("components");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "commentTypes", type = Accessor.Type.SETTER)
    public void setCommentTypes(Collection<CommentTypeModel> value)
    {
        getPersistenceContext().setPropertyValue("commentTypes", value);
    }


    @Accessor(qualifier = "components", type = Accessor.Type.SETTER)
    public void setComponents(Collection<ComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("components", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }
}
