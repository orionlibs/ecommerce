package de.hybris.platform.servicelayer.cronjob;

import de.hybris.platform.cronjob.model.TriggerModel;
import java.util.Calendar;
import java.util.List;

public interface TriggerService
{
    void activate(TriggerModel paramTriggerModel);


    Calendar getNextTime(TriggerModel paramTriggerModel, Calendar paramCalendar);


    List<TriggerModel> getActiveTriggers();


    List<TriggerModel> getActiveTriggers(Calendar paramCalendar);


    int getPulseSeconds();


    void setPulseSeconds(int paramInt);
}
