package de.hybris.platform.patches.utils;

import de.hybris.platform.patches.organisation.ImportLanguage;
import de.hybris.platform.patches.organisation.ImportOrganisationUnit;
import de.hybris.platform.patches.organisation.StructureState;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class StructureStateUtils
{
    private StructureStateUtils()
    {
        throw new UnsupportedOperationException("creating instances of StructureStateUtils is not allowed");
    }


    public static Set<ImportLanguage> getNewGlobalLanguages(ImportOrganisationUnit[] units, StructureState structureState)
    {
        Set<ImportLanguage> languagesAlreadyImported = new HashSet<>();
        Set<ImportLanguage> languagesDefinedInThisStructureState = new HashSet<>();
        Collection<ImportOrganisationUnit> leafs = getLeafsUnits(units);
        for(ImportOrganisationUnit unit : leafs)
        {
            if(structureState.isAfter(unit.getStructureState()))
            {
                languagesAlreadyImported.addAll(unit.getLanguages());
                continue;
            }
            if(unit.getStructureState() == structureState)
            {
                languagesDefinedInThisStructureState.addAll(unit.getLanguages());
            }
        }
        languagesDefinedInThisStructureState.removeAll(languagesAlreadyImported);
        return languagesDefinedInThisStructureState;
    }


    public static Set<ImportLanguage> getAllGlobalLanguages(ImportOrganisationUnit[] units, StructureState structureState)
    {
        Collection<ImportOrganisationUnit> leafs = getLeafsUnits(units);
        Set<ImportLanguage> languages = new HashSet<>();
        for(ImportOrganisationUnit unit : leafs)
        {
            if(!unit.getStructureState().isAfter(structureState))
            {
                languages.addAll(unit.getLanguages());
            }
        }
        return languages;
    }


    public static Set<ImportLanguage> getNewLanguages(ImportOrganisationUnit unit, StructureState structureState)
    {
        Set<ImportLanguage> languagesAlreadyImported = new HashSet<>();
        Set<ImportLanguage> languagesDefinedInThisStructureState = new HashSet<>();
        for(ImportOrganisationUnit child : unit.getChildren())
        {
            if(structureState.isAfter(child.getStructureState()))
            {
                languagesAlreadyImported.addAll(child.getLanguages());
                continue;
            }
            if(child.getStructureState() == structureState)
            {
                languagesDefinedInThisStructureState.addAll(child.getLanguages());
            }
        }
        languagesDefinedInThisStructureState.removeAll(languagesAlreadyImported);
        return languagesDefinedInThisStructureState;
    }


    public static List<StructureState> getStructureStateGap(StructureState currentStructureState, StructureState executedStructureState)
    {
        if(currentStructureState == executedStructureState || executedStructureState.isAfter(currentStructureState))
        {
            return Collections.emptyList();
        }
        List<StructureState> list = new ArrayList<>();
        for(StructureState iteratedStructureState : (StructureState[])currentStructureState.getClass().getEnumConstants())
        {
            if(iteratedStructureState.isAfter(executedStructureState))
            {
                list.add(iteratedStructureState);
                if(iteratedStructureState == currentStructureState)
                {
                    break;
                }
            }
        }
        return list;
    }


    private static Collection<ImportOrganisationUnit> getLeafsUnits(ImportOrganisationUnit[] units)
    {
        Set<ImportOrganisationUnit> leafsUnits = new HashSet<>();
        if(units != null)
        {
            for(ImportOrganisationUnit unit : units)
            {
                getLeafsUnits(unit, leafsUnits);
            }
        }
        return leafsUnits;
    }


    private static Collection<ImportOrganisationUnit> getLeafsUnits(ImportOrganisationUnit unit, Collection<ImportOrganisationUnit> collection)
    {
        Collection<ImportOrganisationUnit> children = unit.getChildren();
        if(children == null)
        {
            collection.add(unit);
        }
        else
        {
            for(ImportOrganisationUnit child : children)
            {
                getLeafsUnits(child, collection);
            }
        }
        return collection;
    }
}
