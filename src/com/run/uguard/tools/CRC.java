package com.run.uguard.tools;
public class CRC { 
   public final static int polynomial = 0x1021;   // Represents x^16+x^12+x^5+1 
   private static CRC crcTool = null;
   private int crc; 
   
   private CRC(){ 
	      crc = 0x0000; 
	   } 
   public static CRC getCrcTool(){
	   if(crcTool == null){
		   crcTool = new CRC();
	   }
	   return crcTool;
   }

    
   private String getCRCHexString(){ 
      String crcHexString = Integer.toHexString(crc); 
      while(crcHexString.length() != 4){
    	  crcHexString = "0"+crcHexString;
      }
      crc = 0x0000; 
      return crcHexString.toUpperCase(); 
   } 
    
 
   private void update(byte[] args) { 
        for (byte b : args) { 
            for (int i = 0; i < 8; i++) { 
                boolean bit = ((b   >> (7-i) & 1) == 1); 
                boolean c15 = ((crc >> 15    & 1) == 1); 
                crc <<= 1; 
                // If coefficient of bit and remainder polynomial = 1 xor crc with polynomial 
                if (c15 ^ bit) crc ^= polynomial; 
             } 
        } 

        crc &= 0xffff; 
    }    
   private  byte[] hexStringToBytes(String hexString) {  
	    hexString = hexString.toUpperCase().replace(" ", "");  
	    int length = hexString.length() / 2;  
	    char[] hexChars = hexString.toCharArray();  
	    byte[] d = new byte[length];  
	    for (int i = 0; i < length; i++) {  
	        int pos = i * 2;  
	        d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
	    }  
	    return d;  
	}  
   
   private byte charToByte(char c) {  
	    return (byte) "0123456789ABCDEF".indexOf(c);  
	} 
   public  String getCRCCode(String message){
	   this.update(this.hexStringToBytes(message));
	  return this.getCRCHexString();
   }
} 