package de.hybris.tomcat;

import de.hybris.bootstrap.tomcat.cookieprocessor.CookieHandler;
import de.hybris.bootstrap.tomcat.cookieprocessor.LogHandler;
import de.hybris.bootstrap.tomcat.cookieprocessor.Rfc6265CookieProcessorLogicHolder;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;

public class ConfigurableRfc6265CookieProcessor extends Rfc6265CookieProcessor
{
    private static final Log LOGGER = LogFactory.getLog("cookies.samesite");
    private static final LogHandler FALLBACK_LOG_HANDLER = (LogHandler)new JuliLogHandler();

    static
    {
        Rfc6265CookieProcessorLogicHolder.setCookieHandler((CookieHandler)new FileBasedCookieHandler());
    }

    static Properties loadSameSiteProperties()
    {
        Properties props = new Properties();
        File cfgFile = new File("sameSiteCookies.properties");
        try
        {
            if(cfgFile.canRead())
            {
                InputStream in = new FileInputStream("sameSiteCookies.properties");
                try
                {
                    props.load(in);
                    info("SameSite Cookie Processor: Loaded settings from tomcat\\bin\\" + cfgFile.getName() + ": " + props);
                    in.close();
                }
                catch(Throwable throwable)
                {
                    try
                    {
                        in.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
            }
            else
            {
                info("No Same Site cookie config file found at tomcat\\bin\\" + cfgFile.getName());
            }
        }
        catch(IOException e)
        {
            error("Error loading from " + cfgFile, e);
        }
        return props;
    }


    protected static void debug(String msg)
    {
        getLogHandler().debug(msg);
    }


    protected static void info(String msg)
    {
        getLogHandler().info(msg);
    }


    protected static void warn(String msg)
    {
        getLogHandler().warn(msg);
    }


    protected static void error(String msg, Exception e)
    {
        getLogHandler().error(msg, e);
    }


    private static LogHandler getLogHandler()
    {
        return Rfc6265CookieProcessorLogicHolder.getLogHandler().orElse(FALLBACK_LOG_HANDLER);
    }


    @Deprecated(since = "ages")
    public String generateHeader(Cookie cookie)
    {
        return getAttributeFromConfiguration(cookie, super.generateHeader(cookie));
    }


    public String generateHeader(Cookie cookie, HttpServletRequest request)
    {
        return getAttributeFromConfiguration(cookie, super.generateHeader(cookie, request));
    }


    private String getAttributeFromConfiguration(Cookie cookie, String generated)
    {
        try
        {
            if(generated.matches("(?i)[ ;]SameSite\\w*="))
            {
                debug("Cookie " + toString(cookie) + " already got SameSite setting");
                return generated;
            }
            Optional<String> sameSiteAppSetting = getSameSiteAppSetting(cookie);
            if(sameSiteAppSetting.isEmpty())
            {
                debug("Cookie " + toString(cookie) + " has no SameSite app setting");
                return generated;
            }
            String sameSiteValue = sameSiteAppSetting.get();
            debug("Cookie " + toString(cookie) + " is using SameSite app setting '" + sameSiteValue + "'");
            if(!"Unset".equalsIgnoreCase(sameSiteValue))
            {
                return generated + "; SameSite=" + generated;
            }
            return generated;
        }
        catch(Exception e)
        {
            error("Error adjusting SameSite for cookie " + toString(cookie) + " setting:" + e.getMessage(), e);
            return generated;
        }
    }


    protected String toString(Cookie cookie)
    {
        return cookie.getDomain() + ":" + cookie.getDomain() + ":" + cookie.getPath();
    }


    protected Optional<String> getSameSiteAppSetting(Cookie cookie)
    {
        return Rfc6265CookieProcessorLogicHolder.getCookieHandler().flatMap(ch -> ch.getSameSiteParameter(cookie));
    }
}
