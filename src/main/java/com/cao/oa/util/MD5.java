package com.cao.oa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	// ���ȳ�ʼ��һ���ַ����飬�������ÿ��16�����ַ�
    private static final char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
            'e', 'f' };

    /**
     * ���һ���ַ�����MD5ֵ
     * 
     * @param input ������ַ���
     * @return �����ַ�����MD5ֵ
     * 
     */
    public static String md5(String input) {
        if (input == null)
            return null;

        try {
            // �õ�һ��MD5ת�����������ҪSHA1�������ɡ�SHA1����
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // ������ַ���ת�����ֽ�����
            byte[] inputByteArray = input.getBytes("utf-8");
            // inputByteArray�������ַ���ת���õ����ֽ�����
            messageDigest.update(inputByteArray);
            // ת�������ؽ����Ҳ���ֽ����飬����16��Ԫ��
            byte[] resultByteArray = messageDigest.digest();
            // �ַ�����ת�����ַ�������
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
        	return null;
		}
    }

    /**
     * ��ȡ�ļ���MD5ֵ
     * 
     * @param file
     * @return
     */
    public static String md5(File file) {
        try {
            if (!file.isFile()) {
                System.err.println("�ļ�" + file.getAbsolutePath() + "�����ڻ��߲����ļ�");
                return null;
            }

            FileInputStream in = new FileInputStream(file);

            String result = md5(in);

            in.close();

            return result;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String md5(InputStream in) {

        try {
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");

            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer)) != -1) {
                messagedigest.update(buffer, 0, read);
            }

            in.close();

            String result = byteArrayToHex(messagedigest.digest());

            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String byteArrayToHex(byte[] byteArray) {
        // newһ���ַ����飬�������������ɽ���ַ����ģ�����һ�£�һ��byte�ǰ�λ�����ƣ�Ҳ����2λʮ�������ַ���2��8�η�����16��2�η�����
        char[] resultCharArray = new char[byteArray.length * 2];
        // �����ֽ����飬ͨ��λ���㣨λ����Ч�ʸߣ���ת�����ַ��ŵ��ַ�������ȥ
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b & 0xf];
        }

        // �ַ�������ϳ��ַ�������
        return new String(resultCharArray);

    }
}