package de.hybris.tomcat;

import java.util.Iterator;
import java.util.regex.Pattern;
import javax.naming.Context;
import javax.naming.NamingException;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.core.StandardServer;
import org.apache.catalina.mbeans.GlobalResourcesLifecycleListener;

public class HybrisGlobalResourcesLifecycleListener extends GlobalResourcesLifecycleListener
{
    private final Pattern DATA_SOURCE_NAME_PATTERN = Pattern.compile("(?<prefix>java:)?(?<path>.*\\/)+(?<name>.*)");
    private DataSourceName dataSourceName;


    public void lifecycleEvent(LifecycleEvent event)
    {
        super.lifecycleEvent(event);
        registerJNDIDataSourceInGlobalNamingContext(event);
    }


    protected void registerJNDIDataSourceInGlobalNamingContext(LifecycleEvent event)
    {
        if(this.dataSourceName != null && "start".equals(event
                        .getType()) && event.getSource() instanceof StandardServer)
        {
            Context globalNamingContext = ((StandardServer)event.getSource()).getGlobalNamingContext();
            try
            {
                Object dataSource = globalNamingContext.lookup(this.dataSourceName.getName());
                createSubContextes(globalNamingContext).bind(this.dataSourceName.getName(), dataSource);
            }
            catch(NamingException namingException)
            {
            }
        }
    }


    protected Context createSubContextes(Context globalNamingContext)
    {
        Context parent = globalNamingContext;
        Iterator<String> iterator = this.dataSourceName.getPath().iterator();
        while(iterator.hasNext())
        {
            parent = createSubContext(parent, iterator.next());
        }
        return parent;
    }


    protected Context createSubContext(Context parent, String name)
    {
        Context result = null;
        try
        {
            result = parent.createSubcontext(name);
        }
        catch(NamingException exception)
        {
            try
            {
                return (Context)parent.lookup(name);
            }
            catch(NamingException namingException)
            {
            }
        }
        return result;
    }


    public void setDataSourceName(String dataSourceName)
    {
        this.dataSourceName = new DataSourceName(this, dataSourceName);
    }
}
