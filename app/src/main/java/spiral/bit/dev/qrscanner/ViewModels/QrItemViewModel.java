package spiral.bit.dev.qrscanner.ViewModels;

//CREATED BY SPIRAL BIT DEVELOPMENT

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import spiral.bit.dev.qrscanner.Models.QrItem;
import spiral.bit.dev.qrscanner.Repositories.QrItemRepository;

public class QrItemViewModel extends AndroidViewModel {

    private QrItemRepository repository;
    private LiveData<List<QrItem>> allQrItems;

    public QrItemViewModel(@NonNull Application application) {
        super(application);
        repository = new QrItemRepository(application);
        allQrItems = repository.getAllQrItems();
    }

    public void insert(QrItem qrItem) {
        repository.insert(qrItem);
    }

    public void update(QrItem qrItem) {
        repository.update(qrItem);
    }

    public void delete(QrItem qrItem) {
        repository.delete(qrItem);
    }

    public void deleteAllQrItems() {
        repository.deleteAllQrItems();
    }

    public LiveData<List<QrItem>> getAllQrItems() {
        return allQrItems;
    }
}
