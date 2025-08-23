package de.hybris.platform.catalog.jalo.classification.unspsc;

import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.util.CSVReader;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class UNSPSCReader extends CSVReader
{
    private static final String DOUBLEZERO = "00";
    private final boolean skipRoot;
    private final String code;


    public UNSPSCReader(Media media, String encoding, boolean skipRootClasses) throws UnsupportedEncodingException, JaloBusinessException
    {
        super(media.getDataFromStream(), encoding);
        this.skipRoot = skipRootClasses;
        this.code = media.getCode();
    }


    public String toString()
    {
        return "UNSPSCReader(" + this.code + ")";
    }


    protected Map parseLine(String line)
    {
        StringBuilder superategory = new StringBuilder();
        Map<Integer, String> values = super.parseLine(line);
        String code = (String)values.get(Integer.valueOf(0));
        if(!code.substring(6, 8).equals("00"))
        {
            superategory.append(code.substring(0, 6)).append("00");
        }
        else if(!code.substring(4, 6).equals("00"))
        {
            superategory.append(code.substring(0, 4)).append("00").append("00");
        }
        else if(!code.substring(2, 4).equals("00"))
        {
            superategory.append(code.substring(0, 2)).append("00").append("00").append("00");
        }
        else if(this.skipRoot)
        {
            return null;
        }
        values.put(Integer.valueOf(3), superategory.toString());
        return values;
    }
}
