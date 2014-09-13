package test;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SockTest {
	
	public static void main(String[] args){
		try {
			Socket client = new Socket("localhost", 4321);
			ObjectOutputStream out = new ObjectOutputStream(
					client.getOutputStream());
			out.flush();
			ObjectInputStream in = new ObjectInputStream(
					client.getInputStream());
			System.out.println("Buffer size: " + client.getSendBufferSize());
			for (int i = 0; i < 10; i++) {

				if (i == 3) {
					Thread.currentThread().interrupt();
					System.out.println("Interrupted.");
				}
				out.writeObject("From Client: Hellow." + i);
				out.flush();
				System.out.println(in.readObject());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	}
