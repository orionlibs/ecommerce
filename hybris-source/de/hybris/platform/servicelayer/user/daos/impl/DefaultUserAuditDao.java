package de.hybris.platform.servicelayer.user.daos.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.AbstractUserAuditModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.daos.UserAuditDao;
import java.util.List;
import java.util.Map;

public class DefaultUserAuditDao implements UserAuditDao
{
    private FlexibleSearchService flexibleSearchService;
    private static final String AUDIT_BY_PK_QRY = "SELECT {PK} FROM {AbstractUserAudit} WHERE {userPK} = ?userPk ORDER BY {creationtime} DESC";


    public List<AbstractUserAuditModel> getUserAudit(PK userPK)
    {
        return this.flexibleSearchService.search("SELECT {PK} FROM {AbstractUserAudit} WHERE {userPK} = ?userPk ORDER BY {creationtime} DESC", (Map)ImmutableMap.of("userPk", userPK))
                        .getResult();
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
