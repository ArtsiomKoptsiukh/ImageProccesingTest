package artifox.cry.by.imageproccesingtest;


import android.support.v4.app.Fragment;

public class ImageListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new ImageListFragment();
    }
}
