package de.hybris.platform.cronjob.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.SingletonCreator;

@Deprecated(since = "5.5.1", forRemoval = false)
public class TimerTaskUtils
{
    private static final SingletonCreator.Creator<TimerTaskUtils> SINGLETON_CREATOR = (SingletonCreator.Creator<TimerTaskUtils>)new Object();


    public static TimerTaskUtils getInstance()
    {
        return (TimerTaskUtils)Registry.getSingleton(SINGLETON_CREATOR);
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    public String getTimerTaskNextRun()
    {
        return "n/a";
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    public boolean isDisabled()
    {
        return !isRunning();
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    public boolean isRunning()
    {
        return Config.getBoolean("cronjob.timertask.loadonstartup", true);
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    public void setDisabled(boolean disabled)
    {
        Config.setParameter("cronjob.timertask.loadonstartup", "" + (!disabled ? 1 : 0));
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    public String getTimerTaskStatus()
    {
        return isDisabled() ? "disabled" : "running";
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    void stopTimerTask()
    {
        setDisabled(true);
    }


    @Deprecated(since = "5.5.1", forRemoval = false)
    void startTimerTask()
    {
        setDisabled(false);
    }
}
