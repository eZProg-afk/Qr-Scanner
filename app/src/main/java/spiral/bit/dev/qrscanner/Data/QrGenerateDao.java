package spiral.bit.dev.qrscanner.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import spiral.bit.dev.qrscanner.Models.QrGenerateItem;
import spiral.bit.dev.qrscanner.Models.QrItem;

@Dao
public interface QrGenerateDao {

    //MADE BY SPIRAL BIT DEVELOPMENT

    @Insert
    void insertGenerate(QrGenerateItem qrGenerateItemn);

    @Delete
    void deleteGenerate(QrGenerateItem qrGenerateItemn);

    @Update
    void updateGenerate(QrGenerateItem qrGenerateItemn);

    @Query("DELETE FROM qr_items_generate")
    void deleteAllQrItemsScan();

    @Query("SELECT * FROM qr_items_generate ORDER BY id DESC")
    LiveData<List<QrGenerateItem>> getAllQrItemsScan();
}
