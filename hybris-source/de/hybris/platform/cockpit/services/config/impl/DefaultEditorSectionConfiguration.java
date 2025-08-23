package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.xmlprovider.XmlDataProvider;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class DefaultEditorSectionConfiguration implements EditorSectionConfiguration, Cloneable
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultEditorSectionConfiguration.class);
    private Map<LanguageModel, String> allLabels = new HashMap<>();
    private String qualifier;
    private List<EditorRowConfiguration> sectionRows = new ArrayList<>();
    private boolean tabbed = false;
    private boolean visible = true;
    private boolean initiallyOpened = true;
    private boolean showIfEmptyVar = true;
    private boolean showInCreateModeVar = true;
    private int position;
    private boolean printable = true;
    private XmlDataProvider xmlDataProvider;


    public DefaultEditorSectionConfiguration(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public Map<LanguageModel, String> getAllLabel()
    {
        return this.allLabels;
    }


    public String getLabel()
    {
        return getLabel(UISessionUtils.getCurrentSession().getLanguageIso());
    }


    public String getLabel(String iso)
    {
        String ret = null;
        for(Map.Entry<LanguageModel, String> e : this.allLabels.entrySet())
        {
            if(((LanguageModel)e.getKey()).getIsocode().equals(iso))
            {
                ret = e.getValue();
                break;
            }
        }
        return ret;
    }


    public void setAllLabel(Map<LanguageModel, String> allLabel)
    {
        this.allLabels = allLabel;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public List<EditorRowConfiguration> getSectionRows()
    {
        return new ArrayList<>(this.sectionRows);
    }


    public boolean isTabbed()
    {
        return this.tabbed;
    }


    public boolean isVisible()
    {
        return this.visible;
    }


    public boolean isInitiallyOpened()
    {
        return this.initiallyOpened;
    }


    public boolean showIfEmpty()
    {
        return this.showIfEmptyVar;
    }


    public boolean showInCreateMode()
    {
        return this.showInCreateModeVar;
    }


    public void setSectionRows(List<EditorRowConfiguration> sectionRows)
    {
        this.sectionRows = sectionRows;
    }


    public void setQualifier(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public void setTabbed(boolean tabbed)
    {
        this.tabbed = tabbed;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    public void setInitiallyOpened(boolean initiallyOpened)
    {
        this.initiallyOpened = initiallyOpened;
    }


    public void setShowIfEmpty(boolean showIfEmpty)
    {
        this.showIfEmptyVar = showIfEmpty;
    }


    public void setShowInCreateMode(boolean showInCreateMode)
    {
        this.showInCreateModeVar = showInCreateMode;
    }


    public int getPosition()
    {
        return this.position;
    }


    public void setPosition(int position)
    {
        this.position = position;
    }


    protected void copyValues(DefaultEditorSectionConfiguration fromConfiguration, DefaultEditorSectionConfiguration toConfiguration)
    {
        toConfiguration.allLabels = fromConfiguration.allLabels;
        toConfiguration.qualifier = fromConfiguration.qualifier;
        toConfiguration.sectionRows = new ArrayList<>(fromConfiguration.sectionRows);
        toConfiguration.tabbed = fromConfiguration.tabbed;
        toConfiguration.visible = fromConfiguration.visible;
        toConfiguration.initiallyOpened = fromConfiguration.initiallyOpened;
        toConfiguration.showIfEmptyVar = fromConfiguration.showIfEmptyVar;
        toConfiguration.showInCreateModeVar = fromConfiguration.showInCreateModeVar;
        toConfiguration.position = fromConfiguration.position;
    }


    public DefaultEditorSectionConfiguration clone() throws CloneNotSupportedException
    {
        DefaultEditorSectionConfiguration ret = (DefaultEditorSectionConfiguration)super.clone();
        copyValues(this, ret);
        return ret;
    }


    public boolean isPrintable()
    {
        return this.printable;
    }


    public void setPrintable(boolean value)
    {
        this.printable = value;
    }


    public XmlDataProvider getXmlDataProvider()
    {
        if(this.xmlDataProvider == null)
        {
            setXmlDataProvider("xmlDataProvider");
        }
        return this.xmlDataProvider;
    }


    public void setXmlDataProvider(String xmlDataProvider)
    {
        try
        {
            this.xmlDataProvider = (XmlDataProvider)Registry.getApplicationContext().getBean(xmlDataProvider);
        }
        catch(NoSuchBeanDefinitionException e)
        {
            LOG.error("Couldn't find bean: " + xmlDataProvider + ", section " + getLabel("en") + " will not have xml representation.");
        }
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


    public void setXmlDataProvider(XmlDataProvider xmlDataProvider)
    {
        this.xmlDataProvider = xmlDataProvider;
    }


    public DefaultEditorSectionConfiguration()
    {
    }
}
