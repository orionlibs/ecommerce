package de.hybris.platform.persistence.links.jdbc;

public interface JdbcLinkOperationExecutor
{
    void execute(JdbcInsertLinkOperation paramJdbcInsertLinkOperation);


    void execute(JdbcRemoveLinkOperation paramJdbcRemoveLinkOperation);


    void execute(JdbcSetLinkOperation paramJdbcSetLinkOperation);
}
