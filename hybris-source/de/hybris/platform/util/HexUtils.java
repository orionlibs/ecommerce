package de.hybris.platform.util;

import java.io.ByteArrayOutputStream;

public final class HexUtils
{
    public static final int[] DEC = new int[] {
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, 0, 1,
                    2, 3, 4, 5, 6, 7, 8, 9, -1, -1,
                    -1, -1, -1, -1, -1, 10, 11, 12, 13, 14,
                    15, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, 10, 11, 12,
                    13, 14, 15, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
                    -1, -1, -1, -1, -1, -1};


    public static byte[] convert(String digits)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for(int i = 0; i < digits.length(); i += 2)
        {
            char c1 = digits.charAt(i);
            if(i + 1 >= digits.length())
            {
                throw new IllegalArgumentException("odd input");
            }
            char c2 = digits.charAt(i + 1);
            byte b = 0;
            if(c1 >= '0' && c1 <= '9')
            {
                b = (byte)(b + (c1 - 48) * 16);
            }
            else if(c1 >= 'a' && c1 <= 'f')
            {
                b = (byte)(b + (c1 - 97 + 10) * 16);
            }
            else if(c1 >= 'A' && c1 <= 'F')
            {
                b = (byte)(b + (c1 - 65 + 10) * 16);
            }
            else
            {
                throw new IllegalArgumentException("invalid input");
            }
            if(c2 >= '0' && c2 <= '9')
            {
                b = (byte)(b + c2 - 48);
            }
            else if(c2 >= 'a' && c2 <= 'f')
            {
                b = (byte)(b + c2 - 97 + 10);
            }
            else if(c2 >= 'A' && c2 <= 'F')
            {
                b = (byte)(b + c2 - 65 + 10);
            }
            else
            {
                throw new IllegalArgumentException("invalid input");
            }
            baos.write(b);
        }
        return baos.toByteArray();
    }


    public static String convert(byte[] bytes)
    {
        StringBuffer sb = new StringBuffer(bytes.length * 2);
        for(int i = 0; i < bytes.length; i++)
        {
            sb.append(convertDigit(bytes[i] >> 4));
            sb.append(convertDigit(bytes[i] & 0xF));
        }
        return sb.toString();
    }


    private static char convertDigit(int value)
    {
        value &= 0xF;
        if(value >= 10)
        {
            return (char)(value - 10 + 97);
        }
        return (char)(value + 48);
    }
}
