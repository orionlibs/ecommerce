package de.hybris.platform.persistence.links.jdbc.dml;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import de.hybris.platform.persistence.links.jdbc.JdbcLinksSupport;

public class RelationsSearchParams
{
    private final Iterable<Long> languagePKs;
    private final Iterable<Long> parentPKs;
    private final boolean isParentSource;
    private final boolean isTargetToSourceOrdered;
    private final boolean isSourceToTargetOrdered;
    private final String relationCode;
    private final boolean markAsModified;


    public RelationsSearchParams(Iterable<Long> languagePKs, Iterable<Long> parentPKs, String relationCode, boolean isParentSource, boolean isTargetToSourceOrdered, boolean isSourceToTargetOrdered, boolean markAsModified)
    {
        Preconditions.checkNotNull(languagePKs, "languagePKs can't be null");
        Preconditions.checkNotNull(parentPKs, "parentPKs can't be null");
        Preconditions.checkNotNull(relationCode, "relationCode can't be null");
        this.languagePKs = languagePKs;
        this.parentPKs = parentPKs;
        this.relationCode = relationCode;
        this.isParentSource = isParentSource;
        this.isTargetToSourceOrdered = isTargetToSourceOrdered;
        this.isSourceToTargetOrdered = isSourceToTargetOrdered;
        this.markAsModified = markAsModified;
    }


    public Iterable<Long> getParentPKs()
    {
        return this.parentPKs;
    }


    public boolean isParentSource()
    {
        return this.isParentSource;
    }


    public String getRelationCode()
    {
        return this.relationCode;
    }


    public boolean areValid()
    {
        return (!Iterables.isEmpty(this.parentPKs) && (isSearchForAllLanguages() || !Iterables.isEmpty(this.languagePKs)));
    }


    public int getNumberOfParentPKs()
    {
        return Iterables.size(this.parentPKs);
    }


    public boolean isSearchForAllLanguages()
    {
        return (this.languagePKs == JdbcLinksSupport.ALL_LANGUAGES);
    }


    public boolean isSearchForNonLocalizedRelations()
    {
        return Iterables.contains(this.languagePKs, JdbcLinksSupport.NONE_LANGUAGE_PK_VALUE);
    }


    public int getNumberOfLocalizableLanguagePKs()
    {
        return Iterables.size(getLocalizableLanguagePKs());
    }


    public Iterable<Long> getLocalizableLanguagePKs()
    {
        if(isSearchForAllLanguages())
        {
            return (Iterable<Long>)ImmutableList.of();
        }
        return Iterables.filter(getAllLanguagePKs(), Predicates.not(Predicates.equalTo(JdbcLinksSupport.NONE_LANGUAGE_PK_VALUE)));
    }


    public Iterable<Long> getAllLanguagePKs()
    {
        return this.languagePKs;
    }


    public boolean isParentToChildOrdered()
    {
        return this.isParentSource ? this.isSourceToTargetOrdered : this.isTargetToSourceOrdered;
    }


    public boolean isMarkAsModified()
    {
        return this.markAsModified;
    }
}
