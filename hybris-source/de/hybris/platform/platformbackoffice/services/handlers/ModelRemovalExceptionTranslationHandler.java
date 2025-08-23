package de.hybris.platform.platformbackoffice.services.handlers;

@Deprecated(since = "2205", forRemoval = true)
public class ModelRemovalExceptionTranslationHandler extends ModelExceptionTranslationHandler
{
    public static final String I18N_UNEXPECTED_REMOVE_ERROR = "exceptiontranslation.unexpected.remove.error";


    public boolean canHandle(Throwable exception)
    {
        return (exception instanceof de.hybris.platform.servicelayer.exceptions.ModelRemovalException || (exception instanceof com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectDeletionException && exception
                        .getCause() instanceof de.hybris.platform.servicelayer.exceptions.ModelRemovalException));
    }


    public String toString(Throwable exception)
    {
        return getLabelByKey("exceptiontranslation.unexpected.remove.error");
    }
}
