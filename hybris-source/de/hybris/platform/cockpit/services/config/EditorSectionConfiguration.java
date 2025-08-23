package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.services.xmlprovider.XmlDataProvider;
import de.hybris.platform.core.model.c2l.LanguageModel;
import java.util.List;
import java.util.Map;

public interface EditorSectionConfiguration
{
    String getQualifier();


    void setQualifier(String paramString);


    String getLabel();


    String getLabel(String paramString);


    Map<LanguageModel, String> getAllLabel();


    void setAllLabel(Map<LanguageModel, String> paramMap);


    boolean isVisible();


    void setVisible(boolean paramBoolean);


    boolean isTabbed();


    boolean isInitiallyOpened();


    void setInitiallyOpened(boolean paramBoolean);


    boolean showIfEmpty();


    void setShowIfEmpty(boolean paramBoolean);


    boolean showInCreateMode();


    int getPosition();


    void setPosition(int paramInt);


    List<EditorRowConfiguration> getSectionRows();


    void setSectionRows(List<EditorRowConfiguration> paramList);


    EditorSectionConfiguration clone() throws CloneNotSupportedException;


    boolean isPrintable();


    void setPrintable(boolean paramBoolean);


    XmlDataProvider getXmlDataProvider();


    void setXmlDataProvider(String paramString);
}
