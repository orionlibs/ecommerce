package de.hybris.platform.persistence.audit.gateway;

import java.util.stream.Stream;

public interface ReadAuditGateway
{
    <T extends AuditRecord> Stream<T> search(AuditSearchQuery paramAuditSearchQuery);
}
