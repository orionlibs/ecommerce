package de.hybris.platform.cronjob.jalo;

import java.util.EventListener;

public interface ChangeListener extends EventListener
{
    void notify(ChangeEvent paramChangeEvent);
}
