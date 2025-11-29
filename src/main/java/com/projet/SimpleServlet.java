package com.projet;

import com.utils.Calc;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/service")
public class SimpleServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get parameters FIRST (this triggers parsing of the request body)
        String num1Str = request.getParameter("num1");
        String num2Str = request.getParameter("num2");

        // THEN get the writer
        PrintWriter out = response.getWriter();

        if(num1Str == null || num2Str == null){
            out.println("Missing parameters");
            return;
        }

        int num1 = Integer.parseInt(num1Str);
        int num2 = Integer.parseInt(num2Str);
        int res = num1 + num2;
        out.println("I am Post : " + res);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String num1Str = request.getParameter("num1");
        String num2Str = request.getParameter("num2");

        PrintWriter out = response.getWriter();

        if(num1Str == null || num2Str == null){
            out.println("Missing parameters");
            return;
        }

        int num1 = Integer.parseInt(num1Str);
        int num2 = Integer.parseInt(num2Str);
        int res = num1 + num2;
        out.println("I am Get : " + res);
    }
}