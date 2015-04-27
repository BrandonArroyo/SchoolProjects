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
		//  This will create the encrypted file 
		try{
		byte dataToWrite[] = sounds;
		FileOutputStream out = new FileOutputStream("This.wav");
		out.write(dataToWrite);
		out.close();
		System.out.println("Completed encryption");
		System.out.println(sounds[50]);
		}
		catch(Exception e){
			System.out.println(e);
		
		}

	}

	public static int language(){
		return 5;
	}
	public static boolean evenOdd(){

		return true; 
	}
	public static void Encrypt(String message){
		// the first 3 lines ainclude data that cant be overwritten
		// this will be 48 bytes
		// for(char x : message.toCharArray() ){
			
		// 	System.out.println();
			

		// }
		int i = 0;
		int j = 48; 
		
		while ( i < message.length() && ((j*8) + 48) < sounds.length ){ // each spot takes 8 bits
			int x = (int)message.charAt(i);
			String s = Integer.toBinaryString(x);
			System.out.println(s);
			System.out.println("HERE:" + s.length());
			for(int k = 0; k < s.length(); ++k){
				if(s.charAt(k) == '0') {
					if(sounds[j] % 2 != 0){// will have check to see if this actually works
						sounds[j] +=1;
						System.out.println("this 1: "+sounds[j]);
					}
					System.out.println("This 2: "+sounds[j]);
				}
				else{
					if(sounds[j] % 2 == 0) {// will have check to see if this actually works
						sounds[j] +=1;
						System.out.println(sounds[j]);
					}
						System.out.println(sounds[j]);

				}
			}
			++i;
			++j;
		}
	}


	public static void Decrypt(){

	}




    public static void main(String[] args) {
    	SoundEncrypt x = new SoundEncrypt("Short.wav");
    	Encrypt("HELLO");
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