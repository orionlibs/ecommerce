package de.hybris.bootstrap.loader;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.naming.OperationNotSupportedException;

public class Loader
{
    private static final String EXECUTER_CLASS = "de.hybris.platform.util.ClientExecuter";
    private static final String EXECUTER_METHOD = "execute";


    public static void main(String[] args) throws Exception
    {
        if(args.length == 0)
        {
            System.err.println("Please give java line that should be executed as argument!");
            System.err.println("e.g. java -jar ybootstrap.jar");
            System.err.println("           [-deployname <deployname>]");
            System.err.println("           [-platformhome <dir>]");
            System.err.println("           [-cp <addclpath>]");
            System.err.println("           [-loadhybris <true*|false>]");
            System.err.println("           [-systeminit <true*|false>]");
            System.err.println("           -file <bshscriptfile> OR \"new de.hybris.SomeClass();\" ");
            System.err.println("");
            System.err.println("As first test you can use");
            System.err.println("   java -jar bootstrap" + File.separator + "bin" + File.separator + "ybootstrap.jar \"de.hybris.platform.util.Utilities.printAppInfo();\"");
            System.err.println("if your current directory is the platform home directory.");
            System.exit(0);
        }
        String platHomeString = ".";
        String deployname = "client";
        boolean loadHybris = true;
        boolean systemInit = false;
        String additionalClasspath = "";
        int idx = 0;
        if(args[idx].equals("-deployname"))
        {
            idx++;
            deployname = args[idx];
            idx++;
        }
        if(args[idx].equals("-platformhome"))
        {
            idx++;
            platHomeString = args[idx];
            idx++;
        }
        if(args[idx].equals("-cp"))
        {
            idx++;
            additionalClasspath = args[idx];
            idx++;
        }
        if(args[idx].equals("-loadhybris"))
        {
            idx++;
            loadHybris = Boolean.parseBoolean(args[idx]);
            idx++;
        }
        if(args[idx].equals("-systeminit"))
        {
            idx++;
            systemInit = Boolean.parseBoolean(args[idx]);
            idx++;
        }
        StringBuilder toexecute = new StringBuilder();
        if(args[idx].equals("-file"))
        {
            idx++;
            LineNumberReader reader = new LineNumberReader(new FileReader(args[idx]));
            try
            {
                String line;
                while((line = reader.readLine()) != null)
                {
                    toexecute.append(line).append("\n");
                }
                reader.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    reader.close();
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
            toexecute = new StringBuilder(args[idx]);
        }
        int status = 1;
        try
        {
            execute(deployname, platHomeString, additionalClasspath, toexecute.toString(), loadHybris, systemInit);
            status = 0;
        }
        catch(Exception e)
        {
            throw e;
        }
        finally
        {
            System.exit(status);
        }
    }


    public static void execute(String deployname, String platformHome, String additionalClasspath, String toexecute, boolean loadHybris, boolean systemInit) throws Exception
    {
        if(platformHome == null)
        {
            throw new OperationNotSupportedException("exploded ear classloader not yet implemented.");
        }
        ClassLoader parent = ClassLoader.getPlatformClassLoader();
        PlatformInPlaceClassLoader loader = new PlatformInPlaceClassLoader(platformHome, additionalClasspath, parent, true);
        Thread.currentThread().setContextClassLoader((ClassLoader)loader);
        try
        {
            Class<?> clazz = Class.forName("de.hybris.platform.util.ClientExecuter", false, (ClassLoader)loader);
            Method method = clazz.getMethod("execute", new Class[] {String.class, boolean.class, boolean.class});
            method.invoke(null, new Object[] {toexecute, Boolean.valueOf(loadHybris), Boolean.valueOf(systemInit)});
        }
        catch(ClassNotFoundException e)
        {
            throw new IllegalArgumentException("Could not load class de.hybris.platform.util.ClientExecuter", e);
        }
        catch(InvocationTargetException | IllegalAccessException e)
        {
            throw e;
        }
        catch(NoSuchMethodException e)
        {
            throw new IllegalArgumentException("Could not load a method execute for class de.hybris.platform.util.ClientExecuter", e);
        }
    }
}
