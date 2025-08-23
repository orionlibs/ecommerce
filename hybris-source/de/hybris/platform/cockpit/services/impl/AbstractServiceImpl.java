package de.hybris.platform.cockpit.services.impl;

import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.util.I3LabelResolver;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;

public class AbstractServiceImpl
{
    protected TypeService typeService;
    protected ModelService modelService;
    private I3LabelResolver labelResolver;


    protected String getLocalizedLabel(String key)
    {
        if(this.labelResolver == null)
        {
            return Labels.getLabel(key);
        }
        return this.labelResolver.getLabel(key);
    }


    protected String getLocalizedLabel(String key, Object[] args)
    {
        if(this.labelResolver == null)
        {
            return Labels.getLabel(key, args);
        }
        return this.labelResolver.getLabel(key, args);
    }


    public void setLabelResolver(I3LabelResolver labelResolver)
    {
        this.labelResolver = labelResolver;
    }


    public List<EnumerationValueModel> getEnums(String type)
    {
        List<EnumerationValueModel> ret = new ArrayList<>();
        for(EnumerationValue ev : EnumerationManager.getInstance().getEnumerationType(type).getValues())
        {
            ret.add((EnumerationValueModel)this.modelService.get(ev));
        }
        return ret;
    }


    public EnumerationValueModel getEnum(String type, String code)
    {
        return (EnumerationValueModel)this.modelService.get(EnumerationManager.getInstance().getEnumerationValue(type, code));
    }


    @Required
    public void setCockpitTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }
}
