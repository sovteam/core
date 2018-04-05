package sov.db;

import java.util.List;
import java.util.Map;

public interface Database {

    void createTable(String table, List<String> columnSpecs);

    void insert(String table, Map record);

    void update(String table, String pkColumn, String pkValue, Map record);

    /** @return Column names in the first array, record data in the subsequent arrays. */
    Object[][] select(String table, String column, String value);

}