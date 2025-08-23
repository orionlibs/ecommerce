package de.hybris.platform.ruleengine.init;

import java.util.Optional;

public interface RuleEngineContainerRegistry<RELEASEHOLDER, CONTAINER>
{
    void setActiveContainer(RELEASEHOLDER paramRELEASEHOLDER, CONTAINER paramCONTAINER);


    CONTAINER getActiveContainer(RELEASEHOLDER paramRELEASEHOLDER);


    CONTAINER removeActiveContainer(RELEASEHOLDER paramRELEASEHOLDER);


    Optional<RELEASEHOLDER> lookupForDeployedRelease(String... paramVarArgs);


    void lockReadingRegistry();


    void unlockReadingRegistry();


    void lockWritingRegistry();


    void unlockWritingRegistry();


    boolean isLockedForReading();


    boolean isLockedForWriting();
}
