package de.hybris.platform.servicelayer.interceptor.impl;

import de.hybris.platform.core.model.media.MediaContextModel;
import de.hybris.platform.core.model.media.MediaFormatMappingModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.ValidateInterceptor;
import java.util.HashMap;
import java.util.Map;

public class MediaContextValidator implements ValidateInterceptor
{
    public void onValidate(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof MediaContextModel)
        {
            MediaContextModel mediaContextModel = (MediaContextModel)model;
            if(mediaContextModel.getMappings() != null)
            {
                Map<MediaFormatModel, MediaFormatMappingModel> duplicatedSources = new HashMap<>(mediaContextModel.getMappings().size());
                for(MediaFormatMappingModel currentMapping : mediaContextModel.getMappings())
                {
                    if(currentMapping.getSource() != null)
                    {
                        if(duplicatedSources.containsKey(currentMapping.getSource()))
                        {
                            throw new InterceptorException("Two mappings " + currentMapping + " and " + duplicatedSources
                                            .get(currentMapping
                                                            .getSource()) + " models have the same source format " + currentMapping
                                            .getSource()
                                            .getQualifier() + " in media context " + mediaContextModel
                                            .getQualifier());
                        }
                        duplicatedSources.put(currentMapping.getSource(), currentMapping);
                    }
                }
            }
        }
    }
}
