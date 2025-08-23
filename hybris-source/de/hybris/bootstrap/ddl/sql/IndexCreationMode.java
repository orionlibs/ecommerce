package de.hybris.bootstrap.ddl.sql;

public enum IndexCreationMode
{
    ALL("all"),
    FORCE("force"),
    SAP("sap"),
    HSQLDB("hsqldb"),
    ORACLE("oracle"),
    MYSQL("mysql"),
    SQLSERVER("sqlserver");
    private final String creationMode;


    IndexCreationMode(String creationMode)
    {
        this.creationMode = creationMode;
    }


    public String getCreationModeCode()
    {
        return this.creationMode;
    }
}
