package de.hybris.platform.impex.jalo.util;

public class Character
{
    public static final char MIN_HIGH_SURROGATE = '?';
    public static final int MIN_SUPPLEMENTARY_CODE_POINT = 65536;
    public static final int MAX_CODE_POINT = 1114111;
    public static final char MAX_HIGH_SURROGATE = '?';
    public static final char MIN_LOW_SURROGATE = '?';


    public static char[] toChars(int codePoint)
    {
        if(codePoint < 0 || codePoint > 1114111)
        {
            throw new IllegalArgumentException();
        }
        if(codePoint < 65536)
        {
            return new char[] {(char)codePoint};
        }
        char[] result = new char[2];
        toSurrogates(codePoint, result, 0);
        return result;
    }


    private static void toSurrogates(int codePoint, char[] dst, int index)
    {
        int offset = codePoint - 65536;
        dst[index + 1] = (char)((offset & 0x3FF) + 56320);
        dst[index] = (char)((offset >>> 10) + 55296);
    }
}
