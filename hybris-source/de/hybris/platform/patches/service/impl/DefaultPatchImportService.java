package de.hybris.platform.patches.service.impl;

import de.hybris.platform.patches.data.AbstractImpexFile;
import de.hybris.platform.patches.data.ImpexHeaderFile;
import de.hybris.platform.patches.data.ImpexHeaderOption;
import de.hybris.platform.patches.data.ImpexImportUnit;
import de.hybris.platform.patches.data.ImpexImportUnitOption;
import de.hybris.platform.patches.data.ImpexImportUnitResult;
import de.hybris.platform.patches.exceptions.PatchImportException;
import de.hybris.platform.patches.service.PatchImportService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.StreamBasedImpExResource;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultPatchImportService implements PatchImportService
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPatchImportService.class);
    private static final String NEW_LINE = "\n";
    private static final String FILE_ENCODING = "UTF-8";
    private static final String FALSE_VALUE = "false";
    private ConfigurationService configurationService;
    private ImportService importService;


    public ImpexImportUnitResult importImpexUnit(ImpexImportUnit impexImportUnit)
    {
        return importImpexUnitWithoutTracking(impexImportUnit);
    }


    public ImpexImportUnitResult importImpexUnitWithoutTracking(ImpexImportUnit impexImportUnit)
    {
        ImportResult result;
        log(impexImportUnit);
        try
        {
            StreamBasedImpExResource streamBasedImpExResource = new StreamBasedImpExResource(getStreamForImpexImportUnit(impexImportUnit), "UTF-8");
            ImportConfig importConfiguration = getImportConfiguration((ImpExResource)streamBasedImpExResource, impexImportUnit
                            .getImpexImportUnitOptions());
            result = this.importService.importData(importConfiguration);
        }
        catch(FileNotFoundException | de.hybris.platform.servicelayer.exceptions.SystemException | IllegalArgumentException | IllegalStateException e)
        {
            throw new PatchImportException(e.getMessage(), e);
        }
        ImpexImportUnitResult unitResult = new ImpexImportUnitResult();
        unitResult.setImpexImportUnit(impexImportUnit);
        unitResult.setImportResult(result);
        return unitResult;
    }


    private void log(ImpexImportUnit impexImportUnit)
    {
        if(LOG.isDebugEnabled())
        {
            StringBuilder builder = new StringBuilder("Importing unit with:\nFile: ");
            builder.append(impexImportUnit.getImpexDataFile().getFilePath());
            ImpexHeaderFile[] headers = impexImportUnit.getImpexHeaderFiles();
            if(headers != null)
            {
                for(ImpexHeaderFile header : headers)
                {
                    builder.append("\nHeader: '");
                    builder.append(header.getFilePath());
                    builder.append("'");
                }
            }
            ImpexHeaderOption[] options = impexImportUnit.getImpexHeaderOptions();
            if(options != null)
            {
                for(ImpexHeaderOption option : options)
                {
                    builder.append("\nOption: '");
                    builder.append(option.getMacro());
                    builder.append("'");
                }
            }
            ImpexImportUnitOption[] unitOptions = impexImportUnit.getImpexImportUnitOptions();
            if(unitOptions != null)
            {
                for(ImpexImportUnitOption unitOption : unitOptions)
                {
                    builder.append("\nUnit Option: '");
                    builder.append(unitOption);
                    builder.append("'");
                }
            }
            LOG.debug(builder.toString());
        }
    }


    public SequenceInputStream getStreamForImpexImportUnit(ImpexImportUnit impexImportUnit) throws FileNotFoundException
    {
        List<InputStream> combinedStream = new ArrayList<>();
        ImpexHeaderOption[] impexHeaderOptions = impexImportUnit.getImpexHeaderOptions();
        if(impexHeaderOptions != null && impexHeaderOptions.length > 0)
        {
            combinedStream.add(getStreamForHeaderOptions(impexHeaderOptions));
            combinedStream.add(getNewLineAsStream());
        }
        ImpexHeaderFile[] impexHeaderFiles = impexImportUnit.getImpexHeaderFiles();
        if(impexHeaderFiles != null && impexHeaderFiles.length > 0)
        {
            combinedStream.add(getStreamForHeaders(impexHeaderFiles));
        }
        combinedStream.add(getStreamForFile((AbstractImpexFile)impexImportUnit.getImpexDataFile()));
        return new SequenceInputStream(Collections.enumeration(combinedStream));
    }


    InputStream getStreamForHeaderOptions(ImpexHeaderOption[] impexHeaderOptions)
    {
        List<InputStream> headerOptions = new ArrayList<>();
        for(ImpexHeaderOption headerOption : impexHeaderOptions)
        {
            headerOptions.add(convertString(headerOption.getMacro()));
            headerOptions.add(getNewLineAsStream());
        }
        return new SequenceInputStream(Collections.enumeration(headerOptions));
    }


    InputStream getStreamForHeaders(ImpexHeaderFile[] impexHeaderFiles) throws FileNotFoundException
    {
        List<InputStream> streams = new ArrayList<>();
        for(ImpexHeaderFile impexHeader : impexHeaderFiles)
        {
            InputStream resourceAsStreamForHeader = getStreamForFile((AbstractImpexFile)impexHeader);
            if(resourceAsStreamForHeader != null)
            {
                streams.add(resourceAsStreamForHeader);
                streams.add(getNewLineAsStream());
            }
        }
        return new SequenceInputStream(Collections.enumeration(streams));
    }


    InputStream getStreamForFile(AbstractImpexFile impexFile) throws FileNotFoundException
    {
        InputStream is = getResourceStream(impexFile);
        if(is == null)
        {
            if(impexFile instanceof ImpexHeaderFile)
            {
                ImpexHeaderFile headerFile = (ImpexHeaderFile)impexFile;
                if(!headerFile.isOptional())
                {
                    throw new FileNotFoundException(impexFile.getFilePath() + " - mandatory file is not existing.");
                }
            }
            else
            {
                throw new FileNotFoundException(impexFile.getFilePath() + " - mandatory file is not existing.");
            }
            return null;
        }
        return is;
    }


    ImportConfig getImportConfiguration(ImpExResource script, ImpexImportUnitOption[] impexImportUnitOptions)
    {
        ImportConfig config = new ImportConfig();
        config.setScript(script);
        if(impexImportUnitOptions != null)
        {
            for(ImpexImportUnitOption unitOption : impexImportUnitOptions)
            {
                if(unitOption == ImpexImportUnitOption.LEGACY_MODE)
                {
                    config.setLegacyMode(Boolean.TRUE);
                }
                else
                {
                    config.setMaxThreads(1);
                }
            }
        }
        if(StringUtils.equalsIgnoreCase("false", this.configurationService
                        .getConfiguration()
                        .getString("impex.importConfig.cronJob.removeOnSuccess.enabled")))
        {
            config.setRemoveOnSuccess(false);
        }
        config.setFailOnError(StringUtils.equalsIgnoreCase("true", this.configurationService
                        .getConfiguration()
                        .getString("impex.importConfig.failOnError.enabled")));
        return config;
    }


    private InputStream getResourceStream(AbstractImpexFile impexFile)
    {
        String filePath = impexFile.getFilePath();
        if(filePath != null)
        {
            return getClass().getResourceAsStream(impexFile.getFilePath());
        }
        LOG.info("There is no file path for file: {}", impexFile.getName());
        return null;
    }


    private InputStream getNewLineAsStream()
    {
        return convertString("\n");
    }


    private InputStream convertString(String value)
    {
        try
        {
            return new ByteArrayInputStream(value.getBytes("UTF-8"));
        }
        catch(UnsupportedEncodingException e)
        {
            throw new IllegalStateException("UTF-8 encoding is not supported", e);
        }
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    @Required
    public void setImportService(ImportService importService)
    {
        this.importService = importService;
    }
}
