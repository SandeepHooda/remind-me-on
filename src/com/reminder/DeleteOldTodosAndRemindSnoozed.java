package com.reminder;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cxf.common.util.CollectionUtils;

import com.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reminder.vo.ReminderVO;
import com.reminder.vo.ToDO;

import mangodb.MangoDB;

/**
 * Servlet implementation class DeleteOldTodos
 */
@WebServlet("/DeleteOldTodosAndRemindSnoozed")
public class DeleteOldTodosAndRemindSnoozed extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteOldTodosAndRemindSnoozed() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    private void deleteOldToDos() {
    	System.out.println(" deleting old reminders");
		String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "to-dos", null,null, false, null,null)+"]";
		 Gson  json = new Gson();
		 List<ToDO> toDos  = json.fromJson(data, new TypeToken<List<ToDO>>() {}.getType());
		 for (ToDO todo: toDos) {
			 if (todo.getDateCompleted() > 0 && ( (new Date().getTime() -todo.getDateCompleted())) > Constants.aDay*7) {
				 System.out.println(" deleting this todo : "+todo.getTaskDesc());
				 MangoDB.deleteDocument("remind-me-on", "to-dos", todo.get_id(), null);
			 }
		 }
		
    }
    private void sendReminderToSnoozedOnes() {
    	
        	System.out.println(" Sending emails to Snoozed reminders");
    		String data ="["+ MangoDB.getDocumentWithQuery("remind-me-on", "reminders-snooz", null,null, false, null,null)+"]";
    		
    		Gson  json = new Gson();
			List<ReminderVO> reminders  = json.fromJson(data, new TypeToken<List<ReminderVO>>() {}.getType());
			Map<String, String> soozedRemindersMap = new HashMap<String, String>();
			for(ReminderVO reminder:reminders ) {
				String reminderText = soozedRemindersMap.get(reminder.getEmail());
				if (reminderText == null) {
					reminderText = "";
				}
				reminderText += "<br/><br/> &nbsp;&nbsp;&nbsp; &#8226; &nbsp;<b>" +reminder.getReminderSubject()+" "+reminder.getReminderText()+" </b>";
				soozedRemindersMap.put(reminder.getEmail(), reminderText);
			}
			Set<String> emailIds = soozedRemindersMap.keySet();
			if (!CollectionUtils.isEmpty(emailIds)){
				for (String email: emailIds) {
					Constants.sendEmail(email,"Pending task", soozedRemindersMap.get(email)+" <br/><br/> You will get one consolidated email daily to remind you about your pending tasks that you might not have completed. "
							+ "If you have completed them please delete them from \"Snoozed\" section of the \"Reminder app\".  <br/></br/>Regards");
				}
			}
			
			
    }
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		deleteOldToDos();
		sendReminderToSnoozedOnes();
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
