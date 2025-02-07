package com.example.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloWorldServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//レスポンスをhtmlに指定
		response.setContentType("text/html;charset=UTF=8");

		//出力x
		PrintWriter out = response.getWriter();
		Point point = new Point();
		//point.Test(10,10);
		//point.Test(10, 10);

		//htmlを生成
		out.println(point.Test(10, 20));
		out.println("<!DOCTYPE html>");
		out.println("<html lang=\"ja\">");
		out.println("<head>");
		out.println("<meta charset=\"UTF-8\">");
		out.println("<title>Hello, World!</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("<h1>Hello, World!</h1>");
		out.println("</body>");
		out.println("</html>");

	}
}
