package de.hybris.platform.impex.jalo;

import de.hybris.platform.core.model.ItemModel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public interface BeanShellImportable
{
    void enableSLModeForImport(boolean paramBoolean);


    void setLocale(Locale paramLocale);


    void importQuery(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);


    void importFile(File paramFile, String paramString, int paramInt) throws UnsupportedEncodingException, FileNotFoundException, ImpExException;


    void importStream(InputStream paramInputStream, String paramString, char[] paramArrayOfchar, int paramInt1, int paramInt2) throws UnsupportedEncodingException;


    void importMedia(ImpExMedia paramImpExMedia, Integer paramInteger1, String paramString, Character paramCharacter, Integer paramInteger2) throws UnsupportedEncodingException;


    void enableBeanShellCodeExecution(boolean paramBoolean);


    void enableBeanShellSyntaxParsing(boolean paramBoolean);


    void enableResolving(boolean paramBoolean);


    ItemModel getLastImportedData();
}
