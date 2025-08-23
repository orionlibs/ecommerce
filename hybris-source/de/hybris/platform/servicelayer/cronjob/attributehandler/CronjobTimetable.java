package de.hybris.platform.servicelayer.cronjob.attributehandler;

import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.servicelayer.model.attribute.DynamicAttributeHandler;
import de.hybris.platform.util.localization.Localization;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;

public class CronjobTimetable implements DynamicAttributeHandler<String, CronJobModel>
{
    public String get(CronJobModel model)
    {
        Collection<TriggerModel> triggers = model.getTriggers();
        if(CollectionUtils.isEmpty(triggers))
        {
            return localize("cronjob.timetable.notscheduled");
        }
        if(triggers.size() == 1)
        {
            return ((TriggerModel)triggers.iterator().next()).getTimeTable();
        }
        return localize("cronjob.timetable.severalexectimes");
    }


    private String localize(String key)
    {
        return Localization.getLocalizedString(key);
    }


    public void set(CronJobModel model, String value)
    {
        throw new UnsupportedOperationException("Timetable attribute of the Cronjob model is read-only...");
    }
}
