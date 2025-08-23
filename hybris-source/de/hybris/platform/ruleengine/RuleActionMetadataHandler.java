package de.hybris.platform.ruleengine;

public interface RuleActionMetadataHandler<T>
{
    void handle(T paramT, String paramString);


    void undoHandle(T paramT);
}
