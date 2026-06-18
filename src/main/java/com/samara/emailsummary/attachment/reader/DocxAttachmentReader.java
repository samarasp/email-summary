package com.samara.emailsummary.attachment.reader;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;

@Component
public class DocxAttachmentReader implements AttachmentReader {

    @Override
    public boolean suporta(String mimeType) {
        return "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                .equalsIgnoreCase(mimeType);
    }

    @Override
    public String extrairTexto(byte[] arquivo) throws Exception {

        try (XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(arquivo));
             XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {

            return extractor.getText().trim();
        }
    }
}