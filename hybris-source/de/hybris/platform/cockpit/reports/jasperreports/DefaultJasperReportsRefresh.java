package de.hybris.platform.cockpit.reports.jasperreports;

import de.hybris.platform.cockpit.enums.RefreshTimeOption;
import de.hybris.platform.cockpit.model.WidgetPreferencesModel;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Deprecated
public class DefaultJasperReportsRefresh implements JasperReportsRefresh
{
    private final Map<WidgetPreferencesModel, RefreshUnit> refreshUnits = new HashMap<>();
    private JasperReportsCache jasperReportsCache;


    public void startRefreshing(WidgetPreferencesModel widget, RefreshTimeOption refreshOption)
    {
        stopRefreshing(widget);
        if(refreshOption == RefreshTimeOption.NEVER)
        {
            return;
        }
        RefreshUnit unit = new RefreshUnit(this, widget, (new Date()).getTime(), delayInMilis(refreshOption));
        this.refreshUnits.put(widget, unit);
    }


    public void stopRefreshing(WidgetPreferencesModel widget)
    {
        this.refreshUnits.remove(widget);
    }


    public boolean onRefresh()
    {
        boolean isUpdateRequired = false;
        for(RefreshUnit unit : this.refreshUnits.values())
        {
            if(unit.isObsolete())
            {
                unit.updateData();
                isUpdateRequired = true;
            }
        }
        return isUpdateRequired;
    }


    private int delayInMilis(RefreshTimeOption refreshOption)
    {
        switch(null.$SwitchMap$de$hybris$platform$cockpit$enums$RefreshTimeOption[refreshOption.ordinal()])
        {
            case 1:
                return 5000;
            case 2:
                return 10000;
            case 3:
                return 15000;
            case 4:
                return 30000;
            case 5:
                return 60000;
            case 6:
                return 180000;
            case 7:
                return 300000;
            case 8:
                return 600000;
            case 9:
                return 900000;
            case 10:
                return 1800000;
        }
        throw new RuntimeException("Not implemented RefreshOption");
    }


    public void setJasperReportsCache(JasperReportsCache jasperReportsCache)
    {
        this.jasperReportsCache = jasperReportsCache;
    }
}
