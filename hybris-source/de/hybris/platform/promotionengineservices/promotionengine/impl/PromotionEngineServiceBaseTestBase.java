package de.hybris.platform.promotionengineservices.promotionengine.impl;

import de.hybris.platform.ruleengine.dao.DroolsKIEBaseDao;
import de.hybris.platform.ruleengine.model.DroolsKIEBaseModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.Resource;
import org.apache.commons.io.IOUtils;

public class PromotionEngineServiceBaseTestBase extends ServicelayerTest
{
    @Resource
    private DroolsKIEBaseDao droolsKIEBaseDao;


    protected String readRuleFile(String fileName, String path) throws IOException
    {
        Path rulePath = Paths.get(getApplicationContext().getResource("classpath:" + path + fileName).getURI());
        InputStream is = Files.newInputStream(rulePath, new java.nio.file.OpenOption[0]);
        StringWriter writer = new StringWriter();
        IOUtils.copy(is, writer, Charset.forName("UTF-8"));
        return writer.toString();
    }


    protected DroolsKIEBaseModel getKieBaseModel(String kieBaseName)
    {
        return (DroolsKIEBaseModel)getDroolsKIEBaseDao().findAllKIEBases().stream().filter(b -> b.getName().equals(kieBaseName)).findFirst()
                        .orElseThrow(() -> new IllegalStateException("KieBaseModel with name " + kieBaseName + " was not found. Check your test setup"));
    }


    protected DroolsKIEBaseDao getDroolsKIEBaseDao()
    {
        return this.droolsKIEBaseDao;
    }
}
