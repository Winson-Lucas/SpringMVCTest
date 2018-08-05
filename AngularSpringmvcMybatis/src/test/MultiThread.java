package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MultiThread implements Runnable{
	public static long l1 = 0;
	public static void main(String[] args) throws InterruptedException {
		l1 = System.currentTimeMillis();
		Map<String, List<String>> mmp = new HashMap<String, List<String>>();
		String ind = "A";
		ind = "B";
		ind = "C";
		if("A".equals(ind)){
			Thread t = new Thread(new MultiThread(mmp, "A"));
			Thread t1 = new Thread(new MultiThread(mmp, "B"));
			Thread t2 = new Thread(new MultiThread(mmp, "C"));
			Thread t3 = new Thread(new MultiThread(mmp, "D"));
			t2.start();
			t.start();
			t1.start();
			t3.start();
		}else if("B".equals(ind)){
			mmp = new HashMap<String, List<String>>();
			MultiThread mt = new MultiThread(mmp, "F");
			mt.putMap();
			mt.processMap();
			 mt = new MultiThread(mmp, "G");
			mt.putMap();
			mt.processMap();
			 mt = new MultiThread(mmp, "H");
			mt.putMap();
			mt.processMap();
			 mt = new MultiThread(mmp, "I");
			mt.putMap();
			mt.processMap();
		}else{
			ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4, 200, TimeUnit.MILLISECONDS,
	                 new ArrayBlockingQueue<Runnable>(5));
			 for(int i=0;i<4;i++){
				 MultiThread myTask = new MultiThread(mmp, "A"+i);
	             executor.execute(myTask);
	             System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
	             executor.getQueue().size()+"，已执行玩别的任务数目："+executor.getCompletedTaskCount());
	         }
	         executor.shutdown();
	         System.out.println("close...");
		}
	}

	private Map<String, List<String>> ms;
	public String name = "";
	
	public MultiThread(Map<String, List<String>> mp, String name){
		ms = mp;
		this.name = name;
	}
	
	@Override
	public void run() {
		try {
			putMap();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		processMap();
	}
	
	private void processMap() {
		System.out.println("size "+ms.size());
		System.out.println("NameSize: "+name);
		if(ms.size() < 4){
			System.out.println("---------");
			return;
		}
	    System.out.println("break"+ ms.size());
	    for(Entry<String, List<String>> en : ms.entrySet()){
	    	System.out.println(en.getKey());
	    	System.out.println(en.getValue().get(0));
	    	System.out.println(en.getValue().get(en.getValue().size() - 1)); 
	    }
		System.out.println((System.currentTimeMillis() - l1)+"ms ---------");
	}

	public void putMap() throws InterruptedException{
		System.out.println("Name "+name);
		List<String> ls = new ArrayList<String>();
		for(int i = 0; i<= 1000000; i++){
			ls.add(name+i);
		}
		long ll = System.currentTimeMillis();
		while(true){
			long l2 = System.currentTimeMillis();
			if((l2 - ll) > 1000){
				break;
			}
		}
		synchronized (ms) {
			ms.put(name, ls);
		}
	}

}
