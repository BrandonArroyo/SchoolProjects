import java.io.*;
import java.util.*;
// import java.io.File;
// import java.io.FileInputStream;
// import java.io.IOException;


public class SoundEncrypt {

	private static byte[] sounds;
	private static int length; // should never be altered after constructor to check later after altering file
	private File wavFile;
	private	InputStream is;
	public SoundEncrypt(String file_name){
		try{
			wavFile = new File(file_name);
			is = new FileInputStream(wavFile);
			sounds = new byte[(int)wavFile.length()];
			is.read(sounds,0,sounds.length);
			is.close();
			length = (int)wavFile.length();
			
		}
		catch(Exception e){
			System.out.println(e);
		
		}
	}

	public static void print(){
		// This function will then write it back out 
		try{
		byte dataToWrite[] = sounds;
		FileOutputStream out = new FileOutputStream("This");
		out.write(dataToWrite);
		out.close();
		}
		catch(Exception e){
			System.out.println(e);
		
		}

	}


	public static void Encrypt(){

	}


	public static void Decrypt(){

	}




    public static void main(String[] args) {
    	SoundEncrypt x = new SoundEncrypt("Short.wav");
    	print();		
    }
}


/*

http://grepcode.com/file/repository.grepcode.com/java/root/jdk/openjdk/6-b14/com/sun/media/sound/WaveFileWriter.java
http://www.mkyong.com/java/how-to-read-file-in-java-fileinputstream/

http://www.codeproject.com/Articles/6960/Steganography-VIII-Hiding-Data-in-Wave-Audio-Files

http://stackoverflow.com/questions/7146382/how-can-i-read-a-wav-file-and-replace-bits-in-it

https://docs.oracle.com/javase/7/docs/api/java/io/RandomAccessFile.html

http://stackoverflow.com/questions/858980/file-to-byte-in-java


USED FOR SURE:

http://www.codeproject.com/Articles/6960/Steganography-VIII-Hiding-Data-in-Wave-Audio-Files


*/