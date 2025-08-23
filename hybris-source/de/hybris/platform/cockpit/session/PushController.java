package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.components.PushComponent;
import java.util.Map;

public interface PushController
{
    void update();


    boolean isUpdateNeeded();


    void setDone();


    void setComponent(PushComponent paramPushComponent);


    PushComponent getComponent();


    void setUpdateInterval(int paramInt);


    int getUpdateInterval();


    void startController();


    void setParameters(Map<String, Object> paramMap);


    Map<String, Object> getParameters();
}
