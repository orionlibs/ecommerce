package de.hybris.platform.task;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.jalo.user.UserManager;
import java.util.Date;
import junit.framework.Assert;

public class TxTestTaskRunner extends TestTaskRunner
{
    private Title _t;
    private Date _creationTime;


    synchronized Title getTitle()
    {
        return this._t;
    }


    synchronized Date getCreationTime()
    {
        return this._creationTime;
    }


    private synchronized void setRunData(Title t, Date d)
    {
        this._t = t;
        this._creationTime = d;
    }


    public void run(TaskService taskService, TaskModel task) throws RetryLaterException
    {
        try
        {
            Title t = UserManager.getInstance().createTitle("txTitle");
            Assert.assertTrue(t.isAlive());
            setRunData(t, t.getCreationTime());
        }
        catch(ConsistencyCheckException e)
        {
            e.printStackTrace();
            Assert.fail("unexpected error " + e.getMessage());
        }
        finally
        {
            super.run(taskService, task);
        }
        throw new IllegalStateException("this should make the enclosing tx to rollback!!!");
    }
}
