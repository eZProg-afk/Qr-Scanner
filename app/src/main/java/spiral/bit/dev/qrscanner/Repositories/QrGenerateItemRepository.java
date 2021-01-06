package spiral.bit.dev.qrscanner.Repositories;

//CREATED BY SPIRAL BIT DEVELOPMENT

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

import spiral.bit.dev.qrscanner.Data.QrDao;
import spiral.bit.dev.qrscanner.Data.QrDatabase;
import spiral.bit.dev.qrscanner.Data.QrGenerateDao;
import spiral.bit.dev.qrscanner.Data.QrGenerateDatabase;
import spiral.bit.dev.qrscanner.Models.QrGenerateItem;
import spiral.bit.dev.qrscanner.Models.QrItem;

public class QrGenerateItemRepository {

    private QrGenerateDao qrDao;
    private LiveData<List<QrGenerateItem>> allQrItems;

    public QrGenerateItemRepository(Application application) {
        QrGenerateDatabase qrDatabase = QrGenerateDatabase.getInstance(application);
        qrDao = qrDatabase.getDao();
        allQrItems = qrDao.getAllQrItemsScan();
    }

    public void insert(QrGenerateItem qrItem) {
        new InsertQrItemAsyncTask(qrDao).execute(qrItem);
    }

    public void update(QrGenerateItem qrItem) {
        new UpdateQrItemAsyncTask(qrDao).execute(qrItem);
    }

    public void delete(QrGenerateItem qrItem) {
        new DeleteQrItemAsyncTask(qrDao).execute(qrItem);
    }

    public void deleteAllQrItems() {
        new DeleteAllQrItemsNoteAsyncTask(qrDao).execute();
    }

    public LiveData<List<QrGenerateItem>> getAllQrItems() {
        return allQrItems;
    }

    private static class InsertQrItemAsyncTask extends AsyncTask<QrGenerateItem, Void, Void> {

        private QrGenerateDao qrDao;

        private InsertQrItemAsyncTask(QrGenerateDao qrDao) {
            this.qrDao = qrDao;
        }

        @Override
        protected Void doInBackground(QrGenerateItem... qrGenerateItems) {
            qrDao.insertGenerate(qrGenerateItems[0]);
            return null;
        }
    }

    private static class UpdateQrItemAsyncTask extends AsyncTask<QrGenerateItem, Void, Void> {

        private QrGenerateDao qrDao;

        private UpdateQrItemAsyncTask(QrGenerateDao qrDao) {
            this.qrDao = qrDao;
        }

        @Override
        protected Void doInBackground(QrGenerateItem... qrGenerateItems) {
            qrDao.updateGenerate(qrGenerateItems[0]);
            return null;
        }
    }

        private static class DeleteQrItemAsyncTask extends AsyncTask<QrGenerateItem, Void, Void> {

        private QrGenerateDao qrDao;

        private DeleteQrItemAsyncTask(QrGenerateDao qrDao) {
            this.qrDao = qrDao;
        }

        @Override
        protected Void doInBackground(QrGenerateItem... qrGenerateItems) {
            qrDao.deleteGenerate(qrGenerateItems[0]);
            return null;
        }
    }

    private static class DeleteAllQrItemsNoteAsyncTask extends AsyncTask<Void, Void, Void> {

        private QrGenerateDao qrDao;

        private DeleteAllQrItemsNoteAsyncTask(QrGenerateDao qrDao) {
            this.qrDao = qrDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            qrDao.deleteAllQrItemsScan();
            return null;
        }
    }

}
