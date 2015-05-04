package org.andmar1x.androidormtests.dbflow;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

/**
 * Created by andmar1x on 5/1/15.
 */
@Table(databaseName = EntryDb.NAME)
public class Entry extends BaseModel {

    @Column(columnType = Column.PRIMARY_KEY)
    public Long longValue;

    @Column
    public Boolean booleanValue;
    @Column
    public Short shortValue;
    @Column
    public Integer intValue;
    @Column
    public Float floatValue;
    @Column
    public Double doubleValue;
    @Column
    public String stringValue;
    @Column
    public Date dateValue;
}
