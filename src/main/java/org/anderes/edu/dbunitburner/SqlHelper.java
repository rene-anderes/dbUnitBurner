package org.anderes.edu.dbunitburner;

import static org.apache.commons.lang3.StringUtils.substringBeforeLast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.Validate;

/**
 * SQL-Helper für SQL-Kommandos
 * 
 * @author René Anderes
 *
 */
public abstract class SqlHelper {

    /**
     * Diese Methode extrahiert SQL-Kommandos aus dem übergebenen SQL-File.
     * Jede Zeile entspricht einem SQl-Kommando. 
     * @param sqlFilePath Pfad des SQL-Files
     * @return Liste von SQL-Kommandos
     * @throws IOException wenn das File nicht gelesen werden konnte
     */
    public static List<String> extractSqlCommands(final Path sqlFilePath) throws IOException {
        Validate.notNull(sqlFilePath);
        
        final InputStream is = ClassLoader.getSystemResourceAsStream(sqlFilePath.toString());
        if (is == null) {
            final String msg = "Could not find file named = " + sqlFilePath;
            throw new IOException(msg);
        }
        
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8.name()));
        final List<String> commands = new ArrayList<String>();
        
        String line = null;
        while((line = bufferedReader.readLine()) != null) {
            commands.add(substringBeforeLast(line, ";"));
        }
        bufferedReader.close();
        return commands;
    }
    
    public static int[] execute(final Connection connection, final List<String> queries) throws SQLException {
        Validate.notNull(connection);
        Validate.notNull(queries);
        
        return execute(connection, queries.toArray(new String[]{}));
    }

    public static int[] execute(final Connection connection, final String... queries) throws SQLException {
        Validate.notNull(connection);
        Validate.notNull(queries);
        
        final Statement stmt = connection.createStatement();
        for(String query : queries) {
            stmt.addBatch(query);
        }
        return stmt.executeBatch();
    }
    
}
