package de.hybris.platform.adaptivesearch.model.attributes;

import de.hybris.platform.adaptivesearch.model.AbstractAsBoostItemConfigurationModel;
import org.apache.commons.lang.StringUtils;

public class AsBoostItemConfigurationCorruptedAttributeHandler extends AbstractAsAttributeHandler<Boolean, AbstractAsBoostItemConfigurationModel>
{
    public Boolean get(AbstractAsBoostItemConfigurationModel model)
    {
        if(getModelService().isNew(model))
        {
            return Boolean.FALSE;
        }
        if(model.getItem() == null || StringUtils.isBlank(model.getUniqueIdx()))
        {
            return Boolean.TRUE;
        }
        String expectedUniqueIdx = getAsItemModelHelper().generateBoostItemConfigurationUniqueIdx(model);
        String uniqueIdx = model.getUniqueIdx();
        return Boolean.valueOf(!StringUtils.equals(expectedUniqueIdx, uniqueIdx));
    }


    public void set(AbstractAsBoostItemConfigurationModel model, Boolean value)
    {
        throw new UnsupportedOperationException("Write is not a valid operation for this dynamic attribute");
    }
}
