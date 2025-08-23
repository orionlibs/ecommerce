package de.hybris.platform.impex.jalo.exp;

import de.hybris.platform.impex.jalo.DefaultExportMediaHandler;
import de.hybris.platform.impex.jalo.DocumentIDRegistry;
import de.hybris.platform.impex.jalo.ImpExCsvFile;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.ImpExFile;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.impex.jalo.ImpExZip;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.util.CSVConstants;
import de.hybris.platform.util.CSVWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import org.apache.log4j.Logger;

public class ExportConfiguration
{
    private ImpExFile dataFile;
    private final ImpExZip mediaFile;
    private ImpExExportMedia dataExportTarget = null;
    private ImpExExportMedia mediasExportTarget = null;
    private HeaderLibrary headerLibrary = null;
    private ImpExMedia source = null;
    private DocumentIDRegistry docIDRegistry = null;
    private String fieldseparator = String.valueOf(CSVConstants.DEFAULT_FIELD_SEPARATOR);
    private String quotecharacter = String.valueOf(CSVConstants.DEFAULT_QUOTE_CHARACTER);
    private String commentcharacter = String.valueOf('#');
    private EnumerationValue headerValidationMode = null;
    private DefaultExportMediaHandler mediaHandler = null;
    private static final Logger log = Logger.getLogger(ExportConfiguration.class.getName());
    private boolean singleFile = false;


    public ExportConfiguration(ImpExMedia source, EnumerationValue headerValidationMode) throws ImpExException
    {
        if(source == null)
        {
            throw new ImpExException("Missing source media!");
        }
        this.source = source;
        this.headerValidationMode = (headerValidationMode == null) ? ImpExManager.getImportStrictMode() : headerValidationMode;
        try
        {
            this.dataFile = (ImpExFile)new ImpExZip();
            this.mediaFile = new ImpExZip();
        }
        catch(IOException e)
        {
            throw new ImpExException(e);
        }
        assignMediaFileToHandler(this.mediaFile);
    }


    public void deleteTempFiles()
    {
        deleteQuietly(this.dataFile);
        deleteQuietly((ImpExFile)this.mediaFile);
    }


    private void deleteQuietly(ImpExFile impExFile)
    {
        if(impExFile == null)
        {
            return;
        }
        try
        {
            impExFile.close();
            if(impExFile.getFile() != null)
            {
                Files.delete(impExFile.getFile().toPath());
            }
        }
        catch(IOException e)
        {
            if(log.isDebugEnabled())
            {
                log.debug("cannot close: " + e.getMessage());
            }
        }
    }


    public void setHeaderLibrary(HeaderLibrary headerLibrary)
    {
        this.headerLibrary = headerLibrary;
    }


    public HeaderLibrary getHeaderLibrary()
    {
        return this.headerLibrary;
    }


    public ImpExFile getDataFile()
    {
        return this.dataFile;
    }


    public ImpExZip getMediaFile()
    {
        return this.mediaFile;
    }


