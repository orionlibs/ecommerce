package de.hybris.platform.cockpit.daos.impl;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.cockpit.daos.ReportsDAO;
import de.hybris.platform.cockpit.model.WidgetPreferencesModel;
import de.hybris.platform.cockpit.reports.jalo.JasperWidgetPreferences;
import de.hybris.platform.cockpit.reports.model.JasperMediaModel;
import de.hybris.platform.cockpit.reports.model.JasperWidgetPreferencesModel;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.internal.i18n.LocalizationService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultReportsDAO implements ReportsDAO
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultReportsDAO.class);
    private FlexibleSearchService flexibleSearchService;
    private LocalizationService localeService;


    public JasperMediaModel getMainReport(String prefTitle)
    {
        WidgetPreferencesModel prefModel = getWidgetPreferences(prefTitle);
        if(prefModel == null)
        {
            return null;
        }
        if(prefModel instanceof JasperWidgetPreferencesModel)
        {
            return ((JasperWidgetPreferencesModel)prefModel).getReport();
        }
        return null;
    }


    public WidgetPreferencesModel getWidgetPreferences(String prefTitle)
    {
        Map<String, String> params = new HashMap<>();
        params.put("title", prefTitle);
        String code = TypeManager.getInstance().getComposedType(JasperWidgetPreferences.class).getCode();
        String query = "SELECT {pk} FROM {" + code + " as jwp} WHERE" + getWhereClause();
        List<Object> ret = getFlexibleSearchService().search(query, params).getResult();
        if(ret.isEmpty())
        {
            LOG.warn("No " + GeneratedCockpitConstants.TC.WIDGETPREFERENCES + " with title: " + prefTitle + " was found.");
            return null;
        }
        return (WidgetPreferencesModel)ret.get(0);
    }


    public List<JasperMediaModel> findJasperMediasByMediaFolder(String folderName)
    {
        SearchResult<JasperMediaModel> jasperMedias = this.flexibleSearchService.search("SELECT {jm:PK} FROM {JasperMedia as jm JOIN MediaFolder as mf ON {jm:folder}={mf:pk}} WHERE {mf:qualifier} = ?folderName",
                        Collections.singletonMap("folderName", folderName));
        return jasperMedias.getResult();
    }


    private String getWhereClause()
    {
        Set<Locale> locales = getLocaleService().getSupportedDataLocales();
        StringWriter where = new StringWriter();
        for(Iterator<Locale> it = locales.iterator(); it.hasNext(); )
        {
            Locale locale = it.next();
            where.append(" {jwp:title[" + locale.getLanguage() + "]:o} LIKE ?title");
            if(it.hasNext())
            {
                where.append(" OR");
            }
        }
        return where.toString();
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public LocalizationService getLocaleService()
    {
        return this.localeService;
    }


    public void setLocaleService(LocalizationService localeService)
    {
        this.localeService = localeService;
    }
}
