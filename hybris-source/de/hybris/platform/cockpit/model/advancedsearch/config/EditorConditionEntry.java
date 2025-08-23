package de.hybris.platform.cockpit.model.advancedsearch.config;

import java.util.List;

public interface EditorConditionEntry
{
    public static final String TEXT_INPUT = "$TEXT";
    public static final String DATE_INPUT = "$DATE";
    public static final String DECIMAL_INPUT = "$DECIMAL";
    public static final String INT_INPUT = "$INTEGER";
    public static final String LONG_INPUT = "$LONG";
    public static final String _TYPE_INPUT = "$_TYPE";
    public static final String LABEL = "$LABEL";


    int getIndex();


    String getOperator();


    String getI3Label();


    String[] getViewComponents();


    List<String> getViewComponentsAsList();


    List<String> getValidAttributeTypesAsList();
}
