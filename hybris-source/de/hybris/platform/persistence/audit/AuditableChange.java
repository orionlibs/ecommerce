package de.hybris.platform.persistence.audit;

import de.hybris.platform.directpersistence.cache.SLDDataContainer;

public class AuditableChange
{
    private final SLDDataContainer before;
    private final SLDDataContainer after;


    public AuditableChange(SLDDataContainer before, SLDDataContainer after)
    {
        this.before = before;
        this.after = after;
    }


    public boolean isMeaningful()
    {
        return (getVersion(this.before) != getVersion(this.after));
    }


    public SLDDataContainer getBefore()
    {
        return this.before;
    }


    public SLDDataContainer getAfter()
    {
        return this.after;
    }


    private static long getVersion(SLDDataContainer container)
    {
        if(container == null)
        {
            return Long.MIN_VALUE;
        }
        return container.getVersion();
    }
}
