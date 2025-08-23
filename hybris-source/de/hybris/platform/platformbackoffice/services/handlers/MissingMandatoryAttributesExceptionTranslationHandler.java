package de.hybris.platform.platformbackoffice.services.handlers;

import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.servicelayer.interceptor.impl.MandatoryAttributesValidator;
import java.util.HashSet;
import java.util.Set;

public class MissingMandatoryAttributesExceptionTranslationHandler extends AbstractExceptionTranslationHandler
{
    protected static final String LABEL_KEY = "exceptiontranslation.mandatoryattributes.error";
    private LabelService labelService;


    protected Class<? extends Throwable> getExceptionClass()
    {
        return (Class)MandatoryAttributesValidator.MissingMandatoryAttributesException.class;
    }


    public String toString(Throwable throwable)
    {
        if(throwable instanceof MandatoryAttributesValidator.MissingMandatoryAttributesException)
        {
            return getLocalizedMessage((MandatoryAttributesValidator.MissingMandatoryAttributesException)throwable);
        }
        return (throwable.getCause() == null) ? null : toString(throwable.getCause());
    }


    private String getLocalizedMessage(MandatoryAttributesValidator.MissingMandatoryAttributesException e)
    {
        Set<String> localizedAttribute = new HashSet<>();
        for(String attr : e.getMissingAttributes())
        {
            localizedAttribute.add(this.labelService.getObjectLabel(e.getModel().getItemtype() + "." + e.getModel().getItemtype()));
        }
        return getLocalizedMessage("exceptiontranslation.mandatoryattributes.error", new Object[] {localizedAttribute, this.labelService.getObjectLabel(e.getModel().getItemtype())});
    }


    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }
}
