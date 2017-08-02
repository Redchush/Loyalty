package dynamusic.system.command.commandimpl.change_list;


import atg.repository.MutableRepository;
import atg.repository.Repository;
import atg.repository.RepositoryItem;

import java.util.Collection;

public class RemoveItemFromListProperty extends ChangeListProperty {

    private static final String FORMAT_ERROR = "Can't add %s to %s";

    public RemoveItemFromListProperty() {}

    public RemoveItemFromListProperty(MutableRepository mainRepository, Repository slaveRepository,
                                 String mainItemType, String slaveItemType) {
        super(mainRepository, slaveRepository, mainItemType, slaveItemType);
    }

    protected void operateOnCollection(Collection<RepositoryItem> collection, RepositoryItem slaveItem) {
        collection.remove(slaveItem);
    }


    public Object getErrorString() {
        return errorString == null ? String.format(FORMAT_ERROR, slaveItemType, mainItemType) : errorString;
    }
}
