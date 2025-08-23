package de.hybris.platform.persistence.audit;

public interface AuditChangeFilter
{
    boolean ignoreAudit(AuditableChange paramAuditableChange);
}
