/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import com.google.gson.JsonObject;
import dao.BeanEmployee;
import dao.BeanEmployeeBank;
import dao.BeanSystemLog;
import daoImpls.ImplEmployeeProfile;
import java.sql.Connection;
import interfaces.InterfaceEmployeeProfile;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Nimesha
 */
public class ModelEmployeeProfile {

    Connection connection;
    InterfaceEmployeeProfile employee;
    DateFormat df = new SimpleDateFormat("dd-MM-yyyy");

    public ModelEmployeeProfile(Connection connection) {
        this.connection = connection;
        employee = new ImplEmployeeProfile();
    }

    public JSONArray getSelectedEmpDetails(int empId, String comCode) throws Exception {

        BeanEmployee bean = employee.getSelectedEmployeeProfile(connection, empId, comCode);

        JSONArray jsa = new JSONArray();
        LinkedHashMap mp = new LinkedHashMap();
        mp.put("empid", bean.getEmpid());
        mp.put("date", df.format(bean.getDate()));
        mp.put("empcode", bean.getEmpcode());
        mp.put("titel", bean.getTitel());
        mp.put("initials", bean.getInitials());
        mp.put("surname", bean.getSurname());
        mp.put("empfullname", bean.getEmpfullname());
        mp.put("empinitialname", bean.getEmpinitialname());
        mp.put("nic", bean.getNic());
        mp.put("datebirth", df.format(bean.getDatebirth()));
        mp.put("gender", bean.getGender());
        mp.put("empaddress", bean.getEmpaddress());
        mp.put("tele", bean.getTele());
        mp.put("email", bean.getEmail());
        mp.put("status", bean.getStatus());
        mp.put("dateappointment", df.format(bean.getDateappointment()));
        mp.put("probationenddate", df.format(bean.getProbationenddate()));
        mp.put("groupid", bean.getGroupid());
        mp.put("categoryid", bean.getCategoryid());
        mp.put("typofemp", bean.getTypofemp());
        mp.put("division", bean.getDivision());
        mp.put("designation", bean.getDesignation());
        mp.put("salarytyp", bean.getSalarytyp());
        mp.put("basicsalary", bean.getBasicsalary());
        mp.put("budgetallowance1", bean.getBudgetallowance());
        mp.put("budgetallowance2", bean.getBra2());
        mp.put("allow1", bean.getAllow1());
        mp.put("allow2", bean.getAllow2());
        mp.put("allow3", bean.getAllow3());
        mp.put("allow4", bean.getAllow4());
        mp.put("allow5", bean.getAllow5());
        mp.put("deduct1", bean.getDeduct1());
        mp.put("deduct2", bean.getDeduct2());
        mp.put("deduct3", bean.getDeduct3());
        mp.put("deduct4", bean.getDeduct4());
        mp.put("deduct5", bean.getDeduct5());
        mp.put("epfno", bean.getEpfno());
        mp.put("remarks", bean.getRemarks());
        mp.put("active", bean.getActive());
        mp.put("comcode", bean.getComcode());
        mp.put("user", bean.getUser());
        mp.put("imgfilename", bean.getImgfilename());

        BeanEmployeeBank empBank = bean.getBankDetails();
        if (empBank != null) {
            mp.put("bankid1", empBank.getBankid1());
            mp.put("accno1", empBank.getAccno1());
            mp.put("amount1", empBank.getAmount1());
            mp.put("bankid2", empBank.getBankid2());
            mp.put("accno2", empBank.getAccno2());
            mp.put("amount2", empBank.getAmount2());
        }

        jsa.add(mp);
        return jsa;
    }

