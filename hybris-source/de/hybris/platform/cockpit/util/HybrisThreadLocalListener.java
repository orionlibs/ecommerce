package de.hybris.platform.cockpit.util;

import de.hybris.platform.servicelayer.exceptions.SystemException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.lang.Classes;
import org.zkoss.util.CollectionsX;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventThreadCleanup;
import org.zkoss.zk.ui.event.EventThreadInit;
import org.zkoss.zk.ui.event.EventThreadResume;
import org.zkoss.zk.ui.util.Configuration;
import org.zkoss.zkplus.util.ThreadLocals;

public class HybrisThreadLocalListener implements EventThreadInit, EventThreadCleanup, EventThreadResume
{
    private static final Logger LOG = LoggerFactory.getLogger(HybrisThreadLocalListener.class);
    private Map _fieldsMap;
    private Map _threadLocalsMap;
    private final boolean _enabled;


    public HybrisThreadLocalListener()
    {
        Execution execution = Executions.getCurrent();
        Desktop desk = (execution != null) ? execution.getDesktop() : null;
        if(desk != null)
        {
            WebApp app = desk.getWebApp();
            this._fieldsMap = (Map)app.getAttribute("zkplus.util.ThreadLocalListener.fieldsMap");
            this._enabled = app.getConfiguration().isEventThreadEnabled();
            if(this._fieldsMap == null)
            {
                this._fieldsMap = new HashMap<>(8);
                app.setAttribute("zkplus.util.ThreadLocalListener.fieldsMap", this._fieldsMap);
                Configuration config = app.getConfiguration();
                String val = config.getPreference("ThreadLocal", null);
                if(val != null)
                {
                    Collection klassSets = CollectionsX.parse(null, val, ';');
                    for(Iterator<String> its = klassSets.iterator(); its.hasNext(); )
                    {
                        String klassSetStr = its.next();
                        Collection klassSet = CollectionsX.parse(null, klassSetStr, '=');
                        Iterator<String> itz = klassSet.iterator();
                        String klass = itz.next();
                        String fieldsStr = itz.next();
                        Collection fields = CollectionsX.parse(null, fieldsStr, ',');
                        this._fieldsMap.put(klass, fields.toArray((Object[])new String[fields.size()]));
                    }
                }
            }
            this._threadLocalsMap = new HashMap<>(this._fieldsMap.size());
        }
        else
        {
            this._fieldsMap = Collections.EMPTY_MAP;
            this._threadLocalsMap = Collections.EMPTY_MAP;
            this._enabled = false;
        }
    }


    public void prepare(Component comp, Event evt)
    {
        if(this._enabled)
        {
            getThreadLocals();
        }
    }


    public boolean init(Component comp, Event evt)
    {
        if(this._enabled)
        {
            setThreadLocals();
        }
        return true;
    }


    public void cleanup(Component comp, Event evt, List errs)
    {
        if(this._enabled)
        {
            getThreadLocals();
        }
    }


    public void complete(Component comp, Event evt)
    {
        if(this._enabled)
        {
            setThreadLocals();
        }
    }


    public void beforeResume(Component comp, Event evt)
    {
        if(this._enabled)
        {
            getThreadLocals();
        }
    }


    public void afterResume(Component comp, Event evt)
    {
        if(this._enabled)
        {
            setThreadLocals();
        }
    }


    public void abortResume(Component comp, Event evt)
    {
    }


    private void getThreadLocals()
    {
        for(Iterator<Map.Entry> it = this._fieldsMap.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = it.next();
            try
            {
                String clsName = (String)entry.getKey();
                Class cls = Classes.forNameByThread(clsName);
                String[] fields = (String[])entry.getValue();
                Object[] threadLocals = new Object[fields.length];
                this._threadLocalsMap.put(clsName, threadLocals);
                for(int j = 0; j < threadLocals.length; j++)
                {
                    try
                    {
                        threadLocals[j] = getThreadLocal(cls, fields[j]).get();
                    }
                    catch(SystemException ex)
                    {
                        LOG.warn(ex.getMessage(), (Throwable)ex);
                    }
                }
            }
            catch(ClassNotFoundException ex)
            {
                LOG.warn(ex.getMessage(), ex);
            }
        }
    }


    private void setThreadLocals()
    {
        for(Iterator<Map.Entry> it = this._threadLocalsMap.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry entry = it.next();
            try
            {
                String clsName = (String)entry.getKey();
                Class cls = Classes.forNameByThread(clsName);
                Object[] threadLocals = (Object[])entry.getValue();
                String[] fields = (String[])this._fieldsMap.get(clsName);
                for(int j = 0; j < threadLocals.length; j++)
                {
                    getThreadLocal(cls, fields[j]).set(threadLocals[j]);
                }
            }
            catch(ClassNotFoundException ex)
            {
                LOG.warn(ex.getMessage(), ex);
            }
        }
        this._threadLocalsMap.clear();
    }


    private ThreadLocal getThreadLocal(Class cls, String fldname)
    {
        return ThreadLocals.getThreadLocal(cls, fldname);
    }
}
