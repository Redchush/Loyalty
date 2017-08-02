package dynamusic.system.command.commandimpl;


import atg.dtm.TransactionDemarcationException;
import atg.repository.Repository;
import atg.repository.RepositoryException;
import atg.repository.RepositoryItem;
import atg.repository.RepositoryView;
import atg.repository.rql.RqlStatement;
import dynamusic.system.command.RepositoryCommand;

import java.util.Arrays;
import java.util.List;



public class GetItemListByQuery implements RepositoryCommand {

    private static final String FORMAT_ERROR = "Can't execute query %s";

    private Repository repository;
    private String itemType;
    private RqlStatement rqlStatement;
    private Object[] params;

    public GetItemListByQuery() {
    }

    public GetItemListByQuery(Repository repository, String itemType,
                              RqlStatement rqlStatement, Object[] params) {
        this.repository = repository;
        this.itemType = itemType;
        this.rqlStatement = rqlStatement;
        this.params = params;
    }

    public List<RepositoryItem> execute() throws TransactionDemarcationException, RepositoryException {
        RepositoryView loyaltyView = repository.getView(itemType);
        RepositoryItem[] items = rqlStatement.executeQuery(loyaltyView, params);
        return Arrays.asList(items);
    }

    public Object getErrorString() {
        return String.format(FORMAT_ERROR, rqlStatement.getQuery());
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public RqlStatement getRqlStatement() {
        return rqlStatement;
    }

    public void setRqlStatement(RqlStatement rqlStatement) {
        this.rqlStatement = rqlStatement;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}
