package de.hybris.platform.directpersistence.statement.backend;

import de.hybris.platform.core.PK;
import java.util.Date;

public enum ServiceCol
{
    HJMP_TS("hjmpTS", Long.class),
    PK_STRING("PK", PK.class),
    CREATED_TS("createdTS", Date.class),
    MODIFIED_TS("modifiedTS", Date.class),
    TYPE_PK_STR("TypePkString", PK.class),
    ITEM_PK("ITEMPK", PK.class),
    ITEM_TYPE_PK("ITEMTYPEPK", PK.class),
    LANG_PK("LANGPK", PK.class),
    VALUE1("VALUE1", Object.class),
    NAME("NAME", String.class),
    REALNAME("REALNAME", String.class),
    TYPE1("TYPE1", Integer.class),
    VALUESTRING1("VALUESTRING1", String.class),
    OWNER_PK_STRING("OwnerPkString", PK.class),
    ACL_TS("aCLTS", Long.class),
    PROP_TS("propTS", Long.class),
    QUALIFIER("Qualifier", String.class),
    SOURCE_PK("SourcePK", PK.class),
    TARGET_PK("TargetPK", PK.class),
    SEQUENCE_NUMBER("SequenceNumber", Integer.class),
    RSEQUENCE_NUMBER("RSequenceNumber", Integer.class),
    LANGUAGEPK("languagepk", PK.class),
    SEALED("sealed", Integer.class);
    private final String colName;
    private final Class typeClass;


    ServiceCol(String colName, Class typeClass)
    {
        this.colName = colName;
        this.typeClass = typeClass;
    }


    public Class typeClass()
    {
        return this.typeClass;
    }


    public String colName()
    {
        return this.colName;
    }


    public String toString()
    {
        return colName();
    }
}
