package de.hybris.platform.personalizationfacades.customization.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.data.CustomizationData;
import de.hybris.platform.personalizationfacades.data.VariationData;
import de.hybris.platform.personalizationfacades.enums.ItemStatus;
import de.hybris.platform.personalizationservices.enums.CxItemStatus;
import de.hybris.platform.personalizationservices.model.CxCustomizationModel;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class CustomizationReversePopulator implements Populator<CustomizationData, CxCustomizationModel>
{
    private Converter<VariationData, CxVariationModel> variationConverter;


    public void populate(CustomizationData source, CxCustomizationModel target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        if(StringUtils.isEmpty(target.getCode()))
        {
            target.setCode(source.getCode());
        }
        target.setName(source.getName());
        target.setLongDescription(source.getDescription());
        target.setStatus(getItemStatus(source.getStatus()));
        target.setEnabledStartDate(source.getEnabledStartDate());
        target.setEnabledEndDate(source.getEnabledEndDate());
        target.setRank(source.getRank());
        if(this.variationConverter != null)
        {
            target.setVariations(convertVariationList(source));
        }
    }


    protected CxItemStatus getItemStatus(ItemStatus status)
    {
        if(status == null)
        {
            return CxItemStatus.ENABLED;
        }
        return CxItemStatus.valueOf(status.name());
    }


    protected List<CxVariationModel> convertVariationList(CustomizationData source)
    {
        return this.variationConverter.convertAll(source.getVariations());
    }


    protected Converter<VariationData, CxVariationModel> getVariationConverter()
    {
        return this.variationConverter;
    }


    public void setVariationConverter(Converter<VariationData, CxVariationModel> variationConverter)
    {
        this.variationConverter = variationConverter;
    }
}
