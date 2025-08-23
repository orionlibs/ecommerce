package de.hybris.platform.shutdown;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class InvokeShutdown
{
    private static boolean verbose = false;


    public static void main(String[] args) throws IOException
    {
        verbose = true;
        installTrustAllCerts();
        Path token = (args != null && args.length == 1) ? getTokenPath(args[0]) : getTokenPath();
        shutdown(token);
    }


    public static Path getTokenPath()
    {
        URL location = InvokeShutdown.class.getProtectionDomain().getCodeSource().getLocation();
        Path yBootstrapPath = Paths.get(location.getPath(), new String[0]);
        Path bootstrapRelativeTokenPath = yBootstrapPath.getParent().resolve("../../../../data/shutdown.token");
        if(bootstrapRelativeTokenPath.toFile().exists())
        {
            return bootstrapRelativeTokenPath;
        }
        Path workDirRelativeFallback = Paths.get(".", new String[0]).resolve("../../data/shutdown.token");
        if(workDirRelativeFallback.toFile().exists())
        {
            return workDirRelativeFallback;
        }
        throw new RuntimeException("Couldn't find shutdown.token in data directory");
    }


    public static Path getTokenPath(String dataFolderPath)
    {
        if(dataFolderPath == null || dataFolderPath.isEmpty())
        {
            throw new RuntimeException("Path cannot be empty");
        }
        Path tokenPath = Path.of(dataFolderPath.concat("/shutdown.token"), new String[0]);
        if(!tokenPath.toFile().exists())
        {
            throw new RuntimeException("Couldn't find shutdown.token in given path: " + dataFolderPath);
        }
        return tokenPath;
    }


    public static void shutdown(Path tokenPath) throws IOException
    {
        String[] tokenContent = readTokenFile(tokenPath);
        String tokenUrl = tokenContent[0];
        String token = tokenContent[1];
        URL url = new URL(tokenUrl);
        byte[] postData = buildPostData(token);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Length", Integer.toString(postData.length));
        boolean success = true;
        try
        {
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            try
            {
                wr.write(postData);
                wr.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    wr.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(ConnectException connect)
        {
            log("Failed to connect to hybris @ " + url.toString());
            success = false;
        }
        if(success)
        {
            int code = connection.getResponseCode();
            if(code == 200)
            {
                log("Platform shutdown initiated");
            }
            else
            {
                log("Failed to initiate platform shutdown");
            }
        }
    }


    public static String[] readTokenFile(Path tokenPath)
    {
        String normalizedPath = tokenPath.toAbsolutePath().normalize().toString();
        log("Reading token file: " + normalizedPath);
        try
        {
            String tokenContent = new String(Files.readAllBytes(tokenPath));
            String[] split = tokenContent.split(";");
            if(split.length != 2)
            {
                throw new RuntimeException("Invalid shutdown token file content");
            }
            return split;
        }
        catch(IOException e)
        {
            throw new RuntimeException("Couldn't read token file");
        }
    }


    public static byte[] buildPostData(String token)
    {
        String urlParameters = "suspendToken=" + token + "&forShutdown=true";
        return urlParameters.getBytes(StandardCharsets.UTF_8);
    }


    private static void log(String msg)
    {
        if(verbose)
        {
            System.out.println(msg);
        }
    }


    public static void installTrustAllCerts()
    {
        TrustManager[] trustAllCerts = {(TrustManager)new DummyTrustManager()};
        try
        {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
        }
        catch(Exception e)
        {
            log("Failed to install DummyTrustManager");
        }
    }
}
