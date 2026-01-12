public class Main {
    public static void main(String[] args) throws Exception {
        JVSImage.Encoder.encode("test-img/Test2.jpg", "test-jvsei/Test2.jvsei");
        JVSImage.Decoder.decode("test-jvsei/Test2.jvsei", "Test3.jpg");
    }
}