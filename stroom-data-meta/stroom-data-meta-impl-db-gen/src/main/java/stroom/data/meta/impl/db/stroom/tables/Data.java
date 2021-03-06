/*
 * This file is generated by jOOQ.
 */
package stroom.data.meta.impl.db.stroom.tables;


import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.Index;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import stroom.data.meta.impl.db.stroom.Indexes;
import stroom.data.meta.impl.db.stroom.Keys;
import stroom.data.meta.impl.db.stroom.Stroom;
import stroom.data.meta.impl.db.stroom.tables.records.DataRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Data extends TableImpl<DataRecord> {

    private static final long serialVersionUID = 271862828;

    /**
     * The reference instance of <code>stroom.data</code>
     */
    public static final Data DATA = new Data();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<DataRecord> getRecordType() {
        return DataRecord.class;
    }

    /**
     * The column <code>stroom.data.id</code>.
     */
    public final TableField<DataRecord, Long> ID = createField("id", org.jooq.impl.SQLDataType.BIGINT.nullable(false).identity(true), this, "");

    /**
     * The column <code>stroom.data.create_time</code>.
     */
    public final TableField<DataRecord, Long> CREATE_TIME = createField("create_time", org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * The column <code>stroom.data.effective_time</code>.
     */
    public final TableField<DataRecord, Long> EFFECTIVE_TIME = createField("effective_time", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>stroom.data.parent_id</code>.
     */
    public final TableField<DataRecord, Long> PARENT_ID = createField("parent_id", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>stroom.data.status</code>.
     */
    public final TableField<DataRecord, Byte> STATUS = createField("status", org.jooq.impl.SQLDataType.TINYINT.nullable(false), this, "");

    /**
     * The column <code>stroom.data.status_time</code>.
     */
    public final TableField<DataRecord, Long> STATUS_TIME = createField("status_time", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>stroom.data.task_id</code>.
     */
    public final TableField<DataRecord, Long> TASK_ID = createField("task_id", org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>stroom.data.feed_id</code>.
     */
    public final TableField<DataRecord, Integer> FEED_ID = createField("feed_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>stroom.data.type_id</code>.
     */
    public final TableField<DataRecord, Integer> TYPE_ID = createField("type_id", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>stroom.data.processor_id</code>.
     */
    public final TableField<DataRecord, Integer> PROCESSOR_ID = createField("processor_id", org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>stroom.data</code> table reference
     */
    public Data() {
        this(DSL.name("data"), null);
    }

    /**
     * Create an aliased <code>stroom.data</code> table reference
     */
    public Data(String alias) {
        this(DSL.name(alias), DATA);
    }

    /**
     * Create an aliased <code>stroom.data</code> table reference
     */
    public Data(Name alias) {
        this(alias, DATA);
    }

    private Data(Name alias, Table<DataRecord> aliased) {
        this(alias, aliased, null);
    }

    private Data(Name alias, Table<DataRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Data(Table<O> child, ForeignKey<O, DataRecord> key) {
        super(child, key, DATA);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Stroom.STROOM;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Index> getIndexes() {
        return Arrays.<Index>asList(Indexes.DATA_DATA_FEED_ID, Indexes.DATA_DATA_PROCESSOR_ID, Indexes.DATA_DATA_YPE_ID, Indexes.DATA_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<DataRecord, Long> getIdentity() {
        return Keys.IDENTITY_DATA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<DataRecord> getPrimaryKey() {
        return Keys.KEY_DATA_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<DataRecord>> getKeys() {
        return Arrays.<UniqueKey<DataRecord>>asList(Keys.KEY_DATA_PRIMARY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<DataRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<DataRecord, ?>>asList(Keys.DATA_FEED_ID, Keys.DATA_YPE_ID, Keys.DATA_PROCESSOR_ID);
    }

    public DataFeed dataFeed() {
        return new DataFeed(this, Keys.DATA_FEED_ID);
    }

    public DataType dataType() {
        return new DataType(this, Keys.DATA_YPE_ID);
    }

    public DataProcessor dataProcessor() {
        return new DataProcessor(this, Keys.DATA_PROCESSOR_ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Data as(String alias) {
        return new Data(DSL.name(alias), this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Data as(Name alias) {
        return new Data(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Data rename(String name) {
        return new Data(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Data rename(Name name) {
        return new Data(name, null);
    }
}
