
# JVS Image ED

JVS Image ED is a simple tool to obfuscate images so that it is unviewable. It will output a raw binary file which when opened will show garbage text.

The file will need to be put through the decoder to view the image.

You should create a `.jvsei` file only. as the Encoder and Decoder check the file type.
## Usage/Examples
Write this code in any java file and run to encode and decode.

A batch file to run the tool directly via terminal will be provided soon.
```java
JVSImage.Encoder.encode("Test.jpg", "Test.jvsei"); // Encode image
JVSImage.Decoder.decode("Test.jvsei", "Test2.jpg"); // Decode image
JVSImage.Decoder.view("Test.jvsei"); // Decode and view image. Does not create file.
```

