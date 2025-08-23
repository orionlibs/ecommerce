package de.hybris.platform.hac.data.form;

import org.springframework.web.multipart.MultipartFile;

public class ScriptFileFormData
{
    private String type;
    private String name;
    private MultipartFile file;


    public String getType()
    {
        return this.type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public MultipartFile getFile()
    {
        return this.file;
    }


    public void setFile(MultipartFile file)
    {
        this.file = file;
    }
}
