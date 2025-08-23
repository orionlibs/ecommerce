package de.hybris.platform.impex.jalo.exp;

import de.hybris.platform.impex.jalo.exp.generator.ScriptModifier;

public interface ScriptGenerator
{
    String generateScript();


    void registerScriptModifier(ScriptModifier paramScriptModifier);


    void addIgnoreType(String paramString);


    void addIgnoreColumn(String paramString1, String paramString2);


    void addSpecialColumn(String paramString1, String paramString2);


    void addAdditionalColumn(String paramString1, String paramString2);


    void addAdditionalModifier(String paramString1, String paramString2, String paramString3, String paramString4);


    void addReplacedColumnExpression(String paramString1, String paramString2, String paramString3);


    boolean isIncludeSystemTypes();
}
