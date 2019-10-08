package com.reminder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reminder.vo.ReminderVO;

import mangodb.MangoDB;

/**
 * Servlet implementation class GetSnoozedReminders
 */
@WebServlet("/GetSnoozedReminders")
public class GetSnoozedReminders extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetSnoozedReminders() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String queryEmail = request.getParameter("queryEmail");
		if (null == queryEmail) {
			response.getWriter().append("Invalid request ");
			return;
		}
		System.out.println(" Sending emails to Snoozed reminders");
		String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "reminders-snooz", null,null, false, null,null)+"]";
		Gson  json = new Gson();
		String remindersForUser = "";
		List<ReminderVO> reminders  = json.fromJson(data, new TypeToken<List<ReminderVO>>() {}.getType());
		Map<String, String> soozedRemindersMap = new HashMap<String, String>();
		for(ReminderVO reminder:reminders ) {
			if (queryEmail.equalsIgnoreCase(reminder.getEmail())) {
				String reminderText = soozedRemindersMap.get(reminder.getEmail());
				if (reminderText == null) {
					reminderText = "";
				}
				reminderText += ", " +reminder.getReminderSubject()+" "+reminder.getReminderText()+" , ";
				remindersForUser = reminderText;
				soozedRemindersMap.put(reminder.getEmail(), reminderText);
			}
			
		}
		
		response.getWriter().append(remindersForUser);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		doGet(request, response);
	}

}
