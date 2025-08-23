package de.hybris.platform.adaptivesearch.model.interceptors;

import de.hybris.platform.adaptivesearch.model.AbstractAsSearchConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.strategies.AsItemModelHelper;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProvider;
import de.hybris.platform.adaptivesearch.strategies.AsSearchProviderFactory;
import de.hybris.platform.adaptivesearch.strategies.AsUidGenerator;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelInternalContext;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Date;
import org.springframework.beans.factory.annotation.Required;

public class AbstractAsInterceptor
{
    private ModelService modelService;
    private AsUidGenerator asUidGenerator;
    private AsSearchProviderFactory asSearchProviderFactory;
    private AsItemModelHelper asItemModelHelper;


    protected String generateUid()
    {
        return this.asUidGenerator.generateUid();
    }


    protected String generateItemIdentifier(ItemModel item, InterceptorContext context)
    {
        if(item == null)
        {
            return "null";
        }
        PK itemPk = context.isNew(item) ? getNewPkForNotSavedItem((AbstractItemModel)item) : item.getPk();
        if(itemPk == null)
        {
            throw new IllegalStateException("Could not generate identifier for item with unknown pk");
        }
        return itemPk.getLongValueAsString();
    }


    protected String decorateIdentifier(String identifier)
    {
        if(identifier == null)
        {
            return "null";
        }
        return identifier;
    }


    protected PK getNewPkForNotSavedItem(AbstractItemModel item)
    {
        ItemModelInternalContext ictx = (ItemModelInternalContext)ModelContextUtils.getItemModelContext(item);
        PK newPK = ictx.getNewPK();
        return (newPK == null) ? ictx.generateNewPK() : newPK;
    }


    protected AsSearchProvider resolveSearchProvider()
    {
        return this.asSearchProviderFactory.getSearchProvider();
    }


    protected AbstractAsSearchProfileModel resolveSearchProfile(ItemModel model)
    {
        return (AbstractAsSearchProfileModel)this.modelService.getAttributeValue(model, "searchProfile");
    }


    protected AbstractAsSearchProfileModel resolveAndValidateSearchProfile(AbstractAsSearchConfigurationModel model) throws InterceptorException
    {
        Object searchProfile = this.modelService.getAttributeValue(model, "searchProfile");
        if(!(searchProfile instanceof AbstractAsSearchProfileModel))
        {
            throw new InterceptorException("Invalid search profile");
        }
        return (AbstractAsSearchProfileModel)searchProfile;
    }


    protected AbstractAsSearchConfigurationModel resolveSearchConfiguration(ItemModel model)
    {
        return (AbstractAsSearchConfigurationModel)this.modelService.getAttributeValue(model, "searchConfiguration");
    }


    protected AbstractAsSearchConfigurationModel resolveAndValidateSearchConfiguration(ItemModel model) throws InterceptorException
    {
        Object searchConfiguration = this.modelService.getAttributeValue(model, "searchConfiguration");
        if(!(searchConfiguration instanceof AbstractAsSearchConfigurationModel))
        {
            throw new InterceptorException("Invalid search configuration");
        }
        return (AbstractAsSearchConfigurationModel)searchConfiguration;
    }


    protected void markItemAsModified(InterceptorContext context, ItemModel item, String... path)
    {
        ItemModel currentItem = item;
        int index = 0;
        while(currentItem != null)
        {
            if(!context.isRemoved(currentItem) && !context.contains(currentItem, PersistenceOperation.SAVE))
            {
                if(!context.isModified(currentItem))
                {
                    currentItem.setModifiedtime(new Date());
                }
                context.registerElementFor(currentItem, PersistenceOperation.SAVE);
            }
            if(index < path.length)
            {
                currentItem = (ItemModel)this.modelService.getAttributeValue(currentItem, path[index]);
                index++;
                continue;
            }
            currentItem = null;
        }
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


    public AsUidGenerator getAsUidGenerator()
    {
        return this.asUidGenerator;
    }


    @Required
    public void setAsUidGenerator(AsUidGenerator asUidGenerator)
    {
        this.asUidGenerator = asUidGenerator;
    }


    public AsSearchProviderFactory getAsSearchProviderFactory()
    {
        return this.asSearchProviderFactory;
    }


    @Required
    public void setAsSearchProviderFactory(AsSearchProviderFactory asSearchProviderFactory)
    {
        this.asSearchProviderFactory = asSearchProviderFactory;
    }


    public AsItemModelHelper getAsItemModelHelper()
    {
        return this.asItemModelHelper;
    }


    @Required
    public void setAsItemModelHelper(AsItemModelHelper asItemModelHelper)
    {
        this.asItemModelHelper = asItemModelHelper;
    }
}
