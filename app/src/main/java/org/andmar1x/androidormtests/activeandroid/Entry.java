package org.andmar1x.androidormtests.activeandroid;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by andmar1x on 5/1/15.
 */
@Table(name = Entry.TABLE_NAME)
public class Entry extends Model {

    public static final String TABLE_NAME = "entry";

    @Column(name = "longValue")
    public Long longValue;

    @Column(name = "booleanValue")
    public Boolean booleanValue;
    @Column(name = "shortValue")
    public Short shortValue;
    @Column(name = "intValue")
    public Integer intValue;
    @Column(name = "floatValue")
    public Float floatValue;
    @Column(name = "doubleValue")
    public Double doubleValue;
    @Column(name = "stringValue")
    public String stringValue;
    @Column(name = "dateValue")
    public Date dateValue;
}
