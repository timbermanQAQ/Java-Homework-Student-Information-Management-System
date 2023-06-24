package timberman666;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static timberman666.StudentSystem.*;

public class StudentManagementSystemGUI extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private ArrayList<Student> list;
    private JButton addButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private JButton queryButton;
    private JLabel idLabel;
    private JTextField idField;

    public StudentManagementSystemGUI(ArrayList<Student> list) {
        this.list = list;
        this.setTitle("学生管理系统");
        this.setSize(800, 200);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        addButton = new JButton("添加学生");
        addButton.addActionListener(this);
        buttonPanel.add(addButton);

        modifyButton = new JButton("修改学生信息");
        modifyButton.addActionListener(this);
        buttonPanel.add(modifyButton);

        deleteButton = new JButton("删除学生");
        deleteButton.addActionListener(this);
        buttonPanel.add(deleteButton);

        queryButton = new JButton("查询学生信息");
        queryButton.addActionListener(this);
        buttonPanel.add(queryButton);

        JPanel idPanel = new JPanel();
        idPanel.setLayout(new FlowLayout());
        idLabel = new JLabel("计算机类 班级： 序列号： 学号： 姓名：");
        idPanel.add(idLabel);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(idPanel, BorderLayout.SOUTH);
        this.setContentPane(mainPanel);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addStudent(list);
        } else if (e.getSource() == modifyButton) {
            modifyStudent(list);
        } else if (e.getSource() == deleteButton) {
            deleteStudent(list);
        } else if (e.getSource() == queryButton) {
            queryStudent(list);
        }
    }
}