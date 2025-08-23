package de.hybris.platform.hac.data.form;

import de.hybris.platform.core.model.user.UserModel;
import java.util.Locale;

public class FlexSearchFormData
{
    private String flexibleSearchQuery;
    private String sqlQuery;
    private UserModel user;
    private Locale locale;
    private Integer maxCount;
    private boolean commit;
    private String dataSource;


    public String getFlexibleSearchQuery()
    {
        return this.flexibleSearchQuery;
    }


    public Locale getLocale()
    {
        return this.locale;
    }


    public Integer getMaxCount()
    {
        return this.maxCount;
    }


    public String getSqlQuery()
    {
        return this.sqlQuery;
    }


    public UserModel getUser()
    {
        return this.user;
    }


    public boolean isCommit()
    {
        return this.commit;
    }


    public String getDataSource()
    {
        return this.dataSource;
    }


    public void setFlexibleSearchQuery(String flexibleSearchQuery)
    {
        this.flexibleSearchQuery = flexibleSearchQuery;
    }


    public void setLocale(Locale locale)
    {
        this.locale = locale;
    }


    public void setMaxCount(Integer maxCount)
    {
        this.maxCount = maxCount;
    }


    public void setSqlQuery(String sqlQuery)
    {
        this.sqlQuery = sqlQuery;
    }


    public void setUser(UserModel user)
    {
        this.user = user;
    }


    public void setCommit(boolean commit)
    {
        this.commit = commit;
    }


    public void setDataSource(String dataSource)
    {
        this.dataSource = dataSource;
    }
}
