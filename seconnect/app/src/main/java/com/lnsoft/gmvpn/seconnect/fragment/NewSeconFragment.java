package com.lnsoft.gmvpn.seconnect.fragment;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.lnsoft.gmvpn.seconnect.R;
import com.lnsoft.gmvpn.seconnect.activity.AuthorizationActivity;
import com.lnsoft.gmvpn.seconnect.activity.MainActivity;
import com.lnsoft.gmvpn.seconnect.adapter.SpinCertItemAdapter;
import com.lnsoft.gmvpn.seconnect.api.SeconStatus;
import com.lnsoft.gmvpn.seconnect.item.CertItem;
import com.lnsoft.gmvpn.seconnect.item.SeconItem;
import com.lnsoft.gmvpn.seconnect.utils.FileUtil;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.com.sansec.key.SWXAPI;
import cn.com.sansec.key.exception.SDKeyException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewSeconFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewSeconFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    //父窗口
    private MainActivity mParentActivity;
    //控件变量
    private TextView mTvTitle;
    private EditText mEdtSeconName;
    private EditText mEdtGateAddr;
    private EditText mEdtGatePort;
    private CheckBox mChkUDP, mChkTCP;
    private Spinner mSpnAuthType, mSpnCertChain, mSpnSignCert, mSpnEncCert;
    private Button mBtnSave;

    //连接相关数据
    private SeconItem mSecon = new SeconItem();
    private String mSeconProto;
    private OnFragmentInteractionListener mListener;


    //三未的TF KEY
    SWXAPI mTfkey;
    List<CertItem> mContaineList = new ArrayList<>();

    private String yjzs;
    private String conList;
    private ArrayList<String> conArrayList;
    private SeconItem si;
    private View inflate;
    private ImageView img_newsecon;
    private Button bt_apps;

    public NewSeconFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewSeconFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewSeconFragment newInstance(String param1, String param2) {
        NewSeconFragment fragment = new NewSeconFragment();
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
        si = new SeconItem();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        if (inflate == null) {
            inflate = inflater.inflate(R.layout.fragment_new_secon, container, false);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) inflate.getParent();
        if (parent != null) {
            parent.removeView(inflate);
        }
        return inflate;

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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化控件
        img_newsecon = inflate.findViewById(R.id.img_newsecon);
        mTvTitle = (TextView) (getActivity().findViewById(R.id.tv_new));
        mEdtSeconName = (EditText) (getActivity().findViewById(R.id.edt_secon_name));
        mEdtSeconName.setSingleLine(true);
        mEdtSeconName.setMaxEms(18);
        mEdtGateAddr = (EditText) (getActivity().findViewById(R.id.edt_secon_addr));
        mEdtGateAddr.setSingleLine(true);
        mEdtGateAddr.setMaxEms(20);
        mEdtGatePort = (EditText) (getActivity().findViewById(R.id.edt_secon_port));
        mEdtGatePort.setSingleLine(true);
        mEdtGatePort.setMaxEms(8);

        mChkUDP = (CheckBox) (getActivity().findViewById(R.id.cb_UDP));
        mChkTCP = (CheckBox) (getActivity().findViewById(R.id.cb_TCP));
        bt_apps = inflate.findViewById(R.id.bt_apps);
        //状态初始化---默认选中UDP
        mChkUDP.setChecked(true);
        mSeconProto = "udp";
        mChkUDP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {//如果选中UDP
                    mChkTCP.setChecked(false);
                    mSeconProto = "udp";
                } else {
                    mChkTCP.setChecked(true);
                    mSeconProto = "tcp";
                }
            }
        });
        mChkTCP.setChecked(false);
        mChkTCP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {//如果选中TCP
                    mChkUDP.setChecked(false);
                    mSeconProto = "tcp";
                } else {
                    mChkUDP.setChecked(true);
                    mSeconProto = "udp";
                }
            }
        });

        img_newsecon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MainFragment()).commit();
                mParentActivity.getRg().setVisibility(View.VISIBLE);
            }
        });
        bt_apps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AuthorizationActivity.class));
            }
        });

        mSpnAuthType = (Spinner) (getActivity().findViewById(R.id.spn_auth_type));

        mSpnCertChain = (Spinner) (getActivity().findViewById(R.id.spn_cert_chain));
        mSpnSignCert = (Spinner) (getActivity().findViewById(R.id.spn_sign_cert));
        mSpnEncCert = (Spinner) (getActivity().findViewById(R.id.spn_enc_cert));


        //初始化----认证方式：数据
        final String[] strAuthTypes = {"文件证书", "硬件证书"};
        ArrayAdapter<String> authTypeAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.my_spinner_item, strAuthTypes);
        mSpnAuthType.setAdapter(authTypeAdapter);

        mSpnAuthType.setSelection(0, true);
        mSpnAuthType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                //判断选中的是哪个证书
                OnChangeAuthType(position);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        //初始化Parent窗口
        mParentActivity = (MainActivity) getActivity();
        //初始化----证书数据
        mParentActivity.handleData();
        //默认是文件证书认证方式
        OnFileCertAuthType();


        if (mParentActivity.getmEdtingSeconIndex() >= 0) {
            //编辑模式，进入控件的初始化
            Toast.makeText(getActivity(), "编辑模式", Toast.LENGTH_SHORT).show();
            editMode();
        }

        //保存配置
        mBtnSave = (Button) (getActivity().findViewById(R.id.btn_save));
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnBtnSave();
            }
        });

    }

    private void editMode() {

        int activeIndex = mParentActivity.getmEdtingSeconIndex();
        si = mParentActivity.getmSeconItemList().get(activeIndex);

        mEdtSeconName.setText(si.getmSeconName());  //设置连接名称
        mEdtGateAddr.setText(si.getmSeconAddr());   //设置服务器地址
        mEdtGatePort.setText(si.getmSeconPort());   //设置端口号


        //设置协议
        if (si.getmSeconProto().equals("udp")) {
            mChkUDP.setChecked(true);
        } else {
            mChkTCP.setChecked(true);
        }

        //设置证书链
        int selCert = mParentActivity.getmCertItemList().indexOf(si.getmCertChain());
        mSpnCertChain.setSelection(selCert, true);
        //设置认证方式
        if (null != si.getmAuthType() && si.getmAuthType().equals("硬件证书")) {

            mSpnAuthType.setSelection(1, true);

        }
        if (null != si.getmAuthType() && si.getmAuthType().equals("文件证书")) {//文件证书设置

            //设置签名证书
            selCert = mParentActivity.getmCertItemList().indexOf(si.getmSignCert());
            mSpnSignCert.setSelection(selCert, true);

            //设置加密证书
            selCert = mParentActivity.getmCertItemList().indexOf(si.getmEncCert());
            mSpnEncCert.setSelection(selCert, true);

        } else {//硬件证书设置

        }
    }

    private boolean varifyHSMPin(String input) {
        //认证口令
        int returnValue = 0;

        //登录varifyPin
        try {
            returnValue = mTfkey.login(SWXAPI.PIN_TYPE_USER, input);
        } catch (SDKeyException e) {
            return false;
        }
        Toast.makeText(getActivity(), "输入口令", Toast.LENGTH_SHORT).show();
        // 获取函数返回值
        if (returnValue == 1) {
            Toast.makeText(getActivity(), "口令验证正确，已成功登录！", Toast.LENGTH_SHORT).show();
            mSecon.setmHSMPin(input);
            //登出
            try {
                mTfkey.logout();
            } catch (SDKeyException e) {
                e.printStackTrace();
            }

            return true;
        } else if (returnValue == 0) {
            Toast.makeText(getActivity(), "用户口令已锁死，请联系管理员解锁！", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            int nRetry = 0 - returnValue;
            Toast.makeText(getActivity(), "用户口令错误，还可以重试" + nRetry + "次！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /*
     *//* 认证方式：硬件证书
     *
     * */
    private void initHSMMode() {
        CertItem ci;
        mContaineList.clear();
        try {
            mTfkey.login(SWXAPI.PIN_TYPE_USER, mSecon.getmHSMPin());
        } catch (SDKeyException e) {
            return;
        }

        try {
            conList = mTfkey.getConList();
            String[] list = conList.split(":");
            if (list == null || list.length <= 0) {
                Toast.makeText(getActivity(), "TFkey中没有发现证书 ！", Toast.LENGTH_SHORT).show();
                return;
            }
            //获取证书
            for (int i = 0; i < list.length; i++) {
                //conArrayList.add(list[i]);
                ci = new CertItem(list[i], "", R.drawable.ic_cert, "");
                mContaineList.add(ci);
            }
        } catch (SDKeyException e) {
            return;
        }

        //设置认证方式
        mSecon.setmAuthType("硬件证书");

        //初始化签名证书、加密证书选择
        SpinCertItemAdapter adapter = new SpinCertItemAdapter(getActivity(), R.layout.cert_list, mContaineList);
        //初始化----签名容器
        mSpnSignCert.setAdapter(adapter);
        mSpnSignCert.setSelection(0, true);
        mSecon.setmHSMSignContainer(mContaineList.get(0).getCertName());
        mSpnSignCert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                OnChangeHSMSignContainer(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        //初始化----加密容器
        mSpnEncCert.setAdapter(adapter);
        mSpnEncCert.setSelection(0, true);
        mSecon.setmHSMEncContainer(mContaineList.get(0).getCertName());
        mSpnEncCert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                OnChangeHSMEncContainer(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        //登出
        try {
            mTfkey.logout();
        } catch (SDKeyException e) {
            e.printStackTrace();
        }

    }


    private void validateHSMPIN() {
        if (mSecon.getmHSMPin() != null) {
            if (varifyHSMPin(mSecon.getmHSMPin()))
                //验证成功则退出
                //开启HSM模式
                initHSMMode();
            return;
        }
        final EditText et = new EditText(getActivity());
        et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        new AlertDialog.Builder(getActivity()).setTitle("请输入TFKEY口令：")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String input = et.getText().toString();
                        if (input.equals("")) {
                            Toast.makeText(getActivity(), "口令不能为空！", Toast.LENGTH_SHORT).show();
                            validateHSMPIN();
                        }
                        //如果密码是正确的
                        if (varifyHSMPin(input)) {
                            initHSMMode();
                            return;
                        } else {//如果密码不正确
                            validateHSMPIN();
                            Toast.makeText(getActivity(), "密码错误", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }


    private void OnChangeHSMSignContainer(int i) {
        //设置签名证书的容器名
        if (mSecon != null) {
            mSecon.setmHSMSignContainer(mContaineList.get(i).getCertName());
        }
        //mSecon.setmHSMSignContainer(mSpnSignCert.getSelectedItem().toString());
    }

    private void OnChangeHSMEncContainer(int i) {
        //Toast.makeText(getActivity(), "--------OnChangeHSMEncContainer", 0).show();
        //设置加密证书的容器名
        if (mSecon != null) {
            mSecon.setmHSMEncContainer(mContaineList.get(i).getCertName());
        }

        // mSecon.setmHSMEncContainer(mSpnEncCert.getSelectedItem().toString());

    }


    public void onHSMAuthType() {

        mTfkey = SWXAPI.getInstance();

        boolean bRet = mTfkey.getOpenResult();
        if (!bRet) {
            Toast.makeText(getActivity(), "打开设备失败，请检查是否已插入设备", Toast.LENGTH_SHORT).show();
            mTfkey = null;
            mSpnAuthType.setSelection(0, true);
        } else {
            Toast.makeText(getActivity(), "设备打开成功", Toast.LENGTH_SHORT).show();
            validateHSMPIN();
        }
    }


    /*
     *  保存配置
     * */
    private void OnBtnSave() {

        //Toast.makeText(getActivity(), "保存配置按钮", 0).show();
        //连接名称
        String strSeconName = mEdtSeconName.getText().toString().trim();
        if (strSeconName.equals("")) {
            Toast.makeText(getActivity(), "请输入创建的连接名称", Toast.LENGTH_SHORT).show();
            mEdtSeconName.setFocusable(true);
            mEdtSeconName.setFocusableInTouchMode(true);
            return;
        }

        String strGateAddr = mEdtGateAddr.getText().toString().trim();
        if (strGateAddr.equals("")) {
            Toast.makeText(getActivity(), "请输入网关地址", Toast.LENGTH_SHORT).show();
            mEdtGateAddr.setFocusable(true);
            mEdtGateAddr.setFocusableInTouchMode(true);
            return;
        }
        String strGatePort = mEdtGatePort.getText().toString().trim();
        if (strGatePort.equals("")) {
            Toast.makeText(getActivity(), "请输入网关端口号", Toast.LENGTH_SHORT).show();
            mEdtGatePort.setFocusable(true);
            mEdtGatePort.setFocusableInTouchMode(true);
            return;
        }

        if (si.getmCertChain() + "" == null || mSecon.getmCertChain() == null) {
            Toast.makeText(getActivity(), "请选择证书链", Toast.LENGTH_SHORT).show();
            return;
        }
        if (si.getmSignCert() + "" == null || mSecon.getmSignCert() == null) {
            Toast.makeText(getActivity(), "请选择签名证书", Toast.LENGTH_SHORT).show();
            return;
        }
        if (si.getmEncCert() + "" == null || mSecon.getmEncCert() == null) {
            Toast.makeText(getActivity(), "请选择加密证书", Toast.LENGTH_SHORT).show();
            return;
        }


        String strAuthType = mSpnAuthType.getSelectedItem().toString();


        //统一设置数据
        mSecon.setmSeconName(strSeconName);//连接名称
        mSecon.setmSeconAddr(strGateAddr);
        mSecon.setmSeconPort(strGatePort);
        mSecon.setmSeconProto(mSeconProto);
        mSecon.setmAuthType(strAuthType);

        //保存连接数据
        String seconFile = FileUtil.GMVPN_CONPATH + strSeconName;
        //设置路径
        mSecon.setmSeconPath(seconFile);

        //设置连接状态
        mSecon.setmStatus(SeconStatus.SECON_DISCONNECT);
        if (SaveConfig(seconFile)) {
            //转到连接列表
            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new MainFragment()).commit();
        } else {
            Toast.makeText(getActivity(), "保存配置失败", Toast.LENGTH_SHORT).show();
        }

    }


    /*
     *  执行写入操作
     * */
    private boolean doWrite(String path) {
        try {
            ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(path));
            oos1.writeObject(mSecon);
            oos1.close();
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    public boolean SaveConfig(String path) {
        if (mParentActivity.getmEdtingSeconIndex() < 0) {//新建模式
            //文件是否存在
            if (FileUtil.fileExists(path)) {
                //文件已存在
                confirmRewriteFile(path);
            } else {//写文件
                doWrite(path);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MainFragment()).commit();
                //显示底部栏
                mParentActivity.getRg().setVisibility(View.VISIBLE);

            }

        } else {//编辑模式
            confirmUpdateFile(path);

        }

        return true;
    }

    /*
     *  更新配置，记住要更新文件名称
     * */
    private void confirmUpdateFile(String path) {
        final String filePath = path;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("是否更新该连接的配置？");

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //什么都不做
                //设置编辑号为-1
                mParentActivity.setmEdtingSeconIndex(-1);
                mParentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MainFragment()).commit();
                //显示底部栏
                mParentActivity.getRg().setVisibility(View.VISIBLE);

            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                /* 更新配置
                 *   第一步：删除原文件；
                 *   第二步：创建新文件
                 *   第三步：重新读取列表；
                 * */
                SeconItem si = mParentActivity.getmSeconItemList().get(mParentActivity.getmEdtingSeconIndex());
                //删除原文件
                FileUtil.deleteFile(si.getmSeconPath());

                //创建新文件
                doWrite(filePath);
                //重新读取列表
                mParentActivity.readSeconsFromFile();

                //设置编辑标志
                mParentActivity.setmEdtingSeconIndex(-1);
                mParentActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MainFragment()).commit();
                //显示底部栏
                mParentActivity.getRg().setVisibility(View.VISIBLE);


            }
        });
        builder.show();
    }


    private void confirmRewriteFile(String path) {
        final String filePath = path;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("已存在同名连接，是否覆盖？");

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //什么都不做

            }
        });
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //写入文件
                doWrite(filePath);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new MainFragment()).commit();

                //显示底部栏
                mParentActivity.getRg().setVisibility(View.VISIBLE);
            }
        });
        builder.show();
    }

    /*
     *  切换认证类型
     * */
    private void OnChangeAuthType(int position) {
        if (position == 1) {
            onHSMAuthType();
        } else {
            OnFileCertAuthType();
        }
    }

    private void OnFileCertAuthType() {

        //Toast.makeText(getActivity(), "调用了OnFileCertAuthType()，重新刷新列表", 0).show();
        //初始化----证书链---适配器
        SpinCertItemAdapter adapter = new SpinCertItemAdapter(getActivity(), R.layout.cert_list, mParentActivity.getmCertItemList());
        //初始化----证书链设置
        mSpnCertChain.setAdapter(adapter);
        mSpnCertChain.setSelection(0, true);
        if (!mParentActivity.getmCertItemList().isEmpty()) {
            mSecon.setmCertChain(mParentActivity.getmCertItemList().get(0));
        }
        mSpnCertChain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                OnChangeCertChain(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        //初始化----签名证书设置
        mSpnSignCert.setAdapter(adapter);
        mSpnSignCert.setSelection(0, true);
        if (!mParentActivity.getmCertItemList().isEmpty()) {
            mSecon.setmSignCert(mParentActivity.getmCertItemList().get(0));

        }
        mSpnSignCert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                OnChangeSignCert(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        //初始化----加密证书设置
        mSpnEncCert.setAdapter(adapter);
        mSpnEncCert.setSelection(0, true);
        if (!mParentActivity.getmCertItemList().isEmpty()) {
            mSecon.setmEncCert(mParentActivity.getmCertItemList().get(0));
        }
        mSpnEncCert.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                OnChangeEncCert(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }


    /*
     *  选择证书链
     * */
    private void OnChangeCertChain(int position) {
        mSecon.setmCertChain(mParentActivity.getmCertItemList().get(position));

    }

    /*
     *  选择签名证书
     * */
    private void OnChangeSignCert(int position) {
        mSecon.setmSignCert(mParentActivity.getmCertItemList().get(position));
    }

    /*
     *  选择加密证书
     * */
    private void OnChangeEncCert(int position) {
        mSecon.setmEncCert(mParentActivity.getmCertItemList().get(position));
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
