import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.kaist.MMSClient.MMSClientHandler;
import com.kaist.MMSClient.MMSConfiguration;

public class ServiceProvider {
	public static void main(String args[]) throws Exception{
		String myMRN;
		int port;
		myMRN = "urn:mrn:smart-navi:device:chat-server";
		port = 8902;
		
		//MMSConfiguration.MMSURL="127.0.0.1:8088";
		//MMSConfiguration.CMURL="127.0.0.1";
		
		MMSClientHandler mh = new MMSClientHandler(myMRN);
		mh.setMSP(port);
		mh.setReqCallBack(new MMSClientHandler.reqCallBack() {
			@Override
			public String callbackMethod(String message) {
				try {
					JSONParser Jpar = new JSONParser();
					JSONObject Jobj = (JSONObject) Jpar.parse(message);
					String destMRN = (String) Jobj.get("destMRN");
					mh.sendMSG(destMRN, message);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return "OK";
			}
		});
	}
}
