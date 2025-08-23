package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.threadregistry.RegistrableThread;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.JaloSession;
import java.util.concurrent.CountDownLatch;

public class CatalogVersionSyncWorkerThread extends RegistrableThread
{
    private final Tenant tenant;
    private final JaloSession session;
    private final CatalogVersionSyncWorker worker;
    private final CronJob.CronJobThreadSettings cjts;
    private final CountDownLatch startSignal;
    private final CountDownLatch endSignal;


    CatalogVersionSyncWorkerThread(CatalogVersionSyncWorker worker, CountDownLatch startSignal, CountDownLatch endSignal)
    {
        super((Runnable)worker);
        this.worker = worker;
        this.startSignal = startSignal;
        this.endSignal = endSignal;
        this.session = JaloSession.getCurrentSession();
        this.cjts = CronJob.getCronJobThreadSettings();
        this.tenant = this.session.getTenant();
    }


    protected void prepareThread()
    {
        Registry.setCurrentTenant(this.tenant);
        this.session.activate();
        CronJob.activateCronJobThreadSettings(this.cjts);
    }


    protected void unprepareThread()
    {
        JaloSession.deactivate();
        Registry.unsetCurrentTenant();
        CronJob.unsetCronJobThreadSettings(this.cjts);
    }


    public CatalogVersionSyncWorker getWorker()
    {
        return this.worker;
    }


    public void internalRun()
    {
        setName(this.worker.toString());
        try
        {
            this.startSignal.await();
            prepareThread();
            super.internalRun();
        }
        catch(InterruptedException interruptedException)
        {
        }
        finally
        {
            this.endSignal.countDown();
            unprepareThread();
        }
    }
}
