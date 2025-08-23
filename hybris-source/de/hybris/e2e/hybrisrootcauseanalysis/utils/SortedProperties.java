package de.hybris.e2e.hybrisrootcauseanalysis.utils;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class SortedProperties extends Properties
{
    public synchronized Enumeration<Object> keys()
    {
        Enumeration<Object> keysEnum = super.keys();
        Vector<Comparable> keyList = new Vector();
        while(keysEnum.hasMoreElements())
        {
            keyList.add(keysEnum.nextElement());
        }
        Collections.sort(keyList);
        return keyList.elements();
    }
}
