package de.hybris.platform.webservicescommons.mapping;

public interface FieldSetLevelHelper
{
    public static final String BASIC_LEVEL = "BASIC";
    public static final String FULL_LEVEL = "FULL";
    public static final String DEFAULT_LEVEL = "DEFAULT";


    boolean isLevelName(String paramString, Class paramClass);


    String createBasicLevelDefinition(Class paramClass);


    String createDefaultLevelDefinition(Class paramClass);


    String createFullLevelDefinition(Class paramClass);


    String getLevelDefinitionForClass(Class paramClass, String paramString);
}
