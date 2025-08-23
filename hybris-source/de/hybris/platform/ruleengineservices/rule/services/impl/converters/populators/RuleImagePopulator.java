package de.hybris.platform.ruleengineservices.rule.services.impl.converters.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.ruleengineservices.rule.data.ImageData;
import org.springframework.util.Assert;

public class RuleImagePopulator implements Populator<MediaModel, ImageData>
{
    public void populate(MediaModel source, ImageData target)
    {
        Assert.notNull(source, "Parameter source cannot be null.");
        Assert.notNull(target, "Parameter target cannot be null.");
        target.setUrl(source.getURL());
        target.setAltText(source.getAltText());
        if(source.getMediaFormat() != null)
        {
            target.setFormat(source.getMediaFormat().getQualifier());
        }
    }
}
