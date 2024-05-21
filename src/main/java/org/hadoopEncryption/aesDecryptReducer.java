package org.hadoopEncryption;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class aesDecryptReducer extends Reducer<Text, Text, Text, Text> {
    protected void reduce(Text key, Iterable<Text> values, Context context) {
        try {

            // Get the encrypted text
            String decryptedText = values.iterator().next().toString();

            // Write the decrypted text to HDFS. null is set as parameter so key value do not get stored into hadoop with plain Text.
            context.write(null, new Text(decryptedText));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}