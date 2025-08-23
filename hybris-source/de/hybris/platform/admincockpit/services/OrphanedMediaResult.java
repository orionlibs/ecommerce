package de.hybris.platform.admincockpit.services;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class OrphanedMediaResult
{
    private int filesOverall;
    private final Collection<File> orphanedMedias;
    private final Collection<File> notHybrisMedias;


    public OrphanedMediaResult()
    {
        this.filesOverall = 0;
        this.orphanedMedias = new ArrayList<>();
        this.notHybrisMedias = new ArrayList<>();
    }


    public OrphanedMediaResult(int filesOverall, Collection<File> orphanedMedias, Collection<File> notHybrisMedias)
    {
        this.filesOverall = filesOverall;
        this.orphanedMedias = orphanedMedias;
        this.notHybrisMedias = notHybrisMedias;
    }


    public int getFilesOverall()
    {
        return this.filesOverall;
    }


    public Collection<File> getOrphanedMedias()
    {
        return this.orphanedMedias;
    }


    public Collection<File> getNotHybrisMedias()
    {
        return this.notHybrisMedias;
    }


    public void add(OrphanedMediaResult other)
    {
        this.filesOverall += other.getFilesOverall();
        this.orphanedMedias.addAll(other.getOrphanedMedias());
        this.notHybrisMedias.addAll(other.getNotHybrisMedias());
    }
}
