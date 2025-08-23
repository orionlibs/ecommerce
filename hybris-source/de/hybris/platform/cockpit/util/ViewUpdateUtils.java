package de.hybris.platform.cockpit.util;

import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueService;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;

public class ViewUpdateUtils
{
    private static final String SUPPRESS_NEXT_UPDATE_KEY = "suppressNextUpdate";
    public static final String MODIFIED_PROPERTIES_KEY = "modifiedProperties";
    public static final String MODIFIED_ITEM_KEY = "modifiedItem";
    private static final String CP_UPDATE_CB_OBJ = "cp_update_cb_obj";
    private static final Logger LOG = LoggerFactory.getLogger(ViewUpdateUtils.class);


    public static void performViewUpdate(Component component, Map<String, Object> ctx, boolean recursive)
    {
        Object attribute = component.getAttribute("cp_update_cb_obj");
        if(attribute instanceof UpdateCallbackObject && !Boolean.TRUE.equals(component.getAttribute("suppressNextUpdate")))
        {
            ((UpdateCallbackObject)attribute).update(component, ctx);
        }
        component.removeAttribute("suppressNextUpdate");
        if(recursive)
        {
            for(Object child : component.getChildren())
            {
                if(child instanceof Component)
                {
                    performViewUpdate((Component)child, ctx, recursive);
                }
            }
        }
    }


    public static void setUpdateCallback(Component component, UpdateCallbackObject updateCallbackObject)
    {
        Object attribute = component.getAttribute("cp_update_cb_obj");
        if(attribute instanceof UpdateCallbackObject || attribute == null)
        {
            component.setAttribute("cp_update_cb_obj", updateCallbackObject);
        }
        else
        {
            LOG.warn("Could not add updateCallback to component " + component + ", attribute key 'cp_update_cb_obj'  already in use.");
        }
    }


    public static void suppressNextUpdateOn(Component component)
    {
        if(component.getAttribute("cp_update_cb_obj") != null)
        {
            component.setAttribute("suppressNextUpdate", Boolean.TRUE);
        }
    }


    public static boolean isUpdateNeeded(Map<String, Object> ctx, TypedObject item, PropertyDescriptor propertyDescriptor, TypeService typeService, ValueService valueService)
    {
        if(isMatchingItemPropertyContext(ctx, item, propertyDescriptor))
        {
            return true;
        }
        if(propertyDescriptor != null && "REFERENCE".equals(propertyDescriptor.getEditorType()))
        {
            ObjectTemplate template = TypeTools.getValueTypeAsObjectTemplate(propertyDescriptor, typeService);
            if(template != null)
            {
                BaseType propertyValueType = template.getBaseType();
                Object modifiedItem = ctx.get("modifiedItem");
                if(modifiedItem instanceof TypedObject)
                {
                    BaseType modifiedItemType = ((TypedObject)modifiedItem).getType();
                    if(propertyValueType.isAssignableFrom((ObjectType)modifiedItemType))
                    {
                        if(PropertyDescriptor.Multiplicity.SINGLE.equals(propertyDescriptor.getMultiplicity()))
                        {
                            Object objectAttributeValue = null;
                            try
                            {
                                objectAttributeValue = valueService.getValue(item, propertyDescriptor);
                            }
                            catch(ValueHandlerException e)
                            {
                                LOG.warn(e.getMessage(), (Throwable)e);
                            }
                            return modifiedItem.equals(objectAttributeValue);
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static boolean isMatchingItemPropertyContext(Map<String, Object> ctx, TypedObject item, PropertyDescriptor propertyDescriptor)
    {
        Object object = ctx.get("modifiedItem");
        if(object != null && object.equals(item))
        {
            Object properties = ctx.get("modifiedProperties");
            if(properties == null)
            {
                return true;
            }
            if(properties instanceof Collection)
            {
                return ((Collection)properties).contains(propertyDescriptor);
            }
        }
        return false;
    }


    public static Map<String, Object> createItemPropertyContext(TypedObject item, Collection<PropertyDescriptor> modifiedProperties)
    {
        Map<String, Object> ctx = new HashMap<>();
        ctx.put("modifiedItem", item);
        ctx.put("modifiedProperties", modifiedProperties);
        return ctx;
    }
}
