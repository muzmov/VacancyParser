package net.kuryshev.model.dao.sql;

public class DeleteSql implements Sql {
    private final String SQL = "DELETE FROM %s";
    private String table;
    private String connector = "";
    private String[] columns = {};
    private String[] filters = {};

    public DeleteSql(String table) {
        this.table = table;
    }

    public void setFilters(String[] columns, String[] filters, String connector) {
        if (columns == null || filters == null || filters.length != columns.length) throw new IllegalArgumentException();
        if (!(connector.equals("OR") || connector.equals("AND"))) throw new IllegalArgumentException();
        this.columns = columns;
        this.filters = filters;
        this.connector = connector;
    }

    @Override
    public String generate() {
        String result =  String.format(SQL, table);
        if (columns.length != 0 ) {
            result += " WHERE";
            for (int i = 0; i < columns.length - 1; i++) {
                result +=  " " + columns[i] + " LIKE '" + filters[i] + "'" + " " + connector;
            }
            result += " " + columns[columns.length - 1] + " LIKE '" + filters[filters.length - 1] + "'";
        }
        return result;
    }
}
