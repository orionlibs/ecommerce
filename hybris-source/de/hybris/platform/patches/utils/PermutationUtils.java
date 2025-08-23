package de.hybris.platform.patches.utils;

import de.hybris.platform.patches.data.ImpexHeaderOption;

public final class PermutationUtils
{
    private PermutationUtils()
    {
        throw new UnsupportedOperationException("creating instances of PermutationUtils is not allowed");
    }


    public static ImpexHeaderOption[][] permutate(ImpexHeaderOption[][] source)
    {
        if(source == null)
        {
            return null;
        }
        int sourceLength = source.length;
        int count = 1;
        for(ImpexHeaderOption[] array : source)
        {
            count *= array.length;
        }
        ImpexHeaderOption[][] target = new ImpexHeaderOption[count][sourceLength];
        for(int i = 0; i < count; i++)
        {
            int tempCounter = 1;
            for(int j = 0; j < sourceLength; j++)
            {
                target[i][j] = source[j][i / tempCounter % (source[j]).length];
                tempCounter *= (source[j]).length;
            }
        }
        return target;
    }
}
