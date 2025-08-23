package de.hybris.platform.directpersistence.selfhealing;

import com.google.common.base.Preconditions;
import de.hybris.platform.core.PK;
import java.util.Objects;
import org.apache.commons.lang.StringUtils;

public class ItemToHeal
{
    private final PK pk;
    private final String type;
    private final String attribute;
    private final long version;
    private final Object value;


    public ItemToHeal(PK pk, String type, String attribute, long version, Object value)
    {
        Preconditions.checkNotNull(pk, "pk is required");
        Preconditions.checkArgument(StringUtils.isNotEmpty(type), "type is required");
        Preconditions.checkArgument(StringUtils.isNotEmpty(attribute), "attribute is required");
        this.pk = pk;
        this.type = type;
        this.attribute = attribute;
        this.version = version;
        this.value = value;
    }


    public PK getPk()
    {
        return this.pk;
    }


    public String getType()
    {
        return this.type;
    }


    public String getAttribute()
    {
        return this.attribute;
    }


    public long getVersion()
    {
        return this.version;
    }


    public Object getValue()
    {
        return this.value;
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.attribute, this.pk, this.type, Long.valueOf(this.version)});
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null || !(obj instanceof ItemToHeal))
        {
            return false;
        }
        ItemToHeal other = (ItemToHeal)obj;
        return (this.version == other.version && Objects.equals(this.attribute, other.attribute) && Objects.equals(this.pk, other.pk) &&
                        Objects.equals(this.type, other.type));
    }


    public String toString()
    {
        return "ItemToHeal [pk=" + this.pk + ", type=" + this.type + ", attribute=" + this.attribute + ", version=" + this.version + ", value=" + this.value + "]";
    }
}
