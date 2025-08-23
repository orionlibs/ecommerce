package de.hybris.platform.adaptivesearch.model.attributes;

import de.hybris.platform.adaptivesearch.model.AsCategoryAwareSearchConfigurationModel;
import org.apache.commons.lang.StringUtils;

public class AsCategoryAwareSearchConfigurationCorruptedAttributeHandler extends AbstractAsAttributeHandler<Boolean, AsCategoryAwareSearchConfigurationModel>
{
    public Boolean get(AsCategoryAwareSearchConfigurationModel model)
    {
        if(getModelService().isNew(model))
        {
            return Boolean.FALSE;
        }
        if(StringUtils.isBlank(model.getUniqueIdx()))
        {
            return Boolean.TRUE;
        }
        String expectedUniqueIdx = getAsItemModelHelper().generateCategoryAwareSearchConfigurationUniqueIdx(model);
        String uniqueIdx = model.getUniqueIdx();
        return Boolean.valueOf(!StringUtils.equals(expectedUniqueIdx, uniqueIdx));
    }


    public void set(AsCategoryAwareSearchConfigurationModel model, Boolean value)
    {
        throw new UnsupportedOperationException("Write is not a valid operation for this dynamic attribute");
    }
}
