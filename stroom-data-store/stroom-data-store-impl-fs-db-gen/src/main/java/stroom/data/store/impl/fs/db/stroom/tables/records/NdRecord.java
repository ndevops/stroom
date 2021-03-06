/*
 * This file is generated by jOOQ.
 */
package stroom.data.store.impl.fs.db.stroom.tables.records;


import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record11;
import org.jooq.Row11;
import org.jooq.impl.UpdatableRecordImpl;

import stroom.data.store.impl.fs.db.stroom.tables.Nd;


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
public class NdRecord extends UpdatableRecordImpl<NdRecord> implements Record11<Integer, Byte, String, String, String, String, Short, Boolean, Integer, Long, Long> {

    private static final long serialVersionUID = 265778237;

    /**
     * Setter for <code>stroom.ND.ID</code>.
     */
    public void setId(Integer value) {
        set(0, value);
    }

    /**
     * Getter for <code>stroom.ND.ID</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>stroom.ND.VER</code>.
     */
    public void setVer(Byte value) {
        set(1, value);
    }

    /**
     * Getter for <code>stroom.ND.VER</code>.
     */
    public Byte getVer() {
        return (Byte) get(1);
    }

    /**
     * Setter for <code>stroom.ND.CRT_USER</code>.
     */
    public void setCrtUser(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>stroom.ND.CRT_USER</code>.
     */
    public String getCrtUser() {
        return (String) get(2);
    }

    /**
     * Setter for <code>stroom.ND.UPD_USER</code>.
     */
    public void setUpdUser(String value) {
        set(3, value);
    }

    /**
     * Getter for <code>stroom.ND.UPD_USER</code>.
     */
    public String getUpdUser() {
        return (String) get(3);
    }

    /**
     * Setter for <code>stroom.ND.CLSTR_URL</code>.
     */
    public void setClstrUrl(String value) {
        set(4, value);
    }

    /**
     * Getter for <code>stroom.ND.CLSTR_URL</code>.
     */
    public String getClstrUrl() {
        return (String) get(4);
    }

    /**
     * Setter for <code>stroom.ND.NAME</code>.
     */
    public void setName(String value) {
        set(5, value);
    }

    /**
     * Getter for <code>stroom.ND.NAME</code>.
     */
    public String getName() {
        return (String) get(5);
    }

    /**
     * Setter for <code>stroom.ND.PRIOR</code>.
     */
    public void setPrior(Short value) {
        set(6, value);
    }

    /**
     * Getter for <code>stroom.ND.PRIOR</code>.
     */
    public Short getPrior() {
        return (Short) get(6);
    }

    /**
     * Setter for <code>stroom.ND.ENBL</code>.
     */
    public void setEnbl(Boolean value) {
        set(7, value);
    }

    /**
     * Getter for <code>stroom.ND.ENBL</code>.
     */
    public Boolean getEnbl() {
        return (Boolean) get(7);
    }

    /**
     * Setter for <code>stroom.ND.FK_RK_ID</code>.
     */
    public void setFkRkId(Integer value) {
        set(8, value);
    }

    /**
     * Getter for <code>stroom.ND.FK_RK_ID</code>.
     */
    public Integer getFkRkId() {
        return (Integer) get(8);
    }

    /**
     * Setter for <code>stroom.ND.CRT_MS</code>.
     */
    public void setCrtMs(Long value) {
        set(9, value);
    }

    /**
     * Getter for <code>stroom.ND.CRT_MS</code>.
     */
    public Long getCrtMs() {
        return (Long) get(9);
    }

    /**
     * Setter for <code>stroom.ND.UPD_MS</code>.
     */
    public void setUpdMs(Long value) {
        set(10, value);
    }

    /**
     * Getter for <code>stroom.ND.UPD_MS</code>.
     */
    public Long getUpdMs() {
        return (Long) get(10);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record11 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row11<Integer, Byte, String, String, String, String, Short, Boolean, Integer, Long, Long> fieldsRow() {
        return (Row11) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row11<Integer, Byte, String, String, String, String, Short, Boolean, Integer, Long, Long> valuesRow() {
        return (Row11) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field1() {
        return Nd.ND.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Byte> field2() {
        return Nd.ND.VER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field3() {
        return Nd.ND.CRT_USER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field4() {
        return Nd.ND.UPD_USER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field5() {
        return Nd.ND.CLSTR_URL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<String> field6() {
        return Nd.ND.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Short> field7() {
        return Nd.ND.PRIOR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Boolean> field8() {
        return Nd.ND.ENBL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer> field9() {
        return Nd.ND.FK_RK_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field10() {
        return Nd.ND.CRT_MS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Long> field11() {
        return Nd.ND.UPD_MS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte component2() {
        return getVer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component3() {
        return getCrtUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component4() {
        return getUpdUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component5() {
        return getClstrUrl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String component6() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short component7() {
        return getPrior();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean component8() {
        return getEnbl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer component9() {
        return getFkRkId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component10() {
        return getCrtMs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long component11() {
        return getUpdMs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte value2() {
        return getVer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value3() {
        return getCrtUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value4() {
        return getUpdUser();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value5() {
        return getClstrUrl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String value6() {
        return getName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short value7() {
        return getPrior();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean value8() {
        return getEnbl();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer value9() {
        return getFkRkId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value10() {
        return getCrtMs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long value11() {
        return getUpdMs();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NdRecord value1(Integer value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NdRecord value2(Byte value) {
        setVer(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NdRecord value3(String value) {
        setCrtUser(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NdRecord value4(String value) {
        setUpdUser(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NdRecord value5(String value) {
        setClstrUrl(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NdRecord value6(String value) {
        setName(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NdRecord value7(Short value) {
        setPrior(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NdRecord value8(Boolean value) {
        setEnbl(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NdRecord value9(Integer value) {
        setFkRkId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NdRecord value10(Long value) {
        setCrtMs(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NdRecord value11(Long value) {
        setUpdMs(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NdRecord values(Integer value1, Byte value2, String value3, String value4, String value5, String value6, Short value7, Boolean value8, Integer value9, Long value10, Long value11) {
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
        value11(value11);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached NdRecord
     */
    public NdRecord() {
        super(Nd.ND);
    }

    /**
     * Create a detached, initialised NdRecord
     */
    public NdRecord(Integer id, Byte ver, String crtUser, String updUser, String clstrUrl, String name, Short prior, Boolean enbl, Integer fkRkId, Long crtMs, Long updMs) {
        super(Nd.ND);

        set(0, id);
        set(1, ver);
        set(2, crtUser);
        set(3, updUser);
        set(4, clstrUrl);
        set(5, name);
        set(6, prior);
        set(7, enbl);
        set(8, fkRkId);
        set(9, crtMs);
        set(10, updMs);
    }
}
