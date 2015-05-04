package org.andmar1x.androidormtests.ormlite;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Created by andmar1x on 5/4/15.
 */
public class Entry {

    @DatabaseField(generatedId = true)
    public Long longValue;

    @DatabaseField
    public Boolean booleanValue;
    @DatabaseField
    public Short shortValue;
    @DatabaseField
    public Integer intValue;
    @DatabaseField
    public Float floatValue;
    @DatabaseField
    public Double doubleValue;
    @DatabaseField
    public String stringValue;
    @DatabaseField
    public Date dateValue;
}
