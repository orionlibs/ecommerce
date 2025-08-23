package de.hybris.platform.fraud.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class FraudSymptomScoringModel extends ItemModel
{
    public static final String _TYPECODE = "FraudSymptomScoring";
    public static final String _FRAUDREPORTFRAUDSYMPTOMSCORINGRELATION = "FraudReportFraudSymptomScoringRelation";
    public static final String NAME = "name";
    public static final String SCORE = "score";
    public static final String EXPLANATION = "explanation";
    public static final String FRAUDREPORT = "fraudReport";


    public FraudSymptomScoringModel()
    {
    }


    public FraudSymptomScoringModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FraudSymptomScoringModel(FraudReportModel _fraudReport, String _name)
    {
        setFraudReport(_fraudReport);
        setName(_name);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FraudSymptomScoringModel(FraudReportModel _fraudReport, String _name, ItemModel _owner)
    {
        setFraudReport(_fraudReport);
        setName(_name);
        setOwner(_owner);
    }


    @Accessor(qualifier = "explanation", type = Accessor.Type.GETTER)
    public String getExplanation()
    {
        return (String)getPersistenceContext().getPropertyValue("explanation");
    }


    @Accessor(qualifier = "fraudReport", type = Accessor.Type.GETTER)
    public FraudReportModel getFraudReport()
    {
        return (FraudReportModel)getPersistenceContext().getPropertyValue("fraudReport");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "score", type = Accessor.Type.GETTER)
    public double getScore()
    {
        return toPrimitive((Double)getPersistenceContext().getPropertyValue("score"));
    }


    @Accessor(qualifier = "explanation", type = Accessor.Type.SETTER)
    public void setExplanation(String value)
    {
        getPersistenceContext().setPropertyValue("explanation", value);
    }


    @Accessor(qualifier = "fraudReport", type = Accessor.Type.SETTER)
    public void setFraudReport(FraudReportModel value)
    {
        getPersistenceContext().setPropertyValue("fraudReport", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "score", type = Accessor.Type.SETTER)
    public void setScore(double value)
    {
        getPersistenceContext().setPropertyValue("score", toObject(value));
    }
}
