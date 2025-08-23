package de.hybris.platform.cockpit.model.advancedsearch;

public interface SearchField
{
    String getName();


    String getLabel();


    boolean isVisible();


    boolean isSortable();


    SearchFieldGroup getGroup();
}
