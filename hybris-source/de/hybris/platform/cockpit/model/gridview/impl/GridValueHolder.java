package de.hybris.platform.cockpit.model.gridview.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.GridProperty;
import de.hybris.platform.cockpit.services.config.GridViewConfiguration;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

public class GridValueHolder
{
    private static final String FALLBACK_PREVIEW_IMAGE_PARAMETER = "defaultPreviewImage";
    private static TypeService typeService;
    private final ObjectValueContainer valueContainer;
    private PropertyDescriptor imagePropertyDescriptor = null;
    private PropertyDescriptor labelPropertyDescriptor = null;
    private PropertyDescriptor descriptionPropertyDescriptor = null;
    private PropertyDescriptor shortInfoPropertyDescriptor = null;
    private String labelPrefix = null;
    private String descriptionPrefix = null;
    private String shortinfoPrefix = null;
    private String fallbackPreviewImage = null;


    public GridValueHolder(GridViewConfiguration config, TypedObject item)
    {
        Set<PropertyDescriptor> propertyDescriptors = new HashSet<>();
        GridProperty imageURLProperty = config.getImageURLProperty();
        if(imageURLProperty != null)
        {
            String qualifier = imageURLProperty.getQualifier();
            if(!StringUtils.isEmpty(qualifier))
            {
                this.imagePropertyDescriptor = getTypeService().getPropertyDescriptor(qualifier);
                propertyDescriptors.add(this.imagePropertyDescriptor);
                if(imageURLProperty.getParameters() != null)
                {
                    this.fallbackPreviewImage = (String)imageURLProperty.getParameters().get("defaultPreviewImage");
                }
            }
        }
        if(config.getLabelProperty() != null)
        {
            String qualifier = config.getLabelProperty().getQualifier();
            if(!StringUtils.isEmpty(qualifier))
            {
                this.labelPropertyDescriptor = getTypeService().getPropertyDescriptor(qualifier);
                propertyDescriptors.add(this.labelPropertyDescriptor);
            }
            this.labelPrefix = config.getLabelProperty().getPrefix();
        }
        if(config.getDescriptionProperty() != null)
        {
            String qualifier = config.getDescriptionProperty().getQualifier();
            if(!StringUtils.isEmpty(qualifier))
            {
                this.descriptionPropertyDescriptor = getTypeService().getPropertyDescriptor(qualifier);
                propertyDescriptors.add(this.descriptionPropertyDescriptor);
            }
            this.descriptionPrefix = config.getDescriptionProperty().getPrefix();
        }
        if(config.getShortInfoProperty() != null)
        {
            String qualifier = config.getShortInfoProperty().getQualifier();
            if(!StringUtils.isEmpty(qualifier))
            {
                this.shortInfoPropertyDescriptor = getTypeService().getPropertyDescriptor(qualifier);
                propertyDescriptors.add(this.shortInfoPropertyDescriptor);
            }
            this.shortinfoPrefix = config.getShortInfoProperty().getPrefix();
        }
        this.valueContainer = TypeTools.createValueContainer(item, propertyDescriptors, UISessionUtils.getCurrentSession()
                        .getSystemService().getAvailableLanguageIsos());
    }


    protected String getValue(PropertyDescriptor descriptor)
    {
        if(descriptor == null)
        {
            return null;
        }
        ObjectValueContainer.ObjectValueHolder value = this.valueContainer.getValue(descriptor, descriptor.isLocalized() ?
                        UISessionUtils.getCurrentSession().getGlobalDataLanguageIso() : null);
        return (value.getCurrentValue() == null) ? null : value.getCurrentValue().toString();
    }


    public String getImageURL()
    {
        String image = getValue(this.imagePropertyDescriptor);
        return (image != null) ? image : this.fallbackPreviewImage;
    }


    public String getLabel()
    {
        String value = getValue(this.labelPropertyDescriptor);
        if(value != null && this.labelPrefix != null)
        {
            value = this.labelPrefix + this.labelPrefix;
        }
        return value;
    }


    public String getDescription()
    {
        String value = getValue(this.descriptionPropertyDescriptor);
        if(value != null && this.descriptionPrefix != null)
        {
            value = this.descriptionPrefix + this.descriptionPrefix;
        }
        return value;
    }


    public String getShortInfo()
    {
        String value = getValue(this.shortInfoPropertyDescriptor);
        if(value != null && this.shortinfoPrefix != null)
        {
            value = this.shortinfoPrefix + this.shortinfoPrefix;
        }
        return value;
    }


    public TypeService getTypeService()
    {
        if(typeService == null)
        {
            typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return typeService;
    }
}
