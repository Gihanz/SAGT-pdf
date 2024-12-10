88255555
package com.sagt.PDFGenerator;

import java.io.ByteArrayOutputStream;
import java.util.Iterator;
import java.util.Locale;

import javax.security.auth.Subject;

import org.apache.commons.json.JSONArray;
import org.apache.commons.json.JSONException;
import org.apache.commons.json.JSONObject;
import org.apache.log4j.Logger;

import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.PropertyDescriptionList;
import com.filenet.api.constants.Cardinality;
import com.filenet.api.constants.TypeID;
import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.meta.PropertyDescription;
import com.filenet.api.property.Property;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.UserContext;
import com.filenet.apiimpl.core.IndependentObjectImpl;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


public class PDFCreation {

	public static Logger log = Logger.getLogger("com.boc.connector.CMConnector");
	
	public ByteArrayOutputStream sagtCreatePDF(String empNo, String empNoKey){
		
		try{
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			 
			JSONArray vMOVisitData = getallCaseproperties(empNo, "MEDI_VMOVisiting", empNoKey);
			JSONArray probationData = getallCaseproperties(empNo, "MEDI_Probation", empNoKey);
			Document document = new Document();
			document = new Document(PageSize.A4, 36, 36, 80, 36);
			PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
			document.open();
			addVMOVisitDetails(document,writer,vMOVisitData,probationData);
			document.close();
			return byteArrayOutputStream;
		
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}
		
	}

