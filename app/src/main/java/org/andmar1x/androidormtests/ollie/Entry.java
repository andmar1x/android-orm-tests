package org.andmar1x.androidormtests.ollie;

import java.util.Date;

import ollie.Model;
import ollie.annotation.Column;
import ollie.annotation.Table;

/**
 * Created by andmar1x on 5/1/15.
 */
@Table(Entry.TABLE_NAME)
public class Entry extends Model {

    public static final String TABLE_NAME = "entry";

    @Column("longValue")
    public Long longValue;

    @Column("booleanValue")
    public Boolean booleanValue;
    @Column("shortValue")
    public Short shortValue;
    @Column("intValue")
    public Integer intValue;
    @Column("floatValue")
    public Float floatValue;
    @Column("doubleValue")
    public Double doubleValue;
    @Column("stringValue")
    public String stringValue;
    @Column("dateValue")
    public Date dateValue;
}
