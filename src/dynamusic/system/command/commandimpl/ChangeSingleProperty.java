package dynamusic.system.command.commandimpl;


import atg.repository.MutableRepositoryItem;
import atg.repository.RepositoryItem;

public class ChangeSingleProperty extends ChangeItemProperty {

    private static final String FORMAT_ERROR = "Can't set in %s.%s value of %s with id=%s";

    @Override
    public void operate(MutableRepositoryItem mainItem, RepositoryItem slaveItem) {
        mainItem.setPropertyValue(mainItemSlaveProperty, slaveItem);
    }

    @Override
    public Object getErrorString() {
        return String.format(FORMAT_ERROR, getMainItemType(), getMainItemSlaveProperty(), getSlaveItemType(),
                getSlaveItemIdValue());
    }
}
