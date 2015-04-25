import java.io.*;
// import java.io.File;
// import java.io.FileInputStream;
// import java.io.IOException;


public class SoundEncrypt {

	private byte[] sounds;
	private File wavFile;
	private InputStream wavConvert;
	public SoundEncrypt(String path_name){
		try{
			wavFile = new File(path_name);
			wavConvert = new FileInputStream(wavFile);
			sounds = new byte[ (int)wavFile.length()];
			wavConvert.read(sounds, 0, sounds.length);
			wavConvert.close();
		}
		catch(FileNotFoundException ex){
			System.out.println("error has occured: File not Found");
		}
		catch(IOException ex){

			System.out.println("Error has occured: IO exception has been thrown");
		}
	}






    public static void main(String[] args) {
        System.out.println("Test Project");
    }
}


/*

http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/com/sun/media/sound/WaveFileWriter.java
http://www.mkyong.com/java/how-to-read-file-in-java-fileinputstream/

http://www.codeproject.com/Articles/6960/Steganography-VIII-Hiding-Data-in-Wave-Audio-Files

http://stackoverflow.com/questions/7146382/how-can-i-read-a-wav-file-and-replace-bits-in-it

https://docs.oracle.com/javase/7/docs/api/java/io/RandomAccessFile.html

http://stackoverflow.com/questions/858980/file-to-byte-in-java
*/