public class Main {
    public static void main(String[] args) throws Exception {
        JVSImage.Encoder.encode("Test2.jpg", "Test2.jvsei");
        JVSImage.Decoder.view("Test2.jvsei");
    }
}