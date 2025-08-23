package de.hybris.platform.platformbackoffice.accessors;

import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotPersistedAttributeReadException;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.Ordered;
import org.springframework.expression.AccessException;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypedValue;

public class JaloPropertyNotVisibleInModelAccessor implements PropertyAccessor, Ordered
{
    private static final String MODEL_PK_SET_MODIFIED_JALO_ATTRIBUTES = "modifiedJaloAttributes";
    private static final String MODEL_MODIFIED_JALO_ATTRIBUTES = "modifiedJaloAttributes_";
    private static final Class[] targetClasses = new Class[] {ItemModel.class};
    private static final int DEFAULT_ORDER = 700;
    private ModelService modelService;
    private ObjectFacade objectFacade;
    private TypeFacade typeFacade;
    private PermissionFacade permissionFacade;
    private Map<String, Set<Class>> supportedJaloAttributes = new HashMap<>();
    private int order = 700;


    public int getOrder()
    {
        return this.order;
    }


    public void setOrder(int order)
    {
        this.order = order;
    }


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
    public void setPermissionFacade(PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    @Required
    public void setObjectFacade(ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    public Map<String, Set<Class>> getSupportedJaloAttributes()
    {
        return this.supportedJaloAttributes;
    }


    public void setSupportedJaloAttributes(Map<String, Set<Class>> supportedJaloAttributes)
    {
        this.supportedJaloAttributes = supportedJaloAttributes;
    }


    public Class[] getSpecificTargetClasses()
    {
        return targetClasses;
    }


    public boolean canRead(EvaluationContext evaluationContext, Object currentObject, String attribute) throws AccessException
    {
        return (isSupportedAttribute(currentObject, attribute) && hasReadPermission(currentObject, attribute));
    }


    protected boolean isSupportedAttribute(Object currentObject, String attribute)
    {
        if(this.supportedJaloAttributes != null && this.supportedJaloAttributes.containsKey(attribute))
        {
            Set<Class<?>> classes = this.supportedJaloAttributes.get(attribute);
            for(Class<?> supportedClass : classes)
            {
                if(supportedClass.isAssignableFrom(currentObject.getClass()))
                {
                    return true;
                }
            }
        }
        return false;
    }


    public TypedValue read(EvaluationContext evaluationContext, Object target, String attributeName) throws AccessException
    {
        if(this.objectFacade.isNew(target))
        {
            throw new ObjectNotPersistedAttributeReadException(target.toString().concat(attributeName));
        }
        Map<String, Object> map = lookupModifiedJaloAttributes(lookupWidgetModel(evaluationContext), (ItemModel)target);
        if(map.containsKey(attributeName))
        {
            return new TypedValue(map.get(attributeName));
        }
        Item source = (Item)this.modelService.getSource(target);
        try
        {
            Object attributeValue = source.getAttribute(attributeName);
            if(attributeValue == null)
            {
                return new TypedValue(null);
            }
            if(attributeValue instanceof Collection)
            {
                return createTypedValueForCollection(attributeName, map, attributeValue);
            }
            Object modelAttribute = getModelFromSource(attributeValue);
            map.put(attributeName, modelAttribute);
            return new TypedValue(modelAttribute);
        }
        catch(JaloSecurityException ex)
        {
            throw new AccessException("Cannot read Jalo property", ex);
        }
    }


    private TypedValue createTypedValueForCollection(String attributeName, Map<String, Object> map, Object attributeValue)
    {
        if(attributeValue instanceof List)
        {
            List modelList = createModelList((Collection)attributeValue);
            map.put(attributeName, modelList);
            return new TypedValue(modelList);
        }
        Set modelSet = createModelSet((Collection)attributeValue);
        map.put(attributeName, modelSet);
        return new TypedValue(modelSet);
    }


    private List createModelList(Collection attributeValue)
    {
        List<Object> modelList = new ArrayList();
        for(Object itemFromList : attributeValue)
        {
            modelList.add(getModelFromSource(itemFromList));
        }
        return modelList;
    }


    private Set createModelSet(Collection attributeValue)
    {
        Set<Object> modelSet = new HashSet();
        for(Object itemFromSet : attributeValue)
        {
            modelSet.add(getModelFromSource(itemFromSet));
        }
        return modelSet;
    }


    protected Object getModelFromSource(Object object)
    {
        if(object instanceof Item)
        {
            Object modelInstance = this.modelService.get(object);
            return (modelInstance == null) ? object : modelInstance;
        }
        return object;
    }


    public boolean canWrite(EvaluationContext evaluationContext, Object object, String qualifier) throws AccessException
    {
        return (isSupportedAttribute(object, qualifier) && isAttributeWritable(object, qualifier) &&
                        hasWritePermission(object, qualifier));
    }


    protected boolean isAttributeWritable(Object object, String qualifier)
    {
        try
        {
            DataType dataType = this.typeFacade.load(this.typeFacade.getType(object));
            DataAttribute attribute = dataType.getAttribute(qualifier);
            return attribute.isWritable();
        }
        catch(TypeNotFoundException ex)
        {
            throw new JaloSystemException(ex);
        }
    }


    protected boolean hasReadPermission(Object object, String attributeName)
    {
        return this.permissionFacade.canReadInstanceProperty(object, attributeName);
    }


    protected boolean hasWritePermission(Object object, String attributeName)
    {
        return this.permissionFacade.canChangeInstanceProperty(object, attributeName);
    }


    public void write(EvaluationContext evaluationContext, Object target, String attributeName, Object attributeValue) throws AccessException
    {
        if(this.objectFacade.isNew(target))
        {
            throw new ObjectNotPersistedAttributeReadException(target.toString().concat(attributeName));
        }
        Map<String, Object> map = lookupModifiedJaloAttributes(lookupWidgetModel(evaluationContext), (ItemModel)target);
        map.put(attributeName, attributeValue);
    }


    private Map<String, Object> lookupModifiedJaloAttributes(WidgetModel widgetModel, ItemModel itemModel)
    {
        if(widgetModel != null)
        {
            Map<String, Object> map = (Map<String, Object>)widgetModel.getValue(createModifiedJaloAttributesModelKey(itemModel), Map.class);
            if(map == null)
            {
                map = new HashMap<>();
                widgetModel.put(createModifiedJaloAttributesModelKey(itemModel), map);
                updatePkSet(widgetModel, itemModel);
            }
            return map;
        }
        return new HashMap<>();
    }


    private static void updatePkSet(WidgetModel widgetModel, ItemModel itemModel)
    {
        Set<PK> pkSet = (Set<PK>)widgetModel.getValue("modifiedJaloAttributes", Set.class);
        if(pkSet == null)
        {
            pkSet = new HashSet<>();
            widgetModel.put("modifiedJaloAttributes", pkSet);
        }
        pkSet.add(itemModel.getPk());
    }


    private static String createModifiedJaloAttributesModelKey(ItemModel itemModel)
    {
        return "modifiedJaloAttributes_" + itemModel.getPk();
    }


    private static WidgetModel lookupWidgetModel(EvaluationContext evaluationContext)
    {
        WidgetModel ret = null;
        TypedValue typedRootObject = evaluationContext.getRootObject();
        if(typedRootObject != null && typedRootObject.getValue() instanceof WidgetModel)
        {
            ret = (WidgetModel)typedRootObject.getValue();
        }
        return ret;
    }
}
