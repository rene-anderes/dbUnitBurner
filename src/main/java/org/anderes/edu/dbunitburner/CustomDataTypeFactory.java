package org.anderes.edu.dbunitburner;

import java.sql.Types;

import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.DataTypeException;
import org.dbunit.dataset.datatype.DefaultDataTypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomDataTypeFactory extends DefaultDataTypeFactory {
    
    private Logger logger = LoggerFactory.getLogger(DbUnitRule.class);
    
    public DataType createDataType(int sqlType, String sqlTypeName) throws DataTypeException {
        if (sqlType == Types.TIMESTAMP) {
            logger.debug(String.format("Für den SQL-Type '%s' wird Klasse '%s' eingesetzt.", sqlTypeName, CustomTimestampDataType.class.getName()));
            return new CustomTimestampDataType();
        } else {
            return super.createDataType(sqlType, sqlTypeName);
        }
    }
}
