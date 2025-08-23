package de.hybris.platform.servicelayer.web.session.persister;

import de.hybris.platform.core.Tenant;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Required;

public class LinearDrainingAdaptiveAlgorithm implements DrainingAdaptiveAlgorithm
{
    private Tenant tenant;
    private int maxSessionsToPersist;
    private int sessionQueueCapacity;


    @PostConstruct
    public void init()
    {
        this.sessionQueueCapacity = this.tenant.getConfig().getInt("spring.session.save.async.queue.size", 10000);
        this.maxSessionsToPersist = this.tenant.getConfig().getInt("spring.session.save.async.max.items", 1000);
    }


    public int getDrainSize(long currentSize)
    {
        int threshold = this.tenant.getConfig().getInt("spring.session.save.async.threshold.adaptive.algorithm", 40);
        if(shouldAdapt(currentSize, threshold))
        {
            return computeDrainSizeLineary(currentSize, threshold);
        }
        return this.maxSessionsToPersist;
    }


    protected int computeDrainSizeLineary(long currentSize, long threshold)
    {
        int maxratio = this.tenant.getConfig().getInt("spring.session.save.async.maxratio.adaptive.algorithm", 80);
        double xa = threshold / this.sessionQueueCapacity;
        double xb = this.sessionQueueCapacity;
        double ya = this.maxSessionsToPersist;
        double yb = maxratio * this.sessionQueueCapacity / 100.0D;
        double factorA = (ya - yb) / (xa - xb);
        double factorB = ya - factorA * xa;
        long result = Math.round(factorA * currentSize + factorB);
        return Math.toIntExact(result);
    }


    protected boolean shouldAdapt(long currentSize, long threshold)
    {
        boolean useAdaptiveAlg = this.tenant.getConfig().getBoolean("spring.session.save.async.use.adaptive.algorithm", true);
        return (useAdaptiveAlg && (100L * currentSize) / this.sessionQueueCapacity > threshold);
    }


    @Required
    public void setTenant(Tenant tenant)
    {
        this.tenant = tenant;
    }
}
