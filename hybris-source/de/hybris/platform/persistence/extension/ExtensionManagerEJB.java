package de.hybris.platform.persistence.extension;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.ManagerEJB;
import de.hybris.platform.util.Utilities;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.collections.map.StaticBucketMap;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class ExtensionManagerEJB extends ManagerEJB
{
    private static final Logger LOG = Logger.getLogger(ExtensionManagerEJB.class.getName());
    private final Map<String, ExtensionEJB> extensionCache = (Map<String, ExtensionEJB>)new StaticBucketMap(40);


    public void notifyItemRemove(ItemRemote item)
    {
        System.err.println("deprecated call to ExtensionManagerEJB.notifyItemRemove( " + item + ") at ");
        Thread.dumpStack();
    }


    public boolean canRemoveItem(ItemRemote item) throws ConsistencyCheckException
    {
        System.err.println("deprecated call to ExtensionManagerEJB.canRemoveItem( " + item + ") at ");
        Thread.dumpStack();
        return true;
    }


    public Collection<String> getAllExtensionNames()
    {
        return Utilities.getExtensionNames();
    }


    public ExtensionEJB getExtension(String name)
    {
        ExtensionEJB extension = this.extensionCache.get(name);
        if(extension == null)
        {
            Manager manager;
            String qualifier = name + ".manager";
            ApplicationContext ctx = Registry.getCoreApplicationContext();
            if(ctx.containsBean(qualifier))
            {
                manager = (Manager)ctx.getBean(qualifier);
            }
            else
            {
                Class<Manager> clazz = Utilities.getExtensionClass(Registry.getMasterTenant(), name);
                Preconditions.checkNotNull(clazz, "Can not find the manager class " + name + " current class loader " +
                                getClass()
                                                .getClassLoader());
                manager = (Manager)Manager.getSingletonManagerInstance(clazz, name);
            }
            Class<ExtensionEJB> ejbclass = manager.getRemoteManagerClass();
            if(ejbclass == null)
            {
                GeneralExtensionEJB generalExtensionEJB = new GeneralExtensionEJB(name);
            }
            else
            {
                extension = (ExtensionEJB)Registry.getSingleton(ejbclass);
            }
            this.extensionCache.put(name, extension);
        }
        return extension;
    }


    public Collection<ExtensionEJB> getAllExtensions()
    {
        Collection<ExtensionEJB> result = new ArrayList<>();
        for(String name : getAllExtensionNames())
        {
            if(name.equalsIgnoreCase("core"))
            {
                continue;
            }
            try
            {
                ExtensionEJB extension = getExtension(name);
                result.add(extension);
            }
            catch(Exception e)
            {
                LOG.error("extension \"" + name + "\" was not found due to: " + e.getMessage());
                LOG.info("Stack trace: " + Utilities.getStackTraceAsString(e));
            }
        }
        return result;
    }
}
