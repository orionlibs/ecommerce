package de.hybris.platform.core.suspend;

public class ResumeOptions
{
    private final String resumeToken;


    private ResumeOptions(Builder builder)
    {
        this.resumeToken = builder.resumeToken;
    }


    public String getResumeToken()
    {
        return this.resumeToken;
    }


    public static Builder builder()
    {
        return new Builder();
    }
}
