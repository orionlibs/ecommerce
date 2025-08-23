package de.hybris.platform.persistence.security;

import de.hybris.platform.jalo.security.PasswordEncoderNotFoundException;
import de.hybris.platform.util.Config;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.log4j.Logger;

public class PasswordEncoderFactoryImpl implements PasswordEncoderFactory
{
    private static final String DEFAULT_ENCODER_CONFIG = "md5=" + SaltedMD5PasswordEncoder.class.getName() + ",*=" + PlainTextPasswordEncoder.class
                    .getName();
    public static final String PASSWORD_ENCODER_KEY = "password.encoders";
    private static final Logger LOG = Logger.getLogger(PasswordEncoderFactoryImpl.class.getName());
    private Map<String, PasswordEncoder> passwordEncoders = init();


    private Map<String, PasswordEncoder> init()
    {
        if(Config.getParameter("password.md5.salt") != null || Config.getParameter("password.encoders") != null)
        {
            LOG.warn("DEPRECATED PasswordEncoder/Salt configuration found in 'advanced.properties'!");
            LOG.warn("Please migrate your configuration settings to 'core-spring.xml'.");
            LOG.warn("More infos: https://wiki.hybris.com/display/forum/INFO+PasswordeEncoder+refactoring");
        }
        CaseInsensitiveMap<String, PasswordEncoder> caseInsensitiveMap = new CaseInsensitiveMap();
        String mappingString = Config.getString("password.encoders", DEFAULT_ENCODER_CONFIG);
        if(mappingString != null)
        {
            for(StringTokenizer st = new StringTokenizer(mappingString, ",;\t\n\r\f"); st.hasMoreTokens(); )
            {
                String mapping = st.nextToken();
                int i = mapping.indexOf('=');
                String encoding = mapping.substring(0, i).trim().toLowerCase();
                String className = mapping.substring(i + 1).trim();
                try
                {
                    caseInsensitiveMap.put(encoding, (PasswordEncoder)Class.forName(className).newInstance());
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("added password encoder '" + encoding + "' class '" + className + "'");
                    }
                }
                catch(ClassNotFoundException e)
                {
                    LOG.error("could not load password encoder class '" + className + "' (removed from mapping) : " + e);
                }
                catch(InstantiationException e)
                {
                    LOG.error("could not instantiate password encoder class '" + className + "' (removed from mapping) : " + e);
                }
                catch(IllegalAccessException e)
                {
                    LOG.error("could not access password encoder class '" + className + "' (removed from mapping) : " + e);
                }
            }
        }
        return Collections.unmodifiableMap((Map<? extends String, ? extends PasswordEncoder>)caseInsensitiveMap);
    }


    public PasswordEncoder getEncoder(String encoding) throws PasswordEncoderNotFoundException
    {
        String _encoding = (encoding == null) ? "*" : encoding.toLowerCase();
        PasswordEncoder enc = this.passwordEncoders.get(_encoding);
        if(enc == null)
        {
            try
            {
                Class<?> cl = Class.forName(encoding);
                enc = (PasswordEncoder)cl.newInstance();
            }
            catch(ClassNotFoundException e)
            {
                LOG.warn("password encoder not found: " + e.getMessage());
            }
            catch(InstantiationException e)
            {
                LOG.warn("password encoder not found: " + e.getMessage());
            }
            catch(IllegalAccessException e)
            {
                LOG.warn("password encoder not found: " + e.getMessage());
            }
        }
        if(enc == null)
        {
            throw new PasswordEncoderNotFoundException("cannot find password encoder for encoding '" + encoding + "'", 0);
        }
        return enc;
    }


    public boolean isSupportedEncoding(String encoding)
    {
        return this.passwordEncoders.keySet().contains(encoding.toLowerCase());
    }


    public Collection<String> getSupportedEncodings()
    {
        return this.passwordEncoders.keySet();
    }


    public Map<String, PasswordEncoder> getEncoders()
    {
        return this.passwordEncoders;
    }


    public void setEncoders(Map<String, PasswordEncoder> passwordEncoders)
    {
        Map<String, PasswordEncoder> m = new LinkedHashMap<>();
        for(String key : passwordEncoders.keySet())
        {
            m.put(key.toLowerCase(), passwordEncoders.get(key));
        }
        this.passwordEncoders = Collections.unmodifiableMap(m);
    }
}
