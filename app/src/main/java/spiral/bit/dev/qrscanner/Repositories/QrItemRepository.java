package spiral.bit.dev.qrscanner.Repositories;

//CREATED BY SPIRAL BIT DEVELOPMENT

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import java.util.List;
import spiral.bit.dev.qrscanner.Data.QrDao;
import spiral.bit.dev.qrscanner.Data.QrDatabase;
import spiral.bit.dev.qrscanner.Models.QrItem;

public class QrItemRepository {

    private QrDao qrDao;
    private LiveData<List<QrItem>> allQrItems;

    public QrItemRepository(Application application) {
        QrDatabase qrDatabase = QrDatabase.getInstance(application);
        qrDao = qrDatabase.getDao();
        allQrItems = qrDao.getAllQrItemsScan();
    }

    public void insert(QrItem qrItem) {
        new InsertQrItemAsyncTask(qrDao).execute(qrItem);
    }

    public void update(QrItem qrItem) {
        new UpdateQrItemAsyncTask(qrDao).execute(qrItem);
    }

    public void delete(QrItem qrItem) {
        new DeleteQrItemAsyncTask(qrDao).execute(qrItem);
    }

    public void deleteAllQrItems() {
        new DeleteAllQrItemsNoteAsyncTask(qrDao).execute();
    }

    public LiveData<List<QrItem>> getAllQrItems() {
        return allQrItems;
    }

    private static class InsertQrItemAsyncTask extends AsyncTask<QrItem, Void, Void> {

        private QrDao qrDao;

        private InsertQrItemAsyncTask(QrDao qrDao) {
            this.qrDao = qrDao;
        }

        @Override
        protected Void doInBackground(QrItem... qrItems) {
            qrDao.insertScan(qrItems[0]);
            return null;
        }
    }

    private static class UpdateQrItemAsyncTask extends AsyncTask<QrItem, Void, Void> {

        private QrDao qrDao;

        private UpdateQrItemAsyncTask(QrDao qrDao) {
            this.qrDao = qrDao;
        }

        @Override
        protected Void doInBackground(QrItem... qrItems) {
            qrDao.updateScan(qrItems[0]);
            return null;
        }
    }

        private static class DeleteQrItemAsyncTask extends AsyncTask<QrItem, Void, Void> {

        private QrDao qrDao;

        private DeleteQrItemAsyncTask(QrDao qrDao) {
            this.qrDao = qrDao;
        }

        @Override
        protected Void doInBackground(QrItem... qrItems) {
            qrDao.deleteScan(qrItems[0]);
            return null;
        }
    }

    private static class DeleteAllQrItemsNoteAsyncTask extends AsyncTask<Void, Void, Void> {

        private QrDao qrDao;

        private DeleteAllQrItemsNoteAsyncTask(QrDao qrDao) {
            this.qrDao = qrDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            qrDao.deleteAllQrItemsScan();
            return null;
        }
    }

}
