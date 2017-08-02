package dynamusic.system.command.commandimpl.change_list;


import atg.repository.*;

import java.util.Collection;

public class AddItemToListProperty extends ChangeListProperty {

    private static final String FORMAT_ERROR = "Can't add %s to %s";

    public AddItemToListProperty() {}

    public AddItemToListProperty(MutableRepository mainRepository, Repository slaveRepository,
                                 String mainItemType, String slaveItemType) {
        super(mainRepository, slaveRepository, mainItemType, slaveItemType);
    }

    protected void operateOnCollection(Collection<RepositoryItem> collection, RepositoryItem slaveItem) {
        collection.add(slaveItem);
    }

    public Object getErrorString() {
        return errorString == null ? String.format(FORMAT_ERROR, slaveItemType, mainItemType) : errorString;
    }

}
