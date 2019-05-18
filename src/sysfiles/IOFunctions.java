package sysfiles;

import java.io.*;
import java.util.*;
import classinfo.*;
import gui.controller;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IOFunctions {

	private static Scanner is;
	private static PrintWriter out;

	private static final String path = "src/sysfiles/profiles/";

	private static final boolean debug=false;


	public static int saveEmployees() throws IOException{
		System.out.println("ENTERING SAVEEMPLOYEES");
		System.out.println("gCD: "+controller.getCurrentDate().toString());
		HashMap<Integer, Profession> e=ProgramDriver.getEmployees();
		File f=new File(path+controller.getFileCurrentDate()+".txt");
		//File f=new File(path+"employees.txt");
		out = new PrintWriter(f);

		String storeLine="";
		String currentDate=controller.getCurrentDate().toString();
		int i=0;

		storeLine+=(currentDate+"\r\n");
		storeLine+=(e.size()+"\r\n");

		System.out.println("e.size:"+e.size());
		System.out.println(""+e.values());
		for(Profession p: e.values()) {
			System.out.print(" "+p.getId());
			storeLine+="{";

			storeLine+=p.getId()+"~";
			storeLine+=p.getName()+"~";
			storeLine+=p.getType()+"~";
			storeLine+=p.getActive()+"~";
			storeLine+=p.getHoursWorked()+"~";
			storeLine+=p.getEmail()+"~[";

			for(Shift s:p.getShifts()) {
				storeLine.concat(s.getData()+"`");
			}
			storeLine+=("]~<");
			for(TimeOffRequest tor: p.getTimeOffRequests()) {
				storeLine+=tor.getData()+"`";

			}
			storeLine+=(">~");

			String ldtstr = p.getInactiveDate() != null
					? p.getInactiveDate().toString()
							:"";

					storeLine+=ldtstr;

					if(p.getType()==0) {
						storeLine+=("~"+((Doctor) p).getAttending());
						storeLine+=("~"+((Doctor)p).getAttendingDate());
					}
					else {
						storeLine+="~false~null";

					}

					storeLine+=("}\r\n");

					out.write(storeLine);
					i+=1;
					storeLine="";
		}

		if(out!=null)
			out.close();


		return i;
	}

	public static int loadEmployees() throws IOException{
		if(controller.getCurrentDate()==null) {return -1;}

		File f=new File(path+controller.getFileCurrentDate()+".txt");
		//File f=new File(path+"employees.txt");
		if(!f.exists()) {
			out=new PrintWriter(f);

			System.out.println("gCD: "+controller.getCurrentDate().toString());
			out.write(controller.getCurrentDate().toString()+"\r\n");
			out.write("0\r\n");
			out.close();
			System.out.println("Created employees.txt");
		}
		//ProgramDriver.getID();
		ProgramDriver.getEmployees().clear();
		ProgramDriver.getActiveID().clear();
		ProgramDriver.getNameID().clear();


		int i=0;
		int count=0;
		String currentDate="";
		is= new Scanner(f);

		String line="";
		if (is.hasNextLine()) {

			currentDate=is.nextLine().trim();
			line=is.nextLine();
			System.out.println(line+" "+currentDate);
			count=Integer.parseInt(line);

			DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
			controller.setCurrentDate(LocalDateTime.parse(currentDate, format));
		}
		else {
			out=new PrintWriter(f);
			out.write(controller.getCurrentDate().toString()+"\r\n");
			out.write("0\r\n");

			//out.write("\r\n");
			out.close();

			is= new Scanner(f);
			is.nextLine();

			System.out.println("fixed empty file");
		}

		String ar[];
		while(is.hasNextLine()) {
			int id = -1,type = -1, worked;
			int active = -1;
			boolean attend;
			String name = "";
			String email="";
			String inactiveDate="";
			String attDate="";
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
				active=Integer.parseInt(ar[3]);		
				worked=Integer.parseInt(ar[4]);
				email=ar[5];

			}
			catch (Exception e) {
				System.out.println("Error parsing saved employee file: " + e.toString());

			}
			ProgramDriver.addDoctor(type, name, id,email);

			System.out.println(id);
			System.out.println(ProgramDriver.getEmployees());

			Profession p=ProgramDriver.getEmployees().get(id);
			System.out.println(p);

			p.setActive(active);
			System.out.println("pemail: "+p.getEmail());
			System.out.println(""+ar[6]);
			String shifts=ar[6];
			if(shifts.length()>2) {
				for(String l:ar[6].split("`")) {
					Shift sh=getsh(l);
					p.addShift(sh);			

				}
			}

			System.out.println(""+ar[7]);
			String reqs=ar[7].substring(1, ar[7].length()-1);
			if(reqs.length()>0) {
				for(String l:reqs.split("`")) {
					System.out.println("line "+l);
					TimeOffRequest tor = gettor(l);
					p.addTimeOff(tor);
				}
			}

			inactiveDate=ar[8];
			LocalDateTime ldtInactive;
			DateTimeFormatter format=DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

			if(inactiveDate.length()>0) {
				ldtInactive=LocalDateTime.parse(inactiveDate, format);
				p.setInactiveDate(ldtInactive);
				System.out.println("p. "+p.getInactiveDate());
			}

			if(p.getType()==0) {
				if(ar.length>9) {((Doctor) p).setAttending(Boolean.parseBoolean(ar[8]));}

				attDate=ar[10];
				if(ar.length>10) {
					if(attDate.equals("null")) {
						((Doctor)p).setAttendingDate(null);
						break;
					}
					else{
						ldtInactive=LocalDateTime.parse(attDate, format);
						((Doctor)p).setAttendingDate(ldtInactive);
						System.out.println(((Doctor)p).getAttendingDate());
					}
				}
			}
		}
		if(is!=null)
			is.close();

		System.out.println("IOLoad: "+ProgramDriver.getEmployees());

		return i;
	} 

	public static int killFile(String filePath)throws IOException {
		System.out.println("\nIO: "+filePath);
		File f=new File(filePath);
		System.out.println(f.getParent());
		System.out.println(f.getPath());
		System.out.println(f.list());
		System.out.println(f.getParentFile().listFiles());
		if(f.exists()) {
			System.out.println("exists");
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Delete Calendar Employee Data");
			alert.setHeaderText("Warning");
			alert.setContentText("This action is irreversible.  Are you sure you want to delete this file?");// to "+ProgramDriver.getEmployees().get(currentID).getName());

			alert.initModality(Modality.APPLICATION_MODAL);

			ButtonType yesB= new ButtonType("Yes");
			ButtonType noB= new ButtonType("No");

			alert.getButtonTypes().setAll(yesB,noB);
			alert.setResizable(false);
			Optional<ButtonType> result=alert.showAndWait();

			if(result.get()==yesB) {
				System.out.println("Deleting "+filePath);
				f.delete();
				return 0;
			}


		}

		return -1;
	}

	private static Shift getsh(String str) {

		String[] s=str.split(",");
		System.out.println(str);

		DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		LocalDateTime ldt=LocalDateTime.parse(s[0], formatter);
		Shift sh=new Shift(ldt,Integer.parseInt(s[1]));

		return sh;
	}
	private static TimeOffRequest gettor(String str) {
		String[] s=str.split(";");
		TimeOffRequest tor= new TimeOffRequest(getsh(s[0]), Integer.parseInt(s[1]));		
		return tor;
	}

	public static boolean exportCalendar(String iCal) {
		if (iCal == null) {
			return false;
		}

		StringBuilder builder = new StringBuilder(); 
		File file = new File("cal.ics");
		builder.append("cal");
		builder.append(".ics");

		try {
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(iCal);
			bw.close();

			return true;
		} catch (IOException e) {
			System.out.println("Error saving calendar:" + e.getMessage());
			return false;
		}
	}
}
