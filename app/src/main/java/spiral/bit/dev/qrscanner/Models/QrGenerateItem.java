package spiral.bit.dev.qrscanner.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "qr_items_generate")
public class QrGenerateItem {

    //MADE BY SPIRAL BIT DEVELOPMENT
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String type;
    @ColumnInfo(name = "result_text")
    private String result;
    private Date date;
    private long time;

    public QrGenerateItem(String type, String result, Date date, long time) {
        this.type = type;
        this.result = result;
        this.date = date;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
