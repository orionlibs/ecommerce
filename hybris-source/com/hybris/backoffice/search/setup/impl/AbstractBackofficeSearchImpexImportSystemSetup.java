package com.hybris.backoffice.search.setup.impl;

import com.hybris.backoffice.search.setup.BackofficeSearchImpexImportSystemSetup;
import com.hybris.backoffice.search.setup.BackofficeSearchSystemSetupConfig;
import de.hybris.platform.core.initialization.SystemSetup;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;

public abstract class AbstractBackofficeSearchImpexImportSystemSetup implements BackofficeSearchImpexImportSystemSetup
{
    private static final String RESOURCE_NOT_FOUND = "Resource {} not found";
    private static final String ERROR_IMPORTING_FILE = "Error importing {} file";
    private static final String FILE_IMPORTED = "Impex file {} imported";
    private static final String DOT = ".";
    private static final String BAD_URI = "Bad URI";
    private static final String IMPEX_EXTENSION = "impex";
    private final ImportService importService;
    private final CommonI18NService commonI18NService;
    private final ModelService modelService;
    private final CronJobService cronJobService;
    private final ConfigurationService configurationService;
    private final BackofficeSearchSystemSetupConfig config;
    private final FileBasedImpExResourceFactory fileBasedImpExResourceFactory;


    public AbstractBackofficeSearchImpexImportSystemSetup(ImportService importService, CommonI18NService commonI18NService, ModelService modelService, CronJobService cronJobService, ConfigurationService configurationService, BackofficeSearchSystemSetupConfig backofficeSearchSystemSetupConfig,
                    FileBasedImpExResourceFactory fileBasedImpExResourceFactory)
    {
        this.importService = importService;
        this.commonI18NService = commonI18NService;
        this.modelService = modelService;
        this.cronJobService = cronJobService;
        this.configurationService = configurationService;
        this.config = backofficeSearchSystemSetupConfig;
        this.fileBasedImpExResourceFactory = fileBasedImpExResourceFactory;
    }


    @SystemSetup(type = SystemSetup.Type.PROJECT, process = SystemSetup.Process.ALL)
    public void importImpex()
    {
        Collection<String> nonLocalizedRoots = getConfig().getNonLocalizedRootNames();
        nonLocalizedRoots.forEach(this::importImpexFileWithLogging);
        Collection<String> roots = getConfig().getLocalizedRootNames();
        roots.forEach(this::importLocalizedImpexFiles);
        adjustIndexUpdatingCronjob();
    }


    protected void importImpexFileWithLogging(String filePath)
    {
        tryToImportImpexFile(filePath).ifPresent(ir -> {
            if(ir.isError())
            {
                getLOG().error("Error importing {} file", filePath);
                return;
            }
            getLOG().info("Impex file {} imported", filePath);
        });
    }


    protected Optional<ImportResult> tryToImportImpexFile(String filePath)
    {
        File importedFile;
        ImportConfig importConfig = new ImportConfig();
        URL resource = getClass().getResource(filePath);
        if(resource == null)
        {
            getLOG().debug("Resource {} not found", filePath);
            return Optional.empty();
        }
        try
        {
            importedFile = new File(resource.toURI());
        }
        catch(URISyntaxException e)
        {
            getLOG().error("Bad URI", e);
            return Optional.empty();
        }
        importConfig.setScript((ImpExResource)
                        getFileBasedImpExResourceFactory().createFileBasedImpExResource(importedFile, getConfig().getFileEncoding()));
        return Optional.ofNullable(getImportService().importData(importConfig));
    }


    protected void importLocalizedImpexFiles(String rootPath)
    {
        List<LanguageModel> allLanguages = getCommonI18NService().getAllLanguages();
        for(LanguageModel language : allLanguages)
        {
            String filePath = resolveLocalizedFilePath(rootPath, language);
            importImpexFileWithLogging(filePath);
        }
    }


    protected String resolveLocalizedFilePath(String root, LanguageModel languageModel)
    {
        StringBuilder sb = new StringBuilder(root);
        sb.append(getConfig().getRootNameLanguageSeparator()).append(languageModel.getIsocode()).append(".")
                        .append("impex");
        return sb.toString();
    }


    protected ImportService getImportService()
    {
        return this.importService;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected CronJobService getCronJobService()
    {
        return this.cronJobService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    protected BackofficeSearchSystemSetupConfig getConfig()
    {
        return this.config;
    }


    protected FileBasedImpExResourceFactory getFileBasedImpExResourceFactory()
    {
        return this.fileBasedImpExResourceFactory;
    }


    protected abstract void adjustIndexUpdatingCronjob();


    protected abstract Logger getLOG();
}
