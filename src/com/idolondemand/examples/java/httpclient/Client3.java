package com.idolondemand.examples.java.httpclient;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import com.idolondemand.types.IODAction;
import com.idolondemand.types.IODIndex;
import com.idolondemand.types.IODJobStatus;


public class Client3 {

	private String apikey = "your-apikey-here";
	private String url_querytextindex = "https://api.idolondemand.com/1/api/sync/querytextindex/v1";
	private String url_ocrdocument = "https://api.idolondemand.com/1/api/sync/ocrdocument/v1";
	
	private String url_deletetextindex = "https://api.idolondemand.com/1/api/sync/deletetextindex/v1";
	private String url_listindex = "https://api.idolondemand.com/1/api/sync/listindexes/v1";
	private String url_addtotextindex_sync = "https://api.idolondemand.com/1/api/sync/addtotextindex/v1";
	private String url_addtotextindex_async = "https://api.idolondemand.com/1/api/async/addtotextindex/v1";
	
	private String url_jobstatus = "https://api.idolondemand.com/1/job/status";
	
	private String fileSrc = "images/plos_SemanticParticularityMeasure.jpg";
	
	private String plos_apikey = "v8NSJp7XsuHmtD_phVmX";
	private String plos_url_defaultSearch = "http://api.plos.org/search?q=%text% &api_key={your_api_key}";
	
	
	public String addContentToIndex(String myIndex, String urlToIndex){
		
		String reference = null;
		
		HttpClient httpclient = new DefaultHttpClient();
		
			//index=myindex file= or url= or reference=
			String query = URLEncoder.encode(urlToIndex);
			String url = url_addtotextindex_async+"?apikey="+apikey+"&index="+myIndex+"&url="+query;
			System.out.println("GET: "+url);
			
			HttpGet httpget = new HttpGet(url); 
			CloseableHttpResponse response = null;
			 
			 try {
				response = (CloseableHttpResponse) httpclient.execute(httpget);
				StatusLine statusLine = response.getStatusLine();
				System.out.println("StatusLine: "+statusLine.toString());
				System.out.println("StatusLine(Code Phrase): "+statusLine.getStatusCode()+" "+statusLine.getReasonPhrase());
				 
				HttpEntity entity = response.getEntity();
			    if (entity != null) {
			    	
			    	String responseStr = EntityUtils.toString(entity); 
			    	System.out.println(responseStr);
			    	
			    	// see: http://www.json.org/java/
			        JSONObject json = new JSONObject(responseStr); 
			        String jobId = json.getString("jobID");
			        reference = jobId;
			    }
			    
			 }catch(ClientProtocolException cpe){
				 cpe.printStackTrace();
			 }catch(IOException ioe){
				 ioe.printStackTrace();		 
			 } finally {
				    try {
						response.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			 }		
        
        return reference;
		
	}
	
	public void getJobStatus(String reference){
		
		boolean jobDone = false;
		while(!jobDone){
			
			
		HttpClient httpclient = new DefaultHttpClient();
		String url = url_jobstatus + "/"+reference+"?apikey="+apikey;
		System.out.println("GET: "+url);
		 
		HttpGet httpget = new HttpGet(url); 
		CloseableHttpResponse response = null;
		 
		try {
			response = (CloseableHttpResponse) httpclient.execute(httpget);
			StatusLine statusLine = response.getStatusLine();
			System.out.println("StatusLine: "+statusLine.toString());
			//System.out.println("StatusLine(Code Phrase): "+statusLine.getStatusCode()+" "+statusLine.getReasonPhrase());
			 
			HttpEntity entity = response.getEntity();
		    if (entity != null) {
		    	
		    	String responseStr = EntityUtils.toString(entity); 
		    	//System.out.println(responseStr);
		    	
		    	// see: http://www.json.org/java/
		        JSONObject json = new JSONObject(responseStr); 
		        
		        if(json.has("error")){
		        	String error = json.getString("error");
		        	String reason = json.getString("reason");
		        	String detail = json.getString("detail");
		        	
		        	System.out.println("Error: "+error);
		        	System.out.println("Error Reason: "+reason);
		        	System.out.println("Error Detail: "+detail);
		        	break;
		        }
		        IODJobStatus iodJobStatus = new IODJobStatus();
		        
		        ArrayList<IODAction> iodActions = new ArrayList<IODAction>();
		        JSONArray actions = json.getJSONArray("actions");
		        for(int i=0; i<actions.length(); i++){
		        	
		        	JSONObject action = actions.getJSONObject(i);
		        	IODAction iodAction = new IODAction();
		        	iodAction.setStatus(action.getString("status"));
		        	iodAction.setAction(action.getString("action"));
		        	iodAction.setVersion(action.getString("version"));
		        	iodActions.add(iodAction);
		        	
		        }	
		        iodJobStatus.setActions(iodActions);
	        	
		        String jobId = json.getString("jobID");
		        iodJobStatus.setJobID(jobId);
		        
		        String status = json.getString("status");
		        iodJobStatus.setStatus(status);
		        
		        /**
		        JobStatus Values:
		        - 'queued'. The job is in the queue waiting to start.
		        - 'in progress'. The job is being processed by IDOL OnDemand.
		        - 'finished'. The job has been completed.
		        - 'failed'. The job has failed permanently, and you must resend it.
				*/
		        System.out.println("jobStatus: "+iodJobStatus.getStatus());
		        if(iodJobStatus.getStatus().compareTo("finished")==0){
		        	jobDone = true;
		        }
		    }
		    
		 }catch(ClientProtocolException cpe){
			 cpe.printStackTrace();
		 }catch(IOException ioe){
			 ioe.printStackTrace();		 
		 } finally {
			    try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		 }	
		
		
		
		}
	}
	
	
	public void listIndex(){
		
		 HttpClient httpclient = new DefaultHttpClient();
		 url_listindex += "?apikey="+apikey;
		 HttpGet httpget = new HttpGet(url_listindex); 
		 CloseableHttpResponse response = null;
		 
		 try {
			response = (CloseableHttpResponse) httpclient.execute(httpget);
			StatusLine statusLine = response.getStatusLine();
			System.out.println("StatusLine: "+statusLine.toString());
			System.out.println("StatusLine(Code Phrase): "+statusLine.getStatusCode()+" "+statusLine.getReasonPhrase());
			 
			HttpEntity entity = response.getEntity();
		    if (entity != null) {
		    	
		    	String responseStr = EntityUtils.toString(entity); 
		    	System.out.println(responseStr);
		    	
		    	// see: http://www.json.org/java/
		        JSONObject json = new JSONObject(responseStr); 
		        JSONArray indexes = json.getJSONArray("index");
		        
		        for(int i=0; i<indexes.length(); i++){
		        	
		        	JSONObject index = indexes.getJSONObject(i);
		        	
		        	IODIndex iodindex = new IODIndex();
		        	iodindex.setIndex(index.getString("index"));
		        	iodindex.setFlavor(index.getString("flavor"));
		        	iodindex.setType(index.getString("type"));
		        	iodindex.setDate_created(index.getString("date_created"));
		        	iodindex.setNum_components(index.getInt("num_components"));
		        	
		        	//System.out.println(iodindex.toString());
		        }	    	
		    }
		    
		 }catch(ClientProtocolException cpe){
			 cpe.printStackTrace();
		 }catch(IOException ioe){
			 ioe.printStackTrace();		 
		 } finally {
			    try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		 }		 
	}
	
	public void queryTextIndex(String text, String textIndex){
		
		 HttpClient httpclient = new DefaultHttpClient();
		 String textToSearch = URLEncoder.encode(text);
		 url_querytextindex += "?apikey="+apikey+"&text="+textToSearch+"&indexes="+textIndex;
		 HttpGet httpget = new HttpGet(url_querytextindex); 
		 CloseableHttpResponse response = null;
		 
		 try {

			response = (CloseableHttpResponse) httpclient.execute(httpget);
			StatusLine statusLine = response.getStatusLine();
			System.out.println("StatusLine: "+statusLine.toString());
			//System.out.println("StatusLine(Code Phrase): "+statusLine.getStatusCode()+" "+statusLine.getReasonPhrase());
			 
			HttpEntity entity = response.getEntity();
		    if (entity != null) {
		    	
		    	long len = entity.getContentLength();
		        if (len != -1 && len < 2048) {
		        	String body = EntityUtils.toString(entity);
		            System.out.println(body);
		        } else {
		           
		        }
		        //entity.writeTo(System.out);
		    	
		    }
			 
		 }catch(ClientProtocolException cpe){
			 cpe.printStackTrace();
		 }catch(IOException ioe){
			 ioe.printStackTrace();
		 
		 } finally {
			    try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		 }		 
	}
	
	public static void main(String[] args) {
		
		Client3 cl3 = new Client3();
		cl3.listIndex();
		
		String searchQuery = "DNA barcoding";
		String textIndexToSearch = "geneontologyindex";
		cl3.queryTextIndex(searchQuery, textIndexToSearch);
		
		/**
		String myIndex = "geneontologyindex";
		String urlToIndex1 = "http://www.plosone.org/article/info%3Adoi%2F10.1371%2Fjournal.pone.0098603";
		String urlToIndex2 = "http://www.plosone.org/article/info%3Adoi%2F10.1371%2Fjournal.pone.0098614";
		String[] urlsToIndex = {urlToIndex1, urlToIndex2};
		String[] references = new String[urlsToIndex.length];
		int i=0;
		for(String urlToIndex : urlsToIndex){
			String reference = cl3.addContentToIndex(myIndex, urlToIndex);
			references[i]=reference;
			i++;
			//System.out.println("ref: "+reference);
		}
		for(String reference : references){
			cl3.getJobStatus(reference);
		}
		*/
	}

}
