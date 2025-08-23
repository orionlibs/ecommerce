package de.hybris.platform.persistence.audit.gateway;

public class TargetLinkSearchRule extends GenericSearchRule<Long>
{
    public static final String FIELD_NAME = "targetpk";


    public TargetLinkSearchRule(Long pkLongValue)
    {
        super("targetpk", pkLongValue, false);
    }
}
