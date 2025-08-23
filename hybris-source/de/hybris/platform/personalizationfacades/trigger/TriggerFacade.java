package de.hybris.platform.personalizationfacades.trigger;

import de.hybris.platform.personalizationfacades.data.TriggerData;
import java.util.List;

public interface TriggerFacade
{
    TriggerData getTrigger(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);


    List<TriggerData> getTriggers(String paramString1, String paramString2, String paramString3, String paramString4);


    TriggerData createTrigger(String paramString1, String paramString2, TriggerData paramTriggerData, String paramString3, String paramString4);


    TriggerData updateTrigger(String paramString1, String paramString2, String paramString3, TriggerData paramTriggerData, String paramString4, String paramString5);


    void deleteTrigger(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);
}
