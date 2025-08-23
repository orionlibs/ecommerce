package de.hybris.platform.impex.jalo;

import com.google.common.collect.Lists;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.scripting.engine.ScriptingLanguagesService;
import java.util.List;
import java.util.Map;

public class GroovyCodeLine extends AbstractScriptingEngineCodeLine
{
    private static final List<String> STANDARD_IMPORTS = Lists.newArrayList((Object[])new String[] {
                    "import de.hybris.platform.core.*", "import de.hybris.platform.core.model.user.*", "import de.hybris.platform.core.HybrisEnumValue", "import de.hybris.platform.util.*", "import de.hybris.platform.impex.jalo.*", "import de.hybris.platform.jalo.*", "import " + Currency.class
                    .getName(), "import de.hybris.platform.jalo.c2l.*", "import de.hybris.platform.jalo.user.*", "import de.hybris.platform.jalo.flexiblesearch.*",
                    "import de.hybris.platform.jalo.product.ProductManager"});


    public GroovyCodeLine(Map<Integer, String> csvLine, String marker, String scriptContent, int lineNumber, String location, String scriptingEngineName, ScriptingLanguagesService service)
    {
        super(csvLine, marker, scriptContent, lineNumber, location, scriptingEngineName, service);
    }


    protected List<String> getStandardImports()
    {
        return STANDARD_IMPORTS;
    }
}
