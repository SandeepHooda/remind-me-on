package com.reminder;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reminder.vo.ToDO;

import mangodb.MangoDB;

/**
 * Servlet implementation class DeleteOldTodos
 */
@WebServlet("/DeleteOldTodos")
public class DeleteOldTodos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteOldTodos() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println(" deleting old reminders");
		String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "to-dos", null,null, false, null,null)+"]";
		 Gson  json = new Gson();
		 List<ToDO> toDos  = json.fromJson(data, new TypeToken<List<ToDO>>() {}.getType());
		 for (ToDO todo: toDos) {
			 if (todo.getDateCompleted() > 0 && ( (new Date().getTime() -todo.getDateCompleted())) > 1000 * 60*60*24*7) {
				 System.out.println(" deleting this todo : "+todo.getTaskDesc());
				 MangoDB.deleteDocument("remind-me-on", "to-dos", todo.get_id(), null);
			 }
		 }
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
