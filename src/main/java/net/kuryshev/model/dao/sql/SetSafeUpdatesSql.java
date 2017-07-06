package net.kuryshev.model.dao.sql;

public class SetSafeUpdatesSql implements Sql {
    private final static String SQL = "SET SQL_SAFE_UPDATES = 0";

    public String generate() {
        return SQL;
    }
}
