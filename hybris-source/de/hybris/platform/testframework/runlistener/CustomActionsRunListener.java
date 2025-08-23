package de.hybris.platform.testframework.runlistener;

import de.hybris.platform.core.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.ListableBeanFactory;

public class CustomActionsRunListener extends RunListener
{
    private static Map<Integer, CustomRunListener> orderedListeners;


    public void testRunStarted(Description description) throws Exception
    {
        if(orderedListeners == null)
        {
            initListeners();
        }
        for(CustomRunListener customRunListener : orderedListeners.values())
        {
            customRunListener.testRunStarted(description);
        }
    }


    void initListeners()
    {
        Map<String, CustomRunListener> customRunListenerBeanMap = getCustomListenerBeans();
        orderedListeners = new TreeMap<>();
        for(CustomRunListener customRunListener : customRunListenerBeanMap.values())
        {
            orderedListeners.put(Integer.valueOf(customRunListener.getPriority()), customRunListener);
        }
    }


    Map<String, CustomRunListener> getCustomListenerBeans()
    {
        return BeanFactoryUtils.beansOfTypeIncludingAncestors((ListableBeanFactory)Registry.getApplicationContext(), CustomRunListener.class);
    }


    public void testRunFinished(Result result) throws Exception
    {
        List<CustomRunListener> reversedListeners = new ArrayList<>(orderedListeners.values());
        Collections.reverse(reversedListeners);
        for(CustomRunListener customRunListener : reversedListeners)
        {
            customRunListener.testRunFinished(result);
        }
    }


    static Map<Integer, CustomRunListener> getOrderedListeners()
    {
        return orderedListeners;
    }


    static void setOrderedListeners(Map<Integer, CustomRunListener> orderedListeners)
    {
        CustomActionsRunListener.orderedListeners = orderedListeners;
    }
}
