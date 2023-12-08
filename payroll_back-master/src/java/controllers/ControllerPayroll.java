/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllers;

import dao.BeanEmployee;
import dao.BeanSystemLog;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.ModelPayroll;
import org.json.simple.JSONObject;
import threads.payroll.AutoSaveThread;
import util.JDBC;
import util.JWTTokenGenerator;

/**
 *
 * @author Nimesha
 */
@WebServlet(name = "ControllerPayroll", urlPatterns = {"/ControllerPayroll"})
public class ControllerPayroll extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String requestTokenHeader = request.getHeader("Authorization");
        System.out.println("requestTokenHeader " + requestTokenHeader);
        JWTTokenGenerator jwt = new JWTTokenGenerator();

        try ( PrintWriter out = response.getWriter();  Connection connection = JDBC.con_payroll();) {
            if (jwt.validateToken(requestTokenHeader)) {

                ModelPayroll model = new ModelPayroll(connection);

                if (request.getParameter("getAllPayRollData") != null) {
                    System.out.println("getAllPayRollData");
                    String SalaryMonth = request.getParameter("monthCode");
                    String SalaryType = "Normal";
                    String Company = BeanSystemLog.getComcode();

                    response.setContentType("application/json");
                    response.getWriter().print(model.getPayrollData(connection, SalaryMonth, SalaryType, Company));

                }
                if (request.getParameter("getExcelData") != null) {
                    System.out.println("getExcelData");
                    String SalaryMonth = request.getParameter("monthCode");
                    String SalaryType = "Normal";
                    String Company = BeanSystemLog.getComcode();

                    response.setContentType("application/json");
                    response.getWriter().print(model.getExcelData(connection, SalaryMonth, SalaryType, Company));

                }
                if (request.getParameter("runWizard") != null) {
                    System.out.println("runWizard");
                    JSONObject jsa = new JSONObject();

                    String SalaryMonth = request.getParameter("monthCode");
                    String SalaryType = "Normal";
                    String Company = BeanSystemLog.getComcode();

                    if (model.isFinalized(connection, SalaryMonth, Company)) {
                        jsa.put("type", "error");
                        jsa.put("message", "Error:Payroll already finalized");
                    } else if (model.isAllStopped(connection, SalaryMonth, Company)) {
                        List<BeanEmployee> beansList = model.getSelectedEmployeeListSalary(connection, Company, SalaryType, SalaryMonth);
                        System.out.println("beansList size " + beansList.size());
                        if (beansList.isEmpty()) {
                            jsa.put("type", "error");
                            jsa.put("message", "Error:There is no employees for run payroll wizard");
                        } else {
                            System.out.println("call AutoSaveThread ");
                            AutoSaveThread AutoSaveThread = model.CallAutoSaveThread(beansList, model);
                            AutoSaveThread.join();
                            // double processValue = AutoSaveThread.GetProgressValue();
                            String message = AutoSaveThread.getSaveString();

                            System.out.println("AutoSaveThread Message " + message);

                            if (message.startsWith("Error:")) {
                                jsa.put("type", "error");
                                jsa.put("message", message);
                            } else {
                                jsa.put("type", "sucess");
                                jsa.put("message", message);
                            }

                        }
                    } else {
                        jsa.put("type", "error");
                        jsa.put("message", "Payroll wizard caanot be run,Check the payroll month");
                    }

                    response.setContentType("application/json");
                    out.print(jsa);

                }
                if (request.getParameter("getEmpPayRollData") != null) {
                    String SalaryMonth = request.getParameter("monthCode");
                    String Company = BeanSystemLog.getComcode();
                    int EmpId = Integer.parseInt(request.getParameter("EmpId"));

                    response.setContentType("application/json");
                    response.getWriter().print(model.getEmployeePayrollData(connection, SalaryMonth, EmpId, Company));

                }
                if (request.getParameter("updateEmpPayroll") != null) {
                    JSONObject jsa = new JSONObject();

                    String SalaryMonth = request.getParameter("monthCode");
                    String Company = BeanSystemLog.getComcode();
                    int empID = Integer.parseInt(request.getParameter("empID"));
                    BigDecimal basicSal = new BigDecimal(request.getParameter("basicSal"));
                    BigDecimal salArreas = new BigDecimal(request.getParameter("salArreas"));
                    BigDecimal payCut = new BigDecimal(request.getParameter("payCut"));
                    BigDecimal NoPay = new BigDecimal(request.getParameter("NoPay"));
                    BigDecimal nopaydates = new BigDecimal(request.getParameter("nopaydates"));
                    BigDecimal otpay = new BigDecimal(request.getParameter("otpay"));
                    String remark = request.getParameter("remark");

                    System.out.println("updatePayroll " + empID);
                    if (model.isFinalized(connection, SalaryMonth, Company)) {
                        jsa.put("type", "error");
                        jsa.put("message", "Error:Payroll already finalized");
                    } else {
                        String message = model.updatePayrollData(connection, SalaryMonth, empID, basicSal, salArreas, payCut, NoPay, nopaydates, otpay, remark);
                        if (message.startsWith("Error:")) {
                            jsa.put("type", "error");
                            jsa.put("message", message);
                        } else {
                            jsa.put("type", "sucess");
                            jsa.put("message", message);
                        }
                    }

                    response.setContentType("application/json");
                    out.print(jsa);
                }

                if (request.getParameter("finalizePayroll") != null) {
                    System.out.println("finalizePayroll");
                    JSONObject jsa = new JSONObject();

                    String SalaryMonth = request.getParameter("monthCode");
                    String SalaryType = "Normal";
                    String Company = BeanSystemLog.getComcode();
                    String message = model.finalizePayRoll(connection, SalaryMonth, SalaryType, Company);

                    if (message.startsWith("Error:")) {
                        jsa.put("type", "error");
                        jsa.put("message", message);
                    } else {
                        jsa.put("type", "sucess");
                        jsa.put("message", message);
                    }

                    response.setContentType("application/json");
                    out.print(jsa);
                }

                if (request.getParameter("removeEntries") != null) {
                    System.out.println("removeEntries");
                    JSONObject jsa = new JSONObject();

                    String SalaryMonth = request.getParameter("monthCode");
                    String SalaryType = "Normal";
                    String Company = BeanSystemLog.getComcode();

                    if (model.isFinalized(connection, SalaryMonth, Company)) {
                        jsa.put("type", "error");
                        jsa.put("message", "Error:Payroll already finalized");
                    } else {
                        String message = model.deleteAllPayrollEntries(connection, Company, SalaryMonth, SalaryType);
                        if (message.startsWith("Error:")) {
                            jsa.put("type", "error");
                            jsa.put("message", message);
                        } else {
                            jsa.put("type", "sucess");
                            jsa.put("message", message);
                        }
                    }

                    response.setContentType("application/json");
                    out.print(jsa);
                }
            } else {
                JSONObject jsa = new JSONObject();
                jsa.put("message", "Invalid token");
                jsa.put("type", "error");
                response.setContentType("application/json");
                out.print(jsa);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
