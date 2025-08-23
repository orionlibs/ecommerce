package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.services.config.SearchFieldConfiguration;
import de.hybris.platform.cockpit.services.config.SearchFieldGroupConfiguration;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultSearchFieldGroupConfiguration implements SearchFieldGroupConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSearchFieldGroupConfiguration.class);
    private List<SearchFieldConfiguration> fieldConfigs = new ArrayList<>();
    private List<SearchFieldGroupConfiguration> groupConfigs = new ArrayList<>();
    private String name = null;
    private boolean visible = false;
    private SearchFieldGroupConfiguration parent = null;
    private Map<LanguageModel, String> labels;


    public DefaultSearchFieldGroupConfiguration(String name)
    {
        this.name = name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    public void setSearchFieldGroupConfigurations(List<? extends SearchFieldGroupConfiguration> groupConfigs)
    {
        this.groupConfigs = (List)groupConfigs;
        if(this.groupConfigs != null)
        {
            for(SearchFieldGroupConfiguration groupConfig : this.groupConfigs)
            {
                if(groupConfig instanceof DefaultSearchFieldGroupConfiguration)
                {
                    ((DefaultSearchFieldGroupConfiguration)groupConfig).setParentSearchFieldGroupConfiguration(this);
                }
            }
        }
    }


    public void addSearchFieldConfiguration(SearchFieldConfiguration fieldConfig)
    {
        if(!getAllSearchFieldConfigurations().contains(fieldConfig))
        {
            this.fieldConfigs.add(fieldConfig);
        }
    }


    public void setSearchFieldConfigurations(List<SearchFieldConfiguration> searchFieldConfigs)
    {
        this.fieldConfigs = searchFieldConfigs;
    }


    public void setParentSearchFieldGroupConfiguration(SearchFieldGroupConfiguration parentGroup)
    {
        this.parent = parentGroup;
    }


    public List<SearchFieldConfiguration> getAllSearchFieldConfigurations()
    {
        List<SearchFieldConfiguration> allFields = new ArrayList<>();
        allFields.addAll(this.fieldConfigs);
        for(SearchFieldGroupConfiguration group : this.groupConfigs)
        {
            allFields.addAll(group.getAllSearchFieldConfigurations());
        }
        return allFields;
    }


    public List<SearchFieldGroupConfiguration> getAllSearchFieldGroupConfigurations()
    {
        List<SearchFieldGroupConfiguration> allGroups = new ArrayList<>();
        allGroups.addAll(this.groupConfigs);
        for(SearchFieldGroupConfiguration group : this.groupConfigs)
        {
            allGroups.addAll(group.getAllSearchFieldGroupConfigurations());
        }
        return allGroups;
    }


    public String getName()
    {
        return this.name;
    }


    public SearchFieldGroupConfiguration getParentSearchFieldGroupConfiguration()
    {
        return this.parent;
    }


    public List<SearchFieldConfiguration> getSearchFieldConfigurations()
    {
        return this.fieldConfigs;
    }


    public List<SearchFieldGroupConfiguration> getSearchFieldGroupConfigurations()
    {
        return this.groupConfigs;
    }


    public boolean isVisible()
    {
        return this.visible;
    }


    public int getSize()
    {
        return this.fieldConfigs.size();
    }


    public int getTotalSize()
    {
        int totalSize = getSize();
        for(SearchFieldGroupConfiguration group : this.groupConfigs)
        {
            totalSize += group.getTotalSize();
        }
        return totalSize;
    }


    public Map<LanguageModel, String> getAllLabels()
    {
        return this.labels;
    }


    public String getLabel()
    {
        return getLabel(UISessionUtils.getCurrentSession().getLanguageIso());
    }


    public String getLabel(String iso)
    {
        String ret = null;
        if(this.labels == null || this.labels.isEmpty())
        {
            ret = "<" + getName() + ">";
        }
        else
        {
            for(Map.Entry<LanguageModel, String> e : this.labels.entrySet())
            {
                if(((LanguageModel)e.getKey()).getIsocode().equals(iso))
                {
                    ret = e.getValue();
                    break;
                }
            }
        }
        return ret;
    }


    public void setAllLabels(Map<LanguageModel, String> labels)
    {
        this.labels = labels;
    }


    public String getLabelWithFallback()
    {
        return getLabelWithFallback(UISessionUtils.getCurrentSession().getLanguageIso());
    }


    public String getLabelWithFallback(String iso)
    {
        String label = getLabel(iso);
        try
        {
            if(StringUtils.isBlank(label))
            {
                I18NService i18n = (I18NService)Registry.getApplicationContext().getBean("i18nService");
                CommonI18NService commonI18N = (CommonI18NService)Registry.getApplicationContext().getBean("commonI18NService");
                for(Locale fbLoc : i18n.getFallbackLocales(commonI18N.getLocaleForLanguage(commonI18N.getLanguage(iso))))
                {
                    label = getLabel(fbLoc.getLanguage());
                    if(!StringUtils.isBlank(label))
                    {
                        break;
                    }
                }
            }
        }
        catch(Exception e)
        {
            LOG.warn(e.getMessage(), e);
        }
        if(StringUtils.isBlank(label))
        {
            label = getLabel("en");
        }
        return label;
    }
}
