public class MediaProcessingSystem {

    // 1. 介面一：可播放能力 (Playable)
    public interface Playable {
        void play();
    }

    // 2. 介面二：可壓縮能力 (Compressible)
    public interface Compressible {
        void compress(int qualityRatio);
    }

    // 3. 抽象父類別：媒體檔案 (MediaFile)
    public static abstract class MediaFile {
        private String fileName;
        private double fileSizeMB;

        public MediaFile(String fileName, double fileSizeMB) {
            this.fileName = (fileName == null || fileName.trim().isEmpty()) ? "Untitled_Media" : fileName.trim();
            this.fileSizeMB = (fileSizeMB < 0) ? 0.0 : fileSizeMB;
        }

        public String getFileName() {
            return fileName;
        }

        public double getFileSizeMB() {
            return fileSizeMB;
        }

        // 抽象方法：獲取媒體類型描述
        public abstract String getMediaType();

        // 所有媒體檔案的通用自我介紹
        public void displayInfo() {
            System.out.println("--------------------------------------------------");
            System.out.println("File Name : " + fileName);
            System.out.println("Media Type: " + getMediaType());
            System.out.println("File Size : " + String.format("%.2f", fileSizeMB) + " MB");
        }
    }

    // 4. 子類別一：圖片檔案 (ImageFile) -> 實作 Compressible
    public static class ImageFile extends MediaFile implements Compressible {
        private String resolution; // 解析度 (例如 "1920x1080")

        public ImageFile(String fileName, double fileSizeMB, String resolution) {
            super(fileName, fileSizeMB);
            this.resolution = (resolution == null || resolution.trim().isEmpty()) ? "Unknown" : resolution.trim();
        }

        @Override
        public String getMediaType() {
            return "Image (Resolution: " + resolution + ")";
        }

        @Override
        public void compress(int qualityRatio) {
            System.out.println("[ACTION - Compress] Image [" + getFileName() + "] compressed to quality: " + qualityRatio + "%");
        }
    }

    // 5. 子類別二：音訊檔案 (AudioFile) -> 實作 Playable
    public static class AudioFile extends MediaFile implements Playable {
        private int durationSeconds; // 時長 (秒)

        public AudioFile(String fileName, double fileSizeMB, int durationSeconds) {
            super(fileName, fileSizeMB);
            this.durationSeconds = (durationSeconds < 0) ? 0 : durationSeconds;
        }

        @Override
        public String getMediaType() {
            return "Audio (Duration: " + durationSeconds + "s)";
        }

        @Override
        public void play() {
            System.out.println("[ACTION - Play] Playing audio track: " + getFileName() + " (Audio Output Active)");
        }
    }

    // 6. 子類別三：影片檔案 (VideoFile) -> 同時實作 Playable 與 Compressible
    public static class VideoFile extends MediaFile implements Playable, Compressible {
        private String codec; // 編碼格式 (例如 "H.264")

        public VideoFile(String fileName, double fileSizeMB, String codec) {
            super(fileName, fileSizeMB);
            this.codec = (codec == null || codec.trim().isEmpty()) ? "Unknown Codec" : codec.trim();
        }

        @Override
        public String getMediaType() {
            return "Video (Codec: " + codec + ")";
        }

        @Override
        public void play() {
            System.out.println("[ACTION - Play] Rendering video frame & audio stream: " + getFileName());
        }

        @Override
        public void compress(int qualityRatio) {
            System.out.println("[ACTION - Compress] Re-encoding video [" + getFileName() + "] with bitrate ratio: " + qualityRatio + "%");
        }
    }

    // 主程式測試區
    public static void main(String[] args) {
        // 使用抽象類別陣列 MediaFile[] 保存各種媒體物件
        MediaFile[] mediaLibrary = new MediaFile[] {
            new ImageFile("Profile_Pic.jpg", 3.5, "3840x2160"),
            new AudioFile("Background_Music.mp3", 8.2, 210),
            new VideoFile("Lecture_Recording.mp4", 450.0, "H.264")
        };

        System.out.println("=== Media Processing System ===");

        for (MediaFile media : mediaLibrary) {
            // 輸出基礎資訊
            media.displayInfo();
            System.out.println("[ Supported Capability Actions ]");

            // 檢查是否支援 Playable (可播放)
            if (media instanceof Playable p) {
                p.play();
            }

            // 檢查是否支援 Compressible (可壓縮)
            if (media instanceof Compressible c) {
                c.compress(75); // 壓縮至 75%
            }
        }
        System.out.println("--------------------------------------------------");
    }
}