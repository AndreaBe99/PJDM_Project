package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.AccountDAO;
import db.AccountDAOJDBCImpl;
import entity.Account;

public class AccountServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	private AccountDAO dao;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AccountServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void destroy() {
		dao.closeConnection();
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus(405);
		response.getWriter().append("L'implementazione del metodo PUT e' implementato come esercizio.");
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (request.getParameter("password") == null || request.getParameter("username") == null) {
			response.setStatus(500);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append("Occorre specificare un ssn e nome da inserire");
			return;
		}

		String password = request.getParameter("password");
		String username = request.getParameter("username");

		Account oldAccount = dao.loadAccount(username, password);
		if (oldAccount==null || !oldAccount.getUsername().equalsIgnoreCase(username)) {
			response.setStatus(400);
			response.setCharacterEncoding("UTF-8");
			response.getWriter().append("Purtroppo il seguente musicista non è nel DB");
			return;
		}

		Account newAccount = new Account(username, password);
		boolean isOk = dao.deleteAccount(newAccount);

		if (isOk) {
			response.setStatus(200);
			response.getWriter().append("Tutto OK");
		} else {
			response.setStatus(500);
			response.getWriter().append("Problemi :-(");
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameter("username") == null) {
			response.setStatus(400);
			response.getWriter().append("Occorre specificare un utente");
			return;
		}

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		PrintWriter out = response.getWriter();
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		if (username.equals("tutti")) {
			ArrayList<Account> allAccounts = dao.loadTuttiAccount();
			JSONArray allAccountsJson = new JSONArray(allAccounts);
			out.print(allAccountsJson.toString());
			out.flush();
		} else {
			Account account = dao.loadAccount(username, password);
			if (account == null) {
				response.setStatus(404);
				response.getWriter().append("Non esiste un account con Username " + username + " e con la password impostata.");
				return;
			}
			JSONObject accountJson = new JSONObject(account);
			out.print(accountJson.toString());
			out.flush();
		}
	}

	/**
	 * Il metodo POST permette di inserire un nuovo musicista. Prevede due
	 * modalità:
	 * 
	 * - invare un nuovo musicista specificando name e ssn
	 * 
	 * - invare un nuovo musicista specificandolo in una stringa JSON
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (request.getParameter("json") != null) {
			handleJson(request, response);
		} else {
			handleNameSSN(request, response);
		}

	}

	private void handleJson(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String inputJson = request.getParameter("json");
		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(inputJson);
		} catch (JSONException e) {
			response.setStatus(500);
			response.getWriter().append("Input Malformato");
			return;
		}

		String username = null, password = null;
		
		try {
			username = jsonObject.getString("username");
		} catch (JSONException e) {
			response.setStatus(500);
			response.getWriter().append("Nel campo JSON ho problemi con il campo SSN");
		}

		try {
			password = jsonObject.getString("password");
		} catch (JSONException e) {
			response.setStatus(500);
			response.getWriter().append("Nel campo JSON ho problemi con il campo Name");
		}
		

		Account newAccount = new Account(username, password);
		int res = dao.insertAccount(newAccount);

		if (res > 0) {
			response.setStatus(200);
			response.getWriter().append("Tutto OK");
		} else {
			response.setStatus(500);
			response.getWriter().append("Problemi :-(");
		}

	}

	private void handleNameSSN(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (request.getParameter("username") == null || request.getParameter("password") == null || request.getParameter("email") == null) {
			response.setStatus(500);
			response.getWriter().append("Occorre specificare un username, password e email da inserire");
			return;
		}

		String password = request.getParameter("password");
		String username = request.getParameter("username");
		String email = request.getParameter("email");

		Account newAccount = new Account(username, password,email);
		int res = dao.insertAccount(newAccount);
	

		if (res > 0) {
			response.setStatus(200);
			response.getWriter().append("Tutto OK");
		} else {
			response.setStatus(500);
			response.getWriter().append("Problemi :-(");
		}
	}

	@Override
	public void init() throws ServletException {
		String ip = getInitParameter("ip");
		String port = getInitParameter("port");
		String dbName = getInitParameter("dbName");
		String userName = getInitParameter("userName");
		String password = getInitParameter("password");

		dao = new AccountDAOJDBCImpl(ip, port, dbName, userName, password);
	}


}
