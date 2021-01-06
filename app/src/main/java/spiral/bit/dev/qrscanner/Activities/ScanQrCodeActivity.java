package spiral.bit.dev.qrscanner.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import spiral.bit.dev.qrscanner.Adapters.QrRecyclerViewAdapter;
import spiral.bit.dev.qrscanner.Models.QrItem;
import spiral.bit.dev.qrscanner.Other.BaseActivity;
import spiral.bit.dev.qrscanner.R;
import spiral.bit.dev.qrscanner.ViewModels.QrItemViewModel;

import static spiral.bit.dev.qrscanner.Other.Helper.PICK_IMAGE;
import static spiral.bit.dev.qrscanner.Other.Helper.mailRegex;
import static spiral.bit.dev.qrscanner.Other.Helper.productRegex;
import static spiral.bit.dev.qrscanner.Other.Helper.regexMobilePhone;

public class ScanQrCodeActivity extends BaseActivity {

    //MADE BY SPIRAL BIT DEVELOPMENT

    private QrItemViewModel qrItemViewModel;
    private CodeScanner codeScanner;
    private QrRecyclerViewAdapter adapter = new QrRecyclerViewAdapter();
    private CodeScannerView scannView;
    private TextView resultData;
    private InterstitialAd mInterstitialAd;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        scannView = findViewById(R.id.my_scanner_view);
        codeScanner = new CodeScanner(this, scannView);
        resultData = findViewById(R.id.results_of_qr);
        resultData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doSmthWithQrResult();
            }
        });
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-7780453585001831/3469461865");
        pref = this.getSharedPreferences("showFirstTimeDialog", 0);
        editor = pref.edit();
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        qrItemViewModel = new ViewModelProvider(this).get(QrItemViewModel.class);
        qrItemViewModel.getAllQrItems().observe(this, new Observer<List<QrItem>>() {
            @Override
            public void onChanged(List<QrItem> qrItems) {
                adapter.submitList(qrItems);
            }
        });

        scannView.setFlashButtonColor(Color.YELLOW);
        scannView.setAutoFocusButtonColor(Color.GREEN);

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

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultData.setText(result.getText());
                        String resultMy = result.getText();
                        String type = "simple_text";
                        if (resultMy.length() >= 23 && resultMy.substring(0, 23).equals("https://www.youtube.com")
                                || resultMy.length() >= 22 && resultMy.substring(0, 22).equals("http://www.youtube.com")) {
                            type = "youtube";
                        } else if (resultMy.length() >= 5 && resultMy.substring(0, 5).equals("https") ||
                                resultMy.length() >= 5 && resultMy.substring(0, 4).equals("http")) {
                            type = "web";
                        } else if (resultMy.length() >= 3 && resultMy.substring(0, 3).equals("geo")) {
                            type = "geo_coord";
                        } else if (resultMy.length() >= 11 && resultMy.matches(productRegex)) {
                            type = "product";
                        } else if (resultMy.length() >= 20 && resultMy.matches(mailRegex)) {
                            type = "email";
                        } else if (resultMy.length() >= 11 && resultMy.matches(regexMobilePhone)) {
                            type = "contact";
                        } else {
                            type = "simple_text";
                        }
                        QrItem qrItem = new QrItem(type, resultMy, Calendar.getInstance().getTime(), Calendar.getInstance().getTime().getTime());
                        qrItemViewModel.insert(qrItem);

                        if (!pref.getBoolean("isShowed", true)) {
                            Toast.makeText(ScanQrCodeActivity.this, getString(R.string.tap_result_scan_hint_label), Toast.LENGTH_SHORT).show();
                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                Toast.makeText(ScanQrCodeActivity.this, getString(R.string.happens_error_toast), Toast.LENGTH_LONG).show();
                            }
                            doSmthWithQrResult();
                            editor.putBoolean("isShowed", true);
                            editor.apply();
                        }
                    }
                });
            }
        });
        scannView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            InputStream inputStream = null;
            try {
                inputStream = this.getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                Toast.makeText(this, getString(R.string.not_found_qr), Toast.LENGTH_SHORT).show();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            if (bitmap == null) {
                Toast.makeText(this, getString(R.string.not_qr_code_error), Toast.LENGTH_LONG).show();
            }
            int width = bitmap.getWidth(), height = bitmap.getHeight();
            int[] pixels = new int[width * height];
            bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            bitmap.recycle();
            bitmap = null;
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            BinaryBitmap bBitmap = new BinaryBitmap(new HybridBinarizer(source));
            MultiFormatReader reader = new MultiFormatReader();
            try {
                Result result = reader.decode(bBitmap);
                resultData.setText(result.getText());
                doSmthWithQrResult();
            } catch (NotFoundException e) {
                Toast.makeText(this, getString(R.string.not_found_qr), Toast.LENGTH_SHORT).show();
            }
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            } else {
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestForCamera();
    }

    public void requestForCamera() {
        Dexter.withActivity(this).withPermission(Manifest.permission.CAMERA).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                codeScanner.startPreview();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Toast.makeText(ScanQrCodeActivity.this, getString(R.string.permission_required_toast), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                token.continuePermissionRequest();
            }
        }).check();
    }


    public void doSmthWithQrResult() {
        String[] itemsForAlert = {"Открыть в браузере", "Написать", "Найти на карте", "Позвонить", "Очистить текст"};
        final String regexMobilePhone = "^((8|\\+7)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$";
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(ScanQrCodeActivity.this);
        builder.setTitle(R.string.what_you_want_to_do_with_result);
        builder.setItems(itemsForAlert, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!resultData.getText().toString().isEmpty()) {
                    if (i == 0) {
                        if (resultData.getText().toString().substring(0, 5).equals("https")) {
                            Intent intent = new Intent(ScanQrCodeActivity.this, OpenReferenceActivity.class);
                            intent.putExtra("ref", resultData.getText().toString());
                            startActivity(intent);
                        } else if (resultData.getText().toString().substring(0, 4).equals("http")) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(ScanQrCodeActivity.this);
                            builder1.setTitle(R.string.warning_word);
                            builder1.setMessage(R.string.warning_toast);
                            builder1.setPositiveButton(R.string.go_option, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(ScanQrCodeActivity.this, OpenReferenceActivity.class);
                                    intent.putExtra("ref", resultData.getText().toString());
                                    startActivity(intent);
                                }
                            }).setNegativeButton(R.string.cancel_option, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder1.show();
                        } else {
                            Toast.makeText(ScanQrCodeActivity.this, R.string.not_ref_error_toast, Toast.LENGTH_LONG).show();
                        }
                    } else if (i == 1) {
                        String mailto = resultData.getText().toString().trim();
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", mailto, null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Тема:");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Содержание:");
                        startActivity(Intent.createChooser(emailIntent, getString(R.string.sending_email_label)));
                    } else if (i == 2) {
                        String uri = resultData.getText().toString().trim();
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (uri.substring(0, 3).equals("geo")) {
                            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(mapIntent);
                            }
                        } else {
                            Toast.makeText(ScanQrCodeActivity.this, getString(R.string.not_place_error), Toast.LENGTH_SHORT).show();
                        }
                    } else if (i == 3) {
                        String uri = "tel:" + resultData.getText().toString().trim();
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(uri));
                        if (uri.substring(0, 3).equals("tel") || uri.equals(regexMobilePhone)) {
                            startActivity(intent);
                        } else {
                            Toast.makeText(ScanQrCodeActivity.this, "Это не контакт! \n Перепроверьте и попробуйте ещё раз.", Toast.LENGTH_SHORT).show();
                        }
                    } else if (i == 4) {
                        resultData.setText("");
                    }
                }
            }
        });
        builder.show();
    }

    public void scanFromGallery(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_qr_code)), PICK_IMAGE);
    }

    public void refreshScanView(View view) {
        codeScanner.startPreview();
    }
}