    public String getFieldSeparator()
    {
        return this.fieldseparator;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getCommentCharacetr()
    {
        return getCommentCharacter();
    }


    public String getCommentCharacter()
    {
        return this.commentcharacter;
    }


    public String getQuoteCharacter()
    {
        return this.quotecharacter;
    }


    public void setFieldSeparator(String fieldseparator)
    {
        if(fieldseparator == null)
        {
            return;
        }
        this.fieldseparator = String.valueOf(fieldseparator.charAt(0));
    }


    public void setCommentCharacter(String comment)
    {
        if(comment == null)
        {
            return;
        }
        this.commentcharacter = String.valueOf(comment.charAt(0));
    }


    public void setQuoteCharacter(String quote)
    {
        if(quote == null)
        {
            return;
        }
        this.quotecharacter = String.valueOf(quote.charAt(0));
    }


    public void setMediaDataHandler(DefaultExportMediaHandler mediaHandler)
    {
        this.mediaHandler = mediaHandler;
    }


    public void assignMediaFileToHandler(ImpExZip mediaFile)
    {
        if(this.mediaHandler == null)
        {
            this.mediaHandler = new DefaultExportMediaHandler();
        }
        this.mediaHandler.setMediaFile(mediaFile);
    }


    public DefaultExportMediaHandler getMediaDataHandler()
    {
        return this.mediaHandler;
    }


    public EnumerationValue getHeaderValidationMode()
    {
        return this.headerValidationMode;
    }


    public ImpExMedia getSource()
    {
        return this.source;
    }


    public void setSource(ImpExMedia source)
    {
        this.source = source;
    }


    public DocumentIDRegistry getDocumentIDRegistry()
    {
        if(this.docIDRegistry == null)
        {
            this.docIDRegistry = new DocumentIDRegistry(new CSVWriter(new StringWriter()));
        }
        return this.docIDRegistry;
    }


    public void setDocumentIDRegistry(DocumentIDRegistry registry)
    {
        if(registry == null)
        {
            log.warn("Setting 'DocumentIDRegistry' to <null>', will be ignored! Currently used 'DocumentIDRegistry' is: " +
                            getDocumentIDRegistry());
            return;
        }
        this.docIDRegistry = registry;
    }


    public ImpExExportMedia getDataExportTarget()
    {
        if(this.dataExportTarget == null)
        {
            this.dataExportTarget = ExportUtils.createDataExportTarget("data_export_" +
                            System.currentTimeMillis());
        }
        return this.dataExportTarget;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ImpExExportMedia getDataExportMedia()
    {
        return getDataExportTarget();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setDataExportMedia(ImpExExportMedia target) throws ConsistencyCheckException
    {
        setDataExportTarget(target);
    }


    public void setDataExportTarget(ImpExExportMedia target) throws ConsistencyCheckException
    {
        setDataExportTarget(target, true);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setDataExportMedia(ImpExExportMedia newDataExportMedia, boolean removeCurrentOne) throws ConsistencyCheckException
    {
        setDataExportTarget(newDataExportMedia, removeCurrentOne);
    }


    public void setDataExportTarget(ImpExExportMedia newDataExportMedia, boolean removeCurrentOne) throws ConsistencyCheckException
    {
        if(newDataExportMedia == null)
        {
            log.warn("Setting 'data export media' to <null>', will be ignored! Currently used 'data export media' is: " +
                            getDataExportMedia());
            return;
        }
        if(removeCurrentOne && this.dataExportTarget != null)
        {
            this.dataExportTarget.remove();
        }
        this.dataExportTarget = newDataExportMedia;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ImpExExportMedia getMediasExportMedia()
    {
        return getMediasExportTarget();
    }


    public ImpExExportMedia getMediasExportTarget()
    {
        if(this.mediasExportTarget == null)
        {
            this.mediasExportTarget = ExportUtils.createMediasExportTarget("medias_export_" + System.currentTimeMillis());
        }
        return this.mediasExportTarget;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setMediasExportMedia(ImpExExportMedia target) throws ConsistencyCheckException
    {
        setMediasExportTarget(target);
    }


    public void setMediasExportTarget(ImpExExportMedia target) throws ConsistencyCheckException
    {
        setMediasExportTarget(target, true);
    }


    public void setMediasExportTarget(ImpExExportMedia newMediasExportMedia, boolean removeCurrentOne) throws ConsistencyCheckException
    {
        if(newMediasExportMedia == null)
        {
            log.warn("Setting 'medias export media' to <null>', will be ignored! Currently used 'medias export media' is: " +
                            getMediasExportTarget());
            return;
        }
        if(removeCurrentOne && this.mediasExportTarget != null)
        {
            this.mediasExportTarget.remove();
        }
        this.mediasExportTarget = newMediasExportMedia;
    }


    public void setSingleFile(boolean singleFile)
    {
        this.singleFile = singleFile;
        try
        {
            if(singleFile)
            {
                deleteQuietly(this.dataFile);
                this.dataFile = (ImpExFile)new ImpExCsvFile();
            }
        }
        catch(IOException ioe)
        {
            log.error("Could not use single file export", ioe);
        }
    }


    public boolean isSingleFile()
    {
        return this.singleFile;
    }
}
