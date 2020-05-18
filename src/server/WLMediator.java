package server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import client.Main;

@Component
public class WLMediator {
	
	private Map<Class <? extends IRequest>, Class<? extends IHandler<? extends IRequest, ? extends IResponse>>> handlerMap;
	
	@Autowired
	private UpdateUserHandler userHandler;
	
	@Autowired
	private ReadGroceriesHandler groceriesHandler;
	
	@Autowired
	private CreateItemsHandler createHandler;
	
	@Autowired
	private ReadItemsHandler readHandler;
	
	@Autowired
	private UpdateItemsHandler updateHandler;
	
	@Autowired
	private DeleteItemsHandler deleteHandler;
	
	public WLMediator() {
		handlerMap = new HashMap<>();
		handlerMap.put(UpdateUserCommand.class, UpdateUserHandler.class);
		handlerMap.put(ReadGroceriesQuery.class, ReadGroceriesHandler.class);
		handlerMap.put(CreateItemsCommand.class, CreateItemsHandler.class);
		handlerMap.put(ReadItemsQuery.class, ReadItemsHandler.class);
		handlerMap.put(UpdateItemsCommand.class, UpdateItemsHandler.class);
		handlerMap.put(DeleteItemsCommand.class, DeleteItemsHandler.class);
	}
	
	public <T extends IRequest, R extends IResponse> IHandler<T,R> handle(T command){
		Class<? extends IHandler<? extends IRequest, ? extends IResponse>> handlerType = handlerMap.get(command.getClass());
		if(handlerType == UpdateUserHandler.class) 
			return (IHandler<T, R>) userHandler;
		if(handlerType == ReadGroceriesHandler.class)
			return (IHandler<T,R>) groceriesHandler;
		if(handlerType == CreateItemsHandler.class)
			return (IHandler<T,R>) createHandler;
		if(handlerType == ReadItemsHandler.class)
			return (IHandler<T,R>) readHandler;
		if(handlerType == UpdateItemsHandler.class)
			return (IHandler<T,R>) updateHandler;
		return (IHandler<T,R>) deleteHandler;
	}
	
}
