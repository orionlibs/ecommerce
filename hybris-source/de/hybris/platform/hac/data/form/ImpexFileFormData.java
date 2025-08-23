package de.hybris.platform.hac.data.form;

import org.springframework.web.multipart.MultipartFile;

public class ImpexFileFormData extends ImpexContentFormData
{
    private MultipartFile file;


    public MultipartFile getFile()
    {
        return this.file;
    }


    public void setFile(MultipartFile file)
    {
        this.file = file;
    }
}
