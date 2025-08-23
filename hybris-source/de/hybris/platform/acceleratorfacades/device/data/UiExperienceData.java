package de.hybris.platform.acceleratorfacades.device.data;

import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import java.io.Serializable;

public class UiExperienceData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private UiExperienceLevel level;


    public void setLevel(UiExperienceLevel level)
    {
        this.level = level;
    }


    public UiExperienceLevel getLevel()
    {
        return this.level;
    }
}
