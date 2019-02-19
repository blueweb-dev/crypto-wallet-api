package crypto.wallet.common.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;
import org.web3j.utils.Numeric;

import crypto.wallet.common.constant.WalletConst;
import crypto.wallet.constant.EthereumConst;
import lombok.extern.slf4j.Slf4j;


@Slf4j @Component public class WalletUtil implements WalletConst, EthereumConst {

    private static final String TAG   = "[RPC]";
    private static final TimeZone UTC = TimeZone.getTimeZone("UTC");
    private static final SimpleDateFormat isoFormatter = new SimpleDateFormat(ISO_FORMAT);
    static { isoFormatter.setTimeZone(UTC); }


    /**
     * Especially for ADA
     * @param url
     * @return
     * @throws IOException
     */
    public static String sendHttpGet(String url) throws IOException {
      
        CloseableHttpClient client = HttpClients.custom()
            .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        HttpGet req = new HttpGet(url);
        req.setHeader("Content-type", JSON_HEADER);
        CloseableHttpResponse res = client.execute(req);
        HttpEntity entity = res.getEntity();
        if (entity!=null) {
            try {
                int status = res.getStatusLine().getStatusCode();
                String ret = EntityUtils.toString(entity);
                if (status >= 200 && status < 300) {
                    log.debug(TAG + "[200] " + url);
                }
                return ret;
            } finally {
                EntityUtils.consumeQuietly(entity);
                try {
                    if (client != null) {
                        client.close();
                    }
                } catch (IOException e) { }
            }
        } else {
            return null;
        }
    }
    
    /**
     * Especially for ADA
     * @param url
     * @return
     * @throws IOException
     */
    public static String sendHttpsGet(String url) throws IOException {

        CloseableHttpClient client = HttpClients.custom()
              .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        HttpClientContext ctx = HttpClientContext.create();
        HttpGet req = new HttpGet(url);
        req.setHeader("Content-type", JSON_HEADER);
        CloseableHttpResponse res = client.execute(req, ctx);
        HttpEntity entity = res.getEntity();
        
        if (entity!=null) {
            try {
                int status = res.getStatusLine().getStatusCode();
                String ret = EntityUtils.toString(entity);
                EntityUtils.consumeQuietly(entity);
                if (status >= 200 && status < 300) {
                    log.debug(TAG + "[200] " + url);
                }
                return ret;
            } finally {
                EntityUtils.consumeQuietly(entity);
                try {
                    client.close();
                } catch (IOException e) { }
            }
        } else {
            return null;
        }
    }
    
