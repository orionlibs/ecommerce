package de.hybris.platform.testframework.runlistener;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloObjectNoLongerValidException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LItem;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.PolyglotPersistenceGenericItemSupport;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.persistence.framework.PersistencePool;
import de.hybris.platform.persistence.hjmp.HJMPUtils;
import de.hybris.platform.testframework.ItemCreationLifecycleListener;
import de.hybris.platform.testframework.Transactional;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.jeeapi.YNoSuchEntityException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.apache.log4j.Logger;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import org.springframework.context.ApplicationContext;

public class ItemCreationListener extends RunListener implements PersistencePool.PersistenceListener
{
    private static final Logger log = Logger.getLogger(ItemCreationListener.class);
    public static final String ITEM_CREATION_LIFECYCLE_LISTENER = "ItemCreationLifecycleListener";
    private final Set<PK> createdPKs = Collections.synchronizedSet(new LinkedHashSet<>());
    private volatile boolean taEnabled;
    private volatile Thread junitThread;


    public void testStarted(Description description) throws Exception
    {
        this.junitThread = Thread.currentThread();
        this.createdPKs.clear();
        getItemCreationLifecycleListener().ifPresent(l -> l.registerPersistenceListener(this));
        Transactional anno = (description != null) ? (Transactional)description.getAnnotation(Transactional.class) : null;
        this.taEnabled = (anno != null && transactionsSupported());
        if(log.isDebugEnabled())
        {
            if(this.taEnabled)
            {
                log.debug("Disabling item registration in junit thread for " + description + " since transaction is enabled");
            }
            else
            {
                log.debug("Enabling item registration for " + description + " since transaction is not enabled");
            }
        }
    }


    public void testFinished(Description description) throws Exception
    {
        getItemCreationLifecycleListener().ifPresent(l -> l.unregisterPersistenceListener(this));
        Object pkLookupRestoreToken = HJMPUtils.disablePKLookupRetry();
        try
        {
            if(!this.createdPKs.isEmpty())
            {
                if(this.taEnabled)
                {
                    Tenant t = Registry.getCurrentTenant();
                    JaloSession js = JaloSession.getCurrentSession();
                    Set<PK> toRemove = this.createdPKs;
                    Object object = new Object(this, t, js, toRemove);
                    RegistrableThread registrableThread = new RegistrableThread((Runnable)object, "HybrisJunit4RemovalThread");
                    registrableThread.start();
                    registrableThread.join();
                }
                else
                {
                    try
                    {
                        SessionContext localctx = JaloSession.getCurrentSession().createLocalSessionContext();
                        localctx.setUser((User)UserManager.getInstance().getAdminEmployee());
                        removeItems(this.createdPKs);
                    }
                    finally
                    {
                        JaloSession.getCurrentSession().removeLocalSessionContext();
                    }
                }
            }
        }
        catch(Exception e)
        {
            log.error("unexpected error removing items: " + e.getMessage(), e);
        }
        finally
        {
            this.createdPKs.clear();
            this.junitThread = null;
            this.taEnabled = false;
            HJMPUtils.restorPKLookupRetry(pkLookupRestoreToken);
        }
    }


    private Optional<ItemCreationLifecycleListener> getItemCreationLifecycleListener()
    {
        ApplicationContext ctx = Registry.getCoreApplicationContext();
        if(ctx.containsBean("ItemCreationLifecycleListener"))
        {
            return Optional.of((ItemCreationLifecycleListener)ctx.getBean("ItemCreationLifecycleListener"));
        }
        return Optional.empty();
    }


