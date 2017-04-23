/* -------------------------------------------------------- */
/** 
File name : SC1.java
	Service Consumer of a chatting service using HTTPS.
Author : Jaehee Ha (jaehee.ha@kaist.ac.kr)
Creation Date : 2017-03-22
Version : 0.4.0

Version : 0.5.0
Rev. history : 2017-04-20 
Modifier : Jaehee Ha (jaehee.ha@kaist.ac.kr)
*/
/* -------------------------------------------------------- */

import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import kr.ac.kaist.mms_client.*;

public class SC1 {
	public static void main(String args[]) throws Exception{
		String myMRN;
		int port;
		//myMRN = args[0];
		myMRN = "urn:mrn:imo:imo-no:0100001";

		//MMSConfiguration.MMS_URL="127.0.0.1:8088";
		
		//Service Consumer cannot be HTTP server and should poll from MMS. 
		SecureMMSClientHandler sph = new SecureMMSClientHandler(myMRN);

		int pollInterval = 1;
		String dstMRN = "urn:mrn:smart-navi:device:mms1";
		String svcMRN = "urn:mrn:smart-navi:device:secure-chat-server-kaist";
		sph.startPolling(dstMRN, svcMRN, pollInterval);
		
		//Request Callback from the request message
		sph.setPollingResponseCallback(new SecureMMSClientHandler.PollingResponseCallback() {
			
			@Override
			public void callbackMethod(Map<String, List<String>> headerField, String messages) {
				try {
					for (String message : messages.split("\n")){
						JSONParser Jpar = new JSONParser();
						JSONObject Jobj = (JSONObject) Jpar.parse(message);
						String srcMRN = (String) Jobj.get("srcMRN");
						String msg = (String) Jobj.get("msg");
						
						System.out.println("srcMRN: "+srcMRN);
						System.out.println("msg: "+msg);
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		

		//Service Consumer which can only send message
		SecureMMSClientHandler smh = new SecureMMSClientHandler(myMRN);
		
		smh.setResponseCallback(new SecureMMSClientHandler.ResponseCallback (){

			@Override
			public void callbackMethod(Map<String, List<String>> headerField, String message) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		String dstSCMRN = "urn:mrn:imo:imo-no:0100002";
		while (true){
			String msg = new Scanner(System.in).nextLine();
			JSONObject Jobj = new JSONObject();
			Jobj.put("srcMRN", myMRN);
			Jobj.put("dstMRN", dstSCMRN);
			Jobj.put("msg", msg);
			smh.sendPostMsg("urn:mrn:smart-navi:device:secure-chat-server-kaist", Jobj.toString());
		}
	}
}
