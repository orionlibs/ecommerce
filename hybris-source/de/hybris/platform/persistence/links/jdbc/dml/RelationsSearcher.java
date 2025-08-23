package de.hybris.platform.persistence.links.jdbc.dml;

public interface RelationsSearcher
{
    Iterable<Relation> search(RelationsSearchParams paramRelationsSearchParams);
}
