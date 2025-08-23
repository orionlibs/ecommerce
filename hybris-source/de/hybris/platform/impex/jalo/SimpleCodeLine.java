package de.hybris.platform.impex.jalo;

import java.util.Map;

public class SimpleCodeLine extends AbstractCodeLine
{
    public SimpleCodeLine(Map<Integer, String> csvLine, String marker, String scriptContent, int lineNumber, String location)
    {
        super(csvLine, marker, scriptContent, lineNumber, location);
    }
}
