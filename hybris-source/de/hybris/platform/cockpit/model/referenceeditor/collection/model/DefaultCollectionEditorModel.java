package de.hybris.platform.cockpit.model.referenceeditor.collection.model;

import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.referenceeditor.simple.impl.DefaultSimpleReferenceSelectorModel;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zkplus.spring.SpringUtil;

public class DefaultCollectionEditorModel extends AbstractCollectionEditorModel
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCollectionEditorModel.class);
    private List<Object> collectionItems = new ArrayList();
    private ObjectType rootType = null;
    private ObjectType rootSearchType = null;
    protected DefaultSimpleReferenceSelectorModel referenceSelectorModel;
    private TypeService typeService;


    public void setParameters(Map<String, ? extends Object> parameters)
    {
        this.referenceSelectorModel.setParameters(parameters);
    }


    public DefaultSimpleReferenceSelectorModel getSimpleReferenceSelectorModel()
    {
        return this.referenceSelectorModel;
    }


    public DefaultCollectionEditorModel(ObjectType rootType)
    {
        this.rootType = rootType;
        initializeReferenceSelectorModel(rootType);
    }


    public List<Object> getCollectionItems()
    {
        if(this.collectionItems == null)
        {
            this.collectionItems = new ArrayList();
        }
        return this.collectionItems;
    }


    public void setCollectionItems(Collection<Object> collectionItems)
    {
        if(this.collectionItems != collectionItems && !this.collectionItems.equals(collectionItems))
        {
            this.collectionItems.clear();
            if(collectionItems != null)
            {
                this.collectionItems.addAll(getTypeService().wrapItems(collectionItems));
            }
            fireCollectionItemsChanged();
        }
    }


    public void addCollectionItem(Object item)
    {
        if(item instanceof Collection)
        {
            addCollectionItems((Collection<? extends Object>)item);
        }
        else if(!this.collectionItems.contains(item))
        {
            this.collectionItems.add(item);
        }
    }


    public void addCollectionItems(Collection<? extends Object> collection)
    {
        for(Object item : collection)
        {
            if(!this.collectionItems.contains(item))
            {
                this.collectionItems.add(item);
            }
        }
        fireCollectionItemsChanged();
    }


    public boolean removeCollectionItem(int index)
    {
        boolean ret = false;
        if(this.collectionItems.size() <= index)
        {
            ret = false;
        }
        ret = (this.collectionItems.remove(index) != null);
        if(ret)
        {
            fireCollectionItemsChanged();
        }
        return ret;
    }


    public boolean removeCollectionItem(Object item)
    {
        boolean ret = false;
        if(this.collectionItems.contains(item))
        {
            ret = this.collectionItems.remove(item);
            if(ret)
            {
                fireCollectionItemsChanged();
            }
        }
        return ret;
    }


    public boolean moveCollectionItem(int fromIndex, int toIndex)
    {
        boolean ret = false;
        if(fromIndex < 0 || toIndex >= this.collectionItems.size())
        {
            return ret;
        }
        Object sourceItem = this.collectionItems.get(fromIndex);
        if(fromIndex < toIndex)
        {
            this.collectionItems.add(toIndex, sourceItem);
            this.collectionItems.remove(fromIndex);
            fireCollectionItemsChanged();
            ret = true;
        }
        else if(fromIndex > toIndex)
        {
            this.collectionItems.remove(fromIndex);
            this.collectionItems.add(toIndex, sourceItem);
            fireCollectionItemsChanged();
            ret = true;
        }
        return ret;
    }


    public void clearCollectionItems()
    {
        if(this.collectionItems != null)
        {
            this.collectionItems.clear();
            fireCollectionItemsChanged();
        }
    }


    public String getItemLabel(Object item)
    {
        String label = "";
        if(item instanceof TypedObject)
        {
            LabelService labelService = UISessionUtils.getCurrentSession().getLabelService();
            label = labelService.getObjectTextLabelForTypedObject((TypedObject)item);
        }
        else
        {
            LOG.warn("Can not get label for item since it is not a TypedObject.");
            if(item != null)
            {
                label = item.toString();
            }
        }
        return label;
    }


    public ObjectType getRootType()
    {
        return this.rootType;
    }


    public void setRootType(ObjectType rootType)
    {
        if((this.rootType != null && !this.rootType.equals(rootType)) || (this.rootType == null && rootType != null))
        {
            this.rootType = rootType;
            this.referenceSelectorModel.setRootType(rootType);
            fireRootTypeChanged();
            if(this.rootSearchType == null)
            {
                this.referenceSelectorModel.setRootSearchType(rootType);
                fireRootSearchTypeChanged();
            }
        }
    }


    public ObjectType getRootSearchType()
    {
        return (this.rootSearchType == null) ? this.rootType : this.rootSearchType;
    }


    public void setRootSearchType(ObjectType rootSearchType)
    {
        this.rootSearchType = rootSearchType;
        this.referenceSelectorModel.setRootSearchType(rootSearchType);
        fireRootSearchTypeChanged();
    }


    protected void initializeReferenceSelectorModel(ObjectType rootType)
    {
        this.referenceSelectorModel = new DefaultSimpleReferenceSelectorModel(rootType);
        this.referenceSelectorModel.setRootSearchType(getRootSearchType());
    }


    public TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = (TypeService)SpringUtil.getBean("cockpitTypeService");
        }
        return this.typeService;
    }
}
