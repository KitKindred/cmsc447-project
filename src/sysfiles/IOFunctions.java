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
			
			throw new IOException("employees.txt does not exist!");
			
		}
		
		
		int i=0;
		int count=0;
		is= new Scanner(f);

		String line="";
		line=is.nextLine();
		System.out.println(line);
		count=Integer.parseInt(line);
		String ar[];
		while(is.hasNextLine()) {
			int id,type,worked;
			boolean active, attend;
			String name;

			line=is.nextLine();
			line=line.substring(1, line.length()-1);/*{id~name~type~active~worked~[SHIFTS]~<TORS>~ATTEND}*/
			System.out.println(line);


			ar=line.split("~");
			for(String s:ar) {
				System.out.print(s+" . ");

			}
			//System.out.println(ar);

			id=Integer.parseInt(ar[0]);
			name=ar[1];
			type=Integer.parseInt(ar[2]);
			active=Boolean.parseBoolean(ar[3]);		

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
	//return "startTime(local date time),length";
	private static Shift getsh(String str) {//already split by , now by "2018-11-08T15:00"
		System.out.println(str+"wksuyreigf");
		//str=str.trim();str=str.substring(1,str.length());
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

	public static int saveEmployeeToFile(Integer i,Profession emp) throws IOException {
		System.out.println("saving employee to file "+emp.getName());
		//int id=emp.getId();
		int hoursWorked, type;
		hoursWorked = emp.getHoursWorked();
		type = emp.getType();

		boolean active=emp.getActive();
		String name=emp.getName();

		ArrayList<Shift> sh=emp.getShifts();
		ArrayList<TimeOffRequest> tor=emp.getTimeOffRequests();


		String _i="",_t="",_n="",_a="",_hw="",_shz="",_toz="";
		if(debug) {_i="`id";_n="`name";_hw="`hoursworked";_t="`type";_a="`active";_shz="`shiftSize";_toz="`TimeOffSize";}

		String file;		

		//path="src/sysfiles/profiles/";
		file = i+".employee.txt";

		File f = new File(path+file);

		out = new PrintWriter(f);	

		out.println(i+_i);
		out.println(type+_t);
		out.println(name+_n);
		out.println(active+_a);
		out.println(hoursWorked+_hw);

		//out.println(sh.size()+_shz);
		for(Shift s: sh) {
			out.println(s.getData());
		}
		out.println("`");
		//out.println(tor.size()+_toz);
		for(TimeOffRequest t: tor) {
			out.println(t.getData());
		}	
		out.println("`");

		if(type==0) {
			//System.out.println(emp.getType());
			out.println(((Doctor)emp).getAttending());
		}

		if(out!=null) {
			out.close();
		}

		System.out.println("finished saving employee to file\n");

		return 0;

	}




	public static boolean saveAllEmployees(HashMap<Integer, Profession> prof) {

		try {
			File f = new File (path+"count.txt");
			out = new PrintWriter(f);
			out.println(prof.size());
			out.close();
		}catch(Exception e){
			System.out.println("UNABLE TO WRITE TO "+path+"count.txt");
			return false;
		}


		for(Map.Entry<Integer, Profession> ent: prof.entrySet()) {
			Integer ID = ent.getKey();
			Profession p=ent.getValue();

			try {
				saveEmployeeToFile(ID,p);

			}catch(IOException e) {
				System.out.println("ERROR SAVING EMPLOYEE "+ID+":"+p.getId()+" "+p.getName());


			}
		}

		return true;
	}

	public static Profession readEmployeeFromFile(String file) throws IOException{
		Profession p=null;
		File f=new File(path+file+".employee.txt");
		is=new Scanner(f);

		int id, type, hoursWorked;
		boolean active,attWeek=false;
		String name;

		//ArrayList<Shift> sh;
		//ArrayList<TimeOffRequest> tor;
		/*
String line;
System.out.println("START READING FILE");
		while(((line=is.nextLine())!=null)) {
			System.out.println(line);

		}*/


		id=Integer.parseInt(is.nextLine());
		type=Integer.parseInt(is.nextLine());
		name=is.nextLine();
		//is.nextLine();
		//System.out.println(id+"\n"+type+"\n"+name+"\n"+is.nextLine()+"\n");
		//throw new IllegalArgumentException();

		active=Boolean.parseBoolean(is.nextLine());
		hoursWorked=Integer.parseInt(is.nextLine());

		switch(type) {
		case 0:
			p=new Doctor(id,type,name);
			((Doctor)p).setAttending(attWeek);
			break;
		case 1:
			p=new Moonlighter(id,type,name);
			break;
		case 2:
			p=new Intern(id,type,name);
			break;
		default:
			throw new IllegalArgumentException();
		}	

		String line;
		while((line=is.nextLine().trim()).compareTo("`")!=0) {//get shift data

			System.out.println(line);
			Shift shift=getsh(line);
			shift.setEmployee(p);

			p.addShift(shift);
		}

		String[] s;
		while((line=is.nextLine().trim()).compareTo("`")!=0) {//get time off request data
			s=line.split(";");
			Shift shift=getsh(s[0]);
			TimeOffRequest t=new TimeOffRequest(shift,Integer.parseInt(s[1]));
			p.addTimeOff(t);

		}

		if((line=is.nextLine())!=null)
			attWeek=Boolean.parseBoolean(line);

		if(is!=null)
			is.close();

		p.setActive(active);
		p.setHoursWorked(hoursWorked);


		System.out.println("Emp Data:"+p);

		return p;
	}
	public static void/*HashMap<Integer, Profession>*/ readAllEmployees(){
		//HashMap<Integer, Profession> toReturn=new HashMap<Integer,Profession>();
		int empCount=0;
		try {
			File file=new File(path+"count.txt");
			if(!file.exists()) {
				out=new PrintWriter(file);
				out.println("0");
				out.close();
			}
			is=new Scanner(file);
			empCount=is.nextInt();
			is.close();
		}catch(Exception e) {
			System.out.println("UNABLE TO READ FROM "+path+"count.txt");
			is.close();
			return;
		}

		System.out.println("Employee count: " + empCount);
		for(int num=0; num < empCount; num+=1) {
			try {
				Profession pr=readEmployeeFromFile(""+num);
				ProgramDriver.addDoctor(pr.getType(), pr.getName(), pr.getId());
				//toReturn.put(num, readEmployeeFromFile(""+num));

			}catch(IOException e) {
				System.out.println("UNABLE TO READ EMPLOYEE "+num);

			}
		}
	}
}
