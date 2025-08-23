package de.hybris.platform.servicelayer.web.session.persister;

import de.hybris.platform.core.model.web.StoredHttpSessionModel;
import de.hybris.platform.servicelayer.web.session.PersistedSession;
import java.io.Serializable;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractSessionPersister
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSessionPersister.class);


    protected boolean updateModelIfNecessary(StoredHttpSessionModel model, PersistedSession session)
    {
        if(model.getItemModelContext().isRemoved())
        {
            return false;
        }
        byte[] payload = SerializationUtils.serialize((Serializable)session);
        if(model.getItemModelContext().isNew())
        {
            model.setClusterId(Integer.valueOf(session.getClusterId()));
            model.setExtension(session.getExtension());
            model.setContextRoot(session.getContextRoot());
        }
        else if(ArrayUtils.isEquals(payload, model.getSerializedSession()))
        {
            LOG.debug("No need to save session {} since content is the same.", session.getId());
            return false;
        }
        model.setSerializedSession(payload);
        return true;
    }
}
