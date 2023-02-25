import ProgressListener from "./ProgressListener";
import ProgressEvent from './ProgressEvent';
import VideoWriter from "./VideoWriter";

class FileToVideoTransfigurator {

  private file: File;
  private videoWriter?: VideoWriter;
  onprogress?: ProgressListener;

  constructor(file: File) {
    this.file = file;
  }

  async transfigure(): Promise<Blob | undefined> {
    this.videoWriter = new VideoWriter();
    await this.readFile();
    console.log('File.size', this.videoWriter!.dataLength);
    console.log('Number of images', this.videoWriter!.imagesLength);
    return this.videoWriter.toBlob();
  }

  private readFile(): Promise<void> {
    return new Promise(resolve => {
      const reader = new FileReader();
      reader.onload = event => {
        const binaryData = event.target!.result as ArrayBuffer;
        this.handleBinaryData(binaryData);
      };
      reader.onprogress = event => {
        this.fireProgress({ progress: event.loaded, total: event.total });
      };
      reader.onloadend = () => {
        this
        resolve();
      }
      console.log(this.file);
      reader.readAsArrayBuffer(this.file);
    });
  }

  private handleBinaryData(binaryData: ArrayBuffer): void {
    if (this.videoWriter) {
      this.videoWriter.writeArrayBuffer(binaryData);
    }
  }

  private fireProgress(evt: ProgressEvent): void {
    if (this.onprogress) {
      this.onprogress(evt);
    }
  }

}

export default FileToVideoTransfigurator;