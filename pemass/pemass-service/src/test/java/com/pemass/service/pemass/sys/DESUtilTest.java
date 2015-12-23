/**
 * Copyright  2014  Pemass
 * All Right Reserved.
 */

package com.pemass.service.pemass.sys;

import org.testng.annotations.Test;

/**
 * @Description: DESUtilTest
 * @Author: estn.zuo
 * @CreateTime: 2014-11-07 16:10
 */
public class DESUtilTest {

    @Test
    public void test1() {

        //DESUtil.encrypt("123", "pemass");

    }



    public static void main(String[] args) {
        /*try {
            byte[] key = "12345678".getBytes();
            byte[] data = DESUtil.encrypt("adcd".getBytes(), key);
            String dataString = HexUtil.byteToHex(data);
            System.out.println(dataString);
            System.out.println(new String(DESUtil.decrypt(HexUtil.hexToByte(dataString), key)));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
