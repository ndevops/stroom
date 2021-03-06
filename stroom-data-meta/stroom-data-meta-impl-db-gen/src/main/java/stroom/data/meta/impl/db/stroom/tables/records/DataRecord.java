/*
 * This file is generated by jOOQ.
 */
package stroom.data.meta.impl.db.stroom.tables.records;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record10;
import org.jooq.Row10;
import org.jooq.impl.UpdatableRecordImpl;

import stroom.data.meta.impl.db.stroom.tables.Data;


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
public class DataRecord extends UpdatableRecordImpl<DataRecord> implements Record10<Long, Long, Long, Long, Byte, Long, Long, Integer, Integer, Integer> {

    private static final long serialVersionUID = 686816487;

    /**
     * Setter for <code>stroom.data.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>stroom.data.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>stroom.data.create_time</code>.
     */
    public void setCreateTime(Long value) {
        set(1, value);
    }

    /**
     * Getter for <code>stroom.data.create_time</code>.
     */
    public Long getCreateTime() {
        return (Long) get(1);
    }

    /**
     * Setter for <code>stroom.data.effective_time</code>.
     */
    public void setEffectiveTime(Long value) {
        set(2, value);
    }

    /**
     * Getter for <code>stroom.data.effective_time</code>.
     */
    public Long getEffectiveTime() {
        return (Long) get(2);
    }

    /**
     * Setter for <code>stroom.data.parent_id</code>.
     */
    public void setParentId(Long value) {
        set(3, value);
    }

    /**
     * Getter for <code>stroom.data.parent_id</code>.
     */
    public Long getParentId() {
        return (Long) get(3);
    }

    /**
     * Setter for <code>stroom.data.status</code>.
     */
    public void setStatus(Byte value) {
        set(4, value);
    }

    /**
     * Getter for <code>stroom.data.status</code>.
     */
    public Byte getStatus() {
        return (Byte) get(4);
    }

    /**
     * Setter for <code>stroom.data.status_time</code>.
     */
    public void setStatusTime(Long value) {
        set(5, value);
    }

    /**
     * Getter for <code>stroom.data.status_time</code>.
     */
    public Long getStatusTime() {
        return (Long) get(5);
    }

    /**
     * Setter for <code>stroom.data.task_id</code>.
     */
    public void setTaskId(Long value) {
        set(6, value);
    }

    /**
     * Getter for <code>stroom.data.task_id</code>.
     */
    public Long getTaskId() {
        return (Long) get(6);
    }

    /**
     * Setter for <code>stroom.data.feed_id</code>.
     */
    public void setFeedId(Integer value) {
        set(7, value);
    }

    /**
     * Getter for <code>stroom.data.feed_id</code>.
     */
    public Integer getFeedId() {
        return (Integer) get(7);
    }

    /**
     * Setter for <code>stroom.data.type_id</code>.
     */
    public void setTypeId(Integer value) {
        set(8, value);
    }

    /**
     * Getter for <code>stroom.data.type_id</code>.
     */
    public Integer getTypeId() {
        return (Integer) get(8);
    }

    /**
     * Setter for <code>stroom.data.processor_id</code>.
     */
    public void setProcessorId(Integer value) {
        set(9, value);
    }

    /**
     * Getter for <code>stroom.data.processor_id</code>.
     */
    public Integer getProcessorId() {
        return (Integer) get(9);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Long> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record10 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row10<Long, Long, Long, Long, Byte, Long, Long, Integer, Integer, Integer> fieldsRow() {
        return (Row10) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row10<Long, Long, Long, Long, Byte, Long, Long, Integer, Integer, Integer> valuesRow() {
        return (Row10) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field1() {
        return Data.DATA.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field2() {
        return Data.DATA.CREATE_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field3() {
        return Data.DATA.EFFECTIVE_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field4() {
        return Data.DATA.PARENT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field5() {
        return Data.DATA.STATUS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field6() {
        return Data.DATA.STATUS_TIME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field7() {
        return Data.DATA.TASK_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field8() {
        return Data.DATA.FEED_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field9() {
        return Data.DATA.TYPE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field10() {
        return Data.DATA.PROCESSOR_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component2() {
        return getCreateTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component3() {
        return getEffectiveTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component4() {
        return getParentId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte component5() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component6() {
        return getStatusTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component7() {
        return getTaskId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component8() {
        return getFeedId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component9() {
        return getTypeId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component10() {
        return getProcessorId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value2() {
        return getCreateTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value3() {
        return getEffectiveTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value4() {
        return getParentId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value5() {
        return getStatus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value6() {
        return getStatusTime();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value7() {
        return getTaskId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value8() {
        return getFeedId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value9() {
        return getTypeId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value10() {
        return getProcessorId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord value1(Long value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord value2(Long value) {
        setCreateTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord value3(Long value) {
        setEffectiveTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord value4(Long value) {
        setParentId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord value5(Byte value) {
        setStatus(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord value6(Long value) {
        setStatusTime(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord value7(Long value) {
        setTaskId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord value8(Integer value) {
        setFeedId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord value9(Integer value) {
        setTypeId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord value10(Integer value) {
        setProcessorId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataRecord values(Long value1, Long value2, Long value3, Long value4, Byte value5, Long value6, Long value7, Integer value8, Integer value9, Integer value10) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        value10(value10);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached DataRecord
     */
    public DataRecord() {
        super(Data.DATA);
    }

    /**
     * Create a detached, initialised DataRecord
     */
    public DataRecord(Long id, Long createTime, Long effectiveTime, Long parentId, Byte status, Long statusTime, Long taskId, Integer feedId, Integer typeId, Integer processorId) {
        super(Data.DATA);

        set(0, id);
        set(1, createTime);
        set(2, effectiveTime);
        set(3, parentId);
        set(4, status);
        set(5, statusTime);
        set(6, taskId);
        set(7, feedId);
        set(8, typeId);
        set(9, processorId);
    }
}
