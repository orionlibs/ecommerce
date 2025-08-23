package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.persistence.flexiblesearch.internal.QueryInterner;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

abstract class ParsedText
{
    private String source;
    private String cleanSource;
    private final ParsedText enclosingText;
    private StringBuilder translated;
    private List<ParsedText> nestedTexts;
    private boolean haveSecondPassTexts;
    private int ownInsertPosition = -1;


    public String toString()
    {
        return "ParsedText(source='" + getSource() + "',translated='" + this.translated + "',insertPos=" + this.ownInsertPosition + ",nested=" + this.nestedTexts + ",secondPass=" + this.haveSecondPassTexts + ")";
    }


    ParsedText(ParsedText enclosing, String source)
    {
        this.source = source;
        this.enclosingText = enclosing;
    }


    protected static final int getWholeWordTokenPosition(String src, String token)
    {
        return getWholeWordTokenPosition(src, token, 0);
    }


    protected static final int getWholeWordTokenPosition(String src, String token, int startFrom)
    {
        int tokenLength = token.length();
        int lastPossiblePosition = src.length() - tokenLength;
        int pos = src.indexOf(token, startFrom);
        while(pos >= 0)
        {
            if(pos > 0 && (Character.isLetterOrDigit(src.charAt(pos - 1)) || src.charAt(pos - 1) == '_'))
            {
                if(pos + tokenLength > lastPossiblePosition)
                {
                    return -1;
                }
                pos = src.indexOf(token, pos + tokenLength);
                continue;
            }
            if(pos < lastPossiblePosition && (
                            Character.isLetterOrDigit(src.charAt(pos + tokenLength)) || src.charAt(pos + tokenLength) == '_'))
            {
                if(pos + tokenLength > lastPossiblePosition)
                {
                    return -1;
                }
                pos = src.indexOf(token, pos + tokenLength);
                continue;
            }
            return pos;
        }
        return pos;
    }


    protected void setSource(String source)
    {
        if(this.source != null)
        {
            throw new IllegalArgumentException("source is already set!!!");
        }
        this.source = source;
    }


    protected ParsedText getEnclosingText()
    {
        return this.enclosingText;
    }


    protected List<? extends ParsedText> getNestedTexts()
    {
        return (this.nestedTexts != null) ? this.nestedTexts : Collections.EMPTY_LIST;
    }


    protected boolean hasNestedTexts()
    {
        return (this.nestedTexts != null && !this.nestedTexts.isEmpty());
    }


    int getInsertPosition()
    {
        if(this.ownInsertPosition == -1)
        {
            throw new IllegalStateException("insert position of parsed text " + getClass().getName() + ":" + this + ":" +
                            System.identityHashCode(this) + " not set yet");
        }
        return this.ownInsertPosition;
    }


    void setInsertPosition(int pos)
    {
        if(pos < 0)
        {
            throw new IllegalArgumentException("insert position of " + this + " cannot be less than 0");
        }
        this.ownInsertPosition = pos;
    }


    boolean isTranslated()
    {
        return (this.translated != null && !this.haveSecondPassTexts);
    }


    String getTranslated()
    {
        if(!isTranslated())
        {
            throw new IllegalStateException("not yet translated (buffer=" +
                            getBuffer() + ",secondPass=" + this.haveSecondPassTexts + ")");
        }
        String translatedQry = (this.translated != null) ? this.translated.toString() : null;
        return QueryInterner.intern(translatedQry);
    }


    String getSource()
    {
        return this.source;
    }


    protected StringBuilder getBuffer()
    {
        return this.translated;
    }


    protected StringBuilder setBuffer(StringBuilder buffer)
    {
        return this.translated = buffer;
    }


    protected String getStartDelimiter()
    {
        return "{";
    }


    protected String getEndDelimiter()
    {
        return "}";
    }


