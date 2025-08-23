package de.hybris.platform.mediaconversion.imagemagick;

import de.hybris.platform.mediaconversion.conversion.ImageMagickSecurityService;
import de.hybris.platform.mediaconversion.os.Drain;
import de.hybris.platform.mediaconversion.os.OsConfigurationService;
import de.hybris.platform.mediaconversion.os.ProcessExecutor;
import de.hybris.platform.mediaconversion.os.process.StringDrain;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Required;

public class DefaultImageMagickService extends BasicImageMagickStrategy implements ImageMagickService
{
    @Deprecated(since = "6.1", forRemoval = true)
    public static final String EMBEDDED_BIN_DIR = "/mediaconversion/imagemagick/bin/";
    public static final String IMAGEMAGICK_DIR = "imagemagick.bindir";
    public static final String IMAGEMAGICK_EXECUTABLE_KEY_PREFIX = "imagemagick.executable.";
    public static final String DYLD_LIBRARY_PATH = "imagemagick.dyld.library.directory";
    public static final String DYLD_LIBRARY_PATH_ENV = "DYLD_LIBRARY_PATH";
    public static final String CONVERT_EXEC = "convert";
    public static final String IDENTIFY_EXEC = "identify";
    private ProcessExecutor processExecutor;
    private OsConfigurationService osConfigurationService;
    private ImageMagickSecurityService securityService;
    private final Drain outLogDrain = (Drain)new LoggingDrain(getClass(), Level.DEBUG);
    private final Drain errLogDrain = (Drain)new LoggingDrain(getClass(), Level.ERROR);
    private String[] environmentCache;


    protected String[] buildEnvironment(File executable) throws IOException
    {
        String confDir = getConfigurationDirectory().getAbsolutePath();
        String temp = getTmpDir().getAbsolutePath();
        List<String> ret = new LinkedList<>();
        for(ImageMagickEnvironment env : ImageMagickEnvironment.values())
        {
            String value = env.retrieveValue(confDir, temp);
            if(value != null)
            {
                ret.add("" + env + "=" + env);
            }
        }
        setDynamicLibraryPathForMac(ret);
        return ret.<String>toArray(new String[ret.size()]);
    }


    private void setDynamicLibraryPathForMac(List<String> envs)
    {
        envs.add("DYLD_LIBRARY_PATH=" + getConfigurationService().getConfiguration().getString("imagemagick.dyld.library.directory", null));
    }


    private File retrieveExecutable(String exec) throws IOException
    {
        Configuration conf = getConfigurationService().getConfiguration();
        String executable = conf.getString("imagemagick.executable." + exec, exec);
        String ret = conf.getString("imagemagick.bindir");
        if(ret != null && !ret.isEmpty())
        {
            return new File(ret, executable);
        }
        throw new IOException("Failed to find ImageMagick root directory. Please adjust the property: imagemagick.bindir to your local Image Magick installation");
    }


    public void convert(List<String> commandOpts) throws IOException
    {
        validateCommandOpts(commandOpts);
        File executable = retrieveExecutable("convert");
        List<String> command = new LinkedList<>();
        command.add(executable.getAbsolutePath());
        command.addAll(commandOpts);
        TeeDrain errOut = new TeeDrain(this.errLogDrain);
        int exitCode = getProcessExecutor().execute(command.<String>toArray(new String[command.size()]),
                        buildEnvironment(executable), null, this.outLogDrain, (Drain)errOut);
        if(exitCode != 0)
        {
            throw new IOException("Conversion failed. Process exited with code " + exitCode + ": \n" + errOut);
        }
    }


    public String identify(List<String> commandOptions) throws IOException
    {
        validateCommandOpts(commandOptions);
        File executable = retrieveExecutable("identify");
        List<String> command = new LinkedList<>();
        command.add(executable.getAbsolutePath());
        command.addAll(commandOptions);
        StringDrain out = new StringDrain();
        int exitCode = getProcessExecutor().execute(command.<String>toArray(new String[command.size()]),
                        getEnvironment(executable), null, (Drain)out, this.errLogDrain);
        if(exitCode != 0)
        {
            throw new IOException("Identification failed. Process exited with code " + exitCode + ".");
        }
        return out.toString();
    }


    private void validateCommandOpts(List<String> opts)
    {
        ImageMagickSecurityService.ConvertCommandValidationResult validationResult = this.securityService.isCommandSecure(opts);
        if(!validationResult.isSecure())
        {
            throw new IllegalStateException(validationResult.getMessage());
        }
    }


    private String[] getEnvironment(File executable) throws IOException
    {
        if(this.environmentCache == null)
        {
            this.environmentCache = buildEnvironment(executable);
        }
        return this.environmentCache;
    }


    public ProcessExecutor getProcessExecutor()
    {
        return this.processExecutor;
    }


    @Required
    public void setProcessExecutor(ProcessExecutor processExecutor)
    {
        this.processExecutor = processExecutor;
    }


    public OsConfigurationService getOsConfigurationService()
    {
        return this.osConfigurationService;
    }


    @Required
    public void setOsConfigurationService(OsConfigurationService osDirectoryService)
    {
        this.osConfigurationService = osDirectoryService;
    }


    @Required
    public void setSecurityService(ImageMagickSecurityService securityService)
    {
        this.securityService = securityService;
    }
}
