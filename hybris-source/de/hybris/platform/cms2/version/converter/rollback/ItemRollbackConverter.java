package de.hybris.platform.cms2.version.converter.rollback;

import de.hybris.platform.cms2.exceptions.ItemRollbackException;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.persistence.audit.payload.json.AuditPayload;
import java.util.function.Predicate;

public interface ItemRollbackConverter
{
    Predicate<ItemModel> getConstrainedBy();


    ItemModel rollbackItem(ItemModel paramItemModel, CMSVersionModel paramCMSVersionModel, AuditPayload paramAuditPayload) throws ItemRollbackException;
}
