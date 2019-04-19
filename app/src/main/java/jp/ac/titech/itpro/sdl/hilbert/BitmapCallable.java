package jp.ac.titech.itpro.sdl.hilbert;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.concurrent.Callable;

public class BitmapCallable implements Callable<Bitmap> {
    private final Bitmap bitmap;
    private final int order;

    BitmapCallable(int width, int height, int order) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        this.order = order;
    }

    @Override
    public Bitmap call() {
        final Canvas canvas = new Canvas(bitmap);
        final Paint paint = new Paint();
        HilbertTurtle turtle = new HilbertTurtle(new Turtle.Drawer() {
            @Override
            public void drawLine(double x0, double y0, double x1, double y1) {
                canvas.drawLine((float) x0, (float) y0, (float) x1, (float) y1, paint);
            }
        });

        int w = canvas.getWidth();
        int h = canvas.getHeight();
        paint.setColor(Color.DKGRAY);
        canvas.drawRect(0, 0, w, h, paint);

        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(3);
        int size = Math.min(w, h);
        double step = (double) size / (1 << order);
        turtle.setPos((w - size + step) / 2, (h + size - step) / 2);
        turtle.setDir(HilbertTurtle.E);
        turtle.draw(order, step, HilbertTurtle.R);

        return bitmap;
    }
}
