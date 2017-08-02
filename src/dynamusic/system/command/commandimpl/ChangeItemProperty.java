package dynamusic.system.command.commandimpl;


import atg.dtm.TransactionDemarcationException;
import atg.repository.*;
import dynamusic.system.command.RepositoryCommand;

public abstract class ChangeItemProperty implements RepositoryCommand {
    protected MutableRepository mainRepository;
    protected Repository slaveRepository;

    protected String mainItemType;
    protected String mainItemSlaveProperty;
    protected String slaveItemType;

    protected String mainItemIdValue;
    protected String slaveItemIdValue;

    protected String errorString;

    public ChangeItemProperty() {}

    public ChangeItemProperty(MutableRepository mainRepository, Repository slaveRepository,
                              String mainItemType, String slaveItemType) {
        this.mainRepository = mainRepository;
        this.slaveRepository = slaveRepository;
        this.mainItemType = mainItemType;
        this.slaveItemType = slaveItemType;
    }

    public Object execute() throws TransactionDemarcationException, RepositoryException {
        MutableRepositoryItem mainItem = mainRepository.getItemForUpdate(mainItemIdValue, mainItemType);
        RepositoryItem slaveItem = slaveRepository.getItem(slaveItemIdValue, slaveItemType);
        operate(mainItem, slaveItem);
//        mainItem.setPropertyValue(mainItemSlaveProperty, collection);
        mainRepository.updateItem(mainItem);
        return mainItem;
    }

    protected abstract void operate(MutableRepositoryItem mainItem, RepositoryItem slaveItem);

    public Object getErrorString() {
        return errorString;
    }
    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    public MutableRepository getMainRepository() {
        return mainRepository;
    }

    public void setMainRepository(MutableRepository mainRepository) {
        this.mainRepository = mainRepository;
    }

    public Repository getSlaveRepository() {
        return slaveRepository;
    }

    public void setSlaveRepository(Repository slaveRepository) {
        this.slaveRepository = slaveRepository;
    }

    public String getMainItemType() {
        return mainItemType;
    }

    public void setMainItemType(String mainItemType) {
        this.mainItemType = mainItemType;
    }

    public String getMainItemIdValue() {
        return mainItemIdValue;
    }

    public void setMainItemIdValue(String mainItemIdValue) {
        this.mainItemIdValue = mainItemIdValue;
    }

    public String getMainItemSlaveProperty() {
        return mainItemSlaveProperty;
    }

    public void setMainItemSlaveProperty(String mainItemSlaveProperty) {
        this.mainItemSlaveProperty = mainItemSlaveProperty;
    }

    public String getSlaveItemType() {
        return slaveItemType;
    }

    public void setSlaveItemType(String slaveItemType) {
        this.slaveItemType = slaveItemType;
    }

    public String getSlaveItemIdValue() {
        return slaveItemIdValue;
    }

    public void setSlaveItemIdValue(String slaveItemIdValue) {
        this.slaveItemIdValue = slaveItemIdValue;
    }

}
