package org.hadoopEncryption;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class aesEncryptMapper extends Mapper<LongWritable, Text, Text, Text> {
    protected void map(LongWritable key, Text value, Context context) {
        try {
            String inputText = value.toString();
            String encryptedData = AES.encrypt(inputText);  //Plain Text is encrypted using AES.

            // Output the pair <1,Cipher Text>, Reducer merges all the Cipher Text with key 1 and store in hadoop.
            context.write(new Text("1"), new Text(encryptedData));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}