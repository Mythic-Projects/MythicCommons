package org.mythicprojects.commons.connection.sql.query;

public class SimpleQueryBuilder extends AbstractQueryBuilder<SimpleQueryBuilder> {

    private SimpleQueryBuilder() {
    }

    public static SimpleQueryBuilder create() {
        return new SimpleQueryBuilder();
    }

}
