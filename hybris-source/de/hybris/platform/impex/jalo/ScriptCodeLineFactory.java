package de.hybris.platform.impex.jalo;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import de.hybris.platform.core.Registry;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import de.hybris.platform.scripting.engine.internal.ScriptEnginesRegistry;
import de.hybris.platform.util.CSVUtils;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScriptCodeLineFactory
{
    private static final Joiner ALT_JOINER = Joiner.on("|");
    private static final String DEFAULT_ENGINE_NAME = "bsh";
    private static final Integer DEFAULT_SCRIPT_COL_POS = Integer.valueOf(0);
    private static final String SCRIPT_LINE_MARKER = "%";
    private static final Pattern OLD_SCRIPT_LINE_PATTERN_WITH_CONTROLS = Pattern.compile("\\s*(beforeEach|afterEach|if):(.*)", 42);
    private static final Pattern OLD_SCRIPT_LINE_PATTERN = Pattern.compile("\\s*(.*)", 42);
    private static final Pattern END_CONTROL_MARKER_PATTERN = Pattern.compile("\\s*(beforeEach:end|afterEach:end|endif:)");
    private final Pattern newScriptLinePatternWithControls = createNewScriptLinePatternWithControls();
    private final Pattern newScriptLinePattern = createNewScriptLinePattern();
    private final ScriptingLanguagesService service;
    private final ReaderManager readerManager;


    public ScriptCodeLineFactory(ReaderManager readerManager, ScriptingLanguagesService service)
    {
        this.readerManager = readerManager;
        this.service = service;
    }


    private Pattern createNewScriptLinePatternWithControls()
    {
        String subPattern = buildAllowedLanguagesSubPattern();
        return Pattern.compile("(" + subPattern + ")%\\s*?(beforeEach|afterEach|if):(.*)", 42);
    }


    private Pattern createNewScriptLinePattern()
    {
        String subPattern = buildAllowedLanguagesSubPattern();
        return Pattern.compile("(" + subPattern + ")%\\s*?(.*)", 42);
    }


    private String buildAllowedLanguagesSubPattern()
    {
        ScriptEnginesRegistry registry = getScriptEnginesRegistry();
        return ALT_JOINER.join(Iterables.transform(registry.getRegisteredEngineTypes(), (Function)new Object(this)));
    }


    ScriptEnginesRegistry getScriptEnginesRegistry()
    {
        return (ScriptEnginesRegistry)Registry.getApplicationContext().getBean("scriptEnginesRegistry", ScriptEnginesRegistry.class);
    }


    public AbstractCodeLine createCodeLineFromLine(Map<Integer, String> line)
    {
        String lineValue = getScriptLine(line);
        AbstractCodeLine codeLine = null;
        if(isScriptLine(lineValue))
        {
            String engineName, controlMarker, scriptBody, _lineValue = lineValue.substring(2);
            Matcher endControlMarkerMatcher = END_CONTROL_MARKER_PATTERN.matcher(_lineValue);
            Matcher oldLineMatcher = OLD_SCRIPT_LINE_PATTERN.matcher(_lineValue);
            Matcher oldLineMatcherWithControls = OLD_SCRIPT_LINE_PATTERN_WITH_CONTROLS.matcher(_lineValue);
            Matcher newLineMatcher = this.newScriptLinePattern.matcher(_lineValue);
            Matcher newLineMatcherWithControls = this.newScriptLinePatternWithControls.matcher(_lineValue);
            if(endControlMarkerMatcher.matches())
            {
                engineName = null;
                controlMarker = endControlMarkerMatcher.group(1);
                scriptBody = "";
            }
            else if(newLineMatcherWithControls.matches())
            {
                engineName = newLineMatcherWithControls.group(1);
                controlMarker = newLineMatcherWithControls.group(2) + ":";
                scriptBody = newLineMatcherWithControls.group(3);
            }
            else if(newLineMatcher.matches())
            {
                engineName = newLineMatcher.group(1);
                controlMarker = null;
                scriptBody = newLineMatcher.group(2);
            }
            else if(oldLineMatcherWithControls.matches())
            {
                engineName = "bsh";
                controlMarker = oldLineMatcherWithControls.group(1) + ":";
                scriptBody = oldLineMatcherWithControls.group(2);
            }
            else if(oldLineMatcher.matches())
            {
                engineName = "bsh";
                controlMarker = null;
                scriptBody = oldLineMatcher.group(1);
            }
            else
            {
                throw new IllegalStateException("Scripting in ImpEx is enabled, code line starts with script execution marker but whole line was not parsed correctly");
            }
            codeLine = buildCodeLine(line, engineName, controlMarker, scriptBody);
        }
        return codeLine;
    }


    private AbstractCodeLine buildCodeLine(Map<Integer, String> line, String engineName, String controlMarker, String scriptBody)
    {
        int currentLineNumber = this.readerManager.peekReader().getCurrentLineNumber();
        String currentLocation = this.readerManager.getCurrentLocation();
        String _scriptBody = (scriptBody != null) ? scriptBody.trim() : null;
        if(engineName != null)
        {
            if((engineName.equals("bsh") | engineName.equals("beanshell")) != 0)
            {
                return (AbstractCodeLine)new BeanshellCodeLine(line, controlMarker, _scriptBody, currentLineNumber, currentLocation, engineName, this.service);
            }
            if(engineName.equals("groovy"))
            {
                return (AbstractCodeLine)new GroovyCodeLine(line, controlMarker, _scriptBody, currentLineNumber, currentLocation, engineName, this.service);
            }
            if((engineName.equals("javascript") | engineName.equals("js")) != 0)
            {
                return (AbstractCodeLine)new JavascriptCodeLine(line, controlMarker, _scriptBody, currentLineNumber, currentLocation, engineName, this.service);
            }
            throw new IllegalArgumentException("Engine name " + engineName + " is not supported");
        }
        return (AbstractCodeLine)new SimpleCodeLine(line, controlMarker, _scriptBody, currentLineNumber, currentLocation);
    }


    private boolean isScriptLine(String lineValue)
    {
        return (lineValue != null &&
                        CSVUtils.lineStartsWith(lineValue, this.readerManager.getMainReader().getCommentOut(), "%"));
    }


    private String getScriptLine(Map<Integer, String> line)
    {
        return line.get(DEFAULT_SCRIPT_COL_POS);
    }
}
