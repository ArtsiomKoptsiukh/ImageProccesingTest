package artifox.cry.by.imageproccesingtest;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.UUID;


public class ImageDetailFragment extends Fragment {
    private Image mImage;
    private ImageView mImageView;
    private ImageDownloader<ImageView> mImageDownloader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        UUID id = (UUID) getActivity().getIntent().getSerializableExtra("CHECKED_IMG");
        mImage = ImageLab.getInctanse(getActivity()).getImage(id);

        Handler responseHandler = new Handler();
        mImageDownloader = new ImageDownloader<>(responseHandler);
        mImageDownloader.setImageDownloadListener(
                new ImageDownloader.ImageDownloadListener<ImageView>() {
                    @Override
                    public void onImageDownloaded(ImageView imageView, Bitmap bitmap) {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        mImageView.setImageDrawable(drawable);
                    }
                }
        );
        mImageDownloader.start();
        mImageDownloader.getLooper();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_detail, container, false);

        mImageView = (ImageView) v.findViewById(R.id.detailed_image_view);
        mImageDownloader.queueThumbnail(mImageView, getString(mImage.getUrl()));

        Toast.makeText(getActivity(),"Image downloading",Toast.LENGTH_LONG).show();

        return v;
    }

}
