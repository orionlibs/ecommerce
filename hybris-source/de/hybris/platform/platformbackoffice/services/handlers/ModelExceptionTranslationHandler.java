package de.hybris.platform.platformbackoffice.services.handlers;

import com.hybris.cockpitng.service.ExceptionTranslationHandler;
import org.zkoss.util.resource.Labels;

@Deprecated(since = "2205", forRemoval = true)
public abstract class ModelExceptionTranslationHandler implements ExceptionTranslationHandler
{
    public static final String I18N_UNEXPECTED_DB_ERROR = "exceptiontranslation.unexpected.db.error";


    protected String translateByCause(Throwable throwable)
    {
        if(throwable instanceof java.sql.SQLDataException)
        {
            return getLabelByKey("exceptiontranslation.unexpected.db.error");
        }
        if(throwable instanceof de.hybris.platform.persistence.hjmp.HJMPException || throwable instanceof org.springframework.dao.DataIntegrityViolationException || throwable instanceof de.hybris.platform.jalo.JaloSystemException)
        {
            return translateByCause(throwable.getCause());
        }
        return null;
    }


    protected String getLabelByKey(String key)
    {
        return Labels.getLabel(key);
    }
}
