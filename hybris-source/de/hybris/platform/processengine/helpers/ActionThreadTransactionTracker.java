package de.hybris.platform.processengine.helpers;

import java.util.HashSet;
import java.util.Set;

public class ActionThreadTransactionTracker
{
    private final Set<TrackerGroup> groups = new HashSet<>();


    public void addSynchroGroup(String groupName, String... args)
    {
        this.groups.add(new SynchroGroup(this, groupName, args));
    }


    public void addAsynchroGroup(String groupName, String... args)
    {
        this.groups.add(new AsynchroGroup(this, groupName, args));
    }


    public synchronized void trackThreadAndTransactionForAction(String actionName, String threadName, long transactionId)
    {
        this.groups.forEach(gr -> gr.trackThreadAndTransactionForAction(threadName, Long.valueOf(transactionId), actionName));
    }


    public boolean isTransactionError(String groupName)
    {
        return ((TrackerGroup)this.groups.stream().filter(gr -> groupName.equals(gr.getGroupName())).findFirst().get()).isTransactionError();
    }


    public boolean isThreadError(String groupName)
    {
        return ((TrackerGroup)this.groups.stream().filter(gr -> groupName.equals(gr.getGroupName())).findFirst().get()).isThreadError();
    }
}
