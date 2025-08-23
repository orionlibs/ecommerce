package de.hybris.platform.util;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeTracer implements Serializable
{
    private static final Logger LOG = LoggerFactory.getLogger(TimeTracer.class);
    private static Map instances = new HashMap<>();
    private final NumberFormat numberFormat;
    private final String description;
    private final TimeTracer parent;


    public static TimeTracer getInstance(String name, int autoreport)
    {
        TimeTracer result = (TimeTracer)instances.get(name);
        if(result == null)
        {
            result = new TimeTracer(name);
            result.setAutoReport(autoreport);
        }
        return result;
    }


    private long time = 0L;
    private long timestamp;
    private int count = 0;
    private final HashSet onlineChildren = new HashSet();
    private final Map children = new HashMap<>();
    private static final int OFFLINE = 0;
    private static final int ONLINE = 1;
    private static final int FINISHED = 2;
    private int state = 0;
    private int autoReport = -1;


    public TimeTracer(String description)
    {
        this(description, null);
    }


    public TimeTracer(String description, TimeTracer parent)
    {
        this.description = description;
        this.parent = parent;
        this.numberFormat = NumberFormat.getInstance();
        this.numberFormat.setMaximumFractionDigits(3);
        if(parent == null)
        {
            instances.put(description, this);
        }
        else
        {
            parent.children.put(description, this);
        }
    }


    public long getTime()
    {
        return this.time;
    }


    protected TimeTracer getOrCreateChild(String childName)
    {
        TimeTracer child = (TimeTracer)this.children.get(childName);
        if(child == null)
        {
            child = new TimeTracer(childName, this);
        }
        return child;
    }


    protected TimeTracer getChild(String childName)
    {
        TimeTracer child = (TimeTracer)this.children.get(childName);
        if(child == null)
        {
            throw new RuntimeException("no child tracer with name " + childName);
        }
        return child;
    }


    public void start(String childPathName)
    {
        int index = childPathName.indexOf('/');
        if(index == -1)
        {
            getOrCreateChild(childPathName).start();
        }
        else
        {
            String childName = childPathName.substring(0, index);
            String remainingPath = childPathName.substring(index + 1);
            getOrCreateChild(childName).start(remainingPath);
        }
    }


    public void start()
    {
        switch(this.state)
        {
            case 1:
                LOG.info("TimeTracer {}: called start(), but was already started.", this.description);
                return;
            case 2:
                LOG.info("TimeTracer {}: called start(), but was already finished.", this.description);
                return;
            case 0:
                if(this.parent != null)
                {
                    checkParentStateWhenAdding();
                }
                this.timestamp = System.currentTimeMillis();
                this.state = 1;
                return;
        }
        throw new TimeTracerException();
    }


    protected void checkParentStateWhenAdding()
    {
        switch(this.parent.state)
        {
            case 1:
                if(!this.parent.onlineChildren.add(this))
                {
                    LOG.info("TimerTracer {}: called start(), but parent {} was already started.", this.description, this.parent.description);
                }
                return;
            case 0:
                LOG.info("TimerTracer {}: called start(), but parent {} was not yet started or already stopped.", this.description, this.parent.description);
                return;
            case 2:
                LOG.info("TimerTracer {}: called start(), but parent {} was already finished.", this.description, this.parent.description);
                return;
        }
        throw new TimeTracerException();
    }


    public void stop(String childPathName)
    {
        int index = childPathName.indexOf('/');
        if(index == -1)
        {
            getChild(childPathName).stop();
        }
        else
        {
            String childName = childPathName.substring(0, index);
            String remainingPath = childPathName.substring(index + 1);
            getChild(childName).stop(remainingPath);
        }
    }


    public void stop()
    {
        switch(this.state)
        {
            case 0:
                LOG.info("TimeTracer {}: called stop(), but was not yet started or already stopped.", this.description);
                break;
            case 2:
                LOG.info("TimeTracer {}: called stop(), but was already finished.", this.description);
                break;
            case 1:
                if(this.parent != null)
                {
                    checkParentStateWhenRemoving();
                }
                this.time += System.currentTimeMillis() - this.timestamp;
                this.count++;
                this.state = 0;
                break;
            default:
                throw new TimeTracerException();
        }
        if(this.autoReport > 0 && this.count % this.autoReport == 0)
        {
            printReport();
        }
    }


    protected void checkParentStateWhenRemoving()
    {
        switch(this.parent.state)
        {
            case 1:
                if(!this.parent.onlineChildren.remove(this))
                {
                    throw new TimeTracerException();
                }
                return;
            case 0:
                LOG.info("TimeTracer {}: called stop(), but parent {} was not yet started or already stopped.", this.description, this.parent.description);
                return;
            case 2:
                LOG.info("TimeTracer {}: called stop(), but parent {} was already finished.", this.description, this.parent.description);
                return;
        }
        throw new TimeTracerException();
    }


    public void finish()
    {
        switch(this.state)
        {
            case 1:
                LOG.info("TimeTracer {}: called finish(), but was not yet stopped.", this.description);
            case 2:
                LOG.info("TimeTracer {}: called finish(), but was already finished.", this.description);
            case 0:
                if(!this.onlineChildren.isEmpty())
                {
                    StringBuilder buf = new StringBuilder();
                    for(Iterator i = this.onlineChildren.iterator(); i.hasNext(); )
                    {
                        buf.append(((TimeTracer)i.next()).description);
                        if(i.hasNext())
                        {
                            buf.append(", ");
                        }
                    }
                    if(LOG.isInfoEnabled())
                    {
                        LOG.info("TimeTracer {}: called finish(), but still has children that have not yet been stopped: {}.", this.description, buf);
                    }
                }
                printInstanceReport(0);
                this.state = 2;
                return;
        }
        throw new TimeTracerException();
    }


    public void finishAll()
    {
        finish();
        Iterator<TimeTracer> iter = this.children.values().iterator();
        while(iter.hasNext())
        {
            TimeTracer next = iter.next();
            next.finishAll();
        }
    }


    protected String getPathName()
    {
        if(this.parent == null)
        {
            return this.description;
        }
        return this.parent.getPathName() + "/" + this.parent.getPathName();
    }


    public void printInstanceReport(int indent)
    {
        String name;
        StringBuilder indentString = new StringBuilder();
        for(int i = 0; i < indent; i++)
        {
            indentString.append(' ');
        }
        if(indent == 0)
        {
            name = getPathName();
        }
        else
        {
            name = this.description;
        }
        if(LOG.isInfoEnabled())
        {
            LOG.info("{}{}: count={}, time={}s, avg={}s", new Object[] {indentString, name, Integer.valueOf(this.count), this.numberFormat.format(this.time / 1000.0D), this.numberFormat
                            .format(this.time / this.count / 1000.0D)});
        }
    }


    public void printReport()
    {
        printReport(0);
    }


    protected void printReport(int indent)
    {
        printInstanceReport(indent);
        Iterator<TimeTracer> iter = this.children.values().iterator();
        while(iter.hasNext())
        {
            TimeTracer next = iter.next();
            next.printReport(indent + 2);
        }
    }


    public void setAutoReport(int autoReportPeriod)
    {
        this.autoReport = autoReportPeriod;
    }


    public static synchronized Object execute(String key, int autoreport, TraceBody body) throws Exception
    {
        TimeTracer tracer = getInstance(key, autoreport);
        tracer.start();
        try
        {
            return body.run();
        }
        finally
        {
            tracer.stop();
        }
    }
}
