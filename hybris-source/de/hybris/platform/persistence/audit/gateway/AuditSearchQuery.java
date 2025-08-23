package de.hybris.platform.persistence.audit.gateway;

import de.hybris.platform.audit.internal.config.Type;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditSearchQuery
{
    private static final Logger LOG = LoggerFactory.getLogger(AuditSearchQuery.class);
    private final String typeCode;
    private final List<SearchRule> searchRules;
    private final LinkAuditRecord.LinkSide linkSide;
    private final boolean hasPayloadRules;
    private final boolean hasStandardRules;


    private AuditSearchQuery(QueryBuilder builder)
    {
        this.typeCode = Objects.<String>requireNonNull(builder.typeCode, "typeCode is required to build audit related search query");
        this.searchRules = builder.searchRules;
        this.hasPayloadRules = builder.searchRules.stream().anyMatch(SearchRule::isForPayload);
        this.hasStandardRules = builder.searchRules.stream().anyMatch(c -> !c.isForPayload());
        this.linkSide = null;
        logDebugQueryForOnlyType();
    }


    private AuditSearchQuery(LinkQueryBuilder builder)
    {
        this.typeCode = Objects.<String>requireNonNull(builder.typeCode, "typeCode is required to build audit related search query");
        this.searchRules = builder.searchRules;
        this.linkSide = builder.linkSide;
        this.hasPayloadRules = false;
        this.hasStandardRules = true;
        logDebugQueryForOnlyType();
    }


    private void logDebugQueryForOnlyType()
    {
        if(CollectionUtils.isEmpty(this.searchRules))
        {
            LOG.debug("You are about to query whole audit table for type {}. You may encounter out of memory issues.", this.typeCode);
        }
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public LinkAuditRecord.LinkSide getLinkSide()
    {
        return this.linkSide;
    }


    public boolean isLinkRelatedQuery()
    {
        return (this.linkSide != null);
    }


    public List<SearchRule> getStandardSearchRules()
    {
        return (List<SearchRule>)this.searchRules.stream().filter(c -> !c.isForPayload()).collect(Collectors.toList());
    }


    public List<SearchRule> getPayloadSearchRules()
    {
        return (List<SearchRule>)this.searchRules.stream().filter(SearchRule::isForPayload).collect(Collectors.toList());
    }


    public boolean hasStandardSearchRules()
    {
        return this.hasStandardRules;
    }


    public boolean hasPayloadSearchRules()
    {
        return this.hasPayloadRules;
    }


    public static QueryBuilder forType(String typeCode)
    {
        return new QueryBuilder(typeCode);
    }


    public static QueryBuilder forType(Type type)
    {
        Objects.requireNonNull(type, "type is required");
        return new QueryBuilder(type.getCode());
    }


    public static LinkQueryBuilder forLink(String linkTypeCode)
    {
        return new LinkQueryBuilder(linkTypeCode);
    }


    public static LinkQueryBuilder forLink(Type linkType)
    {
        Objects.requireNonNull(linkType, "linkType is required");
        return new LinkQueryBuilder(linkType.getCode());
    }
}
