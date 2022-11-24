package com.example.demo.controllers;

import com.example.demo.models.Employee;
import com.example.demo.repo.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/employee")
    public String blogMain(Model model)
    {
        Iterable<Employee> employees = employeeRepository.findAll();
        model.addAttribute("employees", employees);
        return "employee-main";
    }



    @PostMapping("/employee/add")
    public String blogPostAdd(@RequestParam(defaultValue = "")  String surname,
                              @RequestParam(defaultValue = "0") float height,
                              @RequestParam(defaultValue = "false")  boolean lovecookie,
                              @RequestParam(defaultValue = "10") int favnumber,
                              @RequestParam(defaultValue = "0")  double weight,
                              Model model)
    {
        Employee employee = new Employee(surname, height, lovecookie,favnumber,weight);
        employeeRepository.save(employee);
        return "redirect:/employee";
    }

    @PostMapping("/employee/filter")
    public String blogResult(@RequestParam(defaultValue = "") String surname, Model model)
    {
        List<Employee> result = employeeRepository.findBySurnameEquals(surname);
        model.addAttribute("result", result);
        return "employee-main";
    }

    @GetMapping("/employee/{id}/edit")
    public  String EmployeeDetails(@PathVariable(value = "id") long id, Model model)
    {
        Optional<Employee> post = employeeRepository.findById(id);
        ArrayList<Employee> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("employee",res);
        if(!employeeRepository.existsById(id))
        {
            return  "redirect:/employee";
        }
        return "employee-main";
    }

    @PostMapping ("/employee/{id}/edit")
    public  String EmployeeUpdate(@PathVariable(value = "id") long id,
                                   @RequestParam(defaultValue = "")  String surname,
                                   @RequestParam(defaultValue = "0") float height,
                                   @RequestParam(defaultValue = "false")  boolean lovecookie,
                                   @RequestParam(defaultValue = "10") int favnumber,
                                   @RequestParam(defaultValue = "0")  double weight,
                                   Model model)
    {
        Employee employee = employeeRepository.findById(id).orElseThrow();
        employee.setLastName(surname);
        employee.setHeight(height);
        employee.setLovecookie(lovecookie);
        employee.setFavnumber(favnumber);
        employee.setWeight(weight);
        employeeRepository.save(employee);
        return "redirect:/employee";
    }


    @GetMapping("/employee/{id}/remove")
    public  String EmployeeDelDetails(@PathVariable(value = "id") long id, Model model)
    {
        Optional<Employee> post = employeeRepository.findById(id);
        ArrayList<Employee> res = new ArrayList<>();
        post.ifPresent(res::add);
        model.addAttribute("employee",res);
        if(!employeeRepository.existsById(id))
        {
            return  "redirect:/employee";
        }
        return EmployeeDelete(id,model);
    }
    @PostMapping("/employee/{id}/remove")
    public String EmployeeDelete(@PathVariable(value = "id") long id, Model model){
        Employee employee = employeeRepository.findById(id).orElseThrow();
        employeeRepository.delete(employee);
        return "redirect:/employee";
    }
}
