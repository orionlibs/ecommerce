package de.hybris.platform.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.math.DoubleMath;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.config.TenantInfo;
import de.hybris.bootstrap.config.WebExtensionModule;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.core.AbstractTenant;
import de.hybris.platform.core.MasterTenant;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.SlaveTenant;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.security.PasswordEncoderNotFoundException;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.licence.Licence;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.security.PasswordEncoder;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.spring.HybrisContextLoaderListener;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.config.ConfigIntf;
import de.hybris.platform.util.typesystem.PlatformStringUtils;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.apache.log4j.Logger;

public class Utilities
{
    private static final double DOUBLE_EQ_EPSILON = Math.pow(10.0D, -20.0D);
    private static boolean warnedAboutXalan = false;
    private static boolean warnedAboutUnsecureXLMProcessing = false;
    private static final Logger LOG = Logger.getLogger(Utilities.class.getName());
    public static final String DEFAULT_PASSWORD_ENCODING = "*";
    public static final String DEFAULT_ITEMLINK_ENCODING = "UTF-8";
    public static final String ITEMLINK_PREFIX = "linkTo.";
    public static final String ITEMLINK_PROTOCOLL_SPECIFIER = "item://";
    public static final String ITEMLINK_TYPE_PREFIX = "type:";
    public static final String ITEMLINK_ITEMATTRIBUTE_START = "(";
    public static final String ITEMLINK_ITEMATTRIBUTE_END = ")";
    public static final String ITEMLINK_ELEMENT_SEPARATOR = "/";
    public static final String ITEMLINK_VALUE_SEPARATOR = "=";
    public static final String ITEMLINK_PATTERN_ATTRIBUTE_SEPARATOR = ",";
    public static final String ITEMLINK_LINK_ID = "$link_id";
    public static final String ITEMLINK_REPLACEMENT_TYPE = "$replacementType";
    public static final String STACKTRACE_START_MARKER = "/*";
    public static final String STACKTRACE_END_MARKER = "*/";
    private static final String DEFAULT_RESOURCE_BUNDLE_ENCODING = "UTF-8";
    private static volatile ItemUtilities itemUtilities = null;
    private static volatile DBUtilities dbUtilities = null;
    private static volatile CoreUtilities coreUtilities = null;
    private static volatile CorePlusUtilities corePlusUtilities = null;
    private static final ConcurrentMap<String, Boolean> relationOrderOverrideCache = new ConcurrentHashMap<>(1024, 0.75F, 4);
    private static final ConcurrentMap<String, Boolean> markModifiedCache = new ConcurrentHashMap<>(1024, 0.75F, 4);


    private static void assureItemUtilities()
    {
        if(itemUtilities == null)
        {
            itemUtilities = new ItemUtilities();
        }
    }


    private static void assureDBUtilities()
    {
        if(dbUtilities == null)
        {
            dbUtilities = new DBUtilities();
        }
    }


    private static void assureCorePlusUtilities()
    {
        assureCoreUtilities();
        if(corePlusUtilities == null)
        {
            corePlusUtilities = new CorePlusUtilities(coreUtilities);
        }
    }


    private static void assureCoreUtilities()
    {
        if(coreUtilities == null)
        {
            coreUtilities = new CoreUtilities(ConfigUtil.getPlatformConfig(Utilities.class), Registry.isStandaloneMode(), Registry.getPreferredClusterID());
        }
    }


    private Utilities()
    {
        throw new AssertionError("Utilities class is not designed for instantiation.");
    }


    public static final Enum getOS()
    {
        String operatingSystem = System.getProperty("os.name");
        if(operatingSystem.startsWith("Windows"))
        {
            return (Enum)OS.WINDOWS;
        }
        if(operatingSystem.startsWith("Linux"))
        {
            return (Enum)OS.LINUX;
        }
        if(operatingSystem.startsWith("Mac"))
        {
            return (Enum)OS.MAC;
        }
        if(operatingSystem.startsWith("NetWare"))
        {
            return (Enum)OS.NETWARE;
        }
        if(operatingSystem.startsWith("SunOS"))
        {
            return (Enum)OS.SOLARIS;
        }
        if(operatingSystem.startsWith("HP-UX"))
        {
            return (Enum)OS.HPUX;
        }
        if(operatingSystem.startsWith("AIX"))
        {
            return (Enum)OS.AIX;
        }
        if(operatingSystem.startsWith("OS/2"))
        {
            return (Enum)OS.OS2;
        }
        if(operatingSystem.startsWith("Digital Unix"))
        {
            return (Enum)OS.DIGITAL;
        }
        if(operatingSystem.startsWith("Irix"))
        {
            return (Enum)OS.IRIX;
        }
        return (Enum)OS.UNKNOWN;
    }


    public static final boolean isUnix()
    {
        return (!getOS().equals(OS.WINDOWS) && !getOS().equals(OS.OS2) && !getOS().equals(OS.NETWARE));
    }


    public static final boolean isWindows()
    {
        return getOS().equals(OS.WINDOWS);
    }


    public static File getPlatformTempDir()
    {
        PlatformConfig platformConfig = ConfigUtil.getPlatformConfig(Utilities.class);
        return platformConfig.getSystemConfig().getTempDir();
    }


