package spiral.bit.dev.qrscanner.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

import spiral.bit.dev.qrscanner.Models.QrItem;
import spiral.bit.dev.qrscanner.Activities.OpenReferenceActivity;
import spiral.bit.dev.qrscanner.R;
import spiral.bit.dev.qrscanner.ViewModels.QrItemViewModel;

import static spiral.bit.dev.qrscanner.Other.Helper.itemsForAlert;
import static spiral.bit.dev.qrscanner.Other.Helper.mailRegex;
import static spiral.bit.dev.qrscanner.Other.Helper.regexMobilePhone;

public class QrRecyclerViewAdapter extends ListAdapter<QrItem, QrRecyclerViewAdapter.QrViewHolder> {

    //MADE BY SPIRAL BIT DEVELOPMENT
    private OnItemClickListener listener;
    private Context context;

    public QrRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<QrItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<QrItem>() {
        @Override
        public boolean areItemsTheSame(QrItem oldItem, QrItem newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(QrItem oldItem, QrItem newItem) {
            return oldItem.getResult().equals(newItem.getResult());
        }
    };

    public QrItem getQrItemAt(int position) {
        return getItem(position);
    }

    @NonNull
    @Override
    public QrViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.qr_item, parent, false);
        context = parent.getContext();
        return new QrViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrViewHolder holder, int position) {
        final QrItem qrItem = getItem(position);
        if (qrItem.getResult().length() >= 30) {
            holder.resultTextView.setText(qrItem.getResult().substring(0, 25) + "...");
        } else {
            holder.resultTextView.setText(qrItem.getResult());
        }
        String type = qrItem.getType();
        if (type != null) {
            if (type.equals("youtube")) {
                holder.imageView.setImageResource(R.drawable.ic_youtube);
            } else if (type.equals("web")) {
                holder.imageView.setImageResource(R.drawable.ic_web);
            } else if (type.equals("simple_text")) {
                holder.imageView.setImageResource(R.drawable.simple_text);
            } else if (type.equals("geo_coord")) {
                holder.imageView.setImageResource(R.drawable.ic_geo_coord);
            } else if (type.equals("product")) {
                holder.imageView.setImageResource(R.drawable.ic_cart);
            } else if (type.equals("email")) {
                holder.imageView.setImageResource(R.drawable.ic_email);
            } else if (type.equals("contact")) {
                holder.imageView.setImageResource(R.drawable.ic_contact);
            }
        } else {
            holder.imageView.setImageResource(R.drawable.simple_text);
        }
        String formatedDate = new SimpleDateFormat("yyyy-MM-dd").format(qrItem.getDate());
        String formattedTime = new SimpleDateFormat("HH:ss").format(qrItem.getTime());
        String dateTime = formatedDate + "           " + formattedTime;
        holder.timeTextView.setText(dateTime);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.what_you_want_to_do_with_result);
                builder.setItems(itemsForAlert, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            if (holder.resultTextView.getText().toString().substring(0, 5).equals("https")) {
                                Intent intent = new Intent(context, OpenReferenceActivity.class);
                                intent.putExtra("ref", holder.resultTextView.getText().toString());
                                context.startActivity(intent);
                            } else if (holder.resultTextView.getText().toString().substring(0, 4).equals("http")) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                builder1.setTitle(R.string.warning_word);
                                builder1.setMessage(R.string.warning_toast);
                                builder1.setPositiveButton(R.string.go_option, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent intent = new Intent(context, OpenReferenceActivity.class);
                                        intent.putExtra("ref", holder.resultTextView.getText().toString());
                                        context.startActivity(intent);
                                    }
                                }).setNegativeButton(R.string.cancel_option, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                                builder1.show();
                            } else Toast.makeText(context, R.string.not_ref_error_toast, Toast.LENGTH_LONG).show();
                        } else if (i == 1) {
                            String mailto = holder.resultTextView.getText().toString().trim();
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto", mailto, null));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.subject));
                            emailIntent.putExtra(Intent.EXTRA_TEXT, context.getString(R.string.body));
                            if (mailto.matches(mailRegex)) {
                                context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.sending_email_label)));
                            } else Toast.makeText(context, context.getString(R.string.not_email_address_toast), Toast.LENGTH_LONG).show();
                        } else if (i == 2) {
                            String uri = holder.resultTextView.getText().toString().trim();
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            mapIntent.setPackage("com.google.android.apps.maps");
                            if (uri.substring(0, 3).equals("geo")) {
                                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                                    context.startActivity(mapIntent);
                                }
                            } else Toast.makeText(context, context.getString(R.string.not_place_error), Toast.LENGTH_SHORT).show();
                        } else if (i == 3) {
                            String uri = "tel:" + holder.resultTextView.getText().toString().trim();
                            Intent intent = new Intent(Intent.ACTION_CALL);
                            intent.setData(Uri.parse(uri));
                            if (uri.matches(regexMobilePhone)) {
                                context.startActivity(intent);
                            } else Toast.makeText(context, context.getString(R.string.not_contact_toast), Toast.LENGTH_SHORT).show();
                        } else if (i == 4) {
                            QrItemViewModel qrItemViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(QrItemViewModel.class);
                            qrItemViewModel.delete(qrItem);
                            Toast.makeText(context, context.getString(R.string.deleted_toast), Toast.LENGTH_SHORT).show();
                        } else dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                showFullImageWindow(holder, qrItem);
                return true;
            }
        });
    }

    private void showFullImageWindow(QrViewHolder holder, QrItem qrItem) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View fullImageWindow = inflater.inflate(R.layout.qr_item_full, null);
        dialog.setTitle(R.string.full_element_view_title);
        dialog.setView(fullImageWindow);
        TextView resultTextViewFull, dateTextViewFull, timeTextViewFull;
        ImageView imageViewFull;
        resultTextViewFull = fullImageWindow.findViewById(R.id.result_text_view_full);
        resultTextViewFull.setText(qrItem.getResult());
        imageViewFull = fullImageWindow.findViewById(R.id.qr_img_full);
        dateTextViewFull = fullImageWindow.findViewById(R.id.date_text_view_full);
        dateTextViewFull.setText(holder.timeTextView.getText().toString().substring(0, 10));
        timeTextViewFull = fullImageWindow.findViewById(R.id.time_text_view_full);
        timeTextViewFull.setText(holder.timeTextView.getText().toString().substring(10));
        imageViewFull.setImageDrawable(holder.imageView.getDrawable());
        dialog.setNegativeButton(
                R.string.close_option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        dialog.show();
    }

    class QrViewHolder extends RecyclerView.ViewHolder {

        public TextView resultTextView, timeTextView;
        public ImageView imageView;

        public QrViewHolder(@NonNull View itemView) {
            super(itemView);
            int position = getAdapterPosition();
            if (listener != null && position != RecyclerView.NO_POSITION) {
                listener.onItemClick(getItem(position));
            }
            imageView = itemView.findViewById(R.id.qr_img);
            resultTextView = itemView.findViewById(R.id.result_text_view);
            timeTextView = itemView.findViewById(R.id.time_text_view);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(QrItem qrItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}