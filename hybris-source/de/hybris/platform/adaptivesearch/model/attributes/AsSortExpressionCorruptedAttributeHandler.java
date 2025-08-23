package de.hybris.platform.adaptivesearch.model.attributes;

import de.hybris.platform.adaptivesearch.model.AsSortExpressionModel;
import org.apache.commons.lang.StringUtils;

public class AsSortExpressionCorruptedAttributeHandler extends AbstractAsAttributeHandler<Boolean, AsSortExpressionModel>
{
    public Boolean get(AsSortExpressionModel model)
    {
        if(getModelService().isNew(model))
        {
            return Boolean.FALSE;
        }
        if(StringUtils.isBlank(model.getUniqueIdx()))
        {
            return Boolean.TRUE;
        }
        String expectedUniqueIdx = getAsItemModelHelper().generateSortExpressionUniqueIdx(model);
        String uniqueIdx = model.getUniqueIdx();
        return Boolean.valueOf(!StringUtils.equals(expectedUniqueIdx, uniqueIdx));
    }


    public void set(AsSortExpressionModel model, Boolean value)
    {
        throw new UnsupportedOperationException("Write is not a valid operation for this dynamic attribute");
    }
}
