package de.hybris.platform.platformbackoffice.services.handlers;

public class SimpleExceptionTranslationHandler extends AbstractExceptionTranslationHandler
{
    private String labelKey;
    private Class<? extends Throwable> exceptionClass;


    protected Class<? extends Throwable> getExceptionClass()
    {
        return this.exceptionClass;
    }


    public String toString(Throwable throwable)
    {
        if(this.labelKey != null)
        {
            return getLocalizedMessage(this.labelKey, new Object[0]);
        }
        return null;
    }


    public void setLabelKey(String labelKey)
    {
        this.labelKey = labelKey;
    }


    public void setExceptionClass(Class<? extends Throwable> exceptionClass)
    {
        this.exceptionClass = exceptionClass;
    }
}
