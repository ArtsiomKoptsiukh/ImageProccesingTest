package artifox.cry.by.imageproccesingtest;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ImageLab {
    private static ImageLab sImageUtil;

    private List<Image> mImages;

    private ImageLab(Context context) {
        mImages = new ArrayList<Image>();

        Image img1 = new Image();
        img1.setUrl(R.string.img_url_1);
        mImages.add(img1);

        Image img2 = new Image();
        img2.setUrl(R.string.img_url_2);
        mImages.add(img2);
    }

    public static ImageLab getInctanse(Context context) {
        if (sImageUtil == null) {
            sImageUtil = new ImageLab(context);
        }
        return sImageUtil;
    }

    public List<Image> getImages() {
        return mImages;
    }

    public Image getImage(UUID id) {
        for (Image image : mImages) {
            if (image.getImageID().equals(id)) {
                return image;
            }
        }
        return null;
    }
}
