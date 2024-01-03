package org.mythicprojects.commons.connection.sql.query;

import org.jetbrains.annotations.Contract;

public class SimpleQueryBuilder extends AbstractQueryBuilder<SimpleQueryBuilder> {

    private SimpleQueryBuilder() {
    }

    @Contract(" -> new")
    public static SimpleQueryBuilder create() {
        return new SimpleQueryBuilder();
    }

}
