package com.lnsoft.gmvpn.seconnect.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SyncStatusObserver;
import android.graphics.Color;
import android.net.SSLSessionCache;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.transition.CircularPropagation;
import android.util.Log;
import android.util.Printer;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lnsoft.gmvpn.sdk.GMVpn;
import com.lnsoft.gmvpn.sdk.GMVpnConfig;
import com.lnsoft.gmvpn.sdk.VpnStatusChangedListener;
import com.lnsoft.gmvpn.seconnect.R;
import com.lnsoft.gmvpn.seconnect.activity.MainActivity;

import com.lnsoft.gmvpn.seconnect.adapter.SeconItemRvAdapter;
import com.lnsoft.gmvpn.seconnect.base.SeconApplicaton;
import com.lnsoft.gmvpn.seconnect.item.SeconItem;
import com.lnsoft.gmvpn.seconnect.utils.FileUtil;
import com.lnsoft.gmvpn.seconnect.utils.WaitDialog;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CertsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CertsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements VpnStatusChangedListener, OnTouchListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView mTextMessage;
    private Button mBtnNew;
    private ListView mListView;
    private MainActivity mParentActivity;

    private OnFragmentInteractionListener mListener;
    private int mStatus;
    private View inflate;
    private ImageView img_about;
    private long position;

    private FloatingActionButton floatingActionButton;
    private SwipeRecyclerView rv;
    private SeconItemRvAdapter rvAdapter;
    private SeconItem si;
    private int mPosition;
    private WaitDialog waitDialog;
    private GMVpn gmVpn;


    public int getmStatus() {
        return mStatus;
    }

    public MainFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflate == null) {
            inflate = inflater.inflate(R.layout.fragment_main, container, false);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) inflate.getParent();
        if (parent != null) {
            parent.removeView(inflate);
        }
        return inflate;

    }


    @SuppressLint("ResourceType")
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        handleData();
    }


    //初始化控件
    private void initView() {
        floatingActionButton = inflate.findViewById(R.id.add_secons);

        //loading
        waitDialog = new WaitDialog(getActivity());


        //初始化Parent窗口
        mParentActivity = (MainActivity) getActivity();
        //初始化列表
        mParentActivity.readSeconsFromFile();


        rv = getView().findViewById(R.id.rv);
        rvAdapter = new SeconItemRvAdapter(getActivity(), mParentActivity.getmSeconItemList());
        rvAdapter.setOnClickListener(new SeconItemRvAdapter.onClickListener() {
            @Override
            public void onClick(int position) {
                mPosition = position;
                mParentActivity.setmActiveSeconIndex(position);
                OnClickSeconItem(position);


            }
        });
        rvAdapter.setOnClickImg(new SeconItemRvAdapter.onClickImg() {
            @Override
            public void onClick(int position) {
                if (mParentActivity.getmActiveSeconIndex() >= 0) {
                    //如果不是未连接状态，要先断开连接
                    confirmDisconnect();

                } else {
                    //Toast.makeText(getActivity(), "编辑操作", Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(), mListView.getAdapter().getItemId(menuInfo.position)+"", 0).show();
                    //设置是哪个Secon需要连接
                    mParentActivity.setmEdtingSeconIndex(position);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new NewSeconFragment()).commit();
                    //隐藏底部栏
                    mParentActivity.getRg().setVisibility(View.GONE);
                    rvAdapter.notifyDataSetChanged();
                }
            }
        });


            // 创建菜单：
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {


                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                deleteItem.setText("删除");
                deleteItem.setTextSize(16);
                deleteItem.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                deleteItem.setWidth(160);
                deleteItem.setBackgroundColor(Color.parseColor("#FF0000"));
                deleteItem.setTextColor(Color.parseColor("#ffffff"));
                // 各种文字和图标属性设置。
                rightMenu.addMenuItem(deleteItem); // 在Item右侧添加一个菜单。

            }
        };


                // 菜单点击监听。
        OnItemMenuClickListener mItemMenuClickListener = new OnItemMenuClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge, final int position) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();

                // 左侧还是右侧菜单：
                int direction = menuBridge.getDirection();
                // 菜单在Item中的Position：
                final int menuPosition = menuBridge.getPosition();
                Builder builder = new Builder(getActivity());
                builder.setTitle("确定要删除该连接？");

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //什么都不做

                    }
                });
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mParentActivity.getmActiveSeconIndex() >= 0) {
                            //如果不是未连接状态，要先断开连接
                            confirmDisconnect();
                        } else {
                            /*删除1条连接分三步：

                             * 第一步：从文件系统删除文件；
                             * 第二步：重新从连接目录中读取连接文件；
                             * 第三步：刷新列表；
                             */
                            FileUtil.deleteFile(mParentActivity.getmSeconItemList().get((int) position).getmSeconPath());
                            mParentActivity.readSeconsFromFile();

                            refreshView();
                        }
                    }

                });
                builder.show();



            }
        };
        rv.setSwipeMenuCreator(mSwipeMenuCreator);
        rv.setOnItemMenuClickListener(mItemMenuClickListener);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        rv.setAutoLoadMore(true);
        rv.setAdapter(rvAdapter);


        //设置编辑的连接：-1
        mParentActivity.setmEdtingSeconIndex(-1);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加连接
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new NewSeconFragment()).commit();
                //隐藏底部栏
                mParentActivity.getRg().setVisibility(View.GONE);

            }
        });

    }

    private void OnClickSeconItem(int i) {
        //创建连接或断开连接
        if (mParentActivity != null) {
            si = mParentActivity.getmSeconItemList().get(i);
            GMVpnConfig.Builder builder = si.buildConfig();
            //获取gmvpn对象
            gmVpn = mParentActivity.getmGMVpn();

            if (gmVpn != null && si != null && builder != null) {
                gmVpn.setStatusChangedListener(this);
                mParentActivity.setmActiveSeconIndex(i);
                gmVpn.setGMVpnConfig(builder.build());

                if (gmVpn.isConnected()) {
                    gmVpn.disconnect();
                    rvAdapter.notifyDataSetChanged();
                } else {
                    waitDialog.show();
                    gmVpn.connect();

                }

            }
        }
    }


    @Override
    public void onStatusChanged(int status) {
        mStatus = status;
        switch (status) {
            case VPN_STATUS_CONNECTING:
                Log.e("SJXC: ", "正在连接服务器.....");
                mParentActivity.setSeconStatus("正在连接服务器.....");
                mParentActivity.setmActiveSeconStatus("正在连接服务器.....");
                mParentActivity.setSeconIcon(R.drawable.secon_iconun);
                refreshView();
                break;
            case VPN_STATUS_CONNECTED:
                Log.e("SJXC: ", "连接成功");
                mParentActivity.setSeconStatus("连接成功");
                mParentActivity.setmActiveSeconStatus("连接成功");
                mParentActivity.setSeconIcon(R.drawable.secon_icon);
                si.setImageID(R.drawable.secon_icon);
                waitDialog.dismiss();
                refreshView();
                break;
            case VPN_STATUS_DISCONNECT:
                Log.e("SJXC: ", "未连接");
                mParentActivity.setSeconStatus("未连接");
                mParentActivity.setmActiveSeconIndex(-1);
                mParentActivity.setSeconIcon(R.drawable.secon_iconun);
                si.setImageID(R.drawable.secon_iconun);
                waitDialog.dismiss();
                refreshView();
                break;
            case VPN_STATUS_RECONNECTING:
                Log.e("SJXC: ", "正在重新连接");
                mParentActivity.setSeconStatus("正在重新连接");
                mParentActivity.setmActiveSeconStatus("正在重新连接");
                refreshView();
                break;
        }
    }


    //初始化数据
    private void handleData() {

    }


    private void confirmDisconnect() {

        Builder builder = new Builder(getActivity());
        builder.setTitle("连接必须是断开状态才可操作，要断开连接吗？");

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //什么都不做
                waitDialog.dismiss();


            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //断开连接
                if (gmVpn != null) {
                    gmVpn.disconnect();
                }
            }
        });
        builder.show();
    }

    private void refreshView() {


        rvAdapter.notifyDataSetChanged();

        if (mStatus == VPN_STATUS_CONNECTED) {
            Toast.makeText(SeconApplicaton.getApplicaton(), "连接成功，点击断开连接", Toast.LENGTH_SHORT).show();
            si.setImageID(R.drawable.secon_icon);
        }



    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return true;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
