package dao.client;

import model.Log;
import model.Account;

public interface IDAO<T> {
    Account login(String field, String value, String password, Log log);
    int insert(T t);
    int update(T t);
    int delete(T t);


}
