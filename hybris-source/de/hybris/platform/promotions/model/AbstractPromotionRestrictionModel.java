package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Locale;

public class AbstractPromotionRestrictionModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractPromotionRestriction";
    public static final String RESTRICTIONTYPE = "restrictionType";
    public static final String DESCRIPTIONPATTERN = "descriptionPattern";
    public static final String RENDEREDDESCRIPTION = "renderedDescription";
    public static final String PROMOTION = "promotion";


    public AbstractPromotionRestrictionModel()
    {
    }


    public AbstractPromotionRestrictionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractPromotionRestrictionModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "descriptionPattern", type = Accessor.Type.GETTER)
    public String getDescriptionPattern()
    {
        return getDescriptionPattern(null);
    }


    @Accessor(qualifier = "descriptionPattern", type = Accessor.Type.GETTER)
    public String getDescriptionPattern(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("descriptionPattern", loc);
    }


    @Accessor(qualifier = "promotion", type = Accessor.Type.GETTER)
    public AbstractPromotionModel getPromotion()
    {
        return (AbstractPromotionModel)getPersistenceContext().getPropertyValue("promotion");
    }


    @Accessor(qualifier = "renderedDescription", type = Accessor.Type.GETTER)
    public String getRenderedDescription()
    {
        return (String)getPersistenceContext().getPropertyValue("renderedDescription");
    }


    @Accessor(qualifier = "restrictionType", type = Accessor.Type.GETTER)
    public String getRestrictionType()
    {
        return getRestrictionType(null);
    }


    @Accessor(qualifier = "restrictionType", type = Accessor.Type.GETTER)
    public String getRestrictionType(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("restrictionType", loc);
    }


    @Accessor(qualifier = "descriptionPattern", type = Accessor.Type.SETTER)
    public void setDescriptionPattern(String value)
    {
        setDescriptionPattern(value, null);
    }


    @Accessor(qualifier = "descriptionPattern", type = Accessor.Type.SETTER)
    public void setDescriptionPattern(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("descriptionPattern", loc, value);
    }


    @Accessor(qualifier = "promotion", type = Accessor.Type.SETTER)
    public void setPromotion(AbstractPromotionModel value)
    {
        getPersistenceContext().setPropertyValue("promotion", value);
    }
}
