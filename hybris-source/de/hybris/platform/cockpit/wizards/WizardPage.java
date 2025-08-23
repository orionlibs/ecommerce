package de.hybris.platform.cockpit.wizards;

import java.util.Map;
import org.zkoss.zk.ui.Component;

public interface WizardPage
{
    String getId();


    String getTitle();


    String getComponentURI();


    String getWidth();


    String getHeight();


    WizardPageController getController();


    Map<String, Object> getAttributes();


    void initView(Wizard paramWizard, Component paramComponent);
}
