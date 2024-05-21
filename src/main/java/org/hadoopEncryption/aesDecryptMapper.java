package org.hadoopEncryption;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class aesDecryptMapper extends Mapper<LongWritable, Text, Text, Text> {
    protected void map(LongWritable key, Text value, Context context) {
        try {
            String cipherText = value.toString();
            String encryptedData = AES.decrypt(cipherText);  //Cipher Text is decrypted using AES.

            // Output the pair <1,Plain Text>, Reducer merges all the Plain Text with key 1 and store in hadoop.
            context.write(new Text("1"), new Text(encryptedData));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}