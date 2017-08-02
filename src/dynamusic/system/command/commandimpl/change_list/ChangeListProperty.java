package dynamusic.system.command.commandimpl.change_list;


import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.Repository;
import atg.repository.RepositoryItem;
import dynamusic.system.command.commandimpl.ChangeItemProperty;

import java.util.Collection;

public abstract class ChangeListProperty extends ChangeItemProperty {

    public ChangeListProperty() {}

    public ChangeListProperty(MutableRepository mainRepository, Repository slaveRepository,
                              String mainItemType, String slaveItemType) {
        super(mainRepository, slaveRepository, mainItemType, slaveItemType);
    }

    @Override
    protected void operate(MutableRepositoryItem mainItem, RepositoryItem slaveItem) {
        Collection<RepositoryItem>
                collection = (Collection<RepositoryItem>) mainItem.getPropertyValue(mainItemSlaveProperty);
        operateOnCollection(collection, slaveItem);
    }

    protected abstract void operateOnCollection(Collection<RepositoryItem> collection, RepositoryItem slaveItem);


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ChangeListProperty{");

        sb.append("mainRepository=").append(mainRepository);
        sb.append(", slaveRepository=").append(slaveRepository);
        sb.append(", mainItemType='").append(mainItemType).append('\'');
        sb.append(", mainItemSlaveProperty='").append(mainItemSlaveProperty).append('\'');
        sb.append(", slaveItemType='").append(slaveItemType).append('\'');
        sb.append(", mainItemIdValue='").append(mainItemIdValue).append('\'');
        sb.append(", slaveItemIdValue='").append(slaveItemIdValue).append('\'');
        sb.append(", errorString='").append(errorString).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
