/*
 * This file is generated by jOOQ.
 */
package stroom.config.impl.db.stroom;


import javax.annotation.Generated;

import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;

import stroom.config.impl.db.stroom.tables.Config;
import stroom.config.impl.db.stroom.tables.ConfigHistory;
import stroom.config.impl.db.stroom.tables.records.ConfigHistoryRecord;
import stroom.config.impl.db.stroom.tables.records.ConfigRecord;


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

    public static final Identity<ConfigRecord, Integer> IDENTITY_CONFIG = Identities0.IDENTITY_CONFIG;
    public static final Identity<ConfigHistoryRecord, Integer> IDENTITY_CONFIG_HISTORY = Identities0.IDENTITY_CONFIG_HISTORY;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ConfigRecord> KEY_CONFIG_PRIMARY = UniqueKeys0.KEY_CONFIG_PRIMARY;
    public static final UniqueKey<ConfigRecord> KEY_CONFIG_NAME = UniqueKeys0.KEY_CONFIG_NAME;
    public static final UniqueKey<ConfigHistoryRecord> KEY_CONFIG_HISTORY_PRIMARY = UniqueKeys0.KEY_CONFIG_HISTORY_PRIMARY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 {
        public static Identity<ConfigRecord, Integer> IDENTITY_CONFIG = Internal.createIdentity(Config.CONFIG, Config.CONFIG.ID);
        public static Identity<ConfigHistoryRecord, Integer> IDENTITY_CONFIG_HISTORY = Internal.createIdentity(ConfigHistory.CONFIG_HISTORY, ConfigHistory.CONFIG_HISTORY.ID);
    }

    private static class UniqueKeys0 {
        public static final UniqueKey<ConfigRecord> KEY_CONFIG_PRIMARY = Internal.createUniqueKey(Config.CONFIG, "KEY_config_PRIMARY", Config.CONFIG.ID);
        public static final UniqueKey<ConfigRecord> KEY_CONFIG_NAME = Internal.createUniqueKey(Config.CONFIG, "KEY_config_name", Config.CONFIG.NAME);
        public static final UniqueKey<ConfigHistoryRecord> KEY_CONFIG_HISTORY_PRIMARY = Internal.createUniqueKey(ConfigHistory.CONFIG_HISTORY, "KEY_config_history_PRIMARY", ConfigHistory.CONFIG_HISTORY.ID);
    }
}