    protected void replaceInTranslated(String old, String text)
    {
        if(!isTranslated())
        {
            throw new IllegalStateException("not yet translated (buffer=" +
                            getBuffer() + ",secondPass=" + this.haveSecondPassTexts + ")");
        }
        int pos = getBuffer().indexOf(old);
        while(pos > -1)
        {
            getBuffer().replace(pos, pos + old.length(), text);
            int offset = text.length() - 1 - old.length();
            for(Iterator<? extends ParsedText> it = getNestedTexts().iterator(); it.hasNext(); )
            {
                ParsedText parsedText = it.next();
                if(parsedText.getInsertPosition() >= pos)
                {
                    parsedText.setInsertPosition(parsedText.getInsertPosition() + offset);
                }
            }
            pos = getBuffer().indexOf(old);
        }
    }


    protected void insertIntoTranslated(int pos, String text)
    {
        if(!isTranslated())
        {
            throw new IllegalStateException("not yet translated (buffer=" +
                            getBuffer() + ",secondPass=" + this.haveSecondPassTexts + ")");
        }
        getBuffer().insert(pos, text);
        int offset = text.length();
        for(Iterator<? extends ParsedText> it = getNestedTexts().iterator(); it.hasNext(); )
        {
            ParsedText parsedText = it.next();
            if(parsedText.getInsertPosition() >= pos)
            {
                parsedText.setInsertPosition(parsedText.getInsertPosition() + offset);
            }
        }
    }


    protected void replaceInTranslated(TableField field, String text)
    {
        if(!isTranslated())
        {
            throw new IllegalStateException("not yet translated (buffer=" +
                            getBuffer() + ",secondPass=" + this.haveSecondPassTexts + ")");
        }
        int insertPos = field.getInsertPosition();
        int endPos = insertPos + field.getTranslated().length();
        getBuffer().replace(insertPos, endPos, text);
        int offset = text.length() - endPos - insertPos;
        if(offset != 0)
        {
            for(Iterator<? extends ParsedText> it = getNestedTexts().iterator(); it.hasNext(); )
            {
                ParsedText parsedText = it.next();
                if(parsedText != field && parsedText.getInsertPosition() > insertPos)
                {
                    parsedText.setInsertPosition(parsedText.getInsertPosition() + offset);
                }
            }
        }
    }


    protected void registerParsedText(ParsedText parsed)
    {
        if(this.nestedTexts == null)
        {
            this.nestedTexts = new LinkedList<>();
        }
        this.nestedTexts.add(parsed);
        this.haveSecondPassTexts |= !parsed.isTranslated() ? 1 : 0;
    }


    protected String getCleanSource()
    {
        if(this.cleanSource == null)
        {
            this
                            .cleanSource = FlexibleSearchTools.replace(this, getSource(), getStartDelimiter(), getEndDelimiter(), (FlexibleSearchTools.Translator)new Object(this)).toString();
        }
        return this.cleanSource;
    }


    protected void translate() throws FlexibleSearchException
    {
        if(getBuffer() == null)
        {
            setBuffer(FlexibleSearchTools.replace(this, getSource(), getStartDelimiter(), getEndDelimiter(), (FlexibleSearchTools.Translator)new Object(this)));
        }
        else if(this.haveSecondPassTexts)
        {
            int offset = 0;
            for(ParsedText text : getNestedTexts())
            {
                if(offset > 0)
                {
                    text.setInsertPosition(text.getInsertPosition() + offset);
                }
                if(!text.isTranslated())
                {
                    text.translate();
                    if(!text.isTranslated())
                    {
                        throw new FlexibleSearchException(null, "could not translate " + text + " in second pass", 0);
                    }
                    String result = text.getTranslated();
                    getBuffer().insert(text.getInsertPosition(), result);
                    offset += result.length();
                }
            }
            this.haveSecondPassTexts = false;
        }
    }


    protected abstract ParsedText translateNested(int paramInt, String paramString) throws FlexibleSearchException;


    public int hashCode()
    {
        return getSource().hashCode();
    }


    public boolean equals(Object object)
    {
        if(this == object)
        {
            return true;
        }
        if(object == null)
        {
            return false;
        }
        if(getClass() != object.getClass())
        {
            return false;
        }
        return getSource().equals(((ParsedText)object).getSource());
    }
}
