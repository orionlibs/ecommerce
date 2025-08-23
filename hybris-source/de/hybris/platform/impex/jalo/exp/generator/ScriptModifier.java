package de.hybris.platform.impex.jalo.exp.generator;

import de.hybris.platform.impex.jalo.exp.ScriptGenerator;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Set;

public interface ScriptModifier
{
    void init(ScriptGenerator paramScriptGenerator);


    @Deprecated(since = "ages", forRemoval = false)
    Set<ComposedType> collectSubtypesWithOwnDeployment(ComposedType paramComposedType);


    boolean filterTypeCompletely(ComposedType paramComposedType);


    Set<ComposedType> getExportableRootTypes(ScriptGenerator paramScriptGenerator);
}
