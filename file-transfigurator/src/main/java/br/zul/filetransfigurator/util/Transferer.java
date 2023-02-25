package br.zul.filetransfigurator.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Transferer {

    private final InputStream inputStream;
    private final OutputStream outputStream;

    @Builder.Default
    private int bufferSize = 4096;

    public void transfer() throws IOException {
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
    }

}