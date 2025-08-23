package de.hybris.platform.campaigns.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.cms2.model.restrictions.CMSCampaignRestrictionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengineservices.model.SourceRuleModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

public class CampaignModel extends ItemModel
{
    public static final String _TYPECODE = "Campaign";
    public static final String _CAMPAIGNSFORRESTRICTION = "CampaignsForRestriction";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String STARTDATE = "startDate";
    public static final String ENDDATE = "endDate";
    public static final String ENABLED = "enabled";
    public static final String RESTRICTIONS = "restrictions";
    public static final String SOURCERULES = "sourceRules";


    public CampaignModel()
    {
    }


    public CampaignModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CampaignModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CampaignModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return getDescription(null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("description", loc);
    }


    @Accessor(qualifier = "enabled", type = Accessor.Type.GETTER)
    public Boolean getEnabled()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("enabled");
    }


    @Accessor(qualifier = "endDate", type = Accessor.Type.GETTER)
    public Date getEndDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("endDate");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.GETTER)
    public Collection<CMSCampaignRestrictionModel> getRestrictions()
    {
        return (Collection<CMSCampaignRestrictionModel>)getPersistenceContext().getPropertyValue("restrictions");
    }


    @Accessor(qualifier = "sourceRules", type = Accessor.Type.GETTER)
    public Set<SourceRuleModel> getSourceRules()
    {
        return (Set<SourceRuleModel>)getPersistenceContext().getPropertyValue("sourceRules");
    }


    @Accessor(qualifier = "startDate", type = Accessor.Type.GETTER)
    public Date getStartDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("startDate");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        setDescription(value, null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("description", loc, value);
    }


    @Accessor(qualifier = "enabled", type = Accessor.Type.SETTER)
    public void setEnabled(Boolean value)
    {
        getPersistenceContext().setPropertyValue("enabled", value);
    }


    @Accessor(qualifier = "endDate", type = Accessor.Type.SETTER)
    public void setEndDate(Date value)
    {
        getPersistenceContext().setPropertyValue("endDate", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.SETTER)
    public void setRestrictions(Collection<CMSCampaignRestrictionModel> value)
    {
        getPersistenceContext().setPropertyValue("restrictions", value);
    }


    @Accessor(qualifier = "sourceRules", type = Accessor.Type.SETTER)
    public void setSourceRules(Set<SourceRuleModel> value)
    {
        getPersistenceContext().setPropertyValue("sourceRules", value);
    }


    @Accessor(qualifier = "startDate", type = Accessor.Type.SETTER)
    public void setStartDate(Date value)
    {
        getPersistenceContext().setPropertyValue("startDate", value);
    }
}
