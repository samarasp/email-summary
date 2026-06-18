package com.samara.emailsummary.attachment.reader;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

@Component
public class PdfAttachmentReader implements AttachmentReader {

    @Override
    public boolean suporta(String mimeType) {
        return "application/pdf".equalsIgnoreCase(mimeType);
    }

    @Override
    public String extrairTexto(byte[] arquivo) throws Exception {

        try (PDDocument document = Loader.loadPDF(arquivo)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document).trim();
        }
    }
}