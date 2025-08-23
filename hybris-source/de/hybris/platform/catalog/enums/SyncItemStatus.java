package de.hybris.platform.catalog.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum SyncItemStatus implements HybrisEnumValue
{
    IN_SYNC("IN_SYNC"),
    NOT_SYNC("NOT_SYNC"),
    COUNTERPART_MISSING("COUNTERPART_MISSING"),
    ITEM_MISSING("ITEM_MISSING"),
    NOT_APPLICABLE("NOT_APPLICABLE"),
    IN_PROGRESS("IN_PROGRESS"),
    SYNC_RULES_VIOLATED("SYNC_RULES_VIOLATED");
    public static final String _TYPECODE = "SyncItemStatus";
    public static final String SIMPLE_CLASSNAME = "SyncItemStatus";
    private final String code;


    SyncItemStatus(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "SyncItemStatus";
    }
}
