package net.kuryshev.model.dao.sql;

public class InsertSql implements Sql {
    private static final String INSERT_VACANCY_SQL = "INSERT INTO Vacancies (title, description, url, site_name, city, company, salary, rating) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s', %s)";
    private static final String INSERT_COMPANY_SQL = "INSERT INTO Companies (name, url, rating, reviews_url) VALUES ('%s', '%s', %s, '%s')";
    private static final String COMPANIES_TABLE = "Companies";
    private static final String VACANCIES_TABLE = "Vacancies";
    private String table;
    private String[] values;

    public InsertSql(String table, String[] values) {
        if (!table.equals(VACANCIES_TABLE) && !table.equals(COMPANIES_TABLE)) throw new IllegalArgumentException();
        if (table.equals(VACANCIES_TABLE) && (values == null || values.length != 8)) throw new IllegalArgumentException();
        if (table.equals(COMPANIES_TABLE) && (values == null || values.length != 4)) throw new IllegalArgumentException();
        this.table = table;
        this.values = values;
    }

    public String generate() {
        if (table.equals(VACANCIES_TABLE))
            return String.format(INSERT_VACANCY_SQL, values[0], values[1], values[2],
                    values[3], values[4], values[5], values[6], values[7]);
        if (table.equals(COMPANIES_TABLE))
            return String.format(INSERT_COMPANY_SQL, values[0], values[1], values[2], values[3]);
        return "";
    }
}