    protected void removeItems(Collection<PK> pks)
    {
        JaloSession js = JaloSession.getCurrentSession();
        if(log.isDebugEnabled())
        {
            log.debug("starting to remove test " + pks.size() + " items ");
        }
        List<PK> toBeRemoved = new LinkedList<>(pks);
        Map<PK, Exception> itemRemovalErrors = new HashMap<>();
        Collections.reverse(toBeRemoved);
        if(log.isDebugEnabled())
        {
            log.debug("reversed to remove collection  " + toBeRemoved.size());
        }
        boolean removedAtLeastOne = false;
        int counter = 1;
        long start = System.currentTimeMillis();
        do
        {
            if(log.isDebugEnabled())
            {
                log.debug("removing test  items  (turn " + counter + ")...");
            }
            try
            {
                SessionContext ctx = JaloSession.getCurrentSession().createLocalSessionContext();
                ctx.setAttribute("disableItemCheckBeforeRemovable", Boolean.TRUE);
                removedAtLeastOne = false;
                for(ListIterator<PK> iter = toBeRemoved.listIterator(); iter.hasNext(); )
                {
                    Item item = null;
                    PK pk = iter.next();
                    try
                    {
                        if(pk != null && 55 != pk.getTypeCode())
                        {
                            try
                            {
                                item = js.getItem(pk);
                            }
                            catch(JaloItemNotFoundException e1)
                            {
                                log.debug("Can't remove " + pk + " reason (missing jalo item), ", (Throwable)e1);
                                iter.remove();
                                continue;
                            }
                            catch(YNoSuchEntityException e)
                            {
                                log.debug("Can't remove " + pk + " reason (no entity), ", (Throwable)e);
                                iter.remove();
                                continue;
                            }
                            catch(Exception e)
                            {
                                log.warn("unexpected error resolving pk " + pk + " for item to be removed : " + e.getMessage(), e);
                                iter.remove();
                                continue;
                            }
                            if(!item.isAlive())
                            {
                                iter.remove();
                                continue;
                            }
                            prepareItem(item);
                            item.remove(ctx);
                            getWriteAuditGateway().removeAuditRecordsForType(item.getComposedType().getCode(), item.getPK());
                            removedAtLeastOne = true;
                        }
                        iter.remove();
                    }
                    catch(Exception e)
                    {
                        if(yNoSuchEntityException(pk, e) || jaloObjectNoLongerValidException(pk, e))
                        {
                            iter.remove();
                            continue;
                        }
                        if(item != null && !item.isAlive())
                        {
                            iter.remove();
                            continue;
                        }
                        itemRemovalErrors.put(pk, e);
                    }
                }
            }
            finally
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
            if(log.isDebugEnabled())
            {
                log.debug("done removing test items (turn " + counter + ") in (" +
                                System.currentTimeMillis() - start + " ms).");
            }
            counter++;
        }
        while(!toBeRemoved.isEmpty() && removedAtLeastOne);
        if(!toBeRemoved.isEmpty())
        {
            log.error("--------------------------------------");
            for(PK pk : toBeRemoved)
            {
                Exception exception = itemRemovalErrors.get(pk);
                log.error("item " + pk + " has not been removed due to " + exception.getMessage(), exception);
            }
            log.error("--------------------------------------");
        }
    }


    private boolean jaloObjectNoLongerValidException(PK pk, Exception e)
    {
        return (pk != null && e instanceof JaloObjectNoLongerValidException && pk
                        .equals(((JaloObjectNoLongerValidException)e).getJaloObjectPK()));
    }


    private boolean yNoSuchEntityException(PK pk, Exception e)
    {
        return (pk != null && e instanceof YNoSuchEntityException && pk.equals(((YNoSuchEntityException)e).getPk()));
    }


    private WriteAuditGateway getWriteAuditGateway()
    {
        return (WriteAuditGateway)Registry.getApplicationContext().getBean("writeAuditGateway", WriteAuditGateway.class);
    }


    protected void prepareItem(Item item) throws ConsistencyCheckException
    {
        if(item instanceof C2LItem)
        {
            if(item instanceof Currency)
            {
                checkAndFixLastCurrencyRemoving((Currency)item);
            }
            setInactive((C2LItem)item);
            if(item instanceof Language)
            {
                checkAndFixSessionLanguageRemoving((Language)item);
            }
        }
        else if(item instanceof AttributeDescriptor)
        {
            setRemovable((AttributeDescriptor)item);
        }
    }


    private void setRemovable(AttributeDescriptor item)
    {
        item.setRemovable(true);
    }


    private void setInactive(C2LItem item) throws ConsistencyCheckException
    {
        item.setActive(false);
    }


    private void checkAndFixSessionLanguageRemoving(Language item)
    {
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        if(item.equals(ctx.getLanguage()))
        {
            try
            {
                ctx.setLanguage(C2LManager.getInstance().getLanguageByIsoCode("---"));
            }
            catch(JaloItemNotFoundException e)
            {
                Collection<Language> all = C2LManager.getInstance().getAllLanguages();
                if(all.isEmpty() || (all.size() == 1 && all.contains(item)))
                {
                    ctx.setLanguage(null);
                }
                else
                {
                    for(Language l : all)
                    {
                        if(l.equals(item))
                        {
                            continue;
                        }
                        ctx.setLanguage(l);
                    }
                }
            }
        }
    }


    private void checkAndFixLastCurrencyRemoving(Currency item) throws ConsistencyCheckException
    {
        Collection<Currency> allCurs = C2LManager.getInstance().getAllCurrencies();
        if(allCurs.size() == 1 && allCurs.contains(item))
        {
            createDefaultCurrencyAsBase();
        }
        else if(item.isBase().booleanValue())
        {
            try
            {
                Currency defaultCurr = C2LManager.getInstance().getCurrencyByIsoCode("---");
                defaultCurr.setBase();
            }
            catch(JaloItemNotFoundException e)
            {
                createDefaultCurrencyAsBase();
            }
        }
    }


    private void createDefaultCurrencyAsBase() throws ConsistencyCheckException
    {
        Currency defCur = C2LManager.getInstance().createCurrency("---");
        defCur.setActive(true);
        defCur.setBase();
    }


    public void entityCreated(PK pk)
    {
        if(!this.taEnabled ||
                        !Thread.currentThread().equals(this.junitThread) || !PolyglotPersistenceGenericItemSupport.isBackedByTransactionalRepository(pk))
        {
            this.createdPKs.add(pk);
        }
    }


    protected boolean transactionsSupported()
    {
        return (!Config.isMySQLUsed() || !"myisam".equalsIgnoreCase(Config.getString("mysql.tabletype", "innodb")));
    }
}
