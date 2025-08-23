package de.hybris.platform.persistence.links.jdbc.dml.relation;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import de.hybris.platform.persistence.links.jdbc.JdbcLinksSupport;
import de.hybris.platform.persistence.links.jdbc.dml.Relation;
import de.hybris.platform.persistence.links.jdbc.dml.RelationsSearchParams;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

class RelationsExtractor implements ResultSetExtractor<Iterable<Relation>>
{
    private final boolean isOrdered;
    private final String parentPKColumnName;
    private final String languagePKColumnName;
    private final String relationPKColumnName;
    private final String childPKColumnName;
    private final String childPositionColumnName;
    private final String versionColumnName;
    private final boolean markAsModified;
    private final Map<RelationId, BaseRelation.Builder> relationBuilders;
    private boolean used = false;


    public RelationsExtractor(RelationsSearchParams params)
    {
        Preconditions.checkNotNull(params, "params can't be null");
        this.markAsModified = params.isMarkAsModified();
        this.isOrdered = params.isParentToChildOrdered();
        this.relationPKColumnName = DatabaseHelper.getPkColumnName(params);
        this.parentPKColumnName = DatabaseHelper.getParentPKColumnName(params);
        this.languagePKColumnName = DatabaseHelper.getLanguagePKColumnName(params);
        this.childPKColumnName = DatabaseHelper.getChildPKColumnName(params);
        this.childPositionColumnName = DatabaseHelper.getOrderColumn(params);
        this.versionColumnName = DatabaseHelper.getVersionColumnName(params);
        this.relationBuilders = new HashMap<>();
        for(Long langPK : params.getAllLanguagePKs())
        {
            for(Long parentPK : params.getParentPKs())
            {
                RelationId id = new RelationId(langPK.longValue(), parentPK.longValue());
                BaseRelation.Builder builder = createBuilder(id);
                this.relationBuilders.put(id, builder);
            }
        }
    }


    public Iterable<Relation> extractData(ResultSet rs) throws SQLException, DataAccessException
    {
        if(this.used)
        {
            throw new IllegalStateException(RelationsExtractor.class.getSimpleName() + " can't be reused.");
        }
        this.used = true;
        while(rs.next())
        {
            RelationId id = extractId(rs);
            BaseRelation.Builder builder = this.relationBuilders.get(id);
            if(builder == null)
            {
                builder = createBuilder(id);
                this.relationBuilders.put(id, builder);
            }
            ExistingLinkToChild existingLink = extractExistingLink(rs);
            builder.add(existingLink);
        }
        return buildAllRelations(this.relationBuilders.values());
    }


    private ExistingLinkToChild extractExistingLink(ResultSet rs) throws SQLException
    {
        long relationPK = rs.getLong(this.relationPKColumnName);
        long childPK = rs.getLong(this.childPKColumnName);
        int position = rs.getInt(this.childPositionColumnName);
        long version = rs.getLong(this.versionColumnName);
        return new ExistingLinkToChild(relationPK, childPK, position, version);
    }


    private RelationId extractId(ResultSet rs) throws SQLException
    {
        long parentPK = rs.getLong(this.parentPKColumnName);
        long langPK = getLangPK(rs);
        return new RelationId(langPK, parentPK);
    }


    private BaseRelation.Builder createBuilder(RelationId id)
    {
        return (this.isOrdered ? OrderedRelation.builder(id) : UnorderedRelation.builder(id)).withMarkAsModified(this.markAsModified);
    }


    private Iterable<Relation> buildAllRelations(Collection<BaseRelation.Builder> relationBuilders)
    {
        ImmutableList.Builder<Relation> resultBuilder = ImmutableList.builder();
        for(BaseRelation.Builder relationBuilder : relationBuilders)
        {
            resultBuilder.add(relationBuilder.build());
        }
        return (Iterable<Relation>)resultBuilder.build();
    }


    private long getLangPK(ResultSet rs) throws SQLException
    {
        return (rs.getObject(this.languagePKColumnName) == null) ? JdbcLinksSupport.NONE_LANGUAGE_PK_VALUE.longValue() :
                        rs.getLong(this.languagePKColumnName);
    }
}
