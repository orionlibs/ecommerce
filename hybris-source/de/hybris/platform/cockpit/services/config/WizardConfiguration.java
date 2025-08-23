package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.services.config.impl.WizardConfigurationFactory;
import de.hybris.platform.cockpit.services.config.jaxb.wizard.AfterDoneWizardScript;
import java.util.List;
import java.util.Map;

public interface WizardConfiguration extends UIComponentConfiguration
{
    Map<String, String> getQualifiers(boolean paramBoolean);


    Map<String, String> getQualifiers();


    boolean isShowPrefilledValues();


    boolean isSelectMode();


    boolean isActivateAfterCreate();


    boolean isCreateMode();


    boolean isDisplaySubtypes();


    boolean isCreateWithinEditorArea();


    boolean isCreateWithinPopupEditorArea();


    boolean isValidationInfoIgnored();


    Map<String, String> getParameterMap(String paramString);


    Map<WizardConfigurationFactory.WizardGroupConfiguration, List<String>> getGroups();


    Map<String, String> getUnboundProperties();


    AfterDoneWizardScript getWizardScript();
}
