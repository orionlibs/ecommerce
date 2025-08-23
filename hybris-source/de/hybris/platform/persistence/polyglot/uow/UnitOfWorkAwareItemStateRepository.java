package de.hybris.platform.persistence.polyglot.uow;

import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import de.hybris.platform.persistence.polyglot.PolyglotFeature;
import de.hybris.platform.persistence.polyglot.model.ChangeSet;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.ItemState;
import de.hybris.platform.persistence.polyglot.search.FindResult;
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;
import java.util.Objects;
import java.util.Optional;

public class UnitOfWorkAwareItemStateRepository implements ItemStateRepository
{
    private final ItemStateRepository targetRepo;
    private final UnitOfWorkProvider uowProvider;


    public UnitOfWorkAwareItemStateRepository(ItemStateRepository targetRepo, UnitOfWorkProvider uowProvider)
    {
        this.targetRepo = Objects.<ItemStateRepository>requireNonNull(targetRepo);
        this.uowProvider = Objects.<UnitOfWorkProvider>requireNonNull(uowProvider);
    }


    public ItemState get(Identity id)
    {
        Optional<UnitOfWork> uow = this.uowProvider.getUnitOfWork();
        if(uow.isPresent())
        {
            return ((UnitOfWork)uow.get()).get(this.targetRepo, id);
        }
        return this.targetRepo.get(id);
    }


    public ChangeSet beginCreation(Identity id)
    {
        Optional<UnitOfWork> uow = this.uowProvider.getUnitOfWork();
        if(uow.isPresent())
        {
            return ((UnitOfWork)uow.get()).beginCreation(this.targetRepo, id);
        }
        return this.targetRepo.beginCreation(id);
    }


    public void store(ChangeSet changeSet)
    {
        Optional<UnitOfWork> uow = this.uowProvider.getUnitOfWork();
        if(uow.isPresent())
        {
            ((UnitOfWork)uow.get()).store(this.targetRepo, changeSet);
            return;
        }
        this.targetRepo.store(changeSet);
    }


    public void remove(ItemState state)
    {
        Optional<UnitOfWork> uow = this.uowProvider.getUnitOfWork();
        if(uow.isPresent())
        {
            ((UnitOfWork)uow.get()).remove(this.targetRepo, state);
            return;
        }
        this.targetRepo.remove(state);
    }


    public FindResult find(Criteria criteria)
    {
        Optional<UnitOfWork> uow = this.uowProvider.getUnitOfWork();
        if(uow.isPresent())
        {
            return ((UnitOfWork)uow.get()).find(this.targetRepo, criteria);
        }
        return this.targetRepo.find(criteria);
    }


    public boolean isSupported(PolyglotFeature feautre)
    {
        return this.targetRepo.isSupported(feautre);
    }


    public String toString()
    {
        Optional<UnitOfWork> uow = this.uowProvider.getUnitOfWork();
        return getClass().getSimpleName() + ": " + getClass().getSimpleName();
    }
}
