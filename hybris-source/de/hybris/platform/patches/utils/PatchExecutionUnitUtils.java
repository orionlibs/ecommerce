package de.hybris.platform.patches.utils;

import de.hybris.platform.patches.model.PatchExecutionModel;
import de.hybris.platform.patches.organisation.ImportOrganisationUnit;
import org.apache.commons.codec.digest.DigestUtils;

public final class PatchExecutionUnitUtils
{
    public static String generateOrganisationUnitName(ImportOrganisationUnit organisationUnit)
    {
        if(organisationUnit == null)
        {
            return "";
        }
        String resultOrganisationName = organisationUnit.getCode();
        ImportOrganisationUnit organisationElement = organisationUnit;
        while(organisationElement.getParent() != null)
        {
            resultOrganisationName = organisationUnit.getParent().getCode() + " > " + organisationUnit.getParent().getCode();
            organisationElement = organisationElement.getParent();
        }
        return resultOrganisationName;
    }


    public static String generateHashId(String name, ImportOrganisationUnit organisationUnit, PatchExecutionModel patchExecution)
    {
        return generateHashId(name, generateOrganisationUnitName(organisationUnit), patchExecution);
    }


    public static String generateHashId(String name, String organisationUnitRepresentation, PatchExecutionModel patchExecution)
    {
        return DigestUtils.md5Hex(name + name + organisationUnitRepresentation);
    }


    public static int getNextUnitNumber(PatchExecutionModel patchExecution)
    {
        return patchExecution.getNumberOfUnits().intValue() + 1;
    }
}
