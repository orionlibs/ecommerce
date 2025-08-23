package de.hybris.platform.platformbackoffice.taxdiscountvalueparser;

public interface ValueParser<T>
{
    T parse(String paramString) throws ParserException;


    String render(T paramT);
}
