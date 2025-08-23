package de.hybris.platform.mediaconversion.web.facades;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.servicelayer.exceptions.ModelLoadingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

public interface OnDemandConversionFacade
{
    public static final String BEAN_NAME = "onDemandConversionFacade";


    String convert(PK paramPK, String paramString) throws ModelLoadingException, UnknownIdentifierException;


    String retrieveURL(MediaContainerModel paramMediaContainerModel, String paramString) throws UnknownIdentifierException;


    String retrieveURL(MediaContainerModel paramMediaContainerModel, ConversionMediaFormatModel paramConversionMediaFormatModel);


    String convert(MediaContainerModel paramMediaContainerModel, ConversionMediaFormatModel paramConversionMediaFormatModel);


    String convert(MediaContainerModel paramMediaContainerModel, String paramString);
}
