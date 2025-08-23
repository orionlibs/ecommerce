package de.hybris.bootstrap.codegenerator.platformwebservices.resource;

import de.hybris.bootstrap.codegenerator.ClassWriter;
import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.platformwebservices.ResourceConfig;
import de.hybris.bootstrap.typesystem.YExtension;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.log4j.Logger;

@Deprecated(since = "1818", forRemoval = true)
public class SingleResourceWriter extends AbstractResourceWriter
{
    private static final Logger LOG = Logger.getLogger(SingleResourceWriter.class.getName());


    public SingleResourceWriter(CodeGenerator gen, YExtension ext, ResourceConfig cfg)
    {
        super(cfg, gen, ext);
        String extendsClass = "de.hybris.platform.webservices.AbstractYResource";
        addRequiredImport("de.hybris.platform.webservices.AbstractYResource");
        setClassToExtend("de.hybris.platform.webservices.AbstractYResource<" + getResourceConfig().getDTOConfig().getModelClassSimpleName() + ">");
        String typeCode = cfg.getDTOConfig().getType().getCode();
        String extName = ((YExtension)cfg.getDTOConfig().getType().getNamespace()).getExtensionName();
        setJavadoc("Generated resource class for type " + typeCode + " first defined at extension " + extName);
    }


    protected MethodWriter getReadResourceMethodWriter()
    {
        String modelClassSimpleName = getResourceConfig().getDTOConfig().getModelClassSimpleName();
        UniqueIdentifierResolver uidResolver = ((AbstractResourceConfig)getResourceConfig()).getResourceConfigProvider().getUidResover();
        MethodWriter method = createReadResourceMethodWriter(getResourceConfig().getDTOConfig().getModelClassName());
        method.setJavadoc("Gets the {@link " + modelClassSimpleName + "} resource which is addressed by current resource request.\n@see de.hybris.platform.webservices.AbstractYResource#readResource(String)");
        StringBuilder content = new StringBuilder(100);
        String qualifierRaw = uidResolver.getUniqueIdentifier(getResourceConfig().getDTOConfig().getType());
        if("pk".equals(qualifierRaw))
        {
            method.addRequiredImport("de.hybris.platform.core.PK");
            content.append("return serviceLocator.getModelService().get(PK.parse(resourceId));\n");
        }
        else
        {
            qualifierRaw = ClassWriter.firstLetterUpperCase(qualifierRaw);
            content.append("\t" + modelClassSimpleName + " model = new " + modelClassSimpleName + "();").append("\n");
            content.append("\tmodel.set" + qualifierRaw + "(resourceId);").append("\n");
            content.append("\treturn (").append(modelClassSimpleName).append(") readResourceInternal(model);").append("\n");
        }
        method.setContentPlain(content.toString());
        return method;
    }


    protected Collection<MethodWriter> getSubResourceMethodWriters()
    {
        Collection<MethodWriter> result = null;
        result = new ArrayList<>();
        for(ResourceConfig cfg : getResourceConfig().getSubResources())
        {
            MethodWriter method = createSubResourceMethodWriter(cfg, true);
            String subResUriBase = cfg.getDTOConfig().getPlural().toLowerCase();
            String subResUriId = cfg.getDTOConfig().getType().getCode().toLowerCase();
            method.addAnnotation("Path(\"/" + subResUriBase + "/{" + subResUriId + "}\")");
            result.add(method);
            addRequiredImport(cfg.getClassName());
        }
        return result;
    }


    protected Collection<MethodWriter> getResourceValueMethodWriters()
    {
        ResourceConfig cfg = getResourceConfig();
        return createResourceValueMethodWriters(cfg.getDTOConfig().getModelClassSimpleName(), null, cfg.getDTOConfig()
                        .getSingular() + "Model");
    }


    protected MethodWriter createGetMethodWriter()
    {
        ResourceConfig cfg = getResourceConfig();
        MethodWriter result = createCRUDMethodWriter("javax.ws.rs.GET", "get" + cfg.getDTOConfig().getSingular());
        result.setContentPlain("return createGetResponse().build();");
        return result;
    }


    protected MethodWriter createDeleteMethodWriter()
    {
        ResourceConfig cfg = getResourceConfig();
        MethodWriter result = createCRUDMethodWriter("javax.ws.rs.DELETE", "delete" + cfg.getDTOConfig().getSingular());
        result.setContentPlain("return createDeleteResponse().build();");
        return result;
    }


    protected MethodWriter createPutMethodWriter()
    {
        ResourceConfig cfg = getResourceConfig();
        MethodWriter result = createCRUDMethodWriter("javax.ws.rs.PUT", "put" + cfg.getDTOConfig().getSingular());
        result.addParameter(cfg.getDTOConfig().getDtoClassName(), "dto");
        result.setContentPlain("return createPutResponse(dto).build();");
        return result;
    }


    protected MethodWriter createPostMethodWriter()
    {
        ResourceConfig cfg = getResourceConfig();
        MethodWriter result = createCRUDMethodWriter("javax.ws.rs.POST", "post" + cfg.getDTOConfig().getSingular());
        result.addParameter(cfg.getDTOConfig().getDtoClassName(), "dto");
        result.setContentPlain("return createPostResponse(dto).build();");
        return result;
    }
}
