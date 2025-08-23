package de.hybris.platform.cockpit.services.values;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.validation.pojos.CockpitValidationDescriptor;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ValueHandlerException extends Exception
{
    private Set<CockpitValidationDescriptor> properties = Collections.EMPTY_SET;


    public ValueHandlerException(Set<CockpitValidationDescriptor> properties)
    {
        this(null, properties);
    }


    public ValueHandlerException(String message, Throwable cause)
    {
        super(message, cause);
    }


    public ValueHandlerException(Throwable cause, Set<CockpitValidationDescriptor> properties)
    {
        super((cause != null) ? (cause.getMessage() + " " + cause.getMessage()) : buildMessages(properties), cause);
        setProperties((properties == null) ? Collections.EMPTY_SET : properties);
    }


    @Deprecated
    public ValueHandlerException(String message, Set<PropertyDescriptor> properties)
    {
        this(message, null, properties);
    }


    @Deprecated
    public ValueHandlerException(String message, Throwable cause, Set<PropertyDescriptor> properties)
    {
        super(message, cause);
        if(properties == null)
        {
            setProperties(Collections.EMPTY_SET);
        }
        else
        {
            setProperties(new HashSet<>());
            for(PropertyDescriptor propertyDescriptor : properties)
            {
                CockpitValidationDescriptor props = new CockpitValidationDescriptor(propertyDescriptor, 3, "Error has occured during validation", null);
                getProperties().add(props);
            }
        }
    }


    @Deprecated
    public Set<PropertyDescriptor> getRelatedProperties()
    {
        Set<PropertyDescriptor> result = new HashSet<>();
        for(CockpitValidationDescriptor valDescriptor : getProperties())
        {
            result.add(valDescriptor.getPropertyDescriptor());
        }
        return result;
    }


    public Set<CockpitValidationDescriptor> getProperties()
    {
        return this.properties;
    }


    protected void setProperties(Set<CockpitValidationDescriptor> properties)
    {
        ServicesUtil.validateParameterNotNull(properties, "Properties may not be null! Pass empty set instead.");
        this.properties = properties;
    }


    private static String buildMessages(Set<CockpitValidationDescriptor> validationInfo)
    {
        StringBuffer result = new StringBuffer(17);
        if(validationInfo != null)
        {
            for(CockpitValidationDescriptor valDescriptor : validationInfo)
            {
                result.append(valDescriptor.getValidationMessage());
                result.append(' ');
            }
        }
        return result.toString();
    }
}
