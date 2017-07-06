package net.kuryshev.model.dao.sql;

/**
 * Generates SELECT sql according this template:
 * SELECT * FROM table WHERE columns[0] IS LIKE 'filters[0]' OR columns[1] IS LIKE 'filters[1]' ...
 * There will be no WHERE clause if columns or filters are empty
 */

public class SelectSql implements Sql {
    private final String SQL = "SELECT * FROM %s";
    private String table;
    private String[] columns, filters;

    public SelectSql(String table, String[] columns, String[] filters) {
        if (columns == null || filters == null || filters.length != columns.length) throw new IllegalArgumentException();
        this.columns = columns;
        this.filters = filters;
        this.table = table;
    }

    @Override
    public String generate() {
        String result =  String.format(SQL, table);
        if (columns.length != 0 ) {
            result += " WHERE";
            for (int i = 0; i < columns.length - 1; i++) {
                result += " " + columns[i] + " LIKE '" + filters[i] + "' OR";
            }
            result += " " + columns[columns.length - 1] + " LIKE '" + filters[filters.length - 1] + "'";
        }
        return result;
    }

}