    public static final Object copyViaSerialization(Object originalObject)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("copyViaSerialization: " + originalObject.getClass().getName());
        }
        return (originalObject == null) ? null : copyViaSerializationUsingStandardJava(originalObject);
    }


    protected static Object copyViaSerializationUsingStandardJava(Object originalObject)
    {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        try
        {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(originalObject);
            byte[] data = baos.toByteArray();
            ois = new ObjectInputStream(new ByteArrayInputStream(data));
            return ois.readObject();
        }
        catch(IOException e)
        {
            throw new RuntimeException(e.getClass().getName() + " while copying >>" + e.getClass().getName() + "<<: " + originalObject, e);
        }
        catch(ClassNotFoundException e)
        {
            throw new RuntimeException(e.getClass().getName() + " while copying >>" + e.getClass().getName() + "<<: " + originalObject, e);
        }
        finally
        {
            IOUtils.closeQuietly(oos);
            IOUtils.closeQuietly(ois);
        }
    }


    public static final Object copyViaSerializationIfNecessary(Object s)
    {
        if(s == null)
        {
            return null;
        }
        if(s instanceof String || s instanceof Number || s instanceof Character || s instanceof Boolean || s instanceof ItemPropertyValue || s instanceof PK)
        {
            return s;
        }
        if(s instanceof Date)
        {
            return new Date(((Date)s).getTime());
        }
        if(s == Collections.EMPTY_LIST || s == Collections.EMPTY_MAP || s == Collections.EMPTY_SET)
        {
            return s;
        }
        return copyViaSerialization(s);
    }


    public static final void touchForReplication(HttpSession session)
    {
        for(Enumeration<String> en = session.getAttributeNames(); en.hasMoreElements(); )
        {
            String str = en.nextElement();
            session.setAttribute(str, session.getAttribute(str));
        }
    }


    public static final String replaceXMLEntities(String s)
    {
        StringBuilder buffer = new StringBuilder();
        for(int i = 0; i < s.length(); i++)
        {
            char nextChar = s.charAt(i);
            switch(nextChar)
            {
                case '&':
                    buffer.append("&amp;");
                    break;
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                case '"':
                    buffer.append("&quot;");
                    break;
                default:
                    buffer.append(nextChar);
                    break;
            }
        }
        return buffer.toString();
    }


    public static final int getInt(String intString, int defaultValue)
    {
        if(intString != null)
        {
            try
            {
                return Integer.parseInt(intString);
            }
            catch(NumberFormatException e)
            {
                LOG.warn(e.getMessage());
            }
        }
        return defaultValue;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static final Locale getLocale(String isoCode)
    {
        String[] loc = parseLocaleCodes(isoCode);
        return new Locale(loc[0], loc[1], loc[2]);
    }


    public static final String[] parseLocaleCodes(String isoCodes)
    {
        String[] ret = null;
        String[] loc = {"", "", ""};
        Pattern pattern = Pattern.compile("[,_\\.\\-]");
        Matcher matcher = pattern.matcher(isoCodes);
        int idx = 0;
        int startPos = 0;
        while(matcher.find() && idx < 3)
        {
            loc[idx++] = isoCodes.substring(startPos, matcher.start()).trim();
            startPos = matcher.start() + 1;
        }
        if(startPos < isoCodes.length())
        {
            if(idx > 2)
            {
                idx = 2;
                startPos--;
            }
            loc[idx] = isoCodes.substring(startPos - loc[idx].length()).trim();
        }
        else if(idx == 3 && startPos == isoCodes.length())
        {
            loc[2] = isoCodes.substring(startPos - 1).trim();
        }
        if(idx > 0 && (loc[0].length() > 0 || loc[1].length() > 0))
        {
            ret = loc;
        }
        else
        {
            ret = new String[] {isoCodes, "", ""};
        }
        return ret;
    }


    public static List<String> getExtensionNames()
    {
        return Registry.getCurrentTenant().getTenantSpecificExtensionNames();
    }


    public static Class getExtensionClass(MasterTenant t, String name)
    {
        assureCoreUtilities();
        return (Class)coreUtilities.getInstalledExtensionClassMapping().get(name);
    }


    public static <T> ImmutableSet<T> mergeToImmutableSet(Set<T> col1, Set<T> col2)
    {
        return ImmutableSet.builder().addAll(col1).addAll(col2).build();
    }


    public static Map<String, String> getInstalledWebModules()
    {
        assureCoreUtilities();
        return coreUtilities.getInstalledWebModules();
    }


    public static List<String> getInstalledExtensionNames(Tenant tenant)
    {
        if(tenant == Registry.getMasterTenant())
        {
            assureCoreUtilities();
            return coreUtilities.getAllConfiguredExtensionNames();
        }
        return tenant.getTenantSpecificExtensionNames();
    }


    public static ExtensionInfo getExtensionInfo(String name)
    {
        return getPlatformConfig().getExtensionInfo(name);
    }


    public static Set<String> getPlatformExtensionNames()
    {
        return getPlatformConfig().getAllPlatformExtensionNames();
    }


    public static PlatformConfig getPlatformConfig()
    {
        assureCoreUtilities();
        return coreUtilities.getBootstrapConfig();
    }


    public static ConfigIntf getConfig()
    {
        assureCoreUtilities();
        return coreUtilities.getPlatformConfig();
    }


    public static Collection<String> getInstalledPasswordEncodings()
    {
        if(Registry.hasCurrentTenant())
        {
            return Registry.getCurrentTenant().getJaloConnection().getPasswordEncoderFactory().getSupportedEncodings();
        }
        throw new IllegalStateException("no active tenant - cannot get password decodings yet");
    }


    public static PasswordEncoder getPasswordEncoder(String encoding) throws JaloInvalidParameterException
    {
        if(Registry.hasCurrentTenant())
        {
            try
            {
                return Registry.getCurrentTenant().getJaloConnection().getPasswordEncoder(encoding);
            }
            catch(PasswordEncoderNotFoundException e)
            {
                throw new JaloInvalidParameterException(e.getMessage(), e.getErrorCode());
            }
        }
        throw new IllegalStateException("no active tenant - cannot get password decoders yet");
    }


    public static final String getStackTraceAsString(Throwable throwable)
    {
        if(throwable == null)
        {
            return null;
        }
        StringWriter stackWriter = new StringWriter();
        PrintWriter stackPrinter = new PrintWriter(stackWriter, true);
        throwable.printStackTrace(stackPrinter);
        stackPrinter.flush();
        stackPrinter.close();
        return stackWriter.getBuffer().toString().replaceAll("java.lang.Exception:", "");
    }


    public static <T extends Throwable> Throwable getRootCauseOfType(Throwable e, Class<T> type)
    {
        for(Throwable cause = e; cause != null; cause = cause.getCause())
        {
            if(type.isInstance(cause))
            {
                return cause;
            }
        }
        return null;
    }


    public static <T extends Throwable> Throwable getRootCauseOfName(Throwable e, String name)
    {
        for(Throwable cause = e; cause != null; cause = cause.getCause())
        {
            if(name.equals(cause.getClass().getName()))
            {
                return cause;
            }
        }
        return null;
    }


    public static String escapeJavaScript(String s)
    {
        if(s == null)
        {
            return null;
        }
        return s.replace("%", "%25").replace(" ", "%20").replace("!", "%21").replace("#", "%23")
                        .replace("$", "%24").replace("^", "%5E").replace("&", "%26").replace("(", "%28")
                        .replace(")", "%29").replace("=", "%3D").replace(":", "%3A").replace(";", "%3B")
                        .replace("\"", "%22").replace("'", "%27").replace("\\", "%5C").replace("?", "%3F")
                        .replace("<", "%3C").replace(">", "%3E").replace("~", "%7E").replace("[", "%5B")
                        .replace("]", "%5D").replace("{", "%7B").replace("}", "%7D").replace("`", "%60");
    }


    public static String escapeHTML(String s)
    {
        if(s == null)
        {
            return null;
        }
        int length = s.length();
        StringBuilder sb = new StringBuilder(length * 2);
        for(int i = 0; i < length; i++)
        {
            char c = s.charAt(i);
            int cint = Character.MAX_VALUE & c;
            if(cint < 32)
            {
                switch(c)
                {
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                        sb.append(c);
                        break;
                }
            }
            else
            {
                switch(c)
                {
                    case '"':
                        sb.append("&quot;");
                        break;
                    case '\'':
                        sb.append("&#39;");
                        break;
                    case '&':
                        sb.append("&amp;");
                        break;
                    case '<':
                        sb.append("&lt;");
                        break;
                    case '>':
                        sb.append("&gt;");
                        break;
                    default:
                        sb.append(c);
                        break;
                }
            }
        }
        return sb.toString();
    }


    public static String filterOutHTMLTags(String input)
    {
        if(input == null)
        {
            return null;
        }
        StringBuilder clean = new StringBuilder();
        boolean add = true;
        for(int i = 0; i < input.length(); i++)
        {
            if(input.charAt(i) == '<')
            {
                add = false;
            }
            else if(!add && input.charAt(i) == '>')
            {
                add = true;
            }
            else if(add)
            {
                clean.append(input.charAt(i));
            }
        }
        return new String(clean);
    }


    public static String formatTime(long milliseconds)
    {
        return DurationFormatUtils.formatDuration(milliseconds, "d'd' HH'h':mm'm':ss's':SSS'ms'", true);
    }


    public static String toHex(String val)
    {
        if(val == null)
        {
            return null;
        }
        byte[] bytes = val.getBytes();
        String res = "";
        for(int i = 0; i < bytes.length; i++)
        {
            res = res + res + " ";
        }
        return res;
    }


    public static String toHex(byte val)
    {
        int number;
        if(val < 0)
        {
            number = 256 + val;
        }
        else
        {
            number = val;
        }
        return toHex1(number);
    }


    public static String toHex1(int number)
    {
        String map = "0123456789ABCDEF";
        String res = String.valueOf("0123456789ABCDEF".charAt(number % 16));
        res = "" + "0123456789ABCDEF".charAt((number - number % 16) / 16) + "0123456789ABCDEF".charAt((number - number % 16) / 16);
        return res;
    }


    public static final void tryToCloseJDBC(Connection connection, Statement statement, ResultSet resultSet)
    {
        tryToCloseJDBC(connection, statement, resultSet, false);
    }


    public static final void tryToCloseJDBC(Connection connection, Statement statement, ResultSet resultSet, boolean ignoreErrors)
    {
        Exception ex = null;
        try
        {
            if(resultSet != null)
            {
                resultSet.close();
            }
        }
        catch(SQLException e)
        {
            ex = e;
        }
        try
        {
            if(statement != null)
            {
                statement.close();
            }
        }
        catch(SQLException e)
        {
            ex = e;
        }
        try
        {
            if(connection != null)
            {
                connection.close();
            }
        }
        catch(SQLException e)
        {
            ex = e;
        }
        if(ex != null && !ignoreErrors)
        {
            throw new RuntimeException(ex);
        }
    }


    public static Object callMethod(Object instance, String methodName, Class[] paramClasses, Object[] params) throws Exception
    {
        Method method = instance.getClass().getMethod(methodName, paramClasses);
        return callMethod(instance, method, params);
    }


    public static Object callMethod(Object instance, Method method, Object[] params) throws Exception
    {
        try
        {
            return method.invoke(instance, params);
        }
        catch(InvocationTargetException e)
        {
            Throwable thr = e.getTargetException();
            if(thr instanceof Exception)
            {
                throw (Exception)thr;
            }
            throw (Error)thr;
        }
        catch(Exception e)
        {
            throw e;
        }
    }


    public static Class[] getAllInterfaces(Class cl)
    {
        Set lst = new LinkedHashSet();
        fillInterfaceList(cl, lst);
        return (Class[])lst.toArray((Object[])new Class[lst.size()]);
    }


    private static void fillInterfaceList(Class clazz, Set toFill)
    {
        Class superClass = clazz.getSuperclass();
        if(superClass != null)
        {
            fillInterfaceList(superClass, toFill);
        }
        Class[] ifs = clazz.getInterfaces();
        toFill.addAll(Arrays.asList((Class<?>[][])ifs));
        if(ifs != null)
        {
            for(int i = 0; i < ifs.length; i++)
            {
                fillInterfaceList(ifs[i], toFill);
            }
        }
    }


    public static boolean ejbEquals(ItemRemote first, ItemRemote second)
    {
        return (first == second || (first != null && first.equals(second)));
    }


    public static File getPathFromResource(Class c, String resource)
    {
        try
        {
            URL url = c.getResource(resource);
            if(url == null)
            {
                return null;
            }
            URI uri = url.toURI();
            File f = new File(uri);
            return f;
        }
        catch(URISyntaxException e)
        {
            throw new RuntimeException(e);
        }
    }


    public static String buildRelativePath(File fromDir, File toFileOrDir)
    {
        String ofrom, oto;
        boolean needSepEndForDirs, otoEndsWithSep;
        if(isUnix() && (fromDir.getName().contains(":") || toFileOrDir.getName().contains(":")))
        {
            throw new JaloSystemException("In an UNIX environment, you shouldn't specify filenames, which contains a ':', because the ':' will be used as a path delimiter, too!", -678345);
        }
        char sep = '/';
        try
        {
            ofrom = fromDir.getCanonicalPath().replace('\\', '/');
            oto = toFileOrDir.getCanonicalPath().replace('\\', '/');
        }
        catch(IOException e)
        {
            LOG.warn("getCanonicalPath( ) failed on one of the following dirs, using getAbsolutePath() instead: " + fromDir
                            .getAbsolutePath() + ", " + toFileOrDir.getAbsolutePath());
            ofrom = fromDir.getAbsolutePath().replace('\\', '/');
            oto = toFileOrDir.getAbsolutePath().replace('\\', '/');
        }
        if(!ofrom.endsWith(File.separator))
        {
            ofrom = ofrom + "/";
            needSepEndForDirs = false;
        }
        else
        {
            needSepEndForDirs = true;
        }
        if(!oto.endsWith(File.separator))
        {
            oto = oto + "/";
            otoEndsWithSep = false;
        }
        else
        {
            otoEndsWithSep = true;
        }
        String from = ofrom.toLowerCase(LocaleHelper.getPersistenceLocale());
        String to = oto.toLowerCase(LocaleHelper.getPersistenceLocale());
        StringBuilder path = new StringBuilder(oto.length());
        int fromln = from.length();
        while(true)
        {
            if(to.regionMatches(0, from, 0, fromln))
            {
                File fromf = new File(ofrom.substring(0, needSepEndForDirs ? fromln : (fromln - 1)));
                File tof = new File(oto.substring(0, needSepEndForDirs ? fromln : (fromln - 1)));
                if(fromf.equals(tof))
                {
                    break;
                }
            }
            path.append("../");
            fromln--;
            while(fromln > 0 && from.charAt(fromln - 1) != '/')
            {
                fromln--;
            }
            if(fromln == 0)
            {
                if(to.endsWith("/"))
                {
                    oto = oto.substring(0, oto.length() - 1);
                    return oto;
                }
            }
        }
        path.append(oto.substring(fromln));
        if(!otoEndsWithSep && path.length() != 0)
        {
            path.setLength(path.length() - 1);
        }
        if(path.toString().endsWith("/"))
        {
            path.setLength(path.length() - 1);
        }
        return path.toString();
    }


    public static boolean isSystemInitialized(HybrisDataSource ds)
    {
        assureDBUtilities();
        return dbUtilities.isSystemInitialized(ds);
    }


    public static boolean isTypeInitialized(String typecode)
    {
        if(isSystemInitialized(Registry.getCurrentTenant().getDataSource()))
        {
            try
            {
                return
                                (((TypeService)Registry.getApplicationContext().getBean("typeService", TypeService.class)).getTypeForCode(typecode) != null);
            }
            catch(IllegalArgumentException | de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException e)
            {
                LOG.error("Error while getting instance of " + typecode, e);
            }
        }
        return false;
    }


    public static String getTenantID(HybrisDataSource ds)
    {
        assureDBUtilities();
        return dbUtilities.getTenantID(ds);
    }


    public static boolean isDBConnectionValid(String dbUrl, String dbUser, String dbPassword, String driver, String fromJNDI)
    {
        assureDBUtilities();
        return dbUtilities.isDBConnectionValid(dbUrl, dbUser, dbPassword, driver, fromJNDI);
    }


    public static boolean isDBConnectionValid(String dbUrl, String dbUser, String dbPassword, String driver)
    {
        assureDBUtilities();
        return dbUtilities.isDBConnectionValid(dbUrl, dbUser, dbPassword, driver);
    }


    public static EnumerationValue resolveEncoding(String encoding) throws UnsupportedEncodingException
    {
        EnumerationValue ev;
        try
        {
            ev = (encoding == null) ? EnumerationManager.getInstance().getEnumerationValue("EncodingEnum", CSVConstants.DEFAULT_ENCODING) : EnumerationManager.getInstance().getEnumerationValue("EncodingEnum", encoding);
        }
        catch(JaloInvalidParameterException e)
        {
            throw new UnsupportedEncodingException("unsupported encoding: '" + encoding + "'");
        }
        catch(JaloItemNotFoundException e)
        {
            throw new UnsupportedEncodingException("unsupported encoding: '" + encoding + "'");
        }
        if(ev == null)
        {
            throw new UnsupportedEncodingException("unsupported encoding: '" + encoding + "'");
        }
        return ev;
    }


    public static String resolveEncoding(EnumerationValue enc)
    {
        return (enc != null) ? ((enc.getCode() != null) ? enc.getCode() : CSVConstants.DEFAULT_ENCODING) :
                        CSVConstants.DEFAULT_ENCODING;
    }


    public static String buildStackTraceCompact(Throwable thr)
    {
        StringBuilder buf = new StringBuilder(" ");
        buf.append("/*");
        for(StackTraceElement ste : thr.getStackTrace())
        {
            String clName = ste.getClassName();
            if(clName.startsWith("org.apache.catalina"))
            {
                break;
            }
            if(clName.contains("."))
            {
                clName = clName.substring(clName.lastIndexOf('.') + 1);
            }
            buf.append(clName + ":" + clName + ":");
        }
        buf.append("END ");
        buf.append("*/");
        return buf.toString();
    }


    public static boolean arrayDeepEqualsReverse(Object[] a1, Object[] a2)
    {
        if(a1 == a2)
        {
            return true;
        }
        if(a1 == null || a2 == null)
        {
            return false;
        }
        int length = a1.length;
        if(a2.length != length)
        {
            return false;
        }
        for(int i = length - 1; i >= 0; i--)
        {
            Object e1 = a1[i];
            Object e2 = a2[i];
            if(e1 != e2)
            {
                boolean eq;
                if(e1 == null)
                {
                    return false;
                }
                if(e1 instanceof Object[] && e2 instanceof Object[])
                {
                    eq = arrayDeepEqualsReverse((Object[])e1, (Object[])e2);
                }
                else if(e1 instanceof byte[] && e2 instanceof byte[])
                {
                    eq = Arrays.equals((byte[])e1, (byte[])e2);
                }
                else if(e1 instanceof short[] && e2 instanceof short[])
                {
                    eq = Arrays.equals((short[])e1, (short[])e2);
                }
                else if(e1 instanceof int[] && e2 instanceof int[])
                {
                    eq = Arrays.equals((int[])e1, (int[])e2);
                }
                else if(e1 instanceof long[] && e2 instanceof long[])
                {
                    eq = Arrays.equals((long[])e1, (long[])e2);
                }
                else if(e1 instanceof char[] && e2 instanceof char[])
                {
                    eq = Arrays.equals((char[])e1, (char[])e2);
                }
                else if(e1 instanceof float[] && e2 instanceof float[])
                {
                    eq = Arrays.equals((float[])e1, (float[])e2);
                }
                else if(e1 instanceof double[] && e2 instanceof double[])
                {
                    eq = Arrays.equals((double[])e1, (double[])e2);
                }
                else if(e1 instanceof boolean[] && e2 instanceof boolean[])
                {
                    eq = Arrays.equals((boolean[])e1, (boolean[])e2);
                }
                else
                {
                    eq = e1.equals(e2);
                }
                if(!eq)
                {
                    return false;
                }
            }
        }
        return true;
    }


    public static void setJUnitTenant() throws IllegalStateException
    {
        SlaveTenant junitTenant = Registry.getSlaveJunitTenant();
        if(junitTenant != null)
        {
            Registry.setCurrentTenant((Tenant)junitTenant);
        }
        else if(isSystemInitialized(Registry.getMasterTenant().getDataSource()) && isMasterTenantAsTestSystem())
        {
            Registry.setCurrentTenant((Tenant)Registry.getMasterTenant());
        }
        else
        {
            throw new IllegalStateException("No junit tenant or master tenant as test system has been found");
        }
    }


    public static boolean isMasterTenantAsTestSystem()
    {
        String systemName = getTenantID(Registry.getMasterTenant().getDataSource());
        return "TestSystem".equals(systemName);
    }


    public static void printAppInfo()
    {
        AbstractTenant tenant = (AbstractTenant)Registry.getCurrentTenant();
        SessionContext context = null;
        try
        {
            Registry.activateMasterTenant();
            context = JaloSession.getCurrentSession().createLocalSessionContext();
            context.setAttribute("disableRestrictions", Boolean.TRUE);
            context.setUser((User)UserManager.getInstance().getAdminEmployee());
            LOG.info("Connected to hybris platform version " + Config.getParameter("build.version") + ", " +
                            Config.getParameter("build.builddate"));
            LOG.info("SessionID: " + JaloSession.getCurrentSession().getSessionID());
            LOG.info("Max cache size: " + Config.getParameter("cache.main"));
            LOG.info("Number of languages in system: " + C2LManager.getInstance().getAllLanguages().size());
        }
        finally
        {
            if(context != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
            Registry.setCurrentTenant((Tenant)tenant);
        }
    }


    private static final SingletonCreator.Creator<Map> SINGLETON_CREATOR = (SingletonCreator.Creator<Map>)new Object();


    private static Map getResourceBundleCache()
    {
        return (Map)Registry.getSingleton(SINGLETON_CREATOR);
    }


    private static Locale[] getLocalePath(Language lang)
    {
        List<Locale> lst = new ArrayList<>();
        Locale loc = lang.getLocale();
        if(loc != null)
        {
            lst.add(loc);
        }
        for(Language fbl : lang.getFallbackLanguages())
        {
            loc = fbl.getLocale();
            if(loc != null && !lst.contains(loc))
            {
                lst.add(loc);
            }
        }
        loc = Registry.hasCurrentTenant() ? Registry.getCurrentTenant().getTenantSpecificLocale() : Locale.getDefault();
        if(loc != null && !lst.contains(loc))
        {
            lst.add(loc);
        }
        Collections.reverse(lst);
        return lst.<Locale>toArray(new Locale[lst.size()]);
    }


    private static ResourceBundle load(String baseName, Locale loc, ClassLoader loader)
    {
        String resName = baseName.replace('.', '/') + baseName.replace('.', '/') + ".properties";
        BufferedInputStream bis = null;
        InputStream stream = null;
        try
        {
            stream = loader.getResourceAsStream(resName);
            if(stream != null)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("found " + resName + " within " + loader);
                }
                bis = new BufferedInputStream(stream);
                return (ResourceBundle)new MyResourceBundle(bis);
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("did not find " + resName + " within " + loader);
            }
        }
        catch(Exception e)
        {
            LOG.error("error loading resource '" + resName + "'", e);
        }
        finally
        {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(stream);
        }
        return null;
    }


    private static final ResourceBundle NOT_FOUND = (ResourceBundle)new Object();


    public static ResourceBundle getResourceBundle(String baseName, ClassLoader loader)
    {
        if(JaloSession.hasCurrentSession())
        {
            return getResourceBundle(JaloSession.getCurrentSession().getSessionContext(), baseName, loader);
        }
        Locale loc = Registry.hasCurrentTenant() ? Registry.getCurrentTenant().getTenantSpecificLocale() : Locale.getDefault();
        return getResourceBundle(new Locale[] {loc}, baseName, loader);
    }


    public static ResourceBundle getResourceBundle(String baseName)
    {
        return getResourceBundle(baseName, Utilities.class.getClassLoader());
    }


    public static ResourceBundle getResourceBundle(SessionContext ctx, String baseName) throws MissingResourceException
    {
        return getResourceBundle(ctx, baseName, Utilities.class.getClassLoader());
    }


    public static ResourceBundle getResourceBundle(SessionContext ctx, String baseName, ClassLoader loader) throws MissingResourceException
    {
        Preconditions.checkArgument((ctx != null));
        return getResourceBundle(ctx.getLanguage(), baseName, loader);
    }


    public static ResourceBundle getResourceBundle(Language lang, String baseName) throws MissingResourceException
    {
        return getResourceBundle(lang, baseName, Utilities.class.getClassLoader());
    }


    public static ResourceBundle getResourceBundle(Language lang, String baseName, ClassLoader loader) throws MissingResourceException
    {
        Preconditions.checkArgument((lang != null));
        return getResourceBundle(getLocalePath(lang), baseName, loader);
    }


    public static ResourceBundle getResourceBundle(Locale[] path, String baseName, ClassLoader loader) throws MissingResourceException
    {
        Preconditions.checkArgument((path != null && path.length > 0));
        ResourceBundleCacheKey key = new ResourceBundleCacheKey(baseName, path);
        ResourceBundle ret = getOrLoadBundle(key, getResourceBundleCache(), loader);
        if(ret == null)
        {
            throw new MissingResourceException("no resources found for " + baseName + " in locale " + path[path.length - 1] + " ( locales " +
                            Arrays.asList(key.path) + ")", baseName, "");
        }
        return ret;
    }


    private static ResourceBundle getOrLoadBundle(ResourceBundleCacheKey key, Map<ResourceBundleCacheKey, ResourceBundle> cache, ClassLoader loader)
    {
        ResourceBundle ret = (ResourceBundle)cache.get(key);
        if(ret == null)
        {
            synchronized(cache)
            {
                ret = (ResourceBundle)cache.get(key);
                if(ret == null)
                {
                    ret = load(key.baseName, key.getLocale(), loader);
                    if(ret == null)
                    {
                        ret = load(key.baseName, null, loader);
                        if(ret == null)
                        {
                            ret = NOT_FOUND;
                        }
                    }
                    else
                    {
                        ResourceBundleCacheKey parentKey = key.createParentKey();
                        ((MyResourceBundle)ret).setParent((parentKey != null) ? getOrLoadBundle(parentKey, cache, loader) : null);
                    }
                    cache.put(key, ret);
                }
            }
        }
        if(ret == NOT_FOUND)
        {
            ResourceBundleCacheKey parentKey = key.createParentKey();
            return (parentKey != null) ? getOrLoadBundle(parentKey, cache, loader) : null;
        }
        return ret;
    }


    public static Locale getDefaultLocale()
    {
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        Preconditions.checkArgument((ctx != null));
        return ctx.getLocale();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static Calendar getDefaultCalendar()
    {
        try
        {
            SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
            Preconditions.checkArgument((ctx != null));
            return Calendar.getInstance(ctx.getTimeZone(), ctx.getLocale());
        }
        catch(JaloSystemException systemNotInitialized)
        {
            LOG.warn("Using JDK calendar implementation due to : " + systemNotInitialized.getMessage());
            return Calendar.getInstance();
        }
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static Calendar getDefaultCalendar(Locale loc)
    {
        try
        {
            SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
            Preconditions.checkArgument((ctx != null));
            return Calendar.getInstance(ctx.getTimeZone(), (loc != null) ? loc : ctx.getLocale());
        }
        catch(JaloSystemException systemNotInitialized)
        {
            LOG.warn("Using JDK calendar implementation due to : " + systemNotInitialized.getMessage());
            return (loc != null) ? Calendar.getInstance(loc) : Calendar.getInstance();
        }
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static SimpleDateFormat getSimpleDateFormat(String pattern)
    {
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        Preconditions.checkArgument((ctx != null));
        SimpleDateFormat ret = new SimpleDateFormat(pattern, ctx.getLocale());
        ret.setCalendar(getDefaultCalendar(ctx.getLocale()));
        return ret;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static SimpleDateFormat getSimpleDateFormat(String pattern, Locale loc)
    {
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        Preconditions.checkArgument((ctx != null));
        SimpleDateFormat ret = new SimpleDateFormat(pattern, (loc != null) ? loc : ctx.getLocale());
        ret.setCalendar(getDefaultCalendar((loc != null) ? loc : ctx.getLocale()));
        return ret;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static DateFormat getDateTimeInstance()
    {
        return getDateTimeInstance(2, 2);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static DateFormat getDateTimeInstance(int dateStyle, int timeStyle)
    {
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        Preconditions.checkArgument((ctx != null));
        DateFormat df = DateFormat.getDateTimeInstance(dateStyle, timeStyle, ctx.getLocale());
        df.setCalendar(Calendar.getInstance(ctx.getTimeZone(), ctx.getLocale()));
        return df;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static DateFormat getDateTimeInstance(int dateStyle, int timeStyle, Locale loc)
    {
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        Preconditions.checkArgument((ctx != null));
        DateFormat df = DateFormat.getDateTimeInstance(dateStyle, timeStyle, (loc != null) ? loc : ctx.getLocale());
        df.setCalendar(Calendar.getInstance(ctx.getTimeZone(), ctx.getLocale()));
        return df;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static DecimalFormat getDecimalFormat(String pattern)
    {
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        Preconditions.checkArgument((ctx != null));
        return getDecimalFormat(pattern, ctx.getLocale());
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static DecimalFormat getDecimalFormat(String pattern, Locale loc)
    {
        if(pattern == null)
        {
            if(loc == null)
            {
                return (DecimalFormat)getNumberInstance();
            }
            return (DecimalFormat)getNumberInstance(loc);
        }
        if(loc == null)
        {
            return getDecimalFormat(pattern);
        }
        DecimalFormat ret = new DecimalFormat(pattern, new DecimalFormatSymbols(loc));
        ret.applyPattern(pattern);
        return ret;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static NumberFormat getIntegerInstance()
    {
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        Preconditions.checkArgument((ctx != null));
        return NumberFormat.getIntegerInstance(ctx.getLocale());
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static NumberFormat getIntegerInstance(Locale loc)
    {
        if(loc == null)
        {
            return getIntegerInstance();
        }
        return NumberFormat.getIntegerInstance(loc);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static NumberFormat getNumberInstance()
    {
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        Preconditions.checkArgument((ctx != null));
        return NumberFormat.getNumberInstance(ctx.getLocale());
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static NumberFormat getNumberInstance(Locale loc)
    {
        if(loc == null)
        {
            return getNumberInstance();
        }
        return NumberFormat.getNumberInstance(loc);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static NumberFormat getPercentInstance()
    {
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        Preconditions.checkArgument((ctx != null));
        return NumberFormat.getPercentInstance(ctx.getLocale());
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static NumberFormat getCurrencyInstance(Currency jaloCurrency)
    {
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        Preconditions.checkArgument((ctx != null));
        NumberFormat ret = NumberFormat.getCurrencyInstance(ctx.getLocale());
        if(jaloCurrency != null)
        {
            Currency.adjustDigits((DecimalFormat)ret, jaloCurrency);
            Currency.adjustSymbol((DecimalFormat)ret, jaloCurrency);
        }
        return ret;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static NumberFormat getCurrencyInstance(Currency jaloCurrency, Locale loc)
    {
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        Preconditions.checkArgument((ctx != null));
        NumberFormat ret = NumberFormat.getCurrencyInstance((loc != null) ? loc : ctx.getLocale());
        if(jaloCurrency != null)
        {
            Currency.adjustDigits((DecimalFormat)ret, jaloCurrency);
            Currency.adjustSymbol((DecimalFormat)ret, jaloCurrency);
        }
        return ret;
    }


    public static TransformerFactory getTransformerFactory()
    {
        TransformerFactory tf = null;
        try
        {
            tf = TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", null);
        }
        catch(Exception e)
        {
            if(!warnedAboutXalan)
            {
                LOG.warn("Utilities.getTransformerFactory(): cannot find our internal xalan processor, using system default.");
                warnedAboutXalan = true;
            }
        }
        if(tf == null)
        {
            tf = TransformerFactory.newInstance();
        }
        tf.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
        tf.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet", "");
        try
        {
            tf.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
        }
        catch(TransformerConfigurationException e)
        {
            if(!warnedAboutUnsecureXLMProcessing)
            {
                LOG.warn("Unable to set secure processing for TransformerFactory instance", e);
                warnedAboutUnsecureXLMProcessing = true;
            }
        }
        return tf;
    }


    public static void failLicence(String message, Licence licence)
    {
        message = "\n**************************************************************\n" + message;
        message = message + "\n**************************************************************";
        if(RedeployUtilities.isRunningInHybrisServerMode())
        {
            System.out.println("\n*** LICENSE VERIFICATION HAS FAILED! ***\n");
            System.out.println(message);
            System.out.println(licence.toString());
            System.exit(-1);
        }
        else
        {
            throw new SecurityException(message + "\n" + message);
        }
    }


    public static void failProperties(Map<String, String> missing)
    {
        if(RedeployUtilities.isRunningInHybrisServerMode())
        {
            System.err.println("**************************************************************");
            System.out.println("*** PROPERTIES VERIFICATION HAS FAILED! ENCOUNTERED MISSING MANDATORY PROPERTIES " + missing
                            .keySet() + " ***");
            System.err.println("**************************************************************");
            System.exit(-1);
        }
        else
        {
            throw new SecurityException("PROPERTIES VERIFICATION HAS FAILED! ENCOUNTERED MISSING MANDATORY PROPERTIES " + missing
                            .keySet());
        }
    }


    public static String getInfoAddress(HttpServletRequest req)
    {
        return CallingUtilities.getInfoAddress(req);
    }


    public static String createLink(SessionContext ctx, Item item) throws JaloBusinessException
    {
        assureItemUtilities();
        return itemUtilities.createLink(ctx, item);
    }


    public static String createLink(SessionContext ctx, Item item, String typeName, String attributeList) throws JaloBusinessException
    {
        assureItemUtilities();
        return itemUtilities.createLink(ctx, item, typeName, attributeList);
    }


    public static Item getItemFromLink(SessionContext ctx, String itemLink) throws JaloBusinessException, JaloInvalidParameterException, JaloItemNotFoundException
    {
        assureItemUtilities();
        return itemUtilities.getItemFromLink(ctx, itemLink);
    }


    public static String getAttributeFromLink(SessionContext ctx, String itemLink, String attributeName) throws JaloBusinessException, JaloInvalidParameterException, JaloItemNotFoundException
    {
        assureItemUtilities();
        return itemUtilities.getAttributeFromLink(ctx, itemLink, attributeName);
    }


    public static String getReplacementTypeFromLink(SessionContext ctx, String itemLink) throws JaloBusinessException, JaloInvalidParameterException, JaloItemNotFoundException
    {
        return getAttributeFromLink(ctx, itemLink, "$replacementType");
    }


    public static String getLinkIdFromLink(SessionContext ctx, String itemLink) throws JaloBusinessException, JaloInvalidParameterException, JaloItemNotFoundException
    {
        return getAttributeFromLink(ctx, itemLink, "$link_id");
    }


    public static void invalidateCache(PK primaryKey)
    {
        invalidateCache(Registry.getCurrentTenant().getCache(), primaryKey);
    }


    public static void invalidateCache(Cache cache, PK primaryKey)
    {
        invalidateCache(Transaction.current(), cache, primaryKey);
    }


    public static void invalidateCache(Transaction tx, Cache cache, PK primaryKey)
    {
        invalidateCache(tx, cache, primaryKey, false);
    }


    public static void invalidateCache(Transaction tx, Cache cache, PK primaryKey, boolean sendImmediately)
    {
        if(primaryKey != null)
        {
            if(tx == null)
            {
                throw new IllegalArgumentException("transaction is null");
            }
            if(cache == null)
            {
                throw new IllegalArgumentException("cache is null");
            }
            Object[] key = {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, PlatformStringUtils.valueOf(primaryKey
                            .getTypeCode()), primaryKey};
            if(tx.isRunning())
            {
                tx.invalidate(key, 3, 1, sendImmediately);
                tx.reloadEntityInstance(primaryKey);
            }
            else
            {
                tx.invalidate(key, 3, 1);
            }
        }
    }


    public static Object getIPAddressForLogOutput(String address)
    {
        if(address == null)
        {
            return "";
        }
        if("127.0.0.1".equals(address) || "localhost".equals(address) || address.startsWith("0:0:0:0:0:0:0:1"))
        {
            return "";
        }
        return "[" + address + "] ";
    }


    public static <T> T getCacheBoundVersion(T value)
    {
        assureItemUtilities();
        return (T)itemUtilities.getCacheBoundVersion(value);
    }


    public static Map<String, String> getMySQLSlaveStatus(HybrisDataSource dsi)
    {
        assureDBUtilities();
        return dbUtilities.getMySQLSlaveStatus(dsi);
    }


    public static final boolean equals(Object o1, Object o2)
    {
        return (o1 == o2 || (o1 != null && o1.equals(o2)));
    }


    private static String readAndCheckEncodingFromProperty()
    {
        String encoding = Config.getString("resource.bundle.encoding", "UTF-8");
        if(!Charset.isSupported(encoding))
        {
            LOG.warn("The given encoding \"" + encoding + "\" is not supported by this VM. Please change in your property file the value for the key resource.bundle.encoding to a valid one. As fallback UTF-8 is used for now.");
            return "UTF-8";
        }
        return encoding;
    }


    public static List<? extends Item> sortItemsByPK(Collection<? extends Item> items)
    {
        assureItemUtilities();
        return itemUtilities.sortItemsByPK(items);
    }


    public static Map<String, Properties> getTenantInfos()
    {
        assureCoreUtilities();
        return coreUtilities.getTenantInfos();
    }


    public static Properties loadPlatformProperties()
    {
        assureCoreUtilities();
        return coreUtilities.loadPlatformPropertiesOnce(getPlatformConfig());
    }


    public static String getTenantIdForContext(String servletContextPath, String defaultTenant)
    {
        return getPlatformConfig().getTenantForWebroot(servletContextPath, defaultTenant);
    }


    public static String getWebroot(String extensionsName)
    {
        return getWebroot(extensionsName, Registry.getCurrentTenantNoFallback().getTenantID());
    }


    public static String getWebroot(String extensionsName, String tenantID)
    {
        PlatformConfig plc = getPlatformConfig();
        if("master".equals(tenantID))
        {
            String mapping = Registry.getMasterTenant().getConfig().getParameter(extensionsName + ".webroot");
            if(mapping == null)
            {
                ExtensionInfo ext = plc.getExtensionInfo(extensionsName);
                if(ext == null)
                {
                    throw new IllegalArgumentException("unknown extension " + extensionsName);
                }
                WebExtensionModule webModule = ext.getWebModule();
                if(webModule == null)
                {
                    throw new IllegalArgumentException("no web module in extension " + extensionsName);
                }
                mapping = webModule.getWebRoot();
            }
            return mapping;
        }
        TenantInfo tenantInfo = (TenantInfo)plc.getTenantInfos().get(tenantID);
        if(tenantInfo == null)
        {
            throw new IllegalArgumentException("unknown tenant " + tenantID);
        }
        return tenantInfo.getWebMapping(extensionsName);
    }


    public static String getExtensionForWebroot(String webroot)
    {
        return getExtensionForWebroot(webroot, Registry.getCurrentTenantNoFallback().getTenantID());
    }


    public static String getExtensionNameFromRequest(HttpServletRequest request)
    {
        String webroot = request.getServletContext().getContextPath();
        String tenantIDForWebapp = HybrisContextLoaderListener.getTenantIDForWebapp(request.getServletContext());
        return getExtensionForWebroot(webroot,
                        (tenantIDForWebapp == null) ? "master" : tenantIDForWebapp);
    }


    public static String getExtensionForWebroot(String webroot, String tenantID)
    {
        if("master".equals(tenantID))
        {
            Map<String, String> customExtensionsToWebrootMappings = Registry.getMasterTenant().getConfig().getParametersMatching("([^\\.]+)\\.webroot", true);
            for(Map.Entry<String, String> extension2WebrootMapping : customExtensionsToWebrootMappings.entrySet())
            {
                if(webroot.equalsIgnoreCase(extension2WebrootMapping.getValue()))
                {
                    return extension2WebrootMapping.getKey();
                }
            }
            PlatformConfig platformConfig = getPlatformConfig();
            for(ExtensionInfo ext : platformConfig.getExtensionInfosInBuildOrder())
            {
                if(!customExtensionsToWebrootMappings.containsKey(ext.getName()) && ext.getWebModule() != null && webroot
                                .equalsIgnoreCase(ext.getWebModule().getWebRoot()))
                {
                    return ext.getName();
                }
            }
            throw new IllegalArgumentException("unknow web root '" + webroot + "' cannot find any mapped extension");
        }
        PlatformConfig plc = getPlatformConfig();
        TenantInfo tenantInfo = (TenantInfo)plc.getTenantInfos().get(tenantID);
        if(tenantInfo == null)
        {
            throw new IllegalArgumentException("unknown tenant " + tenantID);
        }
        return tenantInfo.getExtensionForWebroot(webroot);
    }


    public static boolean getRelationOrderingOverride(String cfgKey, boolean defaultValue)
    {
        Boolean value = relationOrderOverrideCache.get(cfgKey);
        if(value == null)
        {
            value = Boolean.valueOf(Config.getBoolean(cfgKey, defaultValue));
            relationOrderOverrideCache.putIfAbsent(cfgKey, value);
            if(!value.booleanValue() && defaultValue == true)
            {
                LOG.info("Ordering disabled for '" + cfgKey + "'");
            }
        }
        return value.booleanValue();
    }


    public static void clearRelationOrderingOverrideCache()
    {
        relationOrderOverrideCache.clear();
    }


    public static boolean getMarkModifiedOverride(String cfgKey)
    {
        return ((Boolean)markModifiedCache.computeIfAbsent(cfgKey, k -> {
            boolean value = Config.getBoolean(cfgKey, true);
            if(!value)
            {
                LOG.info("MarkModified disabled for '" + cfgKey + "'");
            }
            return Boolean.valueOf(value);
        })).booleanValue();
    }


    public static boolean getMarkModifiedOverride(RelationType relation)
    {
        if(relation == null)
        {
            return true;
        }
        return getMarkModifiedOverride("relation." + relation.getCode() + ".markmodified");
    }


    public static void clearMarkModifiedOverrideCache()
    {
        markModifiedCache.clear();
    }


    public static String calculateLocalCorePlusEndPoint(String extensionName, String tenantId) throws IllegalArgumentException
    {
        assureCorePlusUtilities();
        return corePlusUtilities.calculateLocalCorePlusEndPoint(extensionName, getWebroot(extensionName, tenantId));
    }


    public static String getContextFromRequestUri(String uri)
    {
        if(uri == null || uri.trim().indexOf('/') != 0 || uri.trim().indexOf('/', 1) < 0)
        {
            return (uri == null) ? null : uri.trim();
        }
        return uri.trim().substring(0, uri.indexOf('/', 1));
    }


    public static String applySpecialPropertiesToDatabaseUrl(String databaseUrl)
    {
        assureDBUtilities();
        return dbUtilities.applySpecialPropertiesToDatabaseUrl(databaseUrl);
    }


    public static boolean fuzzyEquals(double a, double b)
    {
        return DoubleMath.fuzzyEquals(a, b, DOUBLE_EQ_EPSILON);
    }
}
