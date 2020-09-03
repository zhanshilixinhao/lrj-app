package com.lrj.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lrj.VO.AppleJsonVo;
import io.jsonwebtoken.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;

import java.security.KeyFactory;
import java.security.PublicKey;

import java.security.spec.RSAPublicKeySpec;

import java.util.Map;

/**
 * @author 战士李昕昊
 */
public class AppleUitl {

    private static final Logger logger = LoggerFactory.getLogger(AppleUitl.class);

    /**
     * 获取苹果的公钥
     * @throws Exception
     */
    private static JSONArray getAuthKeys() throws Exception {
        String url = "https://appleid.apple.com/auth/keys";
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(url,JSONObject.class);
        JSONArray arr = json.getJSONArray("keys");
        System.out.println(arr);
        return arr;
    }

    public static Boolean verify(String jwt) throws  Exception{
        JSONArray arr = getAuthKeys();
        if(arr == null){
            return false;
        }
        String n = arr.getJSONObject(0).getString("n");
        String e = arr.getJSONObject(0).getString("e");
        BigInteger intE = new BigInteger(1, Base64.decodeBase64(e));
        BigInteger intN = new BigInteger(1, Base64.decodeBase64(n));
        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(intN, intE);
        final KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey publicKey = kf.generatePublic(rsaPublicKeySpec);
        if(verifyExc(jwt, publicKey)){
            return true;
        }else{
            //再取第二个key校验
            return verifyExc(jwt, publicKey);
        }

    }

    /**
     * 对前端传来的identityToken进行验证
     * @param jwt 对应前端传来的 identityToken
     */
    public static Boolean verifyExc(String jwt, PublicKey publicKey) throws Exception {
        String aud = "";
        String sub = "";
        if (jwt.split("\\.").length > 1) {
            String claim = new String(Base64.decodeBase64(jwt.split("\\.")[1]));
            System.out.println(claim);
            String s = StringEscapeUtils.unescapeJson(claim);
            Map map = JSON.parseObject(s, Map.class);
            System.out.println( map.get("aud"));
            aud = JSONObject.parseObject(s, AppleJsonVo.class).getAud();
            sub = JSONObject.parseObject(s,AppleJsonVo.class).getSub();
        }
        JwtParser jwtParser = Jwts.parser().setSigningKey(publicKey);
        jwtParser.requireIssuer("https://appleid.apple.com");
        jwtParser.requireAudience(aud);
        jwtParser.requireSubject(sub);

        try {
            Jws<Claims> claim = jwtParser.parseClaimsJws(jwt);
            if (claim != null && claim.getBody().containsKey("auth_time")) {
                System.out.println(claim);
                return true;
            }
            return false;
        } catch (ExpiredJwtException e) {
            logger.error("apple identityToken expired", e);
            return false;
        } catch (Exception e) {
            logger.error("apple identityToken illegal", e);
            return false;
        }
    }



    /**
     * 对前端传来的JWT字符串identityToken的第二部分进行解码
     * 主要获取其中的aud和sub，aud大概对应ios前端的包名，sub大概对应当前用户的授权的openID
     * @param identityToken
     * @return  {"aud":"com.xkj.****","sub":"000***.8da764d3f9e34d2183e8da08a1057***.0***","c_hash":"UsKAuEoI-****","email_verified":"true","auth_time":1574673481,"iss":"https://appleid.apple.com","exp":1574674081,"iat":1574673481,"email":"****@qq.com"}
     */
    public static JSONObject parserIdentityToken(String identityToken){
        String[] arr = identityToken.split("\\.");
        Base64 base64 = new Base64();
        String decode = new String (base64.decodeBase64(arr[1]));
        String substring = decode.substring(0, decode.indexOf("}")+1);
        JSONObject jsonObject = JSON.parseObject(substring);
        return  jsonObject;
    }

    public static void main(String[] args) throws Exception {
        JSONArray authKeys = getAuthKeys();
        System.out.println(authKeys);
    }
}

