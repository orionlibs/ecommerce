package de.hybris.platform.comments.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class CommentTypeModel extends ItemModel
{
    public static final String _TYPECODE = "CommentType";
    public static final String _DOMAINCOMMENTTYPERELATION = "DomainCommentTypeRelation";
    public static final String _COMMENTCOMMENTTYPERELATION = "CommentCommentTypeRelation";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String METATYPE = "metaType";
    public static final String DOMAIN = "domain";
    public static final String COMMENT = "comment";


    public CommentTypeModel()
    {
    }


    public CommentTypeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CommentTypeModel(DomainModel _domain)
    {
        setDomain(_domain);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CommentTypeModel(String _code, DomainModel _domain, ItemModel _owner)
    {
        setCode(_code);
        setDomain(_domain);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "domain", type = Accessor.Type.GETTER)
    public DomainModel getDomain()
    {
        return (DomainModel)getPersistenceContext().getPropertyValue("domain");
    }


    @Accessor(qualifier = "metaType", type = Accessor.Type.GETTER)
    public ComposedTypeModel getMetaType()
    {
        return (ComposedTypeModel)getPersistenceContext().getPropertyValue("metaType");
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


    @Accessor(qualifier = "domain", type = Accessor.Type.SETTER)
    public void setDomain(DomainModel value)
    {
        getPersistenceContext().setPropertyValue("domain", value);
    }


    @Accessor(qualifier = "metaType", type = Accessor.Type.SETTER)
    public void setMetaType(ComposedTypeModel value)
    {
        getPersistenceContext().setPropertyValue("metaType", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }
}
