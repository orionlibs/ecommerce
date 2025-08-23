package de.hybris.platform.servicelayer.locking.impl;

import de.hybris.platform.core.locking.LockingNotAllowedException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.directpersistence.CacheInvalidator;
import de.hybris.platform.directpersistence.ChangeSet;
import de.hybris.platform.directpersistence.PersistResult;
import de.hybris.platform.directpersistence.WritePersistenceGateway;
import de.hybris.platform.directpersistence.impl.DefaultChangeSet;
import de.hybris.platform.directpersistence.record.EntityRecord;
import de.hybris.platform.directpersistence.record.impl.PropertyHolder;
import de.hybris.platform.directpersistence.record.impl.UpdateRecord;
import de.hybris.platform.servicelayer.locking.ItemLockingService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Required;

public class DefaultItemLockingService implements ItemLockingService
{
    private UserService userService;
    private WritePersistenceGateway writePersistenceGateway;
    private CacheInvalidator cacheInvalidator;
    private ModelService modelService;


    public void lock(ItemModel item)
    {
        execute(item, true);
    }


    private void execute(ItemModel item, boolean isLocked)
    {
        validateCanLockItem(item);
        updateDirectlyAndInvalidate(item, isLocked);
        this.modelService.refresh(item);
    }


    private void updateDirectlyAndInvalidate(ItemModel item, boolean blocked)
    {
        DefaultChangeSet changeSet = new DefaultChangeSet();
        changeSet.add(new EntityRecord[] {(EntityRecord)new UpdateRecord(item
                        .getPk(), item.getItemtype(), item.getItemModelContext().getPersistenceVersion(), false, new HashSet(
                        Arrays.asList((Object[])new PropertyHolder[] {new PropertyHolder("sealed".intern(), Boolean.valueOf(blocked))})))});
        Collection<PersistResult> persistResult = this.writePersistenceGateway.persist((ChangeSet)changeSet);
        this.cacheInvalidator.invalidate(persistResult);
    }


    public void unlock(ItemModel item)
    {
        execute(item, false);
    }


    private void validateCanLockItem(ItemModel item)
    {
        if(item.getPk() == null)
        {
            throw new LockingNotAllowedException("Item without PK cannot be locked/unlocked");
        }
        if(!this.userService.isMemberOfGroup(this.userService.getCurrentUser(), this.userService.getUserGroupForUID("itemLockingGroup"), true))
        {
            throw new LockingNotAllowedException("Current user (" + this.userService
                            .getCurrentUser()
                            .getPk() + ") is not allowed to lock/unlock item: " + item.getPk());
        }
    }


    public boolean isLocked(ItemModel item)
    {
        return item.isSealed();
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setWritePersistenceGateway(WritePersistenceGateway writePersistenceGateway)
    {
        this.writePersistenceGateway = writePersistenceGateway;
    }


    @Required
    public void setCacheInvalidator(CacheInvalidator cacheInvalidator)
    {
        this.cacheInvalidator = cacheInvalidator;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void lockAll(Collection<ItemModel> items)
    {
        for(ItemModel itemModel : items)
        {
            lock(itemModel);
        }
    }


    public void unlockAll(Collection<ItemModel> items)
    {
        for(ItemModel itemModel : items)
        {
            unlock(itemModel);
        }
    }
}
