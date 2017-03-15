package com.solace.mqtt.domain;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
import com.solace.dto.DomainDetailsDTO;
import com.solace.dto.DomainValueDTO;
import com.solacesystems.jcsmp.JCSMPException;

public class GenericDataManager {

    public static void main(String... args) throws JCSMPException, InterruptedException {
    	loadData("table=product","");
    }
    
    
    @SuppressWarnings("unchecked")
	public static String loadData(String domainName, String whereClause)
    {
    	        
	        try { // JDBC driver name and database URL
	        	StringTokenizer st = new StringTokenizer(domainName, "=;"); 
	        	while(st.hasMoreTokens()) { 
	        		String value = st.nextToken(); 
	        		if(!value.equalsIgnoreCase("table"))
	        		{
	        			domainName = value;
	        		}
	        	}
	        	
	        	final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	        	final String DB_URL = "jdbc:mysql://localhost:3306/solace";

	        	 //  Database credentials
	        	final String USER = "root";
	        	final String PASS = "admin";
	        	Connection  conn =null;
	        	System.out.println("Connecting to database...");
	            conn = (Connection) DriverManager.getConnection(DB_URL,USER,PASS);

	            int domainId = getDomainID(domainName.trim(),conn);
	            List<DomainDetailsDTO> domainDetailsLst  = getDomainDetails(domainId,conn);
	            List<DomainValueDTO> domainDetailsVLst = getDomainValueDetails(domainDetailsLst, conn,domainId,whereClause);
	            int domainIdCount=getCount(conn,domainId);
	            return buildJson(domainDetailsVLst,domainIdCount);
	        	
	        } catch (SQLException ex) {
	            // handle any errors
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
	        }
	        
		return null;
}
    
    public static int getDomainID(String domainName,Connection  conn) throws SQLException
    {
    	Statement  stmt = (Statement) conn.createStatement();
  	    ResultSet rs = stmt.executeQuery("SELECT domin_id FROM domain_master where domain_name='"+domainName+"'");
  	    int domainId=0;
  	    while(rs.next())
  	    {
  	    	domainId=rs.getInt("domin_id");
  	    }
  	    rs.close();
  	    stmt.close();
		return domainId;
    	
    }
    	
    
    public static List<DomainDetailsDTO> getDomainDetails(int domainId,Connection  conn) throws SQLException
    {
    	List<DomainDetailsDTO> domainDetailsLst  = new ArrayList<DomainDetailsDTO>();
    	Statement  stmt = (Statement) conn.createStatement();
  	    ResultSet rs = stmt.executeQuery("SELECT * FROM domain_detail where domin_id="+domainId);
  	   
  	    while(rs.next())
  	    {
  	    	DomainDetailsDTO domainDto = new DomainDetailsDTO(rs.getInt("domin_detail_id"), rs.getInt("domin_id"), rs.getString("domain_detail_name"));
  	    	domainDetailsLst.add(domainDto);
  	    }
  	    rs.close();
  	    stmt.close();
		return domainDetailsLst;
    	
    	
    }
    
    public static List<DomainValueDTO> getDomainValueDetails(List<DomainDetailsDTO> domainDetailsLst,Connection  conn, int domainId,String whereClause) throws SQLException 
    {
    	List<DomainValueDTO> domainDetailsVLst = new ArrayList<DomainValueDTO>();
    	Statement  stmt = (Statement) conn.createStatement();
    	String query = "";
    	if(whereClause.isEmpty())
    		query="SELECT * FROM domain_value_detail where domin_id="+domainId;
    	else
    	{
    		manupulateWhereClause(whereClause);
    		query="SELECT * FROM domain_value_detail where domin_id="+domainId+" and "+manupulateWhereClause(whereClause);
    	}
    		
    	
  	    ResultSet rs = stmt.executeQuery(query);
	  	  while(rs.next())
		    {
	  		  DomainValueDTO domainValueDto = new DomainValueDTO();
	  		  domainValueDto.setDomainDetailsValue(rs.getString("domain_value_detail_name"));
	  		  domainValueDto.setDomainDetails(getDomainValueName(domainDetailsLst, rs.getInt("domin_detail_id")));
	  		  domainDetailsVLst.add(domainValueDto);
	  		  
		    }
		return domainDetailsVLst;
    }
    
    public static String manupulateWhereClause(String whereClause)
    {
    	StringTokenizer st = new StringTokenizer(whereClause, "-"); 
    	String finalClause="";
    	int count=0;
    	while(st.hasMoreTokens()) { 
    		count++;
    		String value = st.nextToken(); 
    		if((count % 2) == 0) 
    			finalClause = finalClause+" domain_value_detail_name in("+value+")";
    		else
    			finalClause = finalClause+" domain_detail_name in ("+value+") and ";
    	
    	}
    	
    
		return finalClause;
    	
    }
    
    public static String getDomainValueName(List<DomainDetailsDTO> domainDetailsLst,Integer domainDetailsID){
    	for (Iterator iterator = domainDetailsLst.iterator(); iterator.hasNext();) {
			DomainDetailsDTO domainDetailsDTO = (DomainDetailsDTO) iterator.next();
			if(domainDetailsDTO.getDomainDetailsId().equals(domainDetailsID))
			{
				return domainDetailsDTO.getDomainName();
			}
			
		}
		return null;
    	
    }
   // var test='{ "records":[ {"trainNo":"12345","trainName":"abc mail","origin":"HWH","destination":"NJP","deperture":"12:10","arrival":"2:20","travelTime":"16","daysOfRun":"2","days":"M"}]}';
    public static String buildJson(List<DomainValueDTO> domainDetailsLst,int domainCount)
    {
    	int count = 0;
    	int mainCount = 0;
    	StringBuilder stb = new StringBuilder();
    	stb.append("{\"records\":[ {");
    	for (Iterator iterator = domainDetailsLst.iterator(); iterator.hasNext();) {
    		count++;
    		mainCount++;
			DomainValueDTO domainValueDTO = (DomainValueDTO) iterator.next();
			stb.append("\"");
			stb.append(domainValueDTO.getDomainDetails());
			stb.append("\":");
			stb.append("\"");
			stb.append(domainValueDTO.getDomainDetailsValue());
			if(count == domainCount)
			{
				if(mainCount!=domainDetailsLst.size())
				{
					stb.append("\"},{");
					count = 0;
				}
				
			}
			else
				stb.append("\",");
		}
//    	String temp = stb.toString();
//    	StringBuilder stbNw = new StringBuilder();
//    	if(temp.charAt(temp.length()-1) == ',')
//    	{
//    		stbNw.append(temp.substring(0, temp.length()-2));
//    	}
//    	
    	stb.append("\"}]}");
    	System.out.println(stb.toString());
    	return stb.toString();
    }
    
    public static int getCount(Connection  conn,int domainId) throws SQLException
    {
    	int domainIdCount=0;
    	Statement  stmt = (Statement) conn.createStatement();
  	    ResultSet rs = stmt.executeQuery("SELECT count(*) FROM domain_detail where domin_id="+domainId);
	  	  while(rs.next())
		    {
	  		  domainIdCount=rs.getInt(1);
		    }
		return domainIdCount;
    	
    }
    
}