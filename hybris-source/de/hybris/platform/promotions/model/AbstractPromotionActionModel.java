package de.hybris.platform.promotions.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AbstractPromotionActionModel extends ItemModel
{
    public static final String _TYPECODE = "AbstractPromotionAction";
    public static final String _PROMOTIONRESULT2PROMOTIONACTIONSRELATION = "PromotionResult2PromotionActionsRelation";
    public static final String MARKEDAPPLIED = "markedApplied";
    public static final String GUID = "guid";
    public static final String PROMOTIONRESULT = "promotionResult";


    public AbstractPromotionActionModel()
    {
    }


    public AbstractPromotionActionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AbstractPromotionActionModel(ItemModel _owner)
    {
        setOwner(_owner);
    }


    @Accessor(qualifier = "guid", type = Accessor.Type.GETTER)
    public String getGuid()
    {
        return (String)getPersistenceContext().getPropertyValue("guid");
    }


    @Accessor(qualifier = "markedApplied", type = Accessor.Type.GETTER)
    public Boolean getMarkedApplied()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("markedApplied");
    }


    @Accessor(qualifier = "promotionResult", type = Accessor.Type.GETTER)
    public PromotionResultModel getPromotionResult()
    {
        return (PromotionResultModel)getPersistenceContext().getPropertyValue("promotionResult");
    }


    @Accessor(qualifier = "guid", type = Accessor.Type.SETTER)
    public void setGuid(String value)
    {
        getPersistenceContext().setPropertyValue("guid", value);
    }


    @Accessor(qualifier = "markedApplied", type = Accessor.Type.SETTER)
    public void setMarkedApplied(Boolean value)
    {
        getPersistenceContext().setPropertyValue("markedApplied", value);
    }


    @Accessor(qualifier = "promotionResult", type = Accessor.Type.SETTER)
    public void setPromotionResult(PromotionResultModel value)
    {
        getPersistenceContext().setPropertyValue("promotionResult", value);
    }
}
