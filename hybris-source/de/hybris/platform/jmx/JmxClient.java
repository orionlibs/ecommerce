package de.hybris.platform.jmx;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import org.tanukisoftware.wrapper.jmx.WrapperManagerMBean;

public class JmxClient
{
    public static void restartWrapper(int pid) throws Exception
    {
        restartWrapper(0, pid, true);
    }


    private static void restartWrapper(int port, int pid, boolean local) throws Exception
    {
        JMXConnector connector = null;
        try
        {
            JMXServiceURL target;
            if(local)
            {
                String url = (String)Class.forName("jdk.internal.agent.ConnectorAddressLink").getMethod("importFrom", new Class[] {int.class}).invoke(null, new Object[] {Integer.valueOf(pid)});
                target = new JMXServiceURL(url);
            }
            else
            {
                String[] argsCustom = {"-host", "localhost", "-port", Integer.toString(port)};
                ConnectionArgs cArgs = new ConnectionArgs(argsCustom);
                target = cArgs.getJMXServiceURL();
            }
            connector = JMXConnectorFactory.connect(target);
            MBeanServerConnection remote = connector.getMBeanServerConnection();
            RuntimeMXBean remoteRuntime = ManagementFactory.<RuntimeMXBean>newPlatformMXBeanProxy(remote, "java.lang:type=Runtime", RuntimeMXBean.class);
            System.out.println("Target VM is: " + remoteRuntime.getName());
            ObjectName mbeanName = new ObjectName("org.tanukisoftware.wrapper:type=WrapperManager");
            WrapperManagerMBean mbeanProxy = JMX.<WrapperManagerMBean>newMBeanProxy(remote, mbeanName, WrapperManagerMBean.class, true);
            if(mbeanProxy.getJavaPID() == pid)
            {
                System.out.println("Now restarting..");
                mbeanProxy.restart();
            }
            else
            {
                System.err.println("PID's do not match. This seems to be a different VM.");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(connector != null)
            {
                connector.close();
            }
        }
    }


    public static void main(String[] args) throws Exception
    {
        String[] argsCustom = {"-host", "localhost", "-port", "9003"};
        ConnectionArgs cArgs = new ConnectionArgs(argsCustom);
        JMXServiceURL target = cArgs.getJMXServiceURL();
        JMXConnector connector = null;
        try
        {
            connector = JMXConnectorFactory.connect(target);
            MBeanServerConnection remote = connector.getMBeanServerConnection();
            RuntimeMXBean remoteRuntime = ManagementFactory.<RuntimeMXBean>newPlatformMXBeanProxy(remote, "java.lang:type=Runtime", RuntimeMXBean.class);
            System.out.println("Target VM is: " + remoteRuntime.getName());
            System.out.println("Started since: " + remoteRuntime.getUptime());
            System.out.println("With Classpath: " + remoteRuntime.getClassPath());
            System.out.println("And args: " + remoteRuntime.getInputArguments());
        }
        finally
        {
            if(connector != null)
            {
                connector.close();
            }
        }
    }
}
