package de.hybris.platform.ruleengine.drools.impl;

import de.hybris.platform.core.model.media.MediaFolderModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.ruleengine.dao.DroolsKIEModuleMediaDao;
import de.hybris.platform.ruleengine.drools.KieModuleService;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleMediaModel;
import de.hybris.platform.ruleengine.util.JarValidator;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.drools.compiler.compiler.io.FileSystemItem;
import org.drools.compiler.compiler.io.Folder;
import org.drools.compiler.compiler.io.memory.MemoryFileSystem;
import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.drools.compiler.kie.builder.impl.MemoryKieModule;
import org.drools.compiler.kproject.ReleaseIdImpl;
import org.drools.compiler.kproject.models.KieModuleModelImpl;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.ReleaseId;
import org.kie.api.builder.model.KieModuleModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultKieModuleService implements KieModuleService
{
    private static final String META_INF_KMODULE_XML = "META-INF/kmodule.xml";
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultKieModuleService.class);
    private DroolsKIEModuleMediaDao droolsKIEModuleMediaDao;
    private ModelService modelService;
    private MediaService mediaService;
    private ConfigurationService configurationService;
    private boolean useCMC = true;


    public void saveKieModule(String kieModuleName, String releaseId, KieModule kieModule)
    {
        if(!isUseCMC())
        {
            LOGGER.debug("centralized module compilation disabled, skipping saveKieModule()");
            return;
        }
        ServicesUtil.validateParameterNotNull(kieModuleName, "kieModuleName can't be null");
        ServicesUtil.validateParameterNotNull(releaseId, "releaseId can't be null");
        ServicesUtil.validateParameterNotNull(kieModule, "kieModule can't be null");
        Optional<DroolsKIEModuleMediaModel> droolsKIEModuleMediaOptional = getKieModuleMedia(kieModuleName, releaseId);
        if(!droolsKIEModuleMediaOptional.isPresent())
        {
            LOGGER.debug("KieModule for name '{}' and releaseId '{}' not found and is valid to be stored", kieModuleName, releaseId);
            DroolsKIEModuleMediaModel kieModuleMedia = createKieModuleMedia(kieModuleName, releaseId);
            getModelService().save(kieModuleMedia);
            getMediaService()
                            .setStreamForMedia((MediaModel)kieModuleMedia, new ByteArrayInputStream(((InternalKieModule)kieModule).getBytes()));
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("save KieModule with name '{}' and releaseId '{}'", kieModuleName, releaseId);
                if(kieModule instanceof MemoryKieModule)
                {
                    debugLogFSFolder(((MemoryKieModule)kieModule).getMemoryFileSystem().getRootFolder(), "");
                }
                debugGenerateKjar(kieModuleName, releaseId, kieModule);
            }
        }
        else
        {
            LOGGER.debug("Stored instance of KieModule for name '{}' and releaseId '{}' already exists", kieModuleName, releaseId);
        }
    }


    protected DroolsKIEModuleMediaModel createKieModuleMedia(String kieModuleName, String releaseId)
    {
        DroolsKIEModuleMediaModel droolsKIEModuleMedia = (DroolsKIEModuleMediaModel)this.modelService.create(DroolsKIEModuleMediaModel.class);
        droolsKIEModuleMedia.setCode(generateMediaCode(kieModuleName, releaseId));
        droolsKIEModuleMedia.setFolder(getMediaFolder());
        droolsKIEModuleMedia.setKieModuleName(kieModuleName);
        droolsKIEModuleMedia.setReleaseId(releaseId);
        return droolsKIEModuleMedia;
    }


    protected MediaFolderModel getMediaFolder()
    {
        String mediaFolderQualifier = getConfigurationService().getConfiguration().getString("ruleengine.kie.module.media.folder.qualifier", "kie-modules");
        return getMediaService().getFolder(mediaFolderQualifier);
    }


    protected void debugGenerateKjar(String kieModuleName, String releaseId, KieModule kieModule)
    {
        if(!LOGGER.isDebugEnabled())
        {
            return;
        }
        try
        {
            FileUtils.writeByteArrayToFile(new File(
                            String.format("%s_%s.jar", new Object[] {kieModuleName, releaseId.replaceAll(":", "-").replaceAll("\\.", "_")})), ((InternalKieModule)kieModule).getBytes());
        }
        catch(IOException e)
        {
            LOGGER.error("error during debugGenerateKjar()", e);
        }
    }


    protected void debugLogFSFolder(Folder folder, String tab)
    {
        if(!LOGGER.isDebugEnabled())
        {
            return;
        }
        LOGGER.debug("{}{}", tab, folder.getName());
        for(FileSystemItem r : folder.getMembers())
        {
            if(r instanceof File)
            {
                try
                {
                    LOGGER.debug("{}{} {} bytes", new Object[] {tab, ((File)r).getName(), Integer.valueOf((IOUtils.toByteArray(((File)r).getContents())).length)});
                }
                catch(IOException e)
                {
                    LOGGER.error("error during debugLogFSFolder()", e);
                }
            }
        }
        for(FileSystemItem r : folder.getMembers())
        {
            if(r instanceof Folder)
            {
                debugLogFSFolder((Folder)r, tab + "    ");
            }
        }
    }


    protected String generateMediaCode(String kieModuleName, String releaseId)
    {
        return String.format("%s:%s", new Object[] {kieModuleName, releaseId});
    }


    public Optional<KieModule> loadKieModule(String kieModuleName, String releaseId)
    {
        if(!isUseCMC())
        {
            LOGGER.debug("centralized module compilation disabled, skipping loadKieModule()");
            return Optional.empty();
        }
        ServicesUtil.validateParameterNotNull(kieModuleName, "kieModuleName can't be null");
        ServicesUtil.validateParameterNotNull(releaseId, "releaseId can't be null");
        Optional<DroolsKIEModuleMediaModel> droolsKIEModuleMediaOptional = getKieModuleMedia(kieModuleName, releaseId);
        if(droolsKIEModuleMediaOptional.isPresent())
        {
            try
            {
                JarValidator.validateZipSlipSecure(getMediaService().getStreamFromMedia((MediaModel)droolsKIEModuleMediaOptional.get()));
                MemoryFileSystem memoryFileSystem = MemoryFileSystem.readFromJar(
                                getMediaService().getStreamFromMedia((MediaModel)droolsKIEModuleMediaOptional.get()));
                File kModuleFile = memoryFileSystem.getFile("META-INF/kmodule.xml");
                if(!kModuleFile.exists())
                {
                    LOGGER.warn("{} is absent for kjar of the KieModule (kieModuleName = '{}', releaseId = '{}')", new Object[] {"META-INF/kmodule.xml", kieModuleName, releaseId});
                    return Optional.empty();
                }
                KieModuleModel kieModuleModel = KieModuleModelImpl.fromXML(kModuleFile.getContents());
                MemoryKieModule memoryKieModule = new MemoryKieModule((ReleaseId)new ReleaseIdImpl(releaseId), kieModuleModel, memoryFileSystem);
                memoryFileSystem.mark();
                return (Optional)Optional.of(memoryKieModule);
            }
            catch(Exception e)
            {
                LOGGER.error("Can't read serialized KieModule (kieModuleName = '{}', releaseId = '{}')", kieModuleName, releaseId);
                LOGGER.error("exception caught.", e);
                return Optional.empty();
            }
        }
        return Optional.empty();
    }


    protected Optional<DroolsKIEModuleMediaModel> getKieModuleMedia(String kieModuleName, String releaseId)
    {
        return getDroolsKIEModuleMediaDao().findKIEModuleMedia(kieModuleName, releaseId);
    }


    protected DroolsKIEModuleMediaDao getDroolsKIEModuleMediaDao()
    {
        return this.droolsKIEModuleMediaDao;
    }


    @Required
    public void setDroolsKIEModuleMediaDao(DroolsKIEModuleMediaDao droolsKIEModuleMediaDao)
    {
        this.droolsKIEModuleMediaDao = droolsKIEModuleMediaDao;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected boolean isUseCMC()
    {
        return this.useCMC;
    }


    @Required
    public void setUseCMC(boolean useCMC)
    {
        this.useCMC = useCMC;
    }
}
