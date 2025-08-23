package de.hybris.platform.cluster;

import com.codahale.metrics.Meter;
import de.hybris.platform.cache.AdditionalInvalidationData;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.cache.RemoteInvalidationSource;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.Initialization;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.metrics.dropwizard.MetricUtils;
import de.hybris.platform.util.SingletonCreator;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

public class InvalidationBroadcastHandler implements BroadcastMessageListener
{
    public static final String TOPIC_DELIMITER = "/TOPIC/";
    public static final String KEY_DELIMITER = "/KEY/";
    public static final String INVTYPE_DELIMITER = "/TYPE/";
    public static final String NR_DELIMITER = "/NR/";
    private static final char KEY_ELEMENT_DELIMITER_CHAR = '_';
    public static final String KEY_ELEMENT_DELIMITER = String.valueOf('_');
    public static final int KIND_INVALIDATION = 1;
    private static final Logger LOG = Logger.getLogger(InvalidationBroadcastHandler.class);
    private static final SingletonCreator.Creator<InvalidationBroadcastHandler> SINGLETON_CREATOR = (SingletonCreator.Creator<InvalidationBroadcastHandler>)new Object();
    private static final int TYPE_FROM_CACHE_KEY_MIN_SIZE = 3;
    private static final int TYPE_FROM_CACHE_KEY_IDX = 2;
    private final MetricUtils.CachedMetrics<Pair<String, String>> metrics = MetricUtils.metricCache();
    private final BroadcastService broadcastService;
    public Pattern invalidationEventRegExpPattern;
    private long number = 0L;


    public InvalidationBroadcastHandler(BroadcastService broadcastService)
    {
        this.broadcastService = broadcastService;
        this.broadcastService.registerBroadcastListener(this, true);
    }


    public static InvalidationBroadcastHandler getInstance()
    {
        return (InvalidationBroadcastHandler)Registry.getNonTenantSingleton(SINGLETON_CREATOR);
    }


    public void sendMessage(Tenant tenant, Object[] topic, Object[] key, int invalidationType)
    {
        this.broadcastService.send(new RawMessage(1,
                        toBinaryMessage(tenant, topic, key, invalidationType, this.number++)));
        markMessageSent(tenant, key);
    }


    private void markMessageSent(Tenant tenant, Object[] key)
    {
        getMeter(key, "sent", tenant).ifPresent(Meter::mark);
        getMeter("total", "sent", tenant).ifPresent(Meter::mark);
    }


    public boolean processMessage(RawMessage message)
    {
        if(message.getKind() == 1)
        {
            InvalidationEvent event = parseEvent(message);
            if(event != null)
            {
                processInvalidation(event, message);
                return true;
            }
        }
        return false;
    }


