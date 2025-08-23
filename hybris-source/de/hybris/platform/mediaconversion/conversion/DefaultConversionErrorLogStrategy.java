package de.hybris.platform.mediaconversion.conversion;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.model.ConversionErrorLogModel;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.mediaconversion.util.HybrisRunnable;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultConversionErrorLogStrategy implements ConversionErrorLogStrategy
{
    private static final Logger LOG = Logger.getLogger(DefaultConversionErrorLogStrategy.class);
    public static final String REMOVECONVERSIONERRORLOGUPONSUCCESS = "mediaconversion.removeConversionErrorLogUponSuccess";
    private ConversionErrorLogStrategyDao dao;
    private ModelService modelService;
    private ConfigurationService configurationService;
    private ExecutorService executor;


    public void init()
    {
        if(this.executor != null)
        {
            throw new IllegalStateException("Executor already present.");
        }
        LOG.debug("Starting executor.");
        this.executor = Executors.newCachedThreadPool((ThreadFactory)new Object(this));
    }


    public void destroy()
    {
        if(this.executor == null)
        {
            LOG.warn("No executor available to shutdown.");
        }
        else
        {
            this.executor.shutdown();
            this.executor = null;
        }
    }


    public void logConversionError(MediaContainerModel container, ConversionMediaFormatModel targetFormat, MediaModel sourceMedia, Exception fault)
    {
        logConversionError(container, targetFormat, sourceMedia, extractErrorMessage(fault));
    }


    protected String extractErrorMessage(Exception fault)
    {
        if(fault.getMessage() == null)
        {
            return toString(fault);
        }
        StringBuffer result = new StringBuffer(fault.getMessage());
        if(fault.getCause() != null)
        {
            result.append(" ").append(fault.getCause().getMessage());
        }
        return result.toString();
    }


    protected void logConversionError(MediaContainerModel container, ConversionMediaFormatModel targetFormat, MediaModel sourceMedia, String message)
    {
        if(this.executor == null)
        {
            error(container, targetFormat, sourceMedia, message, new IllegalStateException("No executor available."));
        }
        else
        {
            this.executor.execute((Runnable)new HybrisRunnable((Runnable)new Object(this, container, targetFormat, sourceMedia, message)));
        }
    }


    protected void doStoreConversionError(MediaContainerModel container, ConversionMediaFormatModel targetFormat, MediaModel sourceMedia, String message)
    {
        try
        {
            ConversionErrorLogModel model = (ConversionErrorLogModel)getModelService().create(ConversionErrorLogModel.class);
            model.setContainer(container);
            model.setTargetFormat(targetFormat);
            model.setSourceMedia(sourceMedia);
            model.setErrorMessage(message);
            getModelService().save(model);
        }
        catch(Exception e)
        {
            error(container, targetFormat, sourceMedia, message, e);
        }
    }


    private void error(MediaContainerModel container, ConversionMediaFormatModel targetFormat, MediaModel sourceMedia, String message, Exception exception)
    {
        LOG.error("Failed to store conversion error log. [container: " + container + "; targetFormat: " + targetFormat + "; sourceMedia: " + sourceMedia + "; message: " + message + "]", exception);
    }


    public void reportConversionSuccess(MediaContainerModel container, ConversionMediaFormatModel targetFormat)
    {
        if(getConfigurationService().getConfiguration()
                        .getBoolean("mediaconversion.removeConversionErrorLogUponSuccess", true))
        {
            for(ConversionErrorLogModel errorLog : getDao().findAllErrorLogs(container, targetFormat))
            {
                try
                {
                    getModelService().remove(errorLog);
                }
                catch(ModelRemovalException e)
                {
                    LOG.error("Failed to remove conversion error log entry '" + errorLog + "'.", (Throwable)e);
                }
            }
        }
    }


    protected String toString(Exception fault)
    {
        if(fault == null)
        {
            return null;
        }
        StringWriter out = new StringWriter();
        fault.printStackTrace(new PrintWriter(out));
        return out.toString();
    }


    public ConversionErrorLogStrategyDao getDao()
    {
        return this.dao;
    }


    @Required
    public void setDao(ConversionErrorLogStrategyDao dao)
    {
        this.dao = dao;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
