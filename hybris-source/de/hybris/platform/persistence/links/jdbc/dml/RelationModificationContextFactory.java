package de.hybris.platform.persistence.links.jdbc.dml;

import de.hybris.platform.persistence.links.jdbc.JdbcLinkOperation;

public interface RelationModificationContextFactory
{
    RelationModifictionContext createContextForNewTransaction(JdbcLinkOperation paramJdbcLinkOperation);


    RelationModifictionContext createContextForRunningTransaction(JdbcLinkOperation paramJdbcLinkOperation);
}
