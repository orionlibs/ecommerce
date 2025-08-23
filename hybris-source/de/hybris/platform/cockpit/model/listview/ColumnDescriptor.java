package de.hybris.platform.cockpit.model.listview;

import de.hybris.platform.core.model.c2l.LanguageModel;

public interface ColumnDescriptor
{
    String getName();


    boolean isEditable();


    boolean isSelectable();


    boolean isVisible();


    boolean isSortable();


    boolean isDynamic();


    void setDynamic(boolean paramBoolean);


    LanguageModel getLanguage();
}
