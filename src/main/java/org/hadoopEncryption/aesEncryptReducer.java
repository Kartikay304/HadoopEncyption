package org.hadoopEncryption;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class aesEncryptReducer extends Reducer<Text, Text, Text, Text> {
    protected void reduce(Text key, Iterable<Text> values, Context context) {
        try {
            // Get the Plain Text
            String encryptedText = values.iterator().next().toString();

            // Write the encrypted text to HDFS. null is set as parameter so key value do not get stored into hadoop with cipher Text.
            context.write(null, new Text(encryptedText));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}