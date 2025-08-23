package de.hybris.platform.servicelayer.impex;

import java.util.Map;

public interface ImpExError
{
    String getItemType();


    ProcessMode getMode();


    String getErrorMessage();


    Map<Integer, String> getSource();
}
