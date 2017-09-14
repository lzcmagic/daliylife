package com.lzc.daliylife.relax;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lzc.daliylife.R;
import com.lzc.daliylife.adapter.OnRecyclerScrolledListener;
import com.lzc.daliylife.adapter.OnRecyclerViewItemClickListener;
import com.lzc.daliylife.adapter.RecycleViewDivider;
import com.lzc.daliylife.base.BaseFragment;
import com.lzc.daliylife.entity.yiyuan.BSQJEntity;
import com.lzc.daliylife.framework.Constants;
import com.lzc.daliylife.http.HttpMethods;
import com.lzc.daliylife.normalUtil.T;
import com.lzc.daliylife.views.ScrollChildSwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by lzc on 2016/12/5.
 *
 */

public class RelaxFragment extends BaseFragment {
    @BindView(R.id.rv_relax)
    RecyclerView mRecyclerView;
    private boolean IsDataRefresh;
    private int Number;
    private int Page = 1;
    private RelaxAdapter mAdapter;
    @BindView(R.id.spl_relax)
    ScrollChildSwipeRefreshLayout mRefreshLayout;
    List<BSQJEntity.ShowapiResBodyBean.PagebeanBean.ContentlistBean> relaxList;


    @Override
    public int getResId() {
        return R.layout.fragment_relax;
    }

    @Override
    public void initUI() {
        relaxList = new ArrayList<>();
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RelaxAdapter(getContext());
        mAdapter.setLoadMoreRecyclerView(new OnRecyclerScrolledListener() {
            @Override
            public void onLoadMoreRequested() {
                if (IsDataRefresh) {
                    return;
                }
                loadData();
            }
        }, mRecyclerView);
        mAdapter.setmOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, final int position) {
                Intent intent = new Intent(getContext(), RelaxDetailActivity.class);
                intent.putExtra("url", relaxList.get(position).getVideo_uri());
                startActivity(intent);
            }
        });
        mAdapter.setData(relaxList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new RecycleViewDivider(getContext(),
                LinearLayoutManager.VERTICAL,
                R.drawable.divider_mileage));
        mAdapter.notifyDataSetChanged();
        initRefreshLayout();


    }


    /**
     * 初始化下拉刷新控件
     */
    @SuppressWarnings("deprecation")
    private void initRefreshLayout() {
        mRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.darker_gray),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_blue_dark));
        mRefreshLayout.setScrollUpChild(mRecyclerView);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (IsDataRefresh)
                    return;
                Page = 1;
                IsDataRefresh = true;
                //初始化
                relaxList.clear();
                mAdapter.notifyDataSetChanged();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        //加载数据
                        initData();
                    }
                }, 1000);

            }
        });
    }

    /**
     * 加载更多数据
     */
    private void loadData() {
        initData();
    }

    private Disposable disposable;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    /**
     * 初始化数据
     */
    @SuppressLint("SimpleDateFormat")
    private void initData() {
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//        String temp=
//                "page"+String.valueOf(Page)
//                +"showapi_appid"+Constants.YIYUANAPPID
//                +"showapi_res_gzip0"
//                +"showapi_sign_methodmd5"
//                +"showapi_timestamp"+time
//                +"title"
//                +"type41";
//        String sign = SignUtil.YiYuanSign(temp);
        Map<String, String> map = new HashMap<>();
        map.put("showapi_appid", Constants.YIYUANAPPID);
        map.put("showapi_timestamp", time);
        map.put("showapi_sign_method", "md5");
        map.put("showapi_res_gzip", "0");
        map.put("type", "41");
        map.put("title", "");
        map.put("page", String.valueOf(Page));
        map.put("showapi_sign", Constants.YIYUANSECRET);


        HttpMethods.getInstance(Constants.YIYUANADDRESS).getBSQJInfo(new Observer<BSQJEntity>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(BSQJEntity value) {
                if (value != null) {
                    if (0 == value.getShowapi_res_body().getRet_code()) {
                        BSQJEntity.ShowapiResBodyBean.PagebeanBean pagebean = value.getShowapi_res_body().getPagebean();
                        if (pagebean == null) {
                            T.toast("数据加载失败");
                            return;
                        }
                        List<BSQJEntity.ShowapiResBodyBean.PagebeanBean.ContentlistBean> contentlist
                                = pagebean.getContentlist();
                        for (BSQJEntity.ShowapiResBodyBean.PagebeanBean.ContentlistBean bean : contentlist) {
                            relaxList.add(bean);
                        }
                        Number = pagebean.getMaxResult();
                        Page = pagebean.getCurrentPage() + 1;
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                if (null != mRefreshLayout && mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                T.showShort(getString(R.string.load_data_abnormal));
                IsDataRefresh = false;
                mAdapter.stopLoadData();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onComplete() {
                if (null != mRefreshLayout && mRefreshLayout.isRefreshing()) {
                    mRefreshLayout.setRefreshing(false);
                }
                IsDataRefresh = false;
                if (Number == 0 || relaxList.size() % Number > 0) {
                    //加载完毕，(正好加载到第十条)
                    mAdapter.stopLoadData();
                }
                mAdapter.notifyDataSetChanged();

            }
        }, "255-1", map);

    }


}
