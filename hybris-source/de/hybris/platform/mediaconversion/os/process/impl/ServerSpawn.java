package de.hybris.platform.mediaconversion.os.process.impl;

import de.hybris.platform.mediaconversion.os.process.rmi.ProcessExecutorServer;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

final class ServerSpawn
{
    private static final Logger LOG = Logger.getLogger(ServerSpawn.class);


    static String buildClasspath() throws IOException
    {
        try
        {
            File extensionRoot = (new File(ProcessExecutorClient.class.getClassLoader().getResource("os/location_tag.txt").toURI())).getAbsoluteFile().getParentFile().getParentFile().getParentFile();
            StringBuilder ret = new StringBuilder();
            File classesDir = new File(extensionRoot, "classes");
            if(classesDir.isDirectory() && classesDir.exists())
            {
                ret.append(classesDir.getAbsolutePath());
            }
            File osJar = new File(new File(extensionRoot, "bin"), "mediaconversionserver.jar");
            if(osJar.isFile() && osJar.canRead())
            {
                if(ret.length() > 0)
                {
                    ret.append(File.pathSeparatorChar);
                }
                ret.append(osJar.getAbsolutePath());
            }
            return ret.toString();
        }
        catch(URISyntaxException e)
        {
            throw new IOException("Failed to locate extension root directory.", e);
        }
    }


    private static void pipe(Level level, BufferedReader bufferedReader)
    {
        Object object = new Object("ProcessExecutor" + level + "Out", bufferedReader, level);
        object.start();
    }


    private static void pipe(Level level, InputStream inStream)
    {
        try
        {
            pipe(level, new BufferedReader(new InputStreamReader(inStream, "UTF-8")));
        }
        catch(UnsupportedEncodingException e)
        {
            throw new IllegalStateException("UTF-8 is not a supported encoding.", e);
        }
    }


    private static void readOK(BufferedReader bufferedReader) throws IOException
    {
        String line = bufferedReader.readLine();
        if("EMMA: collecting runtime coverage data ...".equals(line))
        {
            LOG.debug("Skipping emma tool announce: " + line);
            readOK(bufferedReader);
        }
        else if("OK".equals(line))
        {
            LOG.debug("RMIServer started successfully.");
        }
        else
        {
            throw new IOException("Invalid start confirmation '" + line + "'.");
        }
    }


    static void spawnServer(String javaExe, String classpath, String[] javaOpts, InetAddress loopback, int port, String code) throws IOException
    {
        spawnServer(javaExe, classpath, javaOpts, loopback.getHostName(), port, code);
    }


    static void spawnServer(String javaExe, String classpath, String[] javaOpts, String loopback, int port, String code) throws IOException
    {
        List<String> cmd = new LinkedList<>();
        cmd.add(javaExe);
        if(javaOpts != null && javaOpts.length > 0)
        {
            for(String opt : javaOpts)
            {
                if(opt == null)
                {
                    LOG.warn("Skipping null entry in JVM opts.");
                }
                else
                {
                    String trimOpt = opt.trim();
                    if(trimOpt.isEmpty())
                    {
                        LOG.warn("Skipping empty entry in JVM opts.");
                    }
                    else
                    {
                        cmd.add(opt);
                    }
                }
            }
        }
        cmd.add("-cp");
        cmd.add(classpath);
        cmd.add(ProcessExecutorServer.class.getName());
        cmd.add(code);
        cmd.add(loopback);
        cmd.add(Integer.toString(port));
        String[] cmdArr = cmd.<String>toArray(new String[cmd.size()]);
        LOG.info("Spawning process executor RMI server: " + Arrays.toString((Object[])cmdArr));
        Process process = Runtime.getRuntime().exec(cmdArr);
        pipe(Level.ERROR, process.getErrorStream());
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
        readOK(bufferedReader);
        pipe(Level.INFO, bufferedReader);
    }
}