	public void addVMOVisitDetails(Document document, PdfWriter writer,JSONArray jsonarray, JSONArray pbjsonarray) throws DocumentException{		
		try {			 			
			System.out.println("main"+jsonarray);
			
			String EmpNo = jsonarray.getJSONObject(0).get("MEDI_EmployeeID").toString();
			String EmpName = jsonarray.getJSONObject(0).get("MEDI_EmployeeName").toString();
			String NIC = (jsonarray.getJSONObject(0).containsKey("MEDI_NIC"))?jsonarray.getJSONObject(0).get("MEDI_NIC").toString():null;
			String Age = (jsonarray.getJSONObject(0).containsKey("MEDI_Age"))?jsonarray.getJSONObject(0).get("MEDI_Age").toString():null;
			String Designation = (jsonarray.getJSONObject(0).containsKey("MEDI_Designation"))?jsonarray.getJSONObject(0).get("MEDI_Designation").toString():null;
			String Department = (jsonarray.getJSONObject(0).containsKey("MEDI_Department"))?jsonarray.getJSONObject(0).get("MEDI_Department").toString():null;	
			
			String[][] VisitMediData = new String[jsonarray.length()][7];
			for(int d=0;d<jsonarray.length();d++){
				VisitMediData[d][0]=(jsonarray.getJSONObject(d).containsKey("MEDI_ReferenceNumber"))?jsonarray.getJSONObject(d).get("MEDI_ReferenceNumber").toString():null;
				VisitMediData[d][1]=(jsonarray.getJSONObject(d).containsKey("MEDI_ECGChanges"))?jsonarray.getJSONObject(d).get("MEDI_ECGChanges").toString():null;
				VisitMediData[d][2]=(jsonarray.getJSONObject(d).containsKey("MEDI_Vision"))?jsonarray.getJSONObject(d).get("MEDI_Vision").toString():null;
				VisitMediData[d][3]=(jsonarray.getJSONObject(d).containsKey("MEDI_BloodPressure"))?jsonarray.getJSONObject(d).get("MEDI_BloodPressure").toString():null;
				VisitMediData[d][4]=(jsonarray.getJSONObject(d).containsKey("MEDI_HeartMurmer"))?jsonarray.getJSONObject(d).get("MEDI_HeartMurmer").toString():null;
				VisitMediData[d][5]=(jsonarray.getJSONObject(d).containsKey("MEDI_RestingPulse"))?jsonarray.getJSONObject(d).get("MEDI_RestingPulse").toString():null;
				VisitMediData[d][6]=(jsonarray.getJSONObject(d).containsKey("MEDI_BMI"))?jsonarray.getJSONObject(d).get("MEDI_BMI").toString():null;			
			}
			
			String[][] ProbMediData = new String[pbjsonarray.length()][7];
			for(int p=0;p<pbjsonarray.length();p++){
				ProbMediData[p][0]=(pbjsonarray.getJSONObject(p).containsKey("MEDI_ReferenceNumber"))?pbjsonarray.getJSONObject(p).get("MEDI_ReferenceNumber").toString():null;
				ProbMediData[p][1]=(pbjsonarray.getJSONObject(p).containsKey("MEDI_ECGChanges"))?pbjsonarray.getJSONObject(p).get("MEDI_ECGChanges").toString():null;
				ProbMediData[p][2]=(pbjsonarray.getJSONObject(p).containsKey("MEDI_Vision"))?pbjsonarray.getJSONObject(p).get("MEDI_Vision").toString():null;
				ProbMediData[p][3]=(pbjsonarray.getJSONObject(p).containsKey("MEDI_BloodPressure"))?pbjsonarray.getJSONObject(p).get("MEDI_BloodPressure").toString():null;
				ProbMediData[p][4]=(pbjsonarray.getJSONObject(p).containsKey("MEDI_HeartMurmer"))?pbjsonarray.getJSONObject(p).get("MEDI_HeartMurmer").toString():null;
				ProbMediData[p][5]=(pbjsonarray.getJSONObject(p).containsKey("MEDI_RestingPulse"))?pbjsonarray.getJSONObject(p).get("MEDI_RestingPulse").toString():null;
				ProbMediData[p][6]=(pbjsonarray.getJSONObject(p).containsKey("MEDI_BMI"))?pbjsonarray.getJSONObject(p).get("MEDI_BMI").toString():null;			
			}
			
						
			String [] EmpInfo = {"Employee ID","Employee Name","NIC","Age","Designation","Department"};			
			String [] EmpDetails = {EmpNo,EmpName,NIC,Age,Designation,Department};
			
			String [] MediInfo = {"Reference Number","ECG Changes","Vision","Blood Pressure","Heart Murmer","Resting Pulse","BMI"};
			
			PdfPTable header = new PdfPTable(1);

			header.setTotalWidth(527);	
			header.setLockedWidth(true);
			header.getDefaultCell().setFixedHeight(40);
			header.getDefaultCell().setBorder(Rectangle.BOTTOM);
			header.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);
			header.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			header.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
			
			PdfPCell text = new PdfPCell();
			text.setPaddingBottom(10);
			text.setPaddingLeft(215);		
			text.setBorder(Rectangle.BOTTOM);
			text.setBorderColor(BaseColor.LIGHT_GRAY);
			text.addElement(new Phrase("Historical Medical Records",FontFactory.getFont(FontFactory.defaultEncoding,15,Font.BOLDITALIC,BaseColor.WHITE)));
			text.setBackgroundColor(new BaseColor(91, 125, 235));
			text.setHorizontalAlignment(Element.ALIGN_CENTER);
			text.setVerticalAlignment(Element.ALIGN_BOTTOM);
			header.addCell(text);
			
			header.writeSelectedRows(0,-1,36,803,writer.getDirectContent());
			
			// Employee details table ============
			
			int n = EmpInfo.length;
			
			PdfPTable employeeInfotable = new PdfPTable(3);
			
			for(int i=0;i<n;i++) {
				System.out.println(EmpInfo[i]);
				PdfPCell cell1 = new PdfPCell(new Paragraph(EmpInfo[i],FontFactory.getFont(FontFactory.defaultEncoding,12,BaseColor.WHITE)));
				cell1.setBorder(PdfPCell.NO_BORDER);
				cell1.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell1.setBackgroundColor(BaseColor.GRAY);
				cell1.setVerticalAlignment(Element.ALIGN_TOP);
				cell1.setPaddingBottom(10f);
				
				PdfPCell cell2 = new PdfPCell(new Paragraph(":",FontFactory.getFont(FontFactory.defaultEncoding,14,BaseColor.WHITE)));
				cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell2.setBackgroundColor(BaseColor.GRAY);
				cell2.setVerticalAlignment(Element.ALIGN_TOP);
				cell2.setBorder(PdfPCell.NO_BORDER);
				
				PdfPCell cell3 = new PdfPCell(new Paragraph(EmpDetails[i]));
				cell3.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell3.setVerticalAlignment(Element.ALIGN_TOP);
				cell3.setBorder(PdfPCell.NO_BORDER);
				cell3.setBackgroundColor(new BaseColor(247, 247, 255));
				cell3.setPaddingLeft(5f);
				
				employeeInfotable.addCell(cell1);
				employeeInfotable.addCell(cell2);
				employeeInfotable.addCell(cell3);
			}
			
			employeeInfotable.setWidthPercentage(100); //Width 100%
			employeeInfotable.setWidths(new float[]{2f,0.3f,5f});//Column Width
			employeeInfotable.setSpacingBefore(20f);//Space before table		
			employeeInfotable.setSpacingAfter(10f);
			document.add(employeeInfotable);

			int m = MediInfo.length;
			
			// Probation data table ============
			
			PdfPTable table3 = new PdfPTable(m);
						
				for(int j=0;j<m;j++) {
					System.out.println(MediInfo[j]);
					PdfPCell cell7 = new PdfPCell(new Paragraph(MediInfo[j],FontFactory.getFont(FontFactory.defaultEncoding,10,Font.BOLDITALIC,BaseColor.WHITE)));
					cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell7.setBackgroundColor(BaseColor.GRAY);
					cell7.setVerticalAlignment(Element.ALIGN_TOP);
					cell7.setBorderWidth(1f);
					cell7.setPaddingBottom(10f);
					table3.addCell(cell7);
				}
				for(int k=0;k<ProbMediData.length;k++) {
							
					for(int e=0;e<ProbMediData[k].length;e++){
								
						PdfPCell cell8 = new PdfPCell(new Paragraph(ProbMediData[k][e],FontFactory.getFont(FontFactory.defaultEncoding,9)));
						cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
						cell8.setVerticalAlignment(Element.ALIGN_TOP);
						cell8.setBackgroundColor(new BaseColor(247, 247, 255));
						cell8.setPaddingBottom(10f);
						cell8.setBorderWidth(1f);
						table3.addCell(cell8);
					}			
				}
			table3.setWidthPercentage(100); //Width 100%			
			table3.setSpacingBefore(20f);//Space before table		
			table3.setSpacingAfter(10f);
			document.add(table3);
			
			// VMO Visits data table ============
			
			PdfPTable table2 = new PdfPTable(m);
			
			for(int j=0;j<m;j++) {
				System.out.println(MediInfo[j]);
				PdfPCell cell4 = new PdfPCell(new Paragraph(MediInfo[j],FontFactory.getFont(FontFactory.defaultEncoding,10,Font.BOLDITALIC,BaseColor.WHITE)));
				cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell4.setBackgroundColor(BaseColor.GRAY);
				cell4.setVerticalAlignment(Element.ALIGN_TOP);
				cell4.setBorderWidth(1f);
				cell4.setPaddingBottom(10f);
				table2.addCell(cell4);
			}
			for(int k=0;k<VisitMediData.length;k++) {
				
				for(int e=0;e<VisitMediData[k].length;e++){
					
					PdfPCell cell5 = new PdfPCell(new Paragraph(VisitMediData[k][e],FontFactory.getFont(FontFactory.defaultEncoding,9)));
					cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell5.setVerticalAlignment(Element.ALIGN_TOP);
					cell5.setBackgroundColor(new BaseColor(247, 247, 255));
					cell5.setPaddingBottom(10f);
					cell5.setBorderWidth(1f);
					table2.addCell(cell5);
				}
								
			}
			table2.setWidthPercentage(100); //Width 100%			
			table2.setSpacingBefore(20f);//Space before table		
			table2.setSpacingAfter(10f);
			document.add(table2);
			
			document.newPage();			
			
		} 
		catch (Exception e) {			
			e.printStackTrace();
		}			

	}

	public void displayProperty(Folder folder, Property property,JSONObject propertyMap) throws JSONException
	{

		String propertyName = property.getPropertyName();
		ClassDescription classDescription = folder.get_ClassDescription();
		PropertyDescriptionList propDescs = classDescription.get_PropertyDescriptions();
		Iterator propIt = propDescs.iterator();
		PropertyDescription propertyDescription = null;
		while (propIt.hasNext())
		{  	
			propertyDescription = (PropertyDescription)propIt.next();

			String symbolicName = propertyDescription.get_SymbolicName();	    	
			if (symbolicName.equalsIgnoreCase(propertyName))
			{

				switch(propertyDescription.get_DataType().getValue())
				{
				case TypeID.STRING_AS_INT:
					if (propertyDescription.get_Cardinality().getValue() == Cardinality.SINGLE_AS_INT){	
						if(null!=property.getStringValue())
							propertyMap.put(symbolicName,property.getStringValue());
					}
					else{
						if(null!=property.getStringListValue() && property.getStringListValue().size()>0)
							propertyMap.put(symbolicName, property.getStringListValue());
					}
					break;
				case TypeID.DATE_AS_INT:
					if (propertyDescription.get_Cardinality().getValue() == Cardinality.SINGLE_AS_INT)
					{	
						if (property.getDateTimeValue() != null)
							propertyMap.put(symbolicName, property.getDateTimeValue().toString());

					}
					else
						if (property.getDateTimeListValue() != null && property.getDateTimeListValue().size()>0)
						{
							propertyMap.put(symbolicName, property.getDateTimeListValue().toArray());
						}

					break;

				case TypeID.BOOLEAN_AS_INT:
					if (propertyDescription.get_Cardinality().getValue() == Cardinality.SINGLE_AS_INT)
					{
						if (property.getBooleanValue() != null)
							propertyMap.put(symbolicName,property.getBooleanValue());
					}
					else
						if (property.getBooleanListValue() != null && property.getBooleanListValue().size()>0)
						{
							propertyMap.put(symbolicName, property.getBooleanListValue());	
						}

					break;
				case TypeID.DOUBLE_AS_INT:
					if (propertyDescription.get_Cardinality().getValue() == Cardinality.SINGLE_AS_INT)
					{
						if (property.getFloat64Value() != null)				
							propertyMap.put(symbolicName,property.getFloat64Value().toString());
					}
					else
					{
						if(property.getFloat64ListValue()!=null && property.getFloat64ListValue().size()>0)
							propertyMap.put(symbolicName,property.getFloat64ListValue());
					}

					break;
				case TypeID.LONG_AS_INT:
					if (propertyDescription.get_Cardinality().getValue() == Cardinality.SINGLE_AS_INT)
					{ 
						if (property.getInteger32Value() != null)
							propertyMap.put(symbolicName,property.getInteger32Value().toString());
					}
					else
					{
						if (property.getInteger32ListValue()!=null && property.getInteger32ListValue().size()>0)
							propertyMap.put(symbolicName,property.getInteger32ListValue());	
					}

					break;
				case TypeID.GUID_AS_INT:
					if (propertyDescription.get_Cardinality().getValue() == Cardinality.SINGLE_AS_INT)
					{ 
						if (property.getIdValue() != null)
							propertyMap.put(symbolicName,property.getIdValue().toString());
					}			
					break;	
				default:	
					break;					
				}//Switch
			}//if
		}//While

	}

	public JSONArray getallCaseproperties(String empNo,String caseTypeName,String empNoKey) throws Exception{
		Folder folder = null;
		JSONObject propertyMap=null;
		//System.setProperty("java.security.auth.login.config", "/IBM/FileNet/ContentEngine/config/samples/jaas.conf.WSI");
		Connection conn = Factory.Connection.getConnection("http://172.25.100.120:9080/wsi/FNCEWS40MTOM/");		
		Subject sub = UserContext.createSubject(conn, "p8admin","mit@1234","FileNetP8WSI");
		UserContext.get().pushSubject(sub);
		String targetOsname = "CMTOS";

		JSONArray jsonarray = new JSONArray();
		try {
			Domain domain = Factory.Domain.getInstance(conn, null);
			ObjectStore os = Factory.ObjectStore.fetchInstance(domain, targetOsname,null);
			//ObjectStoreReference osRef = new ObjectStoreReference(os);
			String searchString = "SELECT PathName FROM "+caseTypeName+" WHERE "+empNoKey+" = '" + empNo + "'";
			System.out.println("Get Case Properrty:searchString is "+searchString);
			SearchSQL sqlObject = new SearchSQL (searchString);
			SearchScope searchScope = new SearchScope(os);
			IndependentObjectSet ii  = searchScope.fetchObjects(sqlObject, null, null, null);

			Iterator pp =ii.iterator();

			while(pp.hasNext()) {
				if(!ii.isEmpty()) {
					IndependentObjectImpl rowImpl =  (IndependentObjectImpl) pp.next();
					String caseFolder=rowImpl.getProperties().getStringValue("PathName");
					System.out.println("Cases Folder path "+ caseFolder);
					if(caseFolder!=null) {
						folder = Factory.Folder.fetchInstance(os, caseFolder.toString(), null);
						com.filenet.api.property.Properties caseProperties=  folder.getProperties();

						propertyMap= new JSONObject();
						Iterator propertyIter=caseProperties.iterator();

						while(propertyIter.hasNext()) {
							Property  caseProperty = (Property)propertyIter.next();
							displayProperty(folder,caseProperty,propertyMap);
						}

						jsonarray.add(propertyMap);
					}
					else {
						log.error("Invalid Employee Number:"+ empNo);
						propertyMap.put("Error", "Invalid Employee Number");
					}
				}
				else {
					propertyMap.put("Error", "Invalid Case Reference Number");
				}
			}
		}
		catch(Exception e) {
			log.error("Exception occured "+e.fillInStackTrace());
			throw new Exception(e);
		}
		System.out.println("Json "+jsonarray.toString());

		return jsonarray;
	}


}
