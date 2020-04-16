package com.lrj.util;

import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import java.security.KeyFactory;

import java.security.PrivateKey;

import java.security.spec.PKCS8EncodedKeySpec;


import java.util.Base64;

/**
 * @author : cwj
 * @describe : 极光 一键登录 私钥解密
 * @date : 2020-4-5
 */
public class JiGuangOauthLogin {

    public static String decrypt(String cryptograph, String prikey) throws Exception {

        byte [] keyBytes;
        byte[] bytes = prikey.getBytes();
        keyBytes = Base64Utils.decode(bytes);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes/*Base64.getDecoder().decode(prikey)*/);
        //X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpec);
        Cipher cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte [] b = Base64.getDecoder().decode(cryptograph);
        return new String(cipher.doFinal(b));
    }
   /* public static PrivateKey getPrivateKey(String key) throws Exception {
        byte [] keyBytes;
        byte aByte = Byte.parseByte(key);
        keyBytes = Base64Utils.decode(aByte);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
    }*/

    public static void main(String[] args) throws Exception {
        String cryptograph = "oiE7bL8/CQej3qINe9UMJFw5negCss6cpCM+ZFI+ac8NcdzdmkgAG+BfaKzsXi/B2t5sS9Mw440oqzBZa3y+DhTKtNu8svJpBbe+5r6HCG0fzoD0h1d85kVO3aPWJJaVyRDiiUI4MhVuPAOpA1lXt8VorNpyQkxhoS/cW2SFwXk=";

       //String prikey = "MIICxjBABgkqhkiG9w0BBQ0wMzAbBgkqhkiG9w0BBQwwDgQI77vGEjVLNXICAggAMBQGCCqGSIb3DQMHBAjX5NEzxbrVbASCAoDe2G9wTgQgYxQ5gJZfl4JSeDvfW5QZYke/DsH36oa6AWGdsz7qUBIALXBU7Po6subWVnKtDKhhr9UY1VytXMOoTKvwLVfx2nTono24/SofprknKF3cyMeRV1T/8T1VW76RDyZxV4w6N5ADym2LbjV+rUtLseOVF9/gAJgt0pka3SAB6QsW8ZlUlOEJ3+HpcEOtIz5MJAadnjzCVoesbk40O1vnTaC7ZfCY/lrJNTng8YzKoHAJSxBPx3jLTSMEXOwi2+uD+B+rkftvD9ng/Z276fFY4/RCM+XImH5snRoJmKLFecPBbDPR7XCaGPnc4C0w1SuWi38ZZPRiWAOjRXt8eZ30X5f4cyUqG5uHNPodAGa43s+gWtDOW92Y1o/cq10xe4nkAg9BX5y8TWH++9eP4N6yG3+AKlEk4JDyuRrlvpkub05Ce7VWKipHRgYbhyu88+0vhyb5dQugV75PmYL8sRRHt+aGz16zvA4dp+XRTKd3lfwl2HyJ6/ap8AhiF2bAZZJUV7O+2esGHYPTKy2n0Ri131MZcBohs7FWqVV9tOWyJRDoKW+tIwJIluul7giJ+VXvk6BUZ/CvHfV3SSH3dBZHfY/AHLknaOQ9nmUo11iE85WK9I7lG6h+1C6miwE4gO/ol7UMLkcngLQuec9ygZrhySXm71QGKmgyfxrYJBnoZiiiQZuE6Cv7tsajUf+Dvwzinf1pDId1LxlK3Vf8kNiBHylEz/EuwR5aLH0GzchuzA/7p5AhirLB+5y7fG19c1xgrSbWvJaTPr9SCvNmUp5ZTgV7f9A+b37VA3KnI7ev/jpDDqmLhN8mACCRvqU8QYnnxGmR4wSJf1NkYqRn";
        String prikey = "MIICxjBABgkqhkiG9w0BBQ0wMzAbBgkqhkiG9w0BBQwwDgQIifshjEo6L98CAggAMBQGCCqGSIb3DQMHBAibTQtkQ+EfdgSCAoBONWzB1HCk8uqbCEj2AHgvqKrrQa6dnzwCucJ/wq8HnZ39cUszZ2HsLuarS8/ALlz6TxEeI0EhANj5LhpKA44sbX8hI8XL9wn6Aurzn46XaxqQj7/+8ovyo9diog/fQ0O9bJ6QiqRAtw6xCDlVXrSQirUsjUuWR6mqZfr8+9TDKh56zS/KeYiTJF9n/mv04i7FMRj5tcUafjB+aXis2OOWkSTdKlYTCeY4tt61x9bb4ugQUfYWqHwYkOFuLKo5QyCc6ecLeAAoCJjC5loJJx7COpAms0g5lQEBu1YFqsO+7h+dmJs4KGLXB17/IQQmJ/T16OIjNp/TSZpd/5A6NDLFG3KWJ7T1/Dsm5JhboQ8gJeli5XWOu4LdbzKt2P3D5NFng6iUY2sqW9pcuaaXydXUNrrDcUMlakC8Bil9t1Wl/6s+OxtzcIg3zY7jLgrGaKdgQsRcD7Oo797wFYT9HALto9QZDZo5aHEMW1JeoLdmS0O8WYafbtuj2Oda5u8QKyVJmSspe1vlbQDKA8XKG8R+7sjHue5cLeOqYgzIN8nxnVq0H2cNcl+gEru0CeyHqJ55Tb+CMEudI3Z4fPqBRLF8XytC+k3RmgCWrMvuJMrquCopeVlhxaRzDZcWKia4I16dVLPT+b4bXDCvQcIgNVOJL8HlVqHVMTJbopYQVXpsrP3HrsvPPoqXl29F90T6lR5uNY0gkkstP76vnHO6RSHiva58r80FOLXV0FqvhSdRSzYO5cpwMAsPe1o2Z1Q2nhbxOI3evmUWwalao7ifvHKIdV9ps40JXO6GBl9Ut12JObpfp8cau+dGDp6oSuffdZkcSVeaFrScMEHKdQxwOGsx";

        String decrypt = decrypt(cryptograph, prikey);
        System.out.println(decrypt);
    }
}
