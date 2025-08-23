package de.hybris.platform.scripting.engine;

import java.io.Writer;
import java.util.Map;

public interface ScriptExecutable
{
    <T> T getAsInterface(Class<T> paramClass);


    <T> T getAsInterface(Class<T> paramClass, Map<String, Object> paramMap);


    ScriptExecutionResult execute();


    ScriptExecutionResult execute(Map<String, Object> paramMap);


    ScriptExecutionResult execute(Map<String, Object> paramMap, Writer paramWriter1, Writer paramWriter2);


    boolean isDisabled();
}
