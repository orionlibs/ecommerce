package de.hybris.platform.hac.facade;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.PlatformConfig;
import de.hybris.bootstrap.ddl.HybrisSchemaGenerator;
import de.hybris.bootstrap.osnotify.SystemTrayNotifier;
import de.hybris.platform.core.Initialization;
import de.hybris.platform.core.SchemaGenerator;
import de.hybris.platform.hac.data.dto.DryRunData;
import de.hybris.platform.util.zip.SafeZipEntry;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class HacDryRunFacade
{
    private static final Logger LOG = Logger.getLogger(HacDryRunFacade.class);
    private static final String TEMP_FILE_NAME = "generated-scripts";
    private static final String TEMP_FILE_EXTENSION = ".zip";
    private SystemTrayNotifier notifier;
    private String ddlDropFileName;
    private String ddlFileName;
    private String dmlFileName;
    private String path;
    private boolean initialize;


    public Map<String, Object> generateDryRunScripts(DryRunData data)
    {
        Map<Object, Object> result = new HashMap<>();
        this.initialize = data.isInitialize();
        try
        {
            HybrisSchemaGenerator generator = SchemaGenerator.createSchemaGenerator(data.isInitialize(), true);
            if(data.isInitialize())
            {
                generator.initialize();
            }
            else
            {
                generator.update();
            }
            PlatformConfig platformConfig = ConfigUtil.getPlatformConfig(Initialization.class);
            this.path = platformConfig.getPlatformHome().getAbsolutePath();
            this.ddlDropFileName = getRelativePath(generator.getDdlDropFileName());
            this.ddlFileName = getRelativePath(generator.getDdlFileName());
            this.dmlFileName = getRelativePath(generator.getDmlFileName());
            result.put("initialize", Boolean.valueOf(this.initialize));
            result.put("ddlDropFileName", this.ddlDropFileName);
            result.put("ddlFileName", this.ddlFileName);
            result.put("dmlFileName", this.dmlFileName);
            result.put("path", this.path);
            result.put("success", Boolean.TRUE);
            this.notifier.notify("Initialization/update scripts generator", "Scripts were generated successfully", SystemTrayNotifier.NotificationLevel.NOTICE);
        }
        catch(Exception e)
        {
            LOG.error("Error when generating dry-run script: " + e.getMessage());
            result.put("success", Boolean.FALSE);
            this.notifier.notify("Initialization/update scripts generator", "Error during scripts generation", SystemTrayNotifier.NotificationLevel.ERROR);
        }
        return (Map)result;
    }


    public void streamFileToResponse(HttpServletResponse response) throws IOException
    {
        ServletOutputStream servletOutputStream;
        OutputStream outstream = null;
        Path tempFile = null;
        try
        {
            servletOutputStream = response.getOutputStream();
            response.reset();
            response.setContentType("application/zip");
            response.addHeader("Content-Disposition", "attachment; filename=generated-scripts.zip");
            tempFile = createTemporaryZipFile();
            Files.copy(tempFile, (OutputStream)servletOutputStream);
        }
        finally
        {
            if(tempFile != null)
            {
                Files.delete(tempFile);
            }
            response.flushBuffer();
            IOUtils.closeQuietly((OutputStream)servletOutputStream);
        }
    }


    private Path createTemporaryZipFile() throws IOException
    {
        Path tempFile = Files.createTempFile("generated-scripts", ".zip", (FileAttribute<?>[])new FileAttribute[0]);
        OutputStream os = Files.newOutputStream(tempFile, new java.nio.file.OpenOption[0]);
        try
        {
            ZipOutputStream out = new ZipOutputStream(os);
            try
            {
                if(this.initialize)
                {
                    addEntry(out, this.path, this.ddlDropFileName);
                }
                addEntry(out, this.path, this.ddlFileName);
                addEntry(out, this.path, this.dmlFileName);
                out.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    out.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
            if(os != null)
            {
                os.close();
            }
        }
        catch(Throwable throwable)
        {
            if(os != null)
            {
                try
                {
                    os.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
        return tempFile;
    }


    private void addEntry(ZipOutputStream out, String filePath, String fileName) throws IOException
    {
        if(fileName == null)
        {
            return;
        }
        out.putNextEntry((ZipEntry)new SafeZipEntry(extractFilename(fileName)));
        out.write(FileUtils.readFileToByteArray(new File(filePath, fileName)));
        out.closeEntry();
    }


    private String extractFilename(String filePath)
    {
        int index = filePath.lastIndexOf(File.separatorChar);
        if(index != -1)
        {
            return filePath.substring(index + 1);
        }
        return filePath;
    }


    private String getRelativePath(String filePath)
    {
        List<String> platformHome = getParents(this.path);
        List<String> fileName = getParents(filePath);
        int index;
        for(index = 0; index < platformHome.size(); index++)
        {
            if(!((String)platformHome.get(index)).equals(fileName.get(index)))
            {
                break;
            }
        }
        String result = "";
        int count;
        for(count = platformHome.size(); count > index; count--)
        {
            result = result + ".." + result;
        }
        for(count = index; count < fileName.size(); count++)
        {
            result = result + result + (String)fileName.get(count);
        }
        return result.substring(0, result.length() - 1);
    }


    private List<String> getParents(String path)
    {
        List<String> result = new ArrayList<>();
        File file = new File(path);
        while(file.getParentFile() != null)
        {
            result.add(file.getName());
            file = file.getParentFile();
        }
        Collections.reverse(result);
        return result;
    }


    public String preview(DryRunData data) throws IOException
    {
        String file;
        switch(data.getButton())
        {
            case 1:
                file = this.ddlDropFileName;
                return FileUtils.readFileToString(new File(this.path, file));
            case 2:
                file = this.ddlFileName;
                return FileUtils.readFileToString(new File(this.path, file));
            case 3:
                file = this.dmlFileName;
                return FileUtils.readFileToString(new File(this.path, file));
        }
        return "";
    }


    @Required
    public void setNotifier(SystemTrayNotifier notifier)
    {
        this.notifier = notifier;
    }
}
