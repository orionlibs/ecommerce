package de.hybris.platform.cmscockpit.components.zk;

import com.google.common.base.Preconditions;
import org.zkoss.zul.Label;

public class RawLabel extends Label
{
    public RawLabel()
    {
    }


    public RawLabel(String value)
    {
        super(value);
    }


    public String getEncodedText()
    {
        StringBuffer stringBuffer = null;
        int len = getValue().length();
        if(isPre() || isMultiline())
        {
            for(int j = 0; ; j = k + 1)
            {
                int k = getValue().indexOf('\n', j);
                if(k < 0)
                {
                    stringBuffer = encodeLine(stringBuffer, j, len);
                    break;
                }
                if(stringBuffer == null)
                {
                    Preconditions.checkArgument((j == 0));
                    stringBuffer = new StringBuffer(getValue().length() + 10);
                }
                stringBuffer = encodeLine(stringBuffer, j, (k > j && getValue().charAt(k - 1) == '\r') ? (k - 1) : k);
                stringBuffer.append("<br/>");
            }
        }
        else
        {
            stringBuffer = encodeLine(null, 0, len);
        }
        return (stringBuffer != null) ? stringBuffer.toString() : getValue();
    }


    protected StringBuffer encodeLine(StringBuffer stringBuffer, int beginn, int end)
    {
        boolean prews = (isPre() || isMultiline());
        int linesz = 0;
        if(getMaxlength() > 0)
        {
            int detla = end - beginn;
            if(detla > getMaxlength())
            {
                if(isHyphen())
                {
                    linesz = getMaxlength();
                }
                else if(!prews)
                {
                    Preconditions.checkArgument((beginn == 0));
                    int maxLength = getMaxlength();
                    while(maxLength > 0 && Character.isWhitespace(getValue().charAt(maxLength - 1)))
                    {
                        maxLength--;
                    }
                    return (new StringBuffer(maxLength + 3)).append(getValue().substring(0, maxLength)).append("...");
                }
            }
        }
        for(int cnt = 0, j = beginn; j < end; j++)
        {
            char character = getValue().charAt(j);
            String val = null;
            if(linesz > 0 && ++cnt > linesz && j + 1 < end)
            {
                stringBuffer = alloc(stringBuffer, j);
                if(Character.isLetterOrDigit(character) && Character.isLetterOrDigit(getValue().charAt(j + 1)))
                {
                    cnt = 0;
                    int k = stringBuffer.length();
                    while(true)
                    {
                        if(cnt < 3)
                        {
                            if(!Character.isLetterOrDigit(stringBuffer.charAt(--k)))
                            {
                                stringBuffer.insert(k + 1, "<br/>");
                                j--;
                                break;
                            }
                            cnt++;
                            continue;
                        }
                        stringBuffer.append('-').append("<br/>").append(character);
                        cnt = 1;
                        break;
                    }
                }
                else
                {
                    if(!Character.isWhitespace(character))
                    {
                        stringBuffer.append(character);
                    }
                    stringBuffer.append("<br/>");
                    cnt = 0;
                }
            }
            else
            {
                if(character == '\t')
                {
                    if(prews)
                    {
                        val = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
                    }
                }
                else if(character == ' ' || character == '\f')
                {
                    if(prews)
                    {
                        val = "&nbsp;";
                    }
                }
                else if(isMultiline())
                {
                    prews = false;
                }
                if(val != null)
                {
                    stringBuffer = alloc(stringBuffer, j).append(val);
                }
                else if(stringBuffer != null)
                {
                    stringBuffer.append(character);
                }
            }
        }
        return stringBuffer;
    }


    protected StringBuffer alloc(StringBuffer stringBuffer, int end)
    {
        if(stringBuffer == null)
        {
            stringBuffer = new StringBuffer(getValue().length() + 10);
            stringBuffer.append(getValue().substring(0, end));
        }
        return stringBuffer;
    }
}
