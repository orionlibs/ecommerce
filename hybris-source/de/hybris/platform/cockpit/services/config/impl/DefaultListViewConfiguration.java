package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.listview.DynamicColumnProvider;
import de.hybris.platform.cockpit.services.config.ColumnGroupConfiguration;
import de.hybris.platform.cockpit.services.config.ListViewConfiguration;
import de.hybris.platform.core.Registry;
import java.util.ArrayList;
import java.util.List;

public class DefaultListViewConfiguration extends AbstractUIComponentConfiguration implements ListViewConfiguration
{
    private ColumnGroupConfiguration rootColConfGroup = null;
    private List<String> dynamicColumnProvidersSpringBeans;
    private boolean allowCreateInlineItems;
    private boolean showVariantAttrs;
    private String headerPopupBean;


    public boolean isShowVariantAttrs()
    {
        return this.showVariantAttrs;
    }


    public void setShowVariantAttrs(boolean showVariantAttrs)
    {
        this.showVariantAttrs = showVariantAttrs;
    }


    public DefaultListViewConfiguration()
    {
    }


    public DefaultListViewConfiguration(ColumnGroupConfiguration rootColConfGroup)
    {
        this.rootColConfGroup = rootColConfGroup;
    }


    public DefaultListViewConfiguration(ColumnGroupConfiguration rootColConfGroup, boolean allowCreateInlineItems)
    {
        this.rootColConfGroup = rootColConfGroup;
        this.allowCreateInlineItems = allowCreateInlineItems;
    }


    public ColumnGroupConfiguration getRootColumnGroupConfiguration()
    {
        return this.rootColConfGroup;
    }


    public boolean isAllowCreateInlineItems()
    {
        return this.allowCreateInlineItems;
    }


    public void setAllowCreateInlineItems(boolean allowCreateInlineItems)
    {
        this.allowCreateInlineItems = allowCreateInlineItems;
    }


    public void setRootColumnConfigurationGroup(ColumnGroupConfiguration rootGroup)
    {
        this.rootColConfGroup = rootGroup;
    }


    public String getHeaderPopupBean()
    {
        return this.headerPopupBean;
    }


    public void setHeaderPopupBean(String headerPopupBean)
    {
        this.headerPopupBean = headerPopupBean;
    }


    public List<DynamicColumnProvider> getDynamicColumnProviders()
    {
        List<DynamicColumnProvider> providers = new ArrayList<>();
        if(this.dynamicColumnProvidersSpringBeans != null)
        {
            for(String springBean : this.dynamicColumnProvidersSpringBeans)
            {
                DynamicColumnProvider bean = instantiateDynamicColumnProvider(springBean);
                providers.add(bean);
            }
        }
        return providers;
    }


    protected DynamicColumnProvider instantiateDynamicColumnProvider(String springBean)
    {
        return (DynamicColumnProvider)Registry.getApplicationContext().getBean(springBean);
    }


    public List<String> getDynamicColumnProvidersSpringBeans()
    {
        return this.dynamicColumnProvidersSpringBeans;
    }


    public void setDynamicColumnProvidersSpringBeans(List<String> dynamicColumnProvidersSpringBeans)
    {
        this.dynamicColumnProvidersSpringBeans = dynamicColumnProvidersSpringBeans;
    }
}
