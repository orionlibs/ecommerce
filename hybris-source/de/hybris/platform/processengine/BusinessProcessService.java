package de.hybris.platform.processengine;

import de.hybris.platform.processengine.model.BusinessProcessModel;
import java.util.Date;
import java.util.Map;

public interface BusinessProcessService
{
    <T extends BusinessProcessModel> T startProcess(String paramString1, String paramString2);


    <T extends BusinessProcessModel> T startProcess(String paramString1, String paramString2, Map<String, Object> paramMap);


    void startProcess(BusinessProcessModel paramBusinessProcessModel);


    <T extends BusinessProcessModel> T getProcess(String paramString);


    @Deprecated(since = "ages", forRemoval = true)
    void triggerEvent(BusinessProcessModel paramBusinessProcessModel, String paramString);


    void triggerEvent(String paramString);


    default boolean triggerEvent(BusinessProcessEvent event)
    {
        throw new IllegalStateException("method should be implemented in subclass");
    }


    void triggerEvent(String paramString, Date paramDate);


    <T extends BusinessProcessModel> T createProcess(String paramString1, String paramString2, Map<String, Object> paramMap);


    <T extends BusinessProcessModel> T createProcess(String paramString1, String paramString2);


    void restartProcess(BusinessProcessModel paramBusinessProcessModel, String paramString);
}
