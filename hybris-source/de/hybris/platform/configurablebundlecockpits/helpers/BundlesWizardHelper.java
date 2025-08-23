package de.hybris.platform.configurablebundlecockpits.helpers;

import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.CreateContext;
import de.hybris.platform.cockpit.wizards.generic.NewItemWizard;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BundlesWizardHelper
{
    public void initializeWizard(ObjectTemplate bundleTemplateType, NewItemWizard wizard, Map<String, Object> initialValues)
    {
        wizard.setPredefinedValues(initialValues);
        CreateContext createContext = new CreateContext((ObjectType)bundleTemplateType.getBaseType(), null, null, null);
        Set<ObjectType> allowedTypesSet = new HashSet<>();
        allowedTypesSet.add(getTypeService().getBaseType("BundleTemplate"));
        createContext.setAllowedTypes(allowedTypesSet);
        wizard.setCreateContext(createContext);
        wizard.setDisplaySubTypes(true);
        wizard.setAllowSelect(false);
        wizard.setAllowCreate(true);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("multiple", "true");
        parameters.put("forceCreateInWizard", Boolean.TRUE);
        wizard.setParameters(parameters);
        wizard.start();
    }


    protected TypeService getTypeService()
    {
        return UISessionUtils.getCurrentSession().getTypeService();
    }
}
