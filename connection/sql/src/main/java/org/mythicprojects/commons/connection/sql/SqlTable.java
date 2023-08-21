package org.mythicprojects.commons.connection.sql;

import java.sql.PreparedStatement;
import org.jetbrains.annotations.NotNull;
import org.mythicprojects.commons.connection.sql.util.SqlConsumer;
import org.mythicprojects.commons.util.Validate;

public class SqlTable {

    private final SqlDatabase database;
    private final String name;

    SqlTable(@NotNull SqlDatabase database, @NotNull String name) {
        Validate.notNull(database, "database cannot be null");
        Validate.notEmpty(name, "name cannot be null or empty");
        this.database = database;
        this.name = name;
    }

    public void execute(@NotNull SqlConsumer<PreparedStatement> statementConsumer, @NotNull String query) {
        this.database.executeStatement(statementConsumer, query, this.name);
    }

    public void execute(@NotNull SqlExecutable executable) {
        executable.execute(this.database, this.name);
    }

}
