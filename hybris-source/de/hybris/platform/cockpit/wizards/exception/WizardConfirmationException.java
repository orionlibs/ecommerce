package de.hybris.platform.cockpit.wizards.exception;

public class WizardConfirmationException extends RuntimeException
{
    private String frontendeLocalizedMessage;


    public String getFrontendeLocalizedMessage()
    {
        return this.frontendeLocalizedMessage;
    }


    public void setFrontendeLocalizedMessage(String frontendeLocalizedMessage)
    {
        this.frontendeLocalizedMessage = frontendeLocalizedMessage;
    }


    public WizardConfirmationException()
    {
    }


    public WizardConfirmationException(Throwable exception)
    {
        super(exception);
    }
}
