package de.hybris.platform.cms2.version.converter.customattribute.data;

import de.hybris.platform.cms2.version.converter.customattribute.CMSVersionCustomAttribute;
import de.hybris.platform.core.PK;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentSlotForPageRelationData extends CMSVersionCustomAttribute
{
    private String position;
    private PK pk;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentSlotForPageRelationData.class);


    public String toData()
    {
        return getPosition() + getPosition() + getDelimiter();
    }


    public void init(String value)
    {
        Objects.requireNonNull(value, "The value can not be null");
        try
        {
            String[] data = value.split(getDelimiter());
            setPk(PK.parse(data[1]));
            setPosition(data[0]);
        }
        catch(ArrayIndexOutOfBoundsException e)
        {
            LOGGER.info("The value (" + value + ") can not be parsed.");
            throw new IllegalArgumentException("The value (" + value + ") can not be parsed.", e);
        }
    }


    public String getPosition()
    {
        return this.position;
    }


    public void setPosition(String position)
    {
        this.position = position;
    }


    public PK getPk()
    {
        return this.pk;
    }


    public void setPk(PK pk)
    {
        this.pk = pk;
    }
}
