package de.hybris.platform.personalizationfacades.variation.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.personalizationfacades.data.VariationData;
import de.hybris.platform.personalizationservices.enums.CxItemStatus;
import de.hybris.platform.personalizationservices.model.CxVariationModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class VariationReversePopulator implements Populator<VariationData, CxVariationModel>
{
    public void populate(VariationData source, CxVariationModel target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        if(StringUtils.isEmpty(target.getCode()))
        {
            target.setCode(source.getCode());
        }
        target.setName(source.getName());
        target.setStatus(getItemStatus(source));
        target.setEnabled((target.getStatus() == CxItemStatus.ENABLED));
        target.setRank(source.getRank());
    }


    protected CxItemStatus getItemStatus(VariationData source)
    {
        if(source.getStatus() == null)
        {
            return Boolean.TRUE.equals(source.getEnabled()) ? CxItemStatus.ENABLED : CxItemStatus.DISABLED;
        }
        return CxItemStatus.valueOf(source.getStatus().name());
    }
}
