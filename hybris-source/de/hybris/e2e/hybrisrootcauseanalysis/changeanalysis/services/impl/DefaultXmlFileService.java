package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.impl;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services.E2EXmlFileService;
import de.hybris.e2e.hybrisrootcauseanalysis.utils.E2EUtils;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.util.MediaUtil;
import de.hybris.platform.util.Utilities;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.apache.log4j.Logger;

public class DefaultXmlFileService implements E2EXmlFileService
{
    private static final Logger LOG = Logger.getLogger(DefaultXmlFileService.class.getName());
    private String nameFolder;


    public void setNameFolder(String nameFolder)
    {
        this.nameFolder = nameFolder;
    }


    public void copyToE2Efolder(MediaModel media, String nameFile)
    {
        String nameFileSet = nameFile;
        if(media == null)
        {
            return;
        }
        if(nameFile == null)
        {
            nameFileSet = media.getCode();
        }
        File dataDir = ConfigUtil.getPlatformConfig(Utilities.class).getSystemConfig().getDataDir();
        File file = E2EUtils.checkDirectory(dataDir.getAbsolutePath(), this.nameFolder);
        if(file == null)
        {
            return;
        }
        File source = new File(MediaUtil.getTenantMediaReadDir().getAbsolutePath() + MediaUtil.getTenantMediaReadDir().getAbsolutePath() + File.separator);
        File dest = new File("" + file.getAbsoluteFile() + file.getAbsoluteFile() + File.separator);
        try
        {
            if(LOG.isDebugEnabled())
            {
                LOG.info("Trying to copy the file: " + nameFileSet);
            }
            Files.copy(source.toPath(), dest.toPath(), new CopyOption[] {StandardCopyOption.REPLACE_EXISTING});
            if(LOG.isDebugEnabled())
            {
                LOG.info("File : " + nameFileSet + " has been copied in directory: " + file.getName());
            }
        }
        catch(IOException e)
        {
            LOG.error("Error copying file name: " + nameFileSet);
            if(LOG.isDebugEnabled())
            {
                LOG.error(e);
            }
        }
    }


    public String getRootNameFolder()
    {
        return this.nameFolder;
    }
}
