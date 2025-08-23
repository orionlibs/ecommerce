package de.hybris.platform.mediaconversion.metadata;

import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.mediaconversion.MediaMetaDataService;
import de.hybris.platform.mediaconversion.model.MediaMetaDataModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class DefaultMediaMetaDataService implements MediaMetaDataService, ApplicationContextAware, InitializingBean
{
    private static final Logger LOG = Logger.getLogger(DefaultMediaMetaDataService.class);
    private ApplicationContext applicationContext;
    private ModelService modelService;
    private MediaMetaDataServiceDao dao;
    private Collection<MediaMetaDataProvider> allProviders;


    public void afterPropertiesSet() throws Exception
    {
        Map<String, MediaMetaDataProvider> ret = BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)
                        getApplicationContext(), MediaMetaDataProvider.class);
        this.allProviders = Collections.unmodifiableSet(new HashSet<>(ret.values()));
    }


    public void extractAllMetaData(MediaModel media)
    {
        ServicesUtil.validateParameterNotNull(media, "Media must not be null.");
        try
        {
            Transaction.current().execute((TransactionBody)new Object(this, media));
        }
        catch(Exception e)
        {
            LOG.error("Failed to extract metadata. Transaction threw exception.", e);
        }
    }


    protected Collection<MediaMetaDataProvider> getAllProviders()
    {
        return this.allProviders;
    }


    public void deleteAllMetaData(MediaModel media)
    {
        ServicesUtil.validateParameterNotNull(media, "Media must not be null.");
        try
        {
            Transaction.current().execute((TransactionBody)new Object(this, media));
        }
        catch(Exception e)
        {
            LOG.error("Failed to delete metadata. Transaction threw exception.", e);
        }
    }


    public Map<String, String> getMetaData(MediaModel media, String group)
    {
        ServicesUtil.validateParameterNotNull(media, "Media must not be null.");
        ServicesUtil.validateParameterNotNull(group, "Group must not be null.");
        Collection<MediaMetaDataModel> data = getDao().findMetaData(media, group);
        if(data.isEmpty())
        {
            return Collections.emptyMap();
        }
        Map<String, Map<String, String>> mapped = toMap(data);
        return mapped.containsKey(group) ? mapped.get(group) : Collections.EMPTY_MAP;
    }


    public Map<String, Map<String, String>> getAllMetaData(MediaModel media)
    {
        ServicesUtil.validateParameterNotNull(media, "Media must not be null.");
        Collection<MediaMetaDataModel> data = getDao().findMetaData(media, null);
        return toMap(data);
    }


    private Map<String, Map<String, String>> toMap(Collection<MediaMetaDataModel> data)
    {
        Map<String, Map<String, String>> ret = new TreeMap<>();
        for(MediaMetaDataModel entry : data)
        {
            Map<String, String> group = ret.get(entry.getGroupName());
            if(group == null)
            {
                group = new TreeMap<>();
                ret.put(entry.getGroupName(), group);
            }
            group.put(entry.getCode(), entry.getValue());
        }
        return ret;
    }


    public ApplicationContext getApplicationContext()
    {
        return this.applicationContext;
    }


    public void setApplicationContext(ApplicationContext applicationContext)
    {
        this.applicationContext = applicationContext;
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


    public MediaMetaDataServiceDao getDao()
    {
        return this.dao;
    }


    @Required
    public void setDao(MediaMetaDataServiceDao dao)
    {
        this.dao = dao;
    }
}
