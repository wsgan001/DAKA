
import com.google.common.collect.Sets;
import com.google.common.io.Closeables;

import daka.compute.fpgrowth.parallel.PFPGrowth;
import daka.compute.helpers.Parameters;
import daka.core.Task;
import daka.core.TaskConfig;
import daka.io.FileInput;
import daka.util.FileUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FPDriver extends Task {
	private static final Logger log = LoggerFactory.getLogger(FPDriver.class);

	@Override
	public void PreExecute(TaskConfig config){
		String inputFile=config.getArguments().get("if");
		if(inputFile!=null)
		{
			FileInput fi=new FileInput(inputFile,
				config.getInputPath(),config.getConfig());

			try{
				fi.Update();
			} catch(IOException ex){
			}
		}
	}

	@Override
	public void Execute(TaskConfig config){
		try{
			Parameters params = new Parameters();

			params.set("minSupport", "100");
			params.set("maxHeapSize", "50");
			params.set("numGroups", "1000");
			params.set("treeCacheSize", "4000");
			String encoding = "UTF-8";
			params.set("encoding", encoding);

			Path inputDir = new Path(config.getInputPath());
			Path outputDir = new Path(config.getOutputPath());

			params.set("input", inputDir.toString());
			params.set("output", outputDir.toString());

			FileUtil.delete(config.getConfig(), outputDir);
			PFPGrowth.runPFPGrowth(params);//, config.getConfig());
		} catch(IOException ex){
		} catch(InterruptedException ex){
		} catch(ClassNotFoundException ex){
		} catch(Exception ex){
		}
	}

	@Override
	public void PostExecute(TaskConfig config){}
}
