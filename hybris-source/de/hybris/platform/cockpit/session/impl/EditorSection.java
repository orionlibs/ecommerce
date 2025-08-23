package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.sectionpanel.DefaultSection;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.config.impl.DefaultEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.validation.CockpitValidationService;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.Map;
import org.zkoss.spring.SpringUtil;

public class EditorSection extends DefaultSection
{
    private final EditorSectionConfiguration secConf;
    private CockpitValidationService validationService;


    public EditorSection(EditorSectionConfiguration secConf)
    {
        super(secConf.getQualifier());
        this.secConf = secConf;
        setVisible(secConf.isVisible());
        setTabbed(secConf.isTabbed());
        setInitialOpen(secConf.isInitiallyOpened());
        String lable = (secConf instanceof DefaultEditorSectionConfiguration) ? ((DefaultEditorSectionConfiguration)secConf).getLabelWithFallback() : secConf.getLabel();
        if(lable == null)
        {
            for(Map.Entry<LanguageModel, String> entry : (Iterable<Map.Entry<LanguageModel, String>>)secConf.getAllLabel().entrySet())
            {
                if(entry.getValue() != null)
                {
                    lable = entry.getValue();
                    break;
                }
            }
        }
        setLabel((lable != null) ? lable : ("<" + secConf.getQualifier() + ">"));
    }


    public EditorSectionConfiguration getSectionConfiguration()
    {
        return this.secConf;
    }


    protected CockpitValidationService getValidationService()
    {
        if(this.validationService == null)
        {
            this.validationService = (CockpitValidationService)SpringUtil.getBean("cockpitValidationService");
        }
        return this.validationService;
    }
}
