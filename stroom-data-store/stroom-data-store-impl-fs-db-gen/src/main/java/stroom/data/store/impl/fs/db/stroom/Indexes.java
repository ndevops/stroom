/*
 * This file is generated by jOOQ.
 */
package stroom.data.store.impl.fs.db.stroom;


import javax.annotation.Generated;

import org.jooq.Index;
import org.jooq.OrderField;
import org.jooq.impl.Internal;

import stroom.data.store.impl.fs.db.stroom.tables.DataVolume;
import stroom.data.store.impl.fs.db.stroom.tables.FileFeedPath;
import stroom.data.store.impl.fs.db.stroom.tables.FileTypePath;
import stroom.data.store.impl.fs.db.stroom.tables.Nd;
import stroom.data.store.impl.fs.db.stroom.tables.Rk;
import stroom.data.store.impl.fs.db.stroom.tables.Vol;
import stroom.data.store.impl.fs.db.stroom.tables.VolState;


/**
 * A class modelling indexes of tables of the <code>stroom</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Indexes {

    // -------------------------------------------------------------------------
    // INDEX definitions
    // -------------------------------------------------------------------------

    public static final Index DATA_VOLUME_PRIMARY = Indexes0.DATA_VOLUME_PRIMARY;
    public static final Index FILE_FEED_PATH_NAME = Indexes0.FILE_FEED_PATH_NAME;
    public static final Index FILE_FEED_PATH_PRIMARY = Indexes0.FILE_FEED_PATH_PRIMARY;
    public static final Index FILE_TYPE_PATH_NAME = Indexes0.FILE_TYPE_PATH_NAME;
    public static final Index FILE_TYPE_PATH_PRIMARY = Indexes0.FILE_TYPE_PATH_PRIMARY;
    public static final Index ND_NAME = Indexes0.ND_NAME;
    public static final Index ND_ND_FK_RK_ID = Indexes0.ND_ND_FK_RK_ID;
    public static final Index ND_PRIMARY = Indexes0.ND_PRIMARY;
    public static final Index RK_NAME = Indexes0.RK_NAME;
    public static final Index RK_PRIMARY = Indexes0.RK_PRIMARY;
    public static final Index VOL_FK_ND_ID = Indexes0.VOL_FK_ND_ID;
    public static final Index VOL_PRIMARY = Indexes0.VOL_PRIMARY;
    public static final Index VOL_VOL_STAT_FK_VOL_STATE_ID = Indexes0.VOL_VOL_STAT_FK_VOL_STATE_ID;
    public static final Index VOL_STATE_PRIMARY = Indexes0.VOL_STATE_PRIMARY;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Indexes0 {
        public static Index DATA_VOLUME_PRIMARY = Internal.createIndex("PRIMARY", DataVolume.DATA_VOLUME, new OrderField[] { DataVolume.DATA_VOLUME.DATA_ID, DataVolume.DATA_VOLUME.VOLUME_ID }, true);
        public static Index FILE_FEED_PATH_NAME = Internal.createIndex("name", FileFeedPath.FILE_FEED_PATH, new OrderField[] { FileFeedPath.FILE_FEED_PATH.NAME }, true);
        public static Index FILE_FEED_PATH_PRIMARY = Internal.createIndex("PRIMARY", FileFeedPath.FILE_FEED_PATH, new OrderField[] { FileFeedPath.FILE_FEED_PATH.ID }, true);
        public static Index FILE_TYPE_PATH_NAME = Internal.createIndex("name", FileTypePath.FILE_TYPE_PATH, new OrderField[] { FileTypePath.FILE_TYPE_PATH.NAME }, true);
        public static Index FILE_TYPE_PATH_PRIMARY = Internal.createIndex("PRIMARY", FileTypePath.FILE_TYPE_PATH, new OrderField[] { FileTypePath.FILE_TYPE_PATH.ID }, true);
        public static Index ND_NAME = Internal.createIndex("NAME", Nd.ND, new OrderField[] { Nd.ND.NAME }, true);
        public static Index ND_ND_FK_RK_ID = Internal.createIndex("ND_FK_RK_ID", Nd.ND, new OrderField[] { Nd.ND.FK_RK_ID }, false);
        public static Index ND_PRIMARY = Internal.createIndex("PRIMARY", Nd.ND, new OrderField[] { Nd.ND.ID }, true);
        public static Index RK_NAME = Internal.createIndex("NAME", Rk.RK, new OrderField[] { Rk.RK.NAME }, true);
        public static Index RK_PRIMARY = Internal.createIndex("PRIMARY", Rk.RK, new OrderField[] { Rk.RK.ID }, true);
        public static Index VOL_FK_ND_ID = Internal.createIndex("FK_ND_ID", Vol.VOL, new OrderField[] { Vol.VOL.FK_ND_ID, Vol.VOL.PATH }, true);
        public static Index VOL_PRIMARY = Internal.createIndex("PRIMARY", Vol.VOL, new OrderField[] { Vol.VOL.ID }, true);
        public static Index VOL_VOL_STAT_FK_VOL_STATE_ID = Internal.createIndex("VOL_STAT_FK_VOL_STATE_ID", Vol.VOL, new OrderField[] { Vol.VOL.FK_VOL_STATE_ID }, false);
        public static Index VOL_STATE_PRIMARY = Internal.createIndex("PRIMARY", VolState.VOL_STATE, new OrderField[] { VolState.VOL_STATE.ID }, true);
    }
}