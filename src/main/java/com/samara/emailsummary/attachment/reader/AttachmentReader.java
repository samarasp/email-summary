package com.samara.emailsummary.attachment.reader;

public interface AttachmentReader {

    boolean suporta(String mimeType);

    String extrairTexto(byte[] arquivo) throws Exception;

}