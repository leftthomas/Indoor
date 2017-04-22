package com.indoor.im.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;

import com.gitonway.lee.niftynotification.lib.Effects;
import com.indoor.im.R;
import com.indoor.im.bean.Blog;
import com.indoor.im.bean.User;
import com.indoor.im.config.BmobConstants;
import com.indoor.im.util.PhotoUtil;
import com.indoor.im.view.HeaderLayout.onRightImageButtonClickListener;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import HaoRan.ImageFilter.AutoAdjustFilter;
import HaoRan.ImageFilter.BannerFilter;
import HaoRan.ImageFilter.BigBrotherFilter;
import HaoRan.ImageFilter.BlackWhiteFilter;
import HaoRan.ImageFilter.BlindFilter;
import HaoRan.ImageFilter.BlockPrintFilter;
import HaoRan.ImageFilter.BrickFilter;
import HaoRan.ImageFilter.BrightContrastFilter;
import HaoRan.ImageFilter.CleanGlassFilter;
import HaoRan.ImageFilter.ColorQuantizeFilter;
import HaoRan.ImageFilter.ColorToneFilter;
import HaoRan.ImageFilter.ComicFilter;
import HaoRan.ImageFilter.Distort.BulgeFilter;
import HaoRan.ImageFilter.Distort.RippleFilter;
import HaoRan.ImageFilter.Distort.TwistFilter;
import HaoRan.ImageFilter.Distort.WaveFilter;
import HaoRan.ImageFilter.EdgeFilter;
import HaoRan.ImageFilter.FeatherFilter;
import HaoRan.ImageFilter.FillPatternFilter;
import HaoRan.ImageFilter.FilmFilter;
import HaoRan.ImageFilter.FocusFilter;
import HaoRan.ImageFilter.GammaFilter;
import HaoRan.ImageFilter.GaussianBlurFilter;
import HaoRan.ImageFilter.Gradient;
import HaoRan.ImageFilter.HslModifyFilter;
import HaoRan.ImageFilter.IImageFilter;
import HaoRan.ImageFilter.IllusionFilter;
import HaoRan.ImageFilter.Image;
import HaoRan.ImageFilter.InvertFilter;
import HaoRan.ImageFilter.LensFlareFilter;
import HaoRan.ImageFilter.LightFilter;
import HaoRan.ImageFilter.LomoFilter;
import HaoRan.ImageFilter.MirrorFilter;
import HaoRan.ImageFilter.MistFilter;
import HaoRan.ImageFilter.MonitorFilter;
import HaoRan.ImageFilter.MosaicFilter;
import HaoRan.ImageFilter.NeonFilter;
import HaoRan.ImageFilter.NightVisionFilter;
import HaoRan.ImageFilter.NoiseFilter;
import HaoRan.ImageFilter.OilPaintFilter;
import HaoRan.ImageFilter.OldPhotoFilter;
import HaoRan.ImageFilter.PaintBorderFilter;
import HaoRan.ImageFilter.PixelateFilter;
import HaoRan.ImageFilter.PosterizeFilter;
import HaoRan.ImageFilter.RadialDistortionFilter;
import HaoRan.ImageFilter.RainBowFilter;
import HaoRan.ImageFilter.RaiseFrameFilter;
import HaoRan.ImageFilter.RectMatrixFilter;
import HaoRan.ImageFilter.ReflectionFilter;
import HaoRan.ImageFilter.ReliefFilter;
import HaoRan.ImageFilter.SaturationModifyFilter;
import HaoRan.ImageFilter.SceneFilter;
import HaoRan.ImageFilter.SepiaFilter;
import HaoRan.ImageFilter.SharpFilter;
import HaoRan.ImageFilter.ShiftFilter;
import HaoRan.ImageFilter.SmashColorFilter;
import HaoRan.ImageFilter.SoftGlowFilter;
import HaoRan.ImageFilter.SupernovaFilter;
import HaoRan.ImageFilter.Textures.CloudsTexture;
import HaoRan.ImageFilter.Textures.LabyrinthTexture;
import HaoRan.ImageFilter.Textures.MarbleTexture;
import HaoRan.ImageFilter.Textures.TextileTexture;
import HaoRan.ImageFilter.Textures.TexturerFilter;
import HaoRan.ImageFilter.Textures.WoodTexture;
import HaoRan.ImageFilter.ThreeDGridFilter;
import HaoRan.ImageFilter.ThresholdFilter;
import HaoRan.ImageFilter.TileReflectionFilter;
import HaoRan.ImageFilter.TintFilter;
import HaoRan.ImageFilter.VideoFilter;
import HaoRan.ImageFilter.VignetteFilter;
import HaoRan.ImageFilter.VintageFilter;
import HaoRan.ImageFilter.WaterWaveFilter;
import HaoRan.ImageFilter.XRadiationFilter;
import HaoRan.ImageFilter.YCBCrLinearFilter;
import HaoRan.ImageFilter.ZoomBlurFilter;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

