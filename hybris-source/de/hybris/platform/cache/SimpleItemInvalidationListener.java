package de.hybris.platform.cache;

import de.hybris.platform.core.PK;

public abstract class SimpleItemInvalidationListener implements InvalidationListener
{
    private boolean started;


    public synchronized void start(Cache cache)
    {
        if(!this.started)
        {
            InvalidationManager invMan = cache.getTenant().getInvalidationManager();
            InvalidationTopic topic = invMan.getOrCreateInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
            topic.addInvalidationListener(this);
            this.started = true;
        }
    }


    public void keyInvalidated(Object[] key, int invalidationType, InvalidationTarget target, RemoteInvalidationSource remoteSrc)
    {
        PK pk = (PK)key[3];
        if(invalidationType == 2)
        {
            itemModified(target, pk, true);
        }
        else
        {
            itemModified(target, pk, false);
        }
    }


    protected void invalidate(InvalidationTarget target, Object[] key, boolean removed)
    {
        target.invalidate(key,
                        removed ? 2 : 1);
    }


    public abstract void itemModified(InvalidationTarget paramInvalidationTarget, PK paramPK, boolean paramBoolean);
}
