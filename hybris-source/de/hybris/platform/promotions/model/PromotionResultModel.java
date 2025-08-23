package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;
import java.util.Set;

public class PromotionResultModel extends ItemModel
{
    public static final String _TYPECODE = "PromotionResult";
    public static final String _ORDER2PROMOTIONRESULTSRELATION = "Order2PromotionResultsRelation";
    public static final String ACTIONS = "actions";
    public static final String CONSUMEDENTRIES = "consumedEntries";
    public static final String PROMOTION = "promotion";
    public static final String CERTAINTY = "certainty";
    public static final String CUSTOM = "custom";
    public static final String ORDER = "order";
    public static final String ALLPROMOTIONACTIONS = "allPromotionActions";
    public static final String RULESMODULENAME = "rulesModuleName";
    public static final String MODULEVERSION = "moduleVersion";
    public static final String RULEVERSION = "ruleVersion";
    public static final String MESSAGEFIRED = "messageFired";
    public static final String ORDERCODE = "orderCode";


    public PromotionResultModel()
    {
    }


    public PromotionResultModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionResultModel(AbstractOrderModel _order)
    {
        setOrder(_order);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public PromotionResultModel(AbstractOrderModel _order, ItemModel _owner)
    {
        setOrder(_order);
        setOwner(_owner);
    }


    @Accessor(qualifier = "actions", type = Accessor.Type.GETTER)
    public Collection<AbstractPromotionActionModel> getActions()
    {
        return (Collection<AbstractPromotionActionModel>)getPersistenceContext().getPropertyValue("actions");
    }


    @Accessor(qualifier = "allPromotionActions", type = Accessor.Type.GETTER)
    public Set<AbstractPromotionActionModel> getAllPromotionActions()
    {
        return (Set<AbstractPromotionActionModel>)getPersistenceContext().getPropertyValue("allPromotionActions");
    }


    @Accessor(qualifier = "certainty", type = Accessor.Type.GETTER)
    public Float getCertainty()
    {
        return (Float)getPersistenceContext().getPropertyValue("certainty");
    }


    @Accessor(qualifier = "consumedEntries", type = Accessor.Type.GETTER)
    public Collection<PromotionOrderEntryConsumedModel> getConsumedEntries()
    {
        return (Collection<PromotionOrderEntryConsumedModel>)getPersistenceContext().getPropertyValue("consumedEntries");
    }


    @Accessor(qualifier = "custom", type = Accessor.Type.GETTER)
    public String getCustom()
    {
        return (String)getPersistenceContext().getPropertyValue("custom");
    }


    @Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
    public String getMessageFired()
    {
        return getMessageFired(null);
    }


    @Accessor(qualifier = "messageFired", type = Accessor.Type.GETTER)
    public String getMessageFired(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("messageFired", loc);
    }


    @Accessor(qualifier = "moduleVersion", type = Accessor.Type.GETTER)
    public Long getModuleVersion()
    {
        return (Long)getPersistenceContext().getPropertyValue("moduleVersion");
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public AbstractOrderModel getOrder()
    {
        return (AbstractOrderModel)getPersistenceContext().getPropertyValue("order");
    }


    @Accessor(qualifier = "orderCode", type = Accessor.Type.GETTER)
    public String getOrderCode()
    {
        return (String)getPersistenceContext().getPropertyValue("orderCode");
    }


    @Accessor(qualifier = "promotion", type = Accessor.Type.GETTER)
    public AbstractPromotionModel getPromotion()
    {
        return (AbstractPromotionModel)getPersistenceContext().getPropertyValue("promotion");
    }


    @Accessor(qualifier = "rulesModuleName", type = Accessor.Type.GETTER)
    public String getRulesModuleName()
    {
        return (String)getPersistenceContext().getPropertyValue("rulesModuleName");
    }


    @Accessor(qualifier = "ruleVersion", type = Accessor.Type.GETTER)
    public Long getRuleVersion()
    {
        return (Long)getPersistenceContext().getPropertyValue("ruleVersion");
    }


    @Accessor(qualifier = "actions", type = Accessor.Type.SETTER)
    public void setActions(Collection<AbstractPromotionActionModel> value)
    {
        getPersistenceContext().setPropertyValue("actions", value);
    }


    @Accessor(qualifier = "allPromotionActions", type = Accessor.Type.SETTER)
    public void setAllPromotionActions(Set<AbstractPromotionActionModel> value)
    {
        getPersistenceContext().setPropertyValue("allPromotionActions", value);
    }


    @Accessor(qualifier = "certainty", type = Accessor.Type.SETTER)
    public void setCertainty(Float value)
    {
        getPersistenceContext().setPropertyValue("certainty", value);
    }


    @Accessor(qualifier = "consumedEntries", type = Accessor.Type.SETTER)
    public void setConsumedEntries(Collection<PromotionOrderEntryConsumedModel> value)
    {
        getPersistenceContext().setPropertyValue("consumedEntries", value);
    }


    @Accessor(qualifier = "custom", type = Accessor.Type.SETTER)
    public void setCustom(String value)
    {
        getPersistenceContext().setPropertyValue("custom", value);
    }


    @Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
    public void setMessageFired(String value)
    {
        setMessageFired(value, null);
    }


    @Accessor(qualifier = "messageFired", type = Accessor.Type.SETTER)
    public void setMessageFired(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("messageFired", loc, value);
    }


    @Accessor(qualifier = "moduleVersion", type = Accessor.Type.SETTER)
    public void setModuleVersion(Long value)
    {
        getPersistenceContext().setPropertyValue("moduleVersion", value);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(AbstractOrderModel value)
    {
        getPersistenceContext().setPropertyValue("order", value);
    }


    @Accessor(qualifier = "orderCode", type = Accessor.Type.SETTER)
    public void setOrderCode(String value)
    {
        getPersistenceContext().setPropertyValue("orderCode", value);
    }


    @Accessor(qualifier = "promotion", type = Accessor.Type.SETTER)
    public void setPromotion(AbstractPromotionModel value)
    {
        getPersistenceContext().setPropertyValue("promotion", value);
    }


    @Accessor(qualifier = "rulesModuleName", type = Accessor.Type.SETTER)
    public void setRulesModuleName(String value)
    {
        getPersistenceContext().setPropertyValue("rulesModuleName", value);
    }


    @Accessor(qualifier = "ruleVersion", type = Accessor.Type.SETTER)
    public void setRuleVersion(Long value)
    {
        getPersistenceContext().setPropertyValue("ruleVersion", value);
    }
}
