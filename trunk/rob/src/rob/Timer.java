package rob;

public class Timer implements Runnable{
	private Thread job;
	private int seconds;

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
/*		try{
			Thread.sleep(1000);
		}catch (InterruptedException exc){
		    // Lo stato di sleeping � stato interrotto
		    // da un altro thread  
		}
		job.interrupt();*/
		//while(true){

			System.out.println("ttttttttttttttttt");
			try{
				Thread.sleep(1000*seconds);
			}catch (InterruptedException exc){
			    // Lo stato di sleeping � stato interrotto
			    // da un altro thread  
			}
			System.out.println("KILL!!!");
			//job.interrupt();
			job.stop();
			   try
			   {
			    Thread.sleep(10);
			   }
			   catch (InterruptedException exc)
			   {
			    // Lo stato di sleeping � stato interrotto
			    // da un altro thread  
			   }
			   
		//}
	}
	
	public void set(Thread job, int seconds){
		this.job = job;
		this.seconds = seconds;
	}
	

}
