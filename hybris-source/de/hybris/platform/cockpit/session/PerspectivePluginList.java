package de.hybris.platform.cockpit.session;

import java.util.Collections;
import java.util.List;

public class PerspectivePluginList
{
    private List<UICockpitPerspective> additionalPerspectives;


    public List<UICockpitPerspective> getAdditionalPerspectives()
    {
        return (this.additionalPerspectives == null) ? Collections.EMPTY_LIST : this.additionalPerspectives;
    }


    public void setAdditionalPerspectives(List<UICockpitPerspective> additionalPerspectives)
    {
        this.additionalPerspectives = additionalPerspectives;
    }
}
