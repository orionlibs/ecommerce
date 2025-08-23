package de.hybris.bootstrap.codegenerator;

import java.util.StringTokenizer;

public class PackageInfoWriter extends ClassWriter
{
    public PackageInfoWriter(CodeGenerator gen, String packageName)
    {
        super(gen, null, "package-info", packageName);
    }


    public void write(JavaFile file)
    {
        if(getCopyright() != null)
        {
            file.add("/*");
            for(StringTokenizer st = new StringTokenizer(getCopyright(), "\n"); st.hasMoreTokens(); )
            {
                file.add(" * " + st.nextToken());
            }
            file.add(" */");
        }
        if(getJavadoc() != null)
        {
            file.add("/**");
            for(StringTokenizer st = new StringTokenizer(getJavadoc(), "\n"); st.hasMoreTokens(); )
            {
                file.add(" * " + st.nextToken());
            }
            file.add(" */");
        }
        if(getPackageName() != null)
        {
            file.add("package " + getPackageName() + ";");
            file.add("");
        }
    }
}
