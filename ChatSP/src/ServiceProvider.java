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
		
		MMSConfiguration.MMSURL="127.0.0.1:8088";
		MMSConfiguration.CMURL="127.0.0.1";
		
		MMSClientHandler ch = new MMSClientHandler(myMRN);
		ch.setMSP(port);
		ch.setReqCallBack(new MMSClientHandler.ReqCallBack() {
			
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
