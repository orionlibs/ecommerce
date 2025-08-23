package de.hybris.platform.util.migration;

import de.hybris.platform.core.Constants;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class OldPK
{
    public static final String DELIMITER = "-";
    private static final char[] CHARS62 = new char[] {
                    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                    'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                    'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
                    'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                    'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                    'Y', 'Z'};
    private static final BigInteger CHARS62_LEN = BigInteger.valueOf(CHARS62.length);
    private static final String CHARS62_STRING = new String(CHARS62);
    private final String itemPK;
    private final long creationTime;
    private final int typecode;
    private final int random;
    private final InetAddress inet;


    public OldPK(String pk)
    {
        try
        {
            int idx = pk.lastIndexOf("-");
            if(idx < 0)
            {
                throw new IllegalArgumentException("old pk has wrong format: '" + pk + "' does not contain delimiter '-'.");
            }
            if(idx == 0)
            {
                throw new IllegalArgumentException("old pk has wrong format: '" + pk + "' does not contain a itemPK.");
            }
            if(idx + 1 == pk.length())
            {
                throw new IllegalArgumentException("old pk has wrong format: '" + pk + "' does not contain a typecode.");
            }
            this.typecode = Integer.parseInt(pk.substring(idx + 1));
        }
        catch(NumberFormatException e)
        {
            throw new IllegalArgumentException("old pk has wrong format: '" + pk + "', type code is not an integer.");
        }
        this.itemPK = pk.substring(0, pk.indexOf("-"));
        if(this.itemPK.equals(Constants.USER.ADMIN_EMPLOYEE) || this.itemPK.equals(Constants.USER.ANONYMOUS_CUSTOMER) || this.itemPK
                        .equals(Constants.USER.ADMIN_USERGROUP))
        {
            this.creationTime = 0L;
            this.random = 0;
            this.inet = null;
        }
        else
        {
            byte[] b = base62decode(this.itemPK);
            int first = byte2int(b, 4);
            int second = byte2int(b, 8);
            this.creationTime = (first << 32L) + second;
            this.random = byte2int(b, 12);
            byte[] adr = new byte[4];
            adr[0] = b[0];
            adr[1] = b[1];
            adr[2] = b[2];
            adr[3] = b[3];
            try
            {
                this.inet = InetAddress.getByAddress(adr);
            }
            catch(UnknownHostException e)
            {
                throw new IllegalArgumentException("cannot parse IP address from old pk.");
            }
        }
    }


    public long getCreationTime()
    {
        return this.creationTime;
    }


    public int getTypecode()
    {
        return this.typecode;
    }


    public int getRandom()
    {
        return this.random;
    }


    public InetAddress getInetAddress()
    {
        return this.inet;
    }


    protected static int parseTypeCode(String pk) throws IllegalArgumentException
    {
        try
        {
            int idx = pk.lastIndexOf("-");
            if(idx < 0)
            {
                throw new IllegalArgumentException("old pk has wrong format: '" + pk + "' does not contain delimiter '-'.");
            }
            if(idx + 1 == pk.length())
            {
                throw new IllegalArgumentException("old pk has wrong format: '" + pk + "' does not contain a typecode.");
            }
            return Integer.parseInt(pk.substring(idx + 1));
        }
        catch(NumberFormatException e)
        {
            throw new IllegalArgumentException("old pk has wrong format: '" + pk + "', type code is not an integer.");
        }
    }


    protected static long parseCreationTime(String pk) throws IllegalArgumentException
    {
        String itemPK = parseItemPK(pk);
        byte[] b = base62decode(itemPK);
        int first = byte2int(b, 4);
        int second = byte2int(b, 8);
        long l = (first << 32L) + second;
        return l;
    }


    private static String parseItemPK(String pk)
    {
        parseTypeCode(pk);
        return pk.substring(0, pk.indexOf('-'));
    }


    private static byte[] base62decode(String str)
    {
        BigInteger z = BigInteger.valueOf(0L);
        for(int i = str.length() - 1; i >= 0; i--)
        {
            char ch = str.charAt(i);
            z = z.add(BigInteger.valueOf(CHARS62_STRING.indexOf(ch)));
            if(i > 0)
            {
                z = z.multiply(CHARS62_LEN);
            }
        }
        byte[] ret = z.toByteArray();
        byte[] ret2 = new byte[ret.length - 1];
        System.arraycopy(ret, 1, ret2, 0, ret2.length);
        return ret2;
    }


    private static int byte2int(byte[] bytes, int offset)
    {
        long i = 0L;
        int zw = bytes[offset + 0];
        if(zw < 0)
        {
            zw = 256 + zw;
        }
        i += zw;
        i <<= 8L;
        zw = bytes[offset + 1];
        if(zw < 0)
        {
            zw = 256 + zw;
        }
        i += zw;
        i <<= 8L;
        zw = bytes[offset + 2];
        if(zw < 0)
        {
            zw = 256 + zw;
        }
        i += zw;
        i <<= 8L;
        zw = bytes[offset + 3];
        if(zw < 0)
        {
            zw = 256 + zw;
        }
        i += zw;
        return (int)i;
    }


    public String toString()
    {
        return this.itemPK + "-" + this.itemPK;
    }
}
