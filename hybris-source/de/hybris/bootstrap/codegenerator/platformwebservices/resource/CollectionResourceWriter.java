package de.hybris.bootstrap.codegenerator.platformwebservices.resource;

import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.platformwebservices.ResourceConfig;
import de.hybris.bootstrap.typesystem.YExtension;
import java.util.Collection;
import java.util.Collections;
import org.apache.log4j.Logger;

@Deprecated(since = "1818", forRemoval = true)
public class CollectionResourceWriter extends AbstractResourceWriter
{
    private static final Logger LOG = Logger.getLogger(CollectionResourceWriter.class.getName());
    protected static final String ABSTRACT_COLLECTION_RESOURCE = "de.hybris.platform.webservices.AbstractCollectionResource";


    public CollectionResourceWriter(CodeGenerator gen, YExtension ext, ResourceConfig cfg)
    {
        super(cfg, gen, ext);
        String extendsClass = "de.hybris.platform.webservices.AbstractCollectionResource";
        addRequiredImport("de.hybris.platform.webservices.AbstractCollectionResource");
        addRequiredImport("java.util.Collection");
        addRequiredImport("javax.ws.rs.Path");
        String path = cfg.getDTOConfig().getPlural().toLowerCase();
        addAnnotation("Path(\"/" + path + "\")");
        setClassToExtend("de.hybris.platform.webservices.AbstractCollectionResource <Collection<" + getResourceConfig().getDTOConfig().getModelClassSimpleName() + ">>");
        setJavadoc("Generated REST root resource for a collection of elements of  type " + cfg
                        .getDTOConfig().getType().getCode() + " defined at extension " + ((YExtension)cfg
                        .getDTOConfig().getType().getNamespace()).getExtensionName() + " Allowed methods: GET, POST, PUT, DELETE, HEADER  ");
    }


    protected Collection<MethodWriter> getSubResourceMethodWriters()
    {
        ResourceConfig cfg = getResourceConfig().getSubResources().iterator().next();
        MethodWriter method = createSubResourceMethodWriter(cfg, false);
        String subResUriId = cfg.getDTOConfig().getType().getCode().toLowerCase();
        method.addAnnotation("Path(\"{" + subResUriId + "}\")");
        return Collections.singletonList(method);
    }


    protected MethodWriter getReadResourceMethodWriter()
    {
        return null;
    }


    protected Collection<MethodWriter> getResourceValueMethodWriters()
    {
        return createResourceValueMethodWriters("Collection", getResourceConfig().getDTOConfig().getModelClassSimpleName(),
                        getResourceConfig().getDTOConfig().getPlural() + "Collection");
    }


    protected MethodWriter createGetMethodWriter()
    {
        ResourceConfig cfg = getResourceConfig();
        MethodWriter result = createCRUDMethodWriter("javax.ws.rs.GET", "getAll" + cfg.getDTOConfig().getPlural());
        result.setContentPlain("return createGetResponse().build(" + getResourceConfig().getDTOConfig().getDtoClassSimpleName() + ".class);");
        return result;
    }


    protected MethodWriter createDeleteMethodWriter()
    {
        ResourceConfig cfg = getResourceConfig();
        MethodWriter result = createCRUDMethodWriter("javax.ws.rs.DELETE", "deleteAll" + cfg.getDTOConfig().getPlural());
        result.setContentPlain("return createDeleteResponse().build();");
        return result;
    }


    protected MethodWriter createPutMethodWriter()
    {
        throw new UnsupportedOperationException();
    }


    protected MethodWriter createPostMethodWriter()
    {
        String singleDto = getResourceConfig().getDTOConfig().getDtoPackage() + "." + getResourceConfig().getDTOConfig().getDtoPackage() + "DTO";
        addRequiredImport(singleDto);
        ResourceConfig cfg = getResourceConfig();
        MethodWriter result = createCRUDMethodWriter("javax.ws.rs.POST", "post" + cfg.getDTOConfig().getSingular());
        result.addParameter(singleDto, "dto");
        result.setContentPlain("return createPostResponse(dto).build();");
        return result;
    }


    private String capitalize(String in)
    {
        if(in == null || in.isEmpty())
        {
            return in;
        }
        return in.substring(0, 1).toUpperCase() + in.substring(0, 1).toUpperCase();
    }
}
