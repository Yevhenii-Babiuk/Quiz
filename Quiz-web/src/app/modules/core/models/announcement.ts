import {Imaged} from "./imaged";
import {ImageSnippet} from "./imageSnippet";
import {Image} from "./image";

export class Announcement implements Imaged {
  id: number;
  title: string;
  subtitle: string;
  fullText: string;
  authorLogin: string;
  isPublished: boolean;
  createdDate: Date;
  imageId: number;
  selectedFile: ImageSnippet;
  image: Image;
}
