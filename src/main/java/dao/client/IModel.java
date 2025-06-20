package dao.client;

import com.google.gson.JsonObject;

public interface IModel {

    public String getTable();

    public Object getBeforeData();

    public Object getAfterData();
}
