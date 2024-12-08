package com.sagt.PDFGenerator;

import java.io.ByteArrayOutputStream;

import org.apache.log4j.Logger;

public class SAGTOperations {
	public static Logger log = Logger.getLogger(SAGTOperations.class);
	
	public static void main(String[] args) {
		System.out.println("main");
		SAGTOperations b = new SAGTOperations();
		b.sagtAttachPDF("3001", "MEDI_EmployeeID", "Historical Medical Records", "MEDI_SAGT_MediDocs", "{B044476C-0000-C11B-8455-2B8FF4A8CC04}");
		System.out.println("done");

	}
	
	public void sagtAttachPDF(String empNo, String empNoKey, String docName, String docClass, String caseFolderId){
		
		log.info(">>>>>> Entering SAGTAttachPDF Method >>>>>>");
		PDFCreation pDFCreation = new PDFCreation();
		ByteArrayOutputStream byArrOutStrm = pDFCreation.sagtCreatePDF(empNo,empNoKey);
		AttachDocument attachDocument= new AttachDocument();
		attachDocument.addDocumentWithStream(byArrOutStrm, docName, docClass, caseFolderId);
		log.info(">>>>>> Finishing SAGTAttachPDF Method >>>>>>");	
	}

}
