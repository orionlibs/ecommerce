package de.hybris.platform.adaptivesearch.model.attributes;

import de.hybris.platform.adaptivesearch.model.AsSimpleSearchConfigurationModel;
import org.apache.commons.lang.StringUtils;

public class AsSimpleSearchConfigurationCorruptedAttributeHandler extends AbstractAsAttributeHandler<Boolean, AsSimpleSearchConfigurationModel>
{
    public Boolean get(AsSimpleSearchConfigurationModel model)
    {
        if(getModelService().isNew(model))
        {
            return Boolean.FALSE;
        }
        if(StringUtils.isBlank(model.getUniqueIdx()))
        {
            return Boolean.TRUE;
        }
        String expectedUniqueIdx = getAsItemModelHelper().generateSimpleSearchConfigurationUniqueIdx(model);
        String uniqueIdx = model.getUniqueIdx();
        return Boolean.valueOf(!StringUtils.equals(expectedUniqueIdx, uniqueIdx));
    }


    public void set(AsSimpleSearchConfigurationModel model, Boolean value)
    {
        throw new UnsupportedOperationException("Write is not a valid operation for this dynamic attribute");
    }
}
