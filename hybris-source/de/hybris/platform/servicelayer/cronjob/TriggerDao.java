package de.hybris.platform.servicelayer.cronjob;

import de.hybris.platform.cronjob.model.TriggerModel;
import java.util.Calendar;
import java.util.List;

public interface TriggerDao
{
    List<TriggerModel> findActiveTriggers(Calendar paramCalendar);
}
