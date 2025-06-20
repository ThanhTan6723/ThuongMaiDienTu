package dao.client;

import model.Log;
import model.Account;

public abstract class AbsDAO<T extends IModel> implements IDAO<T> {

    @Override
    public Account login(String field, String value, String password, Log log) {
        Logging.login(log);
        return null;
    }

    @Override
    public int insert(T model) {
        Logging.insert(model);
        return 1;
    }

    @Override
    public int update(T model) {
        Logging.update(model);
        return 1;
    }

    @Override
    public int delete(T model) {
        Logging.delete(model);
        return 1;
    }
}
