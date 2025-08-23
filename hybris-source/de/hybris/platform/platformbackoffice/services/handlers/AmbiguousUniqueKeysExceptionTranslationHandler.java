package de.hybris.platform.platformbackoffice.services.handlers;

import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.servicelayer.interceptor.impl.UniqueAttributesInterceptor;
import java.util.HashSet;
import java.util.Set;

public class AmbiguousUniqueKeysExceptionTranslationHandler extends AbstractExceptionTranslationHandler
{
    protected static final String LABEL_KEY = "exceptiontranslation.uniquekeys.error";
    private LabelService labelService;


    protected Class<? extends Throwable> getExceptionClass()
    {
        return (Class)UniqueAttributesInterceptor.AmbiguousUniqueKeysException.class;
    }


    public String toString(Throwable throwable)
    {
        if(throwable instanceof UniqueAttributesInterceptor.AmbiguousUniqueKeysException)
        {
            return getLocalizedMessage((UniqueAttributesInterceptor.AmbiguousUniqueKeysException)throwable);
        }
        return (throwable.getCause() == null) ? null : toString(throwable.getCause());
    }


    private String getLocalizedMessage(UniqueAttributesInterceptor.AmbiguousUniqueKeysException e)
    {
        Set<String> localizedAttribute = new HashSet<>();
        for(String attr : e.getAttributesKeys())
        {
            localizedAttribute.add(this.labelService.getObjectLabel(e.getModel().getItemtype() + "." + e.getModel().getItemtype()));
        }
        return getLocalizedMessage("exceptiontranslation.uniquekeys.error", new Object[] {localizedAttribute, this.labelService.getObjectLabel(e.getModel().getItemtype())});
    }


    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }
}
