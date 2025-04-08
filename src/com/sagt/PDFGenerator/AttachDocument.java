package com.sagt.PDFGenerator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.security.auth.Subject;

import org.apache.log4j.Logger;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Connection;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.util.Id;
import com.filenet.api.util.UserContext;

public class AttachDocument {
	
	public static Logger log = Logger.getLogger(AttachDocument.class);
	
	public String  addDocumentWithStream(ByteArrayOutputStream fip,  String docName, String docClass, String caseFolderId) throws EngineRuntimeException{

		//System.setProperty("java.security.auth.login.config", "/IBM/FileNet/ContentEngine/config/samples/jaas.conf.WSI");

		Connection connection = Factory.Connection.getConnection("http://172.25.100.120:9080/wsi/FNCEWS40MTOM/");
		Subject sub = UserContext.createSubject(connection, "p8admin","mit@1234","FileNetP8WSI");
		UserContext.get().pushSubject(sub);
		Domain domain = Factory.Domain.getInstance(connection, null);
		ObjectStore objectStore = Factory.ObjectStore.fetchInstance(domain, "CMTOS", null);
		Folder folder = Factory.Folder.fetchInstance(objectStore,new Id(caseFolderId), null);

		log.info("Folder for saving PDF Folder ID : " + folder.get_Id());

		Document doc = Factory.Document.createInstance(objectStore, docClass);

		ContentElementList contEleList = Factory.ContentElement.createList();
		ContentTransfer ct = Factory.ContentTransfer.createInstance();

		ct.setCaptureSource(new ByteArrayInputStream(fip.toByteArray()));
		ct.set_ContentType("application/pdf");
		ct.set_RetrievalName(docName);
		contEleList.add(ct);

		doc.set_ContentElements(contEleList);
		doc.getProperties().putValue("DocumentTitle", docName);

		doc.set_MimeType("application/pdf");
		doc.checkin(AutoClassify.AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
		doc.save(RefreshMode.REFRESH);

		ReferentialContainmentRelationship rcr = folder.file(doc,AutoUniqueName.AUTO_UNIQUE, docName, DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		rcr.save(RefreshMode.REFRESH);
		log.info("Version Series ID of new document is : "+doc.get_VersionSeries().get_Id());


		return doc.get_VersionSeries().get_Id().toString();

		}
}
