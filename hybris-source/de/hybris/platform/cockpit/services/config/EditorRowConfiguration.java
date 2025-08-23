package de.hybris.platform.cockpit.services.config;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.PropertyEditorDescriptor;
import de.hybris.platform.cockpit.services.xmlprovider.XmlDataProvider;
import java.util.Map;

public interface EditorRowConfiguration
{
    boolean isVisible();


    void setVisible(boolean paramBoolean);


    boolean isEditable();


    void setEditable(boolean paramBoolean);


    String getEditor();


    void setEditor(String paramString);


    PropertyDescriptor getPropertyDescriptor();


    PropertyEditorDescriptor getEditorDescriptor();


    void setEditorDescriptor(PropertyEditorDescriptor paramPropertyEditorDescriptor);


    Map<String, String> getParameters();


    String getParameter(String paramString);


    void setParameter(String paramString1, String paramString2);


    void setPrintoutAs(String paramString);


    String getPrintoutAs();


    XmlDataProvider getXmlDataProvider();


    void setXmlDataProvider(String paramString);
}
