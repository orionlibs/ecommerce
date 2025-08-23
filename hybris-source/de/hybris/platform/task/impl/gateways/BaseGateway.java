package de.hybris.platform.task.impl.gateways;

public interface BaseGateway
{
    String getTableName();


    boolean createTable();


    void dropTable();


    boolean doesTableExist();
}
