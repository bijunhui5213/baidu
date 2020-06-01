package com.lnsoft.gmvpn.seconnect.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WriteToFileUtils {

    public static void writeFile(String path, String str) {
        if (path == null || path.equals("")) {
            return;
        }
        if (str == null || str.equals("")) {
            return;
        }
        File file = new File(path);
        OutputStream out = null;
        try {
            if (!file.exists()) {
                try {
                    // 如果文件找不到，就new一个
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 定义输出流，写入文件的流
            out = new FileOutputStream(file);
            byte[] bs = str.getBytes();
            try {
                // 写入bs中的数据到file中
                out.write(bs);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null){
                    out.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void writeFileList(String path, ArrayList<String> list) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(path));
            for (String s : list) {
                bw.write(s);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 读取文件
     * @param path
     * @return
     */
    public static List<String> getStr(String path) {
        List<String> list = new ArrayList<>();
        File file = new File(path);
        BufferedReader in =null;
        if (file.exists()){
            try {
                //读取文件(字符流)
                in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                //循环取出数据
                String str = null;
                while ((str = in.readLine()) != null) {
                    list.add(str);
                }
                in.close();
                //写入相应的文件
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    in.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return  list;
    }




}
