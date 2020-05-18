package server;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import client.Main;
import model.BoughtItem;
import model.ShopItem;
import javafx.application.*;


@RestController
@RequestMapping("/query")
public class QueryController{
	
	private static final long serialVersionUID = 1L;

	@Autowired
	private WLMediator mediator;
	
	@GetMapping("/unuseditems")
	public void getUnused(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{ 
		String id = request.getParameter("id");
		ReadItemsQuery query = new ReadItemsQuery("unused",Integer.parseInt(id));
		ReadItemsHandler itemsHandler = (ReadItemsHandler) mediator.<ReadItemsQuery,ItemsResponse> handle(query);
		List<BoughtItem> items = itemsHandler.handle(query).getItems();
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(items!=null)
				response.getOutputStream().println(mapper.writeValueAsString(items));
			else response.getOutputStream().println("{}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/items")
	public void getItems(HttpServletRequest request, HttpServletResponse response) {
		String id;
		id = request.getParameter("id");
		ReadItemsQuery query = new ReadItemsQuery("all",Integer.parseInt(id));
		ReadItemsHandler itemsHandler = (ReadItemsHandler) mediator.<ReadItemsQuery,ItemsResponse> handle(query);
		List<BoughtItem> items = itemsHandler.handle(query).getItems();
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(items!=null)
				response.getOutputStream().println(mapper.writeValueAsString(items));
			else response.getOutputStream().println("{}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@GetMapping("/groceries")
	public void getUser(HttpServletRequest request, HttpServletResponse response) {
		ReadGroceriesQuery query = new ReadGroceriesQuery();
		ReadGroceriesHandler groceriesHandler = (ReadGroceriesHandler) mediator.<ReadGroceriesQuery,GroceriesResponse> handle(query);
		List<ShopItem> groceries = groceriesHandler.handle(query).getGroceries();
		ObjectMapper mapper = new ObjectMapper();
		try {
			if(groceries!=null)
				response.getOutputStream().println(mapper.writeValueAsString(groceries));
			else response.getOutputStream().println("{}");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
