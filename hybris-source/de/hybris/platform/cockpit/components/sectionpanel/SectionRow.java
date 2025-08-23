package de.hybris.platform.cockpit.components.sectionpanel;

public interface SectionRow
{
    String getLabel();


    String getValueInfo();


    boolean isVisible();


    boolean isEditable();


    void setFocusListener(FocusListener paramFocusListener, String paramString);


    FocusListener getFocusListener(String paramString);
}
