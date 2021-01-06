package spiral.bit.dev.qrscanner.Data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import spiral.bit.dev.qrscanner.Models.QrItem;

@Database(entities = QrItem.class, version = 6)
@TypeConverters({Converters.class})
public abstract class QrDatabase extends RoomDatabase {

    //MADE BY SPIRAL BIT DEVELOPMENT

    public abstract QrDao getDao();

    private static QrDatabase instance;

    public static synchronized QrDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), QrDatabase.class, "qr_items_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {

        private QrDao qrDao;

        private PopulateDBAsyncTask(QrDatabase db) {
            qrDao = db.getDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
