package org.hadoopEncryption;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.net.URI;

public class aesDecryptJob extends Configured implements Tool{

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new aesDecryptJob(), args);
        System.exit(exitCode);

    }
    public int run(String[] args) throws Exception {
        //input and output are retrieved from arguments.
        String inputPath = args[0];
        String outputPath = args[1];

        Configuration conf = getConf();
        // Here hadoop block size is determined, In hadoop 1 mapper is assigned to each block so by adjusting the size in(bytes)
        conf.set("mapreduce.input.fileinputformat.split.minsize", "43690666");
        conf.set("mapreduce.input.fileinputformat.split.maxsize", "43690666");

        Job job = Job.getInstance(conf, "DecryptJob");
//        job.setNumReduceTasks(10); // We can also set no of reducers, with increase in no of reducers  there is less chance to get a failure in merging process.

        job.setJarByClass(getClass());

        // Set input and output paths
        TextInputFormat.addInputPath(job,  new Path(args[0]));
        TextOutputFormat.setOutputPath(job, new Path(args[1]));

        // Set Mapper and Reducer classes
        job.setMapperClass(aesDecryptMapper.class);
        job.setReducerClass(aesDecryptReducer.class);

        //RSA public/private key and AES secrete/four key paths are added in CacheFile so mapper when running  can access these files.
        job.addCacheFile(new URI("file:///home/ubuntu/hadoopEncryption/public.key#public"));
        job.addCacheFile(new URI("file:///home/ubuntu/hadoopEncryption/private.key#private"));
        job.addCacheFile(new URI("file:///home/ubuntu/hadoopEncryption/secrete.key#secrete"));
        job.addCacheFile(new URI("file:///home/ubuntu/hadoopEncryption/four.key#four"));


        // Set input and output formats
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        // Set output key and value classes
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // Submit the job and wait for completion
        return job.waitForCompletion(true) ? 0 : 1;
    }
}