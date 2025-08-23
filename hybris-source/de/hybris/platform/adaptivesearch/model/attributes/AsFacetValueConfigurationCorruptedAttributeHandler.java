package de.hybris.platform.adaptivesearch.model.attributes;

import de.hybris.platform.adaptivesearch.model.AbstractAsFacetValueConfigurationModel;
import org.apache.commons.lang.StringUtils;

public class AsFacetValueConfigurationCorruptedAttributeHandler extends AbstractAsAttributeHandler<Boolean, AbstractAsFacetValueConfigurationModel>
{
    public Boolean get(AbstractAsFacetValueConfigurationModel model)
    {
        if(getModelService().isNew(model))
        {
            return Boolean.FALSE;
        }
        if(StringUtils.isBlank(model.getUniqueIdx()))
        {
            return Boolean.TRUE;
        }
        String expectedUniqueIdx = getAsItemModelHelper().generateFacetValueConfigurationUniqueIdx(model);
        String uniqueIdx = model.getUniqueIdx();
        return Boolean.valueOf(!StringUtils.equals(expectedUniqueIdx, uniqueIdx));
    }


    public void set(AbstractAsFacetValueConfigurationModel model, Boolean value)
    {
        throw new UnsupportedOperationException("Write is not a valid operation for this dynamic attribute");
    }
}
