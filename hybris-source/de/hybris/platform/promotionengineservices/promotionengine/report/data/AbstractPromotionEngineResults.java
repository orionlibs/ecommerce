package de.hybris.platform.promotionengineservices.promotionengine.report.data;

import java.io.Serializable;
import java.util.List;

public class AbstractPromotionEngineResults implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<PromotionEngineResult> promotionEngineResults;


    public void setPromotionEngineResults(List<PromotionEngineResult> promotionEngineResults)
    {
        this.promotionEngineResults = promotionEngineResults;
    }


    public List<PromotionEngineResult> getPromotionEngineResults()
    {
        return this.promotionEngineResults;
    }
}
