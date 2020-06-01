package com.lnsoft.gmvpn.seconnect.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lnsoft.gmvpn.seconnect.FileDialog;
import com.lnsoft.gmvpn.seconnect.R;
import com.lnsoft.gmvpn.seconnect.activity.MainActivity;

import com.lnsoft.gmvpn.seconnect.adapter.CertItemRvAdapter;
import com.lnsoft.gmvpn.seconnect.item.CertItem;
import com.lnsoft.gmvpn.seconnect.utils.CertUtil;
import com.lnsoft.gmvpn.seconnect.utils.FileUtil;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;


import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.Objects;

import javax.net.ssl.SSLContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CertsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CertsFragment extends Fragment {
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
    private OnFragmentInteractionListener mListener;
    private FileDialog fileDialog;
    static private int openfileDialogId = 0;
    private boolean bVerifyOK = false;
    private boolean bRewriteFile = false;
    private String mStrPfxPSW;
    private MainActivity mParentActivity;
    private View inflate;
    //  private ImageView img_addcerts;

    private ArrayList<CertItem> list = new ArrayList<>();
    private SwipeRecyclerView rv;

    private CertItemRvAdapter rvAdapter;

    public CertsFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CertsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CertsFragment newInstance(String param1, String param2) {
        CertsFragment fragment = new CertsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (inflate == null) {
            inflate = inflater.inflate(R.layout.fragment_certs, container, false);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) inflate.getParent();
        if (parent != null) {
            parent.removeView(inflate);
        }
        return inflate;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        //mParentActivity.handleData();
    }

    //初始化控件

    private void initView() {

        //初始化“添加鏈接”按鈕
        FloatingActionButton btnNew = getView().findViewById(R.id.add_new_cert);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //根據添加的連接刷新列表
                OnCreateNewCert();

            }
        });
        //初始化Parent窗口
        mParentActivity = (MainActivity) getActivity();
        //初始化列表
        mParentActivity.handleData();
            PrivateKey privateKey = new PrivateKey() {
                @Override
                public String getAlgorithm() {
                    return null;
                }

                @Override
                public String getFormat() {
                    return null;
                }

                @Override
                public byte[] getEncoded() {
                    return new byte[0];
                }
            };

        rv = (SwipeRecyclerView) getView().findViewById(R.id.rv);
        rvAdapter = new CertItemRvAdapter(getActivity(), R.layout.cert_list, mParentActivity.getmCertItemList());


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
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("确定要删除该证书？");

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //什么都不做

                    }
                });
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (rv != null) {
                            //Toast.makeText(MainActivity.this, "删除操作", Toast.LENGTH_SHORT).show();
                            //獲取位置
                         //   int positions = (int) rv.getAdapter().getItemId(position);

                            boolean bSuccess = FileUtil.deleteFile(mParentActivity.getmCertItemList().get(position).getCertPath());
                            if (!bSuccess) {
                                Toast.makeText(getActivity(), rv.getAdapter().getItemId(position) + "删除失败，文件被占用", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            //从map中删除
                            mParentActivity.getmPfxPswMap().remove(mParentActivity.getmCertItemList().get(position).getCertName());

                            mParentActivity.handleData();
                            rvAdapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), position + "删除成功", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.show();


            }
        };

        rv.setSwipeMenuCreator(mSwipeMenuCreator);
        rv.setOnItemMenuClickListener(mItemMenuClickListener);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.addItemDecoration( new DividerItemDecoration(Objects.requireNonNull(getActivity()),DividerItemDecoration.VERTICAL));
        rv.setAdapter(rvAdapter);




    }

    private void OnCreateNewCert() {
        //从文件系统中遍历添加证书文件
        chooseFile();
    }

