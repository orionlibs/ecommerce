package de.hybris.platform.cockpit.reports.widgets.factory.impl;

import de.hybris.platform.cockpit.enums.OneDayRange;
import de.hybris.platform.cockpit.enums.ReportTimeRange;
import de.hybris.platform.cockpit.model.WidgetParameterModel;
import de.hybris.platform.cockpit.reports.widgets.factory.JasperReportParameterFactory;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Date;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesMap;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class JasperReportParameterFactoryImpl implements JasperReportParameterFactory
{
    private static final String DEFAULTVALUE = "defaultvalue";
    private static final String TYPE_PROPERTY_KEY = "type";
    private TypeService typeService;
    private CommonI18NService commonI18NService;


    public WidgetParameterModel createParameter(JRParameter jrParameterDefinition)
    {
        WidgetParameterModel parameter = new WidgetParameterModel();
        parameter.setName(jrParameterDefinition.getName());
        parameter.setType(getType(jrParameterDefinition));
        parameter.setTargetType(jrParameterDefinition.getValueClass().getCanonicalName());
        parameter.setDescription(jrParameterDefinition.getDescription());
        parameter.setValue(getDefaultValue(jrParameterDefinition));
        return parameter;
    }


    private Object getDefaultValue(JRParameter jrParameterDefinition)
    {
        JRPropertiesMap propertiesMap = jrParameterDefinition.getPropertiesMap();
        if(propertiesMap.containsProperty("type"))
        {
            String propertyValue = propertiesMap.getProperty("type");
            if("Currency".equals(propertyValue))
            {
                return this.commonI18NService.getCurrentCurrency();
            }
            if("Country".equals(propertyValue))
            {
                return CollectionUtils.isEmpty(this.commonI18NService.getAllCountries()) ? null :
                                this.commonI18NService.getAllCountries().iterator().next();
            }
            if("ReportTimeRange".equals(propertyValue))
            {
                if(propertiesMap.containsProperty("defaultvalue"))
                {
                    return ReportTimeRange.valueOf(propertiesMap.getProperty("defaultvalue"));
                }
                return ReportTimeRange.LAST7DAYS;
            }
            if("OneDayRange".equals(propertyValue))
            {
                return OneDayRange.TODAY;
            }
        }
        else
        {
            if(jrParameterDefinition.getValueClass().equals(Date.class))
            {
                return new Date();
            }
            if(jrParameterDefinition.getValueClass().equals(Boolean.class))
            {
                return Boolean.FALSE;
            }
        }
        return null;
    }


    public boolean isSameMetaData(JRParameter jrParameterDefinition, WidgetParameterModel parameter)
    {
        boolean ret = true;
        ret = (ret && StringUtils.equals(jrParameterDefinition.getDescription(), parameter.getDescription()));
        ret = (ret && StringUtils.equals(jrParameterDefinition.getValueClass().getCanonicalName(), parameter.getTargetType()));
        ret = (ret && parameter.getType().equals(getType(jrParameterDefinition)));
        return ret;
    }


    private TypeModel getType(JRParameter jrParameterDefinition)
    {
        TypeModel typeModel = null;
        JRPropertiesMap propertiesMap = jrParameterDefinition.getPropertiesMap();
        if(propertiesMap.containsProperty("type"))
        {
            typeModel = this.typeService.getTypeForCode(propertiesMap.getProperty("type"));
        }
        else
        {
            typeModel = this.typeService.getTypeForCode(jrParameterDefinition.getValueClass().getCanonicalName());
        }
        return typeModel;
    }


    @Deprecated
    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Deprecated
    public void setI18nService(I18NService i18nService)
    {
    }


    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
