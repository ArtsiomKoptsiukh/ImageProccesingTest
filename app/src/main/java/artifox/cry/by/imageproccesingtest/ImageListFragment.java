package artifox.cry.by.imageproccesingtest;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import java.util.List;

public class ImageListFragment extends Fragment {
    private static final String TAG = "ImageListFragment";
    private RecyclerView mImageRecyclerView;
    private ImageAdapter mAdapter;
    private ImageDownloader<ImageHolder> mImageDownloader;

    Intent mIntent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        Handler responseHandler = new Handler();
        mImageDownloader = new ImageDownloader<>(responseHandler);
        mImageDownloader.setImageDownloadListener(
                new ImageDownloader.ImageDownloadListener<ImageHolder>() {
                    @Override
                    public void onImageDownloaded(ImageHolder imageHolder, Bitmap bitmap) {
                        Drawable drawable = new BitmapDrawable(getResources(), bitmap);
                        imageHolder.bindDrawable(drawable);
                    }
                }
        );
        mImageDownloader.start();
        mImageDownloader.getLooper();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_image_list, container, false);

        mImageRecyclerView = (RecyclerView) v.findViewById(R.id.img_recycler_view);
        mImageRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mIntent);
            }
        });


        return v;
    }

    private void updateUI() {
        ImageLab imageUtil = ImageLab.getInctanse(getActivity());
        List<Image> images = imageUtil.getImages();

        mAdapter = new ImageAdapter(images);
        mImageRecyclerView.setAdapter(mAdapter);
    }

    private class ImageHolder extends RecyclerView.ViewHolder {

        ImageView mItemImageView;
        CheckBox mCheckBox;

        public ImageHolder(View itemView) {
            super(itemView);

            mItemImageView = (ImageView) itemView.findViewById(R.id.list_item_image_view);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_checkbox);

        }

        public void bindDrawable(Drawable drawable) {
            mItemImageView.setImageDrawable(drawable);
        }

    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {
        private List<Image> mImages;
        ///////////////////////////
        private CheckBox lastChecked = null;
        private int lastCheckedPos = 0;

        public ImageAdapter(List<Image> images) {
            mImages = images;
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.gallery_item, parent, false);

            return new ImageHolder(v);
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, int position) {
            final Image image = mImages.get(position);
            Drawable placeholder = getResources().getDrawable(R.drawable.img_01);
            holder.bindDrawable(placeholder);
            ////////////////////////////////
            holder.mCheckBox.setChecked(image.isChecked());
            holder.mCheckBox.setTag(new Integer(position));

            if (position == 0 && image.isChecked() && holder.mCheckBox.isChecked()) {
                lastChecked = holder.mCheckBox;
                lastCheckedPos = 0;
            }

            holder.mCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    int clickedPos = ((Integer) cb.getTag()).intValue();

                    if (cb.isChecked()) {
                        if (lastChecked != null) {
                            lastChecked.setChecked(false);
                            image.setChecked(false);
                        }

                        lastChecked = cb;
                        lastCheckedPos = clickedPos;
                    } else
                        lastChecked = null;

                    image.setChecked(cb.isChecked());

                    mIntent = new Intent(getActivity(), ImageDetailActivity.class);
                    mIntent.putExtra("CHECKED_IMG", image.getImageID());
                }
            });

            mImageDownloader.queueThumbnail(holder, getString(image.getUrl()));

        }

        @Override
        public int getItemCount() {
            return mImages.size();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mImageDownloader.quit();
        Log.i(TAG, "Background thread destroyed");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mImageDownloader.clearQueue();
    }
}
