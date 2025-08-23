package de.hybris.platform.persistence.polyglot.model;

import java.util.Objects;

enum WellKnownKey
{
    ITEM_PK("pk"),
    VERSION("hjmpts"),
    TYPE_REF("itemtype"),
    OWNER_REF("owner"),
    CREATION_TIME("creationtime"),
    MODIFICATION_TIME("modifiedtime");
    final NonlocalizedKey key;


    WellKnownKey(String qualifier)
    {
        this.key = new NonlocalizedKey(Objects.<String>requireNonNull(qualifier));
    }


    static NonlocalizedKey fromQualifier(String qualifier)
    {
        for(WellKnownKey k : values())
        {
            if(k.key.getQualifier().equals(qualifier))
            {
                return k.key;
            }
        }
        return null;
    }
}
