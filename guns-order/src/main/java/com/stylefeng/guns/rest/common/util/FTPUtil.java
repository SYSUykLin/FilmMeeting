package com.stylefeng.guns.rest.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/18 1:12 AM
 */
@Slf4j
public class FTPUtil {
    private String hostName = "47.99.100.174";
    private Integer port = 21;
    private String username = "root";
    private String password = "199821130365Lyk";
    private FTPClient ftpClient = null;

    private void initFTPClient() {
        try {
            ftpClient = new FTPClient();
            ftpClient.setControlEncoding("utf-8");
            ftpClient.connect(hostName, port);
            ftpClient.login(username, password);
        } catch (Exception e) {
            log.error("初始化ftp失败");
        }
    }

    public String getFileStrByAddress(String fileAddress) {
        initFTPClient();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(ftpClient.retrieveFileStream(fileAddress)));
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                String lnStr = bufferedReader.readLine();
                if (lnStr == null) {
                    break;
                }
                stringBuffer.append(lnStr);
            }
            ftpClient.logout();
            return stringBuffer.toString();
        } catch (Exception e) {
            log.error("获取文件选项失败");
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        FTPUtil ftpUtil = new FTPUtil();
        System.out.println(ftpUtil.getFileStrByAddress("seats/cgs.json"));
    }

}
