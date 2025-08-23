package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class ParsedSubtype extends ParsedType
{
    private final ParsedType superType;


    ParsedSubtype(ParsedType superType, String code) throws FlexibleSearchException
    {
        super(superType.getFrom(), code, superType.getAlias(), superType.dontIncludeSubtypes(), superType.disableTypeChecks(), superType
                        .excludeSubtypesWithOwnDeployment());
        this.superType = superType;
    }


    protected void createParsedSubtypesAndRestrictionClauses()
    {
        throw new IllegalStateException("subtypes doesnt create their own subtypes or restriction clauses");
    }


    ParsedType getSuperType()
    {
        return this.superType;
    }


    protected void checkType() throws FlexibleSearchException
    {
    }


    protected Collection getApplicableRestrictions() throws FlexibleSearchException
    {
        if(isAbstract())
        {
            throw new IllegalStateException("type is abstract");
        }
        Collection ret = new ArrayList();
        ret.addAll(getOwnRestrictions());
        for(ParsedType st = getSuperType(); st != null;
                        st = (st instanceof ParsedSubtype) ? ((ParsedSubtype)st).getSuperType() : null)
        {
            ret.addAll(st.getOwnRestrictions());
        }
        return ret;
    }


    protected final Collection getSuperTypeRealRestrictions()
    {
        return Collections.EMPTY_LIST;
    }


    boolean isJoined()
    {
        return this.superType.isJoined();
    }


    int getIndex()
    {
        return this.superType.getIndex();
    }


    String getAlias()
    {
        return this.superType.getAlias();
    }


    FromClause getFrom()
    {
        return this.superType.getFrom();
    }


    protected Table getSpecialFieldsTable()
    {
        return this.superType.getSpecialFieldsTable();
    }


    protected void setSpecialFieldsTable(Table sft)
    {
        this.superType.setSpecialFieldsTable(sft);
    }


    protected Map<PK, Table> getCustomLocTables()
    {
        return this.superType.getCustomLocTables();
    }


    protected void setCustomLocTables(Map<PK, Table> map)
    {
        this.superType.setCustomLocTables(map);
    }


    protected Map getDumpTables()
    {
        return this.superType.getDumpTables();
    }


    protected void setDumpTables(Map map)
    {
        this.superType.setDumpTables(map);
    }


    protected Table getCoreTable()
    {
        return this.superType.getCoreTable();
    }


    protected void setCoreTable(Table table)
    {
        this.superType.setCoreTable(table);
    }


    protected Table getUnlocTable()
    {
        return this.superType.getUnlocTable();
    }


    protected void setUnlocTable(Table table)
    {
        this.superType.setUnlocTable(table);
    }


    protected Table getDefaultLocTable()
    {
        return this.superType.getDefaultLocTable();
    }


    protected void setDefaultLocTable(Table table)
    {
        this.superType.setDefaultLocTable(table);
    }


    protected Table getIgnoreLocTable()
    {
        return this.superType.getIgnoreLocTable();
    }


    protected void setIgnoreLocTable(Table table)
    {
        this.superType.setIgnoreLocTable(table);
    }


    TypeJoin getTypeJoin()
    {
        return this.superType.getTypeJoin();
    }


    protected Table getOrCreateCoreTable()
    {
        return this.superType.getOrCreateCoreTable();
    }


    protected Table getOrCreateDumpTable(TableField field) throws FlexibleSearchException
    {
        return this.superType.getOrCreateDumpTable(field);
    }


    protected Table getOrCreateLocalizedTable(TableField field) throws FlexibleSearchException
    {
        return this.superType.getOrCreateLocalizedTable(field);
    }


    int getLocTableCount()
    {
        return this.superType.getLocTableCount();
    }


    int getPropsTableCount()
    {
        return this.superType.getPropsTableCount();
    }


    protected ParsedType getPlaceholderType()
    {
        ParsedType root = getSuperType();
        ParsedType next = (root instanceof ParsedSubtype) ? ((ParsedSubtype)root).getSuperType() : null;
        while(next != null)
        {
            root = next;
            next = (root instanceof ParsedSubtype) ? ((ParsedSubtype)root).getSuperType() : null;
        }
        return root;
    }


    protected void translate() throws FlexibleSearchException
    {
        if(!isTranslated())
        {
            if(this.skipTranslation)
            {
                this.skipTranslation = false;
                translateRestrictionsAndSubtypes();
            }
            else
            {
                translateRestrictionsAndSubtypes();
                setBuffer(new StringBuilder());
            }
        }
    }
}
