package de.hybris.bootstrap.codegenerator.model.enums;

import de.hybris.bootstrap.codegenerator.CodeGenerator;
import de.hybris.bootstrap.codegenerator.JavaFile;
import de.hybris.bootstrap.codegenerator.MethodWriter;
import de.hybris.bootstrap.typesystem.YEnumType;
import de.hybris.bootstrap.typesystem.YExtension;
import java.util.Iterator;
import java.util.StringTokenizer;

public class JavaEnumWriter extends AbstractEnumWriter
{
    public JavaEnumWriter(CodeGenerator gen, YExtension ext, YEnumType myEnum)
    {
        super(gen, ext, myEnum);
    }


    protected void writeSignature(JavaFile file)
    {
        StringBuilder interfaceBuilder = new StringBuilder();
        if(getInterfaces() != null)
        {
            interfaceBuilder.append(" implements ");
            for(String newInterface : getInterfaces())
            {
                interfaceBuilder.append(newInterface).append(",");
            }
            interfaceBuilder.deleteCharAt(interfaceBuilder.length() - 1);
        }
        file.add("" + getVisibility() + getVisibility() + " enum " + MethodWriter.modifiersToString(getModifiers()) + getClassName());
        file.startBlock();
        if(getEnumValues().isEmpty())
        {
            file.add("// This is a generated class.");
        }
        else
        {
            for(Iterator<EnumWriterValue> iter = getEnumValues().iterator(); iter.hasNext(); )
            {
                EnumWriterValue enumWriterValue = iter.next();
                if(enumWriterValue.getJavadoc() != null)
                {
                    file.add("/**");
                    for(StringTokenizer st = new StringTokenizer(enumWriterValue.getJavadoc(), "\n"); st.hasMoreTokens(); )
                    {
                        file.add(" * " + st.nextToken());
                    }
                    file.add(" */");
                }
                file.add(enumWriterValue.getCode().toUpperCase() + "(\"" + enumWriterValue.getCode().toUpperCase() + "\")" + enumWriterValue.getCode());
            }
            file.add(" ");
        }
    }
}
