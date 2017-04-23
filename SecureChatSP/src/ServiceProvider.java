/* -------------------------------------------------------- */
/** 
File name : ServiceProvider.java
	Service Provider of a chatting service using HTTPS.
Author : Jaehee Ha (jaehee.ha@kaist.ac.kr)
Creation Date : 2017-03-22
Version : 0.4.0

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
		myMRN = "urn:mrn:smart-navi:device:secure-chat-server-kaist";
		port = 18902;
		String jksDirectory = System.getProperty("user.dir")+"/testkey.jks";
		String jksPassword = "lovesm13";
		
		//MMSConfiguration.MMS_URL="127.0.0.1:8088";
		
		SecureMMSClientHandler sch = new SecureMMSClientHandler(myMRN);
		sch.setPort(port,jksDirectory,jksPassword);
		sch.setRequestCallback(new SecureMMSClientHandler.RequestCallback() {
			
			@Override
			public int setResponseCode() {
				// TODO Auto-generated method stub
				return 200;
			}
			
			//ChatSP forwards message to dstMRN written in received message
			@Override
			public String respondToClient(Map<String,List<String>> headerField, String message) {
				try {
					JSONParser Jpar = new JSONParser();
					JSONObject Jobj = (JSONObject)Jpar.parse(message);
					String dstMRN = (String) Jobj.get("dstMRN");
					sch.setResponseCallback(new SecureMMSClientHandler.ResponseCallback() {
						
						@Override
						public void callbackMethod(Map<String, List<String>> headerField, String message) {
							// TODO Auto-generated method stub
							System.out.println(message);
						}
					});
					sch.sendPostMsg(dstMRN, Jobj.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "OK";
			}
		});
	}
}
