package de.hybris.platform.mediaconversion.os.config;

final class StringDistance
{
    static int computeDistance(String source, String target)
    {
        return levenshtein(source, target) - countSimilar(source, target);
    }


    static int countSimilar(CharSequence source, CharSequence target)
    {
        int max = 0;
        for(int offset = 0; offset < source.length() + target.length() - 1; offset++)
        {
            int count = innerCountSimilar(source
                            .subSequence(Math.max(0, source.length() - offset - 1), source.length()), target
                            .subSequence(Math.max(0, offset - source.length() + 1), target.length()));
            if(max < count)
            {
                max = count;
            }
        }
        return max;
    }


    private static int innerCountSimilar(CharSequence source, CharSequence target)
    {
        int max = 0;
        int count = 0;
        for(int i = 0; i < source.length() && i < target.length(); i++)
        {
            if(source.charAt(i) == target.charAt(i))
            {
                count++;
            }
            else
            {
                if(count > max)
                {
                    max = count;
                }
                count = 0;
            }
        }
        return (count > max) ? count : max;
    }


    static int levenshtein(CharSequence source, CharSequence target)
    {
        if(source.length() == 0)
        {
            return target.length();
        }
        if(source.length() > target.length())
        {
            return levenshtein(target, source);
        }
        int[][] matrix = new int[source.length() + 1][target.length() + 1];
        for(int k = 0; k < matrix.length; k++)
        {
            matrix[k][0] = k;
        }
        for(int j = 0; j < (matrix[0]).length; j++)
        {
            matrix[0][j] = j;
        }
        for(int i = 1; i < matrix.length; i++)
        {
            for(int m = 1; m < (matrix[0]).length; m++)
            {
                if(source.charAt(i - 1) == target.charAt(m - 1))
                {
                    matrix[i][m] = matrix[i - 1][m - 1];
                }
                else
                {
                    matrix[i][m] = Math.min(matrix[i - 1][m] + 1,
                                    Math.min(matrix[i][m - 1] + 1, matrix[i - 1][m - 1] + 1));
                }
            }
        }
        return matrix[source.length()][target.length()];
    }
}
