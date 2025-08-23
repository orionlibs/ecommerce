package de.hybris.platform.util.config;

import de.hybris.platform.util.Key;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import org.apache.log4j.Logger;

public abstract class AbstractConfig implements ConfigIntf
{
    private static final Logger LOG = Logger.getLogger(AbstractConfig.class);
    private final Set<ConfigIntf.ConfigChangeListener> listeners = new CopyOnWriteArraySet<>();
    private final Map<Key, Object> convertCache = new ConcurrentHashMap<>();
    private final ConfigKeyGetter<Boolean> booleanGetter = (ConfigKeyGetter<Boolean>)new BooleanConfigKeyGetter(this);
    private final ConfigKeyGetter<Integer> integerGetter = (ConfigKeyGetter<Integer>)new IntegerConfigKeyGetter(this);
    private final ConfigKeyGetter<Long> longGetter = (ConfigKeyGetter<Long>)new LongConfigKeyGetter(this);
    private final ConfigKeyGetter<Double> doubleGetter = (ConfigKeyGetter<Double>)new DoubleConfigKeyGetter(this);
    private final ConfigKeyGetter<Character> charGetter = (ConfigKeyGetter<Character>)new CharacterConfigKeyGetter(this);
    private final ConfigKeyGetter<String> stringGetter = (ConfigKeyGetter<String>)new StringConfigKeyGetter(this);


    public void clearCache()
    {
        this.convertCache.clear();
    }


    public void registerConfigChangeListener(ConfigIntf.ConfigChangeListener listener)
    {
        this.listeners.add(listener);
    }


    public void unregisterConfigChangeListener(ConfigIntf.ConfigChangeListener listener)
    {
        this.listeners.remove(listener);
    }


    protected void notifyListeners(String key, String oldValue, String newValue)
    {
        if(oldValue != newValue && (oldValue == null || !oldValue.equals(newValue)))
        {
            this.convertCache.clear();
            for(ConfigIntf.ConfigChangeListener listener : this.listeners)
            {
                try
                {
                    listener.configChanged(key, newValue);
                }
                catch(Exception e)
                {
                    LOG.error("error notifying cfg listener " + listener, e);
                }
            }
        }
    }


    public final Map<String, String> getParametersMatching(String keyRegExp)
    {
        return getParametersMatching(keyRegExp, false);
    }


    public boolean getBoolean(String key, boolean def)
    {
        Boolean value = Boolean.valueOf(def);
        return ((Boolean)this.booleanGetter.get(key, value)).booleanValue();
    }


    public int getInt(String key, int def) throws NumberFormatException
    {
        Integer value = Integer.valueOf(def);
        return ((Integer)this.integerGetter.get(key, value)).intValue();
    }


    public long getLong(String key, long def) throws NumberFormatException
    {
        Long value = Long.valueOf(def);
        return ((Long)this.longGetter.get(key, value)).longValue();
    }


    public double getDouble(String key, double def) throws NumberFormatException
    {
        Double value = Double.valueOf(def);
        return ((Double)this.doubleGetter.get(key, value)).doubleValue();
    }


    public String getString(String key, String def)
    {
        return (String)this.stringGetter.get(key, def);
    }


    public char getChar(String key, char def) throws IndexOutOfBoundsException
    {
        Character value = Character.valueOf(def);
        return ((Character)this.charGetter.get(key, value)).charValue();
    }
}
