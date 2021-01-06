package spiral.bit.dev.qrscanner.ViewModels;

//CREATED BY SPIRAL BIT DEVELOPMENT

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import spiral.bit.dev.qrscanner.Models.QrGenerateItem;
import spiral.bit.dev.qrscanner.Models.QrItem;
import spiral.bit.dev.qrscanner.Repositories.QrGenerateItemRepository;
import spiral.bit.dev.qrscanner.Repositories.QrItemRepository;

public class QrGenerateItemViewModel extends AndroidViewModel {

    private QrGenerateItemRepository repository;
    private LiveData<List<QrGenerateItem>> allQrItems;

    public QrGenerateItemViewModel(@NonNull Application application) {
        super(application);
        repository = new QrGenerateItemRepository(application);
        allQrItems = repository.getAllQrItems();
    }

    public void insert(QrGenerateItem qrItem) {
        repository.insert(qrItem);
    }

    public void update(QrGenerateItem qrItem) {
        repository.update(qrItem);
    }

    public void delete(QrGenerateItem qrItem) {
        repository.delete(qrItem);
    }

    public void deleteAllQrItems() {
        repository.deleteAllQrItems();
    }

    public LiveData<List<QrGenerateItem>> getAllQrItems() {
        return allQrItems;
    }
}
