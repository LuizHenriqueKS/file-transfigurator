import VideoConfig from './VideoConfig';

class VideoWriter {

  private _images: string[] = [];
  private lastImage: number[][] = [];
  private row: number = 0;
  private col: number = 0;
  private maxRow: number;
  private maxCol: number;
  private pixelWidth: number;
  private pixelHeight: number;
  private lastImageFilled: boolean = false;
  dataLength: number = 0;

  constructor() {
    this.maxRow = VideoConfig.pixelPerCol;
    this.maxCol = VideoConfig.pixelPerRow;
    this.pixelWidth = Math.floor(VideoConfig.videoWidth / VideoConfig.pixelPerCol);
    this.pixelHeight = Math.floor(VideoConfig.videoHeight / VideoConfig.pixelPerRow);
    this.createNewImage();
    this.start();
  }

  writeByte(byte: number): void {
    this.dataLength++;
    this.lastImage[this.row][this.col] = byte;
    this.lastImageFilled = true;
    this.nextCell();
  }

  writeByteArray(bytes: number[]): void {
    bytes.forEach(byte => this.writeByte(byte));
  }

  writeArrayBuffer(buffer: ArrayBuffer): void {
    const view = new DataView(buffer);
    for (let i = 0; i < buffer.byteLength; i++) {
      const byte = view.getInt8(i);
      this.writeByte(byte);
    }
  }

  toBlob(): Blob {
    const imgs = [];
    for (const img of this.images) {
      for (let i = 0; i < VideoConfig.repeatImages; i++) {
        imgs.push(img);
      }
    }
    console.log('First image', this.images[0]);
    console.log('Second image', this.images[1]);
    //@ts-ignore
    return Whammy.fromImageArray(imgs, VideoConfig.fps) as Blob;
  }

  private nextCell() {
    this.col++;
    if (this.col >= this.maxCol) {
      this.row++;
      this.col = 0;
    }
    if (this.row >= this.maxRow) {
      this.createNewImage();
    }
  }

  private createNewImage(): void {
    if (this.lastImageFilled) {
      const img = this.lastImageToDataURL();
      this._images.push(img);
    }
    this.lastImage = [];
    for (let y = 0; y < this.maxRow; y++) {
      const row = [];
      for (let x = 0; x < this.maxCol; x++) {
        row.push(0);
      }
      this.lastImage.push(row);
    }
    this.row = 0;
    this.col = 0;
  }

  private lastImageToDataURL(): string {
    const canvas = document.createElement('canvas');
    canvas.width = VideoConfig.videoWidth;
    canvas.height = VideoConfig.videoHeight;
    const context = canvas.getContext('2d')!;
    context.fillStyle = 'rgb(0, 0, 0)';
    context.fillRect(0, 0, canvas.width, canvas.height);
    for (let y = 0; y < this.maxRow; y++) {
      for (let x = 0; x < this.maxCol; x++) {
        context.fillStyle = this.getColor(this.lastImage[y][x]);
        context.fillRect(x * this.pixelWidth, y * this.pixelHeight, this.pixelWidth, this.pixelHeight);
      }
    }
    return canvas.toDataURL('image/webp');
  }

  private getColor(byte: number): string {
    const rr = byte * VideoConfig.intervalBetweenColors;
    const r = rr % 256;
    const g = Math.floor(rr / 256) % 256;
    const b = Math.floor((rr / 256) / 256);
    return `rgb(${r}, ${g}, ${b})`
  }

  private start(): void {
    this.writeByteArray(VideoConfig.signature);
  }

  get imagesLength(): number {
    let result = this.images.length;
    if (this.lastImageFilled) {
      result++;
    }
    return result;
  }

  get images(): string[] {
    const result = [...this._images];
    if (this.lastImageFilled) {
      result.push(this.lastImageToDataURL());
    }
    return result;
  }

}

export default VideoWriter;