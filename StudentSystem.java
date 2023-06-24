package timberman666;

import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StudentSystem {
    public static void main(String[] args) throws IOException {
        InputStream audioStream = StudentSystem.class.getResourceAsStream("/resources/music.wav");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = audioStream.read(buffer)) > -1 ) {
            out.write(buffer, 0, len);
        }
        out.flush();

        AudioInputStream audioInputStream = null;
        try {
            byte[] audioData = out.toByteArray();
            InputStream byteArrayStream = new ByteArrayInputStream(audioData);
            audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(byteArrayStream));
        } catch (UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }


        Clip clip = null;

        try {
            clip = AudioSystem.getClip();
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        try {
            clip.open(audioInputStream); // 直接使用之前缓存的audioInputStream
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();

        ArrayList<Student> list=new ArrayList<>();
        StudentManagementSystemGUI smsg=new StudentManagementSystemGUI(list);

    }

    //add student
    public static void addStudent(ArrayList<Student> list) {
        System.out.println("Add a student");
        Scanner sc=new Scanner(System.in);

        try {
            File outputFile = new File("output.txt");
            Scanner scanner = new Scanner(outputFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Pattern pattern = Pattern.compile("Student\\{id = (.*), name = (.*), age = (.*), address = (.*)\\}");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String id = matcher.group(1).trim();
                    String name = matcher.group(2).trim();
                    int age = Integer.parseInt(matcher.group(3).trim());
                    String address = matcher.group(4).trim();

                    Student s1 = new Student(id, name, age, address);
                    list.add(s1);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Student s= new Student();
        String id=null;
        while (true) {
            System.out.println("Please enter the student's id");
            id=sc.next();
            boolean flag=contains(list,id);
            if(flag){
                System.out.println("The ID already exists, please re-enter it");
            }
            else{
                s.setId(id);
                break;
            }

        }

        System.out.println("Please enter the student's name");
        String name=sc.next();
        s.setName(name);

        System.out.println("Please enter the student's age");
        int age= sc.nextInt();
        s.setAge(age);

        System.out.println("Please enter the student's address");
        String address = sc.next();
        s.setAddress(address);

        list.add(s);

        try {
            PrintWriter pw = new PrintWriter(new FileWriter("output.txt", true));
            pw.println(s);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Student information added successfully");
    }

    //delete student
    public static void deleteStudent(ArrayList<Student> list) {
        System.out.println("Delete a student");
        System.out.println("Please enter the student's id");
        Scanner sc=new Scanner(System.in);
        try {
            File outputFile = new File("output.txt");
            Scanner scanner = new Scanner(outputFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Pattern pattern = Pattern.compile("Student\\{id = (.*), name = (.*), age = (.*), address = (.*)\\}");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String id = matcher.group(1).trim();
                    String name = matcher.group(2).trim();
                    int age = Integer.parseInt(matcher.group(3).trim());
                    String address = matcher.group(4).trim();
                    Student s = new Student(id, name, age, address);
                    list.add(s);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String id=sc.next();
        int index=getIndex(list,id);

        if(index>=0){
            list.remove(index);
            try {
                PrintWriter pw = new PrintWriter(new FileWriter("output.txt"));
                for (Student student : list) {
                    pw.println(student);
                }
                pw.close();
                System.out.println("Student information deleted successfully");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("id does not exist, deletion failed");
        }


    }

    //modify student
    public static void modifyStudent(ArrayList<Student> list) {
        System.out.println("Modify a student");
        System.out.println("Please enter the student's id");
        Scanner sc=new Scanner(System.in);

        try {
            File outputFile = new File("output.txt");
            Scanner scanner = new Scanner(outputFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Pattern pattern = Pattern.compile("Student\\{id = (.*), name = (.*), age = (.*), address = (.*)\\}");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String id = matcher.group(1).trim();
                    String name = matcher.group(2).trim();
                    int age = Integer.parseInt(matcher.group(3).trim());
                    String address = matcher.group(4).trim();
                    Student s = new Student(id, name, age, address);
                    if (getIndex(list, id) != -1) {
                        System.out.println("ID already exists");
                        scanner.close();
                        return;
                    }

                    list.add(s);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        String id=sc.next();
        int index=getIndex(list,id);

        if(index==-1){
            System.out.println("The ID to be modified does not exist " +id+ " please re-enter it");
            return;
        }
        Student stu=list.get(index);
        System.out.println("Please enter the name of the student you want to modify");
        String newName= sc.next();
        stu.setName(newName);

        System.out.println("Please enter the age of the student you want to modify");
        int newAge= sc.nextInt();
        stu.setAge(newAge);

        System.out.println("Please enter the address of the student you want to modify");
        String newAddress= sc.next();
        stu.setAddress(newAddress);

        try {
            PrintWriter pw = new PrintWriter(new FileWriter("output.txt"));
            for (Student student : list) {
                if (student.getId().equals(stu.getId())) {
                    pw.println(stu);
                } else {
                    // 检查当前学生记录的 ID 是否已经在输出文件中存在，避免出现重复记录的情况
                    File outputFile = new File("output.txt");
                    Scanner scanner = new Scanner(outputFile);
                    boolean idExists = false;
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        Pattern pattern = Pattern.compile("Student\\{id = (.*), name = (.*), age = (.*), address = (.*)\\}");
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            String recordId = matcher.group(1).trim();
                            if (recordId.equals(student.getId())) {
                                idExists = true;
                                break;
                            }
                        }
                    }
                    scanner.close();
                    if (!idExists) {
                        pw.println(student);
                    }
                }
            }
            pw.close();
            System.out.println("The student's information was modified successfully");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //query student
    public static void queryStudent(ArrayList<Student> list) {
        System.out.println("Query a student");

        try {
            File outputFile = new File("output.txt");
            Scanner scanner = new Scanner(outputFile);
            if (!scanner.hasNextLine()) {
                System.out.println("There is currently no student information, please add it and inquire");
                scanner.close();
                return;
            }
            System.out.println("id\tname\tage\taddress");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Pattern pattern = Pattern.compile("Student\\{id = (.*), name = (.*), age = (.*), address = (.*)\\}");
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    String id = matcher.group(1).trim();
                    String name = matcher.group(2).trim();
                    int age = Integer.parseInt(matcher.group(3).trim());
                    String address = matcher.group(4).trim();
                    System.out.println(id + "\t" + name + "\t" + age + "\t" + address);
                }
            }
            scanner.close();
            return;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < list.size() ; i++) {
            Student stu=list.get(i);
            System.out.println(stu.getId()+"\t"+stu.getName()+"\t"+stu.getAge()+"\t"+stu.getAddress());

        }

    }

    //Get the index by id
    public static int getIndex(ArrayList<Student> list,String id) {
        for (int i = 0; i < list.size(); i++) {
            Student stu=list.get(i);
            String sid= stu.getId();
            if(sid.equals(id)) {
                return i;
            }
        }
        return -1;
    }

    //Determine whether the id exists in the collection
    public static boolean contains(ArrayList<Student> list,String id) {
        for (int i = 0; i < list.size(); i++) {
            Student s=list.get(i);
            String sid=s.getId();
            if(sid.equals(id)){
                return true;
            }
        }
        return false;
    }
}
