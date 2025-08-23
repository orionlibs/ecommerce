package de.hybris.platform.util.config;

import java.util.Map;

public interface ConfigIntf
{
    Map<String, String> getAllParameters();


    String getParameter(String paramString);


    Map<String, String> getParametersMatching(String paramString);


    Map<String, String> getParametersMatching(String paramString, boolean paramBoolean);


    String setParameter(String paramString1, String paramString2);


    String removeParameter(String paramString);


    int getInt(String paramString, int paramInt) throws NumberFormatException;


    long getLong(String paramString, long paramLong) throws NumberFormatException;


    double getDouble(String paramString, double paramDouble) throws NumberFormatException;


    boolean getBoolean(String paramString, boolean paramBoolean);


    String getString(String paramString1, String paramString2);


    char getChar(String paramString, char paramChar) throws IndexOutOfBoundsException;


    void registerConfigChangeListener(ConfigChangeListener paramConfigChangeListener);


    void unregisterConfigChangeListener(ConfigChangeListener paramConfigChangeListener);


    void clearCache();
}
