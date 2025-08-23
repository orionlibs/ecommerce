package de.hybris.platform.cockpit.model.collection.impl;

import de.hybris.platform.cockpit.model.CockpitObjectAbstractCollectionModel;
import de.hybris.platform.cockpit.model.collection.ObjectCollection;
import de.hybris.platform.cockpit.model.collection.PageableObjectCollection;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.ObjectCollectionService;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.zkoss.spring.SpringUtil;

public class ObjectCollectionImpl implements PageableObjectCollection
{
    private final CockpitObjectAbstractCollectionModel collectionModel;
    private String qualifier;
    private String label;
    private final Map<String, String> allLabels = new HashMap<>();
    private String description;
    private final String type;
    private UserModel user;


    public ObjectCollectionImpl(CockpitObjectAbstractCollectionModel collectionModel)
    {
        this.collectionModel = collectionModel;
        this.label = collectionModel.getLabel();
        this.description = collectionModel.getDescription();
        this.qualifier = collectionModel.getQualifier();
        this.type = collectionModel.getItemtype();
        this.user = collectionModel.getUser();
    }


    @Deprecated
    public ObjectCollectionImpl(List<TypedObject> elements, String qualifier, String label, Map<String, String> allLabels, String description, PK pk, UserModel user)
    {
        this((CockpitObjectAbstractCollectionModel)getModelService().get(pk));
    }


    @Deprecated
    public ObjectCollectionImpl(List<TypedObject> elements, String qualifier, String label, Map<String, String> allLabels, String description, PK pk, UserModel user, String type)
    {
        this((CockpitObjectAbstractCollectionModel)getModelService().get(pk));
    }


    public List<TypedObject> getElements()
    {
        return getObjectCollectionService().getElements((ObjectCollection)this, 0, Config.getInt("cockpit.collections.max_element_size", -1));
    }


    @Deprecated
    public void setElements(List<TypedObject> elements)
    {
    }


    public List<TypedObject> getElements(int offset, int count)
    {
        return getObjectCollectionService().getElements((ObjectCollection)this, offset, count);
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public void setQualifier(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public String getLabel()
    {
        String ret = this.label;
        if(ret == null)
        {
            for(Map.Entry<String, String> entry : this.allLabels.entrySet())
            {
                if(entry.getValue() != null)
                {
                    ret = entry.getValue();
                    break;
                }
            }
        }
        if(ret == null)
        {
            ret = "unnamed";
        }
        return ret;
    }


    public void setLabel(String label)
    {
        this.label = label;
    }


    public void setAllLabels(Map<String, String> labels)
    {
        this.allLabels.clear();
        this.allLabels.putAll(labels);
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public int getTotalCount()
    {
        return getObjectCollectionService().getElementCount((ObjectCollection)this);
    }


    public Date getCreationTime()
    {
        return null;
    }


    public Date getModificationTime()
    {
        return null;
    }


    public String getType()
    {
        return this.type;
    }


    public PK getPK()
    {
        return this.collectionModel.getPk();
    }


    public void setUser(UserModel user)
    {
        this.user = user;
    }


    public UserModel getUser()
    {
        return this.user;
    }


    public boolean equals(Object obj)
    {
        return (obj instanceof ObjectCollectionImpl && ((ObjectCollectionImpl)obj).getPK().equals(getPK()));
    }


    public String getLabel(String iso)
    {
        return this.allLabels.get(iso);
    }


    public static ModelService getModelService()
    {
        return (ModelService)SpringUtil.getBean("modelService");
    }


    public static ObjectCollectionService getObjectCollectionService()
    {
        return (ObjectCollectionService)SpringUtil.getBean("objectCollectionService");
    }
}
