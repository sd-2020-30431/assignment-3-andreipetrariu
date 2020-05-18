package server;

public interface IHandler<TRequest, TResponse> {
	TResponse handle(TRequest q);
}
