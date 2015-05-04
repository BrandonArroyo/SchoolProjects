//package _Sound;

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
			
			for(int k = 0; k < s.length(); ++k){
				
				if(s.charAt(k) == '0') {
					if(sounds[j] % 2 != 0){// will have check to see if this actually works
						sounds[j] +=1;
						System.out.println(sounds[j]);
					}
					else System.out.println(sounds[j]);
				}
				else{
					if(sounds[j] % 2 == 0) {// will have check to see if this actually works
						sounds[j] +=1;
						System.out.println(sounds[j]);
					}
					else
						System.out.println(sounds[j]);

				}
				++j;
			}
			++i;
			
		}
	}


	public static String Decrypt(String file_name, int message ){
		//http://stackoverflow.com/questions/5453017/convert-binary-string-to-ascii-text
		byte[] test ;
		File wavFile = new File(file_name);
		InputStream is = new FileInputStream(wavFile);
		test = new byte[(int)wavFile.length()];
		is.read(test,0,test.length);
		is.close();
		length = (int)wavFile.length();
		String Hidden;
		for(int i = 48 ; i < (message*8); ++i){

			if(test[i] % 2 = 0 ){
				hidden += "0"
			}
			else{
				hidden += "1";
			}

		}
		String s2 = "";
		char nextChar;

		for(int i = 0; i <= s.length()-8; i += 8) //this is a little tricky.  we want [0, 7], [9, 16], etc
		{
			nextChar = (char)Integer.parseInt(s.substring(i, i+8), 2);
			s2 += nextChar;
		}

		return s2;
	}




    public static void main(String[] args) {
    	SoundEncrypt x = new SoundEncrypt("Short.wav");
    	Encrypt("B");
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