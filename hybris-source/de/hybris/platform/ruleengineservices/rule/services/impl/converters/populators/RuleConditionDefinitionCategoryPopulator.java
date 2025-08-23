package de.hybris.platform.ruleengineservices.rule.services.impl.converters.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.ruleengineservices.model.RuleConditionDefinitionCategoryModel;
import de.hybris.platform.ruleengineservices.rule.data.ImageData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionCategoryData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import org.springframework.beans.factory.annotation.Required;

public class RuleConditionDefinitionCategoryPopulator implements Populator<RuleConditionDefinitionCategoryModel, RuleConditionDefinitionCategoryData>
{
    private Converter<MediaModel, ImageData> imageConverter;


    public void populate(RuleConditionDefinitionCategoryModel source, RuleConditionDefinitionCategoryData target)
    {
        target.setId(source.getId());
        target.setName(source.getName());
        target.setPriority(source.getPriority());
        if(source.getIcon() != null)
        {
            target.setIcon((ImageData)getImageConverter().convert(source.getIcon()));
        }
    }


    public Converter<MediaModel, ImageData> getImageConverter()
    {
        return this.imageConverter;
    }


    @Required
    public void setImageConverter(Converter<MediaModel, ImageData> imageConverter)
    {
        this.imageConverter = imageConverter;
    }
}
