package de.hybris.platform.servicelayer.impex;

public interface ImpExValidationResult
{
    boolean isSuccessful();


    String getFailureCause();
}
