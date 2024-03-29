package com.android.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.android.app.R;
import com.android.common.base.recycleview.BaseRecycleViewAdapter;
import com.android.common.base.recycleview.BaseVH;
import com.android.helper.httpclient.CommonApi;
import com.android.http.download.Download;
import com.android.http.download.UploadManagerRetrofit;
import com.android.http.download.UploadProgressListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UploadAdapter extends BaseRecycleViewAdapter<Download, UploadAdapter.UpHV> {

    private final UploadManagerRetrofit manager;
    private final List<MultipartBody.Part> mListPart = new ArrayList<>();

    public UploadAdapter(FragmentActivity activity, List<Download> list) {
        manager = UploadManagerRetrofit.getInstance();
    }

    private void upload(UpHV holder, int position) {
        String tag = String.valueOf(position);
        manager.addParameter("service", "App.Users.UploadFile")
                .addParameter("unid", "o9RWl1JKjbgnEDTAAZjo1-CQFAUo")
                .addParameter("term_suiji", "ncL1")
                .addParameter("cont_suiji", "oL19Dc")
//                .addFileParameter("video", new File("/storage/emulated/0/DCIM/Camera/0c0c1771f326b1171f479d48aa456483.mp4"))
                .addFileParameter("video", new File("/storage/emulated/0/Download/video(1).MP4"));
        UploadProgressListener<String> listener = new UploadProgressListener<String>() {
            @Override
            public void onStart() {
                holder.tv_download.setText("上传中");
            }

            @Override
            public void onProgress(long progress, long contentLength, String percentage) {
                holder.progress.setProgress((int) (Double.parseDouble(percentage)));
                holder.tv_current_progress.setText(percentage + "%");
            }

            @Override
            public void onUploadComplete() {
            }

            @Override
            public void onComplete(Response<String> response, String s) {
                holder.tv_download.setText("上传完成");
            }

            @Override
            public void onError(Throwable throwable) {
                holder.tv_download.setText("上传失败");
            }
        };
        Retrofit retrofit = manager.getRetrofit(listener);
        Call<String> call1 = retrofit.create(CommonApi.class)
                .uploadFile(manager.getParameter());
        manager.uploadFiles(tag, call1, listener);
    }

    @Override
    public void bindHolder(@NonNull UpHV holder, int position) {
        Download bean = mList.get(position);
        holder.tv_download.setText("上传");
        holder.tv_download.setOnClickListener(v -> {
            upload(holder, position);
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(holder.tv_download, position, bean);
            }
        });
        holder.tv_cancel.setOnClickListener(v -> {
            // 取消下载
            manager.cancel(position + "");
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(holder.tv_cancel, position, bean);
            }
        });
    }

    @Override
    public UpHV createVH(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType) {
        return new UpHV(inflater.inflate(R.layout.item_download, parent, false));
    }

    static class UpHV extends BaseVH {
        private ProgressBar progress;
        private TextView tv_download;
        private TextView tv_cancel;
        private TextView tv_current_progress;

        public UpHV(@NonNull View itemView) {
            super(itemView);
            progress = itemView.findViewById(R.id.progress);
            tv_download = itemView.findViewById(R.id.tv_download);
            tv_cancel = itemView.findViewById(R.id.tv_cancel);
            tv_current_progress = itemView.findViewById(R.id.tv_current_progress);
        }
    }
}
