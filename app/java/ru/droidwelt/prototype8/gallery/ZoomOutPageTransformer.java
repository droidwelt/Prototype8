package ru.droidwelt.prototype8.gallery;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.5f;
    private static final float MIN_ALPHA = 0.3f;

    @Override
    public void transformPage(@NonNull View view, float position) {
        int pageWidth = view.getWidth();
        int pageHeight = view.getHeight();

        if (position < -1) { // [-Infinity,-1)
            // Эта страница за экраном слева
            view.setAlpha(0);

        } else if (position <= 1) { // [-1,1]
            // Меняем стандартный переход между страницами
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;
            if (position < 0) {
                view.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                view.setTranslationX(-horzMargin + vertMargin / 2);
            }

            // Уменьшаем масштаб (между MIN_SCALE и 1)
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

            // Скрываем страницу, относительно ее размера
            view.setAlpha(MIN_ALPHA +
                    (scaleFactor - MIN_SCALE) /
                            (1 - MIN_SCALE) * (1 - MIN_ALPHA));

        } else { // (1,+Infinity]
            // Эта страница за экраном справа
            view.setAlpha(0);
        }
    }


}
