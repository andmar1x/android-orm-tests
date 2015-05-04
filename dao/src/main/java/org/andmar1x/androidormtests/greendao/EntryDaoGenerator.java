package org.andmar1x.androidormtests.greendao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class EntryDaoGenerator {

    private static final String PROJECT_DIR = System.getProperty("user.dir").replace("\\", "/");
    private static final String OUT_DIR = PROJECT_DIR + "/app/src/main/java";

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "org.andmar1x.androidormtests.greendao");

        Entity entry = schema.addEntity("Entry");
        entry.addIdProperty().primaryKey().autoincrement();
        entry.addBooleanProperty("booleanValue");
        entry.addByteArrayProperty("blobValue");
        entry.addShortProperty("shortValue");
        entry.addIntProperty("intValue");
        entry.addLongProperty("longValue");
        entry.addFloatProperty("floatValue");
        entry.addDoubleProperty("doubleValue");
        entry.addStringProperty("stringValue");
        entry.addDateProperty("dateValue");

        new DaoGenerator().generateAll(schema, OUT_DIR);
    }
}