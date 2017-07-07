package net.kuryshev.model.dao.sql;


public class SelectSql implements Sql {
    private final String SQL = "SELECT * FROM %s";
    private String table;
    private String connector = "";
    private String[] columns = {};
    private String[] filters = {};

    public SelectSql(String table) {
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
                result +=  generateFilter(columns[i], filters[i]) + " " + connector;
            }
            result += generateFilter(columns[columns.length - 1], filters[filters.length - 1]);
        }
        return result;
    }

    private String generateFilter(String column, String filter) {
        if (!filter.matches("%.*%")) return " " + column + " LIKE '" + filter + "'";
        else {
            filter = filter.substring(1, filter.length() - 1);
            String[] filterWords = filter.split(" ");
            String result = "";
            for (int i = 0; i < filterWords.length - 1; i++) {
                result += " " + column + " LIKE '%" + filterWords[i] + "%' AND";
            }
            result += " " + column + " LIKE '%" + filterWords[filterWords.length - 1] + "%'";
            return result;
        }
    }
}
