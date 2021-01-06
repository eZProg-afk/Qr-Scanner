package spiral.bit.dev.qrscanner.Other;

import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import spiral.bit.dev.qrscanner.Fragments.QrHistoryGenerateFragment;
import spiral.bit.dev.qrscanner.Fragments.QrHistoryScanFragment;

public class CustomScrollScanListener extends RecyclerView.OnScrollListener {

    public CustomScrollScanListener() {
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        switch (newState) {
            case RecyclerView.SCROLL_STATE_IDLE:
                System.out.println("The RecyclerView is not scrolling");
                break;
            case RecyclerView.SCROLL_STATE_DRAGGING:
                System.out.println("Scrolling now");
                break;
            case RecyclerView.SCROLL_STATE_SETTLING:
                System.out.println("Scroll Settling");
                break;
        }
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        if (dx > 0) {
            System.out.println("Scrolled Right");
        } else if (dx < 0) {
            System.out.println("Scrolled Left");
        } else {
            System.out.println("No Horizontal Scrolled");
        }

        if (dy > 0) {
            System.out.println("Scrolled Downwards");
            QrHistoryScanFragment.hideBottomNav();
        } else if (dy < 0) {
            System.out.println("Scrolled Upwards");
            QrHistoryScanFragment.unHideBottomNav();
        } else {
            System.out.println("No Vertical Scrolled");
        }
    }
}
