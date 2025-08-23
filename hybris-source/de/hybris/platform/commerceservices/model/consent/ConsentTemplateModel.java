package de.hybris.platform.commerceservices.model.consent;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class ConsentTemplateModel extends ItemModel
{
    public static final String _TYPECODE = "ConsentTemplate";
    public static final String ID = "id";
    public static final String VERSION = "version";
    public static final String BASESITE = "baseSite";
    public static final String NAME = "name";
    public static final String EXPOSED = "exposed";
    public static final String DESCRIPTION = "description";


    public ConsentTemplateModel()
    {
    }


    public ConsentTemplateModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsentTemplateModel(BaseSiteModel _baseSite, String _id, Integer _version)
    {
        setBaseSite(_baseSite);
        setId(_id);
        setVersion(_version);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsentTemplateModel(BaseSiteModel _baseSite, String _id, ItemModel _owner, Integer _version)
    {
        setBaseSite(_baseSite);
        setId(_id);
        setOwner(_owner);
        setVersion(_version);
    }


    @Accessor(qualifier = "baseSite", type = Accessor.Type.GETTER)
    public BaseSiteModel getBaseSite()
    {
        return (BaseSiteModel)getPersistenceContext().getPropertyValue("baseSite");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return getDescription(null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("description", loc);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
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


    @Accessor(qualifier = "version", type = Accessor.Type.GETTER)
    public Integer getVersion()
    {
        return (Integer)getPersistenceContext().getPropertyValue("version");
    }


    @Accessor(qualifier = "exposed", type = Accessor.Type.GETTER)
    public boolean isExposed()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("exposed"));
    }


    @Accessor(qualifier = "baseSite", type = Accessor.Type.SETTER)
    public void setBaseSite(BaseSiteModel value)
    {
        getPersistenceContext().setPropertyValue("baseSite", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        setDescription(value, null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("description", loc, value);
    }


    @Accessor(qualifier = "exposed", type = Accessor.Type.SETTER)
    public void setExposed(boolean value)
    {
        getPersistenceContext().setPropertyValue("exposed", toObject(value));
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
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


    @Accessor(qualifier = "version", type = Accessor.Type.SETTER)
    public void setVersion(Integer value)
    {
        getPersistenceContext().setPropertyValue("version", value);
    }
}
