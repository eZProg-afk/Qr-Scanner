package spiral.bit.dev.qrscanner.Activities;

import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import spiral.bit.dev.qrscanner.Adapters.QrGenerateRecyclerViewAdapter;
import spiral.bit.dev.qrscanner.Models.QrGenerateItem;
import spiral.bit.dev.qrscanner.Other.BaseActivity;
import spiral.bit.dev.qrscanner.Other.Helper;
import spiral.bit.dev.qrscanner.R;
import spiral.bit.dev.qrscanner.ViewModels.QrGenerateItemViewModel;

import static spiral.bit.dev.qrscanner.Other.Helper.hideKeyboard;
import static spiral.bit.dev.qrscanner.Other.Helper.optionActionsWithQr;

public class QrGenerateActivity extends BaseActivity {

    //MADE BY SPIRAL BIT DEVELOPMENT

    EditText qrvalue;
    Button generateBtn;
    ImageView qrImage;
    WallpaperManager manager;
    QrGenerateItemViewModel qrItemViewModel;
    QrGenerateRecyclerViewAdapter adapter;
    private Spinner spinner;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_generate);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7780453585001831/6913882856");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        qrvalue = findViewById(R.id.my_qr_input);
        generateBtn = findViewById(R.id.generate_btn);
        qrImage = findViewById(R.id.my_qr_place_holder);
        manager = WallpaperManager.getInstance(this);
        adapter = new QrGenerateRecyclerViewAdapter();
        spinner = findViewById(R.id.spinner);
        ArrayAdapter<?> adapterSpinner =
                ArrayAdapter.createFromResource(this, R.array.type_of_qr_code, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
        qrItemViewModel = new ViewModelProvider(this).get(QrGenerateItemViewModel.class);
        qrItemViewModel.getAllQrItems().observe(this, new Observer<List<QrGenerateItem>>() {
            @Override
            public void onChanged(List<QrGenerateItem> qrItems) {
                adapter.submitList(qrItems);
            }
        });
        setUpBottomMenu();

        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = qrvalue.getText().toString();
                if (data.isEmpty()) {
                    qrvalue.setError(getString(R.string.error_empty_field));
                } else {
                    QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 500);
                    try {
                        Bitmap bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Toast.makeText(QrGenerateActivity.this, R.string.not_qr_code_error, Toast.LENGTH_SHORT).show();
                    }
                    if (mInterstitialAd.isLoaded()) mInterstitialAd.show();
                    String selected = spinner.getSelectedItem().toString();
                    String type;
                    if (selected.equals(getString(R.string.simple_text))) {
                        type = "simple_text";
                    } else if (selected.equals(getString(R.string.ref_on_video))) {
                        type = "youtube";
                    } else if (selected.equals(getString(R.string.ref_on_web))) {
                        type = "web";
                    } else if (selected.equals(getString(R.string.geo_coord))) {
                        type = "geo_coord";
                    } else if (selected.equals(getString(R.string.product))) {
                        type = "product";
                    } else if (selected.equals(getString(R.string.contact))) {
                        type = "contact";
                    } else if (selected.equals(getString(R.string.e_mail))) {
                        type = "email";
                    } else {
                        type = "simple_text";
                    }
                    QrGenerateItem qrItem = new QrGenerateItem(type, data, Calendar.getInstance().getTime(), Calendar.getInstance().getTime().getTime());
                    qrItemViewModel.insert(qrItem);
                    Toast.makeText(QrGenerateActivity.this, R.string.saved_toast, Toast.LENGTH_SHORT).show();
                    hideKeyboard(QrGenerateActivity.this);
                    qrvalue.setText("");
                    spinner.setSelection(0);
                }
            }
        });

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdClicked() {
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdClosed() {
            }
        });

        qrImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(getString(R.string.what_you_want_to_do_title));
                builder.setItems(optionActionsWithQr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Bitmap bitmap = ((BitmapDrawable) qrImage.getDrawable()).getBitmap();
                            ContentValues values = new ContentValues();
                            values.put(MediaStore.Images.Media.TITLE, "any_picture_name");
                            values.put(MediaStore.Images.Media.BUCKET_ID, "test");
                            values.put(MediaStore.Images.Media.DESCRIPTION, "test Image taken");
                            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            SharedPreferences pref = getSharedPreferences("generate", 0);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("uri", String.valueOf(uri));
                            editor.apply();
                            OutputStream outstream;
                            try {
                                outstream = getContentResolver().openOutputStream(uri);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outstream);
                                outstream.close();
                                Toast.makeText(getApplicationContext(), getString(R.string.successfully_downloaded_toast), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), getString(R.string.failure_downloaded_toast), Toast.LENGTH_LONG).show();
                            }
                        } else if (i == 1) {
                            Bitmap bitmap = ((BitmapDrawable) qrImage.getDrawable()).getBitmap();
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/jpeg");
                            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                            String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                                    bitmap, "ahah", null);
                            Uri imageUri = Uri.parse(path);
                            share.putExtra(Intent.EXTRA_STREAM, imageUri);
                            startActivity(Intent.createChooser(share, getString(R.string.select_whom_send_file)));
                        } else if (i == 2) {
                            qrImage.buildDrawingCache();
                            Bitmap bmap = qrImage.getDrawingCache();
                            DisplayMetrics metrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(metrics);
                            int phoneHeight = metrics.heightPixels;
                            int phoneWidth = metrics.widthPixels;
                            Bitmap mBit = Helper.returnBitmap(bmap, phoneWidth, phoneHeight);
                            try {
                                manager.setBitmap(mBit);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else if (i == 3) {
                            dialogInterface.dismiss();
                        } else {
                            dialogInterface.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });
    }
}