package jp.ac.titech.itpro.sdl.hilbert;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class HilbertView extends View {

    public final static int MAX_ORDER = 9;

    private int order = 1;
    private List<Future<Bitmap>> bitmapFutures;

    private final ExecutorService exec = Executors.newCachedThreadPool();
    private final Paint paint = new Paint();

    public HilbertView(Context context) {
        this(context, null);
    }

    public HilbertView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HilbertView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        ExecutorService exec = Executors.newCachedThreadPool();
        bitmapFutures = new ArrayList<>(MAX_ORDER + 1);
        bitmapFutures.add(null); // index: 0
        for (int i=1; i<MAX_ORDER + 1; i++) {
            bitmapFutures.add(exec.submit(new BitmapCallable(getMeasuredWidth(), getMeasuredHeight(), i)));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            Bitmap bitmap = bitmapFutures.get(order).get();
            canvas.drawBitmap(bitmap, 0, 0, paint);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        bitmapFutures = new ArrayList<>(MAX_ORDER + 1);
        bitmapFutures.add(null); // index: 0
        for (int i=1; i<MAX_ORDER + 1; i++) {
            bitmapFutures.add(exec.submit(new BitmapCallable(getMeasuredWidth(), getMeasuredHeight(), i)));
        }
    }

    public void setOrder(int n) {
        order = n;
        invalidate();
    }
}
