package tech.mobisuite.faceplace;

//View to place bitmap and add an overlay to display various landmarks

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.Landmark;

public class FaceFunctionsView extends View {

    int facewidth;
    int faceheight;

    private Bitmap mBitmap;
    private SparseArray<Face> mFaces; //since there can be number of faces in the picture
    int mSetnum; //set the number of necklace and earing

    public FaceFunctionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    } //default constructor

    /**
     * Sets the bitmap background and the associated face detections.
     */
    void setContent(Bitmap bitmap, SparseArray<Face> faces, int setnum) {
        mBitmap = bitmap;
        mFaces = faces;
        mSetnum = setnum;
        invalidate();
    }

    /**
     * Draws the bitmap background and the associated face landmarks.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ((mBitmap != null) && (mFaces != null)) {
            double scale = drawBitmap(canvas);
            placeFace(canvas, scale);
        }
    }

    /**
     * Draws the bitmap background, scaled to the device size.  Returns the scale for future use in
     * positioning the facial landmark graphics.
     */
    private double drawBitmap(Canvas canvas) {
        double viewWidth = canvas.getWidth();
        double viewHeight = canvas.getHeight();
        double imageWidth = mBitmap.getWidth();
        double imageHeight = mBitmap.getHeight();
        double scale = Math.min(viewWidth / imageWidth, viewHeight / imageHeight);

        Rect destBounds = new Rect(0, 0, (int)(imageWidth * scale), (int)(imageHeight * scale));
        canvas.drawBitmap(mBitmap, null, destBounds, null);
        return scale;
    }

    /**
     * Draws a small circle for each detected landmark, centered at the detected landmark position.
     * <p>
     *
     * Note that eye landmarks are defined to be the midpoint between the detected eye corner
     * positions, which tends to place the eye landmarks at the lower eyelid rather than at the
     * pupil position.
     */
    private void placeFace(Canvas canvas, double scale) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        // if ()

        for (int i = 0; i < mFaces.size(); ++i) {
            Face face = mFaces.valueAt(i);
            Bitmap icon = BitmapFactory.decodeResource(getContext().getResources(),
                    R.raw.earing);
            Bitmap iconq = BitmapFactory.decodeResource(getContext().getResources(),
                    R.raw.necklace);


            float x1 = face.getPosition().x;
            float y1 =face.getPosition().y;
            float x2 = x1 + face.getWidth();
            float y2 = y1 +face.getHeight();

            int widthofface = (int) x2;
            int heightofface = (int) y2;

            facewidth = widthofface;
            faceheight=heightofface;

            Log.v("facew",widthofface+"");
            Log.v("faceh",heightofface+"");




            //canvas.drawRoundRect(new RectF(x1, y1, x2, y2), 0, 0, paint);

            /*for (Landmark landmark : face.getLandmarks()) {
                if (landmark.getType()==Landmark.LEFT_EAR){
                    leftearpx = (int) (landmark.getPosition().x * scale);
                    leftearpy = (int) (landmark.getPosition().y * scale);
                }
                if (landmark.getType()==Landmark.RIGHT_EAR){
                    rightearpx = (int) (landmark.getPosition().x * scale);
                    rightearpy = (int) (landmark.getPosition().y * scale);
                }
                if (landmark.getType()==Landmark.)

            }*/



            for (Landmark landmark : face.getLandmarks()) {
                if (landmark.getType()==Landmark.LEFT_EAR || landmark.getType()==Landmark.RIGHT_EAR)
                    /*if (landmark.getType()==Landmark.LEFT_EAR)*/{
                    int cx = (int) (landmark.getPosition().x * scale);
                    int cy = (int) (landmark.getPosition().y * scale);

                    //scaling of earings

                    //float scaledearingwidth = (float) 0.062*facewidth;
                    //float scaledearingheight = (float) scaledearingwidth * 3;

                    float scaledearingheight = (float) 0.251*faceheight;
                    float scaledearingwidth = (float) scaledearingheight / 3;


                    int scaleearingwidthint = Math.round(scaledearingwidth);
                    int scaleearingheightint = Math.round(scaledearingheight);



                    Bitmap smallear = Bitmap.createScaledBitmap(icon, scaleearingwidthint, scaleearingheightint, false);

                    float imagewidth = smallear.getScaledWidth(canvas);
                    float imageheight = smallear.getScaledHeight(canvas);
                    canvas.drawBitmap(smallear, cx-imagewidth/2, cy, null);
                     //canvas.drawCircle(cx, cy, 10, paint);
                    //
                }
                if (landmark.getType()==Landmark.BOTTOM_MOUTH){
                    int cx = (int) (landmark.getPosition().x * scale);
                    int cy = (int) (landmark.getPosition().y * scale);
                    //canvas.drawBitmap(iconq, cx, cy+30, null);

                    //int necklacewidth = 220;
                    //double necklaceheightind= necklacewidth * 1.26 ;
                    //int necklaceheight = (int) necklaceheightind;

                    double necklaceheightinfloat = 0.38 * heightofface;
                    int necklaceheight = (int) necklaceheightinfloat;
                    double necklacewidthinfloat = necklaceheightinfloat/1.26;
                    int necklacewidth = (int) necklacewidthinfloat;



                    Bitmap smallneck = Bitmap.createScaledBitmap(iconq, necklacewidth, necklaceheight, false);


                    float imagewidth = smallneck.getScaledWidth(canvas);
                    float imageheight = smallneck.getScaledHeight(canvas);

                    double marginind = 0.251 * faceheight;
                    int margin = (int) marginind;

                    canvas.drawBitmap(smallneck,cx-imagewidth/2,cy-imageheight/2+margin , null);
                    //canvas.drawCircle(cx, cy, 10, paint);
                }
            }
        }
    }


}
