package de.hybris.platform.hac.facade;

import de.hybris.platform.jmx.MBeanRegisterUtilities;
import de.hybris.platform.jmx.mbeans.impl.AbstractJMXMBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class HacJmxFacade
{
    private MBeanRegisterUtilities mbeanRegisterUtility;
    private static final Logger LOG = LoggerFactory.getLogger(HacJmxFacade.class);


    public List<Map<String, Object>> jmxData()
    {
        return getMBeansData();
    }


    public List<Map<String, Object>> jmxToggle(String id)
    {
        return jmxToggleBean(id);
    }


    public String beanRegister(String id, boolean register)
    {
        Map<String, AbstractJMXMBean> regBeans = this.mbeanRegisterUtility.getRegisteredBeans();
        Map<String, AbstractJMXMBean> unregBeans = this.mbeanRegisterUtility.getUnRegisteredBeans();
        Map<String, AbstractJMXMBean> selection = new HashMap<>();
        if(!regBeans.containsKey(id) && !unregBeans.containsKey(id))
        {
            if(register)
            {
                return "Retrieved AbstractJMXMBean bean instance during registration was invalid";
            }
            return "Retrieved AbstractJMXMBean bean instance during unregistration was invalid";
        }
        if(register)
        {
            if(regBeans.containsKey(id))
            {
                return String.format("MBean %s is already registered.", new Object[] {id});
            }
            selection.put(id, unregBeans.get(id));
            this.mbeanRegisterUtility.registerMBeans(selection);
            String str = String.format("MBean %s has been registered.", new Object[] {id});
            LOG.debug(str);
            return str;
        }
        if(unregBeans.containsKey(id))
        {
            return String.format("MBean %s is already unregistered.", new Object[] {id});
        }
        selection.put(id, regBeans.get(id));
        this.mbeanRegisterUtility.unregisterMBeans(selection);
        String ret = String.format("MBean %s has been unregistered.", new Object[] {id});
        LOG.debug(ret);
        return ret;
    }


    private List<Map<String, Object>> jmxToggleBean(String id)
    {
        LOG.debug("Toggling MBean Status for {}", id);
        Map<String, AbstractJMXMBean> regBeans = this.mbeanRegisterUtility.getRegisteredBeans();
        Map<String, AbstractJMXMBean> unregBeans = this.mbeanRegisterUtility.getUnRegisteredBeans();
        Map<String, AbstractJMXMBean> selection = new HashMap<>();
        if(regBeans.containsKey(id))
        {
            selection.put(id, regBeans.get(id));
            this.mbeanRegisterUtility.unregisterMBeans(selection);
            LOG.debug("MBean {} has been unregistered.", id);
        }
        else if(unregBeans.containsKey(id))
        {
            selection.put(id, unregBeans.get(id));
            this.mbeanRegisterUtility.registerMBeans(selection);
            LOG.debug("MBean {} has been registered.", id);
        }
        return getMBeansData();
    }


    private List<Map<String, Object>> getMBeansData()
    {
        ArrayList<Map<String, Object>> beans = new ArrayList<>();
        Map<String, AbstractJMXMBean> regBeans = this.mbeanRegisterUtility.getRegisteredBeans();
        Map<String, AbstractJMXMBean> unregBeans = this.mbeanRegisterUtility.getUnRegisteredBeans();
        createJMXXBeanMap(regBeans, beans, true);
        createJMXXBeanMap(unregBeans, beans, false);
        return beans;
    }


    private void createJMXXBeanMap(Map<String, AbstractJMXMBean> jmxmBeanMap, ArrayList<Map<String, Object>> beans, boolean registered)
    {
        for(Map.Entry<String, AbstractJMXMBean> entry : jmxmBeanMap.entrySet())
        {
            Map<String, Object> map = new HashMap<>();
            map.put("registered", Boolean.valueOf(registered));
            map.put("key", entry.getKey());
            map.put("domain", ((AbstractJMXMBean)entry.getValue()).getJmxDomain());
            map.put("objectName", ((AbstractJMXMBean)entry.getValue()).getObjectNameString());
            beans.add(map);
        }
    }


    @Required
    public HacJmxFacade setMbeanRegisterUtility(MBeanRegisterUtilities mbeanRegisterUtility)
    {
        this.mbeanRegisterUtility = mbeanRegisterUtility;
        return this;
    }
}
