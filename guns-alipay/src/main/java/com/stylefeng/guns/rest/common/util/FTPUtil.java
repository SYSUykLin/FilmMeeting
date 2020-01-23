package com.stylefeng.guns.rest.common.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.*;

/**
 * @author greenArrow
 * @version 1.0
 * @date 2020/1/18 1:12 AM
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "ftp")
public class FTPUtil {
    private String hostName;
    private Integer port;
    private String userName;
    private String password;
    private FTPClient ftpClient = null;
    private String uploadPath;

    private void initFTPClient() {
        try {
            ftpClient = new FTPClient();
            ftpClient.setControlEncoding("utf-8");
            ftpClient.connect(hostName, port);
            ftpClient.login(userName, password);
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
                log.error("关闭流失败，FTP读取文件失败");
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean uploadFile(String fileName, File file){
        FileInputStream fileInputStream = null;
        try{
            fileInputStream = new FileInputStream(file);
            initFTPClient();
            ftpClient.setControlEncoding("utf-8");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();

            boolean change = ftpClient.changeWorkingDirectory(this.getUploadPath());
            System.out.println(this.getUploadPath());
            System.out.println("切换目录是否成功：" + change);
            ftpClient.storeFile(fileName, fileInputStream);
            return true;
        }catch (Exception e){
            log.error("上传文件失败！", e);
            return false;

        }finally {
            try {
                assert fileInputStream != null;
                fileInputStream.close();
                ftpClient.logout();
            } catch (IOException e) {
                log.error("关闭流异常");
                e.printStackTrace();
            }
        }
    }

}
