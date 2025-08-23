package de.hybris.platform.comments.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

public class ComponentModel extends ItemModel
{
    public static final String _TYPECODE = "Component";
    public static final String _DOMAINCOMPONENTRELATION = "DomainComponentRelation";
    public static final String _COMMENTCOMPONENTRELATION = "CommentComponentRelation";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String AVAILABLECOMMENTTYPES = "availableCommentTypes";
    public static final String DOMAIN = "domain";
    public static final String COMMENT = "comment";
    public static final String READPERMITTED = "readPermitted";
    public static final String WRITEPERMITTED = "writePermitted";
    public static final String CREATEPERMITTED = "createPermitted";
    public static final String REMOVEPERMITTED = "removePermitted";


    public ComponentModel()
    {
    }


    public ComponentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ComponentModel(DomainModel _domain)
    {
        setDomain(_domain);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ComponentModel(String _code, DomainModel _domain, ItemModel _owner)
    {
        setCode(_code);
        setDomain(_domain);
        setOwner(_owner);
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "availableCommentTypes", type = Accessor.Type.GETTER)
    public Collection<CommentTypeModel> getAvailableCommentTypes()
    {
        return (Collection<CommentTypeModel>)getPersistenceContext().getPropertyValue("availableCommentTypes");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "createPermitted", type = Accessor.Type.GETTER)
    public Collection<PrincipalModel> getCreatePermitted()
    {
        return getCreatePermitted(null);
    }


    @Accessor(qualifier = "createPermitted", type = Accessor.Type.GETTER)
    public Collection<PrincipalModel> getCreatePermitted(Locale loc)
    {
        return (Collection<PrincipalModel>)getPersistenceContext().getLocalizedRelationValue("createPermitted", loc);
    }


    @Accessor(qualifier = "domain", type = Accessor.Type.GETTER)
    public DomainModel getDomain()
    {
        return (DomainModel)getPersistenceContext().getPropertyValue("domain");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "readPermitted", type = Accessor.Type.GETTER)
    public Collection<PrincipalModel> getReadPermitted()
    {
        return getReadPermitted(null);
    }


    @Accessor(qualifier = "readPermitted", type = Accessor.Type.GETTER)
    public Collection<PrincipalModel> getReadPermitted(Locale loc)
    {
        return (Collection<PrincipalModel>)getPersistenceContext().getLocalizedRelationValue("readPermitted", loc);
    }


    @Accessor(qualifier = "removePermitted", type = Accessor.Type.GETTER)
    public Collection<PrincipalModel> getRemovePermitted()
    {
        return getRemovePermitted(null);
    }


    @Accessor(qualifier = "removePermitted", type = Accessor.Type.GETTER)
    public Collection<PrincipalModel> getRemovePermitted(Locale loc)
    {
        return (Collection<PrincipalModel>)getPersistenceContext().getLocalizedRelationValue("removePermitted", loc);
    }


    @Accessor(qualifier = "writePermitted", type = Accessor.Type.GETTER)
    public Collection<PrincipalModel> getWritePermitted()
    {
        return getWritePermitted(null);
    }


    @Accessor(qualifier = "writePermitted", type = Accessor.Type.GETTER)
    public Collection<PrincipalModel> getWritePermitted(Locale loc)
    {
        return (Collection<PrincipalModel>)getPersistenceContext().getLocalizedRelationValue("writePermitted", loc);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "createPermitted", type = Accessor.Type.SETTER)
    public void setCreatePermitted(Collection<PrincipalModel> value)
    {
        setCreatePermitted(value, null);
    }


    @Accessor(qualifier = "createPermitted", type = Accessor.Type.SETTER)
    public void setCreatePermitted(Collection<PrincipalModel> value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("createPermitted", loc, value);
    }


    @Accessor(qualifier = "domain", type = Accessor.Type.SETTER)
    public void setDomain(DomainModel value)
    {
        getPersistenceContext().setPropertyValue("domain", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "readPermitted", type = Accessor.Type.SETTER)
    public void setReadPermitted(Collection<PrincipalModel> value)
    {
        setReadPermitted(value, null);
    }


    @Accessor(qualifier = "readPermitted", type = Accessor.Type.SETTER)
    public void setReadPermitted(Collection<PrincipalModel> value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("readPermitted", loc, value);
    }


    @Accessor(qualifier = "removePermitted", type = Accessor.Type.SETTER)
    public void setRemovePermitted(Collection<PrincipalModel> value)
    {
        setRemovePermitted(value, null);
    }


    @Accessor(qualifier = "removePermitted", type = Accessor.Type.SETTER)
    public void setRemovePermitted(Collection<PrincipalModel> value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("removePermitted", loc, value);
    }


    @Accessor(qualifier = "writePermitted", type = Accessor.Type.SETTER)
    public void setWritePermitted(Collection<PrincipalModel> value)
    {
        setWritePermitted(value, null);
    }


    @Accessor(qualifier = "writePermitted", type = Accessor.Type.SETTER)
    public void setWritePermitted(Collection<PrincipalModel> value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("writePermitted", loc, value);
    }
}
