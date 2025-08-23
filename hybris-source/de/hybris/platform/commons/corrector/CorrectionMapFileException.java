package de.hybris.platform.commons.corrector;

public class CorrectionMapFileException extends RuntimeException
{
    public CorrectionMapFileException(String message, String correctionMapFileName)
    {
        super("Correction map file problem: '" + correctionMapFileName + "', original message is: " + message);
    }
}
