package de.hybris.platform.persistence.audit;

import de.hybris.platform.core.PK;

public interface InternalAuditableOperation extends AuditableOperation
{
    boolean isCreation();


    boolean isDeletion();


    PK getItemPk();
}
