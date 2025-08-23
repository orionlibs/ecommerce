package de.hybris.platform.masterserver.impl;

import com.google.common.collect.HashBiMap;
import com.hybris.encryption.asymmetric.AsymmetricEncryptor;
import com.hybris.encryption.asymmetric.impl.RSAEncryptor;
import com.hybris.encryption.asymmetric.impl.RSAKeyPairManager;
import com.hybris.encryption.symmetric.SymmetricEncryptor;
import com.hybris.encryption.symmetric.impl.AESEncryptor;
import com.hybris.statistics.StatisticsGateway;
import com.hybris.statistics.collector.BusinessStatisticsCollector;
import com.hybris.statistics.collector.SystemStatisticsCollector;
import de.hybris.platform.masterserver.StatisticsGatewayFactory;
import de.hybris.platform.masterserver.StatisticsPayloadEncryptor;
import de.hybris.platform.util.Utilities;
import java.security.interfaces.RSAPublicKey;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;

public final class DefaultStatisticsGatewayFactory implements StatisticsGatewayFactory<StatisticsPayload>
{
    public static final String MASTERSERVER_PUBLIC_KEY_FILE = "/security/hybris_license_key_pub.der";
    private static final StatisticsGatewayFactory<StatisticsPayload> factory = new DefaultStatisticsGatewayFactory();
    private volatile StatisticsGateway<StatisticsPayload> statisticsGateway;
    private final Reflections localScanner = new Reflections("de.hybris.platform.masterserver.collector", new org.reflections.scanners.Scanner[0]);
    private final Reflections simpleStatsScanner = new Reflections("com.hybris.statistics.collector", new org.reflections.scanners.Scanner[0]);
    private final RSAPublicKey publicKey;


    private DefaultStatisticsGatewayFactory()
    {
        this.publicKey = (new RSAKeyPairManager()).getPublicKey("/security/hybris_license_key_pub.der");
    }


    public static StatisticsGatewayFactory<StatisticsPayload> getInstance()
    {
        return factory;
    }


    public StatisticsGateway<StatisticsPayload> getOrCreateStatisticsGateway()
    {
        try
        {
            if(this.statisticsGateway == null)
            {
                synchronized(this)
                {
                    if(this.statisticsGateway == null)
                    {
                        DefaultStatisticsPayloadEncryptor defaultStatisticsPayloadEncryptor = new DefaultStatisticsPayloadEncryptor((SymmetricEncryptor)new AESEncryptor(), (AsymmetricEncryptor)new RSAEncryptor(), this.publicKey);
                        this
                                        .statisticsGateway = (StatisticsGateway<StatisticsPayload>)new DefaultStatisticsGateway(getCollectorsFor(BusinessStatisticsCollector.class), getCollectorsFor(SystemStatisticsCollector.class), (Map)HashBiMap.create(Utilities.getInstalledWebModules()).inverse(),
                                        (StatisticsPayloadEncryptor)defaultStatisticsPayloadEncryptor);
                    }
                }
            }
            return this.statisticsGateway;
        }
        catch(Exception e)
        {
            throw new RuntimeException(e.getMessage(), e);
        }
    }


    private <T extends com.hybris.statistics.collector.StatisticsCollector> Set<T> getCollectorsFor(Class<T> klass) throws InstantiationException, IllegalAccessException
    {
        Set<T> collectors = new HashSet<>();
        Set<Class<? extends T>> localImpls = this.localScanner.getSubTypesOf(klass);
        Set<Class<? extends T>> simpleStatsImpls = this.simpleStatsScanner.getSubTypesOf(klass);
        for(Class<? extends T> _klass : localImpls)
        {
            if(!_klass.isAnonymousClass())
            {
                collectors.add(_klass.newInstance());
            }
        }
        for(Class<? extends T> _klass : simpleStatsImpls)
        {
            if(!_klass.isAnonymousClass())
            {
                collectors.add(_klass.newInstance());
            }
        }
        return collectors;
    }
}
