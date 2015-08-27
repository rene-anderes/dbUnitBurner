package org.anderes.edu.dbunitburner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.sql.Timestamp;

import org.dbunit.dataset.datatype.TypeCastException;
import org.junit.Before;
import org.junit.Test;

public class CustomTimestampDataTypeTest {
    
    private CustomTimestampDataType datatype;
    
    @Before
    public void setup() {
        datatype = new CustomTimestampDataType();
    }
    
    @Test
    public void shouldBeTimestampWithSeconds() throws TypeCastException {
        final Timestamp expectedTimestamp = Timestamp.valueOf("2015-01-22 23:03:20");
        final Timestamp timestamp = datatype.typeCast("22.01.2015 23:03:20");
        
        assertThat(timestamp, is(notNullValue()));
        assertThat(timestamp, is(expectedTimestamp));
    }
    
    @Test
    public void shouldBeTimestampWithoutSeconds() throws TypeCastException {
        final Timestamp expectedTimestamp = Timestamp.valueOf("2015-01-22 23:03:00");
        final Timestamp timestamp = datatype.typeCast("22.01.2015 23:03");
        
        assertThat(timestamp, is(notNullValue()));
        assertThat(timestamp, is(expectedTimestamp));
    }
    
    @Test
    public void shouldBeTimestampWithoutTime() throws TypeCastException {
        final Timestamp expectedTimestamp = Timestamp.valueOf("2015-01-22 00:00:00");
        final Timestamp timestamp = datatype.typeCast("22.01.2015");
        
        assertThat(timestamp, is(notNullValue()));
        assertThat(timestamp, is(expectedTimestamp));
    }

}
