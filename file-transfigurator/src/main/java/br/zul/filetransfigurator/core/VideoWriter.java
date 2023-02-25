package br.zul.filetransfigurator.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import br.zul.filetransfigurator.properties.ImageProperties;
import lombok.Getter;

@Getter
public class VideoWriter extends OutputStream {

    private final File videoFile;
    private final ImageBuilder imageBuilder;
    private final FFmpegFrameRecorder recorder;
    private final Java2DFrameConverter frameConverter = new Java2DFrameConverter();
    private final ImageProperties imageProperties;
    private boolean firstImage = true;

    public VideoWriter(File videoFile, ImageProperties imageProperties) {
        this.imageProperties = imageProperties;
        this.videoFile = videoFile;
        this.imageBuilder = new ImageBuilder(imageProperties);
        this.recorder = new FFmpegFrameRecorder(videoFile, imageProperties.getWidth(), imageProperties.getHeight());
        recorder.setVideoCodec(org.bytedeco.ffmpeg.global.avcodec.AV_CODEC_ID_MPEG4);
        this.recorder.setFrameRate(imageProperties.getFrameRate());
    }

    @Override
    public void write(int b) throws IOException {
        if (this.imageBuilder.isFull()) {
            nextImage();
        }
        this.imageBuilder.write(b);
    }

    @Override
    public void close() throws IOException {
        if (!this.imageBuilder.isEmpty()) {
            nextImage();
        }
        this.recorder.stop();
        super.close();
    }

    private void nextImage() throws IOException {
        try {
            if (!this.imageBuilder.isEmpty()) {
                BufferedImage image = imageBuilder.buildBufferedImage();
                Frame frame = frameConverter.convert(image);
                if (firstImage) {
                    this.recorder.start();
                    firstImage = false;
                }
                for (int i = 0; i < imageProperties.getDurationPerImage(); i++) {
                    this.recorder.record(frame, org.bytedeco.ffmpeg.global.avutil.AV_PIX_FMT_RGB32_1);
                }
            }
            this.imageBuilder.clear();
        } catch (Exception ex) {
            throw new IOException(ex);
        }
    }

    public void writeLong(long l) throws IOException {
        byte[] bytes = ByteBuffer.allocate(Long.BYTES).putLong(l).array();
        write(bytes);
    }

    public void writeInt(int i) throws IOException {
        byte[] bytes = ByteBuffer.allocate(Long.BYTES).putInt(i).array();
        write(bytes);
    }

    public void writeString(String str) throws IOException {
        try {
            writeInt(str.length());
            write(str.getBytes("utf-8"));
        } catch (UnsupportedEncodingException ex) {
            throw new IOException(ex);
        }
    }

}
