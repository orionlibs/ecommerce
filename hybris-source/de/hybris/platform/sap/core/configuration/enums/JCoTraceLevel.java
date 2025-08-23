package de.hybris.platform.sap.core.configuration.enums;

import de.hybris.platform.core.HybrisEnumValue;

public enum JCoTraceLevel implements HybrisEnumValue
{
    NO_TRACE("NO_TRACE"),
    ERRORS("ERRORS"),
    ERRORS_WARNINGS("ERRORS_WARNINGS"),
    INFOS_ERRORS_WARNINGS("INFOS_ERRORS_WARNINGS"),
    EXPATH_INFOS_ERRORS_WARNINGS("EXPATH_INFOS_ERRORS_WARNINGS"),
    VERBEXPATH_INFOS_ERRORS_WARNINGS("VERBEXPATH_INFOS_ERRORS_WARNINGS"),
    VERBEXPATH_LIMDATADUMPS_INFOS_ERRORS_WARNINGS("VERBEXPATH_LIMDATADUMPS_INFOS_ERRORS_WARNINGS"),
    FULLEXPATH_DATADUMPS_VERBINFOS_ERRORS_WARNINGS("FULLEXPATH_DATADUMPS_VERBINFOS_ERRORS_WARNINGS"),
    FULLEXPATH_FULLDATADUMPS_VERBINFOS_ERRORS_WARNINGS("FULLEXPATH_FULLDATADUMPS_VERBINFOS_ERRORS_WARNINGS");
    public static final String _TYPECODE = "JCoTraceLevel";
    public static final String SIMPLE_CLASSNAME = "JCoTraceLevel";
    private final String code;


    JCoTraceLevel(String code)
    {
        this.code = code.intern();
    }


    public String getCode()
    {
        return this.code;
    }


    public String getType()
    {
        return "JCoTraceLevel";
    }
}
