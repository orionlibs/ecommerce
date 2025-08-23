package de.hybris.platform.personalizationservices.dynamic;

import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxCustomizationsGroupModel;
import de.hybris.platform.personalizationservices.strategies.RankAssignmentStrategy;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class CxCustomizationRankAttributeHandler implements DynamicAttributeHandler<Integer, CxCustomizationModel>
{
    private RankAssignmentStrategy rankAssigmentStrategy;


    public Integer get(CxCustomizationModel model)
    {
        CxCustomizationsGroupModel catalog = model.getGroup();
        if(catalog == null)
        {
            return null;
        }
        return Integer.valueOf(catalog.getCustomizations().indexOf(model));
    }


    public void set(CxCustomizationModel model, Integer value)
    {
        CxCustomizationsGroupModel catalog = model.getGroup();
        if(catalog == null)
        {
            return;
        }
        List<CxCustomizationModel> customizations = getCustomizationList(catalog);
        customizations.remove(model);
        int pos = this.rankAssigmentStrategy.getRank(value, customizations.size());
        customizations.add(pos, model);
        catalog.setCustomizations(customizations);
    }


    protected List<CxCustomizationModel> getCustomizationList(CxCustomizationsGroupModel catalog)
    {
        List<CxCustomizationModel> customizations = catalog.getCustomizations();
        if(customizations == null)
        {
            return new LinkedList<>();
        }
        return new LinkedList<>(customizations);
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
