package spiral.bit.dev.qrscanner.Data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import spiral.bit.dev.qrscanner.Models.QrItem;

@Dao
public interface QrDao {

    //MADE BY SPIRAL BIT DEVELOPMENT

    @Insert
    void insertScan(QrItem qrItem);

    @Delete
    void deleteScan(QrItem qrItem);

    @Update
    void updateScan(QrItem qrItem);

    @Query("DELETE FROM qr_items_scan")
    void deleteAllQrItemsScan();

    @Query("SELECT * FROM qr_items_scan ORDER BY id DESC")
    LiveData<List<QrItem>> getAllQrItemsScan();
}
