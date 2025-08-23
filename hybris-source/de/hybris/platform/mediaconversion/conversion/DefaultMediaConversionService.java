package de.hybris.platform.mediaconversion.conversion;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaFormatModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.MediaConversionService;
import de.hybris.platform.mediaconversion.enums.ConversionStatus;
import de.hybris.platform.mediaconversion.model.ConversionMediaFormatModel;
import de.hybris.platform.mediaconversion.util.LockRowInTransactionStrategy;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultMediaConversionService implements MediaConversionService, ApplicationContextAware, InitializingBean
{
    private static final Logger LOG = Logger.getLogger(DefaultMediaConversionService.class);
    private MediaConversionServiceDao dao;
    private ModelService modelService;
    private MediaService mediaService;
    private ApplicationContext applicationContext;
    private LockRowInTransactionStrategy lockRowInTransactionStrategy;
    private ConversionErrorLogStrategy conversionErrorLogStrategy;
    private Map<String, MediaConversionStrategy> allConversionStrategies;


    public void afterPropertiesSet()
    {
        this.allConversionStrategies = Collections.unmodifiableMap(BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)
                        getApplicationContext(), MediaConversionStrategy.class));
    }


    public int deleteConvertedMedias(MediaContainerModel container)
    {
        ServicesUtil.validateParameterNotNull(container, "Container must not be null.");
        try
        {
            return ((Integer)Transaction.current().execute((TransactionBody)new Object(this, container)))
                            .intValue();
        }
        catch(Exception e)
        {
            throw new IllegalStateException("Transaction body threw exception.", e);
        }
    }


    public Collection<MediaModel> getConvertedMedias(MediaContainerModel container)
    {
        ServicesUtil.validateParameterNotNull(container, "Container must not be null.");
        if(getModelService().isNew(container))
        {
            return selectDerivedMedias(container);
        }
        return getDao().getConvertedMedias(container);
    }


    protected Collection<MediaModel> selectDerivedMedias(MediaContainerModel container)
    {
        if(container.getMedias() == null || container.getMedias().isEmpty())
        {
            return Collections.emptyList();
        }
        Collection<MediaModel> ret = new LinkedList<>();
        for(MediaModel media : container.getMedias())
        {
            if(media.getOriginalDataPK() == null)
            {
                ret.add(media);
            }
        }
        return ret;
    }


    public void convertMedias(MediaContainerModel container)
    {
        ServicesUtil.validateParameterNotNull(container, "Container must not be null.");
        for(ConversionMediaFormatModel format : (container.getConversionGroup() == null) ? getAllConversionFormats() :
                        container.getConversionGroup().getSupportedFormats())
        {
            try
            {
                getOrConvert(container, (MediaFormatModel)format);
            }
            catch(ModelNotFoundException e)
            {
                LOG.error("Failed to convert media to format '" + format + "'.", (Throwable)e);
            }
        }
    }


    public Collection<ConversionMediaFormatModel> getAllConversionFormats()
    {
        return getDao().allConversionFormats();
    }


    public ConversionStatus getConversionStatus(MediaContainerModel model)
    {
        ServicesUtil.validateParameterNotNull(model, "Container must not be null.");
        if(model.getMaster() == null)
        {
            return ConversionStatus.EMPTY;
        }
        Set<ConversionMediaFormatModel> todo = new HashSet<>((model.getConversionGroup() == null) ? getAllConversionFormats() : model.getConversionGroup().getSupportedFormats());
        if(todo.isEmpty())
        {
            return ConversionStatus.CONVERTED;
        }
        int totalSize = todo.size();
        for(MediaModel media : getConvertedMedias(model))
        {
            if(media.getMediaFormat() == null)
            {
                LOG.warn("MediaFormat not set on media '" + media + "' in media container '" + model + "'.");
                continue;
            }
            todo.remove(media.getMediaFormat());
        }
        return todo.isEmpty() ? ConversionStatus.CONVERTED : ((totalSize == todo.size()) ? ConversionStatus.UNCONVERTED :
                        ConversionStatus.PARTIALLY_CONVERTED);
    }


    public MediaModel getMaster(MediaContainerModel model)
    {
        ServicesUtil.validateParameterNotNull(model, "Container must not be null.");
        if(getModelService().isNew(model))
        {
            return selectMasterFromUnsavedContainer(model);
        }
        try
        {
            return getDao().retrieveMaster(model);
        }
        catch(AmbiguousIdentifierException e)
        {
            LOG.warn("Multiple medias which claim to be master detected in '" + model + "'.");
            return null;
        }
        catch(ModelNotFoundException e)
        {
            LOG.debug("No master media found for container '" + model + "'.");
            return null;
        }
    }


    protected MediaModel selectMasterFromUnsavedContainer(MediaContainerModel model)
    {
        MediaModel master = null;
        if(model.getMedias() != null)
        {
            for(MediaModel media : model.getMedias())
            {
                if(media.getOriginal() == null && media.getOriginalDataPK() == null)
                {
                    if(master == null)
                    {
                        master = media;
                        continue;
                    }
                    LOG.debug("Multiple medias which claim to be master detected in unsaved container '" + model + "'.");
                    return null;
                }
            }
        }
        if(master == null)
        {
            LOG.debug("No master media found in unsaved container '" + model + "'.");
        }
        return master;
    }


    public MediaModel getOrConvert(MediaContainerModel container, MediaFormatModel format)
    {
        ServicesUtil.validateParameterNotNull(container, "Container must not be null.");
        ServicesUtil.validateParameterNotNull(format, "Format must not be null.");
        ModelNotFoundException notFound = null;
        try
        {
            MediaModel ret = getUpToDateMediaByFormat(container, format);
            if(ret != null)
            {
                return ret;
            }
        }
        catch(ModelNotFoundException e)
        {
            notFound = e;
        }
        if(format instanceof ConversionMediaFormatModel)
        {
            try
            {
                return convertInTransaction(container, (ConversionMediaFormatModel)format);
            }
            catch(MediaConversionException conversionFault)
            {
                throw new ModelNotFoundException("Failed to convert container '" + container.getQualifier() + " to format " + format
                                .getQualifier() + ".", conversionFault);
            }
        }
        throw (notFound == null) ? new ModelNotFoundException("Cannot convert to format '" + format + "'. Not a ConversionMediaFormat.") :
                        notFound;
    }


    protected MediaModel getUpToDateMediaByFormat(MediaContainerModel container, MediaFormatModel format)
    {
        MediaModel ret = getMediaService().getMediaByFormat(container, format);
        if(isUpToDate(ret))
        {
            return ret;
        }
        throw new ModelNotFoundException("MediaModel '" + ret + "' is not up to date.");
    }


    protected boolean isUpToDate(MediaModel ret)
    {
        return (
                        !(ret.getMediaFormat() instanceof ConversionMediaFormatModel) || ret
                                        .getOriginalDataPK() == null || (ret
                                        .getOriginal() != null && ret.getOriginal().getDataPK() != null && ret.getOriginal().getDataPK()
                                        .equals(ret.getOriginalDataPK())));
    }


    private MediaModel convertInTransaction(MediaContainerModel container, ConversionMediaFormatModel format) throws MediaConversionException
    {
        try
        {
            return (MediaModel)Transaction.current().execute((TransactionBody)new Object(this, container, format));
        }
        catch(MediaConversionException e)
        {
            throw e;
        }
        catch(RuntimeException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new MediaConversionException("Transaction threw unknown exception.", e);
        }
    }


    protected MediaModel doConvert(MediaContainerModel container, ConversionMediaFormatModel format) throws MediaConversionException
    {
        try
        {
            MediaModel ret = getUpToDateMediaByFormat(container, (MediaFormatModel)format);
            if(ret != null)
            {
                return ret;
            }
        }
        catch(ModelNotFoundException modelNotFoundException)
        {
        }
        LOG.debug("Converting '" + container.getQualifier() + "' to format '" + format.getQualifier() + "'.");
        MediaModel input = (format.getInputFormat() == null) ? container.getMaster() : getOrConvert(container, (MediaFormatModel)format
                        .getInputFormat());
        try
        {
            if(input == null)
            {
                throw new MediaConversionException("Failed to retrieve input media for conversion.");
            }
            long startMillis = System.currentTimeMillis();
            try
            {
                MediaModel ret = convert(input, format);
                getConversionErrorLogStrategy().reportConversionSuccess(container, format);
                return ret;
            }
            finally
            {
                LOG.debug("Conversion took " + System.currentTimeMillis() - startMillis + " ms.");
            }
        }
        catch(MediaConversionException e)
        {
            getConversionErrorLogStrategy().logConversionError(container, format, input, (Exception)e);
            throw e;
        }
    }


    private MediaModel convert(MediaModel master, ConversionMediaFormatModel format) throws MediaConversionException
    {
        try
        {
            return getMediaConversionStrategy(format).convert(this, master, format);
        }
        catch(NoSuchBeanDefinitionException e)
        {
            throw new MediaConversionException("Invalid conversion strategy '" + format.getConversionStrategy() + "' defined.", e);
        }
    }


    protected MediaConversionStrategy getMediaConversionStrategy(ConversionMediaFormatModel format) throws NoSuchBeanDefinitionException
    {
        MediaConversionStrategy ret = this.allConversionStrategies.get(format.getConversionStrategy());
        if(ret == null)
        {
            throw new NoSuchBeanDefinitionException(format.getConversionStrategy());
        }
        return ret;
    }


    public MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
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


    public MediaConversionServiceDao getDao()
    {
        return this.dao;
    }


    @Required
    public void setDao(MediaConversionServiceDao dao)
    {
        this.dao = dao;
    }


    public ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
    }


    public LockRowInTransactionStrategy getLockRowInTransactionStrategy()
    {
        return this.lockRowInTransactionStrategy;
    }


    @Required
    public void setLockRowInTransactionStrategy(LockRowInTransactionStrategy strategy)
    {
        this.lockRowInTransactionStrategy = strategy;
    }


    public ConversionErrorLogStrategy getConversionErrorLogStrategy()
    {
        return this.conversionErrorLogStrategy;
    }


    @Required
    public void setConversionErrorLogStrategy(ConversionErrorLogStrategy whoToDoIt)
    {
        this.conversionErrorLogStrategy = whoToDoIt;
    }
}
