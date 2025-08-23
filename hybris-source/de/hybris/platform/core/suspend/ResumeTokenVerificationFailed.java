package de.hybris.platform.core.suspend;

public class ResumeTokenVerificationFailed extends RuntimeException
{
    public ResumeTokenVerificationFailed()
    {
        super("Provided token is not valid.");
    }
}
