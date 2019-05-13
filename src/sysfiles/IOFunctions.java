package sysfiles;

import java.io.*;
import java.util.*;
import classinfo.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IOFunctions {

	private static Scanner is;
	private static PrintWriter out;

	private static final String path = "src/sysfiles/profiles/";

	private static final boolean debug=false;


	public static int saveEmployees() throws IOException{
		System.out.println("ENTERING SAVEEMPLOYEES");

		HashMap<Integer, Profession> e=ProgramDriver.getEmployees();
		File f=new File(path+"employees.txt");
		out = new PrintWriter(f);

		String storeLine="";
		int i=0;

		storeLine+=(e.size()+"\r\n");
		System.out.println("e.size:"+e.size());
		System.out.println(""+e.values());
		for(Profession p: e.values()) {
			System.out.println(" "+p.getId());
			storeLine+="{";

			storeLine+=p.getId()+"~";
			storeLine+=p.getName()+"~";
			storeLine+=p.getType()+"~";
			storeLine+=p.getActive()+"~";
			storeLine+=p.getHoursWorked()+"~[";
			for(Shift s:p.getShifts()) {
				storeLine.concat(s.getData()+"`");//return "startTime(local date time),length";
			}
			storeLine+=("]~<");
			for(TimeOffRequest tor: p.getTimeOffRequests()) {
				storeLine+=tor.getData()+"`";//return this.s.getData()+";"+this.priority;

			}
			storeLine+=(">");


			if(p.getType()==0) {

				storeLine+=("~"+((Doctor) p).getAttending());

			}

			storeLine+=("}\r\n");

			System.out.println(storeLine);
			out.write(storeLine);
			i+=1;
			storeLine="";
		}

		if(out!=null)
			out.close();


		return i;
	}


	public static int loadEmployees() throws IOException{
		File f=new File(path+"employees.txt");
		if(!f.exists()) {
			out=new PrintWriter(f);
			out.write("0");
			out.write("\r\n");
			out.close();
			System.out.println("Created employees.txt");
		}
		
		
		int i=0;
		int count=0;
		is= new Scanner(f);

		String line="";
		if (is.hasNextLine()) {
			line=is.nextLine();
			System.out.println(line);
			count=Integer.parseInt(line);
		}
		else {
			out=new PrintWriter(f);
			out.write("0");
			out.write("\r\n");
			out.close();

			is= new Scanner(f);
			is.nextLine();
			
			System.out.println("fixed empty file");
		}
		
		
		String ar[];
		while(is.hasNextLine()) {
			int id = -1,type = -1, worked;
			boolean active = false, attend;
			String name = "";

			line=is.nextLine();
			line=line.substring(1, line.length()-1);/*{id~name~type~active~worked~[SHIFTS]~<TORS>~ATTEND}*/
			System.out.println(line);


			ar=line.split("~");
			for(String s:ar) {
				System.out.print(s+" . ");
			}
			try {
				id=Integer.parseInt(ar[0]);
				name=ar[1];
				type=Integer.parseInt(ar[2]);
				active=Boolean.parseBoolean(ar[3]);		
			}
			catch (Exception e) {
				System.out.println("Error parsing saved employee file: " + e.toString());
				
			}
			ProgramDriver.addDoctor(type, name, id);

			Profession p=ProgramDriver.getEmployees().get(id);

			System.out.println(""+ar[5]);
			String shifts=ar[5];
			if(shifts.length()>2) {
				for(String l:ar[5].split("`")) {
					Shift sh=getsh(l);
					p.addShift(sh);			

				}
			}

			System.out.println(""+ar[6]);
			String reqs=ar[6].substring(1, ar[6].length()-1);
			if(reqs.length()>0) {
				for(String l:reqs.split("`")) {
					System.out.println("line "+l);
					TimeOffRequest tor = gettor(l);
					p.addTimeOff(tor);
				}
			}
			if(ar.length>7) {((Doctor) p).setAttending(Boolean.parseBoolean(ar[7]));}

		}
		if(is!=null)
			is.close();
		return i;
	} 
	
	private static Shift getsh(String str) {//already split by , now by "2018-11-08T15:00"

		String[] s=str.split(",");
		System.out.println(str);
		
		DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime ldt=LocalDateTime.parse(s[0], formatter);
		Shift sh=new Shift(ldt,Integer.parseInt(s[1]));

		return sh;
	}
	private static TimeOffRequest gettor(String str) {//return this.s.getData()+";"+this.priority;
		String[] s=str.split(";");
		TimeOffRequest tor= new TimeOffRequest(getsh(s[0]), Integer.parseInt(s[1]));		
		return tor;
	}
}