    public JSONArray getEmployeeList(String comCode) throws Exception {
        JSONArray jsa = new JSONArray();
        try {

            List<BeanEmployee> itemList = employee.getEmployeeList(connection, comCode);
            Iterator<BeanEmployee> Listiterator = itemList.iterator();

            while (Listiterator.hasNext()) {
                BeanEmployee bean = Listiterator.next();
                LinkedHashMap mp = new LinkedHashMap();
                mp.put("empid", bean.getEmpid());
                mp.put("empinitialname", bean.getEmpinitialname());
                mp.put("empcode", bean.getEmpcode());
                mp.put("division", bean.getDivision());
                mp.put("designation", bean.getDesignation());
                mp.put("dateappointment", df.format(bean.getDateappointment()));
                mp.put("datebirth", df.format(bean.getDatebirth()));
                mp.put("gender", bean.getGender());
                mp.put("active", bean.getActive());
                mp.put("salary", bean.getSalary());
                jsa.add(mp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsa;
    }

    public JSONObject saveEmployeeProfile(JsonObject requestJson, Connection connection) throws Exception {

        JSONObject jsa = new JSONObject();

        BeanEmployee details = new BeanEmployee();
        BeanEmployeeBank empBank = new BeanEmployeeBank();
        DateFormat Dateformat_YYMMDD = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        JsonObject employeeDetails = requestJson.getAsJsonObject();

        details.setDate(new Date());
        details.setEpfAuto(employeeDetails.get("epfauto").getAsBoolean());
        details.setEmpid(employeeDetails.get("empid").getAsInt());
        details.setEmpcode(employeeDetails.get("empcode").getAsInt());
        details.setTitel(employeeDetails.get("titel").getAsString());
        details.setInitials(employeeDetails.get("initials").getAsString());
        details.setSurname(employeeDetails.get("surname").getAsString());
        details.setEmpinitialname(employeeDetails.get("empinitialname").getAsString());
        details.setEmpfullname(employeeDetails.get("empfullname").getAsString());
        details.setNic(employeeDetails.get("nic").getAsString());
        details.setDatebirth(Dateformat_YYMMDD.parse(employeeDetails.get("datebirth").getAsString()));
        details.setGender(employeeDetails.get("gender").getAsString().equals("Male") ? 1 : 0);
        details.setEmpaddress(employeeDetails.get("empaddress").getAsString());
        if (employeeDetails.get("tele") != null) {
            details.setTele(employeeDetails.get("tele").getAsString());
        } else {
            details.setTele("");
        }
        if (employeeDetails.get("email") != null) {
            details.setEmail(employeeDetails.get("email").getAsString());
        } else {
            details.setEmail("");
        }
        details.setStatus(employeeDetails.get("status").getAsString());
        details.setDateappointment(Dateformat_YYMMDD.parse(employeeDetails.get("dateappointment").getAsString()));
        if (employeeDetails.get("probationenddate") != null) {
            details.setProbationenddate(Dateformat_YYMMDD.parse(employeeDetails.get("probationenddate").getAsString()));
        } else {
            details.setProbationenddate(Dateformat_YYMMDD.parse("1900-01-01"));
        }
        details.setGroupid(employeeDetails.get("groupid").getAsInt());
        details.setCategoryid(employeeDetails.get("categoryid").getAsInt());
        details.setTypofemp(employeeDetails.get("typofemp").getAsString());
        details.setDivision(employeeDetails.get("division").getAsString());
        details.setDesignation(employeeDetails.get("designation").getAsString());
        details.setSalarytyp("Normal");
        if (employeeDetails.get("basicsalary") != null) {
            details.setBasicsalary(employeeDetails.get("basicsalary").getAsBigDecimal());
        } else {
            details.setBasicsalary(BigDecimal.ZERO);
        }
        if (employeeDetails.get("budgetallowance1") != null) {
            details.setBudgetallowance(employeeDetails.get("budgetallowance1").getAsBigDecimal());
        } else {
            details.setBudgetallowance(BigDecimal.ZERO);
        }
        if (employeeDetails.get("budgetallowance2") != null) {
            details.setBra2(employeeDetails.get("budgetallowance2").getAsBigDecimal());
        } else {
            details.setBra2(BigDecimal.ZERO);
        }
        if (employeeDetails.get("allow1") != null) {
            details.setAllow1(employeeDetails.get("allow1").getAsBigDecimal());
        } else {
            details.setAllow1(BigDecimal.ZERO);
        }
        if (employeeDetails.get("allow2") != null) {
            details.setAllow2(employeeDetails.get("allow2").getAsBigDecimal());
        } else {
            details.setAllow2(BigDecimal.ZERO);
        }
        if (employeeDetails.get("allow3") != null) {
            details.setAllow3(employeeDetails.get("allow3").getAsBigDecimal());
        } else {
            details.setAllow3(BigDecimal.ZERO);
        }
        if (employeeDetails.get("allow4") != null) {
            details.setAllow4(employeeDetails.get("allow4").getAsBigDecimal());
        } else {
            details.setAllow4(BigDecimal.ZERO);
        }
        if (employeeDetails.get("allow5") != null) {
            details.setAllow5(employeeDetails.get("allow5").getAsBigDecimal());
        } else {
            details.setAllow5(BigDecimal.ZERO);
        }
        if (employeeDetails.get("deduct1") != null) {
            details.setDeduct1(employeeDetails.get("deduct1").getAsBigDecimal());
        } else {
            details.setDeduct1(BigDecimal.ZERO);
        }
        if (employeeDetails.get("deduct2") != null) {
            details.setDeduct2(employeeDetails.get("deduct2").getAsBigDecimal());
        } else {
            details.setDeduct2(BigDecimal.ZERO);
        }
        if (employeeDetails.get("deduct3") != null) {
            details.setDeduct3(employeeDetails.get("deduct3").getAsBigDecimal());
        } else {
            details.setDeduct3(BigDecimal.ZERO);
        }
        if (employeeDetails.get("deduct4") != null) {
            details.setDeduct4(employeeDetails.get("deduct4").getAsBigDecimal());
        } else {
            details.setDeduct4(BigDecimal.ZERO);
        }
        if (employeeDetails.get("deduct5") != null) {
            details.setDeduct5(employeeDetails.get("deduct5").getAsBigDecimal());
        } else {
            details.setDeduct5(BigDecimal.ZERO);
        }
        /*if (employeeDetails.get("epfno") == null || employeeDetails.get("epfno").getAsString().equals("") || employeeDetails.get("epfno").getAsString().equals("0") || employeeDetails.get("epfno").getAsString().equals("<<Auto>>")) {
            details.setEpfno("");
            details.setEmpcode(0);
        } else {
            details.setEpfno(employeeDetails.get("epfno").getAsString());
            details.setEmpcode(employeeDetails.get("empcode").getAsInt());
        }*/
        if (employeeDetails.get("remarks") != null) {
            details.setRemarks(employeeDetails.get("remarks").getAsString());
        } else {
            details.setRemarks("");
        }
        if (employeeDetails.get("imgfilename") != null) {
            details.setImgfilename(employeeDetails.get("imgfilename").getAsString());
        } else {
            details.setImgfilename("");
        }

        details.setActive(1);
        details.setUser(BeanSystemLog.getUser());
        details.setComcode(BeanSystemLog.getComcode());

        empBank.setEmpid(employeeDetails.get("empid").getAsInt());
        if (employeeDetails.get("bankid1") != null) {
            empBank.setBankid1(employeeDetails.get("bankid1").getAsInt());
        } else {
            empBank.setBankid1(0);
        }
        if (employeeDetails.get("accno1") != null) {
            empBank.setAccno1(employeeDetails.get("accno1").getAsString());
        } else {
            empBank.setAccno1("-");
        }
        if (employeeDetails.get("amount1") != null) {
            empBank.setAmount1(employeeDetails.get("amount1").getAsBigDecimal());
        } else {
            empBank.setAmount1(BigDecimal.ZERO);
        }
        System.out.print("bankid2 " + employeeDetails.get("bankid2"));
        if (employeeDetails.get("bankid2") != null) {
            empBank.setBankid2(employeeDetails.get("bankid2").getAsInt());
        } else {
            empBank.setBankid2(0);
        }

        if (employeeDetails.get("accno2") != null) {
            empBank.setAccno2(employeeDetails.get("accno2").getAsString());
        } else {
            empBank.setAccno2("-");
        }
        if (employeeDetails.get("amount2") != null) {
            empBank.setAmount2(employeeDetails.get("amount2").getAsBigDecimal());
        } else {
            empBank.setAmount2(BigDecimal.ZERO);
        }
        empBank.setUser(BeanSystemLog.getUser());
        empBank.setComcode(BeanSystemLog.getComcode());
        details.setBankDetails(empBank);

        String status = null;
        if (employeeDetails.get("empid").getAsInt() != 0) {
            status = employee.updateEmployeeProfile(details);
        } else {
            status = employee.saveEmployeeProfile(details);
        }

        if (status.startsWith("Error:")) {
            jsa.put("message", status);
            jsa.put("type", "error");
        } else {
            jsa.put("message", status);
            jsa.put("type", "sucess");
        }

        return jsa;
    }

}
