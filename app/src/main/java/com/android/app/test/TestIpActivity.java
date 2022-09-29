package com.android.app.test;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.app.R;
import com.android.app.databinding.ActivityTestIpBinding;
import com.android.helper.base.title.AppBaseBindingTitleActivity;
import com.android.helper.utils.LogUtil;

import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

public class TestIpActivity extends AppBaseBindingTitleActivity<ActivityTestIpBinding> {

    @Override
    protected String setTitleContent() {
        return "测试Ip地址";
    }

    @Override
    public ActivityTestIpBinding getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return ActivityTestIpBinding.inflate(inflater, container, true);
    }

    @Override
    public void initListener() {
        super.initListener();
        setonClickListener(R.id.btn_get_ip, R.id.btn_send_data);
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_get_ip:
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        InetAddress address = null;
                        try {
                            address = InetAddress.getLocalHost();
                            String hostAddress = address.getHostAddress();
                            List<Inet4Address> addressList = getLocalIp4AddressFromNetworkInterface();
                            if (addressList.size() > 0) {
                                for (int i = 0; i < addressList.size(); i++) {
                                    Inet4Address inet4Address = addressList.get(i);
                                    String hostAddress1 = inet4Address.getHostAddress();
                                    LogUtil.e("Address:" + hostAddress);
                                    Message message = mHandler.obtainMessage();
                                    message.obj = hostAddress;
                                    mHandler.sendMessage(message);
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                break;
            case R.id.btn_send_data:

                break;
        }
    }

    public static List<Inet4Address> getLocalIp4AddressFromNetworkInterface() throws SocketException {
        List<Inet4Address> addresses = new ArrayList<>(1);

        // 所有网络接口信息
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        if (networkInterfaces != null) {
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                //滤回环网卡、点对点网卡、非活动网卡、虚拟网卡并要求网卡名字是eth或ens开头
//                if (!isValidInterface(networkInterface)) {
//                    continue;
//                }

                // 所有网络接口的IP地址信息
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // 判断是否是IPv4，并且内网地址并过滤回环地址.
//                    if (isValidAddress(inetAddress)) {
//                        addresses.add((Inet4Address) inetAddress);
//                    }
                    String hostAddress = inetAddress.getHostAddress();
                    String hostName = inetAddress.getHostName();
                    LogUtil.e("hostName:" + hostName + "  address:" + hostAddress);

                    addresses.add((Inet4Address) inetAddress);
                }
            }

        }
        return addresses;
    }

    /**
     * 过滤回环网卡、点对点网卡、非活动网卡、虚拟网卡并要求网卡名字是eth或ens开头
     *
     * @param ni 网卡
     * @return 如果满足要求则true，否则false
     */
    private static boolean isValidInterface(NetworkInterface ni) throws SocketException {
        return !ni.isLoopback() && !ni.isPointToPoint() && ni.isUp() && !ni.isVirtual()
                && (ni.getName().startsWith("eth") || ni.getName().startsWith("ens"));
    }

    /**
     * 判断是否是IPv4，并且内网地址并过滤回环地址.
     */
    private static boolean isValidAddress(InetAddress address) {
        return address instanceof Inet4Address && address.isSiteLocalAddress() && !address.isLoopbackAddress();
    }

    /*
     * 通过Socket 唯一确定一个IP
     * 当有多个网卡的时候，使用这种方式一般都可以得到想要的IP。甚至不要求外网地址8.8.8.8是可连通的
     * @return Inet4Address>
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static Optional<Inet4Address> getIpBySocket() throws SocketException {
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            if (socket.getLocalAddress() instanceof Inet4Address) {
                return Optional.of((Inet4Address) socket.getLocalAddress());
            }
        } catch (UnknownHostException networkInterfaces) {
            throw new RuntimeException(networkInterfaces);
        }
        return Optional.empty();
    }

    /*
     * 获取本地IPv4地址
     * @return Inet4Address>
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Optional<Inet4Address> getLocalIp4Address() throws SocketException {
        final List<Inet4Address> inet4Addresses = getLocalIp4AddressFromNetworkInterface();
        if (inet4Addresses.size() != 1) {
            final Optional<Inet4Address> ipBySocketOpt = getIpBySocket();
            if (ipBySocketOpt.isPresent()) {
                return ipBySocketOpt;
            } else {
                return inet4Addresses.isEmpty() ? Optional.empty() : Optional.of(inet4Addresses.get(0));
            }
        }
        return Optional.of(inet4Addresses.get(0));
    }

    private String value = "";
    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String address = (String) msg.obj;
            value += address + "    ";
            mBinding.tvAddress.setText("Address:" + value);
        }
    };
}