package de.hybris.platform.licence.sap;

import com.sap.security.core.server.likey.Admin;
import com.sap.security.core.server.likey.KeySystem;
import com.sap.security.core.server.likey.LicenseChecker;
import com.sap.security.core.server.likey.LogAndTrace;
import com.sap.security.core.server.likey.Persistence;
import com.sap.security.core.server.likey.StdLogAndTrace;
import de.hybris.bootstrap.config.ConfigUtil;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Vector;

public class HybrisAdmin extends Admin
{
    private static String[] ARGS;

    static
    {
        initializeLicenseLocales();
    }

    public HybrisAdmin(Persistence persistence, KeySystem keySystem, LogAndTrace logAndTrace)
    {
        super(persistence, keySystem, logAndTrace);
    }


    public static void main(String[] args)
    {
        if(!configFolderExists())
        {
            System.out.println("hybris config folder does not exists. Invoke 'ant clean all' first");
            return;
        }
        ARGS = args;
        Persistence persistence = getPersistence();
        DefaultKeySystem defaultKeySystem = new DefaultKeySystem();
        StdLogAndTrace stdLogAndTrace = new StdLogAndTrace();
        persistence.init();
        defaultKeySystem.init();
        HybrisAdmin admin = new HybrisAdmin(persistence, (KeySystem)defaultKeySystem, (LogAndTrace)stdLogAndTrace);
        if(ARGS.length == 0 || isCmdArgUsed("-h") || isCmdArgUsed("-help"))
        {
            printUsage((KeySystem)defaultKeySystem);
        }
        else if(isCmdArgUsed("-v") || isCmdArgUsed("-version"))
        {
            printMessage("SAP License Key version " + getVersion());
        }
        else if(isCmdArgUsed("-get") || isCmdArgUsed("-number") || isCmdArgUsed("-g") || isCmdArgUsed("-n"))
        {
            showSystemInfo(admin);
        }
        else if(isCmdArgUsed("-s") || isCmdArgUsed("-show"))
        {
            showLicenses(admin);
        }
        else if(isCmdArgUsed("-delete") || isCmdArgUsed("-d"))
        {
            deleteLicense(admin, (KeySystem)defaultKeySystem);
        }
        else if(isCmdArgUsed("-temppossible"))
        {
            checkIsTempLicensePossible(admin, (KeySystem)defaultKeySystem);
        }
        else if(isCmdArgUsed("-temp") || isCmdArgUsed("-t"))
        {
            installTempLicense(admin, (KeySystem)defaultKeySystem);
        }
        else if(isCmdArgUsed("-install") || isCmdArgUsed("-i"))
        {
            installLicenseFromFile(admin, (KeySystem)defaultKeySystem);
        }
        else if(isCmdArgUsed("-dump"))
        {
            admin.dumpPersistence();
        }
        else
        {
            printMessage("Invalid option \"" + args[0] + "\" specified!");
            printUsage((KeySystem)defaultKeySystem);
        }
    }


    private static boolean configFolderExists()
    {
        File configDir = new File(ConfigUtil.getConfigDirPath(HybrisAdmin.class));
        return configDir.exists();
    }


    private static Persistence getPersistence()
    {
        String persistenceImpl = System.getProperty("persistence.impl");
        if(persistenceImpl == null)
        {
            return (Persistence)new DefaultPersistence();
        }
        return getInstanceFromString(persistenceImpl);
    }


