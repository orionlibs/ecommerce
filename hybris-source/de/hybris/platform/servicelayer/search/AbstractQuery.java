package de.hybris.platform.servicelayer.search;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.search.restriction.session.SessionSearchRestriction;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public abstract class AbstractQuery
{
    private static final List ITEM_RESULTLIST = Collections.singletonList(Item.class);
    private int count = -1;
    private int start = 0;
    private boolean needTotal = false;
    private boolean typeExclusive = false;
    private Locale locale = null;
    private Collection<SessionSearchRestriction> sessionSearchRestrictions;
    private Collection<CatalogVersionModel> catalogVersions = null;
    private List resultClasses = ITEM_RESULTLIST;
    private boolean failOnUnknownFields = true;
    private UserModel user = null;
    private boolean disableSearchRestrictions = false;
    private Boolean disableSpecificDbLimitSupport = null;
    private boolean disableCaching = false;


    public boolean isDisableSearchRestrictions()
    {
        return this.disableSearchRestrictions;
    }


    public void setDisableSearchRestrictions(boolean disableSearchRestrictions)
    {
        this.disableSearchRestrictions = disableSearchRestrictions;
    }


    public void setTypeExclusive(boolean typeExclusive)
    {
        this.typeExclusive = typeExclusive;
    }


    public void setFailOnUnknownFields(boolean isFailOnUnknownFields)
    {
        this.failOnUnknownFields = isFailOnUnknownFields;
    }


    public boolean isFailOnUnknownFields()
    {
        return this.failOnUnknownFields;
    }


    @Deprecated(since = "ages", forRemoval = true)
    private LanguageModel language = null;


    public boolean isTypeExclusive()
    {
        return this.typeExclusive;
    }


    public int getCount()
    {
        return this.count;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public LanguageModel getLanguage()
    {
        return this.language;
    }


    public Locale getLocale()
    {
        return this.locale;
    }


    public Collection<SessionSearchRestriction> getSessionSearchRestrictions()
    {
        return this.sessionSearchRestrictions;
    }


    public int getStart()
    {
        return this.start;
    }


    public boolean isNeedTotal()
    {
        return this.needTotal;
    }


    public void setCount(int count)
    {
        this.count = count;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setLanguage(LanguageModel language)
    {
        this.language = language;
    }


    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }


    public void setNeedTotal(boolean needTotal)
    {
        this.needTotal = needTotal;
    }


    public void setSessionSearchRestrictions(Collection<SessionSearchRestriction> sessionSearchRestrictions)
    {
        this.sessionSearchRestrictions = sessionSearchRestrictions;
    }


    public void setSessionSearchRestrictions(SessionSearchRestriction... sessionSearchRestrictions)
    {
        setSessionSearchRestrictions(Arrays.asList(sessionSearchRestrictions));
    }


    public void setStart(int start)
    {
        this.start = start;
    }


    public <T extends Class> List<T> getResultClassList()
    {
        return this.resultClasses;
    }


    public <T extends Class> void setResultClassList(List<T> resultClassList)
    {
        this.resultClasses = resultClassList;
    }


    public Collection<CatalogVersionModel> getCatalogVersions()
    {
        return this.catalogVersions;
    }


    public void setCatalogVersions(Collection<CatalogVersionModel> catalogVersions)
    {
        this.catalogVersions = catalogVersions;
    }


    public void setCatalogVersions(CatalogVersionModel... catalogVersions)
    {
        setCatalogVersions(Arrays.asList(catalogVersions));
    }


    public UserModel getUser()
    {
        return this.user;
    }


    public void setUser(UserModel user)
    {
        this.user = user;
    }


    public Boolean isDisableSpecificDbLimitSupport()
    {
        return this.disableSpecificDbLimitSupport;
    }


    public void setDisableSpecificDbLimitSupport(Boolean disableSpecificDbLimitSupport)
    {
        this.disableSpecificDbLimitSupport = disableSpecificDbLimitSupport;
    }


    public boolean isDisableCaching()
    {
        return this.disableCaching;
    }


    public void setDisableCaching(boolean disableCaching)
    {
        this.disableCaching = disableCaching;
    }
}
