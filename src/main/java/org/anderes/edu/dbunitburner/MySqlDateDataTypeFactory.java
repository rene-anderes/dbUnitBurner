package org.anderes.edu.dbunitburner;

import java.util.Optional;

import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.ext.mysql.MySqlDataTypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mittels dieser Data-Type-Factory ist es möglich ein Zeitstempel in folgenden Formaten im Datenbankfile abzulegen:<br>
 * dd.MM.yyyy HH:mm:ss     (Nanosekunden sind 0)<br>
 * dd.MM.yyyy HH:mm        (Sekunden und Nanosekunden sind 0)<br>
 * dd.MM.yyyy              (Stunden, Minuten, Sekunden und Nanosekunden sind 0)<br>
 * <p>
 * Diese Data-Type-Factory kann der {@link DbUnitRule} im Konstruktor angegeben werden.
 * 
 * @author René Anderes
 *
 */
public class MySqlDateDataTypeFactory extends MySqlDataTypeFactory {

    private Logger logger = LoggerFactory.getLogger(MySqlDateDataTypeFactory.class);
    
    @Override
    public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException {
        Optional<DataType> optionalDatType = DbUnitBurnerHelper.createDataType(sqlType, sqlTypeName);
        if (optionalDatType.isPresent()) {
            final DataType dataType = optionalDatType.get();
            logger.debug(String.format("Für den SQL-Type '%s' wird Klasse '%s' eingesetzt.", sqlTypeName, dataType.getClass().getName()));
            return dataType;
        }
        return super.createDataType(sqlType, sqlTypeName);
    }
    
}
