package de.hybris.platform.servicelayer.user.daos;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.AbstractUserAuditModel;
import java.util.List;

public interface UserAuditDao
{
    List<AbstractUserAuditModel> getUserAudit(PK paramPK);
}
