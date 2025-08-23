package de.hybris.bootstrap.util;

public class RegexUtil
{
    public static String wildcardToRegex(String wildcard)
    {
        StringBuffer sBuffer = new StringBuffer(wildcard.length());
        int textLength = wildcard.length();
        for(int i = 0; i < textLength; i++)
        {
            char character = wildcard.charAt(i);
            switch(character)
            {
                case '*':
                    if(i != textLength - 1 && wildcard.charAt(i + 1) == '*')
                    {
                        sBuffer.append("[^/]*.*[^/]*");
                        break;
                    }
                    sBuffer.append("[^/]*");
                    break;
                case '?':
                    sBuffer.append('.');
                    break;
                case '$':
                case '(':
                case ')':
                case '.':
                case '[':
                case '\\':
                case ']':
                case '^':
                case '{':
                case '|':
                case '}':
                    sBuffer.append('\\');
                    sBuffer.append(character);
                    break;
                default:
                    sBuffer.append(character);
                    break;
            }
        }
        return sBuffer.toString();
    }
}
