package demo;

	import java.text.SimpleDateFormat;
	import java.util.Date;
	import java.util.List;

	import kafka.javaapi.producer.Producer;
	import kafka.producer.KeyedMessage;

	import org.springframework.scheduling.annotation.Scheduled;
	import org.springframework.stereotype.Component;

	import demo.ResultProducer;

	@Component
	public class Scheduler {

		private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		public static Producer<Integer, String> producer= ResultProducer.getResultProducer();

		PollService ps= new PollService();

		@Scheduled(fixedRate = 50000)
		public void reportResultsOfExpiredPolls() {
			//System.out.println("The time is now " + dateFormat.format(new Date()));

			List<String> results=ps.getResultsOfExpiredPolls();

			if(results.size()!=0){

				for(int i=0;i<results.size();i++){
					KeyedMessage<Integer, String> data = new KeyedMessage<>(ResultProducer.topic, results.get(i));
					producer.send(data);
				}

				//producer.close();
				System.out.println("--------------producer has sent the result of expired Polls--------------");
			}
			else{
				
				System.out.println("None of the existing pole has expired !! - Pepole still voting !!!");
			}
		} 
	}


