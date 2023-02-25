package br.zul.filetransfigurator.core;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import br.zul.filetransfigurator.properties.ImageProperties;
import lombok.Cleanup;

public class VideoReader extends InputStream {

    private final ImageProperties imageProperties;
    private final VideoCapture videoCapture;
    private final Java2DFrameConverter java2DFrameConverter = new Java2DFrameConverter();
    private final OpenCVFrameConverter.ToMat converterToMat = new OpenCVFrameConverter.ToMat();
    private final DataInputStream dataInputStream;
    private final ImageReaderFactory imageReaderFactory;
    private ImageReader imageReader;
    private boolean finished;

    public VideoReader(File videoFile, ImageProperties imageProperties, ImageReaderFactory imageReaderFactory) {
        this.imageReaderFactory = imageReaderFactory;
        this.imageProperties = imageProperties;
        this.videoCapture = new VideoCapture(videoFile.getAbsolutePath());
        this.dataInputStream = new DataInputStream(this);
    }

    @Override
    public int read() throws IOException {
        if (finished) {
            return -1;
        } else if (imageReader == null) {
            nextImage();
            return read();
        } else {
            int b = imageReader.read();
            if (b == -1) {
                nextImage();
                return read();
            }
            return b;
        }
    }

    public long readLong() throws IOException {
        return dataInputStream.readLong();
    }

    public int readInt() throws IOException {
        return dataInputStream.readInt();
    }

    public String readString() throws IOException {
        int length = readInt();
        byte[] buffer = new byte[length];
        readNBytes(4);
        read(buffer);
        return new String(buffer, "utf-8");
    }

    @Override
    public void close() throws IOException {
        videoCapture.release();
    }

    private void nextImage() throws IOException {
        @Cleanup
        Mat mat = new Mat();
        if (!videoCapture.isOpened()) {
            throw new IOException("Video is not open");
        }
        if (videoCapture.read(mat)) {
            BufferedImage image = convertToImage(mat);
            imageReader = imageReaderFactory.create(image, imageProperties);
        } else {
            finished = true;
        }
    }

    private BufferedImage convertToImage(Mat mat) {
        Frame frame = converterToMat.convert(mat);
        return java2DFrameConverter.convert(frame);
    }

}
