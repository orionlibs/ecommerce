package de.hybris.platform.cockpit.systemsetup;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.platform.cockpit.jalo.CockpitManager;
import de.hybris.platform.cockpit.jalo.template.CockpitItemTemplate;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.initialization.SystemSetupContext;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.StringUtils;

@SystemSetup(extension = "ALL_EXTENSIONS")
public class CockpitImportConfig
{
    private static final Logger LOG = LoggerFactory.getLogger(CockpitImportConfig.class);
    private static final String MEDIA_HEADER = "INSERT_UPDATE CockpitUIConfigurationMedia;code[unique=true];mime;realfilename;@media[translator=de.hybris.platform.impex.jalo.media.MediaDataTranslator][forceWrite=true]";
    private static final String COCKPIT_UICOMPONENT_CONFIGURATION_HEADER = "INSERT_UPDATE CockpitUIComponentConfiguration;code[unique=true];factoryBean;objectTemplateCode[unique=true];principal(uid)[unique=true];media(code)";
    private static final String COCKPIT_CLASSNAME = "de.hybris.platform.cockpit.jalo.CockpitManager";
    private StringBuilder media;
    private StringBuilder cockpitUIComponentConfiguration;
    private Map<String, String> ctxID2FactoryMappings = null;
    private TypeService typeService = null;
    private ModelService modelService = null;
    private FlexibleSearchService flexibleSearchService;
    private final XmlFileFilter directoriesPermittedFileFilter = new XmlFileFilter(true);
    private final XmlFileFilter directoriesSkippedFileFilter = new XmlFileFilter(false);


    @Required
    public void setCtxID2FactoryMappings(Map<String, String> ctxID2FactoryMappings)
    {
        this.ctxID2FactoryMappings = ctxID2FactoryMappings;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
    public void importCockpitConfig(SystemSetupContext context)
    {
        ExtensionInfo extensionInfo = ConfigUtil.getPlatformConfig(getClass()).getExtensionInfo(context
                        .getExtensionName());
        if(findAndPrepareConfig(extensionInfo))
        {
            LOG.info("Importing cockpit configuration files for extension: " + extensionInfo.getName());
            importConfig(this.media.append("\n\n").append(this.cockpitUIComponentConfiguration));
        }
    }


    private boolean findAndPrepareConfig(ExtensionInfo extensionInfo)
    {
        File resourceDirectory = new File(extensionInfo.getExtensionDirectory(), "resources");
        if(resourceDirectory.isDirectory())
        {
            List<File> files = Arrays.asList(resourceDirectory.listFiles());
            for(File file : files)
            {
                if(file.isDirectory() && file.getName().equals(extensionInfo.getName() + "-config"))
                {
                    this.media = new StringBuilder("\nINSERT_UPDATE CockpitUIConfigurationMedia;code[unique=true];mime;realfilename;@media[translator=de.hybris.platform.impex.jalo.media.MediaDataTranslator][forceWrite=true]\n");
                    this.cockpitUIComponentConfiguration = new StringBuilder("INSERT_UPDATE CockpitUIComponentConfiguration;code[unique=true];factoryBean;objectTemplateCode[unique=true];principal(uid)[unique=true];media(code)\n");
                    scanConfig(file);
                    return true;
                }
            }
        }
        return false;
    }


    private void scanConfig(File configFolder)
    {
        List<File> uidFiles = Arrays.asList(configFolder.listFiles((FileFilter)this.directoriesPermittedFileFilter));
        abortProcessingIfFolderIsEmpty(uidFiles, "This directory doesn't have any uid folders and xml files:", "Remove or fill this configuration folder with uid folders.\n", new String[] {configFolder
                        .getName()});
        for(File uidFile : uidFiles)
        {
            if(uidFile.isDirectory())
            {
                scanUidFolder(configFolder, uidFile);
                continue;
            }
            List<String> list = scanAndCheckFileName(configFolder.getName(), uidFile, null);
            createCsvContent(configFolder.getName(), uidFile, list);
        }
    }


    private void scanUidFolder(File configFolder, File uidFolder)
    {
        List<File> configFiles = Arrays.asList(uidFolder.listFiles((FileFilter)this.directoriesSkippedFileFilter));
        abortProcessingIfFolderIsEmpty(configFiles, "This directory doesn't have any xml files:", "Remove or fill this configuration folder with xml files.\n", new String[] {configFolder
                        .getName(), uidFolder.getName()});
        for(File configFile : configFiles)
        {
            List<String> list = scanAndCheckFileName(configFolder.getName(), configFile, uidFolder);
            createCsvContent(configFolder.getName(), configFile, list);
        }
    }


    private List<String> scanAndCheckFileName(String configFolderName, File configFile, File uidFile)
    {
        String stringToScan = configFile.getName().substring(0, configFile.getName().lastIndexOf("."));
        List<String> list = new ArrayList<>(Arrays.asList(stringToScan.split("_")));
        if(list.size() != 2)
        {
            throw new CockpitImportConfigException("Invalid configuration file name: ../resources/" + configFolderName + "/" + configFile
                            .getName() + "\n");
        }
        checkContextID(configFolderName, configFile, list.get(0));
        checkObjectTemplateCode(configFolderName, configFile, list.get(1));
        if(uidFile != null)
        {
            checkUserID(configFolderName, configFile, uidFile.getName());
            list.add(uidFile.getName());
        }
        else
        {
            list.add(null);
        }
        String factoryName = this.ctxID2FactoryMappings.get(list.get(0));
        list.add(factoryName);
        return list;
    }


    private void checkContextID(String configFolderName, File configFile, String contextID)
    {
        if(this.ctxID2FactoryMappings.get(contextID) == null)
        {
            throw new CockpitImportConfigException("Invalid contextID (" + contextID + ") within configuration file name: ../resources/" + configFolderName + "/" + configFile
                            .getName() + "\n");
        }
    }


    private void checkObjectTemplateCode(String configFolderName, File configFile, String objectTemplateCode)
    {
        try
        {
            String[] delimitedObjectTemplate = StringUtils.delimitedListToStringArray(objectTemplateCode, ".");
            ComposedTypeModel composedTypeModel = this.typeService.getComposedTypeForCode(delimitedObjectTemplate[0]);
            if(delimitedObjectTemplate.length == 2)
            {
                CockpitItemTemplate objectTemplate = CockpitManager.getInstance().getCockpitItemTemplate((ComposedType)this.modelService
                                .getSource(composedTypeModel), delimitedObjectTemplate[1]);
                if(objectTemplate == null)
                {
                    throw new CockpitImportConfigException("Invalid objectTemplateCode (" + objectTemplateCode + ") within configuration file name: ../resources/" + configFolderName + "/" + configFile
                                    .getName() + "\n");
                }
            }
        }
        catch(UnknownIdentifierException uie)
        {
            throw new CockpitImportConfigException("Invalid objectTemplateCode (" + objectTemplateCode + ") within configuration file name: ../resources/" + configFolderName + "/" + configFile
                            .getName() + "\n", uie);
        }
    }


    private void checkUserID(String configFolderName, File configFile, String userID)
    {
        try
        {
            PrincipalModel principalModel = new PrincipalModel();
            principalModel.setUid(userID);
            this.flexibleSearchService.getModelByExample(principalModel);
        }
        catch(ModelNotFoundException mnfe)
        {
            throw new CockpitImportConfigException("Invalid UID (" + userID + ") within configuration  file name: ../resources/" + configFolderName + "/" + configFile
                            .getName() + "\n", mnfe);
        }
    }


    private void createCsvContent(String configFolderName, File configFile, List<String> list)
    {
        String xmlFileName = configFile.getName();
        String contextID = list.get(0);
        String objectTemplateCode = list.get(1);
        String userID = list.get(2);
        String factory = list.get(3);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("uid: " + userID);
            LOG.debug("objectTemplateCode: " + objectTemplateCode);
            LOG.debug("factoryBean: " + factory);
            LOG.debug("code: " + contextID);
            LOG.debug("xmlFileName: " + xmlFileName + "\n\n");
        }
        StringBuilder mediaUniqueCode = new StringBuilder();
        if(userID != null)
        {
            mediaUniqueCode.append(userID).append("_");
        }
        mediaUniqueCode.append(objectTemplateCode).append("_").append(factory).append("_").append(contextID);
        constructMediaRow(mediaUniqueCode.toString(), configFolderName, userID, xmlFileName);
        constructCockpitUIComponentConfigurationRow(mediaUniqueCode.toString(), userID, objectTemplateCode, factory, contextID);
    }


