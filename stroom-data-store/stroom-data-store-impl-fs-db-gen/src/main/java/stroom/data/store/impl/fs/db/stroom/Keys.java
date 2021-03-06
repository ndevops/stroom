/*
 * This file is generated by jOOQ.
 */
package stroom.data.store.impl.fs.db.stroom;


import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;

import stroom.data.store.impl.fs.db.stroom.tables.DataVolume;
import stroom.data.store.impl.fs.db.stroom.tables.FileFeedPath;
import stroom.data.store.impl.fs.db.stroom.tables.FileTypePath;
import stroom.data.store.impl.fs.db.stroom.tables.Nd;
import stroom.data.store.impl.fs.db.stroom.tables.Rk;
import stroom.data.store.impl.fs.db.stroom.tables.Vol;
import stroom.data.store.impl.fs.db.stroom.tables.VolState;
import stroom.data.store.impl.fs.db.stroom.tables.records.DataVolumeRecord;
import stroom.data.store.impl.fs.db.stroom.tables.records.FileFeedPathRecord;
import stroom.data.store.impl.fs.db.stroom.tables.records.FileTypePathRecord;
import stroom.data.store.impl.fs.db.stroom.tables.records.NdRecord;
import stroom.data.store.impl.fs.db.stroom.tables.records.RkRecord;
import stroom.data.store.impl.fs.db.stroom.tables.records.VolRecord;
import stroom.data.store.impl.fs.db.stroom.tables.records.VolStateRecord;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>stroom</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.11.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<FileFeedPathRecord, Integer> IDENTITY_FILE_FEED_PATH = Identities0.IDENTITY_FILE_FEED_PATH;
    public static final Identity<FileTypePathRecord, Integer> IDENTITY_FILE_TYPE_PATH = Identities0.IDENTITY_FILE_TYPE_PATH;
    public static final Identity<NdRecord, Integer> IDENTITY_ND = Identities0.IDENTITY_ND;
    public static final Identity<RkRecord, Integer> IDENTITY_RK = Identities0.IDENTITY_RK;
    public static final Identity<VolRecord, Integer> IDENTITY_VOL = Identities0.IDENTITY_VOL;
    public static final Identity<VolStateRecord, Integer> IDENTITY_VOL_STATE = Identities0.IDENTITY_VOL_STATE;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<DataVolumeRecord> KEY_DATA_VOLUME_PRIMARY = UniqueKeys0.KEY_DATA_VOLUME_PRIMARY;
    public static final UniqueKey<FileFeedPathRecord> KEY_FILE_FEED_PATH_PRIMARY = UniqueKeys0.KEY_FILE_FEED_PATH_PRIMARY;
    public static final UniqueKey<FileFeedPathRecord> KEY_FILE_FEED_PATH_NAME = UniqueKeys0.KEY_FILE_FEED_PATH_NAME;
    public static final UniqueKey<FileTypePathRecord> KEY_FILE_TYPE_PATH_PRIMARY = UniqueKeys0.KEY_FILE_TYPE_PATH_PRIMARY;
    public static final UniqueKey<FileTypePathRecord> KEY_FILE_TYPE_PATH_NAME = UniqueKeys0.KEY_FILE_TYPE_PATH_NAME;
    public static final UniqueKey<NdRecord> KEY_ND_PRIMARY = UniqueKeys0.KEY_ND_PRIMARY;
    public static final UniqueKey<NdRecord> KEY_ND_NAME = UniqueKeys0.KEY_ND_NAME;
    public static final UniqueKey<RkRecord> KEY_RK_PRIMARY = UniqueKeys0.KEY_RK_PRIMARY;
    public static final UniqueKey<RkRecord> KEY_RK_NAME = UniqueKeys0.KEY_RK_NAME;
    public static final UniqueKey<VolRecord> KEY_VOL_PRIMARY = UniqueKeys0.KEY_VOL_PRIMARY;
    public static final UniqueKey<VolRecord> KEY_VOL_FK_ND_ID = UniqueKeys0.KEY_VOL_FK_ND_ID;
    public static final UniqueKey<VolStateRecord> KEY_VOL_STATE_PRIMARY = UniqueKeys0.KEY_VOL_STATE_PRIMARY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<NdRecord, RkRecord> ND_FK_RK_ID = ForeignKeys0.ND_FK_RK_ID;
    public static final ForeignKey<VolRecord, NdRecord> VOL_FK_ND_ID = ForeignKeys0.VOL_FK_ND_ID;
    public static final ForeignKey<VolRecord, VolStateRecord> VOL_STAT_FK_VOL_STATE_ID = ForeignKeys0.VOL_STAT_FK_VOL_STATE_ID;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 {
        public static Identity<FileFeedPathRecord, Integer> IDENTITY_FILE_FEED_PATH = Internal.createIdentity(FileFeedPath.FILE_FEED_PATH, FileFeedPath.FILE_FEED_PATH.ID);
        public static Identity<FileTypePathRecord, Integer> IDENTITY_FILE_TYPE_PATH = Internal.createIdentity(FileTypePath.FILE_TYPE_PATH, FileTypePath.FILE_TYPE_PATH.ID);
        public static Identity<NdRecord, Integer> IDENTITY_ND = Internal.createIdentity(Nd.ND, Nd.ND.ID);
        public static Identity<RkRecord, Integer> IDENTITY_RK = Internal.createIdentity(Rk.RK, Rk.RK.ID);
        public static Identity<VolRecord, Integer> IDENTITY_VOL = Internal.createIdentity(Vol.VOL, Vol.VOL.ID);
        public static Identity<VolStateRecord, Integer> IDENTITY_VOL_STATE = Internal.createIdentity(VolState.VOL_STATE, VolState.VOL_STATE.ID);
    }

    private static class UniqueKeys0 {
        public static final UniqueKey<DataVolumeRecord> KEY_DATA_VOLUME_PRIMARY = Internal.createUniqueKey(DataVolume.DATA_VOLUME, "KEY_data_volume_PRIMARY", DataVolume.DATA_VOLUME.DATA_ID, DataVolume.DATA_VOLUME.VOLUME_ID);
        public static final UniqueKey<FileFeedPathRecord> KEY_FILE_FEED_PATH_PRIMARY = Internal.createUniqueKey(FileFeedPath.FILE_FEED_PATH, "KEY_file_feed_path_PRIMARY", FileFeedPath.FILE_FEED_PATH.ID);
        public static final UniqueKey<FileFeedPathRecord> KEY_FILE_FEED_PATH_NAME = Internal.createUniqueKey(FileFeedPath.FILE_FEED_PATH, "KEY_file_feed_path_name", FileFeedPath.FILE_FEED_PATH.NAME);
        public static final UniqueKey<FileTypePathRecord> KEY_FILE_TYPE_PATH_PRIMARY = Internal.createUniqueKey(FileTypePath.FILE_TYPE_PATH, "KEY_file_type_path_PRIMARY", FileTypePath.FILE_TYPE_PATH.ID);
        public static final UniqueKey<FileTypePathRecord> KEY_FILE_TYPE_PATH_NAME = Internal.createUniqueKey(FileTypePath.FILE_TYPE_PATH, "KEY_file_type_path_name", FileTypePath.FILE_TYPE_PATH.NAME);
        public static final UniqueKey<NdRecord> KEY_ND_PRIMARY = Internal.createUniqueKey(Nd.ND, "KEY_ND_PRIMARY", Nd.ND.ID);
        public static final UniqueKey<NdRecord> KEY_ND_NAME = Internal.createUniqueKey(Nd.ND, "KEY_ND_NAME", Nd.ND.NAME);
        public static final UniqueKey<RkRecord> KEY_RK_PRIMARY = Internal.createUniqueKey(Rk.RK, "KEY_RK_PRIMARY", Rk.RK.ID);
        public static final UniqueKey<RkRecord> KEY_RK_NAME = Internal.createUniqueKey(Rk.RK, "KEY_RK_NAME", Rk.RK.NAME);
        public static final UniqueKey<VolRecord> KEY_VOL_PRIMARY = Internal.createUniqueKey(Vol.VOL, "KEY_VOL_PRIMARY", Vol.VOL.ID);
        public static final UniqueKey<VolRecord> KEY_VOL_FK_ND_ID = Internal.createUniqueKey(Vol.VOL, "KEY_VOL_FK_ND_ID", Vol.VOL.FK_ND_ID, Vol.VOL.PATH);
        public static final UniqueKey<VolStateRecord> KEY_VOL_STATE_PRIMARY = Internal.createUniqueKey(VolState.VOL_STATE, "KEY_VOL_STATE_PRIMARY", VolState.VOL_STATE.ID);
    }

    private static class ForeignKeys0 {
        public static final ForeignKey<NdRecord, RkRecord> ND_FK_RK_ID = Internal.createForeignKey(stroom.data.store.impl.fs.db.stroom.Keys.KEY_RK_PRIMARY, Nd.ND, "ND_FK_RK_ID", Nd.ND.FK_RK_ID);
        public static final ForeignKey<VolRecord, NdRecord> VOL_FK_ND_ID = Internal.createForeignKey(stroom.data.store.impl.fs.db.stroom.Keys.KEY_ND_PRIMARY, Vol.VOL, "VOL_FK_ND_ID", Vol.VOL.FK_ND_ID);
        public static final ForeignKey<VolRecord, VolStateRecord> VOL_STAT_FK_VOL_STATE_ID = Internal.createForeignKey(stroom.data.store.impl.fs.db.stroom.Keys.KEY_VOL_STATE_PRIMARY, Vol.VOL, "VOL_STAT_FK_VOL_STATE_ID", Vol.VOL.FK_VOL_STATE_ID);
    }
}
