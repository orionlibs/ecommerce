package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.config.ColumnGroupConfiguration;
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

public class DefaultColumnGroupConfiguration implements ColumnGroupConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultColumnGroupConfiguration.class);
    private List<ColumnConfiguration> columnConfigs = new ArrayList<>();
    private List<ColumnGroupConfiguration> groupConfigs = new ArrayList<>();
    private String name = null;
    private ColumnGroupConfiguration parent = null;
    private Map<LanguageModel, String> labels;


    public DefaultColumnGroupConfiguration(String name)
    {
        this.name = name;
    }


    public void setColumnGroupConfigurations(List<? extends ColumnGroupConfiguration> groups)
    {
        this.groupConfigs = (List)groups;
        if(this.groupConfigs != null)
        {
            for(ColumnGroupConfiguration groupConfig : this.groupConfigs)
            {
                if(groupConfig instanceof DefaultColumnGroupConfiguration)
                {
                    ((DefaultColumnGroupConfiguration)groupConfig).setParentColumnGroupConfiguration(this);
                }
            }
        }
    }


    public List<? extends ColumnGroupConfiguration> getColumnGroupConfigurations()
    {
        return this.groupConfigs;
    }


    public void addColumnConfiguration(ColumnConfiguration column)
    {
        this.columnConfigs.add(column);
    }


    public void addColumnGroupConfiguration(ColumnGroupConfiguration columnGroupConfiguration)
    {
        this.groupConfigs.add(columnGroupConfiguration);
        if(columnGroupConfiguration instanceof DefaultColumnGroupConfiguration)
        {
            ((DefaultColumnGroupConfiguration)columnGroupConfiguration).setParentColumnGroupConfiguration(this);
        }
    }


    public void setColumnConfigurations(List<? extends ColumnConfiguration> columns)
    {
        this.columnConfigs = (List)columns;
    }


    public List<? extends ColumnConfiguration> getColumnConfigurations()
    {
        return this.columnConfigs;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setParentColumnGroupConfiguration(ColumnGroupConfiguration parentGroup)
    {
        this.parent = parentGroup;
    }


    public ColumnGroupConfiguration getParentGroupConfiguration()
    {
        return this.parent;
    }


    public int getSize()
    {
        return this.columnConfigs.size();
    }


    public int getTotalSize()
    {
        int totalSize = getSize();
        for(ColumnGroupConfiguration group : this.groupConfigs)
        {
            totalSize += group.getTotalSize();
        }
        return totalSize;
    }


    public List<ColumnConfiguration> getAllColumnConfigurations()
    {
        List<ColumnConfiguration> allColumns = new ArrayList<>();
        allColumns.addAll(this.columnConfigs);
        for(ColumnGroupConfiguration group : this.groupConfigs)
        {
            allColumns.addAll(group.getAllColumnConfigurations());
        }
        return allColumns;
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
