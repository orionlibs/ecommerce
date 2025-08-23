package de.hybris.platform.patches.organisation;

import java.util.Collection;

public interface ImportOrganisationUnit<T extends ImportOrganisationUnit, S extends ImportOrganisationUnit>
{
    String getCode();


    String getName();


    String getFolderName();


    String getCommonFolderName();


    Collection<T> getChildren();


    Collection<ImportLanguage> getLanguages();


    S getParent();


    void setParent(S paramS);


    StructureState getStructureState();
}
