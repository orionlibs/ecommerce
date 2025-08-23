package de.hybris.platform.persistence.audit.gateway;

public interface AuditSqlQueryFactory
{
    AuditSqlQuery createSqlQuery(AuditSearchQuery paramAuditSearchQuery);


    AuditSqlQuery createStandardSqlQuery(AuditSearchQuery paramAuditSearchQuery);
}
