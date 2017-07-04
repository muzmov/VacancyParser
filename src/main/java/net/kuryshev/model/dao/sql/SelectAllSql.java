package net.kuryshev.model.dao.sql;

public class SelectAllSql implements Sql {
    private final String SQL = "SELECT * FROM %s";
    private String table;


    public SelectAllSql(String table) {
        this.table = table;
    }

    @Override
    public String generate() {
        return String.format(SQL, table);
    }

}
