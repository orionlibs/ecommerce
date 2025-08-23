package de.hybris.platform.servicelayer.impex.impl;

import de.hybris.platform.core.enums.EncodingEnum;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.interceptor.InitDefaultsInterceptor;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.util.CSVConstants;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class ImpExMediaInitDefaultsInterceptor implements InitDefaultsInterceptor
{
    private static final Logger LOG = Logger.getLogger(ImpExMediaInitDefaultsInterceptor.class);
    private KeyGenerator mediaCodeGenerator;
    private MediaService mediaService;


    public void onInitDefaults(Object model, InterceptorContext ctx) throws InterceptorException
    {
        if(model instanceof ImpExMediaModel)
        {
            ImpExMediaModel mediaModel = (ImpExMediaModel)model;
            mediaModel.setCode((String)this.mediaCodeGenerator.generate());
            mediaModel.setEncoding(EncodingEnum.valueOf(CSVConstants.DEFAULT_ENCODING));
            mediaModel.setRealfilename(mediaModel.getCode());
            mediaModel.setFieldSeparator(Character.valueOf(CSVConstants.DEFAULT_FIELD_SEPARATOR));
            mediaModel.setCommentCharacter(Character.valueOf('#'));
            mediaModel.setQuoteCharacter(Character.valueOf(CSVConstants.DEFAULT_QUOTE_CHARACTER));
            try
            {
                mediaModel.setFolder(this.mediaService.getFolder("impex"));
            }
            catch(UnknownIdentifierException e)
            {
                LOG.warn("Can not get impex media folder, using root folder instead.");
                mediaModel.setFolder(this.mediaService.getRootFolder());
            }
        }
    }


    @Required
    public void setMediaCodeGenerator(KeyGenerator mediaCodeGenerator)
    {
        this.mediaCodeGenerator = mediaCodeGenerator;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
