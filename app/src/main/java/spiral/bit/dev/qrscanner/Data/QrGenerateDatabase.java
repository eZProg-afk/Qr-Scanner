package spiral.bit.dev.qrscanner.Data;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import spiral.bit.dev.qrscanner.Models.QrGenerateItem;
import spiral.bit.dev.qrscanner.Models.QrItem;

@Database(entities = QrGenerateItem.class, version = 4)
@TypeConverters({Converters.class})
public abstract class QrGenerateDatabase extends RoomDatabase {

    //MADE BY SPIRAL BIT DEVELOPMENT

    public abstract QrGenerateDao getDao();

    private static QrGenerateDatabase instance;

    public static synchronized QrGenerateDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), QrGenerateDatabase.class, "qr_items_generate_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static Callback roomCallback = new Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {

        private QrGenerateDao qrDao;

        private PopulateDBAsyncTask(QrGenerateDatabase db) {
            qrDao = db.getDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