@SuppressWarnings("deprecation")
public class PublishStatusActivity extends ActivityBase {

    protected static final int PICK_CODE = 0x110;
    private CircleProgressBar progress;
    private ImageView mphoto;
    private EditText brief;
    private Bitmap mPhotoImg;
    private String mCurrentPhotoStr = "";
    private Bitmap finalphoto;
    private int whatphoto = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_status);
        mphoto = (ImageView) findViewById(R.id.photo);
        progress = (CircleProgressBar) findViewById(R.id.progress);
        brief = (EditText) findViewById(R.id.brief);
        progress.setVisibility(View.GONE);
        mphoto.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PICK_CODE);
            }
        });
        initTopBarForBoth("发布动态", R.drawable.base_action_bar_true_bg_n, new onRightImageButtonClickListener() {

            @Override
            public void onClick() {
                InputMethodManager manager = ((InputMethodManager) PublishStatusActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE));
                if (getCurrentFocus() != null)
                    manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if (mCurrentPhotoStr.equals("")) {
                    showTag("请选择图片！", Effects.jelly, R.id.tagview);
                } else if (brief.getText().toString().equals("")) {
                    showTag("请填写文字！", Effects.jelly, R.id.tagview);
                } else {
                    addBlog();
                }
            }
        });
        LoadImageFilter();
    }

    /**
     * 加载图片filter
     */
    private void LoadImageFilter() {
        Gallery gallery = (Gallery) findViewById(R.id.galleryFilter);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        MarginLayoutParams mlp = (MarginLayoutParams) gallery.getLayoutParams();
        mlp.setMargins(-(metrics.widthPixels / 2 + 400), mlp.topMargin, mlp.rightMargin,
                mlp.bottomMargin);
        gallery.setLayoutParams(mlp);
        final ImageFilterAdapter filterAdapter = new ImageFilterAdapter(this);
        gallery.setAdapter(new ImageFilterAdapter(this));
        gallery.setSelection(0);
        gallery.setAnimationDuration(3000);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                whatphoto = position;
                if (mCurrentPhotoStr.equals("")) {
                    showTag("请选择图片！", Effects.jelly, R.id.tagview);
                }
                //因为第一种情况不做图像处理
                else if (position == 0) {
                    mphoto.setImageBitmap(mPhotoImg);
                } else {
                    IImageFilter filter = (IImageFilter) filterAdapter.getItem(position);
                    new processImageTask(PublishStatusActivity.this, filter).execute();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        if (arg0 == PICK_CODE) {
            if (arg2 != null) {
                Uri uri = arg2.getData();
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                mCurrentPhotoStr = cursor.getString(idx);
                cursor.close();
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(mCurrentPhotoStr, options);
                double ratio = Math.max(options.outWidth * 1.0d / 1024f, options.outHeight * 1.0d / 1024f);
                options.inSampleSize = (int) Math.ceil(ratio);
                options.inJustDecodeBounds = false;
                mPhotoImg = BitmapFactory.decodeFile(mCurrentPhotoStr, options);
                mphoto.setImageBitmap(mPhotoImg);
            }
        }
        super.onActivityResult(arg0, arg1, arg2);
    }

    /**
     * 发布动态
     *
     * @param
     * @return void
     * @throws
     * @Title: addBlog
     * @Description: TODO
     */
    public void addBlog() {
        progress.setVisibility(View.VISIBLE);
        if (whatphoto != 0) {
            // 保存图片
            String filename = new SimpleDateFormat("yyMMddHHmmss").format(new Date()) + ".png";
            String path = BmobConstants.BMOB_PICTURE_PATH + filename;
            PhotoUtil.saveBitmap(BmobConstants.BMOB_PICTURE_PATH, filename, finalphoto, true);
            mCurrentPhotoStr = path;
        }
        User current = (User) userManager.getCurrentUser(User.class);
        final Blog blog = new Blog();
        blog.setBrief(brief.getText().toString());
        //添加一对一关联
        blog.setAuthor(current);
        final BmobFile bmobFile = new BmobFile(new File(mCurrentPhotoStr));
        bmobFile.uploadblock(this, new UploadFileListener() {

            @Override
            public void onSuccess() {
                blog.setPhoto(bmobFile);
                blog.save(PublishStatusActivity.this, new SaveListener() {

                    @Override
                    public void onSuccess() {
                        progress.setVisibility(View.GONE);
                        finish();
                    }

                    @Override
                    public void onFailure(int arg0, String arg1) {

                    }
                });
            }

            @Override
            public void onProgress(Integer value) {

            }

            @Override
            public void onFailure(int code, String msg) {

            }
        });
    }

    ;

    public class processImageTask extends AsyncTask<Void, Void, Bitmap> {
        private IImageFilter filter;
        private Activity activity = null;

        public processImageTask(Activity activity, IImageFilter imageFilter) {
            this.filter = imageFilter;
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        public Bitmap doInBackground(Void... params) {
            Image img = null;
            try {
                img = new Image(mPhotoImg);
                if (filter != null) {
                    img = filter.process(img);
                    img.copyPixelsFromBuffer();
                }
                return img.getImage();
            } catch (Exception e) {
                if (img != null && img.destImage.isRecycled()) {
                    img.destImage.recycle();
                    img.destImage = null;
                    System.gc(); // 提醒系统及时回收
                }
            } finally {
                if (img != null && img.image.isRecycled()) {
                    img.image.recycle();
                    img.image = null;
                    System.gc(); // 提醒系统及时回收
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                super.onPostExecute(result);
                mphoto.setImageBitmap(result);
                finalphoto = result;
            }
            progress.setVisibility(View.GONE);
        }
    }

    public class ImageFilterAdapter extends BaseAdapter {
        private Context mContext;
        private List<FilterInfo> filterArray = new ArrayList<FilterInfo>();

        public ImageFilterAdapter(Context c) {
            mContext = c;

            //99种效果
            filterArray.add(new FilterInfo(R.drawable.saturationmodity_filter, null/* 此处会生成原图效果 */));
            //v0.4
            filterArray.add(new FilterInfo(R.drawable.video_filter1, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_STAGGERED)));
            filterArray.add(new FilterInfo(R.drawable.video_filter2, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_TRIPED)));
            filterArray.add(new FilterInfo(R.drawable.video_filter3, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_3X3)));
            filterArray.add(new FilterInfo(R.drawable.video_filter4, new VideoFilter(VideoFilter.VIDEO_TYPE.VIDEO_DOTS)));
            filterArray.add(new FilterInfo(R.drawable.tilereflection_filter1, new TileReflectionFilter(20, 8, 45, (byte) 1)));
            filterArray.add(new FilterInfo(R.drawable.tilereflection_filter2, new TileReflectionFilter(20, 8, 45, (byte) 2)));
            filterArray.add(new FilterInfo(R.drawable.fillpattern_filter, new FillPatternFilter(PublishStatusActivity.this, R.drawable.texture1)));
            filterArray.add(new FilterInfo(R.drawable.fillpattern_filter1, new FillPatternFilter(PublishStatusActivity.this, R.drawable.texture2)));
            filterArray.add(new FilterInfo(R.drawable.mirror_filter1, new MirrorFilter(true)));
            filterArray.add(new FilterInfo(R.drawable.mirror_filter2, new MirrorFilter(false)));
            filterArray.add(new FilterInfo(R.drawable.ycb_crlinear_filter, new YCBCrLinearFilter(new YCBCrLinearFilter.Range(-0.3f, 0.3f))));
            filterArray.add(new FilterInfo(R.drawable.ycb_crlinear_filter2, new YCBCrLinearFilter(new YCBCrLinearFilter.Range(-0.276f, 0.163f), new YCBCrLinearFilter.Range(-0.202f, 0.5f))));
            filterArray.add(new FilterInfo(R.drawable.texturer_filter, new TexturerFilter(new CloudsTexture(), 0.8f, 0.8f)));
            filterArray.add(new FilterInfo(R.drawable.texturer_filter1, new TexturerFilter(new LabyrinthTexture(), 0.8f, 0.8f)));
            filterArray.add(new FilterInfo(R.drawable.texturer_filter2, new TexturerFilter(new MarbleTexture(), 1.8f, 0.8f)));
            filterArray.add(new FilterInfo(R.drawable.texturer_filter3, new TexturerFilter(new WoodTexture(), 0.8f, 0.8f)));
            filterArray.add(new FilterInfo(R.drawable.texturer_filter4, new TexturerFilter(new TextileTexture(), 0.8f, 0.8f)));
            filterArray.add(new FilterInfo(R.drawable.hslmodify_filter, new HslModifyFilter(20f)));
            filterArray.add(new FilterInfo(R.drawable.hslmodify_filter0, new HslModifyFilter(40f)));
            filterArray.add(new FilterInfo(R.drawable.hslmodify_filter1, new HslModifyFilter(60f)));
            filterArray.add(new FilterInfo(R.drawable.hslmodify_filter2, new HslModifyFilter(80f)));
            filterArray.add(new FilterInfo(R.drawable.hslmodify_filter3, new HslModifyFilter(100f)));
            filterArray.add(new FilterInfo(R.drawable.hslmodify_filter4, new HslModifyFilter(150f)));
            filterArray.add(new FilterInfo(R.drawable.hslmodify_filter5, new HslModifyFilter(200f)));
            filterArray.add(new FilterInfo(R.drawable.hslmodify_filter6, new HslModifyFilter(250f)));
            filterArray.add(new FilterInfo(R.drawable.hslmodify_filter7, new HslModifyFilter(300f)));

            //v0.3
            filterArray.add(new FilterInfo(R.drawable.zoomblur_filter, new ZoomBlurFilter(30)));
            filterArray.add(new FilterInfo(R.drawable.threedgrid_filter, new ThreeDGridFilter(16, 100)));
            filterArray.add(new FilterInfo(R.drawable.colortone_filter, new ColorToneFilter(Color.rgb(33, 168, 254), 192)));
            filterArray.add(new FilterInfo(R.drawable.colortone_filter2, new ColorToneFilter(0x00FF00, 192)));//green
            filterArray.add(new FilterInfo(R.drawable.colortone_filter3, new ColorToneFilter(0xFF0000, 192)));//blue
            filterArray.add(new FilterInfo(R.drawable.colortone_filter4, new ColorToneFilter(0x00FFFF, 192)));//yellow
            filterArray.add(new FilterInfo(R.drawable.softglow_filter, new SoftGlowFilter(10, 0.1f, 0.1f)));
            filterArray.add(new FilterInfo(R.drawable.tilereflection_filter, new TileReflectionFilter(20, 8)));
            filterArray.add(new FilterInfo(R.drawable.blind_filter1, new BlindFilter(true, 96, 100, 0xffffff)));
            filterArray.add(new FilterInfo(R.drawable.blind_filter2, new BlindFilter(false, 96, 100, 0x000000)));
            filterArray.add(new FilterInfo(R.drawable.raiseframe_filter, new RaiseFrameFilter(20)));
            filterArray.add(new FilterInfo(R.drawable.shift_filter, new ShiftFilter(10)));
            filterArray.add(new FilterInfo(R.drawable.wave_filter, new WaveFilter(25, 10)));
            filterArray.add(new FilterInfo(R.drawable.bulge_filter, new BulgeFilter(-97)));
            filterArray.add(new FilterInfo(R.drawable.twist_filter, new TwistFilter(27, 106)));
            filterArray.add(new FilterInfo(R.drawable.ripple_filter, new RippleFilter(38, 15, true)));
            filterArray.add(new FilterInfo(R.drawable.illusion_filter, new IllusionFilter(3)));
            filterArray.add(new FilterInfo(R.drawable.supernova_filter, new SupernovaFilter(0x00FFFF, 20, 100)));
            filterArray.add(new FilterInfo(R.drawable.lensflare_filter, new LensFlareFilter()));
            filterArray.add(new FilterInfo(R.drawable.posterize_filter, new PosterizeFilter(2)));
            filterArray.add(new FilterInfo(R.drawable.gamma_filter, new GammaFilter(50)));
            filterArray.add(new FilterInfo(R.drawable.sharp_filter, new SharpFilter()));

            //v0.2
            filterArray.add(new FilterInfo(R.drawable.invert_filter, new ComicFilter()));
            filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene())));//green
            filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene1())));//purple
            filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene2())));//blue
            filterArray.add(new FilterInfo(R.drawable.invert_filter, new SceneFilter(5f, Gradient.Scene3())));
            filterArray.add(new FilterInfo(R.drawable.invert_filter, new FilmFilter(80f)));
            filterArray.add(new FilterInfo(R.drawable.invert_filter, new FocusFilter()));
            filterArray.add(new FilterInfo(R.drawable.invert_filter, new CleanGlassFilter()));
            filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0x00FF00)));//green
            filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0x00FFFF)));//yellow
            filterArray.add(new FilterInfo(R.drawable.invert_filter, new PaintBorderFilter(0xFF0000)));//blue
            filterArray.add(new FilterInfo(R.drawable.invert_filter, new LomoFilter()));

            //v0.1
            filterArray.add(new FilterInfo(R.drawable.invert_filter, new InvertFilter()));
            filterArray.add(new FilterInfo(R.drawable.blackwhite_filter, new BlackWhiteFilter()));
            filterArray.add(new FilterInfo(R.drawable.edge_filter, new EdgeFilter()));
            filterArray.add(new FilterInfo(R.drawable.pixelate_filter, new PixelateFilter()));
            filterArray.add(new FilterInfo(R.drawable.neon_filter, new NeonFilter()));
            filterArray.add(new FilterInfo(R.drawable.bigbrother_filter, new BigBrotherFilter()));
            filterArray.add(new FilterInfo(R.drawable.monitor_filter, new MonitorFilter()));
            filterArray.add(new FilterInfo(R.drawable.relief_filter, new ReliefFilter()));
            filterArray.add(new FilterInfo(R.drawable.brightcontrast_filter, new BrightContrastFilter()));
            filterArray.add(new FilterInfo(R.drawable.saturationmodity_filter, new SaturationModifyFilter()));
            filterArray.add(new FilterInfo(R.drawable.threshold_filter, new ThresholdFilter()));
            filterArray.add(new FilterInfo(R.drawable.noisefilter, new NoiseFilter()));
            filterArray.add(new FilterInfo(R.drawable.banner_filter1, new BannerFilter(10, true)));
            filterArray.add(new FilterInfo(R.drawable.banner_filter2, new BannerFilter(10, false)));
            filterArray.add(new FilterInfo(R.drawable.rectmatrix_filter, new RectMatrixFilter()));
            filterArray.add(new FilterInfo(R.drawable.blockprint_filter, new BlockPrintFilter()));
            filterArray.add(new FilterInfo(R.drawable.brick_filter, new BrickFilter()));
            filterArray.add(new FilterInfo(R.drawable.gaussianblur_filter, new GaussianBlurFilter()));
            filterArray.add(new FilterInfo(R.drawable.light_filter, new LightFilter()));
            filterArray.add(new FilterInfo(R.drawable.mosaic_filter, new MistFilter()));
            filterArray.add(new FilterInfo(R.drawable.mosaic_filter, new MosaicFilter()));
            filterArray.add(new FilterInfo(R.drawable.oilpaint_filter, new OilPaintFilter()));
            filterArray.add(new FilterInfo(R.drawable.radialdistortion_filter, new RadialDistortionFilter()));
            filterArray.add(new FilterInfo(R.drawable.reflection1_filter, new ReflectionFilter(true)));
            filterArray.add(new FilterInfo(R.drawable.reflection2_filter, new ReflectionFilter(false)));
            filterArray.add(new FilterInfo(R.drawable.saturationmodify_filter, new SaturationModifyFilter()));
            filterArray.add(new FilterInfo(R.drawable.smashcolor_filter, new SmashColorFilter()));
            filterArray.add(new FilterInfo(R.drawable.tint_filter, new TintFilter()));
            filterArray.add(new FilterInfo(R.drawable.vignette_filter, new VignetteFilter()));
            filterArray.add(new FilterInfo(R.drawable.autoadjust_filter, new AutoAdjustFilter()));
            filterArray.add(new FilterInfo(R.drawable.colorquantize_filter, new ColorQuantizeFilter()));
            filterArray.add(new FilterInfo(R.drawable.waterwave_filter, new WaterWaveFilter()));
            filterArray.add(new FilterInfo(R.drawable.vintage_filter, new VintageFilter()));
            filterArray.add(new FilterInfo(R.drawable.oldphoto_filter, new OldPhotoFilter()));
            filterArray.add(new FilterInfo(R.drawable.sepia_filter, new SepiaFilter()));
            filterArray.add(new FilterInfo(R.drawable.rainbow_filter, new RainBowFilter()));
            filterArray.add(new FilterInfo(R.drawable.feather_filter, new FeatherFilter()));
            filterArray.add(new FilterInfo(R.drawable.xradiation_filter, new XRadiationFilter()));
            filterArray.add(new FilterInfo(R.drawable.nightvision_filter, new NightVisionFilter()));
        }

        public int getCount() {
            return filterArray.size();
        }

        public Object getItem(int position) {
            return position < filterArray.size() ? filterArray.get(position).filter
                    : null;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            Bitmap bmImg = BitmapFactory
                    .decodeResource(mContext.getResources(),
                            filterArray.get(position).filterID);
            int width = 200;// bmImg.getWidth();
            int height = 200;// bmImg.getHeight();
            bmImg.recycle();
            ImageView imageview = new ImageView(mContext);
            imageview.setImageResource(filterArray.get(position).filterID);
            imageview.setLayoutParams(new Gallery.LayoutParams(width, height));
            imageview.setScaleType(ImageView.ScaleType.CENTER);// 设置显示比例类型
            return imageview;
        }

        private class FilterInfo {
            public int filterID;
            public IImageFilter filter;

            public FilterInfo(int filterID, IImageFilter filter) {
                this.filterID = filterID;
                this.filter = filter;
            }
        }
    }
}
