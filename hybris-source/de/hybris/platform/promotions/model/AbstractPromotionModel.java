package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

public class AbstractPromotionModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractPromotion";
    public static final String _PROMOTIONGROUPPROMOTIONSRELATION = "PromotionGroupPromotionsRelation";
    public static final String PROMOTIONTYPE = "promotionType";
    public static final String CODE = "code";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String STARTDATE = "startDate";
    public static final String ENDDATE = "endDate";
    public static final String DETAILSURL = "detailsURL";
    public static final String RESTRICTIONS = "restrictions";
    public static final String ENABLED = "enabled";
    public static final String PRIORITY = "priority";
    public static final String IMMUTABLEKEYHASH = "immutableKeyHash";
    public static final String IMMUTABLEKEY = "immutableKey";
    public static final String NAME = "name";
    public static final String PROMOTIONGROUP = "PromotionGroup";


    public AbstractPromotionModel()
    {
    }


    public AbstractPromotionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractPromotionModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractPromotionModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return (String)getPersistenceContext().getPropertyValue("description");
    }


    @Accessor(qualifier = "detailsURL", type = Accessor.Type.GETTER)
    public String getDetailsURL()
    {
        return (String)getPersistenceContext().getPropertyValue("detailsURL");
    }


    @Accessor(qualifier = "enabled", type = Accessor.Type.GETTER)
    public Boolean getEnabled()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("enabled");
    }


    @Accessor(qualifier = "endDate", type = Accessor.Type.GETTER)
    public Date getEndDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("endDate");
    }


    @Accessor(qualifier = "immutableKey", type = Accessor.Type.GETTER)
    public String getImmutableKey()
    {
        return (String)getPersistenceContext().getPropertyValue("immutableKey");
    }


    @Accessor(qualifier = "immutableKeyHash", type = Accessor.Type.GETTER)
    public String getImmutableKeyHash()
    {
        return (String)getPersistenceContext().getPropertyValue("immutableKeyHash");
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


    @Accessor(qualifier = "priority", type = Accessor.Type.GETTER)
    public Integer getPriority()
    {
        return (Integer)getPersistenceContext().getPropertyValue("priority");
    }


    @Accessor(qualifier = "PromotionGroup", type = Accessor.Type.GETTER)
    public PromotionGroupModel getPromotionGroup()
    {
        return (PromotionGroupModel)getPersistenceContext().getPropertyValue("PromotionGroup");
    }


    @Accessor(qualifier = "promotionType", type = Accessor.Type.GETTER)
    public String getPromotionType()
    {
        return getPromotionType(null);
    }


    @Accessor(qualifier = "promotionType", type = Accessor.Type.GETTER)
    public String getPromotionType(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("promotionType", loc);
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.GETTER)
    public Collection<AbstractPromotionRestrictionModel> getRestrictions()
    {
        return (Collection<AbstractPromotionRestrictionModel>)getPersistenceContext().getPropertyValue("restrictions");
    }


    @Accessor(qualifier = "startDate", type = Accessor.Type.GETTER)
    public Date getStartDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("startDate");
    }


    @Accessor(qualifier = "title", type = Accessor.Type.GETTER)
    public String getTitle()
    {
        return (String)getPersistenceContext().getPropertyValue("title");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        getPersistenceContext().setPropertyValue("description", value);
    }


    @Accessor(qualifier = "detailsURL", type = Accessor.Type.SETTER)
    public void setDetailsURL(String value)
    {
        getPersistenceContext().setPropertyValue("detailsURL", value);
    }


    @Accessor(qualifier = "enabled", type = Accessor.Type.SETTER)
    public void setEnabled(Boolean value)
    {
        getPersistenceContext().setPropertyValue("enabled", value);
    }


    @Accessor(qualifier = "endDate", type = Accessor.Type.SETTER)
    public void setEndDate(Date value)
    {
        getPersistenceContext().setPropertyValue("endDate", value);
    }


    @Accessor(qualifier = "immutableKey", type = Accessor.Type.SETTER)
    public void setImmutableKey(String value)
    {
        getPersistenceContext().setPropertyValue("immutableKey", value);
    }


    @Accessor(qualifier = "immutableKeyHash", type = Accessor.Type.SETTER)
    public void setImmutableKeyHash(String value)
    {
        getPersistenceContext().setPropertyValue("immutableKeyHash", value);
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


    @Accessor(qualifier = "priority", type = Accessor.Type.SETTER)
    public void setPriority(Integer value)
    {
        getPersistenceContext().setPropertyValue("priority", value);
    }


    @Accessor(qualifier = "PromotionGroup", type = Accessor.Type.SETTER)
    public void setPromotionGroup(PromotionGroupModel value)
    {
        getPersistenceContext().setPropertyValue("PromotionGroup", value);
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.SETTER)
    public void setRestrictions(Collection<AbstractPromotionRestrictionModel> value)
    {
        getPersistenceContext().setPropertyValue("restrictions", value);
    }


    @Accessor(qualifier = "startDate", type = Accessor.Type.SETTER)
    public void setStartDate(Date value)
    {
        getPersistenceContext().setPropertyValue("startDate", value);
    }


    @Accessor(qualifier = "title", type = Accessor.Type.SETTER)
    public void setTitle(String value)
    {
        getPersistenceContext().setPropertyValue("title", value);
    }
}
