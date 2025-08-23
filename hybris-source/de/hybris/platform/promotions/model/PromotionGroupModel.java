package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class PromotionGroupModel extends ItemModel
{
    public static final String _TYPECODE = "PromotionGroup";
    public static final String IDENTIFIER = "Identifier";
    public static final String PROMOTIONS = "Promotions";
    public static final String PROMOTIONSOURCERULES = "promotionSourceRules";


    public PromotionGroupModel()
    {
    }


    public PromotionGroupModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionGroupModel(String _Identifier)
    {
        setIdentifier(_Identifier);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionGroupModel(String _Identifier, ItemModel _owner)
    {
        setIdentifier(_Identifier);
        setOwner(_owner);
    }


    @Accessor(qualifier = "Identifier", type = Accessor.Type.GETTER)
    public String getIdentifier()
    {
        return (String)getPersistenceContext().getPropertyValue("Identifier");
    }


    @Accessor(qualifier = "Promotions", type = Accessor.Type.GETTER)
    public Collection<AbstractPromotionModel> getPromotions()
    {
        return (Collection<AbstractPromotionModel>)getPersistenceContext().getPropertyValue("Promotions");
    }


    @Accessor(qualifier = "promotionSourceRules", type = Accessor.Type.GETTER)
    public Collection<PromotionSourceRuleModel> getPromotionSourceRules()
    {
        return (Collection<PromotionSourceRuleModel>)getPersistenceContext().getPropertyValue("promotionSourceRules");
    }


    @Accessor(qualifier = "Identifier", type = Accessor.Type.SETTER)
    public void setIdentifier(String value)
    {
        getPersistenceContext().setPropertyValue("Identifier", value);
    }


    @Accessor(qualifier = "Promotions", type = Accessor.Type.SETTER)
    public void setPromotions(Collection<AbstractPromotionModel> value)
    {
        getPersistenceContext().setPropertyValue("Promotions", value);
    }


    @Accessor(qualifier = "promotionSourceRules", type = Accessor.Type.SETTER)
    public void setPromotionSourceRules(Collection<PromotionSourceRuleModel> value)
    {
        getPersistenceContext().setPropertyValue("promotionSourceRules", value);
    }
}
