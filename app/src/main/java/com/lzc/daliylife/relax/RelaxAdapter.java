package com.lzc.daliylife.relax;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzc.daliylife.R;
import com.lzc.daliylife.adapter.BaseAdapter;
import com.lzc.daliylife.adapter.BaseViewHolder;
import com.lzc.daliylife.adapter.OnRecyclerViewItemClickListener;
import com.lzc.daliylife.entity.yiyuan.BSQJEntity;
import com.lzc.daliylife.normalUtil.DensityUtils;
import com.lzc.daliylife.utils.GlideUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import butterknife.BindView;

import static android.media.MediaMetadataRetriever.OPTION_NEXT_SYNC;

/**
 * Created by lzc on 2017/9/12.
 *
 */

public class RelaxAdapter extends BaseAdapter<BSQJEntity.ShowapiResBodyBean.PagebeanBean.ContentlistBean, RelaxAdapter.BSQJHolder> {

    private int mLastPosition = 2;//从第三个开始
    private Context context;
    private Map<String, RelaxBitmap> bitmapMaps;
    private OnRecyclerViewItemClickListener mOnItemClickListener;
    private float ratio;
    private int WindowWidth;

    RelaxAdapter(Context context) {
        super(context);
        this.context = context;
        bitmapMaps = new HashMap<>();
        WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowWidth = wm.getDefaultDisplay().getWidth();
        int height= DensityUtils.dp2px(context,260);
        ratio=height/(float)WindowWidth;
    }

    void setmOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        addAnimation(holder);
    }

    private void addAnimation(RecyclerView.ViewHolder holder) {
        if (holder.getLayoutPosition() > mLastPosition) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(holder.itemView, "scaleX", 0.9f, 1f);

            scaleX.setDuration(500).start();
            scaleX.setInterpolator(new DecelerateInterpolator());
            if (holder.getLayoutPosition() > 1) {
                mLastPosition = holder.getLayoutPosition();
            }
        }
    }


    @Override
    public BSQJHolder getViewHolder(LayoutInflater mInflater, ViewGroup parent) {
        View view = mInflater.inflate(R.layout.adapter_bsqj, parent, false);
        return new BSQJHolder(view);
    }

    @Override
    public void initNormalHolder(BSQJEntity.ShowapiResBodyBean.PagebeanBean.ContentlistBean value, final BSQJHolder holder) {

        GlideUtils.loadGankRatioImage(context, value.getProfile_image(), holder.imageView);
        holder.name.setText(value.getName());
        holder.time.setText(value.getCreate_time());
        holder.title.setText(value.getText().replace("\n",""));
        if (bitmapMaps.containsKey(value.getVideo_uri())) {
            holder.thumbnails.setImageBitmap(bitmapMaps.get(value.getVideo_uri()).getBitmap());
        } else {
            new ThumbTask(holder.thumbnails).executeOnExecutor(Executors.newCachedThreadPool(), value.getVideo_uri());
        }
        holder.thumbnails_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(holder, holder.getAdapterPosition());
            }
        });
    }

    private class ThumbTask extends AsyncTask<String, Integer, RelaxBitmap> {

        private ImageView imageView;

        private String key;
        ThumbTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            imageView.setImageResource(R.mipmap.icon_gb_dengdai);
        }

        @Override
        protected RelaxBitmap doInBackground(String... params) {
            String url = params[0];
            this.key=url;
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            //获取网络视频
            retriever.setDataSource(url, new HashMap<String, String>());
            String height = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); // 视频高度
            String width = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); // 视频宽度

            Bitmap bitmap = retriever.getFrameAtTime(10000,OPTION_NEXT_SYNC);//截取第秒的图片
            retriever.release();
            Bitmap imageCrop = DensityUtils.ImageCrop(bitmap, ratio,WindowWidth,DensityUtils.dp2px(context,260));
            return new RelaxBitmap(imageCrop,Float.parseFloat(width),Float.parseFloat(height));
        }

        @Override
        protected void onPostExecute(RelaxBitmap bitmap) {
            super.onPostExecute(bitmap);
            bitmapMaps.put(key,bitmap);
            imageView.setImageBitmap(bitmap.getBitmap());
        }
    }

    class BSQJHolder extends BaseViewHolder {

        @BindView(R.id.iv_avator)
        ImageView imageView;
        @BindView(R.id.tv_name)
        TextView name;
        @BindView(R.id.tv_time)
        TextView time;
        @BindView(R.id.tv_title)
        TextView title;
        @BindView(R.id.iv_thumbnails)
        ImageView thumbnails;
        @BindView(R.id.ib_thumbnails_play)
        ImageView thumbnails_play;

        BSQJHolder(View itemView) {
            super(itemView);
        }
    }
}
