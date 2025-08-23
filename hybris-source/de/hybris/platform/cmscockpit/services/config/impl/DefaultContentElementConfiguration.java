package de.hybris.platform.cmscockpit.services.config.impl;

import de.hybris.platform.cmscockpit.services.config.ContentElementConfiguration;
import de.hybris.platform.cockpit.model.meta.ObjectType;

public class DefaultContentElementConfiguration implements ContentElementConfiguration
{
    private String name = null;
    private String description = null;
    private String image = null;
    private String refImage = null;
    private String imageSmall = null;
    private ObjectType type = null;


    public DefaultContentElementConfiguration()
    {
        this(null);
    }


    public DefaultContentElementConfiguration(ObjectType type)
    {
        this(type, null);
    }


    public DefaultContentElementConfiguration(ObjectType type, String name)
    {
        this(type, name, null);
    }


    public DefaultContentElementConfiguration(ObjectType type, String name, String description)
    {
        this(type, name, description, null);
    }


    public DefaultContentElementConfiguration(ObjectType type, String name, String description, String image)
    {
        this(type, name, description, image, null);
    }


    public DefaultContentElementConfiguration(ObjectType type, String name, String description, String image, String refImage)
    {
        this(type, name, description, image, refImage, null);
    }


    public DefaultContentElementConfiguration(ObjectType type, String name, String description, String image, String refImage, String imageSmall)
    {
        this.type = type;
        this.name = name;
        this.description = description;
        this.image = image;
        this.refImage = refImage;
        this.imageSmall = imageSmall;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getImage()
    {
        return this.image;
    }


    public void setImage(String image)
    {
        this.image = image;
    }


    public String getRefImage()
    {
        return this.refImage;
    }


    public void setRefImage(String refImage)
    {
        this.refImage = refImage;
    }


    public String getImageSmall()
    {
        return this.imageSmall;
    }


    public void setImageSmall(String imageSmall)
    {
        this.imageSmall = imageSmall;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public ObjectType getType()
    {
        return this.type;
    }


    public void setType(ObjectType type)
    {
        this.type = type;
    }
}
