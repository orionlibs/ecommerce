package de.hybris.platform.impex.systemsetup;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.util.Config;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.PathMatcher;

@SystemSetup(extension = "ALL_EXTENSIONS")
public class ImpExSystemSetup implements InitializingBean
{
    private static final Logger LOG = Logger.getLogger(ImpExSystemSetup.class);
    private static final String ROOT_DIRECTORY = "resources";
    private static final String DEFAULT_DIRECTORY = "resources/impex";
    private static final String DEFAULT_PROJECT_PATTERN = "resources/impex/projectdata*.impex";
    private static final String DEFAULT_ESSENTIAL_PATTERN = "resources/impex/essentialdata*.impex";
    public static final String PARAMETER_ESSENTIAL = "essentialdata-impex-pattern";
    public static final String PARAMETER_PROJECT = "projectdata-impex-pattern";
    private PathMatcher pathMatcher;


    public void afterPropertiesSet() throws Exception
    {
    }


    @SystemSetup(type = SystemSetup.Type.ESSENTIAL, process = SystemSetup.Process.ALL)
    public void createAutoImpexEssentialData(SystemSetupContext context)
    {
        String path = "resources";
        String patternCfg = Config.getParameter(context.getExtensionName() + ".essentialdata-impex-pattern");
        if(StringUtils.isEmpty(patternCfg))
        {
            path = "resources/impex";
            patternCfg = "resources/impex/essentialdata*.impex";
        }
        else if(!StringUtils.startsWithIgnoreCase(patternCfg, "resources"))
        {
            patternCfg = "resources/" + patternCfg;
        }
        if(!"resources/impex/essentialdata*.impex".equals(patternCfg))
        {
            LOG.info("AutoImpEx for extension '" + context
                            .getExtensionName() + "' will use userdefined filepattern '" + patternCfg + "' for creating the essential data...");
        }
        importData(context.getExtensionName(), path, patternCfg);
    }


    @SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
    public void createAutoImpexProjectData(SystemSetupContext context)
    {
        String path = "resources";
        String patternCfg = Config.getParameter(context.getExtensionName() + ".projectdata-impex-pattern");
        if(StringUtils.isEmpty(patternCfg))
        {
            path = "resources/impex";
            patternCfg = "resources/impex/projectdata*.impex";
        }
        else if(!StringUtils.startsWithIgnoreCase(patternCfg, "resources"))
        {
            patternCfg = "resources/" + patternCfg;
        }
        if(Boolean.parseBoolean(System.getProperty("glassfish", "false")))
        {
            patternCfg = patternCfg.replace(path, context.getExtensionName());
        }
        if(!"resources/impex/projectdata*.impex".equals(patternCfg))
        {
            LOG.info("AutoImpEx for extension '" + context
                            .getExtensionName() + "' will use userdefined filepattern '" + patternCfg + "' for creating the project data...");
        }
        importData(context.getExtensionName(), path, patternCfg);
    }


    private void importData(String extensionName, String path, String expression)
    {
        ExtensionInfo extension = ConfigUtil.getPlatformConfig(ImpExSystemSetup.class).getExtensionInfo(extensionName);
        for(String file : getFiles(extension, path, expression))
        {
            importCSVFromResources(file);
        }
    }


    protected void importCSVFromResources(String csv)
    {
        String csvWithoutResourceFolder = csv.replace("resources/", "/");
        LOG.info("importing resource : " + csvWithoutResourceFolder);
        String csvWithoutResourceFolderWithSlash = (csvWithoutResourceFolder.charAt(0) == '/') ? csvWithoutResourceFolder : ("/" + csvWithoutResourceFolder);
        InputStream inputstream = ImpExSystemSetup.class.getResourceAsStream(csvWithoutResourceFolderWithSlash);
        printWarningIfMultipleFilesExistForResourceName(csvWithoutResourceFolderWithSlash);
        ImpExManager impexManager = ImpExManager.getInstance();
        impexManager.importData(inputstream, "UTF-8", ';', '"', true);
    }


    private void printWarningIfMultipleFilesExistForResourceName(String resourceName)
    {
        try
        {
            List<String> pathNames = getPathsNamesForResource(resourceName);
            if(pathNames.size() > 1)
            {
                StringBuilder filePaths = new StringBuilder();
                for(String path : pathNames)
                {
                    if(filePaths.length() > 0)
                    {
                        filePaths.append(", ");
                    }
                    filePaths.append(path);
                }
                LOG.warn(String.format("When importing %s resource, the following duplicates were found: %s. Change them as file names must be unique.", new Object[] {resourceName, filePaths
                                .toString()}));
            }
        }
        catch(IOException | URISyntaxException ex)
        {
            LOG.debug("Exception when verifying duplicate file names during import", ex);
        }
    }


    List<String> getPathsNamesForResource(String resourceName) throws IOException, URISyntaxException
    {
        if(resourceName.startsWith("/"))
        {
            resourceName = resourceName.substring(1);
        }
        Enumeration<URL> urls = getClass().getClassLoader().getResources(resourceName);
        List<String> pathNames = new ArrayList<>();
        while(urls.hasMoreElements())
        {
            pathNames.add(Paths.get(((URL)urls.nextElement()).toURI()).toString());
        }
        return pathNames;
    }


    private List<String> getFiles(ExtensionInfo extension, String path, String regex)
    {
        if(Boolean.parseBoolean(System.getProperty("glassfish", "false")))
        {
            return getResourcesPathsForExtension(extension.getName(), regex);
        }
        return scanDir(extension, path, regex);
    }


    private List<String> getResourcesPathsForExtension(String extensionName, String regex)
    {
        List<String> foundFiles = new ArrayList<>();
        InputStream input = getClass().getClassLoader().getResourceAsStream(extensionName + ".index.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
        try
        {
            String fileName;
            while((fileName = bufferedReader.readLine()) != null)
            {
                boolean accept = filePathMatches(fileName, regex.replace("resources/", ""));
                if(accept)
                {
                    foundFiles.add(fileName);
                }
            }
        }
        catch(IOException e)
        {
            LOG.error("Error while loading resources list for extension: " + extensionName + ".", e);
        }
        finally
        {
            try
            {
                input.close();
            }
            catch(IOException e)
            {
                LOG.error("Error closing stream", e);
            }
        }
        return foundFiles;
    }


    private List<String> scanDir(ExtensionInfo extension, String path, String regex)
    {
        List<String> foundFiles = new ArrayList<>();
        File extensionDirectory = new File(extension.getExtensionDirectory(), "");
        File directory = new File(extensionDirectory, path);
        if(directory.isDirectory())
        {
            File[] files = directory.listFiles();
            Arrays.sort((Object[])files);
            for(File file : files)
            {
                String filePath = createPathString(file, path);
                if(file.isDirectory())
                {
                    foundFiles.addAll(getFiles(extension, filePath, regex));
                }
                else if(filePathMatches(filePath, regex))
                {
                    foundFiles.add(filePath);
                }
            }
        }
        return foundFiles;
    }


    private boolean filePathMatches(String filePath, String regex)
    {
        return this.pathMatcher.match(regex, filePath.replace("\\", "/"));
    }


    private String createPathString(File file, String path)
    {
        if(StringUtils.isNotEmpty(path))
        {
            return path + "/" + path;
        }
        return file.getName();
    }


    public void setPathMatcher(PathMatcher pathMatcher)
    {
        this.pathMatcher = pathMatcher;
    }
}
