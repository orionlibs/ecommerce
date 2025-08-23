package de.hybris.platform.cockpit.wizards.generic;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.wizards.WizardPage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class AdditionalPageMapping
{
    private Map<String, List<WizardPage>> pageMap;


    @Required
    public void setPageMap(Map<String, List<WizardPage>> pageMap)
    {
        this.pageMap = pageMap;
    }


    public Map<String, List<WizardPage>> getPageMap()
    {
        return (this.pageMap == null) ? Collections.EMPTY_MAP : this.pageMap;
    }


    public List<WizardPage> getPageList(ObjectTemplate template)
    {
        if(template == null)
        {
            return Collections.EMPTY_LIST;
        }
        List<ObjectType> allSupertypes = new ArrayList<>(TypeTools.getAllSupertypes((ObjectType)template.getBaseType()));
        allSupertypes.add(0, template);
        List<WizardPage> list = null;
        for(ObjectType type : allSupertypes)
        {
            list = getPageMap().get(type.getCode());
            if(list != null)
            {
                break;
            }
        }
        return (list == null) ? Collections.EMPTY_LIST : list;
    }
}
