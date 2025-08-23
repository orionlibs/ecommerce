package de.hybris.platform.jalo.extension;

import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.persistence.extension.ExtensionManagerEJB;
import de.hybris.platform.util.RedeployUtilities;
import de.hybris.platform.util.Utilities;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

public class ExtensionManager extends Manager
{
    public static final String BEAN_NAME = "core.extensionManager";
    private final ConcurrentMap<String, Extension> extensionInstanceMap = new ConcurrentHashMap<>(100, 0.75F, 2);
    private volatile List<? extends Extension> allExtensionsCache = null;
    private static final Logger LOG = Logger.getLogger(ExtensionManager.class);
    private volatile boolean onFirstSessionCreationCalled = false;


    public Class getRemoteManagerClass()
    {
        return ExtensionManagerEJB.class;
    }


    public static ExtensionManager getInstance()
    {
        Registry.startup();
        return Registry.getCurrentTenant().getJaloConnection().getExtensionManager();
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
        for(Iterator<? extends Extension> it = getExtensions().iterator(); it.hasNext(); )
        {
            ((Extension)it.next()).checkBeforeItemRemoval(ctx, item);
        }
    }


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
        for(Iterator<? extends Extension> it = getExtensions().iterator(); it.hasNext(); )
        {
            ((Extension)it.next()).notifyItemRemoval(ctx, item);
        }
    }


    public Extension getExtension(String extName) throws ExtensionNotFoundException
    {
        Extension ext = this.extensionInstanceMap.get(extName);
        if(ext == null)
        {
            ext = loadExtension(extName);
            Extension previus = this.extensionInstanceMap.putIfAbsent(extName, ext);
            if(previus != null)
            {
                ext = previus;
            }
        }
        return ext;
    }


    public Collection getExtensionNames()
    {
        return Utilities.getExtensionNames();
    }


    public List<? extends Extension> getExtensions()
    {
        if(this.allExtensionsCache == null)
        {
            this.allExtensionsCache = loadAllExtensions();
        }
        return this.allExtensionsCache;
    }


    protected List<? extends Extension> loadAllExtensions()
    {
        Collection<String> extensionNames = getExtensionNames();
        List<Extension> tmp = new ArrayList(extensionNames.size());
        for(String name : extensionNames)
        {
            try
            {
                Extension extension = getExtension(name);
                if(extension != null)
                {
                    tmp.add(extension);
                }
            }
            catch(Exception e)
            {
                if(LOG.isInfoEnabled())
                {
                    LOG.info(e.getMessage(), e);
                }
            }
        }
        return Collections.unmodifiableList(tmp);
    }


    public boolean isExtensionInstalled(String name)
    {
        for(Iterator<String> it = getExtensionNames().iterator(); it.hasNext(); )
        {
            if(((String)it.next()).equals(name))
            {
                return true;
            }
        }
        return false;
    }


    private Extension loadExtension(String extName) throws ExtensionNotFoundException
    {
        String qualifier = extName + ".manager";
        ApplicationContext ctx = Registry.getCoreApplicationContext();
        if(ctx.containsBean(qualifier))
        {
            return (Extension)ctx.getBean(qualifier);
        }
        Tenant tenant = getTenant();
        MasterTenant masterTenant = (tenant instanceof MasterTenant) ? (MasterTenant)tenant : Registry.getMasterTenant();
        Class<Extension> clazz = Utilities.getExtensionClass(masterTenant, extName);
        if(clazz == null)
        {
            throw new ExtensionNotFoundException("extension '" + extName + "' is not a configured extension.", 0);
        }
        assureBeanNotDuplicated(ctx, clazz, extName);
        return (Extension)getSingletonManagerInstance(clazz, extName);
    }


    private void assureBeanNotDuplicated(ApplicationContext ctx, Class<Extension> clazz, String extName)
    {
        Map beanOfTypes = ctx.getBeansOfType(clazz);
        if(MapUtils.isNotEmpty(beanOfTypes))
        {
            LOG.warn("Bean of type <<" + clazz
                            .getName() + ">> which is the manager for <" + extName + "> extension has to have a bean id as <" + extName + ".manager>\n, otherwise redundant singleton manager will be created as manager class for extension.\nPlease adjust a spring configuartion accordingly.");
        }
    }


    public void notifyOnFirstSessionCreation()
    {
        if(!this.onFirstSessionCreationCalled)
        {
            synchronized(this)
            {
                if(!this.onFirstSessionCreationCalled)
                {
                    if(Utilities.isSystemInitialized(getTenant().getDataSource()))
                    {
                        this.onFirstSessionCreationCalled = true;
                        for(Extension e : getExtensions())
                        {
                            try
                            {
                                e.onFirstSessionCreation();
                            }
                            catch(Exception ex)
                            {
                                if(!RedeployUtilities.isShutdownInProgress())
                                {
                                    LOG.error("unexpected error notifying " + e
                                                    .getName() + " on session creation : " + ex.getMessage(), ex);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public Object writeReplace() throws ObjectStreamException
    {
        return new ExtensionManagerSerializableDTO(getTenant());
    }
}
