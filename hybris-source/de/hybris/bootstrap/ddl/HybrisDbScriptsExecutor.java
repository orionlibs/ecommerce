package de.hybris.bootstrap.ddl;

import java.io.Reader;
import java.nio.file.Path;

public interface HybrisDbScriptsExecutor
{
    void executeDDl(Path paramPath);


    void executeDDl(Reader paramReader);


    void executeDropDdl(Path paramPath);


    void executeDropDdl(Reader paramReader);


    void executeDml(Path paramPath);


    void executeDml(Reader paramReader);
}
