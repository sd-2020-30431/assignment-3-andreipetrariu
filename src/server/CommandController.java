package server;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import client.Main;
import model.BoughtItem;
import model.ShopItem;
import org.springframework.context.ApplicationContextAware;

@RestController
@RequestMapping("/command")
public class CommandController{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	private WLMediator mediator;
	
	@PutMapping("/user")
	public void putUser(HttpServletRequest request, HttpServletResponse response) throws IOException, 
	ServletException {
		ObjectMapper mapper = new ObjectMapper();
		String username = request.getParameter("user");
		String password = request.getParameter("pass");
		String type = request.getParameter("type");
		int result;
		UpdateUserCommand command;
		if(Integer.parseInt(type)==1) {
			command = new UpdateUserCommand(username,password,1);
		}
		else 
			command = new UpdateUserCommand(username,password,0);
		UpdateUserHandler userHandler = (UpdateUserHandler) mediator.<UpdateUserCommand,LoginResponse> handle(command);
		result = userHandler.handle(command).getId();
		response.getOutputStream().println(mapper.writeValueAsString(result));
	}
	
	@PutMapping("/items")
	public void putItem(HttpServletRequest request, HttpServletResponse response) {
		ObjectMapper mapper = new ObjectMapper();
		String id = request.getParameter("id");
		Scanner in;
		List<String> itemNames = null;
		try {
			in = new Scanner(request.getInputStream());
			String json="";
			while(in.hasNext())
				json += in.nextLine();
			itemNames = mapper.readValue(json,new TypeReference<List<String>>() {});
			UpdateItemsCommand command = new UpdateItemsCommand(itemNames,Integer.parseInt(id));
			UpdateItemsHandler itemsHandler = (UpdateItemsHandler) mediator.<UpdateItemsCommand,TrueFalseResponse> handle(command);
			boolean result = itemsHandler.handle(command).getResult();
			response.getOutputStream().println(mapper.writeValueAsString(result));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@PostMapping("/items")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, 
	ServletException {
		int id = Integer.parseInt(request.getParameter("id"));
		ObjectMapper mapper = new ObjectMapper();
		Scanner in = new Scanner(request.getInputStream());
		String json="";
		while(in.hasNext())
			json += in.nextLine();
		System.out.println(json);
		List<BoughtItem> items = mapper.readValue(json,new TypeReference<List<BoughtItem>>() {});
		
		CreateItemsCommand command = new CreateItemsCommand(items, id);
		CreateItemsHandler itemsHandler = (CreateItemsHandler) mediator.<CreateItemsCommand,TrueFalseResponse> handle(command);
		boolean result = itemsHandler.handle(command).getResult();
		response.getOutputStream().println(mapper.writeValueAsString(result));
	}
	
	@DeleteMapping("/items")
	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, 
	ServletException {
		int id = Integer.parseInt(request.getParameter("id"));
		DeleteItemsCommand command = new DeleteItemsCommand(id);
		DeleteItemsHandler itemsHandler = (DeleteItemsHandler) mediator.<DeleteItemsCommand,ItemsResponse> handle(command);
		List<BoughtItem> wastedItems = itemsHandler.handle(command).getItems();
		if(wastedItems!=null) {
			ObjectMapper mapper = new ObjectMapper();
			response.getOutputStream().println(mapper.writeValueAsString(wastedItems));
		}
		else {
			response.getOutputStream().println("{}");}
	}
}
