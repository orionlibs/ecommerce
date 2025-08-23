package de.hybris.platform.servicelayer.internal.model.extractor.impl;

import de.hybris.platform.persistence.framework.EntityInstance;
import de.hybris.platform.servicelayer.internal.model.extractor.PersistenceTypeService;
import de.hybris.platform.servicelayer.internal.model.impl.PersistenceType;
import de.hybris.platform.servicelayer.internal.model.impl.wrapper.ModelWrapper;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.persistence.PersistenceUtils;
import java.util.Collection;
import org.apache.log4j.Logger;

public class DefaultPersistenceTypeService implements PersistenceTypeService
{
    private static final Logger LOG = Logger.getLogger(DefaultPersistenceTypeService.class.getName());
    public static final String PERSISTENCE_LEGACY_MODE = "persistence.legacy.mode";


    public static boolean getLegacyPersistenceGlobalSettingFromConfig()
    {
        return PersistenceUtils.isPersistenceLagacyModeEnabledInConfig();
    }


    public PersistenceType getPersistenceType(Collection<ModelWrapper> wrappers)
    {
        if(PersistenceUtils.isPersistenceLegacyModeEnabled())
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Legacy persistence is configured globally");
            }
            return PersistenceType.JALO;
        }
        for(ModelWrapper wrapper : wrappers)
        {
            if(wrapper.isConfiguredForLegacyPersistence())
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("At least one wrapper in the changeset is configured for legacy persistence: " + wrapper);
                }
                return PersistenceType.JALO;
            }
            if(Transaction.current().isRunning())
            {
                EntityInstance entityInstance = Transaction.current().getAttachedEntityInstance(wrapper.getPk());
                if(entityInstance != null && entityInstance.needsStoring())
                {
                    return PersistenceType.JALO;
                }
            }
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Whole changeset is configured for direct persistence");
        }
        return PersistenceType.DIRECT;
    }
}
