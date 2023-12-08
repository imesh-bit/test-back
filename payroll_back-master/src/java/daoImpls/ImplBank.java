/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package daoImpls;

import dao.BeanBank;
import dao.BeanSystemLog;
import interfaces.InterfaceBank;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import util.JDBC;

/**
 *
 * @author Nimesha
 */
public class ImplBank implements InterfaceBank {

    @Override
    public List<BeanBank> getBankList(Connection connection) throws Exception {
        List<BeanBank> bankList = new ArrayList();
        String query = "SELECT `id`,`bankcode`,`bankname`,`branchcode`,`branchname` FROM `bank`;";

        PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery();

        if (rs.isBeforeFirst()) {
            while (rs.next()) {
                BeanBank b = new BeanBank();
                b.setId(rs.getInt("id"));
                b.setBankcode(rs.getString("bankcode"));
                b.setBankname(rs.getString("bankname"));
                b.setBranchcode(rs.getString("branchcode"));
                b.setBranchname(rs.getString("branchname"));
                bankList.add(b);
            }
        }
        ps.close();
        return bankList;
    }

    @Override
    public String saveBank(BeanBank b) throws SQLException, Exception {
        Connection connection = JDBC.con_payroll();
        connection.setAutoCommit(false);

        try {
            String sql_allowance = "INSERT INTO `bank`(`bankname`,`bankcode`,`branchname`,`branchcode`,`user`,`comcode`) VALUES(?,?,?,?,?,?);";
            PreparedStatement ps_allow = connection.prepareStatement(sql_allowance);
            ps_allow.setString(1, b.getBankname());
            ps_allow.setString(2, b.getBankcode());
            ps_allow.setString(3, b.getBranchname());
            ps_allow.setString(4, b.getBranchcode());
            ps_allow.setString(5, BeanSystemLog.getUser());
            ps_allow.setString(6, BeanSystemLog.getComcode());
            ps_allow.execute();
            ps_allow.close();

            String saveQuery = "INSERT INTO `log`(`event`,`description`,`comcode`,`user`) VALUES (?,?,?,?);";
            PreparedStatement ps = connection.prepareStatement(saveQuery);
            ps.setString(1, "Save");
            ps.setString(2, "Bank " + b.getBankcode() + " Branch " + b.getBranchcode());
            ps.setString(3, BeanSystemLog.getComcode());
            ps.setString(4, BeanSystemLog.getUser());
            ps.execute();
            ps.close();

            connection.commit();
            connection.setAutoCommit(true);

            return "Details sucessfully saved.";
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
            return "Error:" + e.getLocalizedMessage();
        } finally {
            connection.close();
        }

    }

    @Override
    public String updateBank(BeanBank b) throws SQLException, Exception {
        Connection connection = JDBC.con_payroll();
        connection.setAutoCommit(false);

        try {
            String sql_allowance = "UPDATE `bank` SET `bankname` = ?,`bankcode` = ?,`branchname` = ?,`branchcode` = ?,`user` = ?,`comcode` = ?\n"
                    + "WHERE `id` = ?;";
            PreparedStatement ps_allow = connection.prepareStatement(sql_allowance);
            ps_allow.setString(1, b.getBankname());
            ps_allow.setString(2, b.getBankcode());
            ps_allow.setString(3, b.getBranchname());
            ps_allow.setString(4, b.getBranchcode());
            ps_allow.setString(5, BeanSystemLog.getUser());
            ps_allow.setString(6, BeanSystemLog.getComcode());
            ps_allow.setInt(7, b.getId());
            ps_allow.execute();
            ps_allow.close();

            String saveQuery = "INSERT INTO `log`(`event`,`description`,`comcode`,`user`) VALUES (?,?,?,?);";
            PreparedStatement ps = connection.prepareStatement(saveQuery);
            ps.setString(1, "Update");
            ps.setString(2, "Bank " + b.getBankcode() + " Branch " + b.getBranchcode());
            ps.setString(3, BeanSystemLog.getComcode());
            ps.setString(4, BeanSystemLog.getUser());
            ps.execute();
            ps.close();

            connection.commit();
            connection.setAutoCommit(true);

            return "Details sucessfully updated.";
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
            return "Error:" + e.getLocalizedMessage();
        } finally {
            connection.close();
        }

    }

    @Override
    public String deleteBank(int bankId, String bank) throws SQLException, Exception {
        Connection connection = JDBC.con_payroll();
        connection.setAutoCommit(false);
        try {
            String query1 = "SELECT COUNT(*) FROM `employee_bank` WHERE `bankid1`=?;";
            PreparedStatement ps1 = connection.prepareStatement(query1);
            ps1.setInt(1, bankId);
            ResultSet rs1 = ps1.executeQuery();

            if (rs1.isBeforeFirst()) {
                rs1.first();
                if (rs1.getInt(1) > 0) {
                    return "Error:This bank already used to update employee details";
                }
            }
            ps1.close();

            String query2 = "SELECT COUNT(*) FROM `employee_bank` WHERE `bankid2`=?;";
            PreparedStatement ps2 = connection.prepareStatement(query2);
            ps2.setInt(1, bankId);
            ResultSet rs2 = ps2.executeQuery();

            if (rs2.isBeforeFirst()) {
                rs2.first();
                if (rs2.getInt(1) > 0) {
                    return "Error:This bank already used to update employee details";
                }
            }
            ps2.close();

            String sql_attendance = "DELETE FROM `bank` WHERE `id`=?;";
            PreparedStatement ps_attendance = connection.prepareStatement(sql_attendance);
            ps_attendance.setInt(1,bankId);
            ps_attendance.executeUpdate();
            ps_attendance.close();

            String saveQuery = "INSERT INTO `log`(`event`,`description`,`comcode`,`user`) VALUES (?,?,?,?);";
            PreparedStatement ps_log = connection.prepareStatement(saveQuery);
            ps_log.setString(1, "Delete Bank");
            ps_log.setString(2, "Bank: " + bank+ " Id:" + bankId);
            ps_log.setString(3, BeanSystemLog.getComcode());
            ps_log.setString(4, BeanSystemLog.getUser());
            ps_log.execute();
            ps_log.close();

            connection.commit();
            connection.setAutoCommit(true);

            return "Record sucessfully deleted.";
        } catch (Exception e) {
            e.printStackTrace();
            connection.rollback();
            return "Error:" + e.getLocalizedMessage();
        } finally {
            connection.close();
        }
    }

}
