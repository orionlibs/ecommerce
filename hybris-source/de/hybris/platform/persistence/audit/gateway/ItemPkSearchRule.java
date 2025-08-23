package de.hybris.platform.persistence.audit.gateway;

import de.hybris.platform.core.PK;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ItemPkSearchRule extends GenericSearchRule<Long>
{
    public static final String PK_FIELD_NAME = "itempk";


    private ItemPkSearchRule(PK pk)
    {
        super("itempk", pk.getLong(), false);
    }


    public static ItemPkSearchRule withPk(PK pk)
    {
        return new ItemPkSearchRule(pk);
    }


    public String toString()
    {
        return (new ToStringBuilder(this)).append("PK", getValue()).toString();
    }
}
