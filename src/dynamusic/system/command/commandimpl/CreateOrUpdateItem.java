package dynamusic.system.command.commandimpl;


import atg.dtm.TransactionDemarcationException;
import atg.nucleus.logging.ApplicationLoggingImpl;
import atg.repository.MutableRepository;
import atg.repository.MutableRepositoryItem;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import dynamusic.system.command.RepositoryCommand;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;

public class CreateOrUpdateItem extends ApplicationLoggingImpl implements RepositoryCommand {

    private static final String FORMAT_ERROR = "Can't create item %s with properties\n " +
            "from map: %s,\n from dictionary: %s";

    private MutableRepository repository;
    private String descriptorName;
    private String createId;
    private String updateId;

    private Map<String, Object> mapProperties;

    private Dictionary<String, Object> dictionaryProperties;
    private boolean collectFromAll;

    public CreateOrUpdateItem() {}

    public CreateOrUpdateItem(MutableRepository repository, String descriptorName) {
        this.repository = repository;
        this.descriptorName = descriptorName;
    }

    public RepositoryItem execute() throws TransactionDemarcationException, RepositoryException {
        MutableRepositoryItem item = null;
        if (getUpdateId() == null)
        {
            item = getCreateId() == null ?  this.getRepository().createItem(getDescriptorName())
                                         :  this.getRepository().createItem(getCreateId(), getDescriptorName());
            assignProperties(item);
            return getRepository().addItem(item);
        } else
        {
            item = getRepository().getItemForUpdate(getUpdateId(), getDescriptorName());
            assignProperties(item);
            getRepository().updateItem(item);
            return item;
        }
    }

    public Object getErrorString() {
        return String.format(FORMAT_ERROR,
                descriptorName, mapProperties == null ? null : mapProperties.toString(), dictionaryProperties == null
                                                                                         ? null : dictionaryProperties.toString());
    }

    protected void assignProperties(MutableRepositoryItem item){
        if (getMapProperties() != null){
            assignPropertiesFromMap(item);
            if (isCollectFromAll()){
                assignPropertiesFromDictionary(item);
            }
        } else {
            if (getDictionaryProperties() != null){
                assignPropertiesFromDictionary(item);
            }
        }
    }


    private void assignPropertiesFromMap(MutableRepositoryItem item){
        for(Map.Entry<String, Object> entry : getMapProperties().entrySet()){
            item.setPropertyValue(entry.getKey(), entry.getValue());
        }
    }

    private void assignPropertiesFromDictionary(MutableRepositoryItem item){
        Enumeration<String> keys = getDictionaryProperties().keys();
        while(keys.hasMoreElements()) {
            String descriptor = keys.nextElement();
            item.setPropertyValue(descriptor, getDictionaryProperties().get(descriptor));
        }
    }

    public MutableRepository getRepository() {
        return repository;
    }

    public void setRepository(MutableRepository repository) {
        this.repository = repository;
    }

    public String getDescriptorName() {
        return descriptorName;
    }

    public void setDescriptorName(String descriptorName) {
        this.descriptorName = descriptorName;
    }

    public Map<String, Object> getMapProperties() {
        return mapProperties;
    }

    public void setMapProperties(Map<String, Object> mapProperties) {
        this.mapProperties = mapProperties;
    }

    public Dictionary<String, Object> getDictionaryProperties() {
        return dictionaryProperties;
    }

    public void setDictionaryProperties(Dictionary<String, Object> dictionaryProperties) {
        this.dictionaryProperties = dictionaryProperties;
    }

    public boolean isCollectFromAll() {
        return collectFromAll;
    }

    public void setCollectFromAll(boolean collectFromAll) {
        this.collectFromAll = collectFromAll;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CreateOrUpdateItem{");
        sb.append(super.toString()).append(" ");

        sb.append("repository=").append(repository);
        sb.append(", descriptorName='").append(descriptorName).append('\'');
        sb.append(", createId='").append(createId).append('\'');
        sb.append(", updateId='").append(updateId).append('\'');
        sb.append(", mapProperties=").append(mapProperties);
        sb.append(", dictionaryProperties=").append(dictionaryProperties);
        sb.append(", collectFromAll=").append(collectFromAll);
        sb.append('}');
        return sb.toString();
    }
}
