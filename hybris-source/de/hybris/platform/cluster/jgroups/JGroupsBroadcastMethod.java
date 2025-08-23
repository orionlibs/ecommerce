package de.hybris.platform.cluster.jgroups;

import de.hybris.platform.cluster.AbstractBroadcastMethod;
import de.hybris.platform.cluster.BroadcastService;
import de.hybris.platform.cluster.DefaultBroadcastService;
import de.hybris.platform.cluster.RawMessage;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.config.ConfigIntf;
import java.io.File;
import java.net.InetAddress;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.conf.PropertyConverters;
import org.jgroups.stack.IpAddress;
import org.jgroups.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JGroupsBroadcastMethod extends AbstractBroadcastMethod
{
    public static final String BASE_CONFIG_DIR = "jgroups";
    private static final Logger LOG = LoggerFactory.getLogger(JGroupsBroadcastMethod.class);
    private static final String JGROUPS_UDP_CONFIG_FILE = "jgroups-udp.xml";
    private static final String JGROUPS_TCP_JDBCPING_CONFIG_FILE = "jgroups-tcp.xml";
    private static final String JGROUPS_TCP_MPING_CONFIG_FILE = "jgroups-tcp.xml";
    private static final String DEFAULT_CONFIGURATION = "jgroups-udp.xml";
    private static final String DEFAULT_CHANNEL_NAME = "hybris-broadcast";
    private static final String CHANNEL_JMX_ENABLED = "cluster.broadcast.method.jgroups.channel.jmx.enabled";
    private static final String CHANNEL_JMX_DOMAIN_NAME = "cluster.broadcast.method.jgroups.channel.jmx.domain.name";
    private static final String DEFAULT_JMX_DOMAIN_NAME = "JGroups";
    private static final String JGROUPS_INIT_ERROR = "Error during jgroups initialization: ";
    private BroadcastService service;
    private volatile JChannel channel;


    public void send(RawMessage message)
    {
        if(this.channel != null)
        {
            try
            {
                this.channel.send(null, message.toRawByteArray());
            }
            catch(Exception e)
            {
                LOG.error("Error during jgroups initialization: ", e);
            }
        }
    }


    public void shutdown()
    {
        super.shutdown();
        if(this.channel != null)
        {
            try
            {
                this.channel.close();
            }
            finally
            {
                this.channel = null;
            }
        }
    }


    public void init(BroadcastService service)
    {
        super.init(service);
        this.service = service;
        this.channel = startChannel();
        if(jmxJGroupsRegistrationEnabled())
        {
            registerChannelWithMBeanServer(this.channel);
        }
    }


    private boolean jmxJGroupsRegistrationEnabled()
    {
        return Config.getBoolean("cluster.broadcast.method.jgroups.channel.jmx.enabled", false);
    }


    private void registerChannelWithMBeanServer(JChannel channel)
    {
        Util.registerChannel(channel, getJMXDomainName());
    }


    private String getJMXDomainName()
    {
        return getConfigValue("cluster.broadcast.method.jgroups.channel.jmx.domain.name", "JGroups");
    }


    private JChannel startChannel()
    {
        String configuration = getJgroupsConfiguration();
        setSystemProperties(configuration);
        return openChannel(configuration);
    }


    private void setSystemProperties(String configuration)
    {
        JGroupsPropertyConfigurer configurer = new JGroupsPropertyConfigurer();
        configurer.configure(getConfigFileName());
        String bindAddr = System.getProperty("hybris.jgroups.bind_addr");
        String bindPort = System.getProperty("hybris.jgroups.bind_port");
        if(isTcpConfigurationUsed(configuration))
        {
            validateTcpConnection(bindAddr, bindPort);
        }
        if(isJdbcPingUsed(configuration))
        {
            setDatabaseSystemProperties();
        }
    }


    private void validateTcpConnection(String bindAddr, String port)
    {
        try
        {
            validateBindAddressConfiguration(bindAddr, port);
        }
        catch(Exception e)
        {
            LOG.error("Error during jgroups initialization: ", e);
        }
    }


    void validateBindAddressConfiguration(String bindAddress, String bindPort) throws Exception
    {
        if(bindAddress.startsWith("match") || bindAddress.startsWith("custom:"))
        {
            PropertyConverters.Default defaultPropertyConverter = new PropertyConverters.Default();
            defaultPropertyConverter.convert(null, InetAddress.class, "cluster.broadcast.method.jgroups.tcp.bind_addr", bindAddress, false);
        }
        else
        {
            new IpAddress(bindAddress, Integer.parseInt(bindPort));
        }
    }


    private boolean isTcpConfigurationUsed(String configuration)
    {
        return (StringUtils.endsWith(configuration, "jgroups-tcp.xml") || StringUtils.endsWith(configuration, "jgroups-tcp.xml"));
    }


    private boolean isJdbcPingUsed(String configuration)
    {
        return StringUtils.endsWith(configuration, "jgroups-tcp.xml");
    }


    private void setDatabaseSystemProperties()
    {
        HybrisDataSource dataSource = Registry.getCurrentTenant().getDataSource();
        if(isJndiDataSource())
        {
            System.setProperty("hybris.datasource.jndi.name", dataSource.getJNDIName());
            LOG.debug("--- setting up hybris.datasource.jndi.name to: {}", dataSource.getJNDIName());
        }
        else
        {
            Map<String, String> connParams = dataSource.getConnectionParameters();
            System.setProperty("hybris.database.driver", connParams.get(Config.SystemSpecificParams.DB_DRIVER));
            System.setProperty("hybris.database.user", connParams.get(Config.SystemSpecificParams.DB_USERNAME));
            System.setProperty("hybris.database.password", connParams.get(Config.SystemSpecificParams.DB_PASSWORD));
            System.setProperty("hybris.database.url", connParams.get(Config.SystemSpecificParams.DB_URL));
            LOG.debug("--- setting up hybris.database.driver to: {}", connParams.get(Config.SystemSpecificParams.DB_DRIVER));
            LOG.debug("--- setting up hybris.database.user to: {}", connParams.get(Config.SystemSpecificParams.DB_USERNAME));
            LOG.debug("--- setting up hybris.database.password to: {}", connParams.get(Config.SystemSpecificParams.DB_PASSWORD));
            LOG.debug("--- setting up hybris.database.url to: {}", connParams.get(Config.SystemSpecificParams.DB_URL));
        }
        String schemaInitDDL = createSchemaInitDDL();
        System.setProperty("hybris.jgroups.schema", schemaInitDDL);
        LOG.debug("--- setting up hybris.jgroups.schema to: {}", schemaInitDDL);
    }


    private boolean isJndiDataSource()
    {
        return StringUtils.isNotBlank(getConfigValue(Config.SystemSpecificParams.DB_POOL_FROMJNDI, null));
    }


    private String createSchemaInitDDL()
    {
        return "CREATE TABLE JGROUPSPING (own_addr varchar(200) NOT NULL, cluster_name varchar(200) NOT NULL, ping_data " + getPingDataFieldType() + " DEFAULT NULL, PRIMARY KEY (own_addr, cluster_name) )";
    }


    private String getPingDataFieldType()
    {
        if(Config.isOracleUsed())
        {
            return "blob";
        }
        if(Config.isPostgreSQLUsed())
        {
            return "bytea";
        }
        return "varbinary(5000)";
    }


    private JChannel openChannel(String confgiuration)
    {
        JChannel jChannel = null;
        try
        {
            jChannel = new JChannel(confgiuration);
            jChannel.setName("hybrisnode-" + Registry.getClusterID());
            String channelName = getConfigValue("cluster.broadcast.method.jgroups.channel.name", "hybris-broadcast");
            jChannel.connect(channelName);
            jChannel.setReceiver((Receiver)new Object(this));
        }
        catch(Exception e)
        {
            LOG.error("Error during jgroups initialization: ", e);
        }
        return jChannel;
    }


    private String getJgroupsConfiguration()
    {
        String configFile = getConfigFileName();
        LOG.debug("JGroups configuration has been chosen (file: {})", configFile);
        return "jgroups" + File.separator + configFile;
    }


    private String getConfigFileName()
    {
        return getConfigValue("cluster.broadcast.method.jgroups.configuration", "jgroups-udp.xml");
    }


    private String getConfigValue(String key, String defaultValue)
    {
        ConfigIntf config = Registry.getCurrentTenant().getConfig();
        return config.getString(key, (defaultValue == null) ? "" : defaultValue);
    }


    protected void processMessage(Message messageFromChannel)
    {
        RawMessage msg = new RawMessage(messageFromChannel.getRawBuffer(), messageFromChannel.getOffset(), messageFromChannel.getLength());
        DefaultBroadcastService dbs = (DefaultBroadcastService)this.service;
        if(dbs.accept(msg))
        {
            notifyMessgageReceived(msg);
        }
    }
}
