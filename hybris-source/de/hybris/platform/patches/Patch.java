package de.hybris.platform.patches;

import de.hybris.platform.patches.organisation.StructureState;

public interface Patch
{
    void createProjectData(StructureState paramStructureState);


    String getPatchId();


    String getPatchName();


    String getPatchDescription();


    Release getRelease();


    StructureState getStructureState();


    default void createEssentialData()
    {
    }
}
