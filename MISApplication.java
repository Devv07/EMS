import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Scanner;

// Class to represent an employee
class Employee implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private int id;
    private String department;
    private double salary;

    public Employee(String name, int id, String department, double salary) {
        this.name = name;
        this.id = id;
        this.department = department;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return id + "\t" + name + "\t" + department + "\t$" + salary;
    }
}

// Class to manage the system
class ManagementSystem {
    private ArrayList<Employee> employeeList;

    public ManagementSystem() {
        employeeList = new ArrayList<>();
        loadEmployeesFromFile();
    }

    // Add an employee
    public void addEmployee(Employee emp) {
        employeeList.add(emp);
        saveEmployeesToFile();
        System.out.println("Employee added successfully.");
    }

    // Display all employees
    public void displayEmployees() {
        if (employeeList.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            for (Employee emp : employeeList) {
                System.out.println(emp);
            }
        }
    }

    // Save employees to file
    public void saveEmployeesToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("employees.dat"))) {
            oos.writeObject(employeeList);
            System.out.println("Employees saved to file.");
        } catch (IOException e) {
            System.out.println("Error saving employees to file: " + e.getMessage());
        }
    }

    // Load employees from file
    @SuppressWarnings("unchecked")
    public void loadEmployeesFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("employees.dat"))) {
            employeeList = (ArrayList<Employee>) ois.readObject();
            System.out.println("Employees loaded from file.");
        } catch (FileNotFoundException e) {
            System.out.println("No existing data found. Starting fresh.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading employees from file: " + e.getMessage());
        }
    }

    // Handle employee not found exception
    public Employee findEmployeeById(int id) throws EmployeeNotFoundException {
        for (Employee emp : employeeList) {
            if (emp.getId() == id) {
                return emp;
            }
        }
        throw new EmployeeNotFoundException("Employee with ID " + id + " not found.");
    }

    // Delete an employee
    public void deleteEmployee(int id) throws EmployeeNotFoundException {
        Employee emp = findEmployeeById(id);
        employeeList.remove(emp);
        saveEmployeesToFile();
        System.out.println("Employee deleted successfully.");
    }

    // Update an employee
    public void updateEmployee(int id, Scanner scanner) throws EmployeeNotFoundException {
        Employee emp = findEmployeeById(id);
        System.out.println("What would you like to update?");
        System.out.println("1. Name");
        System.out.println("2. ID");
        System.out.println("3. Department");
        System.out.println("4. Salary");
        System.out.print("Choose an option: ");
        int updateChoice = scanner.nextInt();

        switch (updateChoice) {
            case 1:
                System.out.print("Enter New Name: ");
                scanner.nextLine();  // Consume newline
                String newName = scanner.nextLine();
                emp.setName(newName);
                break;

            case 2:
                System.out.print("Enter New ID: ");
                int newId = scanner.nextInt();
                emp.setId(newId);
                break;

            case 3:
                System.out.print("Enter New Department: ");
                scanner.nextLine();  // Consume newline
                String newDepartment = scanner.nextLine();
                emp.setDepartment(newDepartment);
                break;

            case 4:
                System.out.print("Enter New Salary: ");
                double newSalary = scanner.nextDouble();
                emp.setSalary(newSalary);
                break;

            default:
                System.out.println("Invalid option. No changes made.");
                return;
        }
        saveEmployeesToFile();
        System.out.println("Employee updated successfully.");
    }
}

// Custom exception for employee not found
class EmployeeNotFoundException extends Exception {
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}

// Main class
public class MISApplication {
    public static void main(String[] args) {
        ManagementSystem system = new ManagementSystem();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nManagement Information System");
            System.out.println("1. Add Employee");
            System.out.println("2. Display All Employees");
            System.out.println("3. Find Employee by ID");
            System.out.println("4. Delete Employee");
            System.out.println("5. Update Employee");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter Employee Name: ");
                    scanner.nextLine();  // Consume newline
                    String name = scanner.nextLine();
                    System.out.print("Enter Employee ID: ");
                    int id = scanner.nextInt();
                    System.out.print("Enter Department: ");
                    scanner.nextLine();
                    String department = scanner.nextLine();
                    System.out.print("Enter Salary: ");
                    double salary = scanner.nextDouble();
                    system.addEmployee(new Employee(name, id, department, salary));
                    break;

                case 2:
                    System.out.println("\n*****************-Empoyeer Management Information System-*******************\n");
                    System.out.println("----------------------------------------------------------------");
                    System.out.println("ID\tEMP-NAME\tDEPARTMENT\tSALARY");
                    System.out.println("----------------------------------------------------------------");
                    system.displayEmployees();
                    System.out.println("\n****************************************************************************");
                    break;

                case 3:
                    System.out.print("Enter Employee ID to Search: ");
                    int searchId = scanner.nextInt();
                    try {
                        Employee emp = system.findEmployeeById(searchId);
                        System.out.println("\n*****************-Empoyeer Management Information System-*******************\n");
                        System.out.println("----------------------------------------------------------------");
                        System.out.println("ID\tEMP-NAME\tDEPARTMENT\tSALARY");
                        System.out.println("----------------------------------------------------------------");
                        System.out.println(emp);
                    System.out.println("\n****************************************************************************");
                    } catch (EmployeeNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 4:
                    System.out.print("Enter Employee ID to Delete: ");
                    int deleteId = scanner.nextInt();
                    try {
                        system.deleteEmployee(deleteId);
                    } catch (EmployeeNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 5:
                    System.out.print("Enter Employee ID to Update: ");
                    int updateId = scanner.nextInt();
                    try {
                        system.updateEmployee(updateId, scanner);
                    } catch (EmployeeNotFoundException e) {
                        System.out.println(e.getMessage());
                    }
                    break;

                case 6:
                    System.out.println("Exiting the system. Goodbye!");
                    scanner.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid option. Please choose again.");
            }
        }
    }
}
