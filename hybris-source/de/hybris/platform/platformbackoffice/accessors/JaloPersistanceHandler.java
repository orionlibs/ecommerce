package de.hybris.platform.platformbackoffice.accessors;

import com.hybris.backoffice.cockpitng.dataaccess.facades.object.savedvalues.ItemModificationHistoryService;
import com.hybris.backoffice.cockpitng.dataaccess.facades.object.savedvalues.ItemModificationInfo;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class JaloPersistanceHandler
{
    private ModelService modelService;
    private ItemModificationHistoryService itemModificationHistoryService;
    private TypeFacade typeFacade;


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setTypeFacade(TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    @Required
    public void setItemModificationHistoryService(ItemModificationHistoryService itemModificationHistoryService)
    {
        this.itemModificationHistoryService = itemModificationHistoryService;
    }


    public void save(ItemModel itemModel, Map<String, Object> jaloAttributes)
    {
        Map<String, Object> copyJaloAttributes = new HashMap<>(jaloAttributes);
        Item source = (Item)this.modelService.getSource(itemModel);
        convertAttributes(copyJaloAttributes);
        filterWritableAttributes(itemModel, copyJaloAttributes);
        try
        {
            ItemModificationInfo modificationInfo = collectChanges(itemModel, copyJaloAttributes);
            source.setAllAttributes(copyJaloAttributes);
            this.itemModificationHistoryService.logItemModification(modificationInfo);
        }
        catch(Exception ex)
        {
            throw new JaloSystemException(ex);
        }
    }


    private ItemModificationInfo collectChanges(ItemModel itemModel, Map<String, Object> jaloAttributes)
    {
        ItemModificationInfo modificationInfo = new ItemModificationInfo(itemModel);
        Item source = (Item)this.modelService.getSource(itemModel);
        for(Map.Entry<String, Object> entry : jaloAttributes.entrySet())
        {
            modificationInfo.addEntry(entry.getKey(), false, getOriginalValue(source, entry.getKey()), entry.getValue());
        }
        return modificationInfo;
    }


    private Object getOriginalValue(Item source, String attributeName)
    {
        try
        {
            return source.getAttribute(attributeName);
        }
        catch(Exception ex)
        {
            throw new JaloSystemException(ex);
        }
    }


    private void convertAttributes(Map<String, Object> jaloAttributes)
    {
        Iterator<Map.Entry<String, Object>> iterator = jaloAttributes.entrySet().iterator();
        while(iterator.hasNext())
        {
            Map.Entry<String, Object> entry = iterator.next();
            if(entry.getValue() instanceof ItemModel)
            {
                entry.setValue(this.modelService.getSource(entry.getValue()));
                continue;
            }
            if(entry.getValue() instanceof Collection)
            {
                Collection<Object> jaloCollection;
                Collection inputCollection = (Collection)entry.getValue();
                if(entry.getValue() instanceof java.util.Set)
                {
                    jaloCollection = new HashSet();
                }
                else
                {
                    jaloCollection = new ArrayList();
                }
                Iterator inputCollectionIterator = inputCollection.iterator();
                while(inputCollectionIterator.hasNext())
                {
                    Object modelOrJalo = inputCollectionIterator.next();
                    jaloCollection.add(getSourceOrJalo(modelOrJalo));
                }
                entry.setValue(jaloCollection);
            }
        }
    }


    private Object getSourceOrJalo(Object modelOrJalo)
    {
        if(modelOrJalo instanceof ItemModel)
        {
            return this.modelService.getSource(modelOrJalo);
        }
        return modelOrJalo;
    }


    protected void filterWritableAttributes(ItemModel itemModel, Map<String, Object> jaloAttributes)
    {
        jaloAttributes.entrySet().removeIf(entry -> !canWrite(itemModel, (String)entry.getKey()));
    }


    protected boolean canWrite(ItemModel itemModel, String attributeName)
    {
        try
        {
            DataType dataType = this.typeFacade.load(this.typeFacade.getType(itemModel));
            DataAttribute attribute = dataType.getAttribute(attributeName);
            return attribute.isWritable();
        }
        catch(TypeNotFoundException ex)
        {
            throw new JaloSystemException(ex);
        }
    }
}
