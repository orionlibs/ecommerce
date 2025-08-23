package de.hybris.platform.cockpit.session;

public interface FocusablePerspectiveArea
{
    boolean isFocused();


    void setFocus(boolean paramBoolean);


    UICockpitPerspective getManagingPerspective();
}
