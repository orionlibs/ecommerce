package de.hybris.platform.adaptivesearch.model.attributes;

import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import org.apache.commons.lang.StringUtils;

public class AsFacetConfigurationCorruptedAttributeHandler extends AbstractAsAttributeHandler<Boolean, AbstractAsFacetConfigurationModel>
{
    public Boolean get(AbstractAsFacetConfigurationModel model)
    {
        if(getModelService().isNew(model))
        {
            return Boolean.FALSE;
        }
        if(StringUtils.isBlank(model.getUniqueIdx()))
        {
            return Boolean.TRUE;
        }
        String expectedUniqueIdx = getAsItemModelHelper().generateFacetConfigurationUniqueIdx(model);
        String uniqueIdx = model.getUniqueIdx();
        return Boolean.valueOf(!StringUtils.equals(expectedUniqueIdx, uniqueIdx));
    }


    public void set(AbstractAsFacetConfigurationModel model, Boolean value)
    {
        throw new UnsupportedOperationException("Write is not a valid operation for this dynamic attribute");
    }
}
