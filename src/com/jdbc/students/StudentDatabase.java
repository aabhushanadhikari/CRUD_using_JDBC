package com.jdbc.students;
import java.sql.*;
import java.util.*;

public class StudentDatabase {
	static Scanner sc=new Scanner(System.in);
	 public static Connection connection=null;
	public static void main(String[] args) throws Exception  {
		
		
		StudentDatabase studentDatabase=new StudentDatabase();
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			String dburl="jdbc:mysql://localhost:3306/jdbc";
			String dbusername="root";
			String dbpassword="";
			connection=DriverManager.getConnection(dburl,dbusername,dbpassword);
			System.out.println("Enter your choice:");
			System.out.println("1.Enter Records");
			System.out.println("2.Read Records");
			System.out.println("3.Read all the records of the table");
			System.out.println("4.Read  the records of the table using callable");
			System.out.println("5. Update the record");
			int choice=Integer.parseInt(sc.nextLine());
			switch(choice) {
			case 1:
				studentDatabase.insertRecords();
				break;
			case 2:
				studentDatabase.readRecords();
				break;
			case 3:
				studentDatabase.readAllRecords();
				break;
			case 4:
				studentDatabase.readRecordsByRollNo();
				break;
			case 5:
				studentDatabase.updateRecords();
				break;
			default:
				break;
			}
			
			
				
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Something went wrong");
		}
	}
	private void insertRecords() throws SQLException{
		System.out.println("Inside Insert Records");
		String sql="Insert into students(name,percentage,address) values (?,?,?)";
		PreparedStatement statement=connection.prepareStatement(sql);
		System.out.println("Enter the name:");
		statement.setString(1, sc.nextLine());
		System.out.println("Enter the percentage:");
		statement.setDouble(2, Double.parseDouble(sc.nextLine()));
		System.out.println("Enter the address");
		statement.setString(3,sc.nextLine());
		int rows=statement.executeUpdate();
		if(rows>0) {
			System.out.println("Update Successful");
		}
	}
	private void readRecords() throws SQLException {
		System.out.println("Enter the roll number you want to read");
		int number=Integer.parseInt(sc.nextLine());
		String sql="Select * from students where roll_number = "+number;
		Statement statement=connection.createStatement();
		ResultSet result=statement.executeQuery(sql);
		if(result.next()) {
			int rollnumber=result.getInt("roll_number");
			String name=result.getString("name");
			double percentage=result.getDouble("percentage");
			String address=result.getString("address");
			System.out.println("The roll number is "+rollnumber);
			System.out.println("The address is "+address);
			System.out.println("The percentage is "+percentage);
			System.out.println("The name is "+name);
		}
		else {
			System.out.println("The roll number was not found");
		}
		
	}
	private void readAllRecords () throws SQLException{
		CallableStatement callableStatement= connection.prepareCall("{ call GET_ALL() }");
		ResultSet resultset=callableStatement.executeQuery();
		while(resultset.next()) {
			System.out.println("Roll number="+resultset.getInt("roll_number"));
			System.out.println("Name ="+resultset.getString("name"));
			System.out.println("Address ="+resultset.getString("address"));
			System.out.println("Percentage ="+resultset.getDouble("percentage"));
			System.out.println("-----------------------------------------------");
		}
		
	}
	private void readRecordsByRollNo () throws SQLException{
		System.out.println("Type the roll-number of the student ");
		int s=sc.nextInt();
		CallableStatement callableStatement= connection.prepareCall("{ call GET_RECORD(?) }");
		callableStatement.setInt(1, s);
		ResultSet resultset=callableStatement.executeQuery();
		while(resultset.next()) {
			System.out.println("Roll number="+resultset.getInt("roll_number"));
			System.out.println("Name ="+resultset.getString("name"));
			System.out.println("Address ="+resultset.getString("address"));
			System.out.println("Percentage ="+resultset.getDouble("percentage"));
			System.out.println("-----------------------------------------------");
		}
	}
	private void updateRecords() throws SQLException{
		System.out.println("Enter the roll no. of the student that you want to update");
		int rollNumber=Integer.parseInt(sc.nextLine());
		String sql="Select * from students where roll_number = "+rollNumber;
		Statement statement=connection.createStatement();
		ResultSet resultSet=statement.executeQuery(sql);
		if(resultSet.next()) {
			System.out.println("Here are the choices");
			System.out.println("Press 1 for name");
			System.out.println("Press 2 for percentage");
			System.out.println("Press 3 for address");
			String sqlQuery="update students set ";
			int choice=Integer.parseInt(sc.nextLine());
			
			switch(choice) {
			case 1:
				System.out.println("Enter the name you want to update : ");
				String name=sc.nextLine();
				sqlQuery=sqlQuery+"name = ? where roll_number = "+rollNumber;
				PreparedStatement preparedStatement=connection.prepareStatement(sqlQuery);
				preparedStatement.setString(1,name);
				int rows=preparedStatement.executeUpdate();
				if(rows>0) {
					System.out.println("The name was updated successfully");
				}
				break;
			case 2:
				System.out.println("Enter the new percentage");
				double percentage=Double.parseDouble(sc.nextLine());
				sqlQuery=sqlQuery+"percentage = ? where roll_number = "+rollNumber;
				PreparedStatement preparedStatement2=connection.prepareStatement(sqlQuery);
				preparedStatement2.setDouble(1, percentage);
				int rows2=preparedStatement2.executeUpdate();
				if(rows2>0) {
					System.out.println("The percentage was updated successfully");
				}
				break;
			case 3:System.out.println("Enter the new address");
			String address=sc.nextLine();
			sqlQuery=sqlQuery+"address = ? where roll_number = "+rollNumber;
			PreparedStatement preparedStatement3=connection.prepareStatement(sqlQuery);
			preparedStatement3.setString(1, address);
			int rows3=preparedStatement3.executeUpdate();
			if(rows3>0) {
				System.out.println("The address was updated successfully");
			}
				break;
			}
		}else {
			System.out.println("The roll no. that you have entered is not in the table");
		}
	}
}
