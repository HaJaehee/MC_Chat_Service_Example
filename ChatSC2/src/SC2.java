import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.kaist.MMSClient.MMSClientHandler;
import com.kaist.MMSClient.MMSConfiguration;

public class SC2 {
	public static void main(String args[]) throws Exception{
		String myMRN;
		//myMRN = args[0];
		myMRN = "urn:mrn:imo:imo-no:1000007";
		
		//MMSConfiguration.MMSURL="127.0.0.1:8088";
		//MMSConfiguration.CMURL="127.0.0.1";

		//Service Consumer cannot be HTTP server and should poll from MMS. 
		MMSClientHandler ph = new MMSClientHandler(myMRN);
		int pollInterval = 1000;
		ph.setPolling("urn:mrn:smart-navi:device:mms1",pollInterval);
		//Request Callback from the request message
		ph.setReqCallBack(new MMSClientHandler.reqCallBack() {
			@Override
			public String callbackMethod(String message) {
				try {
					JSONParser Jpar = new JSONParser();
					JSONObject Jobj = (JSONObject) Jpar.parse(message);
					String srcMRN = (String) Jobj.get("srcMRN");
					String msg = (String) Jobj.get("msg");
					
					System.out.println("srcMRN: "+srcMRN);
					System.out.println("msg: "+msg);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return "OK";
			}
		});
		
		//Service Consumer which can only send message
		MMSClientHandler mh = new MMSClientHandler(myMRN);
		
		String destMRN = "urn:mrn:imo:imo-no:0100006";
		while (true){
			String msg = new Scanner(System.in).nextLine();
			JSONObject Jobj = new JSONObject();
			Jobj.put("srcMRN", myMRN);
			Jobj.put("destMRN", destMRN);
			Jobj.put("msg", msg);
			String a = mh.sendMSG("urn:mrn:smart-navi:device:chat-server", Jobj.toString());
		}
		
		/*
		("urn:mrn:imo:imo-no:1000007", "127.0.0.1:8901"); // SC
		("urn:mrn:imo:imo-no:0100006", "127.0.0.1:8901"); // SC2
	    ("urn:mrn:smart-navi:device:tm-server", "127.0.0.1:8902"); // SP
	    ("urn:mrn:smart-navi:device:mir1", "127.0.0.1:8903"); // MIR
	    ("urn:mrn:smart-navi:device:msr1", "127.0.0.1:8904"); // MSR
	    ("urn:mrn:smart-navi:device:mms1", "127.0.0.1:8904"); // MMS
	    ("urn:mrn:smart-navi:device:cm1", "127.0.0.1:8904"); // CM
	    */

		//file transferring
		/*
		String response = mh.requestFile("urn:mrn:smart-navi:device:tm-server", "test.xml");
	    System.out.println("response from SC :" + response);
	    response = mh.sendMSG("urn:mrn:smart-navi:device:tm-server", "hello, SC");
		System.out.println("response from MSR :" + response);
		*/
	}
}
