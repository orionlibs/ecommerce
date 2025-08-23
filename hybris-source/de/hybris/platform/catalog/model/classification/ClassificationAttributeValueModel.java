package de.hybris.platform.catalog.model.classification;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class ClassificationAttributeValueModel extends ItemModel
{
    public static final String _TYPECODE = "ClassificationAttributeValue";
    public static final String CODE = "code";
    public static final String EXTERNALID = "externalID";
    public static final String NAME = "name";
    public static final String SYSTEMVERSION = "systemVersion";


    public ClassificationAttributeValueModel()
    {
    }


    public ClassificationAttributeValueModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ClassificationAttributeValueModel(String _code, ClassificationSystemVersionModel _systemVersion)
    {
        setCode(_code);
        setSystemVersion(_systemVersion);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ClassificationAttributeValueModel(String _code, ItemModel _owner, ClassificationSystemVersionModel _systemVersion)
    {
        setCode(_code);
        setOwner(_owner);
        setSystemVersion(_systemVersion);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "externalID", type = Accessor.Type.GETTER)
    public String getExternalID()
    {
        return (String)getPersistenceContext().getPropertyValue("externalID");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "systemVersion", type = Accessor.Type.GETTER)
    public ClassificationSystemVersionModel getSystemVersion()
    {
        return (ClassificationSystemVersionModel)getPersistenceContext().getPropertyValue("systemVersion");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "externalID", type = Accessor.Type.SETTER)
    public void setExternalID(String value)
    {
        getPersistenceContext().setPropertyValue("externalID", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "systemVersion", type = Accessor.Type.SETTER)
    public void setSystemVersion(ClassificationSystemVersionModel value)
    {
        getPersistenceContext().setPropertyValue("systemVersion", value);
    }
}
