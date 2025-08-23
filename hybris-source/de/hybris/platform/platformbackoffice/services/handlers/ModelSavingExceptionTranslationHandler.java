package de.hybris.platform.platformbackoffice.services.handlers;

@Deprecated(since = "2205", forRemoval = true)
public class ModelSavingExceptionTranslationHandler extends ModelExceptionTranslationHandler
{
    public static final String I18N_UNEXPECTED_UPDATE_ERROR = "exceptiontranslation.unexpected.update.error";


    public boolean canHandle(Throwable exception)
    {
        return (exception instanceof de.hybris.platform.servicelayer.exceptions.ModelSavingException || (exception instanceof com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException && exception
                        .getCause() instanceof de.hybris.platform.servicelayer.exceptions.ModelSavingException));
    }


    public String toString(Throwable exception)
    {
        return getLabelByKey("exceptiontranslation.unexpected.update.error");
    }
}
