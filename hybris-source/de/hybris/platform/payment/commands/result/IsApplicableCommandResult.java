package de.hybris.platform.payment.commands.result;

public class IsApplicableCommandResult
{
    private final boolean applicable;


    public IsApplicableCommandResult(boolean applicable)
    {
        this.applicable = applicable;
    }


    public boolean isApplicable()
    {
        return this.applicable;
    }
}