    /**
     * Especially for ADA
     * @param url
     * @param body
     * @return
     * @throws IOException
     */
    public static String sendPost(String url, String body) throws IOException {

        CloseableHttpClient client = HttpClients.custom()
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
        HttpPost req = new HttpPost(url);
        req.setHeader("Content-type", JSON_HEADER);
        req.setHeader("Accept", "application/json");
        req.setEntity(new StringEntity(body, "UTF-8"));
        CloseableHttpResponse res = client.execute(req);
        HttpEntity entity = res.getEntity();
        if (entity!=null) {
            try {
                String ret = EntityUtils.toString(entity);
                int status = res.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    log.debug(TAG + "[200] " + url + " " + body);
                }
                return ret;
            } finally {
                EntityUtils.consumeQuietly(entity);
                try {
                    client.close();
                } catch (IOException e) { }
            }
        } else {
            return null;
        }
    }
    
    /**
     * 
     * @param url
     * @param method
     * @param paramArr
     * @return
     * @throws IOException
     */
    public static String sendJsonRpcJson(String url, String method, JSONArray paramArr) 
              throws IOException {
    
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost req = new HttpPost(url);
        req.setHeader("Content-type", JSON_HEADER);
        JSONObject params = new JSONObject();
        params.put("jsonrpc", "2.0");
        params.put("id",      1);
        params.put("method",  method);
        params.put("params",  paramArr);
        req.setEntity(new StringEntity(params.toString(), "UTF-8"));
        CloseableHttpResponse res = client.execute(req);
        HttpEntity entity = res.getEntity();
        if (entity!=null) {
            try {
                int status = res.getStatusLine().getStatusCode();
                String ret = EntityUtils.toString(entity);
                if (status >= 200 && status < 300) {
                    log.debug(TAG + "[200] " + url + " " + params.toString());
                }
                return ret;
            } finally {
                EntityUtils.consumeQuietly(entity);
                try {
                    client.close();
                } catch (IOException e) { }
            }
        } else {
            return null;
        }
    }

    /**
     * 
     * @param url
     * @param method
     * @param paramArr
     * @return
     * @throws IOException
     */
	public static String sendJsonRpc2(String url, String method, Object[] paramArr) 
	            throws IOException {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost req = new HttpPost(url);
		req.setHeader("Content-type", JSON_HEADER);
		StringBuilder params = new StringBuilder();
		params.append("{\"jsonrpc\":\"2.0\", \"id\":1,\"method\":\"");
		params.append(method + "\", \"params\":[");
		if (paramArr != null) {
			for (int i = 0; i < paramArr.length; i++) {
				params.append(paramArr[i]);
				if (i < paramArr.length - 1) {
					params.append(",");
				}
			}
		}
		params.append("]}");
		req.setEntity(new StringEntity(params.toString(), "UTF-8"));
		CloseableHttpResponse res = client.execute(req);
		HttpEntity entity = res.getEntity();
		if (entity!=null) {
    		try {
    			int status = res.getStatusLine().getStatusCode();
    			String ret = EntityUtils.toString(entity);
    			if (status >= 200 && status < 300) {
    				if (!METHOD_BLOCKBYNUMBER.equals(method)) {
    					log.debug(TAG + "[200] " + url + " " + params.toString());
    				}
                }
                return ret;
            } finally {
                EntityUtils.consumeQuietly(entity);
                try {
                    client.close();
                } catch (IOException e) { }
            }
        } else {
            return null;
        }
	}

	/**
	 * Wallet??JSON-RPC�?보내??공통 ?�틸
	 * @param url
	 */
	public static String sendJsonRpcBasicAuth(String url, String method, Object[] paramArr, 
	        String authId, String authPw) throws IOException {

		CloseableHttpClient client = HttpClients.createDefault();
		HttpClientContext ctx = HttpClientContext.create();
		CredentialsProvider auth = new BasicCredentialsProvider();
		auth.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(authId, authPw));
		ctx.setCredentialsProvider(auth);

		HttpPost req = new HttpPost(url);
		req.setHeader("Content-type", JSON_HEADER);
		StringBuilder params = new StringBuilder();
		params.append("{\"id\":1,\"method\":\"" + method + "\", \"params\":[");
		if (paramArr != null) {
			for (int i = 0; i < paramArr.length; i++) {
				params.append(paramArr[i]);
				if (i < paramArr.length - 1) {
					params.append(",");
				}
			}
		}
		params.append("]}");
		req.setEntity(new StringEntity(params.toString(), "UTF-8"));
		CloseableHttpResponse res = client.execute(req, ctx);
		HttpEntity entity = res.getEntity();
		if (entity!=null) {
    		try {
    		    String ret = EntityUtils.toString(entity);
    			int status = res.getStatusLine().getStatusCode();
    			if (status >= 200 && status < 300) {
    			    log.debug(TAG + "[200] " + url + " " + params.toString());
                } else {
                	log.error(TAG + "[" + status + "] " + url + " " + params.toString() + " " + ret);
                }
                return ret;
            } finally {
                EntityUtils.consumeQuietly(entity);
                try {
                    client.close();
                } catch (IOException e) { }
            }
        } else {
            return null;
        }
	}

    public static String getTestRandomOrderId() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
        Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String token = encoder.encodeToString(bytes);
        return "TEST" + token;
    }
    
    public static String getRandomSystemOrderId() {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);
        Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String token = encoder.encodeToString(bytes);
        return BROKER_ID_SYSTEM + token;
    }
    
    public static String getTestRandomBrokerId() {
        return "TEST";
    }
    
    public static String getTestRandomTxId() {
        return "TEST" + getTestRandomOrderId();
    }
    
    public static long getTestRandomTxTime() {
        return Calendar.getInstance().getTimeInMillis();
    }
    
    public static int getTestRandomInt() {
        Random rand = new SecureRandom();
        return rand.nextInt(999999999);
    }
      
    /**
     * 
     * @param rpcurl
     * @return
     */
    public static String[] parseIpPort(String rpcurl) {
        String[] ret = new String[2];
        int idx = rpcurl.lastIndexOf(":");
        ret[0] = rpcurl.substring(0, idx).replaceFirst("http://", "");
        ret[1] = rpcurl.substring(idx+1).replace("/", "");
        return ret; 
    }
    
    public static long nowToUTCDisplayLong() {
        return Long.parseLong(Instant.now().toString().substring(0,19).replaceAll("[-]", "").replaceAll("[:]","").replaceAll("T", ""));
    }
    
    public static long utcTimestampToEpoch(String timestamp) {
        // String timestamp = "2017-18-08T12:59:30";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-dd-MM HH:mm:ss");
        // parse to LocalDateTime
        LocalDateTime dt = LocalDateTime.parse(timestamp.replace("T", " "), dtf);
        // assume the LocalDateTime is in UTC
        Instant instant = dt.toInstant(ZoneOffset.UTC);
        return instant.getEpochSecond();
    }
    
    public static String toISOFormatTimestamp(long yyyymmddhhmmss) {
        try {
            Date thatday = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH).parse("" + yyyymmddhhmmss);
            return isoFormatter.format(thatday).toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static String longToHex(long longval) {
        return "0x" + Long.toHexString(longval);
    }
    public static Long hexToLong(String hexval) {
        return new BigInteger(hexval.substring(2), 16).longValue();
    }
    
    public static String ethToWeiHex(double ethval) {
        return Numeric.toHexStringWithPrefix(
        		Convert.toWei(BigDecimal.valueOf(ethval), Unit.ETHER)
        			.toBigInteger());
    }
    public static String tokenAmountToHex(double tokenval, int decimals) {
        return Numeric.toHexStringNoPrefix(BigDecimal.valueOf(tokenval)
        		.multiply(BigDecimal.TEN.pow(decimals)).toBigInteger());
    }
    public static double hexToEth(String hexval) {
    	return Convert.fromWei(new BigDecimal(Numeric.toBigInt(hexval))
      		, Unit.ETHER).doubleValue();
	}
    public static Long hexToWei(String hex) {
        return hexToLong(hex);
    }
    public static String weiToHex(Long wei) {
        return "0x" + Long.toHexString(wei);
    }
    public static BigInteger ethToWei(double ethval) {
    	return Convert.toWei(BigDecimal.valueOf(ethval), Unit.ETHER).toBigInteger();
    }
    public static double ethToGWei(double ethval) {
    	return Convert.fromWei(Convert.toWei(BigDecimal.valueOf(ethval), Unit.ETHER)
    			, Unit.GWEI).doubleValue();
    }

    public static double hexTokenAmountToDouble(String hex, int decimals) {
        return new BigDecimal(Numeric.toBigInt(hex)).divide(BigDecimal.TEN.pow(decimals)).doubleValue();
    }
    
    // String to 64 length HexString (equivalent to 32 Hex lenght)
    public static String asciiToHex(String asciiValue) {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }

        return hex.toString() + "".join("", Collections.nCopies(32 - (hex.length()/2), "00"));
    }
    
    public static String hexToASCII(String hexValue) {
        StringBuilder output = new StringBuilder("");
        for (int i = 0; i < hexValue.length(); i += 2)
        {
            String str = hexValue.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }
        return output.toString();
    }
    
    public static int integerDigits(BigDecimal n) {
        return n.signum() == 0 ? 0 : n.precision() - n.scale() -1;
    }
    
    public static BigInteger bigIntegerCeil(BigInteger src) {
    	int digits = WalletUtil.integerDigits(new BigDecimal(src));
    	BigInteger biggestFirst = BigInteger.valueOf(Math.round(Math.pow(10, digits)));
    	return src.subtract(src.mod(biggestFirst)).add(biggestFirst);
    }
    
    public static BigInteger bigIntegerAddFirst(BigInteger src) {
    	int digits = WalletUtil.integerDigits(new BigDecimal(src));
    	BigInteger biggestFirst = BigInteger.valueOf(Math.round(Math.pow(10, digits)));
    	return src.add(biggestFirst);
    }
    
    /**
     * 8 GWEI
     * @param src
     * @return
     */
    public static BigInteger getMinGasPrice(BigInteger src) {
    	if (Convert.fromWei(src.toString(), Convert.Unit.GWEI).doubleValue() < 8) {
    		return new BigInteger("8000000000");
    	}
    	int digits = WalletUtil.integerDigits(new BigDecimal(src));
    	BigInteger biggestFirst = BigInteger.valueOf(Math.round(Math.pow(10, digits)));
    	return src.add(biggestFirst);
    }
    
    public static int getBigintZeroCount(BigInteger i) {
        return ("" + i).length()-1;
    }
    
    public static Integer timestampToEpoch(String timestamp){
        if(timestamp == null) return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"); // .SSSZ
            Date dt = sdf.parse(timestamp);
            long epoch = dt.getTime();
            return (int)(epoch/1000);
        } catch(ParseException e) {
              e.printStackTrace();
             return null;
        }
    }
    
    public static String toCurrencyFormat(Double number) {
        return String.format("%,.4f", number);
    }
    public static String toCurrencyFormat8(Double number) {
    	NumberFormat nf = NumberFormat.getInstance();
        return nf.format(number);
    }
    public static String toCurrencyFormat8Int(Double number) {
    	NumberFormat nf = NumberFormat.getInstance();
        return nf.format(number.intValue());
    }
    
}
