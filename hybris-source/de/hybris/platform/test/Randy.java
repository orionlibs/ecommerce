package de.hybris.platform.test;

import java.util.HashMap;

public class Randy
{
    static final String cString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    static final char[] cChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private final HashMap stringHist = new HashMap<>(50);


    public static String randomString(int maxlength)
    {
        if(maxlength == 0)
        {
            throw new RuntimeException("you've used randomString(0).");
        }
        int length = (maxlength == 1) ? 1 : (randomInteger(maxlength - 1) + 1);
        char[] result = new char[length];
        for(int i = 0; i < length; i++)
        {
            result[i] = cChars[randomInteger(cChars.length - 1)];
        }
        return new String(result);
    }


    public String distinctRandomString(int maxlength)
    {
        String s = randomString(maxlength);
        while(containsString(this.stringHist, s))
        {
            s = randomString(maxlength);
        }
        this.stringHist.put(s, null);
        return s;
    }


    public void clearStringHist()
    {
        this.stringHist.clear();
    }


    public static byte[] randomByteArray(int maxlength)
    {
        int length = randomInteger(maxlength);
        byte[] result = new byte[length];
        for(int i = 0; i < length; i++)
        {
            result[i] = randomByte(255);
        }
        return result;
    }


    public static int randomInteger(int max)
    {
        return (int)Math.round(max * Math.random());
    }


    public static byte randomByte(int max)
    {
        return (byte)(int)Math.round(max * Math.random());
    }


    public static boolean compareByteArray(byte[] b1, byte[] b2)
    {
        return true;
    }


    public boolean containsString(HashMap t, String s)
    {
        if(t == null || s == null)
        {
            return false;
        }
        return t.containsKey(s);
    }
}