    private void constructMediaRow(String mediaUniqueCode, String config, String uid, String xmlFileName)
    {
        this.media.append(";").append(mediaUniqueCode);
        this.media.append(";").append("text/xml");
        this.media.append(";").append(xmlFileName);
        this.media.append(";").append("jar:").append("de.hybris.platform.cockpit.jalo.CockpitManager").append("&/").append(config).append("/");
        if(uid != null)
        {
            this.media.append(uid).append("/");
        }
        this.media.append(xmlFileName);
        this.media.append("\n");
    }


    private void constructCockpitUIComponentConfigurationRow(String mediaUniqueCode, String uid, String objectTemplateCode, String factoryBean, String code)
    {
        this.cockpitUIComponentConfiguration.append(";").append(code);
        this.cockpitUIComponentConfiguration.append(";").append(factoryBean);
        this.cockpitUIComponentConfiguration.append(";").append(objectTemplateCode);
        if(uid != null)
        {
            this.cockpitUIComponentConfiguration.append(";").append(uid);
        }
        else
        {
            this.cockpitUIComponentConfiguration.append(";").append("");
        }
        this.cockpitUIComponentConfiguration.append(";").append(mediaUniqueCode);
        this.cockpitUIComponentConfiguration.append("\n");
    }


    private void importConfig(StringBuilder csvContent)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(csvContent.toString());
        }
        InputStream inStream = new ByteArrayInputStream(csvContent.toString().getBytes());
        ImpExManager impexManager = ImpExManager.getInstance();
        impexManager.importData(inStream, "UTF-8", ';', '"', true);
    }


    private void abortProcessingIfFolderIsEmpty(List<File> files, String prefixMessage, String postfixMessage, String... pathElements)
    {
        if(files.isEmpty())
        {
            prepareAndThrowException(prefixMessage, postfixMessage, pathElements);
        }
    }


    private void prepareAndThrowException(String prefixMessage, String postfixMessage, String... pathElements)
    {
        List<String> pathElementsList = Arrays.asList(pathElements);
        StringBuilder path = new StringBuilder();
        for(String pathElement : pathElementsList)
        {
            path.append("/").append(pathElement);
        }
        throw new CockpitImportConfigException(prefixMessage + " ../resources" + prefixMessage + " . " + path);
    }
}
