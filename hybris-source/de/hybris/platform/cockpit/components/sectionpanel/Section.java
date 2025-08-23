package de.hybris.platform.cockpit.components.sectionpanel;

public interface Section
{
    String getLabel();


    String getLocalizedLabel();


    boolean isVisible();


    boolean isTabbed();


    boolean isInitialOpen();


    boolean isOpen();


    void setOpen(boolean paramBoolean);
}
