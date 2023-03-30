package com.android.app.utils.room;

import androidx.room.Room;
import androidx.room.migration.Migration;


import com.android.app.BuildConfig;
import com.android.app.app.App;
import com.android.app.ui.activity.jetpack.room.room2.DaoTable1;
import com.android.app.ui.activity.jetpack.room.room2.DaoTable2;

public abstract class RoomManager extends BaseRoomManager {

    public static int VERSION = BuildConfig.ROOM_VERSION;
    public static String ROOM_DB = BuildConfig.ROOM_VERSION_FILE_NAME;

    private static volatile RoomManager INSTANCE;
    private static volatile RoomManager INSTANCE2;

    public static RoomManager getInstance() {
        if (INSTANCE == null) {
            synchronized (RoomManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            App.getInstance().getApplicationContext(),
                            RoomManager.class,
                            ROOM_DB
                    ).build();
                }
            }
        }
        return INSTANCE;
    }

    public static RoomManager getInstance(Migration migration) {
        if (INSTANCE2 == null) {
            synchronized (RoomManager.class) {
                if (INSTANCE2 == null) {
                    INSTANCE2 = Room.databaseBuilder(
                                    App.getInstance().getApplicationContext(),
                                    RoomManager.class,
                                    "room_table_2.db"
                            )
                            .addMigrations(migration)
                            .build();
                }
            }
        }
        return INSTANCE2;
    }

    public abstract DaoTable1 getDao1();

    public abstract DaoTable2 getDao2();
}
