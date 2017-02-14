/* -------------------------------------------------------- */
/** 
File name : ServiceProvider.java
	Service Provider of a chatting service.
Author : Jaehee Ha (jaehee.ha@kaist.ac.kr)
Creation Date : 2016-12-03
Version : 0.3.01
Rev. history : 2017-02-01
	Added header field features.
Modifier : Jaehee Ha (jaehee.ha@kaist.ac.kr)
*/
/* -------------------------------------------------------- */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import kr.ac.kaist.mms_client.*;

public class ServiceProvider {
	public static void main(String args[]) throws Exception{
		String myMRN;
		int port;
		myMRN = "urn:mrn:smart-navi:device:chat-server-kaist";
		port = 18902;
		
		MMSConfiguration.MMS_URL="127.0.0.1:8088";
		
		MMSClientHandler ch = new MMSClientHandler(myMRN);
		ch.setMSP(port);
		ch.setCallback(new MMSClientHandler.Callback() {
			
			//ChatSP forwards message to dstMRN written in received message
			@Override
			public String callbackMethod(Map<String,List<String>> headerField, String message) {
				try {
					JSONParser Jpar = new JSONParser();
					JSONObject Jobj = (JSONObject) Jpar.parse(message);
					String dstMRN = (String) Jobj.get("dstMRN");
					String res = ch.sendPostMsg(dstMRN, message);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "OK";
			}
		});
	}
}
