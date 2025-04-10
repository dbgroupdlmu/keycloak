/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.connections.jpa.updater.liquibase;

import liquibase.Scope;
import liquibase.database.DatabaseConnection;
import liquibase.database.core.KingBase8Database;
import liquibase.exception.DatabaseException;
import liquibase.executor.ExecutorService;
import liquibase.statement.core.RawSqlStatement;

/**
 * @author <a href="mailto:mposolda@redhat.com">Marek Posolda</a>
 */
public class KingbaseES extends KingBase8Database {

    public static final String KINGBASEES_PRODUCT_NAME = "kingbasees";

    @Override
    public String getShortName() {
        return "kingbasees";
    }

    @Override
    protected String getDefaultDatabaseProductName() {
        return KINGBASEES_PRODUCT_NAME;
    }

    @Override
    public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
        return KINGBASEES_PRODUCT_NAME.equalsIgnoreCase(conn.getDatabaseProductName());
    }

    @Override
    public String getDefaultDriver(String url) {
        String defaultDriver = super.getDefaultDriver(url);

        if (defaultDriver == null) {

                defaultDriver = "com.kingbase8.Driver";

        }

        return defaultDriver;
    }

    @Override
    protected String getConnectionSchemaName() {
        try {
            return Scope.getCurrentScope().getSingleton(ExecutorService.class).getExecutor(LiquibaseConstants.JDBC_EXECUTOR, this)
                    .queryForObject(new RawSqlStatement("select current_schema()"), String.class);

        } catch (Exception e) {
            throw new RuntimeException("Failed to get current schema", e);
        }
    }

}
