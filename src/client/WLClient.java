package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Flushable;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import model.BoughtItem;
import model.ShopItem;

public class WLClient {
	private static MainGUI mainGUI;
	private File weeklyReports = new File("WebContent/resources/weeklyReports.txt");
	private File monthlyReports = new File("WebContent/resources/monthlyReports.txt");
	private File donationPlaces = new File("WebContent/resources/places.txt");
	private static int id;

	
	private void updateReports(List<BoughtItem> wastedItems) {
		Report report = AbstractReportFactory.getFactory("weekly").getReport();
		String weeklyReport = report.createFile(wastedItems,weeklyReports.getAbsolutePath());
		report = AbstractReportFactory.getFactory("monthly").getReport();
		String monthlyReport = report.createFile(wastedItems,monthlyReports.getAbsolutePath());
		
		//reading the reports and sending them to the GUI
		try {
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(weeklyReports.getAbsolutePath()));
			BufferedWriter bw2 = new BufferedWriter(new FileWriter(monthlyReports.getAbsolutePath()));
			bw1.append(weeklyReport);
			bw2.append(monthlyReport);
			bw1.close();
			bw2.close();
			
			mainGUI.initReports(weeklyReport,monthlyReport);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void computeRates(List<BoughtItem> allItems) {
		int idealRate = 0, userRate = 0,  userSum = 0, userN = 0;
		long diffDays = 1;
		Date currentDate, lastDate;

		//calculating the burndown rates
			Collections.sort(allItems, new Comparator<BoughtItem>() {
				@Override
				public int compare(BoughtItem i1, BoughtItem i2) {
					if(i1.getConsumptionDate() == null)
						return -1;
					if(i2.getConsumptionDate() == null)
						return 1;
					if((i1.getConsumptionDate() == null) && (i2.getConsumptionDate() == null))
						return i1.getPurchaseDate().compareTo(i2.getPurchaseDate());
					return i1.getConsumptionDate().compareTo(i2.getConsumptionDate());
				}
			});
			lastDate = new Date(0,0,0);
			for(BoughtItem item : allItems) {
				if(item.getConsumptionDate() == null) {
					diffDays = item.getExpirationDate().getTime() - new Date().getTime();
					diffDays /= 1000*60*60*24;
					if(diffDays == 0)
						diffDays = 1;
					idealRate += (int) (item.getQuantity() *item.getCalories() / diffDays);
					}
				else {
					currentDate = item.getConsumptionDate();
					if(currentDate.getDay() != lastDate.getDay())
						userN ++;
					diffDays = item.getConsumptionDate().getTime() - item.getPurchaseDate().getTime();
					diffDays /= 1000*60*60*24; 
					if(diffDays == 0)
						diffDays = 1;
					userSum += item.getQuantity() * item.getCalories() / diffDays;
					lastDate = currentDate;
					}
			}
			
			if(userN == 0)
				userN = 1;
			userRate = userSum / userN;
			mainGUI.setRates(idealRate, userRate);
	}
	
	private static List<ShopItem> readGroceries() {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8081/sd-assignment3/query/groceries").openConnection();
			connection.setRequestMethod("GET");
			int responseCode = connection.getResponseCode();
			if(responseCode == 200) {
				String json = "";
				Scanner scanner = new Scanner(connection.getInputStream());
				while(scanner.hasNext())
					json = scanner.nextLine();
				scanner.close();
				System.out.println("READGROCERIES:"+json);
				ObjectMapper mapper = new ObjectMapper();
				List<ShopItem> groceries = mapper.readValue(json, new TypeReference<List<ShopItem>>(){});
				return groceries;
			}
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static List<BoughtItem> initWastedItems() {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8081/sd-assignment3/command/items?id="+id).openConnection();
			connection.setRequestMethod("DELETE");
			int responseCode = connection.getResponseCode();
			if(responseCode == 200) {
				String json = "";
				Scanner scanner = new Scanner(connection.getInputStream());
				while(scanner.hasNextLine())
					json += scanner.nextLine();
				//TODO: read wasted items and update reports with them
				System.out.println("INITWASTED:"+json);
				scanner.close();
				ObjectMapper mapper = new ObjectMapper();
				List<BoughtItem> wastedItems = mapper.readValue(json, new TypeReference<List<BoughtItem>>(){});
				return wastedItems;
			}
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static List<BoughtItem> readAllItems() {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8081/sd-assignment3/query/items?id="+id).openConnection();
			connection.setRequestMethod("GET");
			int responseCode = connection.getResponseCode();
			if(responseCode == 200) {
				String json = "";
				Scanner scanner = new Scanner(connection.getInputStream());
				while(scanner.hasNextLine()) {
					json += scanner.nextLine();
					json += "\n";
				}
				System.out.println("READALL:"+json);
				scanner.close();
				ObjectMapper mapper = new ObjectMapper();
				List<BoughtItem> items = mapper.readValue(json, new TypeReference<List<BoughtItem>>(){});
				return items;
			}
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
}
	
	private static List<BoughtItem> readUnusedItems() {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8081/sd-assignment3/query/unuseditems?id="+id).openConnection();
			connection.setRequestMethod("GET");
			int responseCode = connection.getResponseCode();
			if(responseCode == 200) {
				String json = "";
				Scanner scanner = new Scanner(connection.getInputStream());
				while(scanner.hasNextLine()) {
					json += scanner.nextLine();
					json += "\n";
				}
				//TODO: read unused items and send them to GUI
				System.out.println("READUNUSED:"+json);
				scanner.close();
				ObjectMapper mapper = new ObjectMapper();
				List<BoughtItem> items = mapper.readValue(json, new TypeReference<List<BoughtItem>>(){});
				return items;
			}
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void createItems(List<BoughtItem> items) {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8081/sd-assignment3/command/items?id="+id).openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
	        connection.setRequestProperty("Content-Type", "application/json; utf-8");
	        
			String requestData;
			OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
			ObjectMapper mapper = new ObjectMapper();
			requestData = mapper.writeValueAsString(items);
			wr.write(requestData);
			wr.flush();
			connection.getResponseCode();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			mainGUI.initItems(readUnusedItems());
			computeRates(readAllItems());
		}
	}
	
	public void consumeItems(List<String> removedItems) {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8081/sd-assignment3/command/items?id="+id).openConnection();
			connection.setRequestMethod("PUT");
			connection.setDoOutput(true);
	        connection.setRequestProperty("Content-Type", "application/json; utf-8");
	        
			String requestData;
			OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
			ObjectMapper mapper = new ObjectMapper();
			requestData = mapper.writeValueAsString(removedItems);
			wr.write(requestData);
			wr.flush();
			connection.getResponseCode();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			mainGUI.initItems(readUnusedItems());
			computeRates(readAllItems());
		}
	}
	
	public void setWeeklyReports(String weeklyReports) {
		this.weeklyReports = new File(weeklyReports);
	}
	public void setMonthlyReports(String monthlyReports) {
		this.monthlyReports = new File(monthlyReports);
	}
	public void setDonationPlaces(String donationPlaces) {
		this.donationPlaces = new File(donationPlaces);
	}
	
	public void login(String username, String password) {
		String response = "";
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8081/sd-assignment3/command/user?user="+username+"&pass="+password+"&type=1").openConnection();
			connection.setRequestMethod("PUT");
			int responseCode = connection.getResponseCode();
			if(responseCode == 200){
				Scanner scanner = new Scanner(connection.getInputStream());
				while(scanner.hasNext()) {
					response+= scanner.nextLine();
					response+= "\n";
				}
				scanner.close();
				connection.getInputStream().close();
				connection.disconnect();
			}
			else System.out.println("LOGIN FAILED!");
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		if(!response.contains("0")) {
			response = response.trim();
			id = Integer.parseInt(response);
			mainGUI = new MainGUI(this,username,password);
			mainGUI.initGroceries(readGroceries());
			updateReports(initWastedItems());
			computeRates(readAllItems());
			mainGUI.initItems(readUnusedItems());
			}
		else {
			Alert failedLogin = new Alert(AlertType.ERROR);
			failedLogin.setHeaderText("Login failed!");
			failedLogin.showAndWait();
		}
}
	
	public static void logout(String username,String password) {
		HttpURLConnection connection;
		try {
			connection = (HttpURLConnection) new URL("http://localhost:8081/sd-assignment3/command/user?user="+username+"&pass="+password+"&type=0").openConnection();
			connection.setRequestMethod("PUT");
			int responseCode = connection.getResponseCode();
			if(responseCode == 200){
				String response = "";
				Scanner scanner = new Scanner(connection.getInputStream());
				while(scanner.hasNextLine()){
					response += scanner.nextLine();
					response += "\n";
				}
				scanner.close();
				System.out.println("LOGOUT:"+response);
			}
		mainGUI.close();
		}
		catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public List<String> getDonationPlacesContents(){
		List<String> places = new ArrayList<String>();
		try {
			Scanner fin = new Scanner(donationPlaces);
			while(fin.hasNextLine())
				places.add(fin.nextLine());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return places;
	}
	
}
