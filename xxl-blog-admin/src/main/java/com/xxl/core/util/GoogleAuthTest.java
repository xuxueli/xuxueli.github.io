//package com.xxl.core.util;
//
//import org.junit.Test;
//
///*
// * Not really a unit test- but it shows usage
// */
//public class GoogleAuthTest {
//
//    @Test
//    public void genSecretTest() {
//        String secret = GoogleAuthenticator.generateSecretKey();
//        String url = GoogleAuthenticator.getQRBarcodeURL("testuser", "testhost", secret);
//        System.out.println("Please register " + url);	// 生成secret二维码
//        System.out.println("Secret key is " + secret);	// 私钥
//    }
//
//    // Change this to the saved secret from the running the above test.
//    static String savedSecret = "SCNASVFNK6EMGXDT";
//
//    @Test
//    public void authTest() {
//        // enter the code shown on device. Edit this and run it fast before the code expires!
//        long code = 333569;
//        long t = System.currentTimeMillis();
//        GoogleAuthenticator ga = new GoogleAuthenticator();
//        ga.setWindowSize(5); //should give 5 * 30 seconds of grace...		// 3秒容纳，怀疑有问题
//        boolean r = ga.check_code(savedSecret, code, t);					// 匹配当前时间生成code和密码器生成的code
//        System.out.println("Check code = " + r);
//    }
//}
//