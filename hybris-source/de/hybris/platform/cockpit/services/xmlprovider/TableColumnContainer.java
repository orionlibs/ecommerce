package de.hybris.platform.cockpit.services.xmlprovider;

public class TableColumnContainer
{
    private final XmlDataProvider.TABLE_COLUMN_TYPE columnType;
    private final String value;


    public TableColumnContainer(String columnValue)
    {
        this.columnType = XmlDataProvider.TABLE_COLUMN_TYPE.simple;
        this.value = columnValue;
    }


    public TableColumnContainer(XmlDataProvider.TABLE_COLUMN_TYPE columnType, String columnValue)
    {
        this.columnType = columnType;
        this.value = columnValue;
    }


    public XmlDataProvider.TABLE_COLUMN_TYPE getColumnType()
    {
        return this.columnType;
    }


    public String getValue()
    {
        return this.value;
    }
}