//**********************************File Choose Depart****************************************

    private static final String TAG1 = "FileChoose";

    // 调用系统文件管理器
    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*").addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件"), CHOOSE_FILE_CODE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getActivity(), "没有发现文件管理器", Toast.LENGTH_SHORT).show();
        }
    }

    private static final int CHOOSE_FILE_CODE = 0;


    private static byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }

    private void validatePfxPsw(final String srcPath, String destPath) {

        final String fSrcPath = srcPath;
        final String fDestPath = destPath;

        final EditText et = new EditText(getActivity());
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        new AlertDialog.Builder(getActivity()).setTitle("请输入pfx文件口令：")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        Log.e(TAG1, "源文件: " + fSrcPath);
                        Log.e(TAG1, "输入的口令: " + input);
                        if (input.equals("")) {
                            Toast.makeText(getActivity(), "口令不能为空！" + input, Toast.LENGTH_SHORT).show();
                        } else {
                            //保存口令
                            if (CertUtil.checkPfxPsw(fSrcPath, input)) {
                                mStrPfxPSW = input;
                                //口令正确
                                if (FileUtil.fileExists(fDestPath)) {//如果存在该文件
                                    confirmRewriteFile(srcPath, fDestPath);
                                } else {//不存在该文件则直接复制，并刷新列表
                                    addCertItem(fSrcPath, fDestPath);
                                    refreshView();
                                }
                            } else {//口令错误
                                Toast.makeText(getActivity(), "口令错误，请重新输入！" + input, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void confirmRewriteFile(String srcPath, String destPath) {
        final String fSrcPath = srcPath;
        final String fDestPath = destPath;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("证书库中已存在该文件，是否覆盖？");

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //什么都不做

            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //复制文件&刷新视图
                addCertItem(fSrcPath, fDestPath);
                refreshView();
            }
        });
        builder.show();
    }


    @Override
// 文件选择完之后，自动调用此函数
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CHOOSE_FILE_CODE) {

                /*
                 * 准备工作：获取源路径、目的路径
                 * FileUtil.getPath是对路径进行转换，以防止出现路径无法复制的问题；
                 * */
                Uri uri = data.getData();

                String srcPath = FileUtil.getPath(getActivity(), uri);
                if (null != srcPath) {
                    Log.e(TAG1, "文件名称tra: " + srcPath);
                } else {
                    Log.e(TAG1, "文件名称ori: " + uri.getPath());
                    srcPath = uri.getPath();
                }
                //目的路径
                String destPath = FileUtil.GMVPN_CERTPATH + FileUtil.getFileNameFromPath(srcPath);

                final String fsrcPath = srcPath;
                final String fdestPath = destPath;
                /*
                 *  检测源文件类型
                 * */
                if (FileUtil.isPfxFile(srcPath)) {//若为PFX格式证书，测试口令&添加文件&刷新视图
                    validatePfxPsw(srcPath, destPath);
                } else {//若为证书链、根证书等类型证书
                    if (FileUtil.fileExists(destPath)) {//如果存在该文件
                        confirmRewriteFile(srcPath, destPath);
                    } else {
                        //复制文件&刷新视图
                        addCertItem(srcPath, destPath);
                        refreshView();
                    }
                }
            }
        } else {
            Log.e(TAG1, "onActivityResult() error, resultCode: " + resultCode);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addCertItem(String srcPath, String destPath) {

        String filename = FileUtil.getFileNameFromPath(srcPath);
        mParentActivity.getmPfxPswMap().put(filename, mStrPfxPSW);
        //复制文件
        boolean bSuccess = FileUtil.copyFile(srcPath, destPath);
        Log.e(TAG1, "源文件: " + srcPath);
        Log.e(TAG1, "目标文件: " + destPath);
        if (bSuccess) {
            Toast.makeText(getActivity(), "证书添加成功！", Toast.LENGTH_SHORT).show();
            mParentActivity.savePfxPsw();
            mParentActivity.handleData();
        } else {
            Toast.makeText(getActivity(), "证书添加失败！请检查文件类型", Toast.LENGTH_SHORT).show();
        }
    }


    private void refreshView() {
        rvAdapter.notifyDataSetChanged();
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
