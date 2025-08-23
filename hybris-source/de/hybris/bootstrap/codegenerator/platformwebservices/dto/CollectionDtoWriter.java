package de.hybris.bootstrap.codegenerator.platformwebservices.dto;

import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.codegenerator.Visibility;
import de.hybris.bootstrap.codegenerator.platformwebservices.DtoConfig;
import de.hybris.bootstrap.codegenerator.platformwebservices.WebservicesConfig;
import de.hybris.bootstrap.typesystem.YExtension;
import java.util.Collection;

@Deprecated(since = "1818", forRemoval = true)
public class CollectionDtoWriter extends AbstractDtoWriter
{
    private static final String ABSTRACT_COLLECTION_DTO = "de.hybris.platform.webservices.dto.AbstractCollectionDTO";
    private final String dtoListMemberName;


    public CollectionDtoWriter(WebservicesConfig wsConfig, DtoConfig cfg, CodeGenerator gen, YExtension ext)
    {
        super(wsConfig, cfg, gen, ext);
        addRequiredImport("de.hybris.platform.webservices.dto.AbstractCollectionDTO");
        addRequiredImport("java.util.List");
        addRequiredImport(getDtoConfig().getCollectionElementConfig().getDtoClassName());
        addRequiredImport(cfg.getDtoClassSimpleName());
        setClassToExtend("de.hybris.platform.webservices.dto.AbstractCollectionDTO");
        addAnnotation("XmlRootElement(name = \"" + getDtoConfig().getPlural().toLowerCase() + "\")");
        this.dtoListMemberName = getDtoConfig().getPlural().toLowerCase() + "List";
        String dtoListElement = getDtoConfig().getCollectionElementConfig().getDtoClassSimpleName();
        addDeclaration("/** <i>Generated variable</i> - List of <code>" + dtoListElement + "*/\nprivate List<" + dtoListElement + ">  " + this.dtoListMemberName + ";\n ");
        setJavadoc("Generated collection dto class for type " + cfg.getType().getCode() + " first defined at extension " + ((YExtension)cfg
                        .getType().getNamespace()).getExtensionName());
    }


    public CollectionDtoConfig getDtoConfig()
    {
        return (CollectionDtoConfig)super.getDtoConfig();
    }


    protected Collection<MethodWriter> getConstructorMethodWriters()
    {
        Collection<MethodWriter> result = super.getConstructorMethodWriters();
        String element = getDtoConfig().getCollectionElementConfig().getDtoClassName();
        MethodWriter writer = createDefaultConstructorMethodWriter();
        writer.addParameter("List<" + element + ">", this.dtoListMemberName);
        writer.setContentPlain("super();\nthis." + this.dtoListMemberName + "=" + this.dtoListMemberName + ";");
        result.add(writer);
        return result;
    }


    protected Collection<MethodWriter> getBeanMethodWriters()
    {
        Collection<MethodWriter> result = super.getBeanMethodWriters();
        DtoConfig cfg = getDtoConfig().getCollectionElementConfig();
        MethodWriter getter = new MethodWriter(Visibility.PUBLIC, "List<" + cfg.getDtoClassName() + ">", "get" + cfg.getPlural());
        getter.addRequiredImport("javax.xml.bind.annotation.XmlElement");
        getter.addAnnotation("XmlElement(name = \"" + cfg.getSingular().toLowerCase() + "\")");
        getter.setJavadoc("@return the " + cfg.getPlural().toLowerCase());
        getter.setContentPlain("return\t" + this.dtoListMemberName + ";\n");
        MethodWriter setter = new MethodWriter(Visibility.PUBLIC, "void", "set" + cfg.getPlural());
        setter.setJavadoc("@param " + this.dtoListMemberName + " \n the " + this.dtoListMemberName + " to set   ");
        setter.addParameter("List<" + cfg.getDtoClassName() + ">", this.dtoListMemberName);
        setter.setContentPlain("this." + this.dtoListMemberName + "=" + this.dtoListMemberName + ";\n");
        result.add(getter);
        result.add(setter);
        return result;
    }
}
