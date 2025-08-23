package de.hybris.platform.fraud.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.basecommerce.enums.FraudStatus;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;
import java.util.List;

public class FraudReportModel extends ItemModel
{
    public static final String _TYPECODE = "FraudReport";
    public static final String _ORDERFRAUDREPORTRELATION = "OrderFraudReportRelation";
    public static final String CODE = "code";
    public static final String PROVIDER = "provider";
    public static final String TIMESTAMP = "timestamp";
    public static final String STATUS = "status";
    public static final String EXPLANATION = "explanation";
    public static final String ORDER = "order";
    public static final String FRAUDSYMPTOMSCORINGS = "fraudSymptomScorings";


    public FraudReportModel()
    {
    }


    public FraudReportModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FraudReportModel(String _code, Date _timestamp)
    {
        setCode(_code);
        setTimestamp(_timestamp);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FraudReportModel(String _code, ItemModel _owner, Date _timestamp)
    {
        setCode(_code);
        setOwner(_owner);
        setTimestamp(_timestamp);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "explanation", type = Accessor.Type.GETTER)
    public String getExplanation()
    {
        return (String)getPersistenceContext().getPropertyValue("explanation");
    }


    @Accessor(qualifier = "fraudSymptomScorings", type = Accessor.Type.GETTER)
    public List<FraudSymptomScoringModel> getFraudSymptomScorings()
    {
        return (List<FraudSymptomScoringModel>)getPersistenceContext().getPropertyValue("fraudSymptomScorings");
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public OrderModel getOrder()
    {
        return (OrderModel)getPersistenceContext().getPropertyValue("order");
    }


    @Accessor(qualifier = "provider", type = Accessor.Type.GETTER)
    public String getProvider()
    {
        return (String)getPersistenceContext().getPropertyValue("provider");
    }


    @Accessor(qualifier = "status", type = Accessor.Type.GETTER)
    public FraudStatus getStatus()
    {
        return (FraudStatus)getPersistenceContext().getPropertyValue("status");
    }


    @Accessor(qualifier = "timestamp", type = Accessor.Type.GETTER)
    public Date getTimestamp()
    {
        return (Date)getPersistenceContext().getPropertyValue("timestamp");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "explanation", type = Accessor.Type.SETTER)
    public void setExplanation(String value)
    {
        getPersistenceContext().setPropertyValue("explanation", value);
    }


    @Accessor(qualifier = "fraudSymptomScorings", type = Accessor.Type.SETTER)
    public void setFraudSymptomScorings(List<FraudSymptomScoringModel> value)
    {
        getPersistenceContext().setPropertyValue("fraudSymptomScorings", value);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(OrderModel value)
    {
        getPersistenceContext().setPropertyValue("order", value);
    }


    @Accessor(qualifier = "provider", type = Accessor.Type.SETTER)
    public void setProvider(String value)
    {
        getPersistenceContext().setPropertyValue("provider", value);
    }


    @Accessor(qualifier = "status", type = Accessor.Type.SETTER)
    public void setStatus(FraudStatus value)
    {
        getPersistenceContext().setPropertyValue("status", value);
    }


    @Accessor(qualifier = "timestamp", type = Accessor.Type.SETTER)
    public void setTimestamp(Date value)
    {
        getPersistenceContext().setPropertyValue("timestamp", value);
    }
}
