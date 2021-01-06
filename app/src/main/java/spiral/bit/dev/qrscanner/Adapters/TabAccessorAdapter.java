package spiral.bit.dev.qrscanner.Adapters;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import spiral.bit.dev.qrscanner.Fragments.QrHistoryGenerateFragment;
import spiral.bit.dev.qrscanner.Fragments.QrHistoryScanFragment;

public class TabAccessorAdapter extends FragmentPagerAdapter {

    public TabAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                return new QrHistoryScanFragment();
            case 1:
                return new QrHistoryGenerateFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Сканер";
            case 1:
                return "Генерация";
            default:
                return null;
        }
    }
}