    protected void processInvalidation(InvalidationEvent event, RawMessage message)
    {
        try
        {
            AbstractTenant targetSystem = (AbstractTenant)Registry.getTenantByID(event.systemID);
            if(targetSystem != null && targetSystem.getState() == AbstractTenant.State.STARTED &&
                            !targetSystem.isStopping() && !Initialization.isTenantInitializingLocally((Tenant)targetSystem))
            {
                Registry.setCurrentTenant((Tenant)targetSystem);
                InvalidationTopic topic = targetSystem.getInvalidationManager().getInvalidationTopic(event.topic);
                if(topic != null)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Received invalidation event: " + event);
                    }
                    topic.invalidateLocally(event.key, event.type, new RemoteInvalidationSource(message
                                    .getRemoteAddress()));
                    markMessageReceived(event, targetSystem);
                }
                else if(LOG.isDebugEnabled())
                {
                    LOG.debug("unknown topic in remove invalidation event from " + message.getRemoteAddress() + ": " + event);
                }
            }
            else if(LOG.isDebugEnabled())
            {
                LOG.debug("discarded invalidation event " + event + " due to missing or inactive tenant " + event.systemID);
            }
        }
        catch(IllegalStateException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error at receive of udp message while system shuts down", e);
            }
        }
        finally
        {
            Registry.unsetCurrentTenant();
        }
    }


    private void markMessageReceived(InvalidationEvent event, AbstractTenant targetSystem)
    {
        getMeter(event.key, "received", (Tenant)targetSystem).ifPresent(Meter::mark);
        getMeter("total", "received", (Tenant)targetSystem).ifPresent(Meter::mark);
    }


    private Optional<Meter> getMeter(Object[] key, String operation, Tenant tenant)
    {
        if(key.length < 3)
        {
            return Optional.empty();
        }
        return getMeter(String.valueOf(key[2]), operation, tenant);
    }


    private Optional<Meter> getMeter(String code, String operation, Tenant tenant)
    {
        return this.metrics.getMeter(Pair.of(code, operation), tenant.getTenantID(),
                        ctx -> MetricUtils.metricName("cache", new String[] {"invalidations", (String)((Pair)ctx.getMetricKey()).getRight()}).tag("code", (String)((Pair)ctx.getMetricKey()).getLeft()).tenant(ctx.getTenantId()).extension("core").module("cache").build());
    }


    protected byte[] toBinaryMessage(Tenant tenant, Object[] topic, Object[] key, int invalidationType, long number)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("sendInvalidation(" + tenant + ", " + topic + ", " + key + ", " + invalidationType + ", " + number + " )");
        }
        StringBuilder msg = new StringBuilder();
        String tenantID = tenant.getTenantID();
        msg.append(tenantID);
        msg.append("/TOPIC/");
        int i;
        for(i = 0; i < topic.length; i++)
        {
            if(i != 0)
            {
                msg.append(KEY_ELEMENT_DELIMITER);
            }
            Object next = topic[i];
            msg.append(objectToString(next));
        }
        msg.append("/KEY/");
        for(i = 0; i < key.length; i++)
        {
            if(i != 0)
            {
                msg.append(KEY_ELEMENT_DELIMITER);
            }
            Object next = key[i];
            msg.append(objectToString(next));
        }
        msg.append("/TYPE/").append(Integer.toString(invalidationType));
        msg.append("/NR/").append(Long.toString(number));
        return msg.toString().getBytes();
    }


    private InvalidationEvent parseEvent(RawMessage message)
    {
        InvalidationEvent ret = null;
        Matcher matcher = getInvalidationEventPattern().matcher(new String(message.getData()));
        if(matcher.matches())
        {
            String[] topicTokens = matcher.group(2).split(KEY_ELEMENT_DELIMITER);
            Object[] topic = new Object[topicTokens.length];
            int index = 0;
            for(String st : topicTokens)
            {
                topic[index++] = stringToObject(st);
            }
            String[] keyTokens = matcher.group(3).split(KEY_ELEMENT_DELIMITER);
            Object[] key = new Object[keyTokens.length];
            index = 0;
            for(String st : keyTokens)
            {
                key[index++] = stringToObject(st);
            }
            ret = new InvalidationEvent(message, matcher.group(1), Integer.parseInt(matcher.group(4)), key, topic, Long.parseLong(matcher.group(5)));
        }
        return ret;
    }


    protected Pattern getInvalidationEventPattern()
    {
        if(this.invalidationEventRegExpPattern == null)
        {
            this.invalidationEventRegExpPattern = Pattern.compile("(.*)/TOPIC/(.+)/KEY/(.+)/TYPE/(.+)/NR/(.*)");
        }
        return this.invalidationEventRegExpPattern;
    }


    protected Object stringToObject(String string)
    {
        return parseKeyElement(string);
    }


    static Object parseKeyElement(String string)
    {
        if(string.startsWith("{PK:"))
        {
            return PK.parse(string.substring("{PK:".length(), string.length() - 1));
        }
        if(string.startsWith("{REL="))
        {
            Objects.requireNonNull(Object.class);
            return unescapeKeyElement(string).flatMap(AdditionalInvalidationData::fromString).map(Object.class::cast)
                            .orElse(string);
        }
        return string;
    }


    protected String objectToString(Object object)
    {
        return serializeKeyElement(object);
    }


    static String serializeKeyElement(Object object)
    {
        if(object instanceof PK)
        {
            return "{PK:" + object.toString() + "}";
        }
        if(object instanceof String)
        {
            return (String)object;
        }
        if(object instanceof AdditionalInvalidationData)
        {
            String additionalInvalidationDataString = ((AdditionalInvalidationData)object).asString();
            return escapeKeyElement(additionalInvalidationDataString);
        }
        throw new RuntimeException("cannot convert object " + object + " to String for sending via network:" + object);
    }


    static String escapeKeyElement(String str)
    {
        StringBuilder builder = new StringBuilder(str.length() + 16);
        str.codePoints().forEach(character -> {
            if(character == 95)
            {
                builder.append("~");
            }
            else if(character == 126)
            {
                builder.append("|~");
            }
            else if(character == 124)
            {
                builder.append("||");
            }
            else
            {
                builder.appendCodePoint(character);
            }
        });
        return builder.toString();
    }


    static Optional<String> unescapeKeyElement(String str)
    {
        StringBuilder builder = new StringBuilder(str.length());
        char[] tab = str.toCharArray();
        int i = 0;
        while(i < tab.length)
        {
            char currentChar = tab[i++];
            if(currentChar == '~')
            {
                builder.append('_');
                continue;
            }
            if(currentChar == '|')
            {
                if(i == tab.length)
                {
                    return Optional.empty();
                }
                char nextChar = tab[i++];
                if(nextChar == '~')
                {
                    builder.append("~");
                    continue;
                }
                if(nextChar == '|')
                {
                    builder.append("|");
                    continue;
                }
                return Optional.empty();
            }
            builder.append(currentChar);
        }
        return Optional.of(builder.toString());
    }


    public void destroy()
    {
        this.broadcastService.unregisterBroadcastListener(this);
    }
}
