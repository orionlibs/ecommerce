package de.hybris.platform.personalizationservices.dynamic;

import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.personalizationservices.strategies.RankAssignmentStrategy;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class CxVariationRankAttributeHandler implements DynamicAttributeHandler<Integer, CxVariationModel>
{
    private RankAssignmentStrategy rankAssigmentStrategy;


    public Integer get(CxVariationModel model)
    {
        CxCustomizationModel customization = model.getCustomization();
        if(customization == null)
        {
            return null;
        }
        return Integer.valueOf(customization.getVariations().indexOf(model));
    }


    public void set(CxVariationModel model, Integer value)
    {
        CxCustomizationModel customization = model.getCustomization();
        if(customization == null)
        {
            return;
        }
        List<CxVariationModel> variations = getVariationList(customization);
        variations.remove(model);
        int pos = this.rankAssigmentStrategy.getRank(value, variations.size());
        variations.add(pos, model);
        customization.setVariations(variations);
    }


    protected List<CxVariationModel> getVariationList(CxCustomizationModel customization)
    {
        List<CxVariationModel> variations = customization.getVariations();
        if(variations == null)
        {
            return new ArrayList<>();
        }
        return new LinkedList<>(variations);
    }


    @Required
    public void setRankAssigmentStrategy(RankAssignmentStrategy rankAssigmentStrategy)
    {
        this.rankAssigmentStrategy = rankAssigmentStrategy;
    }


    protected RankAssignmentStrategy getRankAssigmentStrategy()
    {
        return this.rankAssigmentStrategy;
    }
}