    private static <T> T getInstanceFromString(String className)
    {
        try
        {
            Class<?> persistenceClass = Class.forName(className);
            return (T)persistenceClass.newInstance();
        }
        catch(Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    private static boolean isCmdArgUsed(String cmdArg)
    {
        return (cmdArg != null && cmdArg.equals(ARGS[0]));
    }


    private static void printUsage(KeySystem keySystem)
    {
        Method printHelp = getMethodAsPublic(Admin.class, "printHelp", new Class[] {KeySystem.class});
        invokeStaticMethod(printHelp, new Object[] {keySystem});
    }


    private static void showSystemInfo(HybrisAdmin admin)
    {
        Method printSystemData = getMethodAsPublic(Admin.class, "printSystemData", new Class[0]);
        invokeMethod(printSystemData, admin, new Object[0]);
    }


    private static void showLicenses(HybrisAdmin admin)
    {
        Method printLiKeys = getMethodAsPublic(Admin.class, "printLiKeys", new Class[0]);
        invokeMethod(printLiKeys, admin, new Object[0]);
    }


    private static void deleteLicense(HybrisAdmin admin, KeySystem keySystem)
    {
        checkArgs(length(4), "Usage: " + keySystem.getCmdPrefix() + " -delete <System Id> <Hardware Key> <Product>");
        Vector<String> messages = new Vector<>();
        admin.deleteLicenses(ARGS[1], ARGS[2], ARGS[3], messages);
        printMessages(messages);
    }


    private static void checkIsTempLicensePossible(HybrisAdmin admin, KeySystem keySystem)
    {
        checkArgs(length(2), "Usage: " + keySystem.getCmdPrefix() + " -temppossible <Product>\nName of the software product missing.");
        Vector<String> messages = new Vector<>();
        System.out.print("A temporary license key for " + ARGS[1] + " may ");
        if(licenseExists(admin.getSWProductsWithLikey(), ARGS[1]))
        {
            boolean allowed = admin.isExtendable(keySystem.getSystemId(), keySystem.getHwId(), ARGS[1], messages);
            if(!allowed)
            {
                System.out.print(" not ");
            }
        }
        System.out.println("be installed.");
        printMessages(messages);
    }


    private static void installTempLicense(Admin admin, KeySystem keySystem)
    {
        checkArgs(length(2), "Usage: " + keySystem.getCmdPrefix() + " -temp <Product>\nName of the software product missing.");
        Vector<String> messages = new Vector<>();
        if(licenseExists(admin.getSWProductsWithLikey(), ARGS[1]))
        {
            admin.installSubsequentTempLicense(ARGS[1], messages);
        }
        else
        {
            boolean result = admin.installFirstTempLicense(ARGS[1], messages);
            if(result)
            {
                messages.add("First temporary license key installed.");
            }
        }
        printMessages(messages);
    }


    public static boolean licenseExists(String[] productsWithLikey, String swProduct)
    {
        if(productsWithLikey == null)
        {
            return false;
        }
        for(String _swProduct : productsWithLikey)
        {
            if(_swProduct.equals(swProduct))
            {
                return true;
            }
        }
        return false;
    }


    private static void installLicenseFromFile(HybrisAdmin admin, KeySystem keySystem)
    {
        checkArgs(length(2), "Usage: " + keySystem.getCmdPrefix() + " -install <path to file>\nPath to license file missing.");
        try
        {
            Vector<String> messages = new Vector<>();
            admin.installFromFile(ARGS[1], messages);
            printMessages(messages);
        }
        catch(Exception e)
        {
            printMessage("License key(s) not completely installed.");
            printMessage("Error occured: " + e);
        }
    }


    private static void printMessage(String message)
    {
        Method formatAndPrint = getMethodAsPublic(StdLogAndTrace.class, "formatAndPrint", new Class[] {PrintStream.class, String.class});
        invokeStaticMethod(formatAndPrint, new Object[] {System.out, message});
    }


    private static void printMessages(Vector<String> messages)
    {
        Method printMessages = getMethodAsPublic(Admin.class, "printMessages", new Class[] {Vector.class});
        invokeStaticMethod(printMessages, new Object[] {messages});
    }


    private static void checkArgs(int validLength, String validationMsg)
    {
        if(ARGS.length != validLength)
        {
            printMessage(validationMsg);
            System.exit(-1);
        }
    }


    private static int length(int length)
    {
        return length;
    }


    private static void invokeStaticMethod(Method method, Object... args)
    {
        try
        {
            method.invoke(null, args);
        }
        catch(Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    private static void invokeMethod(Method method, Object targetObj, Object... args)
    {
        try
        {
            method.invoke(targetObj, args);
        }
        catch(Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    private static Method getMethodAsPublic(Class targetClass, String methodName, Class<?>... args)
    {
        try
        {
            Method declaredMethod = targetClass.getDeclaredMethod(methodName, args);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        }
        catch(Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


    public static void initializeLicenseLocales()
    {
        Locale originLocale = Locale.getDefault();
        changeDefaultLocale(Locale.ROOT);
        try
        {
            Field dateformatter = LicenseChecker.class.getDeclaredField("dateformatter");
            dateformatter.setAccessible(true);
            dateformatter.get(null);
        }
        catch(Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
        finally
        {
            changeDefaultLocale(originLocale);
        }
    }


    private static void changeDefaultLocale(Locale locale)
    {
        Locale.setDefault(locale);
        System.setProperty("user.language", Locale.getDefault().getLanguage());
        System.setProperty("user.country", Locale.getDefault().getCountry());
    }
}
