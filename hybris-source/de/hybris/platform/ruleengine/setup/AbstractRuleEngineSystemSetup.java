package de.hybris.platform.ruleengine.setup;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractRuleEngineSystemSetup
{
    private static final Logger LOG = Logger.getLogger(AbstractRuleEngineSystemSetup.class);
    private String impexExt = ".impex";
    private String fileEncoding = "UTF-8";
    private ImportService importService;
    private CommonI18NService commonI18NService;


    public void importImpexFile(String file, boolean errorIfMissing, boolean legacyMode)
    {
        try
        {
            InputStream resourceAsStream = getClass().getResourceAsStream(file);
            try
            {
                if(resourceAsStream == null)
                {
                    if(errorIfMissing)
                    {
                        LOG.error("Importing [" + file + "]... ERROR (MISSING FILE)", null);
                    }
                    else
                    {
                        LOG.info("Importing [" + file + "]... SKIPPED (Optional File Not Found)");
                    }
                }
                else
                {
                    importImpexFile(file, resourceAsStream, legacyMode);
                    importLanguageSpecificImpexFiles(file, legacyMode);
                }
                if(resourceAsStream != null)
                {
                    resourceAsStream.close();
                }
            }
            catch(Throwable throwable)
            {
                if(resourceAsStream != null)
                {
                    try
                    {
                        resourceAsStream.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            LOG.error("FAILED", e);
        }
    }


    protected void importLanguageSpecificImpexFiles(String file, boolean legacyMode) throws IOException
    {
        if(!file.endsWith(getImpexExt()))
        {
            return;
        }
        String filePath = file.substring(0, file.length() - getImpexExt().length());
        List<LanguageModel> languages = getCommonI18NService().getAllLanguages();
        for(LanguageModel language : languages)
        {
            String languageFilePath = filePath + "_" + filePath + language.getIsocode();
            InputStream languageResourceAsStream = getClass().getResourceAsStream(languageFilePath);
            try
            {
                if(languageResourceAsStream != null)
                {
                    importImpexFile(languageFilePath, languageResourceAsStream, legacyMode);
                }
                if(languageResourceAsStream != null)
                {
                    languageResourceAsStream.close();
                }
            }
            catch(Throwable throwable)
            {
                if(languageResourceAsStream != null)
                {
                    try
                    {
                        languageResourceAsStream.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
    }


    protected void importImpexFile(String file, InputStream stream, boolean legacyMode)
    {
        String message = "Importing [" + file + "]...";
        try
        {
            LOG.info(message);
            ImportConfig importConfig = new ImportConfig();
            importConfig.setScript((ImpExResource)new StreamBasedImpExResource(stream, getFileEncoding()));
            importConfig.setLegacyMode(Boolean.valueOf(legacyMode));
            ImportResult importResult = getImportService().importData(importConfig);
            if(importResult.isError())
            {
                LOG.error(message + " FAILED");
            }
        }
        catch(Exception e)
        {
            LOG.error(message + " FAILED", e);
        }
    }


    protected String getImpexExt()
    {
        return this.impexExt;
    }


    public void setImpexExt(String impexExt)
    {
        this.impexExt = impexExt;
    }


    protected String getFileEncoding()
    {
        return this.fileEncoding;
    }


    public void setFileEncoding(String fileEncoding)
    {
        this.fileEncoding = fileEncoding;
    }


    protected ImportService getImportService()
    {
        return this.importService;
    }


    @Required
    public void setImportService(ImportService importService)
    {
        this.importService = importService;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }
}
