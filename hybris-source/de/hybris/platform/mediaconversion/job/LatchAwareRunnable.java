package de.hybris.platform.mediaconversion.job;

import java.util.concurrent.CountDownLatch;

class LatchAwareRunnable implements Runnable
{
    private final Runnable runnable;
    private final CountDownLatch latch;


    LatchAwareRunnable(CountDownLatch latch, Runnable runnable)
    {
        this.latch = latch;
        if(this.latch == null)
        {
            throw new IllegalArgumentException("Latch must not be null.");
        }
        this.runnable = runnable;
        if(this.runnable == null)
        {
            throw new IllegalArgumentException("Runnable delete must not be null.");
        }
    }


    public void run()
    {
        try
        {
            this.runnable.run();
        }
        finally
        {
            this.latch.countDown();
        }
    }
}
