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

    private Long maxSize;

    public void transfer() throws IOException {
        byte[] buffer = new byte[bufferSize];
        int len;
        long size = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            size += len;
            if (maxSize != null && size > maxSize) {
                outputStream.write(buffer, 0, len - (int) (size - maxSize));
                break;
            } else {
                outputStream.write(buffer, 0, len);
            }
        }
    }

}