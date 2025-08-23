package de.hybris.platform.hac.facade;

import de.hybris.platform.core.PK;
import de.hybris.platform.hac.data.dto.PkData;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import java.util.Date;
import org.apache.log4j.Logger;

public class HacPkAnalyzerFacade
{
    private static final Logger LOG = Logger.getLogger(HacPkAnalyzerFacade.class);
    private static final char[] DIGITS = new char[] {
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                    'u', 'v', 'w', 'x', 'y', 'z'};


    public PkData parsePkString(String pkString)
    {
        PkData pkData = new PkData();
        pkData.setPkString(pkString);
        try
        {
            PK pk = PK.parse(pkString);
            pkData.setBits(buildBits(pk.getLongValue(), 1));
            pkData.setCounterBased(pk.isCounterBased());
            pkData.setPkAsHex(Long.toHexString(pk.getLongValue()));
            pkData.setPkAsHex(Long.toHexString(pk.getLongValue()));
            pkData.setPkAsBinary(Long.toBinaryString(pk.getLongValue()));
            pkData.setPkTypeCode(pk.getTypeCode());
            pkData.setPkCreationTime(pk.getCreationTime());
            pkData.setPkCreationDate((new Date(pk.getCreationTime())).toString());
            pkData.setPkMilliCnt(pk.getMilliCnt());
            pkData.setPkClusterId(pk.getClusterID());
            pkData.setPkComposedTypeCode(getCoposedTypeCodeForPk(pk));
        }
        catch(Exception e)
        {
            String errorMsg = "Provided PK string is not valid";
            LOG.error("Provided PK string is not valid");
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Provided PK string is not valid", e);
            }
            pkData.setPossibleException(e);
        }
        return pkData;
    }


    public String[] getBitHeaders()
    {
        String[] result = new String[64];
        for(int pp = 63, index = 0; pp >= 0; pp--, index++)
        {
            result[index] = (pp >= 10) ? String.valueOf(pp) : ("0" + pp);
        }
        return result;
    }


    private String[] buildBits(long value, int shift)
    {
        char[] buffer = buildBuffer(value, shift);
        String[] result = new String[64];
        for(int pp = 0; pp <= 63; pp++)
        {
            result[pp] = String.valueOf(buffer[pp]);
        }
        return result;
    }


    private char[] buildBuffer(long value, int shift)
    {
        char[] buf = new char[64];
        int charPos = 64;
        int radix = 1 << shift;
        long mask = (radix - 1);
        do
        {
            buf[--charPos] = DIGITS[(int)(value & mask)];
            value >>>= shift;
        }
        while(charPos != 0);
        return buf;
    }


    private String getCoposedTypeCodeForPk(PK pk) throws EJBItemNotFoundException
    {
        ComposedType rootComposedType = TypeManager.getInstance().getRootComposedType(pk.getTypeCode());
        if(rootComposedType != null)
        {
            return rootComposedType.getCode();
        }
        return null;
    }
}
