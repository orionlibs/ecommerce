package de.hybris.platform.persistence.audit.gateway;

public class SourceLinkSearchRule extends GenericSearchRule<Long>
{
    public static final String FIELD_NAME = "sourcepk";


    public SourceLinkSearchRule(Long pkLongValue)
    {
        super("sourcepk", pkLongValue, false);
    }
}
