package net.kuryshev.model.dao.sql;

public class DeleteSql implements Sql {
    private final String SQL = "DELETE FROM %s";
    private String table;


    public DeleteSql(String table) {
        this.table = table;
    }

    @Override
    public String generate() {
        return String.format(SQL, table);
    }
}
