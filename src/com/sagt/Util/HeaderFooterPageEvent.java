package com.sagt.Util;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooterPageEvent extends PdfPageEventHelper {

	public void onEndPage(PdfWriter writer, Document document){
		addHeader(writer);
	}

	private void addHeader(PdfWriter writer) {
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
		text.addElement(new Phrase("Pdf Generator",FontFactory.getFont(FontFactory.defaultEncoding,11,Font.BOLDITALIC,BaseColor.WHITE)));
		text.setBackgroundColor(new BaseColor(32,32,32));
		text.setHorizontalAlignment(Element.ALIGN_CENTER);
		text.setVerticalAlignment(Element.ALIGN_BOTTOM);
		header.addCell(text);

		header.writeSelectedRows(0,-1,36,803,writer.getDirectContent());


	}
}
