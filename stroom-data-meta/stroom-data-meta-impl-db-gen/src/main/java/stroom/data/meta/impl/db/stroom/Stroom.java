/*
 * This file is generated by jOOQ.
 */
package stroom.data.meta.impl.db.stroom;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import stroom.data.meta.impl.db.DefaultCatalog;
import stroom.data.meta.impl.db.stroom.tables.Data;
import stroom.data.meta.impl.db.stroom.tables.DataFeed;
import stroom.data.meta.impl.db.stroom.tables.DataProcessor;
import stroom.data.meta.impl.db.stroom.tables.DataType;
import stroom.data.meta.impl.db.stroom.tables.MetaKey;
import stroom.data.meta.impl.db.stroom.tables.MetaVal;


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
public class Stroom extends SchemaImpl {

    private static final long serialVersionUID = 925061086;

    /**
     * The reference instance of <code>stroom</code>
     */
    public static final Stroom STROOM = new Stroom();

    /**
     * The table <code>stroom.data</code>.
     */
    public final Data DATA = stroom.data.meta.impl.db.stroom.tables.Data.DATA;

    /**
     * The table <code>stroom.data_feed</code>.
     */
    public final DataFeed DATA_FEED = stroom.data.meta.impl.db.stroom.tables.DataFeed.DATA_FEED;

    /**
     * The table <code>stroom.data_processor</code>.
     */
    public final DataProcessor DATA_PROCESSOR = stroom.data.meta.impl.db.stroom.tables.DataProcessor.DATA_PROCESSOR;

    /**
     * The table <code>stroom.data_type</code>.
     */
    public final DataType DATA_TYPE = stroom.data.meta.impl.db.stroom.tables.DataType.DATA_TYPE;

    /**
     * The table <code>stroom.meta_key</code>.
     */
    public final MetaKey META_KEY = stroom.data.meta.impl.db.stroom.tables.MetaKey.META_KEY;

    /**
     * The table <code>stroom.meta_val</code>.
     */
    public final MetaVal META_VAL = stroom.data.meta.impl.db.stroom.tables.MetaVal.META_VAL;

    /**
     * No further instances allowed
     */
    private Stroom() {
        super("stroom", null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        List result = new ArrayList();
        result.addAll(getTables0());
        return result;
    }

    private final List<Table<?>> getTables0() {
        return Arrays.<Table<?>>asList(
            Data.DATA,
            DataFeed.DATA_FEED,
            DataProcessor.DATA_PROCESSOR,
            DataType.DATA_TYPE,
            MetaKey.META_KEY,
            MetaVal.META_VAL);
    }
}
